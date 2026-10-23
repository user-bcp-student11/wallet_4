import { registerLocaleData } from '@angular/common';
import localeEsPe from '@angular/common/locales/es-PE';
import { LOCALE_ID } from '@angular/core';
import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';

registerLocaleData(localeEsPe);

bootstrapApplication(AppComponent, {
  providers: [{ provide: LOCALE_ID, useValue: 'es-PE' }]
}).catch((error: unknown) => {
  console.error('No se pudo iniciar la aplicación de la billetera.', error);
});
