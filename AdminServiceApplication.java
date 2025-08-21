// Jenkinsfile — Complete ADD API flow (Token → Payload → Call → Artifacts)
// Requires: Pipeline Utility Steps plugin

import groovy.json.JsonOutput

pipeline {
  agent any
  options {
    timestamps()
    ansiColor('xterm')
    disableConcurrentBuilds()
  }

  parameters {
    choice(name: 'ENV', choices: ['dev','qa','prod'], description: 'Target environment')
    text(name: 'ADD_JSON', defaultValue: '', description: 'Optional: full JSON body for ADD API. Leave empty to use field parameters.')
    string(name: 'finId',            defaultValue: '1235', description: 'Example: 1235')
    string(name: 'platform',         defaultValue: 'GKE',  description: 'Example: GKE')
    string(name: 'rbnf',             defaultValue: 'COO IT', description: 'Example: COO IT')
    string(name: 'appReferenceId',   defaultValue: 'HAP-COO-00095', description: 'Example: HAP-COO-00095')
    string(name: 'namespace',        defaultValue: 'hap-hccto-116', description: 'Example: hap-hccto-116')
    string(name: 'cluster',          defaultValue: 'Cluster', description: 'Example: Cluster')
    string(name: 'environment',      defaultValue: 'dev', description: 'Example: dev')
    string(name: 'project',          defaultValue: 'testProject1', description: 'Example: testProject1')
    string(name: 'region',           defaultValue: 'UK', description: 'Example: UK')
    string(name: 'proxy',            defaultValue: '12345', description: 'Example: 12345')
    string(name: 'deployUtilName',   defaultValue: 'deploy1237', description: 'Example: deploy1237')
    string(name: 'deployUrl',        defaultValue: 'https://anypoint.mulesoft.com', description: 'Example URL')
    string(name: 'ecsNode',          defaultValue: 'node', description: 'Example: node')

    choice(name: 'AUTH_MODE', choices: ['oauth2_form','oauth2_json','use_static_token'],
           description: 'How to get Authorization token')
    booleanParam(name: 'INCLUDE_COOKIE', defaultValue: false,
                 description: 'Also send Cookie: JSESSIONID=… (if your server needs it)')
  }

  environment {
    BASE_DEV  = 'http://localhost:8080'
    BASE_QA   = 'https://qa.your-domain.com'
    BASE_PROD = 'https://api.your-domain.com'

    TOKEN_PATH = '/oauth2/token'
    ADD_PATH   = '/cer/add/shp/data'
  }

  stages {

    stage('Resolve Config') {
      steps {
        script {
          def base = [
            dev : env.BASE_DEV,
            qa  : env.BASE_QA,
            prod: env.BASE_PROD
          ][params.ENV]
          if (!base) error "Unknown ENV '${params.ENV}'"

          env.API_BASE = base
          env.AUTH_URL = "${env.API_BASE}${env.TOKEN_PATH}"
          env.ADD_URL  = "${env.API_BASE}${env.ADD_PATH}"

          echo "ENV         : ${params.ENV}"
          echo "API_BASE    : ${env.API_BASE}"
          echo "AUTH_URL    : ${env.AUTH_URL}"
          echo "ADD_URL     : ${env.ADD_URL}"
          echo "AUTH_MODE   : ${params.AUTH_MODE}"
        }
      }
    }

    stage('Generate/Load Token') {
      when { not { expression { params.AUTH_MODE == 'use_static_token' } } }
      steps {
        script {
          withCredentials([usernamePassword(
              credentialsId: 'SNOW_CREDENTIAL_UAT',
              usernameVariable: 'USERNAME',
              passwordVariable: 'PASSWORD'
          )]) {
            String tokenResp
            if (params.AUTH_MODE == 'oauth2_form') {
              tokenResp = sh(
                script: """#!/usr/bin/env bash
                  set -e
                  curl -sS --fail-with-body -X POST "${AUTH_URL}" \\
                    -H 'Content-Type: application/x-www-form-urlencoded' \\
                    --data "grant_type=client_credentials&client_id=${USERNAME}&client_secret=${PASSWORD}"
                """,
                returnStdout: true
              ).trim()
            } else {
              tokenResp = sh(
                script: """#!/usr/bin/env bash
                  set -e
                  curl -sS --fail-with-body -X POST "${AUTH_URL}" \\
                    -H 'Content-Type: application/json' \\
                    -d '{"grant_type":"client_credentials","client_id":"${USERNAME}","client_secret":"${PASSWORD}"}'
                """,
                returnStdout: true
              ).trim()
            }

            writeFile file: 'token.json', text: tokenResp
            def tokenJson = readJSON file: 'token.json'
            env.BEARER_TOKEN = [
              tokenJson?.access_token,
              tokenJson?.token,
              tokenJson?.id_token
            ].find { it } as String

            if (!env.BEARER_TOKEN) {
              error "Token not found in response. See token.json"
            }
            echo 'Token acquired using SNOW_CREDENTIAL_UAT.'
          }
        }
      }
    }

    stage('Load Static Token') {
      when { expression { params.AUTH_MODE == 'use_static_token' } }
      steps {
        withCredentials([string(credentialsId: 'api_static_token', variable: 'STATIC_TOKEN')]) {
          script {
            env.BEARER_TOKEN = STATIC_TOKEN
            echo 'Using static token from credentials.'
          }
        }
      }
    }

    stage('Build ADD Payload') {
      steps {
        script {
          if (params.ADD_JSON?.trim()) {
            writeFile file: 'add_payload.json', text: params.ADD_JSON.trim()
          } else {
            def payload = [
              finId          : params.finId?.trim(),
              platform       : params.platform?.trim(),
              rbnf           : params.rbnf?.trim(),
              appReferenceId : params.appReferenceId?.trim(),
              namespace      : params.namespace?.trim(),
              cluster        : params.cluster?.trim(),
              environment    : params.environment?.trim(),
              project        : params.project?.trim(),
              region         : params.region?.trim(),
              proxy          : params.proxy?.trim(),
              deployUtilName : params.deployUtilName?.trim(),
              deployUrl      : params.deployUrl?.trim(),
              ecsNode        : params.ecsNode?.trim()
            ].findAll { k,v -> v != null && v.toString().trim() }

            ['finId','platform','namespace','environment','project','region'].each { key ->
              if (!payload.containsKey(key)) error "Missing required field: ${key}"
            }

            writeFile file: 'add_payload.json',
                     text: JsonOutput.prettyPrint(JsonOutput.toJson(payload))
          }
          echo 'add_payload.json prepared.'
          sh 'cat add_payload.json'
        }
      }
    }

    stage('Call ADD API') {
      steps {
        script {
          def cookieHeader = ''
          if (params.INCLUDE_COOKIE) {
            withCredentials([string(credentialsId: 'api_session_cookie', variable: 'JSC')]) {
              cookieHeader = "-H 'Cookie: JSESSIONID=${JSC}'"
            }
          }

          int attempts = 3
          int delaySec = 5
          String httpCode = ''
          for (int i=1; i<=attempts; i++) {
            try {
              httpCode = sh(
                script: """#!/usr/bin/env bash
                  set -e
                  curl -sS -o add_response.json -w "%{http_code}" \\
                    -X POST "${ADD_URL}" \\
                    -H "Authorization: Bearer ${BEARER_TOKEN}" \\
                    -H 'Content-Type: application/json' \\
                    ${cookieHeader} \\
                    --data @add_payload.json
                """,
                returnStdout: true
              ).trim()
              if (['200','201','202'].contains(httpCode)) break
              echo "Attempt #${i} returned HTTP ${httpCode}"
              if (i < attempts) sleep time: delaySec, unit: 'SECONDS'
            } catch (err) {
              echo "Attempt #${i} threw error: ${err}"
              if (i < attempts) sleep time: delaySec, unit: 'SECONDS'
            }
          }

          echo "ADD API HTTP ${httpCode}"
          def body = readFile('add_response.json')
          if (!(httpCode in ['200','201','202'])) {
            error "ADD API failed (HTTP ${httpCode}). Body:\n${body}"
          }

          try {
            def j = readJSON text: body
            def createdId = (j.id ?: j.data?.id ?: j.result?.id ?: '').toString()
            if (createdId) echo "ADD success. Created ID: ${createdId}"
          } catch (ignored) {
            echo "ADD success (non-JSON body)."
          }
        }
      }
    }
  }

  post {
    success {
      archiveArtifacts artifacts: 'token.json,add_payload.json,add_response.json',
                       fingerprint: true, allowEmptyArchive: true
      echo 'Artifacts archived.'
    }
    failure {
      archiveArtifacts artifacts: 'token.json,add_payload.json,add_response.json',
                       allowEmptyArchive: true
      echo 'Artifacts archived for debugging.'
    }
  }
		}
