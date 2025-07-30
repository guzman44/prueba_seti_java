# Getting Started


# Proyecto Seti Franquicia

Este proyecto implementa una API completamente reactiva utilizando **Spring WebFlux**, con arquitectura **hexagonal / Clean Architecture** basada en el [Scaffold de Bancolombia](https://bancolombia.github.io/scaffold-clean-architecture/docs/intro/).

---


## 0. Tecnologías

- Java 11
- Spring Boot 2.7.1
- Spring WebFlux
- Spring Data R2DBC
- MySQL 8 con RDS de AWS
- Flyway
- Docker
- Terraform
- SLF4J / Logback
- OpenAPI (springdoc-openapi-webflux-ui 1.6.15)
- JUnit 5 / Mockito / StepVerifier

---


### 1. Arquitectura

Este proyecto ha sido desarrollado siguiendo los principios de Clean Architecture propuestos por Bancolombia, con el objetivo de lograr una estructura escalable, mantenible y desacoplada. A continuación se describen las decisiones clave de diseño:

- **Separación por capas**: la solución se divide en `domain`, `application` y `infrastructure`.
- **Flujo de dependencia**: todas las dependencias apuntan hacia el dominio. 


El proyecto sigue una estructura de carpetas alineada a Clean Architecture:

```
seti.franquicia
├── domain
│   ├── model
│   └── gateway
├── application
│   └── usecases
├── infrastructure
│   ├── entrypoints
│   │   ├── handler
│   │   └── router
│   └── drivenadapter
│   │   └── franquicia
│   │   │   └── entity
│   │   │   └── mapper
│   │   │   └──repository
│   │   └── producto
│   │   │   └── entity
│   │   │   └── mapper
│   │   │   └──repository
│   │   └── sucursal
│   │   │   └── entity
│   │   │   └── mapper
│   │   │   └──repository        
│   └── helpers
```


---


#### 2. Programación Reactiva

- Se utiliza **Spring WebFlux** y **Spring Data R2DBC** para implementar operaciones no bloqueantes, lo cual permite escalar de forma eficiente bajo alta concurrencia.
- Se aplican operadores como `map`, `flatMap`, `switchIfEmpty`, `zip`, y `merge` para manejar los flujos reactivos.
- Uso de señales reactivas (`onNext`, `onError`, `onComplete`) en los `handlers` para mayor control y trazabilidad de los flujos.
- Manejo de errores y respuesta unificada
- Logging con `SLF4J` y `logback`



---


#### 3. Migración de Base de Datos
- Se integra **Flyway** para la gestión de esquemas y migraciones versionadas.
- Se usan scripts SQL en el directorio `resources/db/migration` iniciando con `V1__init_franquicia_schema.sql`.

```java
src/main/resources/db/migration/
└── V1__init_franquicia_schema.sql
```

- Se ejecuta automáticamente al iniciar la app si el perfil correspondiente (`aws`, `local`) está configurado. 
- Si requiere agregar otro script de base de datos; el archivo nuevo debe comenzar `V2__` para continuar con la ejecucion siguiente.



---



#### 4. Configuración y Entornos
- Uso de perfiles (`local`, `aws`) mediante `application-<profile>.yml`.
- Separación clara de configuraciones según el entorno, especialmente para base de datos y puertos.



---



#### 5. Funcionalidades

1. Agregar nueva franquicia
2. Agregar nueva sucursal a una franquicia
3. Agregar nuevo producto a una sucursal
4. Eliminar producto de una sucursal
5. Modificar el stock de un producto
6. Obtener el producto con mayor stock por sucursal de una franquicia puntual
7. Actualizar el nombre de una franquicia
8. Actualizar el nombre de una sucursal
9. Actualizar el nombre de un producto.  


---


#### 6. Pruebas Unitarias
- Pruebas unitarias de Handlers con Mockito
- Uso de StepVerifier para validación de flujos reactivos
- Cobertura actual del proyecto: Superior al 80% (objetivo 80%)



##### FranquiciaHandlerTest (Pruebas Unitarias)

```java
public class FranquiciaHandlerTest {

    private FranquiciaUseCase useCase;
    private FranquiciaHandler handler;

    @BeforeEach
    void setUp() {
        useCase = mock(FranquiciaUseCase.class);
        handler = new FranquiciaHandler(useCase);
    }

    @Test
    void testCreate() {
        Franquicia franquicia = new Franquicia(1L, "Franquicia 1");
        ServerRequest request = MockServerRequest.builder().body(Mono.just(franquicia));

        when(useCase.saveFranquicia(any())).thenReturn(Mono.just(franquicia));

        Mono<ServerResponse> response = handler.create(request);

        StepVerifier.create(response)
                .expectNextMatches(res -> res.statusCode().is2xxSuccessful())
                .verifyComplete();

        verify(useCase, times(1)).saveFranquicia(any());
    }

    @Test
    void testFindAll() {
        Franquicia f = new Franquicia(1L, "Franquicia A");
        when(useCase.getAllFranquicias()).thenReturn(Flux.just(f));

        ServerRequest request = MockServerRequest.builder().build();
        Mono<ServerResponse> response = handler.findAll(request);

        StepVerifier.create(response)
                .expectNextMatches(r -> r.statusCode().is2xxSuccessful())
                .verifyComplete();

        verify(useCase, times(1)).getAllFranquicias();
    }

    @Test
    void testFindByIdFound() {
        Franquicia f = new Franquicia(1L, "Franquicia B");
        when(useCase.getFranquiciaById(1L)).thenReturn(Mono.just(f));

        ServerRequest request = MockServerRequest.builder().pathVariable("id", "1").build();
        Mono<ServerResponse> response = handler.findById(request);

        StepVerifier.create(response)
                .expectNextMatches(r -> r.statusCode().is2xxSuccessful())
                .verifyComplete();

        verify(useCase).getFranquiciaById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(useCase.getFranquiciaById(99L)).thenReturn(Mono.empty());

        ServerRequest request = MockServerRequest.builder().pathVariable("id", "99").build();
        Mono<ServerResponse> response = handler.findById(request);

        StepVerifier.create(response)
                .expectNextMatches(r -> r.statusCode().is4xxClientError())
                .verifyComplete();

        verify(useCase).getFranquiciaById(99L);
    }

    @Test
    void testUpdate() {
        Franquicia f = new Franquicia(1L, "Franquicia Actualizada");
        ServerRequest request = MockServerRequest.builder().pathVariable("id", "1").body(Mono.just(f));

        when(useCase.updateFranquicia(eq(1L), any())).thenReturn(Mono.just(f));

        Mono<ServerResponse> response = handler.update(request);

        StepVerifier.create(response)
                .expectNextMatches(r -> r.statusCode().is2xxSuccessful())
                .verifyComplete();

        verify(useCase).updateFranquicia(eq(1L), any());
    }

    @Test
    void testDelete() {
        when(useCase.deleteFranquicia(1L)).thenReturn(Mono.empty());

        ServerRequest request = MockServerRequest.builder().pathVariable("id", "1").build();
        Mono<ServerResponse> response = handler.delete(request);

        StepVerifier.create(response)
                .expectNextMatches(r -> r.statusCode().is2xxSuccessful())
                .verifyComplete();

        verify(useCase).deleteFranquicia(1L);
    }
}

```

##### ProductoHandlerTest (Pruebas Unitarias)

```java
public class ProductoHandlerTest {

	private ProductoUseCase useCase;
	private ProductoHandler handler;

	@BeforeEach
	void setup() {
		useCase = mock(ProductoUseCase.class);
		handler = new ProductoHandler(useCase);
	}

	@Test
	void testCreate() {
		Producto producto = new Producto(1L, "Producto 1", 10L, 1L);
		ServerRequest request = MockServerRequest.builder()
				.body(Mono.just(producto));

		when(useCase.saveProducto(any())).thenReturn(Mono.just(producto));

		Mono<ServerResponse> responseMono = handler.create(request);

		StepVerifier.create(responseMono).expectNextMatches(res -> res.statusCode().is2xxSuccessful()).verifyComplete();

		verify(useCase, times(1)).saveProducto(any());
	}

	@Test
	void testFindAll() {
		Producto p = new Producto(1L, "Producto A", 5L, 1L);
		when(useCase.getAllProductos()).thenReturn(Flux.just(p));

		ServerRequest request = MockServerRequest.builder().build();
		Mono<ServerResponse> response = handler.findAll(request);

		StepVerifier.create(response).expectNextMatches(r -> r.statusCode().is2xxSuccessful()).verifyComplete();
	}

	@Test
	void testFindByIdFound() {
		Producto producto = new Producto(1L, "Producto B", 10L, 1L);
		when(useCase.getProductoById(1L)).thenReturn(Mono.just(producto));

		ServerRequest request = MockServerRequest.builder().pathVariable("id", "1").build();

		Mono<ServerResponse> response = handler.findById(request);

		StepVerifier.create(response).expectNextMatches(r -> r.statusCode().is2xxSuccessful()).verifyComplete();
	}

	@Test
	void testFindByIdNotFound() {
		when(useCase.getProductoById(99L)).thenReturn(Mono.empty());

		ServerRequest request = MockServerRequest.builder().pathVariable("id", "99").build();

		Mono<ServerResponse> response = handler.findById(request);

		StepVerifier.create(response).expectNextMatches(r -> r.statusCode().is4xxClientError()).verifyComplete();
	}

	@Test
	void testUpdate() {
		Producto updated = new Producto(1L, "Producto Updated", 5L, 1L);

		ServerRequest request = MockServerRequest.builder().pathVariable("id", "1")
				.body(Mono.just(updated));

		when(useCase.updateProducto(eq(1L), any())).thenReturn(Mono.just(updated));

		Mono<ServerResponse> response = handler.update(request);

		StepVerifier.create(response).expectNextMatches(r -> r.statusCode().is2xxSuccessful()).verifyComplete();
	}

	@Test
	void testUpdateStock() {
		Producto updated = new Producto(1L, "Producto Updated", 20L, 1L);

		ServerRequest request = MockServerRequest.builder().pathVariable("id", "1")
				.body(Mono.just(updated));

		when(useCase.updateProductoStock(eq(1L), any())).thenReturn(Mono.just(updated));

		Mono<ServerResponse> response = handler.updateStock(request);

		StepVerifier.create(response).expectNextMatches(r -> r.statusCode().is2xxSuccessful()).verifyComplete();
	}

	@Test
	void testDelete() {
		when(useCase.deleteProducto(1L)).thenReturn(Mono.empty());

		ServerRequest request = MockServerRequest.builder().pathVariable("id", "1").build();

		Mono<ServerResponse> response = handler.delete(request);

		StepVerifier.create(response).expectNextMatches(r -> r.statusCode().is2xxSuccessful()).verifyComplete();
	}

	@Test
	void testObtenerProductoMayorStockPorSucursalDeFranquicia() {
		ProductoStockPorSucursal stock = new ProductoStockPorSucursal(1L, "Producto", 100, 1L, "Sucursal A");
		when(useCase.obtenerProductoMayorStockPorSucursalDeFranquicia(1L)).thenReturn(Flux.just(stock));

		ServerRequest request = MockServerRequest.builder().pathVariable("idFranquicia", "1").build();

		Mono<ServerResponse> response = handler.obtenerProductoMayorStockPorSucursalDeFranquicia(request);

		StepVerifier.create(response).expectNextMatches(r -> r.statusCode().is2xxSuccessful()).verifyComplete();
	}
}
```


##### SucursalHandlerTest (Pruebas Unitarias)

```java
public class SucursalHandlerTest {

	private SucursalUseCase useCase;
	private SucursalHandler handler;

	@BeforeEach
	void setup() {
		useCase = mock(SucursalUseCase.class);
		handler = new SucursalHandler(useCase);
	}

	@Test
	void testCreate() {
		Sucursal sucursal = new Sucursal(1L, "Sucursal 1", 1L);
		ServerRequest request = MockServerRequest.builder().body(Mono.just(sucursal));

		when(useCase.saveSucursal(any())).thenReturn(Mono.just(sucursal));

		Mono<ServerResponse> responseMono = handler.create(request);

		StepVerifier.create(responseMono).expectNextMatches(res -> res.statusCode().is2xxSuccessful()).verifyComplete();

		verify(useCase, times(1)).saveSucursal(any());
	}

	@Test
	void testFindAll() {
		Sucursal s = new Sucursal(1L, "Sucursal A", 1L);
		when(useCase.getAllSucursales()).thenReturn(Flux.just(s));

		ServerRequest request = MockServerRequest.builder().build();
		Mono<ServerResponse> response = handler.findAll(request);

		StepVerifier.create(response).expectNextMatches(r -> r.statusCode().is2xxSuccessful()).verifyComplete();
	}

	@Test
	void testFindByIdFound() {
		Sucursal sucursal = new Sucursal(2L, "Sucursal B", 2L);
		when(useCase.getSucursalById(2L)).thenReturn(Mono.just(sucursal));

		ServerRequest request = MockServerRequest.builder().pathVariable("id", "2").build();
		Mono<ServerResponse> response = handler.findById(request);

		StepVerifier.create(response).expectNextMatches(r -> r.statusCode().is2xxSuccessful()).verifyComplete();
	}

	@Test
	void testFindByIdNotFound() {
		when(useCase.getSucursalById(99L)).thenReturn(Mono.empty());

		ServerRequest request = MockServerRequest.builder().pathVariable("id", "99").build();
		Mono<ServerResponse> response = handler.findById(request);

		StepVerifier.create(response).expectNextMatches(r -> r.statusCode().is4xxClientError()).verifyComplete();
	}

	@Test
	void testUpdate() {
		Sucursal s = new Sucursal(3L, "Sucursal Actualizada", 1L);
		ServerRequest request = MockServerRequest.builder().pathVariable("id", "3").body(Mono.just(s));
		
		when(useCase.updateSucursal(eq(3L), any())).thenReturn(Mono.just(s));

		Mono<ServerResponse> response = handler.update(request);

		StepVerifier.create(response).expectNextMatches(r -> r.statusCode().is2xxSuccessful()).verifyComplete();

		verify(useCase).updateSucursal(eq(3L), any());
	}

	@Test
	void testDelete() {
		when(useCase.deleteSucursal(4L)).thenReturn(Mono.empty());

		ServerRequest request = MockServerRequest.builder().pathVariable("id", "4").build();
		Mono<ServerResponse> response = handler.delete(request);

		StepVerifier.create(response).expectNextMatches(r -> r.statusCode().is2xxSuccessful()).verifyComplete();

		verify(useCase).deleteSucursal(4L);
	}
}

```

---


#### 6. Documentación OpenAPI
- Integración con `springdoc-openapi-webflux-ui` (versión 1.6.15 compatible con Java 11).
- Se documentan todos los endpoints expuestos usando anotaciones `@RouterOperations` y `@RouterOperation`.

Disponible en:

```
http://localhost:9008/webjars/swagger-ui/index.html
```

##### API Endpoints

1. [Agregar nueva franquicia](https://www.postman.com/marko-dark/workspace/api-seti-pruebas/request/247402-909886d4-5fbd-4c08-86fe-04edd3ea72c5?action=share&source=copy-link&creator=247402&ctx=documentation)

2. [Agregar nueva sucursal a una franquicia](https://www.postman.com/marko-dark/workspace/api-seti-pruebas/request/247402-4c7f5f5d-5108-4565-81fd-8ba1bc442294?action=share&source=copy-link&creator=247402&ctx=documentation)
3. [Agregar nuevo producto a una sucursal](https://www.postman.com/marko-dark/workspace/api-seti-pruebas/request/247402-0df88685-30f7-4e63-97cf-4b385f4363ce?action=share&source=copy-link&creator=247402&ctx=documentation)
4. [Eliminar producto de una sucursal](https://www.postman.com/marko-dark/workspace/api-seti-pruebas/request/247402-e155ef65-10a9-4240-9656-158e47a15a3a?action=share&source=copy-link&creator=247402&ctx=documentation)
5. [Modificar el stock de un producto](https://www.postman.com/marko-dark/workspace/api-seti-pruebas/request/247402-eb6fc443-09ff-49ae-9f3b-4e024183cb20?action=share&source=copy-link&creator=247402&ctx=documentation)
6. [Obtener el producto con mayor stock por sucursal de una franquicia puntual](https://www.postman.com/marko-dark/workspace/api-seti-pruebas/request/247402-ce523452-0b39-44ba-a2b7-64953eb05ee6?action=share&source=copy-link&creator=247402&ctx=documentation)
7. [Actualizar el nombre de una franquicia](https://www.postman.com/marko-dark/workspace/api-seti-pruebas/request/247402-e2340d3f-0f8c-4554-ae6a-0dcc8e9281fd?action=share&source=copy-link&creator=247402&ctx=documentation)
8. [Actualizar el nombre de una sucursal](https://www.postman.com/marko-dark/workspace/api-seti-pruebas/request/247402-4694a5b5-ad82-481a-a2d0-199242477e93?action=share&source=copy-link&creator=247402&ctx=documentation)
9. [Actualizar el nombre de un producto](https://www.postman.com/marko-dark/workspace/api-seti-pruebas/request/247402-175fbe44-b2cf-4eac-8128-acceb80ecc16?action=share&source=copy-link&creator=247402&ctx=documentation)

[Colección Completa Endpoint en Postman](https://www.postman.com/marko-dark/workspace/api-seti-pruebas/collection/247402-67d5f868-a5e7-43a2-9f93-cd1cbde87503?action=share&source=copy-link&creator=247402).



---


#### 7. Despliegue
- Se usa un `Dockerfile` para empaquetar la solución y exponer el servicio en el puerto `9008`.


## 7.1 Docker

Archivo `Dockerfile` incluido:

```dockerfile
# Dockerfile
FROM eclipse-temurin:11-jre

# Crear directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el jar construido en el contenedor
COPY target/seti.franquicia-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto 9008
EXPOSE 9008

# Configuracion 
ENV SPRING_PROFILES_ACTIVE=aws

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Comandos para construir y ejecutar:

```bash
docker build -t prueba-seti-app .
docker run -e SERVER_PORT=9008 -p 9008:9008 --name seti-container prueba-seti-app
```


**Nota**: 
- Debe estar ubicado en el proyecto
- Debe usar una terminar para su ejecucion
- Debe tener instalado docker en la maquina para que el sistema reconozca las lineas de comando

---

## 7.2 Despliegue en AWS con Terraform

**Notas**: 
- Debe tener una cuenta AWS para poder tener AWS Access Key ID y el AWS Secret Access Key, e igualmente tener instalado previamente instalado AWSCLIV2.msi **(Requerido)**
- El usuario de AWS, debe crearlo en IAM y tener los politicas de permisos  AmazonRDSFullAccess, AmazonVPCFullAccess y tener deshabilitado los limite de permisos. **(Requerido)**
- Debe tener instalado previamente terraform en su maquina. por ejemplo puede instalarlo : choco install terraform -y para windowns **(Requerido)**

Terraform configura:
  - Una subnet
  - Una grupo de segurida
  - La creacion de un RDS de mysql

> Revisar el directorio `terraform/`.

```
Para su ejecucion debe ubicarse en la carpeta terraform y ejecutar los siguiente comandos:

terraform init
terraform plan
terraform apply
```


---

##  7.3 Ejecución local

1. Clonar repositorio

```bash
git clone https://github.com/guzman44/prueba_seti_java.git
cd seti.franquicia
```

2. Configurar el archivo `application-local.yml` con tu conexión a base de datos. o utilizar la de preferencia. `local` o `aws`

3. Ejecutar con:

```bash
mvn spring-boot:run "-Dspring-boot.run.profiles=local"
```


###   7.3.1 Configuración por Ambientes

####  7.3.1.1 Principal

```yaml
#application.yml
springdoc:
  api-docs:
    path: /api-docs
  swagger-ui:
    path: /swagger-ui.html
    tags-sorter: alpha

spring:
  application:
    name: seti-franquicia

  profiles:
    active: aws  # Cambiar de "local" a "aws" en despliegue en la nube cuando sea a procduccoin

  main:
    web-application-type: reactive

  jackson:
    serialization:
      INDENT_OUTPUT: true

logging:
  level:
    root: INFO
    com.co.nequi: DEBUG
    org.flywaydb: DEBUG
    
```


####  7.3.1.2 Local
```yaml
#application-local.yml
spring:
  r2dbc:
    url: r2dbc:mysql://localhost:3306/franquicias_db
    username: root
    password: 123
    
  flyway:
    enabled: true  
    locations: classpath:db/migration    
    baseline-on-migrate: true
    validate-on-migrate: true
    out-of-order: true
    
    
  datasource:
    url: jdbc:mysql://localhost:3306/franquicias_db
    username: root
    password: 123
    driver-class-name: com.mysql.cj.jdbc.Driver    
 
  server:
    port: 8080 


```


####  7.3.1.3 Aws
```yaml
#application-aws.yml

spring:
  r2dbc:
    url: r2dbc:mysql://mysql-franquicia.c5m0qsoac3uj.us-east-2.rds.amazonaws.com:3306/franquicias_db
    username: root
    password: SetiTest987

  flyway:
    enabled: true
    baseline-on-migrate: true
    validate-on-migrate: true
    locations: classpath:db/migration
    url: jdbc:mysql://mysql-franquicia.c5m0qsoac3uj.us-east-2.rds.amazonaws.com:3306/franquicias_db
    user: root
    password: SetiTest987 

  server:
    port: 9008 
    
```

---

# Autor - **Marco Tulio Guzmán Martínez**

# PERFIL PROFESIONAL

Ingeniero de sistemas, Especialista en Ingeniería de Software y estudiante de último semestre de la Maestría de Ingeniería de sistemas y computación en la Javeriana. Con competencias en Arquitectura de Software con capacidad para diseñar, desarrollar e implementar soluciones de plataformas ORACLE, JAVA, .NET con conectividad con bases de datos ORACLE, Microsoft SQL Server, PostgreSQL y MySQL, como también los conocimientos amplios en tecnologías de micro servicios en API REST. 

He desarrollado competencias orientadas al logro, con liderazgo y capacidad de dirigir grupos de trabajo interdisciplinarios que desarrollen proyectos especialmente de tecnologías de la información y las comunicaciones. 

Tengo avanzado conocimiento en modelos, métodos, metodologías, técnicas, herramientas y lenguajes de programación de diferentes generaciones usados en el desarrollo de software a nivel empresarial, en la gerencia de proyectos de tecnología y en todas las áreas de conocimientos asociadas a la Ingeniería de Software. Capacidad de adoptar, adaptar e implementar nuevas tecnologías que apoyen los negocios, con pensamiento sistémico, basado en la capacidad de Investigación y aprendizaje continuo. Caracterizado por ser una persona inquieta, con sed de conocimiento e investigación. Excelentes relaciones interpersonales, alta disponibilidad para el trabajo en equipo, capacidad de auto aprendizaje y liderazgo.


Repositorio: [github.com/guzman44/prueba_seti_java](https://github.com/guzman44/prueba_seti_java)

---
