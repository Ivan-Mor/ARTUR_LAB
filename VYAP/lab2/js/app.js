'use strict';

const STORAGE_KEY = 'lab2-polyclinic-records';
const PROPS_KEY   = 'lab2-polyclinic-props';

class PatientRecord {
  constructor(id, fullName, address, phone, inpatient) {
    this.id        = id;
    this.fullName  = fullName;
    this.address   = address;
    this.phone     = phone;
    this.inpatient = Boolean(inpatient);
  }
}

class PatientRegistry {
  constructor() {
    this.records    = [];
    this.extraProps = [];
  }

  save() {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(this.records));
    localStorage.setItem(PROPS_KEY,   JSON.stringify(this.extraProps));
  }

  load() {
    const rawRecords = localStorage.getItem(STORAGE_KEY);
    const rawProps   = localStorage.getItem(PROPS_KEY);
    this.records = rawRecords
      ? JSON.parse(rawRecords).map(r => Object.assign(new PatientRecord(), r))
      : [];
    this.extraProps = rawProps ? JSON.parse(rawProps) : [];
  }

  nextId() {
    return this.records.reduce((max, r) => Math.max(max, r.id), 0) + 1;
  }

  addRecord(fields) {
    const record = new PatientRecord(
      Number.isInteger(fields.id) ? fields.id : this.nextId(),
      fields.fullName,
      fields.address,
      fields.phone,
      fields.inpatient
    );
    this.extraProps.forEach(prop => { record[prop] = fields[prop] || ''; });
    this.records.push(record);
    this.save();
    return record;
  }

  findById(id) { return this.records.find(r => r.id === Number(id)); }

  hasId(id)    { return this.records.some(r => r.id === Number(id)); }

  removeRecord(id) {
    const before = this.records.length;
    this.records = this.records.filter(r => r.id !== Number(id));
    const removed = this.records.length !== before;
    if (removed) this.save();
    return removed;
  }

  getInpatients() {
    return this.records.filter(r => r.inpatient).map(r => r.fullName);
  }

  hasProperty(name) { return this.extraProps.includes(name); }

  addProperty(name, value, recordId) {
    if (!this.hasProperty(name)) this.extraProps.push(name);
    const target = this.findById(recordId);
    if (target) target[name] = value;
    this.save();
    return target;
  }

  addInputElementForProperty(name, container) {
    const inputId = 'dyn-' + name.replace(/\s+/g, '-');
    if (document.getElementById(inputId)) return;

    const label = document.createElement('label');
    label.className = 'field';
    const span = document.createElement('span');
    span.textContent = name;
    const input = document.createElement('input');
    input.type = 'text';
    input.id = inputId;
    input.dataset.prop = name;
    input.placeholder = 'значение для новой записи';
    label.append(span, input);
    container.appendChild(label);
  }

  removeProperty(name) {
    this.extraProps = this.extraProps.filter(p => p !== name);
    this.records    = this.records.map(r => { delete r[name]; return r; });
    this.save();
  }

  showAll(tableEl) {
    const columns = ['ID', 'ФИО', 'Адрес', 'Телефон', 'Стационарная помощь', ...this.extraProps];

    const thead = document.createElement('thead');
    const headRow = document.createElement('tr');
    columns.forEach(title => {
      const th = document.createElement('th');
      th.textContent = title;
      headRow.appendChild(th);
    });
    thead.appendChild(headRow);

    const tbody = document.createElement('tbody');
    this.records.forEach(record => {
      const tr = document.createElement('tr');
      const cells = [
        record.id,
        record.fullName,
        record.address,
        record.phone,
        record.inpatient ? 'да' : 'нет',
        ...this.extraProps.map(prop => record[prop] || '—')
      ];
      cells.forEach(value => {
        const td = document.createElement('td');
        td.textContent = value;
        if (value === 'да') td.classList.add('cell-flag');
        tr.appendChild(td);
      });
      tbody.appendChild(tr);
    });

    tableEl.replaceChildren(thead, tbody);
  }
}

