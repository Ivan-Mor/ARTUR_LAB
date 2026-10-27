'use strict';
const colors = new Set([           // цвета блоков
  '#c0392b', '#d35400', '#e67e22', '#16a085',
  '#27ae60', '#2980b9', '#8e44ad', '#34495e'
]);

const titles = new Set([           // заголовки блоков
  'Марс', 'Венера', 'Юпитер', 'Сатурн',
  'Уран', 'Нептун', 'Меркурий', 'Земля'
]);

const texts = new Set([            // содержимое параграфов (3-5 слов)
  'Марс богат железным песком',
  'Венера раскалена сильнее всех',
  'Юпитер огромен и газовый',
  'Сатурн славится яркими кольцами',
  'Уран вращается вокруг оси боком',
  'Нептун дует ледяными ветрами',
  'Меркурий мчится вокруг Солнца',
  'Земля укрывает жизнь океанами'
]);

const blocksMap = new Map();       // номер блока по порядку -> DOM-элемент

const $ = id => document.getElementById(id);

function getItem(set, index) {
  let i = 0;
  for (const value of set.values()) {
    if (i === index) return value;
    i += 1;
  }
  return undefined;
}

function describe(value) {
  if (typeof value === 'string') return value;
  if (value instanceof Element) return value.textContent;   // блок на странице
  return String(value);
}

function refreshMap(parent) {
  blocksMap.clear();
  let number = 1;
  for (const block of parent.children) {
    blocksMap.set(number, block);
    number += 1;
  }
}

class BlockManager {
  constructor(name = 'Родительский блок', color = '#2c3e50', width = 620) {
    this.name = name;
    this.color = color;
    this.width = width;
    this.parentElem = null;
    this.extra = new Map();        // дополнительные свойства класса
  }

  addExtra(key, value) {
    this.extra.set(key, value);
  }


  makeBlock(title, tag, text, color, width) {
    const block = document.createElement('div');
    block.className = 'block';
    block.style.background = color;      // CSS-свойство задаётся из JS
    block.style.width = width + 'px';

    const span = document.createElement('span');
    const heading = document.createElement(tag);
    heading.appendChild(document.createTextNode(title));
    span.appendChild(heading);

    const paragraph = document.createElement('p');
    paragraph.appendChild(document.createTextNode(text));

    block.appendChild(span);
    block.appendChild(paragraph);
    return block;
  }

  placeParent(areaId) {
    const parent = document.createElement('div');
    parent.className = 'parent-block';
    parent.style.background = this.color;
    parent.style.width = this.width + 'px';
    $(areaId).appendChild(parent);
    this.parentElem = parent;
    return parent;
  }

  placeInitialBlocks() {
    const first  = this.makeBlock(getItem(titles, 0), 'h2', getItem(texts, 0), getItem(colors, 0), 260);
    const second = this.makeBlock(getItem(titles, 1), 'h3', getItem(texts, 1), getItem(colors, 3), 260);
    this.parentElem.appendChild(first);
    this.parentElem.appendChild(second);
    refreshMap(this.parentElem);
  }

  fillSelect(select, collection) {
    select.replaceChildren();
    collection.forEach(value => {              // forEach — метод Set
      const option = document.createElement('option');
      option.textContent = value;
      option.value = value;
      select.appendChild(option);
    });
  }

  fillPositionSelect(select) {
    select.replaceChildren();
    for (let i = 1; i <= blocksMap.size + 1; i += 1) {   // size — свойство Map
      const option = document.createElement('option');
      option.value = String(i);
      option.textContent = (i === blocksMap.size + 1)
        ? `позиция ${i} — в конец`
        : `позиция ${i}`;
      select.appendChild(option);
    }
  }

  showAsList(panel) {
    const list = document.createElement('ul');

    const addGroup = (name, collection, isMap) => {
      const group = document.createElement('li');
      group.textContent = name + ':';
      const inner = document.createElement('ul');
      collection.forEach((value, key) => {
        const item = document.createElement('li');
        item.textContent = isMap ? `${key} → ${describe(value)}` : describe(value);
        inner.appendChild(item);
      });
      group.appendChild(inner);
      list.appendChild(group);
    };

    addGroup('Цвета (Set)', colors, false);
    addGroup('Заголовки (Set)', titles, false);
    addGroup('Тексты (Set)', texts, false);
    addGroup('Блоки на странице (Map)', blocksMap, true);
    addGroup('Доп. свойства класса (Map)', this.extra, true);

    panel.replaceChildren(list);
  }

  showAsText(panel) {
    panel.textContent = [
      `Цвета (Set): ${[...colors].join(', ')}`,
      `Заголовки (Set): ${[...titles].join(', ')}`,
      `Тексты (Set): ${[...texts].join(' | ')}`,
      `Блоков на странице (Map.size): ${blocksMap.size}`,
      `Доп. свойства класса: ${[...this.extra].map(pair => pair.join(' = ')).join('; ')}`
    ].join('\n');
  }
}


class BlockEditor extends BlockManager {
  constructor(name = 'Редактор блоков', color = '#1f2a44', width = 640) {
    super(name, color, width);           // вызов конструктора родительского класса
  }

  insertBlock(position, title, tag, text, color, width) {
    const block = this.makeBlock(title, tag, text, color, width);
    const blocks = [...blocksMap.values()];        // values() — метод Map

    if (position >= 1 && position <= blocks.length) {
      this.parentElem.insertBefore(block, blocks[position - 1]);
    } else {
      this.parentElem.appendChild(block);
    }
    refreshMap(this.parentElem);
    return block;
  }
}

// ---------- Запуск страницы ----------

const manager = new BlockManager();                                      // конструктор без аргументов
const editor  = new BlockEditor('Космический редактор', '#1f2a44', 640); // конструктор с аргументами

manager.addExtra('тема оформления', 'космос');   // дополнительные свойства классов
editor.addExtra('режим работы', 'вставка блоков');

function bindEvents() {
  $('btn-insert').addEventListener('click', () => {
    editor.insertBlock(
      Number($('select-position').value),
      $('select-title').value,
      $('select-tag').value,
      $('select-text').value,
      $('select-color').value,
      Number($('select-width').value)
    );
    manager.fillPositionSelect($('select-position'));
  });

  $('btn-show-list').addEventListener('click', () => manager.showAsList($('info-panel')));
  $('btn-show-text').addEventListener('click', () => manager.showAsText($('info-panel')));
}

function init() {
  manager.placeParent('work-area');
  editor.parentElem = manager.parentElem;   // вставки идут в тот же родительский блок
  manager.placeInitialBlocks();

  manager.fillSelect($('select-color'), colors);
  manager.fillSelect($('select-title'), titles);
  manager.fillSelect($('select-text'), texts);
  manager.fillPositionSelect($('select-position'));

  bindEvents();
}

document.addEventListener('DOMContentLoaded', init);
