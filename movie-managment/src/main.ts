import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideAnimations } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
// ✅ Import provideForms
import { routes } from './app/app.routes';
import { tokenInterceptor } from './app/auth/token.interceptor';

// ✅ Import required PrimeNG modules
import { importProvidersFrom } from '@angular/core';
import { TableModule } from 'primeng/table';
import { PaginatorModule } from 'primeng/paginator';
import { ButtonModule } from 'primeng/button';
import { MenubarModule } from 'primeng/menubar';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { RatingModule } from 'primeng/rating';

bootstrapApplication(AppComponent, {
  providers: [
    provideRouter(routes, withComponentInputBinding()),
    provideHttpClient(withInterceptors([tokenInterceptor])),
    provideAnimations(), // ✅ Add missing comma here

    // ✅ Register PrimeNG modules globally
    importProvidersFrom(
      FormsModule,
      ReactiveFormsModule,
      TableModule,
      PaginatorModule,
      ButtonModule,
      MenubarModule,
      DialogModule,
      InputTextModule,
      RatingModule
    )
  ]
}).catch(err => console.error(err));
