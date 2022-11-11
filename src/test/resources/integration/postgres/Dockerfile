FROM postgres:15.1-alpine

COPY db.sql /docker-entrypoint-initdb.d/db.sql

ENV POSTGRES_USER test
ENV POSTGRES_PASSWORD test
ENV POSTGRES_DB test
ENV PGDATA=/data