# Alcance W1 - Registro y activación de Wallet

> Documento de alcance para la Hackathon del Día 5. Estado: propuesta para confirmar en G1. Este archivo define qué significa "terminado" para W1; el README principal conserva las instrucciones técnicas del repositorio.

## 1. Decisión de producto

### Límite explícito: una sola cuenta de demostración

Para esta hackathon, **"cuenta" significa la Wallet digital**, no una cuenta bancaria. El alcance queda limitado a:

- Un usuario sintético por recorrido de demostración.
- Un único onboarding y, si el mock lo crea, un único `wallet_id` mostrado en pantalla.
- Una sola Wallet en la demo, en PEN y con el estado y saldo devueltos por el mock.
- Ninguna acción para agregar, vincular, seleccionar o cambiar a otra cuenta bancaria o a una segunda Wallet.

El recorrido aprobado crea una Wallet de demostración con saldo inicial cero. Los escenarios `PENDING` y `REJECTED` muestran la misma cuenta en su estado correspondiente; `UNAVAILABLE` no crea cuenta.

Esta demo **no** vincula ni agrega cuentas bancarias. Una persona podría relacionar varias cuentas bancarias con una Wallet en una evolución futura, pero esa capacidad no forma parte de W1 ni es necesaria para probar el alta.

La regla es una Wallet por recorrido de demostración, no una garantía técnica de una Wallet por persona: el mock no impide altas repetidas. No iniciar un segundo onboarding dentro de la misma demo y usar un identificador ficticio `TEST-` nuevo para cada intento autorizado.

## 2. Objetivo y usuario

**Objetivo:** demostrar que una persona puede completar un onboarding guiado, recibir un resultado claro de verificación y conocer el estado de su Wallet sin depender de un proveedor real.

**Usuario:** persona que usa un smartphone y quiere terminar el registro con claridad para poder empezar a pagar más adelante.

**Historia W1:** Como usuario de smartphone que realiza transferencias diarias de bajo monto, quiero completar el onboarding mediante un flujo guiado y entender el siguiente paso, para poder empezar a pagar sin abandonar el proceso.

## 3. Alcance de la demo

### Must have

- Mostrar los pasos requeridos y el progreso actual del onboarding.
- Validar los campos obligatorios antes de llamar al API; bloquear el avance con mensajes que expliquen cómo corregirlos.
- Admitir solo personas de 18 años o más, comprobando la fecha de nacimiento en el formulario.
- Solicitar un celular peruano nacional de 9 dígitos que empiece en 9; mostrar el prefijo `+51` separado del número.
- Solicitar tipo de documento `TEST` y un sufijo ficticio obligatorio; presentar el identificador completo con prefijo `TEST-`.
- Crear una verificación con `documentType: TEST`, un documento ficticio `TEST-...` y un escenario permitido.
- Consultar la verificación y la Wallet usando los identificadores devueltos por la creación.
- Mostrar el estado que devuelve el API. No inferir ni marcar una Wallet como activa desde el frontend.
- En el recorrido principal `APPROVED`, mostrar la Wallet `ACTIVE`, su ID, moneda y saldo devueltos por el mock.
- Mostrar como máximo una tarjeta de Wallet y no ofrecer controles para agregar o elegir otras cuentas.
- Tras el registro de demostración, mostrar una pantalla de bienvenida personalizada, una sola Wallet y saldo inicial ilustrativo de `S/ 0.00`.
- Mostrar movimientos únicamente como ejemplos visuales claramente rotulados; no presentarlos como historial real ni sumarlos al saldo.
- Mostrar un error accionable si el mock responde `503`, si ocurre un error de red o si una consulta no encuentra el recurso.
- Probar el flujo con datos sintéticos y guardar evidencia reproducible.

### Should have si el flujo principal ya está estable

