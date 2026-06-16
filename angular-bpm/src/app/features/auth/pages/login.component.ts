import { Component, inject } from '@angular/core';
import { RouterLink, Router } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatCard, MatCardContent } from '@angular/material/card';
import { MatFormField, MatLabel, MatError, MatSuffix } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { MatIcon } from '@angular/material/icon';
import { MatButton, MatIconButton } from '@angular/material/button';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'bpm-login',
  standalone: true,
  imports: [RouterLink, ReactiveFormsModule, MatCard, MatCardContent, MatFormField, MatLabel, MatError, MatSuffix, MatInput, MatIcon, MatButton, MatIconButton, MatProgressSpinner],
  template: `
    <div class="auth-container auth-bg">
      <div class="auth-card-wrapper">
        <div class="auth-brand">
          <mat-icon class="brand-icon">assignment</mat-icon>
          <span class="brand-name">BPM Platform</span>
        </div>
        <mat-card class="auth-card">
          <mat-card-content>
            <h2 class="auth-title">Iniciar sesión</h2>
            <p class="auth-subtitle">Ingresa tus credenciales para continuar</p>
            <form [formGroup]="form" (ngSubmit)="onSubmit()" class="auth-form">
              <mat-form-field>
                <mat-label>Correo electrónico</mat-label>
                <input matInput type="email" formControlName="email" placeholder="ejemplo@correo.com" autocomplete="email">
                @if (form.get('email')?.hasError('required') && form.get('email')?.touched) {
                  <mat-error>El correo es requerido</mat-error>
                }
                @if (form.get('email')?.hasError('email') && form.get('email')?.touched) {
                  <mat-error>Correo inválido</mat-error>
                }
              </mat-form-field>
              <mat-form-field>
                <mat-label>Contraseña</mat-label>
                <input matInput [type]="showPassword ? 'text' : 'password'" formControlName="password" placeholder="••••••••" autocomplete="current-password">
                <button mat-icon-button matSuffix type="button" (click)="showPassword = !showPassword" tabindex="-1">
                  <mat-icon>{{ showPassword ? 'visibility_off' : 'visibility' }}</mat-icon>
                </button>
                @if (form.get('password')?.hasError('required') && form.get('password')?.touched) {
                  <mat-error>La contraseña es requerida</mat-error>
                }
              </mat-form-field>
              <button mat-flat-button color="primary" class="submit-btn" type="submit" [disabled]="loading">
                @if (loading) { <mat-spinner diameter="20" /> }
                <span>Iniciar sesión</span>
              </button>
            </form>
            <p class="auth-redirect">
              ¿No tienes cuenta? <a routerLink="/auth/register">Regístrate</a>
            </p>
          </mat-card-content>
        </mat-card>
      </div>
    </div>
  `,
  styles: [`
    .auth-container { display: flex; align-items: center; justify-content: center; min-height: 100vh; padding: 24px; }
    .auth-card-wrapper { width: 100%; max-width: 420px; }
    .auth-brand { display: flex; align-items: center; justify-content: center; gap: 12px; margin-bottom: 32px; }
    .brand-icon { font-size: 40px; width: 40px; height: 40px; color: white; }
    .brand-name { font-size: 24px; font-weight: 700; color: white; }
    .auth-card { border-radius: var(--radius-lg); box-shadow: var(--shadow-xl); }
    .auth-card mat-card-content { padding: 32px !important; }
    .auth-title { font-size: 22px; font-weight: 700; margin-bottom: 4px; }
    .auth-subtitle { font-size: 14px; color: var(--color-text-secondary); margin-bottom: 24px; }
    .auth-form { display: flex; flex-direction: column; gap: 16px; }
    .submit-btn { height: 48px; font-size: 15px; font-weight: 600; display: flex; align-items: center; justify-content: center; gap: 8px; border-radius: var(--radius-md); }
    .auth-redirect { text-align: center; margin-top: 20px; font-size: 14px; color: var(--color-text-secondary); }
    .auth-redirect a { color: var(--color-primary); font-weight: 600; text-decoration: none; }
    .auth-redirect a:hover { text-decoration: underline; }
    @media (max-width: 480px) {
      .auth-container { padding: 16px; }
      .auth-brand { margin-bottom: 24px; }
    }
  `]
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private auth = inject(AuthService);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);

  showPassword = false;
  loading = false;

  form = this.fb.nonNullable.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]],
  });

  onSubmit(): void {
    if (this.form.invalid) return;
    this.loading = true;
    this.auth.login(this.form.getRawValue()).subscribe({
      next: () => {
        this.snackBar.open('Inicio de sesión exitoso', '', { duration: 3000, panelClass: 'success-snackbar' });
        this.router.navigate(['/tasks']);
      },
      error: (err) => {
        this.loading = false;
        this.snackBar.open(err.error?.message || 'Error al iniciar sesión', 'Cerrar', { duration: 5000, panelClass: 'error-snackbar' });
      }
    });
  }
}
