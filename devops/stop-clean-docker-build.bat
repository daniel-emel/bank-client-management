:: This script was created to clean up the docker components of the actual build. Please use with care. Use at your own risk.
:: Using 1a9 as postfix to avoid conflicts with containers and images that already exist in Docker
docker stop client-backend-1a9 & ^
docker stop client-db-1a9 & ^
docker stop test-client-db-1a9 & ^
docker rm client-backend-1a9 & ^
docker rm client-db-1a9 & ^
docker rm test-client-db-1a9 & ^
docker image rm bank-client-management-client-backend & ^
docker image rm bank-client-management-client-db & ^
docker image rm test-client-db-1a9