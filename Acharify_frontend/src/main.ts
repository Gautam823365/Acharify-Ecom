import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { appConfig } from './app/app.config';

import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './app/authIntercepter/auth.Intercepter';

import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { importProvidersFrom } from '@angular/core';
import { MatNativeDateModule } from '@angular/material/core';

bootstrapApplication(AppComponent, {
  providers: [
    provideHttpClient(withInterceptors([authInterceptor])),

    // ✅ Angular Material requirements
    provideAnimationsAsync(),
    importProvidersFrom(MatNativeDateModule),

    ...appConfig.providers
  ]
}).catch(err => console.error(err));
