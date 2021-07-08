mvn clean install
docker build -t minhaudocao/backend .
docker stop minhaudocao-backend
docker rm minhaudocao-backend
docker run -p 8080:8080 --name minhaudocao-backend minhaudocao/backend