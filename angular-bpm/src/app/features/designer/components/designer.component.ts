import { Component, OnInit, OnDestroy, inject, ElementRef, viewChild, afterNextRender } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { MatIcon } from '@angular/material/icon';
import { MatButton } from '@angular/material/button';
import { MatSnackBar } from '@angular/material/snack-bar';
import { lastValueFrom } from 'rxjs';
import { environment } from 'src/environments/environment';
import BpmnModeler from 'bpmn-js/lib/Modeler';

const EMPTY_DIAGRAM = `<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL"
  xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI"
  xmlns:dc="http://www.omg.org/spec/DD/20100524/DC"
  xmlns:di="http://www.omg.org/spec/DD/20100524/DI"
  id="Definitions_1" targetNamespace="http://bpmn.io/schema/bpmn">
  <process id="Process_1" isExecutable="false" />
  <bpmndi:BPMNDiagram id="BpmnDiagram_1">
    <bpmndi:BPMNPlane id="BpmnPlane_1" bpmnElement="Process_1" />
  </bpmndi:BPMNDiagram>
</definitions>`;

@Component({
  selector: 'bpm-designer',
  standalone: true,
  imports: [MatIcon, MatButton],
  template: `
    <div class="designer-page">
      <div class="designer-mobile-blocker">
        <mat-icon>desktop_windows</mat-icon>
        <h2>Solo escritorio</h2>
        <p>El diseñador BPMN solo está disponible en vista de escritorio. Accedé desde una computadora.</p>
      </div>
      <div class="designer-desktop">
        <div class="designer-header">
          <button mat-stroked-button (click)="location.back()">
            <mat-icon>arrow_back</mat-icon> Volver
          </button>
          <span class="designer-title">{{ processId ? 'Editar proceso' : 'Nuevo proceso' }}</span>
          <div class="designer-actions">
            <button mat-stroked-button [disabled]="!modeler" (click)="save()">
              <mat-icon>save</mat-icon> Guardar
            </button>
            <button mat-flat-button color="primary" [disabled]="!processId" (click)="publish()">
              <mat-icon>publish</mat-icon> Publicar
            </button>
          </div>
        </div>
        <div class="designer-canvas" #canvas></div>
      </div>
    </div>
  `,
  styles: [`
    .designer-mobile-blocker { display: none; text-align: center; padding: 80px 24px; }
    .designer-mobile-blocker mat-icon { font-size: 64px; width: 64px; height: 64px; color: var(--color-on-surface-muted); margin-bottom: 16px; }
    .designer-mobile-blocker h2 { font-size: 20px; margin-bottom: 8px; }
    .designer-mobile-blocker p { color: var(--color-text-secondary); font-size: 14px; max-width: 400px; margin: 0 auto; }

    .designer-desktop { display: flex; flex-direction: column; height: calc(100vh - var(--header-height) - 48px); }
    .designer-page { margin: -24px; }
    .designer-header { display: flex; align-items: center; gap: 12px; padding: 12px 24px; background: var(--color-surface); border-bottom: 1px solid var(--color-border); }
    .designer-title { flex: 1; font-weight: 600; font-size: 15px; color: var(--color-on-surface); }
    .designer-actions { display: flex; gap: 8px; }

    .designer-canvas { flex: 1; background: #FFFFFF; }
    .designer-canvas, .designer-canvas * { transition: none !important; }
    .designer-canvas {
      --color-grey-225-10-15: hsl(225, 10%, 15%);
      --color-grey-225-10-35: hsl(225, 10%, 35%);
      --color-grey-225-10-55: hsl(225, 10%, 55%);
      --color-grey-225-10-75: hsl(225, 10%, 75%);
      --color-grey-225-10-80: hsl(225, 10%, 80%);
      --color-grey-225-10-85: hsl(225, 10%, 85%);
      --color-grey-225-10-90: hsl(225, 10%, 90%);
      --color-grey-225-10-95: hsl(225, 10%, 95%);
      --color-grey-225-10-97: hsl(225, 10%, 97%);
      --color-blue-205-100-45: hsl(205, 100%, 45%);
      --color-blue-205-100-45-opacity-30: hsla(205, 100%, 45%, 30%);
      --color-blue-205-100-50: hsl(205, 100%, 50%);
      --color-blue-205-100-50-opacity-15: hsla(205, 100%, 50%, 15%);
      --color-blue-205-100-70: hsl(205, 100%, 75%);
      --color-blue-205-100-95: hsl(205, 100%, 95%);
      --color-green-150-86-44: hsl(150, 86%, 44%);
      --color-red-360-100-40: hsl(360, 100%, 40%);
      --color-red-360-100-45: hsl(360, 100%, 45%);
      --color-red-360-100-92: hsl(360, 100%, 92%);
      --color-red-360-100-97: hsl(360, 100%, 97%);
      --color-white: hsl(0, 0%, 100%);
      --color-black: hsl(0, 0%, 0%);
      --color-black-opacity-05: hsla(0, 0%, 0%, 5%);
      --color-black-opacity-10: hsla(0, 0%, 0%, 10%);
      --color-black-opacity-30: hsla(0, 0%, 0%, 30%);
      --canvas-fill-color: var(--color-white);
      --palette-entry-color: var(--color-grey-225-10-15);
      --palette-entry-hover-color: var(--color-blue-205-100-45);
      --palette-entry-selected-color: var(--color-blue-205-100-50);
      --palette-separator-color: var(--color-grey-225-10-75);
      --palette-toggle-hover-background-color: var(--color-grey-225-10-55);
      --palette-background-color: var(--color-grey-225-10-97);
      --palette-border-color: var(--color-grey-225-10-75);
      --context-pad-entry-background-color: var(--color-white);
      --context-pad-entry-hover-background-color: var(--color-grey-225-10-95);
      --popup-background-color: var(--color-white);
      --popup-border-color: transparent;
      --popup-shadow-color: var(--color-black-opacity-30);
    }

    @media (max-width: 1023px) {
      .designer-desktop { display: none; }
      .designer-mobile-blocker { display: flex; flex-direction: column; align-items: center; justify-content: center; min-height: calc(100vh - var(--header-height) - 48px); }
      .designer-page { margin: -16px; }
      .designer-header { padding: 12px 16px; flex-wrap: wrap; }
      .designer-title { order: -1; width: 100%; }
    }
    @media (min-width: 1024px) {
      .designer-mobile-blocker { display: none !important; }
    }
  `]
})
export class DesignerComponent implements OnInit, OnDestroy {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private http = inject(HttpClient);
  private snackBar = inject(MatSnackBar);
  location = inject(Location);

