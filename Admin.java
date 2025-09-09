def statusCode = sh(script: """#!/bin/bash
set +x
curl -s -w "%{http_code}" -o ${fileName} \
    -H "${contentType}" \
    -H "X-HSBC-E2E-Trust-Token: ${token}" \
    -X GET "${url}"
""", returnStdout: true).trim()
