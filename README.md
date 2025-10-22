## Arquitectura hexagonal multimódulo

El proyecto sigue un enfoque DDD y se divide en tres módulos maven principales que implementan los puertos y adaptadores de la arquitectura hexagonal y un quinto modulo para ensamblar todos.:

- `pricing-domain`: encapsula el modelo de dominio y los puertos de salida (`Price` y `PriceRepository`). No depende de nadie.
- `pricing-application`: implementa los casos de uso (`GetPrice`) y DTOs que expone la capa de aplicación. Solo depende del módulo de dominio.
- `pricing-infra`: Este modulo padre agrupa los submódulos de infraestructua (rest y jpa) esto ayuda a tener las fronter más claras, reutilizables y faciles de probar.
- `pricing-boot`: ensambla los modulos anteriores y contiene el `main` para empaquetar y ejecutar el microservicio.


## Ejecución del servicio

Para ejecutar las pruebas:

```bash
mvn test
```

### Pruebas E2E con Postman

El repositorio incluye la colección `postman/pricing-e2e.postman_collection.json`.

## Documentación OpenAPI

La aplicación expone la especificacion OpenAPI del servicio:
- Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Ejecución con Docker

El repositorio incluye un `Dockerfile` multi stage.
Para construir y ejecutar la imagen:

```bash
docker build -t pricing-service .
docker run --rm -p 8080:8080 -e SPRING_DATASOURCE_USERNAME=user -e SPRING_DATASOURCE_PASSWORD=pass pricing-service
```

También se aporta un simple `docker-compose.yml` por si se quiere añadir mas servicios (kafka, redis, integración con otros microservicios, etc) 

```bash
docker compose up --build
```
o para versiones antiguas

```bash
docker-compose up --build
```

Una vez levantado el contenedor puedes invocar el endpoint en `http://localhost:8080/api/prices` con los mismos parámetros vistos anteriormente.

# Aclaraciones

- No queda claro que debe ocurre en el caso de que existíera dos o más ofertas que tuvieran rangos de fechas solapadas y con el mismo identificador de producto y de marca.
Por tanto en esos casos se devolverá el primero que se encuentre en lugar de una lista.
>  Aceptar como parámetros de entrada la fecha de consulta (o aplicación), el identificador del producto y el identificador de la marca.

- Con un servicio tan sencillo relamente no hacia falta montarlo con multi modulo pero sirve como ejemplo para reforzar las fronteras y dependencias entre aplicación, dominio e infraestructura.
Además, facilita la reutilización, el versionado independiente de los módulos, etc.

- Dockerfile la imagen del build viene con una versión más reciente reduciendo los CVEs y la imagen del runtime se actualiza a una version fija.
  Se añade usuario no root sin shell (menos privilegios).
  Siempre se puede tener más margen de mejora añadiendo healthcheck y Actuator. 
