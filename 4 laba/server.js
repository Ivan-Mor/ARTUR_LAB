const http = require('http');
const fs = require('fs');
const path = require('path');
const data = require('./js/arrayModule');

const port = 3000;

const types = {
  '.html': 'text/html; charset=utf-8',
  '.css': 'text/css; charset=utf-8',
  '.js': 'application/javascript; charset=utf-8',
  '.svg': 'image/svg+xml',
  '.png': 'image/png',
  '.ico': 'image/x-icon'
};

function sendFile(res, filePath) {
  fs.readFile(filePath, (err, content) => {
    if (err) {
      res.writeHead(404, { 'Content-Type': 'text/plain; charset=utf-8' });
      res.end('404 Not Found');
      return;
    }
    res.writeHead(200, { 'Content-Type': types[path.extname(filePath)] || 'application/octet-stream' });
    res.end(content);
  });
}

function sendJson(res, payload) {
  res.writeHead(200, { 'Content-Type': 'application/json; charset=utf-8' });
  res.end(JSON.stringify(payload));
}

const server = http.createServer((req, res) => {
  const url = req.url.split('?')[0];

  if (url === '/api/data') {
    sendJson(res, {
      original: data.original,
      sorted: data.sorted,
      min: data.min
    });
    return;
  }

  if (url === '/' || url === '/index.html') {
    sendFile(res, path.join(__dirname, 'html', 'index.html'));
    return;
  }

  if (url === '/sorted' || url === '/sorted.html') {
    sendFile(res, path.join(__dirname, 'html', 'sorted.html'));
    return;
  }

  if (url.includes('..')) {
    res.writeHead(403);
    res.end('403 Forbidden');
    return;
  }

  const safePath = path.normalize(url).replace(/^(\.\.[\/\\])+/, '');
  const filePath = path.join(__dirname, safePath);

  if (!filePath.startsWith(__dirname)) {
    res.writeHead(403);
    res.end('403 Forbidden');
    return;
  }

  sendFile(res, filePath);
});

server.listen(port, () => {
  console.log('Server running at http://localhost:' + port + '/');
});
