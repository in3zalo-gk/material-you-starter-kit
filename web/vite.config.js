import { defineConfig } from 'vite';
import { readFileSync } from 'node:fs';

export default defineConfig({
  base: './',
  plugins: [{ name: 'third-party-notices', generateBundle() { this.emitFile({ type: 'asset', fileName: 'THIRD_PARTY_NOTICES.md', source: readFileSync(new URL('../THIRD_PARTY_NOTICES.md', import.meta.url), 'utf8') }); } }],
  build: {
    outDir: 'dist',
    emptyOutDir: true,
    rollupOptions: {
      input: {
        main: 'index.html',
        minimal: 'examples/minimal.html',
        catalog: 'catalog.html',
        materialWeb: 'examples/material-web.html'
      }
    }
  }
});
