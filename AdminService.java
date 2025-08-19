// Jenkinsfile — ADD API flow (token → payload → call)
// Put this in your pipeline repo root as "Jenkinsfile"

@Library('pipeline-utility-steps') _  // if you have shared libs; safe to keep or remove

import groovy.json.JsonOutput

pipeline {
  agent any
  options {
    timestamps()
    ansiColor('xterm')
  }

  parameters {
    choice(name: 'ENV', choices: ['dev', 'qa', 'prod'], description: 'Target environment')
    // Option 1: paste full JSON payload to send to ADD API (takes priority if non-empty)
    text(name: 'ADD_JSON', defaultValue: '', description: 'Optional: full JSON body for ADD API (if provided, other ADD_* fields are ignored)')
    // Option 2: provide fields (used only when ADD_JSON is empty). Change to match your API:
    string(name: 'ADD_ID', defaultValue: '', description: 'Example field')
    string(name: 'ADD_NAME', defaultValue: '', description: 'Example field')
    string(name: 'ADD_EMAIL', defaultValue: '', description: 'Example field')
    string(name: 'ADD_AMOUNT', defaultValue: '', description: 'Example numeric field')
  }

  environment {
    // You can move this to a separate config file if you prefer
    // Adjust paths to match your real API
    CONFIG_DEV  = 'https://dev.example.com'
    CONFIG_QA   = 'https://qa.example.com'
    CONFIG_PROD = 'https://api.example.com'
    TOKEN_PATH  = '/oauth2/token'   // token endpoint path
    ADD_PATH    = '/cer/add'        // ADD endpoint path
  }

  stages {

    stage('Resolve Config') {
      steps {
        script {
          def base = [
            dev : env.CONFIG_DEV,
            qa  : env.CONFIG_QA,
            prod: env.CONFIG_PROD
          ][params.ENV]

          if (!base) {
            error "Unknown ENV '${params.ENV}'."
          }
          env.API_BASE   = base
          env.AUTH_URL   = "${env.API_BASE}${env.TOKEN_PATH}"
          env.ADD_URL    = "${env.API_BASE}${env.ADD_PATH}"
          echo "ENV: ${params.ENV}"
          echo "AUTH_URL: ${env.AUTH_URL}"
          echo "ADD_URL: ${env.ADD_URL}"
        }
      }
    }

    stage('Validate Input') {
      steps {
        script {
          if (!params.ADD_JSON?.trim()) {
            // Enforce minimum required fields for your ADD API
            if (!params.ADD_ID?.trim() || !params.ADD_NAME?.trim()) {
              error "Provide either ADD_JSON or minimally ADD_ID and ADD_NAME."
            }
          }
        }
      }
    }

    stage('Generate Token (service account)') {
      steps {
        withCredentials([usernamePassword(credentialsId: 'svc_api_client', usernameVariable: 'CLIENT_ID', passwordVariable: 'CLIENT_SECRET')]) {
          script {
            // Default: client_credentials (x-www-form-urlencoded). Change if your auth expects JSON.
            def tokenResp = sh(
              script: """#!/usr/bin/env bash
                set -e
                curl -sS --fail -X POST "${AUTH_URL}" \\
                  -H 'Content-Type: application/x-www-form-urlencoded' \\
                  -d "grant_type=client_credentials&client_id=${CLIENT_ID}&client_secret=${CLIENT_SECRET}"
              """,
              returnStdout: true
            ).trim()

            writeFile file: 'token.json', text: tokenResp
            def tokenJson = readJSON file: 'token.json'
            env.BEARER_TOKEN = (tokenJson.access_token ?: tokenJson.token ?: '').toString()
            if (!env.BEARER_TOKEN) {
              error "Token not found in token response.\nResponse: ${tokenResp}"
            }
            echo 'Token acquired.'
          }
        }
      }
    }

    stage('Build ADD Payload') {
      steps {
        script {
          if (params.ADD_JSON?.trim()) {
            // Use pasted JSON as-is
            writeFile file: 'add_payload.json', text: params.ADD_JSON.trim()
          } else {
            // Build JSON from individual fields (edit keys to match your API)
            def payloadMap = [
              id    : params.ADD_ID?.trim(),
              name  : params.ADD_NAME?.trim(),
              email : params.ADD_EMAIL?.trim(),
              amount: params.ADD_AMOUNT?.trim() ? new BigDecimal(params.ADD_AMOUNT.trim()) : null
            ].findAll { k, v -> v != null && v.toString().trim() } // remove null/empty

            def payload = JsonOutput.prettyPrint(JsonOutput.toJson(payloadMap))
            writeFile file: 'add_payload.json', text: payload
          }
          echo "add_payload.json prepared."
        }
      }
    }

    stage('Call ADD API') {
      steps {
        script {
          // Capture HTTP code & body separately
          def httpCode = sh(
            script: """#!/usr/bin/env bash
              set -e
              curl -sS -o add_response.json -w "%{http_code}" \\
                -X POST "${ADD_URL}" \\
                -H "Authorization: Bearer ${BEARER_TOKEN}" \\
                -H "Content-Type: application/json" \\
                --data @add_payload.json
            """,
            returnStdout: true
          ).trim()

          echo "ADD API HTTP ${httpCode}"
          def body = readFile('add_response.json')

          if (!(httpCode in ['200','201','202'])) {
            error "ADD API failed (HTTP ${httpCode}). Body:\n${body}"
          }

          // Optional: parse to extract created ID
          try {
            def j = readJSON text: body
            def createdId = (j.id ?: j.data?.id ?: j.result?.id ?: '').toString()
            if (createdId) {
              echo "ADD success. Created ID: ${createdId}"
            } else {
              echo "ADD success. (No 'id' field found; see add_response.json)"
            }
          } catch (ignored) {
            echo "ADD success. (Non-JSON response; see add_response.json)"
          }
        }
      }
    }
  }

  post {
    success {
      archiveArtifacts artifacts: 'token.json,add_payload.json,add_response.json', fingerprint: true, allowEmptyArchive: true
      echo 'Artifacts archived.'
    }
    failure {
      archiveArtifacts artifacts: 'token.json,add_payload.json,add_response.json', allowEmptyArchive: true
      echo 'Artifacts archived for debugging.'
    }
    always {
      // Optional cleanup
      // cleanWs deleteDirs: true, notFailBuild: true
    }
  }
      }