- Cubrir `MANUAL_REVIEW`: mostrar `PENDING`, consultar el caso de soporte y ofrecer decisión humana `ACTIVE` o `REJECTED`.
- Cubrir el resultado `REJECTED` y comprobar que no se ofrece continuar a pagos.
- Cubrir `UNAVAILABLE` y comprobar que `503 KYC_UNAVAILABLE` no crea una Wallet.
- Mostrar eventos de inicio, pasos completados y cierre en el panel de actividad del navegador.

### Fuera de alcance

- Vincular, listar, seleccionar o conciliar varias cuentas bancarias.
- Abrir varias Wallets por recorrido o usuario, cambiar de Wallet o garantizar unicidad de identidad.
- Pagos, transferencias, QR, reversos, conciliación, recargas o movimiento de fondos: corresponden a otras historias.
- KYC, OTP, biometría, identidad o conexiones bancarias reales.
- Verificar edad o teléfono con una fuente oficial, o usar estas validaciones como verificación legal de identidad.
- Guardar nombre, fecha de nacimiento, correo o teléfono en el mock; el contrato W1 no recibe esos campos.
- Usar tipos reales de documento peruano como DNI o CE; el mock solo acepta `TEST`.
- Registrar movimientos reales o afirmar que el formulario creó una cuenta: el prototipo actual no llama al API ni persiste la Wallet.
- Analítica persistente o demostrar los objetivos trimestrales de tiempo de onboarding, abandono, MAU o CAC.
- Construir microservicios o ampliar el backend fuera de lo necesario para la decisión de integración aprobada por el equipo.

## 4. Recorrido de usuario

1. La persona abre el registro y ve los pasos y el progreso.
2. Completa los campos de demostración: fecha de nacimiento de 18 años o más y celular nacional peruano de 9 dígitos que empieza en 9. No usa datos personales reales.
3. La interfaz valida los campos y exige un identificador ficticio nuevo con prefijo `TEST-`.
4. La persona selecciona el escenario de prueba aprobado para la demo.
5. La aplicación envía la solicitud de KYC al mock.
6. La aplicación conserva por separado `id` de KYC y `wallet_id`.
7. La aplicación consulta el estado de KYC y el de la Wallet; presenta lo que respondió el API.
8. Si el estado es `ACTIVE`, muestra la Wallet y un punto de entrada a W2 marcado como pendiente. Si es `PENDING`, explica que hace falta una decisión humana. Si hay rechazo o indisponibilidad, indica que no se puede continuar.

## 5. Contrato W1 del mock

**URL base del Grupo 4:** `https://bcp-wallet-mock.yefri.workers.dev/groups/4`

| Operación | Endpoint | Uso en el flujo |
|---|---|---|
| Crear KYC y Wallet | `POST /kyc/verifications` | Envía `documentType`, `document` y `scenario`; responde `201` con `id`, `wallet_id`, `scenario` y `status`. |
| Consultar KYC | `GET /kyc/verifications/{id}` | Consulta usando el ID de la verificación KYC. |
| Consultar Wallet | `GET /wallets/{id}` | Consulta usando `wallet_id`; saldo y montos están expresados en céntimos de PEN. |
| Listar casos | `GET /support/cases` | Consultar si la verificación entra en revisión manual. |
| Resolver revisión | `POST /kyc/verifications/{id}/decision` | Envía `{"decision":"ACTIVE"}` o `{"decision":"REJECTED"}` para una verificación `PENDING`. |

Escenarios de `POST /kyc/verifications`:

| Escenario | Resultado esperado |
|---|---|
| `APPROVED` | Crea una Wallet `ACTIVE` con saldo cero. |
| `MANUAL_REVIEW` | Crea una Wallet `PENDING` y un caso de soporte. |
| `REJECTED` | Crea una Wallet rechazada; no debe habilitar pagos. |
| `UNAVAILABLE` | Devuelve `503 KYC_UNAVAILABLE` y no crea Wallet. |

Errores relevantes: `400` para entradas inválidas, `404` para IDs inexistentes, `409` si se intenta cambiar una decisión final y `503` si el proveedor simulado no está disponible. El mock puede devolver también `413` o `429`; mostrarlos de forma segura, pero no es necesario provocar esos límites para la demo.

