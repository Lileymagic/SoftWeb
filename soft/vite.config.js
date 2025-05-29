import { defineConfig } from 'vite';
import { resolve } from 'path';
import { viteStaticCopy } from 'vite-plugin-static-copy';

export default defineConfig({

  build: {
    // === 경로 수정된 부분 ===
    outDir: resolve(__dirname, '../back/src/main/resources/static'),
    // =======================
    emptyOutDir: true, //백엔드의 static 폴더를 빌드 전에 비움
    rollupOptions: {
      input: {
        index: resolve(__dirname, 'front/index.html'),
        login: resolve(__dirname, 'front/login.html'),
        join:  resolve(__dirname, 'front/join.html'),
        profile: resolve(__dirname, 'front/profile.html'),
        findmyid: resolve(__dirname, 'front/findmyid.html'),
        findmypw: resolve(__dirname, 'front/findmypw.html'),
        projectboard: resolve(__dirname, 'front/projectboard.html'),
        projectmanage: resolve(__dirname, 'front/projectmanage.html'),
        projectlist: resolve(__dirname, 'front/projectlist.html'),
        worklist: resolve(__dirname, 'front/worklist.html'),
        writework: resolve(__dirname, 'front/writework.html'),
      },
      output: {
        assetFileNames(info) {
            const name = info.name;
            if (!name) {
                return 'assets/[name].[hash][extname]';
            }
            if (name.startsWith('front/icon/')) {
                return '[name][extname]';
            }
            if (name.endsWith('.css')){
                return 'css/[name].[hash][extname]';    
            }

            if (/\.(png|jpe?g|svg|json)$/.test(name)) {
                const parts = name.split('/');
                const file  = parts.pop();          
                const dir   = parts.join('/');   
                    
                const match = file.match(/^(.*)(\.[^\.]+)$/);
                if (match) {
                    const [, base, ext] = match;
                    return dir
                    ? `${dir}/${base}.[hash]${ext}`
                    : `${base}.[hash]${ext}`;
                }
                return `${file}.[hash]`;
            }
            return 'assets/[name].[hash][extname]';
        }
      }
    }
  },
  plugins: [
    viteStaticCopy({
      targets: [
        { src: 'front/public/css',  dest: '' },  
        { src: 'front/public/icon', dest: '' }
      ]
    })
  ]
});