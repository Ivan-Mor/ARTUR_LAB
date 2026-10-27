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

function findMin(array) {
  return Math.min(...array);
}

const original = generateArray(100, 10, 60);
const sorted = sortAscending(original);
const min = findMin(original);

module.exports = { generateArray, sortAscending, findMin, original, sorted, min };