  private api = environment.apiUrl;
  modeler: BpmnModeler | null = null;
  canvas = viewChild.required<ElementRef<HTMLElement>>('canvas');

  processId: string | null = null;

  constructor() {
    afterNextRender(() => {
      this.initModeler();
    });
  }

  ngOnInit() {
    const pid = this.route.snapshot.paramMap.get('processId');
    this.processId = (pid && pid !== 'new') ? pid : null;
  }

  private async initModeler() {
    this.modeler = new BpmnModeler({
      container: this.canvas().nativeElement,
      keyboard: { bindTo: document }
    });

    if (this.processId) {
      this.http.get<any>(`${this.api}/processes/${this.processId}`).subscribe({
        next: async (r) => {
          const bpmnXml = r.data?.bpmnXml || EMPTY_DIAGRAM;
          await this.modeler!.importXML(bpmnXml);
        },
        error: async () => {
          await this.modeler!.importXML(EMPTY_DIAGRAM);
        }
      });
    } else {
      await this.modeler.importXML(EMPTY_DIAGRAM);
    }
  }

  ngOnDestroy() {
    this.modeler?.destroy();
  }

  async save() {
    if (!this.modeler) return;

    try {
      const { xml } = await this.modeler.saveXML({ format: true });

      if (this.processId) {
        await lastValueFrom(this.http.put(`${this.api}/processes/${this.processId}/bpmn`, { bpmnXml: xml }));
        this.snackBar.open('Diagrama guardado', '', { duration: 3000 });
      } else {
        const r = await lastValueFrom(this.http.post<any>(`${this.api}/processes`, {
          name: 'Nuevo proceso',
          slug: 'nuevo-proceso-' + Date.now(),
          description: '',
          ownerUserId: null
        }));
        const id = r.data.id;
        await lastValueFrom(this.http.put(`${this.api}/processes/${id}/bpmn`, { bpmnXml: xml }));
        this.snackBar.open('Proceso creado y guardado', '', { duration: 3000 });
        this.router.navigate(['/designer', id]);
      }
    } catch (e) {
      this.snackBar.open('Error al guardar el diagrama', 'Cerrar', { duration: 4000 });
    }
  }

  async publish() {
    if (!this.processId) return;
    await this.save();
    try {
      await lastValueFrom(this.http.post(`${this.api}/processes/publish-version`, {
        processId: this.processId,
        versionId: crypto.randomUUID()
      }));
      this.snackBar.open('Versión publicada', '', { duration: 3000 });
    } catch {
      this.snackBar.open('Error al publicar', 'Cerrar', { duration: 4000 });
    }
  }
}
