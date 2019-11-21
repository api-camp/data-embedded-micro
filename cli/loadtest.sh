#!/bin/zsh

# https://github.com/rakyll/hey

die () {
    echo >&2 "$@"
    exit 1
}

[ "$#" -eq 2 ] || die "2 arguments required, $# provided. {simulationId, conccurencyLevel}"
echo "$2" | grep -E -q '^([1-9]|[1-9][0-9]|[1-2][0-9]{2})$' || die "Integer < 300 required for argument 2, '$2' provided"

SIM_ID=$1
C=$2
NAMESPACE=__ac_load2

function load_test_read {
  BASE_URL=$1
  CONCURRENCY=$2
  DURATION=$3
  echo "Simulation $SIM_ID => Read..."
  echo "   > hey -c $CONCURRENCY -z $DURATION -disable-keepalive $BASE_URL/api/v1/data/$NAMESPACE"
  hey -o csv -c "$CONCURRENCY" -z $DURATION -disable-keepalive "$BASE_URL/api/v1/data/$NAMESPACE"
}

function load_test_write {
  BASE_URL=$1
  CONCURRENCY=$2
  DURATION=$3
  echo "Simulation $SIM_ID => Write..."
  echo "   > hey -c $CONCURRENCY -z $DURATION -disable-keepalive -m POST -d {'loadTest':true,'simId':'$SIM_ID'} -T application/json $BASE_URL/api/v1/data/$NAMESPACE"
  hey -o csv -c "$CONCURRENCY" -z $DURATION -disable-keepalive -m "POST" -d "{\"loadTest\":true,\"simId\":\"$SIM_ID\"}" -T "application/json" "$BASE_URL/api/v1/data/$NAMESPACE"
}

SERVICE_URL="http://localhost:8080"
#SERVICE_URL="http://159.203.100.90"

load_test_write $SERVICE_URL $C "10s"
load_test_read $SERVICE_URL $C "10s"