### Datos y persistencia

- El mock solo acepta `documentType: TEST` y documentos cuyo prefijo sea `TEST-`.
- Nombre, apellido, correo y teléfono pueden aparecer en la interfaz, pero no se envían al contrato W1 ni se persisten en el mock.
- La fecha de nacimiento y el celular también son validaciones locales de la demo: no se envían al mock ni prueban edad o titularidad del número.
- Las verificaciones y wallets son persistentes y compartidas por el Grupo 4. No repetir documentos ni ejecutar el reset del grupo.
- El mock no evita altas duplicadas. La interfaz no debe presentar esta demo como prueba de unicidad por persona.
- El mock no ofrece un endpoint de analítica W1. Los eventos visibles en la interfaz son temporales y desaparecen al recargar.

## 6. Criterios de aceptación

| ID | Criterio | Prioridad | Evidencia de aceptación |
|---|---|---|---|
| W1-AC1 | La interfaz presenta los pasos y el paso actual durante el onboarding. | Must | Captura o recorrido en vivo. |
| W1-AC2 | Si falta un campo o su formato es inválido, no se avanza ni se envía la solicitud; se señala cómo corregirlo. | Must | Prueba de campos vacíos y formato inválido. |
| W1-AC10 | Solo se acepta fecha de nacimiento correspondiente a una persona de 18 años o más. | Must | Probar menor de 18, cumpleaños 18 y fecha futura. |
| W1-AC11 | El celular debe tener exactamente 9 dígitos, empezar en 9 y mostrarse con prefijo `+51`. | Must | Probar número válido, longitud incorrecta y prefijo incorrecto. |
| W1-AC12 | El tipo de documento es `TEST`; el sufijo es obligatorio y la interfaz muestra el identificador con prefijo `TEST-`. | Must | Probar sufijo vacío y uno válido; verificar que el formulario no acepta tipos reales. |
| W1-AC13 | El registro válido muestra saludo personalizado, una Wallet y saldo inicial `S/ 0.00`. | Must | Completar el formulario con datos ficticios. |
| W1-AC14 | Los movimientos de ejemplo se distinguen del historial real y no cambian el saldo. | Must | Revisar etiquetas y aviso; confirmar que se identifican como ejemplos no registrados. |
| W1-AC3 | La solicitud al mock usa `documentType: TEST`, un `TEST-` único y el escenario elegido. | Must | Request/response sanitizados o log de prueba, sin datos reales. |
| W1-AC4 | La aplicación consulta KYC y Wallet con sus IDs correspondientes y presenta el estado real del API. | Must | Respuestas de consulta y pantalla correspondiente. |
| W1-AC5 | `APPROVED` muestra estado `ACTIVE`, referencia de Wallet y saldo inicial devuelto por el API. | Must | Recorrido reproducible de alta aprobada. |
| W1-AC6 | `UNAVAILABLE` u otro error de API no se muestra como activación exitosa y deja una acción/mensaje entendible. | Must | Prueba de `503` o error de red. |
| W1-AC7 | `MANUAL_REVIEW` aparece como `PENDING`; una decisión permitida actualiza el estado consultado. | Should | Caso de soporte y resultado de decisión. |
| W1-AC8 | Los eventos locales reflejan inicio, pasos y cierre, identificándose como temporales. | Should | Panel de eventos antes de recargar. |
| W1-AC9 | La demo muestra como máximo una Wallet y no permite agregar, vincular o seleccionar otra cuenta. | Must | Inspección del flujo y la interfaz completa. |

## 7. Decisión técnica para el timebox

El repositorio contiene un backend Java 21/Quarkus contract-first cuya lógica de negocio todavía no está implementada, además del mock remoto del Grupo 4.

**Propuesta para la demo corta:** mantener el prototipo de frontend y consumir directamente el mock del Grupo 4, cuyo contrato habilita CORS. Esto evita construir una API duplicada y concentra el tiempo en el recorrido W1. El backend Quarkus queda fuera de la demo funcional salvo que el equipo o mentor confirme que debe formar parte del flujo evaluado.

