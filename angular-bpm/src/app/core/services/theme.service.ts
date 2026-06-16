import { Injectable, signal } from '@angular/core';

export type Theme = 'light' | 'dark';

const STORAGE_KEY = 'bpm_theme';

@Injectable({ providedIn: 'root' })
export class ThemeService {
  theme = signal<Theme>('light');

  constructor() {
    const stored = localStorage.getItem(STORAGE_KEY) as Theme | null;
    const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
    const initial: Theme = stored ?? (prefersDark ? 'dark' : 'light');
    this.apply(initial);

    window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', (e) => {
      if (localStorage.getItem(STORAGE_KEY) === null) {
        this.apply(e.matches ? 'dark' : 'light');
      }
    });
  }

  toggle(): void {
    const next = this.theme() === 'light' ? 'dark' : 'light';
    this.apply(next);
    localStorage.setItem(STORAGE_KEY, next);
  }

  private apply(theme: Theme): void {
    this.theme.set(theme);
    document.documentElement.setAttribute('data-theme', theme);
  }
}
