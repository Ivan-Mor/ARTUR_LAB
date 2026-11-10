// Сервер Node.js (только встроенные модули). Файлы читаются асинхронно — fs/promises.
// Для видео поддержаны Range-запросы (перемотка в <video>).

const http = require('http');
const fs = require('fs/promises');
const path = require('path');

const port = 3001;
const root = __dirname;

const types = {
  '.html': 'text/html; charset=utf-8',
  '.css': 'text/css; charset=utf-8',
  '.js': 'application/javascript; charset=utf-8',
  '.json': 'application/json; charset=utf-8',
  '.txt': 'text/plain; charset=utf-8',
  '.svg': 'image/svg+xml',
  '.png': 'image/png',
  '.ico': 'image/x-icon',
  '.mp4': 'video/mp4'
};

function sendNotFound(res) {
  res.writeHead(404, { 'Content-Type': 'text/plain; charset=utf-8' });
  res.end('404 Not Found');
}

function sendForbidden(res) {
  res.writeHead(403);
  res.end('403 Forbidden');
}

// Асинхронное чтение и отправка файла целиком
async function sendFile(res, filePath, req) {
  let content;
  try {
    content = await fs.readFile(filePath);
  } catch (err) {
    sendNotFound(res);
    return;
  }

  const type = types[path.extname(filePath).toLowerCase()] || 'application/octet-stream';

  // Простейшая поддержка Range для видео (206 Partial Content)
  const range = req.headers['range'];
  if (range && type === 'video/mp4') {
    const match = /bytes=(\d*)-(\d*)/.exec(range);
    if (match) {
      const total = content.length;
      const start = match[1] === '' ? 0 : parseInt(match[1], 10);
      const end = match[2] === '' ? total - 1 : Math.min(parseInt(match[2], 10), total - 1);
      if (start <= end && start < total) {
        res.writeHead(206, {
          'Content-Type': type,
          'Content-Range': 'bytes ' + start + '-' + end + '/' + total,
          'Content-Length': end - start + 1,
          'Accept-Ranges': 'bytes'
        });
        res.end(content.slice(start, end + 1));
        return;
      }
    }
  }

  res.writeHead(200, {
    'Content-Type': type,
    'Content-Length': content.length,
    'Accept-Ranges': 'bytes'
  });
  res.end(content);
}

const server = http.createServer((req, res) => {
  const url = req.url.split('?')[0];

  if (url.includes('..')) {
    sendForbidden(res);
    return;
  }

  if (url === '/' || url === '/index.html') {
    sendFile(res, path.join(root, 'html', 'index.html'), req);
    return;
  }

  const safePath = path.normalize(decodeURIComponent(url)).replace(/^(\.\.[\/\\])+/, '');
  const filePath = path.join(root, safePath);

  if (!filePath.startsWith(root)) {
    sendForbidden(res);
    return;
  }

  sendFile(res, filePath, req);
});

server.listen(port, () => {
  console.log('Server running at http://localhost:' + port + '/');
});
