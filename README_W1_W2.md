# Wallet MVP - Hackathon Día 5

MVP demostrativo de una billetera digital enfocado en **W1: registro y activación** y **W2: confirmación de pagos de bajo monto**. El alcance usa datos ficticios y un proveedor de pagos simulado; no procesa dinero real ni se conecta a sistemas bancarios.

> Estado del proyecto: alcance definido para W1 y W2. Actualiza esta sección a medida que cada flujo se implemente y valide.

## Objetivo

Permitir que una persona complete el onboarding, active su Wallet y realice un pago simulado a un contacto, entendiendo con claridad el resultado de la operación.

## Alcance

### W1 - Registro y activación del MVP de Wallet

Como usuario de smartphone que realiza transferencias diarias de bajo monto, quiero completar el onboarding mediante un flujo guiado y entender el siguiente paso, para empezar a pagar sin abandonar el proceso.

Criterios de aceptación:

- El flujo muestra la secuencia de pasos requeridos y el paso actual.
- Si falta información obligatoria o hay datos inválidos, bloquea el avance y muestra cómo corregirlos.
- Al completar el onboarding, la Wallet cambia a estado activa y permite acceder al flujo de pagos.
- Se registran eventos de inicio, finalización de cada paso y cierre del onboarding.

### W2 - Confirmación de pagos de bajo monto

Como pagador, quiero completar una transferencia de bajo monto y recibir una confirmación definitiva, para saber si el pago se realizó.

Criterios de aceptación:

- Antes de confirmar, se muestran la identidad del destinatario y el importe.
- Cada intento termina con un único resultado: exitoso, fallido o expirado.
- Si el pago es exitoso, se muestra una referencia y se guarda en el historial.
- Si falla o expira, se ofrece reintentar o volver sin crear un cobro duplicado.
- Las transacciones exitosas emiten el evento de activación usado por la analítica de MAU.

## Fuera de alcance

- W3: pagos QR para comercios.
- W4: idempotencia como historia independiente, salvo la protección mínima contra duplicados requerida por W2.
- W5: manejo de fallas y reversos como flujo operativo completo.
- W6: conciliación.
- KYC real, integraciones con proveedores/bancos y movimiento de fondos.
- Métricas de negocio de trimestre (reducción de abandono, objetivo de tiempo de onboarding, MAU y CAC) como resultados demostrables de esta jornada. El prototipo puede registrar eventos ficticios, pero no demostrar esos resultados de producción.

## Plan de sprints

### Sprint 1 - W1: Registro y activación

**Horario:** 10:15-12:30  
**Meta:** dejar una aplicación local que permita completar el onboarding y activar la Wallet.

Actividades:

1. Acordar campos obligatorios, pasos, estados y mensajes de validación. Definir el mock de KYC con datos sintéticos.
2. Preparar la estructura mínima de frontend, backend y persistencia para ejecutar la aplicación localmente.
3. Implementar el onboarding guiado con progreso visible, validaciones y mensajes accionables.
4. Activar la Wallet al completar el registro y habilitar el acceso al flujo de pagos.
5. Registrar eventos de inicio, finalización de pasos y cierre del onboarding.
6. Probar alta válida, datos inválidos y activación; revisar el uso de datos sintéticos y la ausencia de secretos.

**Responsables:** Luis y Edert acuerdan flujo y criterios; Álvaro implementa API y persistencia; Erick implementa la interfaz; Rodrigo prepara y ejecuta pruebas; Michael revisa validaciones y datos.

**Entregable:** onboarding ejecutable y probado que termina con la Wallet activa.

### Sprint 2 - W2: Pago y confirmación

**Horario:** 13:45-15:15  
**Meta:** completar un pago a contacto con proveedor simulado y resultado claro.

Actividades:

1. Acordar límite de bajo monto, etiquetas de estado y contrato del proveedor mock.
2. Mostrar destinatario e importe antes de confirmar el pago.
3. Implementar resultados exitoso, fallido y expirado, con un resultado terminal por intento.
4. En caso de éxito, mostrar la referencia y guardar la operación en el historial.
5. En caso de fallo o expiración, permitir volver o reintentar sin crear un cobro duplicado.
6. Probar el flujo completo, los resultados del mock y el comportamiento del reintento.

**Responsables:** Luis y Edert cierran las reglas y contratos; Álvaro implementa estados, persistencia y mock; Erick implementa la interfaz; Rodrigo prueba los escenarios; Michael revisa datos, errores y duplicados. Juan puede aportar una explicación de estados con IA si queda tiempo, sin ejecutar pagos.

