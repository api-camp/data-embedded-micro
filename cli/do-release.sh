#!/bin/zsh

# https://blog.machinebox.io/deploy-machine-box-in-digital-ocean-385265fbeafd

SERVER_NAME="api-camp-data-1"

# Create
# docker-machine create --digitalocean-size "s-1vcpu-2gb" --driver digitalocean --digitalocean-access-token $DO_PERSONAL_ACCESS_TOKEN $SERVER_NAME
# docker-machine create --digitalocean-size "s-1vcpu-2gb" --driver digitalocean --digitalocean-access-token $DO_PERSONAL_ACCESS_TOKEN api-camp-data-1

docker-machine ls

eval $(docker-machine env $SERVER_NAME)

docker run -d -p 80:8080 de314/data-embedded-micro

eval $(docker-machine env -u)

docker-machine ip $SERVER_NAME