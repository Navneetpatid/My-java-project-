def statusCode = sh(script: """#!/bin/bash
  set -e
  curl -s -o ${fileName} -w "%{http_code}" \
    -H "${contentType}" \
    -H "X-HSBC-E2E-Trust-Token: ${token}" \
    -X POST "${url}" \
    --data @kong_request.json
""", returnStdout: true).trim()
