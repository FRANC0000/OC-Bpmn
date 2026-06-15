import { Component, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DatePipe } from '@angular/common';
import { MatIcon } from '@angular/material/icon';
import { MatButton, MatFabButton } from '@angular/material/button';
import { MatCard, MatCardContent } from '@angular/material/card';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { DocumentService } from '../../../core/services/document.service';
import { DocumentDefinition } from '../../../core/models/document.model';

@Component({
  selector: 'bpm-document-list',
  standalone: true,
  imports: [RouterLink, DatePipe, MatIcon, MatButton, MatFabButton, MatCard, MatCardContent],
  template: `
    <div class="page">
      <div class="page-header">
        <div>
          <h1>Documentos</h1>
          <p class="subtitle">Gestiona las definiciones de documentos</p>
        </div>
        <button mat-flat-button color="primary" routerLink="new">
          <mat-icon>add</mat-icon> Nuevo documento
        </button>
      </div>

      @if (loading()) {
        <div class="loading-grid">
          @for (_ of [1,2,3]; track _) { <div class="skeleton-card"></div> }
        </div>
      } @else if (definitions().length === 0) {
        <div class="empty-state">
          <mat-icon class="empty-icon">description</mat-icon>
          <h3>No hay documentos</h3>
          <p>Crea tu primer documento para empezar</p>
          <button mat-flat-button color="primary" routerLink="new">
            <mat-icon>add</mat-icon> Crear documento
          </button>
        </div>
      } @else {
        @if (isMobile()) {
          <div class="card-list">
            @for (doc of definitions(); track doc.id) {
              <mat-card class="doc-card" (click)="null">
                <mat-card-content>
                  <div class="card-header">
                    <mat-icon class="doc-icon">description</mat-icon>
                    <div>
                      <span class="doc-name">{{ doc.name }}</span>
                      <span class="doc-code">{{ doc.code }}</span>
                    </div>
                    <span class="status-badge" [class.active]="doc.status === 'active'">{{ doc.status }}</span>
                  </div>
                  <div class="card-meta">
                    <span>{{ doc.versions?.length || 0 }} versiones</span>
                  </div>
                </mat-card-content>
              </mat-card>
            }
          </div>
        } @else {
          <table class="doc-table">
            <thead>
              <tr>
                <th>Nombre</th>
                <th>Código</th>
                <th>Estado</th>
                <th>Versiones</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              @for (doc of definitions(); track doc.id) {
                <tr>
                  <td>
                    <div class="cell-name">
                      <mat-icon>description</mat-icon>
                      <span>{{ doc.name }}</span>
                    </div>
                  </td>
                  <td><code>{{ doc.code }}</code></td>
                  <td><span class="status-badge" [class.active]="doc.status === 'active'">{{ doc.status }}</span></td>
                  <td>{{ doc.versions?.length || 0 }}</td>
                  <td>
                    <button mat-stroked-button size="small">Ver</button>
                  </td>
                </tr>
              }
            </tbody>
          </table>
        }
      }

      <button mat-fab class="fab" routerLink="new" color="primary">
        <mat-icon>add</mat-icon>
      </button>
    </div>
  `,
  styles: [`
    .page { max-width: 960px; }
    .page-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 24px; flex-wrap: wrap; gap: 12px; }
    .page-header h1 { font-size: 24px; font-weight: 700; margin-bottom: 4px; }
    .subtitle { color: var(--color-text-secondary); font-size: 14px; }
    .loading-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 16px; }
    .skeleton-card { height: 120px; background: var(--color-surface); border-radius: var(--radius-md); animation: pulse 1.5s infinite; }
    @keyframes pulse { 0%,100% { opacity: 0.6; } 50% { opacity: 1; } }
    .empty-state { text-align: center; padding: 64px 24px; }
    .empty-icon { font-size: 64px; width: 64px; height: 64px; color: var(--color-text-disabled); margin-bottom: 16px; }
    .empty-state h3 { font-size: 18px; font-weight: 600; margin-bottom: 8px; }
    .empty-state p { color: var(--color-text-secondary); margin-bottom: 24px; font-size: 14px; }
    .card-list { display: flex; flex-direction: column; gap: 12px; }
    .doc-card { border-radius: var(--radius-md); border: 1px solid var(--color-border); cursor: pointer; transition: all var(--transition-fast); }
    .doc-card:hover { box-shadow: var(--shadow-md); }
    .doc-card mat-card-content { padding: 16px !important; }
    .card-header { display: flex; align-items: center; gap: 12px; }
    .doc-icon { color: var(--color-primary); }
    .doc-name { display: block; font-weight: 600; font-size: 14px; }
    .doc-code { display: block; font-size: 12px; color: var(--color-text-secondary); font-family: monospace; }
    .status-badge { display: inline-block; padding: 2px 10px; border-radius: 12px; font-size: 11px; font-weight: 600; text-transform: uppercase; background: var(--color-border); color: var(--color-text-secondary); margin-left: auto; }
    .status-badge.active { background: #e8f5e9; color: #2e7d32; }
    .card-meta { margin-top: 8px; font-size: 12px; color: var(--color-text-secondary); }
    .fab { display: none; }
    .doc-table { width: 100%; border-collapse: collapse; background: var(--color-surface); border-radius: var(--radius-md); overflow: hidden; box-shadow: var(--shadow-sm); }
    .doc-table th, .doc-table td { text-align: left; padding: 14px 16px; font-size: 14px; }
    .doc-table th { background: var(--color-surface-hover); font-weight: 600; font-size: 12px; text-transform: uppercase; letter-spacing: 0.5px; color: var(--color-text-secondary); }
    .doc-table td { border-bottom: 1px solid var(--color-border-light); }
    .doc-table tr:last-child td { border-bottom: none; }
    .doc-table tr:hover td { background: var(--color-surface-hover); }
    .cell-name { display: flex; align-items: center; gap: 8px; }
    .cell-name mat-icon { color: var(--color-primary); font-size: 20px; }
    .cell-name span { font-weight: 500; }
    code { background: var(--color-surface-hover); padding: 2px 8px; border-radius: 4px; font-size: 12px; }
    @media (max-width: 767px) {
      .page-header button { display: none; }
      .fab { display: inline-flex; position: fixed; bottom: 72px; right: 16px; z-index: 100; }
    }
  `]
})
export class DocumentListComponent {
  private service = inject(DocumentService);
  private breakpoint = inject(BreakpointObserver);

  loading = signal(true);
  definitions = signal<DocumentDefinition[]>([]);
  isMobile = () => this.breakpoint.isMatched(Breakpoints.Handset);

  constructor() {
    this.service.getDefinitions().subscribe({
      next: r => this.definitions.set(r.data),
      error: () => this.loading.set(false),
      complete: () => this.loading.set(false),
    });
  }
}
