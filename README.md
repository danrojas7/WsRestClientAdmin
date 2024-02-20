# WsRestClientAdmin
Microservicio de API Rest en el que se definen los métodos CRUD para la administración de clientes, como parte de la prueba técnica para Alianza

# Comando para construir imagen docker

docker build \
    --build-arg APP=WsRestClientAdmin-0.0.1-SNAPSHOT \
    --build-arg TZ=America/Bogota \
    --build-arg CONFIG_PATH=src/main/resources/application.properties \
    -t danrojas7/wsrestclientadmin:0.0.1 .

