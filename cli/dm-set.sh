#!/bin/zsh

docker-machine env api-camp-data-1
echo 'Running...'
eval $(docker-machine env api-camp-data-1)
echo 'Done...'
