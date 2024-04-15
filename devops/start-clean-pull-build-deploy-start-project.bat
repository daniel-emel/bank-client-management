:: Important: Prerequisites for a successful build
::   - Dependencies: The following dependencies must be installed on the host machine and found in the path environment variable.
::     - Java 17 (developed using OpenJDK 17.0.2)
::     - Maven 3.8.x+ (developed using Apache Maven 3.9.6 (bc0240f3c744dd6b6ec2920b3cd08dcc295161ae))
::     - Docker 25.0.x+ (developed using Docker version 25.0.3, build 4debf41)
::   - Ports:
::     - Make sure that you don't have any containers or other executables running on localhost ports 8080 and 3306, otherwise the script won't work and the build will fail.
::   - Script execution location:
::     - This script can be started strictly only from the "project-root/devops" location using Windows CMD.
::     - It contains location-specific commands. So starting it from the wrong location will not work.
::     - Location: "project-root/devops"
::   - How to run this script:
::     - Open a Windows CMD at the location ("projet-root/devops") of this file.
::     - Execute command: start-clean-pull-build-deploy-start-project.bat

:: This script was created to automate the entire development lifecycle.
:: Be careful not to break your existing Docker environment with this script. Use it at your own risk.
:: Using 1a9 as postfix to avoid conflicts with containers and images that already exist in Docker

cd .. & :: Go to project root from devops folder, important for later executed commands
docker stop client-backend-1a9 & ^
docker stop client-db-1a9 & ^
docker stop test-client-db-1a9 & ^
docker rm client-backend-1a9 & ^
docker rm client-db-1a9 & ^
docker rm test-client-db-1a9 & ^
docker image rm bank-client-management-client-backend & ^
docker image rm bank-client-management-client-db & ^
docker image rm test-client-db-1a9 & ^
docker pull openjdk:17 && ^
docker pull mariadb:latest && ^
docker build ./client-db --tag test-client-db-1a9 && ^
docker run --name test-client-db-1a9 -d -p 3306:3306 test-client-db-1a9:latest && ^
cd ./client-backend && ^
mvn clean package && ^
cd .. && ^
docker stop test-client-db-1a9 && ^
docker rm test-client-db-1a9 && ^
docker image rm test-client-db-1a9 && ^
docker-compose up -d && ^
cd ./devops & :: Go back to the starting location (project-root/devops) in the CMD for future re-runs. Other wise the CMD won't find this .bat file.
