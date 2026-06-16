import { Component, computed, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { MatSidenavContainer, MatSidenav, MatSidenavContent } from '@angular/material/sidenav';
import { HeaderComponent } from './header.component';
import { SidenavComponent } from './sidenav.component';
import { BottomNavComponent } from './bottom-nav.component';

@Component({
  selector: 'bpm-main-layout',
  standalone: true,
  imports: [RouterOutlet, MatSidenavContainer, MatSidenav, MatSidenavContent, HeaderComponent, SidenavComponent, BottomNavComponent],
  template: `
    <div class="layout">
      <bpm-header (menuToggle)="sidenav.toggle()" />
      <mat-sidenav-container class="sidenav-container">
        <mat-sidenav #sidenav
          [mode]="sidenavMode()"
          [opened]="sidenavOpen()"
          (closed)="sidenavOpen.set(false)"
          class="sidenav">
          <bpm-sidenav />
        </mat-sidenav>
        <mat-sidenav-content class="content">
          <main class="main-content">
            <router-outlet />
          </main>
        </mat-sidenav-content>
      </mat-sidenav-container>
      @if (showBottomNav()) {
        <bpm-bottom-nav class="mobile-nav" />
      }
    </div>
  `,
  styles: [`
    .layout { display: flex; flex-direction: column; height: 100vh; overflow: hidden; }
    .sidenav-container { flex: 1; }
    .sidenav { width: var(--sidebar-width); border-right: 1px solid var(--color-border); background: var(--color-surface-raised); }
    .content { display: flex; flex-direction: column; background: var(--color-bg); }
    .main-content { flex: 1; padding: 24px; overflow-y: auto; }
    .mobile-nav { display: none; }
    @media (max-width: 767px) {
      .main-content { padding: 16px; }
      .mobile-nav { display: flex; }
      .sidenav { width: 280px; }
    }
    @media (min-width: 768px) and (max-width: 1023px) {
      .sidenav { width: var(--sidebar-collapsed); }
    }
  `]
})
export class MainLayoutComponent {
  sidenavOpen = signal(true);
  isMobile = signal(false);
  isTablet = signal(false);

  constructor(observer: BreakpointObserver) {
    observer.observe([Breakpoints.Handset]).subscribe(r => this.isMobile.set(r.matches));
    observer.observe([Breakpoints.Tablet]).subscribe(r => this.isTablet.set(r.matches));

    if (this.isMobile()) this.sidenavOpen.set(false);
  }

  sidenavMode = computed(() => this.isMobile() ? 'over' : 'side');
  showBottomNav = computed(() => this.isMobile());
}
