docker build . --tag test-client-db-1a9 && ^
docker run --name test-client-db-1a9 -d -p 3306:3306 test-client-db-1a9:latest &&