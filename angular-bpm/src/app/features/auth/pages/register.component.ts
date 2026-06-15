import { Component, inject } from '@angular/core';
import { RouterLink, Router } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators, AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import { MatCard, MatCardContent } from '@angular/material/card';
import { MatFormField, MatLabel, MatError, MatSuffix } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { MatIcon } from '@angular/material/icon';
import { MatButton, MatIconButton } from '@angular/material/button';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../../../core/services/auth.service';

function passwordsMatch(ctrl: AbstractControl): ValidationErrors | null {
  const pw = ctrl.get('password');
  const cf = ctrl.get('confirmPassword');
  return pw && cf && pw.value !== cf.value ? { passwordsMismatch: true } : null;
}

@Component({
  selector: 'bpm-register',
  standalone: true,
  imports: [RouterLink, ReactiveFormsModule, MatCard, MatCardContent, MatFormField, MatLabel, MatError, MatSuffix, MatInput, MatIcon, MatButton, MatIconButton, MatProgressSpinner],
  template: `
    <div class="auth-container">
      <div class="auth-card-wrapper">
        <div class="auth-brand">
          <mat-icon class="brand-icon">assignment</mat-icon>
          <span class="brand-name">BPM Platform</span>
        </div>
        <mat-card class="auth-card">
          <mat-card-content>
            <h2 class="auth-title">Crear cuenta</h2>
            <p class="auth-subtitle">Completa los datos para registrarte</p>
            <form [formGroup]="form" (ngSubmit)="onSubmit()" class="auth-form">
              <mat-form-field>
                <mat-label>Nombre completo</mat-label>
                <input matInput formControlName="displayName" placeholder="Tu nombre" autocomplete="name">
                @if (form.get('displayName')?.hasError('required') && form.get('displayName')?.touched) {
                  <mat-error>El nombre es requerido</mat-error>
                }
              </mat-form-field>
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
                <input matInput [type]="showPassword ? 'text' : 'password'" formControlName="password" placeholder="Mínimo 6 caracteres" autocomplete="new-password">
                <button mat-icon-button matSuffix type="button" (click)="showPassword = !showPassword" tabindex="-1">
                  <mat-icon>{{ showPassword ? 'visibility_off' : 'visibility' }}</mat-icon>
                </button>
                @if (form.get('password')?.hasError('required') && form.get('password')?.touched) {
                  <mat-error>La contraseña es requerida</mat-error>
                }
                @if (form.get('password')?.hasError('minlength') && form.get('password')?.touched) {
                  <mat-error>Mínimo 6 caracteres</mat-error>
                }
              </mat-form-field>
              <mat-form-field>
                <mat-label>Confirmar contraseña</mat-label>
                <input matInput [type]="showConfirm ? 'text' : 'password'" formControlName="confirmPassword" placeholder="Repite la contraseña" autocomplete="new-password">
                <button mat-icon-button matSuffix type="button" (click)="showConfirm = !showConfirm" tabindex="-1">
                  <mat-icon>{{ showConfirm ? 'visibility_off' : 'visibility' }}</mat-icon>
                </button>
                @if (form.hasError('passwordsMismatch') && form.get('confirmPassword')?.touched) {
                  <mat-error>Las contraseñas no coinciden</mat-error>
                }
              </mat-form-field>
              <button mat-flat-button color="primary" class="submit-btn" type="submit" [disabled]="loading">
                @if (loading) { <mat-spinner diameter="20" /> }
                <span>Crear cuenta</span>
              </button>
            </form>
            <p class="auth-redirect">
              ¿Ya tienes cuenta? <a routerLink="/auth/login">Inicia sesión</a>
            </p>
          </mat-card-content>
        </mat-card>
      </div>
    </div>
  `,
  styles: [`
    .auth-container { display: flex; align-items: center; justify-content: center; min-height: 100vh; padding: 24px; background: linear-gradient(135deg, var(--color-primary) 0%, var(--color-primary-dark) 100%); }
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
      .auth-container { padding: 16px; background: var(--color-bg); }
      .auth-brand { margin-bottom: 24px; }
      .brand-icon { color: var(--color-primary); }
      .brand-name { color: var(--color-text); }
      .auth-card { box-shadow: var(--shadow-sm); }
    }
  `]
})
export class RegisterComponent {
  private fb = inject(FormBuilder);
  private auth = inject(AuthService);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);

  showPassword = false;
  showConfirm = false;
  loading = false;

  form = this.fb.nonNullable.group({
    displayName: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    confirmPassword: ['', [Validators.required]],
  }, { validators: passwordsMatch });

  onSubmit(): void {
    if (this.form.invalid) return;
    this.loading = true;
    const { confirmPassword, ...data } = this.form.getRawValue();
    this.auth.register(data).subscribe({
      next: () => {
        this.snackBar.open('Cuenta creada exitosamente. Inicia sesión.', '', { duration: 4000, panelClass: 'success-snackbar' });
        this.router.navigate(['/auth/login']);
      },
      error: (err) => {
        this.loading = false;
        this.snackBar.open(err.error?.message || 'Error al registrarse', 'Cerrar', { duration: 5000, panelClass: 'error-snackbar' });
      }
    });
  }
}
