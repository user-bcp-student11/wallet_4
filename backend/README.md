# Wallet Backend

Base Java 21 con Quarkus 3.27.2, Maven y enfoque contract-first. El frontend no forma parte de esta entrega. No hay persistencia, autenticacion ni logica de pagos implementada todavia.

## Dependencias

- Quarkus REST Jackson: servidor HTTP sobre el stack reactivo de Quarkus.
- Quarkus REST Client Jackson: cliente no bloqueante sobre Vert.x con conexiones reutilizables.
- Hibernate Validator: validacion Jakarta Bean Validation de los modelos generados.
- SmallRye OpenAPI: documentacion de los recursos implementados y Swagger UI en desarrollo.
- SmallRye Health: liveness y readiness tecnicos.
- JUnit 5, Quarkus Test y REST Assured: pruebas unitarias, HTTP y del artefacto empaquetado.
- OpenAPI Generator 7.16.0: interfaces Jakarta REST y modelos Jackson derivados del contrato.

Las versiones de extensiones y librerias administradas se alinean mediante el BOM de Quarkus. Se fijan las versiones de Quarkus y del generador para reproducibilidad; revisar sus parches de mantenimiento antes de desplegar en produccion.

## Estructura

```text
backend/
  pom.xml
  mvnw / mvnw.cmd
  .mvn/wrapper/
  Dockerfile
  Dockerfile.dockerignore
  src/main/java/pe/wallet/
    domain/                        # Espacio para futuras reglas de negocio
    application/health/            # Servicio de aplicacion de ejemplo
    infrastructure/rest/           # Recursos que implementan interfaces generadas
    infrastructure/client/         # Integraciones HTTP salientes
  src/main/resources/
    application.properties
  src/test/java/pe/wallet/
    application/health/             # Pruebas unitarias
    contract/                      # Compatibilidad JSON del codigo generado
    infrastructure/rest/           # Pruebas HTTP y de integracion empaquetada
    infrastructure/client/         # REST Client contra un servidor HTTP local
  src/test/resources/
  target/generated-sources/openapi/src/main/java/pe/wallet/contract/
    api/                           # Interfaces generadas; no editar
    model/                         # DTO y enums generados; no editar
```

## Requisitos

- JDK 21 con `JAVA_HOME` apuntando al JDK, no a su subcarpeta `bin`.
- Maven Wrapper incluido; no requiere una instalacion global de Maven. La primera ejecucion descarga Maven 3.9.11 y las dependencias.
- Docker opcional para construir y ejecutar el contenedor.

Comprobar el JDK utilizado por Maven en PowerShell:

```powershell
cd backend
.\mvnw.cmd --version
```

El resultado debe indicar Java 21. Si `java --version` y Maven usan JDK diferentes, corregir `JAVA_HOME` antes de construir.

## Desarrollo

Desde `backend`, en PowerShell:

```powershell
.\mvnw.cmd compile quarkus:dev
```

En Linux/macOS, sustituir `.\mvnw.cmd` por `sh ./mvnw`. El paso `compile` garantiza que se generen las clases antes de iniciar el modo desarrollo.

- `http://localhost:8080/health`: recurso de ejemplo que implementa la interfaz generada `HealthApi` y devuelve el modelo generado `Health`.
- `http://localhost:8080/q/health/live`: liveness tecnico.
- `http://localhost:8080/q/health/ready`: readiness tecnico; no comprueba base de datos ni proveedores porque aun no hay dependencias operativas.
- `http://localhost:8080/q/swagger-ui`: Swagger UI en desarrollo.
- `http://localhost:8080/q/openapi`: descripcion de recursos implementados, no promesa de que todo el contrato este implementado.
- `http://localhost:8080/contracts/openapi.json`: contrato original completo, publicado sin alterar sus servidores del mock.

`storage=memory` en `/health` identifica esta base sin persistencia; no significa que existan wallets o pagos almacenados. Las operaciones de negocio todavia responden 404 al no tener recursos implementados. No apuntar la suite completa de Postman al backend esperando el comportamiento del mock.

## Contract-first

La unica fuente es [client/openapi.json](../client/openapi.json). No se mantiene una segunda copia editable en el backend.

