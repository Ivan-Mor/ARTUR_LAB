function cellColor(value) {
  const ratio = (value - 10) / 50;
  const hue = 200 - ratio * 200;
  return 'hsl(' + hue + ', 75%, 55%)';
}

function buildTable(values) {
  const table = document.createElement('table');
  for (let row = 0; row < 10; row++) {
    const tr = document.createElement('tr');
    for (let col = 0; col < 10; col++) {
      const td = document.createElement('td');
      const value = values[row * 10 + col];
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
    document.getElementById('sorted-table').appendChild(buildTable(data.sorted));
    document.getElementById('min-value').textContent = data.min;
  });
