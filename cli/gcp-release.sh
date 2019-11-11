#!/bin/zsh

gcloud config set project $PROJECT_ID
gcloud config set compute/zone us-central1

echo ':: ========================'
echo ':: CREATING CLUSTER'
echo ':: ========================'
#gcloud container clusters create api-camp-data --num-nodes=1
#gcloud compute instances list

echo ':: ========================'
echo ':: DEPLOYING LATEST'
echo ':: ========================'

kubectl create deployment api-camp-data --image=gcr.io/${PROJECT_ID}/data-embedded-micro:0.1.0
kubectl get pods