1. Cambiar primero el contrato y validar su compatibilidad con los consumidores.
2. Regenerar modelos e interfaces:

```powershell
.\mvnw.cmd generate-sources
```

3. Implementar o ajustar los recursos en `infrastructure/rest` usando las interfaces de `pe.wallet.contract.api` y delegar los casos de uso en `application`.
4. Agregar pruebas y ejecutar `clean verify`.

La generacion se ejecuta automaticamente durante `compile`, `test`, `package` y `verify`. `target` no se versiona y nunca debe editarse manualmente. Si cambia el contrato durante una sesion de desarrollo, reiniciar con `compile quarkus:dev` para regenerar y volver a copiar el contrato estatico.

Las interfaces usan `Uni<Response>` para permitir composicion no bloqueante y devolver los codigos de respuesta previstos. Los metodos con entrada/salida bloqueante que se agreguen en el futuro requieren ejecucion apropiada fuera del event loop; usar Quarkus reactivo no convierte automaticamente una llamada JDBC o de archivos en no bloqueante.

El contrato actual tiene cuatro operaciones sin `operationId`; el generador les asigna nombres automaticamente y muestra avisos. Si se modifican esas rutas, revisar los nombres generados. `Payment.fingerprint` permite texto o arreglo: se mapea el esquema inline `Payment_fingerprint` a `Object` porque el generador no representa correctamente esa union. Dos pruebas verifican ambos formatos. Este atributo es interno del mock y no debe formar parte del dominio de negocio.

## REST Client

`WalletMockClient` es una interfaz de integracion de ejemplo escrita a mano para `GET /health`. Reutiliza el modelo `Health` generado desde el contrato y devuelve `Uni<Health>`. No se genera automaticamente un SDK cliente completo ni se llama al mock al arrancar el backend.

Para inyectarlo en un servicio usar `@RestClient WalletMockClient`. La configuracion limita conexion a 2 segundos, respuesta a 5 segundos y el pool a 20 conexiones; reutiliza conexiones y no registra cuerpos. No se agregan reintentos automaticos a operaciones financieras.

| Variable de entorno | Valor predeterminado |
| --- | --- |
| `HTTP_PORT` | `8080` |
| `WALLET_ENVIRONMENT` | `local` |
| `WALLET_MOCK_URL` | `https://bcp-wallet-mock.yefri.workers.dev/groups/4` |
| `WALLET_MOCK_LATENCY_MS` | `0` |

Las pruebas fijan la URL configurada a una direccion local no operativa y la prueba del cliente usa su propio servidor HTTP local. Ninguna prueba consulta o modifica datos del grupo compartido.

## Pruebas y empaquetado

Desde `backend`:

```powershell
.\mvnw.cmd test
.\mvnw.cmd clean verify "-DskipITs=false"
```

`test` ejecuta pruebas unitarias, de contrato, HTTP con `@QuarkusTest` y del cliente. `verify -DskipITs=false` agrega las pruebas `*IT` con `@QuarkusIntegrationTest`, arrancando el JAR empaquetado. Estas pruebas no necesitan Docker.

Ejecutar el artefacto construido:

```powershell
java -jar target/quarkus-app/quarkus-run.jar
```

El formato es fast-jar: para desplegar debe copiarse toda la carpeta `target/quarkus-app`, no solamente `quarkus-run.jar`.

## Docker

Desde la raiz del repositorio, no desde `backend`, porque el build tambien necesita el contrato en `client`:

```sh
docker build -f backend/Dockerfile -t wallet-backend:local .
docker run --rm -p 8080:8080 -e WALLET_ENVIRONMENT=docker wallet-backend:local
```

El Dockerfile compila dentro de una imagen Maven/JDK 21 y copia el fast-jar a una imagen JRE 21. La imagen final no lleva Maven ni fuentes y se ejecuta con UID/GID `10001`. El archivo Dockerfile.dockerignore restringe el contexto al POM, fuentes y contrato. Las pruebas se ejecutan antes del build mediante `verify`; el build de imagen las omite para no duplicarlas.

Docker debe validarse en una maquina con el motor instalado y activo. Esta base no incluye imagen nativa, Kubernetes, base de datos, seguridad ni despliegue productivo.