#!/bin/zsh

die () {
    echo >&2 "$@"
    exit 1
}

[ "$#" -eq 1 ] || die "1 argument required, $# provided"
echo "$1" | grep -E -q '^[0-9]+\.[0-9]+\.[0-9]+$' || die "Sematic version required e.g. 1.0.0, $1 provided"

VERSION="${1}"
JAR_FILENAME="data-embedded-micro-${VERSION}.jar"

echo "Install..."
cp "build/libs/$JAR_FILENAME" app.jar
docker build -t de314/data-embedded-micro .
rm app.jar
echo "Release..."
docker tag de314/data-embedded-micro "de314/data-embedded-micro:${VERSION}"
