const http = require('http');
const fs = require('fs/promises');
const path = require('path');

const root = __dirname;
const port = Number(process.env.PORT || 3006);
const contentTypes = {
  '.html': 'text/html; charset=utf-8',
  '.css': 'text/css; charset=utf-8',
  '.js': 'application/javascript; charset=utf-8',
  '.svg': 'image/svg+xml'
};

async function sendFile(response, filePath) {
  try {
    const content = await fs.readFile(filePath);
    response.writeHead(200, { 'Content-Type': contentTypes[path.extname(filePath)] || 'application/octet-stream' });
    response.end(content);
  } catch {
    response.writeHead(404, { 'Content-Type': 'text/plain; charset=utf-8' });
    response.end('Файл не найден');
  }
}

const server = http.createServer((request, response) => {
  const pathname = decodeURIComponent(new URL(request.url, 'http://localhost').pathname);
  if (pathname === '/' || pathname === '/index.html') {
    sendFile(response, path.join(root, 'html', 'index.html'));
    return;
  }
  const filePath = path.resolve(root, `.${pathname}`);
  if (!filePath.startsWith(`${root}${path.sep}`)) {
    response.writeHead(403);
    response.end('Доступ запрещён');
    return;
  }
  sendFile(response, filePath);
});

server.listen(port, () => process.stdout.write(`Лабораторная работа №6: http://localhost:${port}\n`));
