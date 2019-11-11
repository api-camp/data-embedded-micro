#!/bin/zsh

export PROJECT_ID="api-camp"
echo $PROJECT_ID
docker build -t gcr.io/${PROJECT_ID}/data-embedded-micro:0.1.0 .
gcloud auth configure-docker
docker push gcr.io/${PROJECT_ID}/data-embedded-micro:0.1.0
