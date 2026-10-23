# Onboarding y pago con QR

Diagrama funcional del MVP: registro y activación de wallet, seguido del pago a un comercio mediante QR.

```mermaid
flowchart TD
    C["Cliente"] --> WEB["PWA responsiva"]

    subgraph ONBOARD["Registro y activación"]
        WEB --> FORM["Completa datos de registro"]
        FORM --> APIKYC["Backend: POST /kyc/verifications"]
        APIKYC --> KYCSTATUS{"Estado KYC"}
        KYCSTATUS -->|"ACTIVE"| ACTIVE["Wallet activa<br/>continúa al pago"]
        KYCSTATUS -->|"PENDING"| WAITKYC["Onboarding pendiente<br/>requiere revisión"]
        KYCSTATUS -->|"REJECTED"| REJECTKYC["Mostrar rechazo<br/>no habilitar pagos"]
        OPS["Operaciones / simulador<br/>solo para pruebas"] -. "si KYC queda PENDING" .-> DECISION["POST /kyc/verifications/{id}/decision"]
        DECISION --> KYCSTATUS
    end

    subgraph QRPAY["Pago con QR"]
        ACTIVE --> SCAN["Escanea QR con la cámara"]
        SCAN --> RESOLVE["Backend: POST /qrs/resolve"]
        RESOLVE --> VALID{"QR válido y vigente"}
        VALID -->|"No"| QRERROR["Mostrar error<br/>no crear pago"]
        VALID -->|"Sí"| SUMMARY["Mostrar comercio y monto<br/>solicitar confirmación"]
        SUMMARY -->|"Confirma"| CREATE["POST /payments<br/>Idempotency-Key + qrId"]
        SUMMARY -->|"Cancela"| CANCEL["Cerrar flujo sin crear pago"]
        CREATE --> RESULT{"Resultado del intento"}
        RESULT -->|"SUCCEEDED"| SUCCESS["Mostrar pago exitoso<br/>referencia / comprobante"]
        RESULT -->|"REJECTED"| FAILURE["Mostrar rechazo<br/>ofrecer siguiente acción"]
        RESULT -->|"PENDING · HTTP 202"| PENDING["Mostrar pago en proceso<br/>no afirmar éxito"]
        CREATE -->|"HTTP 504 con paymentId"| UNCERTAIN["Resultado incierto<br/>consultar el mismo pago"]
        UNCERTAIN --> STATUS["GET /payments/{id}"]
        PENDING --> STATUS
        STATUS --> RESULT
    end

    subgraph TESTS["Solo para pruebas del mock"]
        SIM["POST /control/provider/{id}/complete"]
        DELIVER["POST /control/events/{id}/deliver<br/>o POST /callbacks/payments"]
        SIM --> DELIVER
        DELIVER -. "actualiza el estado local" .-> STATUS
    end
```

## Comportamiento funcional

- Si el KYC queda `PENDING`, la wallet no se activa hasta que se resuelva.
- Si el QR es inválido o venció, se muestra un error y no se crea el pago.
- El pago se envía solo después de mostrar el comercio y el monto y recibir la confirmación del cliente.
- Si el pago queda `PENDING` o la respuesta es `504`, se consulta el mismo pago. No se crea otro intento automáticamente.
- Para reintentar la misma solicitud, se reutilizan la misma `Idempotency-Key` y el mismo contenido. Un nuevo intento explícito debe tener una clave nueva.
- Los endpoints de control del mock son exclusivamente para pruebas; no se exponen al cliente.