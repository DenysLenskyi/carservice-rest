Run docker DB container:

docker run --name=carservice_db -d -e POSTGRES_DB=carservice_db -e POSTGRES_USER=lenskyi -e POSTGRES_PASSWORD=lenskyi -p 5432:5432 postgres:15-alpine