const registry = new PatientRegistry();
const $ = id => document.getElementById(id);

function seedData() {
  registry.records = [
    new PatientRecord(1, 'Никитина Елена Владимировна',    'г. Минск, ул. Кальварийская, 7',    '+375 (29) 101-20-30', true),
    new PatientRecord(2, 'Ковалёв Дмитрий Александрович',  'г. Минск, пр. Победителей, 88',     '+375 (33) 202-30-40', false),
    new PatientRecord(3, 'Романова Ольга Игоревна',        'г. Минск, ул. Сурганова, 51',       '+375 (25) 303-40-50', true),
    new PatientRecord(4, 'Мороз Виктор Петрович',          'г. Минск, ул. Киселёва, 3',         '+375 (17) 404-50-60', false),
    new PatientRecord(5, 'Савицкая Анна Леонидовна',       'г. Минск, ул. Богдановича, 121',    '+375 (44) 505-60-70', false)
  ];
  registry.save();
}

function refreshAll() {
  refreshIdSelect();
  refreshRemovePropSelect();
  registry.showAll($('records-table'));
}

function refreshIdSelect() {
  const select = $('select-id');
  const previous = select.value;
  const ids = registry.records.map(r => r.id).sort((a, b) => a - b);

  select.replaceChildren(...ids.map(id => new Option(String(id), String(id))));
  if (ids.map(String).includes(previous)) select.value = previous;
}

function refreshRemovePropSelect() {
  const select = $('select-remove-prop');
  select.replaceChildren(
    ...(registry.extraProps.length
      ? registry.extraProps.map(p => new Option(p, p))
      : [new Option('— свойств не добавлено —', '', false, true)])
  );
}

function collectForm() {
  const fields = {
    id:        $('inp-id').value === '' ? null : Number($('inp-id').value),
    fullName:  $('inp-name').value.trim(),
    address:   $('inp-address').value.trim(),
    phone:     $('inp-phone').value.trim(),
    inpatient: $('inp-flag').checked
  };
  document.querySelectorAll('#dynamic-inputs input').forEach(input => {
    fields[input.dataset.prop] = input.value.trim();
  });
  return fields;
}

function clearForm() {
  $('record-form').reset();
  $('inp-id').value = '';
  document.querySelectorAll('#dynamic-inputs input').forEach(i => { i.value = ''; });
}

function showOutput(html) { $('output').innerHTML = html; }

function escapeHtml(text) {
  const div = document.createElement('div');
  div.textContent = text;
  return div.innerHTML;
}

function onAddRecord() {
  const fields = collectForm();

  if (!fields.fullName || !fields.address || !fields.phone) {
    showOutput('<span class="msg-error">Заполните обязательные поля: ФИО, адрес и телефон.</span>');
    return;
  }
  if (fields.id !== null && registry.hasId(fields.id)) {
    showOutput(`<span class="msg-error">Запись с ID ${fields.id} уже существует. Выберите другой ID.</span>`);
    return;
  }
  if (fields.id !== null && fields.id < 1) {
    showOutput('<span class="msg-error">ID должен быть натуральным числом.</span>');
    return;
  }

  const record = registry.addRecord(fields);
  clearForm();
  refreshAll();
  $('select-id').value = String(record.id);
  showOutput(`Запись добавлена: ID ${record.id}, ${escapeHtml(record.fullName)} (${escapeHtml(record.phone)}). Записей в хранилище: ${registry.records.length}.`);
}

function onDeleteRecord() {
  const id = $('select-id').value;
  if (!id) {
    showOutput('<span class="msg-error">В картотеке нет записей для удаления.</span>');
    return;
  }
  if (registry.removeRecord(id)) {
    refreshAll();
    showOutput(`Запись с ID ${escapeHtml(id)} удалена из хранилища (Array и WebStorage).`);
  } else {
    showOutput('<span class="msg-error">Не удалось удалить запись.</span>');
  }
}