**Entregable:** recorrido reproducible: registro → Wallet activa → revisión de destinatario e importe → pago simulado → resultado e historial.

Al finalizar Sprint 2, congelar el código a las 15:15. Usar el bloque 15:15-15:45 para preparar README, `evidence.md` y la demo.

## Recorrido de demostración

1. Registrar un usuario con datos sintéticos.
2. Mostrar los pasos del onboarding y probar una validación fallida.
3. Completar el onboarding y confirmar que la Wallet queda activa.
4. Iniciar un pago a un contacto y revisar destinatario e importe antes de confirmar.
5. Ejecutar el pago contra el mock y mostrar el resultado.
6. Si es exitoso, mostrar referencia e historial; si falla o expira, mostrar la opción de reintento o regreso.
7. Verificar que un reintento no genera una operación duplicada.

## Arquitectura y decisiones

Completar cuando el equipo acuerde la implementación:

- Interfaz: [tecnología y responsabilidad]
- Backend/API: [tecnología y responsabilidad]
- Persistencia: [tecnología y datos guardados]
- Proveedor de pagos simulado: [contrato y resultados controlables]
- Analítica local: [eventos registrados y dónde consultarlos]
- Diagrama: [ruta al diagrama]
- Decisiones técnicas/ADR: [rutas o enlaces]

## Requisitos y ejecución local

Stack y comandos de ejecución: **pendientes de definir e implementar**. Completar esta sección cuando la aplicación sea ejecutable:

- Runtime y versión: [pendiente]
- Dependencias: [pendiente]
- Configuración: [instrucciones; no incluir secretos]
- Instalar: `[comando pendiente]`
- Ejecutar: `[comando pendiente]`
- URL local: [pendiente]

No publiques tokens, contraseñas ni datos personales en el repositorio. Si se requieren variables de entorno, documenta sus nombres en `.env.example` con valores ficticios.

## Pruebas y evidencia

Registrar los comandos ejecutados y resultados reales. No marcar un criterio como completo sin una prueba o demostración reproducible.

| Historia | Caso | Resultado | Evidencia |
|---|---|---|---|
| W1 | Onboarding válido activa la Wallet | Pendiente | [ruta/enlace] |
| W1 | Datos ausentes o inválidos bloquean el avance con mensaje accionable | Pendiente | [ruta/enlace] |
| W1 | Eventos de inicio, pasos y cierre quedan registrados | Pendiente | [ruta/enlace] |
| W2 | Destinatario e importe se muestran antes de confirmar | Pendiente | [ruta/enlace] |
| W2 | Éxito devuelve referencia y aparece en historial | Pendiente | [ruta/enlace] |
| W2 | Fallo/expiración permiten volver o reintentar sin duplicar el cobro | Pendiente | [ruta/enlace] |

Comando de pruebas: `[pendiente]`

## Seguridad y limitaciones

- Usar únicamente datos de prueba sintéticos.
- El proveedor de pagos es un mock; los resultados no representan una operación real.
- No almacenar ni registrar credenciales, secretos o datos financieros reales.
- Documentar cómo se evita la duplicidad en un reintento de W2 y las limitaciones de esa protección.
- Los límites de monto y las etiquetas de estados deben ser acordados por Producto antes de la demo.

## Uso de IA

[Describir agentes, Skills, prompts o herramientas utilizadas, su propósito y cómo se validaron. Si no hay IA integrada en el producto, indicarlo explícitamente.]

## Equipo

| Rol | Integrante | Responsabilidad principal |
|---|---|---|
| Product Owner | Luis | Priorizar alcance y aceptar criterios |
| Arquitectura | Edert | Definir flujo, contratos, mocks y decisiones técnicas |
| Backend | Alvaro | API, estados, persistencia y proveedor simulado |
| Frontend | Erick | Onboarding, Wallet y flujo de pago |
| Testing | Rodrigo | Casos de aceptación, pruebas y evidencia |
| Seguridad | Michael | Validaciones, manejo de datos y revisión de duplicados |
| Ingeniería de IA | Juan | Aporte de IA acotado y validado, si aplica |

## Decisiones abiertas

- Regla y límite para definir un pago de bajo monto.
- Etiquetas finales de éxito, fallo y expiración que verá el usuario.
- Frontera de los eventos que miden inicio y fin del onboarding.
- Contrato del mock y estrategia mínima para evitar duplicados.

## Entregables

- Aplicación local ejecutable con el flujo W1-W2.
- Pruebas reproducibles y datos sintéticos.
- README actualizado con instrucciones verificadas.
- `evidence.md` con resultados, capturas/logs y criterios cubiertos.
- Pull request y decisiones técnicas documentadas.
