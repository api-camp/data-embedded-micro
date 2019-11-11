#!/bin/zsh

docker-machine create --digitalocean-size "s-1vcpu-2gb" --driver digitalocean --digitalocean-access-token DO_PERSONAL_ACCESS_TOKEN api-camp-data-001

docker-machine ls