function onFindRecord() {
  const id = $('select-id').value || $('inp-id').value;
  if (!id) {
    showOutput('<span class="msg-error">Выберите ID в списке или введите его в форме.</span>');
    return;
  }
  const record = registry.findById(id);
  if (!record) {
    showOutput(`<span class="msg-error">Запись с ID ${escapeHtml(id)} не найдена.</span>`);
    return;
  }
  $('inp-id').value = record.id;
  $('inp-name').value = record.fullName;
  $('inp-address').value = record.address;
  $('inp-phone').value = record.phone;
  $('inp-flag').checked = record.inpatient;
  registry.extraProps.forEach(prop => {
    const input = $('dyn-' + prop.replace(/\s+/g, '-'));
    if (input) input.value = record[prop] || '';
  });
  showOutput(
    `Найдена запись ID ${record.id}: ФИО — ${escapeHtml(record.fullName)}, адрес — ${escapeHtml(record.address)}, ` +
    `телефон — ${escapeHtml(record.phone)}, стационарная помощь — ${record.inpatient ? 'да' : 'нет'}.` +
    (registry.extraProps.length
      ? ' ' + registry.extraProps.map(p => `${p}: ${escapeHtml(record[p] || '—')}`).join('; ') + '.'
      : '')
  );
}

function onShowInpatients() {
  const inpatients = registry.getInpatients();
  if (inpatients.length === 0) {
    showOutput('Пациентов, нуждающихся в стационарной помощи, в картотеке нет.');
    return;
  }
  showOutput(
    `Пациенты, нуждающиеся в стационарной помощи (${inpatients.length} чел.):<ul>` +
    inpatients.map(name => `<li>${escapeHtml(name)}</li>`).join('') +
    '</ul>'
  );
}

function onAddProperty() {
  const propName = $('select-prop').value;
  const value    = $('inp-prop-value').value.trim();
  const recordId = $('select-id').value;

  if (!recordId) {
    showOutput('<span class="msg-error">Выберите ID записи в списке «Все ID записей».</span>');
    return;
  }
  if (!value) {
    showOutput('<span class="msg-error">Введите значение нового свойства.</span>');
    return;
  }

  const target = registry.addProperty(propName, value, recordId);
  registry.addInputElementForProperty(propName, $('dynamic-inputs'));
  refreshAll();
  $('select-id').value = String(target.id);
  $('inp-prop-value').value = '';
  showOutput(`Свойство «${escapeHtml(propName)}» = «${escapeHtml(value)}» добавлено объекту записи ID ${target.id}. Значение выведено в таблице.`);
}

function onRemoveProperty() {
  const propName = $('select-remove-prop').value;
  if (!propName || !registry.hasProperty(propName)) {
    showOutput('<span class="msg-error">Нет добавленных свойств для удаления.</span>');
    return;
  }
  registry.removeProperty(propName);
  const input = $('dyn-' + propName.replace(/\s+/g, '-'));
  if (input) input.closest('label').remove();
  refreshAll();
  showOutput(`Свойство «${escapeHtml(propName)}» удалено из всех хранилищ и из объектов записей.`);
}

function bindEvents() {
  $('btn-add').addEventListener('click', onAddRecord);
  $('btn-clear').addEventListener('click', clearForm);
  $('btn-find').addEventListener('click', onFindRecord);
  $('btn-delete').addEventListener('click', onDeleteRecord);
  $('btn-inpatients').addEventListener('click', onShowInpatients);
  $('btn-add-prop').addEventListener('click', onAddProperty);
  $('btn-remove-prop').addEventListener('click', onRemoveProperty);
}

function init() {
  registry.load();
  if (!localStorage.getItem(STORAGE_KEY)) seedData();
  registry.extraProps.forEach(p => registry.addInputElementForProperty(p, $('dynamic-inputs')));
  bindEvents();
  refreshAll();
}

document.addEventListener('DOMContentLoaded', init);
