/*const staticAssets = [
  './',
  './404.html',
  './avisos.html',
  './blank.html',
  './dashboard.html',
  './funcionario.html',
  './index.js',
  './instituicao.html',
  './listafuncionarios.html',
  './listainstituicoes.html',
  './listaocorrencias.html',
  './listareclusos.html',
  './login.html',
  './perfil.html',
  './recluso.html',
  './registar_funcionario.html',
  './registar_instituicao.html',
  './registar_recluso.html'
];

self.addEventListener('install', async event => {
    console.log('install event')
    const cache = await caches.open('smth'); 
    cache.addAll(staticAssets); 
  });
  
  self.addEventListener('fetch', async event => {
    console.log('fetch event')
    const req = event.request;
    event.respondWith(cacheFirst(req));
  });
  async function cacheFirst(req) {
    const cache = await caches.open('smth'); 
    const cachedResponse = await cache.match(req); 
    return cachedResponse || fetch(req); 
  }*/