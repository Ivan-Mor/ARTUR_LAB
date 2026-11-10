// Клиентский сценарий второй страницы: массив после обработки + максимальный элемент

function cellColor(value) {
  const ratio = (value - 1) / 9; // значения от 1 до 10
  const hue = 200 - ratio * 200;
  return 'hsl(' + hue + ', 75%, 55%)';
}

function buildTable(values, rows, cols) {
  const table = document.createElement('table');
  for (let row = 0; row < rows; row++) {
    const tr = document.createElement('tr');
    for (let col = 0; col < cols; col++) {
      const td = document.createElement('td');
      const value = values[row * cols + col];
      td.textContent = value;
      td.style.background = cellColor(value);
      tr.appendChild(td);
    }
    table.appendChild(tr);
  }
  return table;
}

fetch('/api/data')
  .then(function (response) { return response.json(); })
  .then(function (data) {
    document.getElementById('sorted-table').appendChild(buildTable(data.sorted, 8, 10));
    document.getElementById('max-value').textContent = data.max;
  });
