#!/bin/zsh

die () {
    echo >&2 "$@"
    exit 1
}

[ "$#" -eq 1 ] || die "1 argument required, $# provided"
echo "$1" | grep -E -q '^[0-9]+\.[0-9]+\.[0-9]+$' || die "Sematic version required e.g. 1.0.0, $1 provided"

VERSION="${1}"

echo "Releasing $VERSION..."
docker push "de314/data-embedded-micro:${VERSION}"
docker push "de314/data-embedded-micro:latest"
