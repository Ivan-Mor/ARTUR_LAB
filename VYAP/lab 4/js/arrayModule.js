// Модуль бизнес-логики: генерация массива, сортировка, поиск максимума

function generateArray(count, min, max) {
  const array = [];
  for (let i = 0; i < count; i++) {
    array.push(Math.floor(Math.random() * (max - min + 1)) + min);
  }
  return array;
}

function sortAscending(array) {
  return [...array].sort((a, b) => a - b);
}

function findMax(array) {
  return Math.max(...array);
}

// Вариант 2: массив из 80 элементов со случайными значениями от 1 до 10
const original = generateArray(80, 1, 10);
const sorted = sortAscending(original);
const max = findMax(original);

module.exports = { generateArray, sortAscending, findMax, original, sorted, max };
