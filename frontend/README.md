# Dashboard de Mi Billetera (Angular)

Mock responsive de la pantalla principal posterior al inicio de sesión. Presenta el saludo al usuario, saldo actual, últimos movimientos y accesos a pago por contacto y pago por QR. Los datos son demostrativos; los accesos muestran un aviso y no ejecutan pagos ni se conectan a un backend.

## Ejecutar

Requiere Node.js 22.22.3, 24.15.0 o 26.0.0 en adelante (versiones admitidas por Angular CLI 22).

```powershell
cd frontend
npm install
npm start
```

Abre `http://localhost:4200`. Para generar el build de producción:

```powershell
npm run build
```

El nombre, el saldo y los movimientos de ejemplo se definen en `src/app/app.component.ts`.
