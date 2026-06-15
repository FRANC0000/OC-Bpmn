import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Location } from '@angular/common';
import { MatIcon } from '@angular/material/icon';
import { MatButton } from '@angular/material/button';
import { MatCard, MatCardContent } from '@angular/material/card';
import { MatFormField, MatLabel, MatError } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DocumentService } from '../../../core/services/document.service';

@Component({
  selector: 'bpm-document-form',
  standalone: true,
  imports: [RouterLink, ReactiveFormsModule, MatIcon, MatButton, MatCard, MatCardContent, MatFormField, MatLabel, MatError, MatInput, MatProgressSpinner],
  template: `
    <div class="page">
      <div class="page-header">
        <button mat-stroked-button (click)="location.back()">
          <mat-icon>arrow_back</mat-icon> Volver
        </button>
      </div>
      <mat-card class="form-card">
        <mat-card-content>
          <h2>Nuevo documento</h2>
          <p class="subtitle">Define la estructura básica del documento</p>
          <form [formGroup]="form" (ngSubmit)="onSubmit()" class="form">
            <mat-form-field>
              <mat-label>Código</mat-label>
              <input matInput formControlName="code" placeholder="factura-2026" autocomplete="off">
              @if (form.get('code')?.hasError('required') && form.get('code')?.touched) {
                <mat-error>El código es requerido</mat-error>
              }
            </mat-form-field>
            <mat-form-field>
              <mat-label>Nombre</mat-label>
              <input matInput formControlName="name" placeholder="Factura 2026">
              @if (form.get('name')?.hasError('required') && form.get('name')?.touched) {
                <mat-error>El nombre es requerido</mat-error>
              }
            </mat-form-field>
            <mat-form-field>
              <mat-label>Descripción</mat-label>
              <textarea matInput formControlName="description" rows="3" placeholder="Descripción opcional del documento"></textarea>
            </mat-form-field>
            <div class="form-actions">
              <button mat-stroked-button type="button" routerLink="/documents">Cancelar</button>
              <button mat-flat-button color="primary" type="submit" [disabled]="loading">
                @if (loading) { <mat-spinner diameter="18" /> }
                <span>Crear documento</span>
              </button>
            </div>
          </form>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [`
    .page { max-width: 640px; }
    .page-header { margin-bottom: 16px; }
    .form-card { border-radius: var(--radius-md); border: 1px solid var(--color-border); }
    .form-card mat-card-content { padding: 32px !important; }
    h2 { font-size: 20px; font-weight: 700; margin-bottom: 4px; }
    .subtitle { font-size: 14px; color: var(--color-text-secondary); margin-bottom: 24px; }
    .form { display: flex; flex-direction: column; gap: 16px; }
    .form-actions { display: flex; justify-content: flex-end; gap: 12px; margin-top: 8px; }
    .form-actions button { min-width: 120px; display: flex; align-items: center; gap: 8px; }
  `]
})
export class DocumentFormComponent {
  private fb = inject(FormBuilder);
  private service = inject(DocumentService);
  private router = inject(Router);
  private snackBar = inject(MatSnackBar);
  location = inject(Location);

  loading = false;

  form = this.fb.nonNullable.group({
    code: ['', Validators.required],
    name: ['', Validators.required],
    description: [''],
  });

  onSubmit(): void {
    if (this.form.invalid) return;
    this.loading = true;
    this.service.createDefinition(this.form.getRawValue()).subscribe({
      next: () => {
        this.snackBar.open('Documento creado exitosamente', '', { duration: 3000, panelClass: 'success-snackbar' });
        this.router.navigate(['/documents']);
      },
      error: (err) => {
        this.loading = false;
        this.snackBar.open(err.error?.message || 'Error al crear documento', 'Cerrar', { duration: 5000, panelClass: 'error-snackbar' });
      }
    });
  }
}
