import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    host: '0.0.0.0',
    port: 5173, // Der Port für den Vite-Server
    open: true,
    strictPort: true,
    proxy: {
      '/backend': {
        target: 'http://localhost:8080', // Dein Backend
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/backend/, ''), // Anfragen von /backend an das Backend weiterleiten
      },
    },
  },
})