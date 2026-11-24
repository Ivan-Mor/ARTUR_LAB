(() => {
  const storageKey = 'vyap-lab6-equipment';
  const seed = [
    { id: '1001', serial: 'SN-AX1200-24', releaseDate: '2024-02-15', purpose: 'Рабочая станция', name: 'Astra Workstation X5', manufacturerAddress: 'Минск, ул. Сурганова, 57Б' },
    { id: '1002', serial: 'SN-NB7400-23', releaseDate: '2023-11-03', purpose: 'Мобильное обучение', name: 'NovaBook Air 14', manufacturerAddress: 'Брест, Варшавское шоссе, 11' },
    { id: '1003', serial: 'SN-SRV900-25', releaseDate: '2025-01-20', purpose: 'Хранение данных', name: 'DataCore Server R9', manufacturerAddress: 'Гродно, ул. Горького, 91' }
  ];
  const template = Handlebars.compile(document.querySelector('#equipment-template').innerHTML);
  const form = document.querySelector('#equipment-form');
  const list = document.querySelector('#equipment-list');
  const selection = document.querySelector('#record-id');
  const lookup = document.querySelector('#lookup-id');
  const status = document.querySelector('#status');

  function loadCatalog() {
    let data;
    try {
      data = JSON.parse(localStorage.getItem(storageKey));
    } catch {
      data = null;
    }
    return new EquipmentCatalog(Array.isArray(data) ? data : seed);
  }

  const catalog = loadCatalog();

  function persist() {
    localStorage.setItem(storageKey, JSON.stringify(catalog.all()));
  }

  function render(records = catalog.all()) {
    list.innerHTML = template({ records, hasRecords: records.length > 0 });
    selection.innerHTML = '<option value="">Выберите ID</option>' + catalog.all().map(item => `<option value="${Handlebars.escapeExpression(item.id)}">${Handlebars.escapeExpression(item.id)} · ${Handlebars.escapeExpression(item.name)}</option>`).join('');
  }

  function showMessage(message, type = 'success') {
    status.textContent = message;
    status.dataset.type = type;
  }

  form.addEventListener('submit', event => {
    event.preventDefault();
    const values = Object.fromEntries(new FormData(form));
    try {
      catalog.add(values);
      persist();
      render();
      form.reset();
      showMessage('Запись добавлена в каталог.');
    } catch (error) {
      showMessage(error.message, 'error');
    }
  });

  document.querySelector('#clear-form').addEventListener('click', () => {
    form.reset();
    showMessage('Поля формы очищены.');
  });

  document.querySelector('#delete-record').addEventListener('click', () => {
    if (!selection.value) return showMessage('Выберите ID записи для удаления.', 'error');
    if (!catalog.remove(selection.value)) return showMessage('Запись не найдена.', 'error');
    persist();
    render();
    showMessage(`Запись ${selection.value} удалена.`);
  });

  document.querySelector('#show-all').addEventListener('click', () => {
    render();
    showMessage(`В каталоге записей: ${catalog.all().length}.`);
  });

  document.querySelector('#show-one').addEventListener('click', () => {
    const id = lookup.value.trim();
    if (!id) return showMessage('Введите ID записи.', 'error');
    const item = catalog.get(id);
    render(item ? [item] : []);
    showMessage(item ? `Показана запись ${id}.` : `Запись ${id} не найдена.`, item ? 'success' : 'error');
  });

  render();
})();
