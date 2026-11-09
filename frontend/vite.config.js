
import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'
import path from 'node:path'

export default defineConfig({
  plugins: [react()],

  build: {
    outDir: path.resolve(
      __dirname,
      '../AudioVisualStudio.back/AudioVisualStudio/audiovisualstudio/audiovisualstudio/src/main/resources/static'
    ),
    emptyOutDir: true,
  },

  server: {
    host: 'localhost',
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
    },
  },
})