Si se decide incluir Quarkus, hacerlo como una sola tarea explícita y acotada: un adaptador/BFF que valide la entrada y delegue al mock, sin inventar persistencia local ni copiar reglas del proveedor. La elección debe cerrarse en G1; no implementar ambas opciones.

## 8. Plan de trabajo sugerido

### Sprint 1 - Camino aprobado

- Confirmar el alcance, campos de interfaz y opción de integración.
- Completar onboarding guiado y validaciones.
- Integrar `POST /kyc/verifications` con `APPROVED`.
- Consultar KYC y Wallet y mostrar activación con saldo/ID devueltos.
- Probar un identificador ficticio nuevo y dejar evidencia del flujo principal.

**Salida del Sprint 1:** recorrido aprobado end-to-end ejecutable localmente.

### Sprint 2 - Excepciones y entrega

- Implementar mensajes para `UNAVAILABLE`, rechazo y errores de consulta.
- Si hay tiempo, implementar revisión manual y decisión humana.
- Ejecutar pruebas de aceptación y revisar seguridad/datos.
- Actualizar instrucciones de ejecución, `evidence.md` y decisiones pendientes.
- Congelar la versión en G3 y ensayar la demo.

**Salida del Sprint 2:** un flujo principal estable, al menos una excepción comprobada y evidencia honesta de lo que queda fuera.

## 9. Roles sugeridos

| Rol | Responsable | Foco W1 |
|---|---|---|
| Product Owner | Luis | Priorizar must/should, cerrar decisiones y aceptar criterios. |
| Arquitectura | Edert | Confirmar consumo directo del mock o BFF; mantener una sola ruta técnica. |
| Backend | Alvaro | Revisar contrato/OpenAPI y, solo si se acuerda, implementar el adaptador Quarkus acotado. |
| Frontend | Erick | Flujo, validaciones, estados de API y accesibilidad. |
| Testing | Rodrigo | Casos de aceptación, escenarios del mock y evidencia reproducible. |
| Seguridad | Michael | Confirmar datos `TEST-`, evitar PII/secretos y revisar errores. |
| Ingeniería de IA | Juan | Opcional; IA no es dependencia para completar W1 y no toma decisiones KYC. |

## 10. Definition of Done

W1 se considera terminado para la hackathon cuando:

- El recorrido aprobado se puede ejecutar en local de principio a fin contra el mock del Grupo 4.
- La Wallet se muestra activa solo después de consultar el estado devuelto por el API.
- Campos inválidos y al menos un resultado de error tienen comportamiento probado.
- Los documentos son ficticios, únicos por intento y usan el prefijo `TEST-`.
- El README técnico permite ejecutar la demo y `evidence.md` registra lo probado, lo simulado y lo pendiente.
- El equipo declara explícitamente que no hay cuentas bancarias vinculadas, pagos reales, protección contra altas duplicadas ni analítica persistente.
- El formulario acepta solo mayores de 18 y celular peruano nacional `9XXXXXXXX`; se declara que ambas comprobaciones son locales, no verificaciones legales.
- La pantalla de bienvenida es solo visual: no crea una Wallet, no consulta el saldo del API y los movimientos de ejemplo no son operaciones registradas.

## 11. Pendientes de decisión del PO en G1

- ¿Los campos de perfil del frontend serán solo demostrativos o se eliminarán para evitar sugerir que se persisten?
- ¿El punto de integración será frontend → mock o frontend → backend Quarkus → mock?
- ¿`MANUAL_REVIEW` es requisito del demo o se deja como objetivo de Sprint 2 si hay tiempo?
- ¿Qué nombre/etiqueta de estado se mostrará para `REJECTED` y `UNAVAILABLE`?
- ¿Qué captura/log sanitizado se usará como evidencia sin exponer datos de otros integrantes del Grupo 4?
