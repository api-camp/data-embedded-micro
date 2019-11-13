#!/bin/zsh

# https://github.com/rakyll/hey

function load_test_read {
  BASE_URL=$1
  CONCURRENCY=$2
  hey -c "$CONCURRENCY" -z 10s "$BASE_URL/api/v1/data/de314"
}

function load_test_write {
  BASE_URL=$1
  CONCURRENCY=$2
  hey -c "$CONCURRENCY" -z 10s -m "POST" -d '{"loadTest":true}' -T "application/json" "$BASE_URL/api/v1/data/de314"
}

#SERVICE_URL="http://localhost:8080"
SERVICE_URL="http://159.203.100.90"
C=100

load_test_write $SERVICE_URL $C
load_test_read $SERVICE_URL $C