const express = require('express');
const path = require('path');
const connectDatabase = require('./js/database');

const app = express();
const port = Number(process.env.PORT || 3007);

app.use(express.json());
app.use('/css', express.static(path.join(__dirname, 'css')));
app.use('/js', express.static(path.join(__dirname, 'js')));
app.use('/resource', express.static(path.join(__dirname, 'resource')));
app.get('/', (request, response) => response.sendFile(path.join(__dirname, 'html', 'index.html')));
app.use('/api/orders', require('./js/routes/orders'));
app.use((error, request, response, next) => {
  const status = error.name === 'ValidationError' ? 400 : 500;
  response.status(status).json({ message: error.message || 'Внутренняя ошибка сервера' });
});

app.listen(port, async () => {
  process.stdout.write(`Лабораторная работа №7: http://localhost:${port}\n`);
  try {
    await connectDatabase();
  } catch (error) {
    process.stdout.write(`MongoDB недоступна: ${error.message}\n`);
  }
});
