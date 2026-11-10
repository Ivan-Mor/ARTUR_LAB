// Клиентский сценарий: асинхронная подгрузка данных (промисы, async/await — ECMA6)

// Пути к файлам для каждого артиста (json, txt — папка data; фото и видео — resource)
const resources = {
  tchaikovsky: {
    json: '/data/tchaikovsky.json',
    txt: '/data/tchaikovsky.txt',
    photo: '/resource/tchaikovsky.svg',
    video: '/resource/tchaikovsky.mp4'
  },
  rachmaninov: {
    json: '/data/rachmaninov.json',
    txt: '/data/rachmaninov.txt',
    photo: '/resource/rachmaninov.svg',
    video: '/resource/rachmaninov.mp4'
  },
  glinka: {
    json: '/data/glinka.json',
    txt: '/data/glinka.txt',
    photo: '/resource/glinka.svg',
    video: '/resource/glinka.mp4'
  }
};

const infoBlock = document.getElementById('artist-info');
const textBlock = document.getElementById('artist-text');
const mediaBlock = document.getElementById('artist-media');

function selectedKey() {
  return document.getElementById('artist-select').value;
}

// Асинхронный запрос файла с проверкой ответа (возвращает промис)
async function fetchResource(url) {
  const response = await fetch(url);
  if (!response.ok) {
    throw new Error('HTTP ' + response.status + ' при запросе ' + url);
  }
  return response;
}

// Кнопка 1: данные о выбранном артисте из JSON-файла
async function loadArtistJson() {
  const key = selectedKey();
  infoBlock.innerHTML = '<p class="placeholder">Загрузка данных…</p>';
  try {
    const response = await fetchResource(resources[key].json);
    const artist = await response.json(); // промис разбора JSON
    renderArtist(artist);
  } catch (err) {
    infoBlock.innerHTML = '<p class="error">Ошибка загрузки: ' + err.message + '</p>';
  }
}

// Отображение данных из JSON на странице
function renderArtist(artist) {
  infoBlock.innerHTML = '';

  const name = document.createElement('p');
  name.className = 'artist-name';
  name.textContent = artist.name;
  infoBlock.appendChild(name);

  const facts = document.createElement('dl');
  facts.className = 'artist-facts';
  addFact(facts, 'Годы жизни', artist.years);
  addFact(facts, 'Страна', artist.country);
  addFact(facts, 'Жанр', artist.genre);
  infoBlock.appendChild(facts);

  const worksTitle = document.createElement('p');
  worksTitle.className = 'facts-subtitle';
  worksTitle.textContent = 'Известные произведения:';
  infoBlock.appendChild(worksTitle);

  const works = document.createElement('ul');
  works.className = 'works-list';
  artist.famousWorks.forEach(function (work) {
    const li = document.createElement('li');
    li.textContent = work;
    works.appendChild(li);
  });
  infoBlock.appendChild(works);

  const fact = document.createElement('p');
  fact.className = 'artist-fact';
  fact.textContent = artist.fact;
  infoBlock.appendChild(fact);
}

function addFact(facts, title, value) {
  const dt = document.createElement('dt');
  dt.textContent = title;
  const dd = document.createElement('dd');
  dd.textContent = value;
  facts.appendChild(dt);
  facts.appendChild(dd);
}

// Кнопка 2: текст о артисте из текстового файла в заранее заданный блок
async function loadArtistText() {
  const key = selectedKey();
  textBlock.innerHTML = '<p class="placeholder">Загрузка текста…</p>';
  try {
    const response = await fetchResource(resources[key].txt);
    const text = await response.text(); // промис чтения текста
    textBlock.textContent = text;
  } catch (err) {
    textBlock.innerHTML = '<p class="error">Ошибка загрузки: ' + err.message + '</p>';
  }
}

// Кнопка 3: фото и видеоролик об артисте
async function loadArtistMedia() {
  const key = selectedKey();
  const { photo, video } = resources[key];

  mediaBlock.innerHTML = '';

  const row = document.createElement('div');
  row.className = 'media-row';

  function makeErrorNote(message) {
    const p = document.createElement('p');
    p.className = 'error';
    p.textContent = message;
    return p;
  }

  // Фото
  const photoFigure = document.createElement('figure');
  const img = document.createElement('img');
  img.className = 'artist-photo';
  img.alt = 'Фото артиста';
  img.src = photo;
  img.addEventListener('error', function () {
    img.replaceWith(makeErrorNote('Не удалось загрузить фото'));
  });
  photoFigure.appendChild(img);
  const photoCaption = document.createElement('figcaption');
  photoCaption.className = 'media-caption';
  photoCaption.textContent = 'Фото';
  photoFigure.appendChild(photoCaption);
  row.appendChild(photoFigure);

  // Видео
  const videoFigure = document.createElement('figure');
  const videoEl = document.createElement('video');
  videoEl.className = 'artist-video';
  videoEl.controls = true;
  videoEl.preload = 'auto';
  videoEl.src = video;
  videoEl.addEventListener('error', function () {
    videoEl.replaceWith(makeErrorNote('Не удалось загрузить видео'));
  });
  videoFigure.appendChild(videoEl);
  const videoCaption = document.createElement('figcaption');
  videoCaption.className = 'media-caption';
  videoCaption.textContent = 'Видеоролик';
  videoFigure.appendChild(videoCaption);
  row.appendChild(videoFigure);

  mediaBlock.appendChild(row);
}

document.getElementById('load-json').addEventListener('click', loadArtistJson);
document.getElementById('load-text').addEventListener('click', loadArtistText);
document.getElementById('load-media').addEventListener('click', loadArtistMedia);
