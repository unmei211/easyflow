#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE gateway;
    CREATE DATABASE users;
    CREATE DATABASE social;
    CREATE DATABASE task;
EOSQL

# docker exec -it easyflow-postgres-1 psql -U easyflow