class ComputerEquipment {
  constructor({ id, serial, releaseDate, purpose, name, manufacturerAddress }) {
    this.id = String(id).trim();
    this.serial = String(serial).trim();
    this.releaseDate = releaseDate;
    this.purpose = String(purpose).trim();
    this.name = String(name).trim();
    this.manufacturerAddress = String(manufacturerAddress).trim();
  }
}

class EquipmentCatalog {
  constructor(records = []) {
    this.records = new Map();
    this.serialNumbers = new Set();
    records.forEach(record => this.add(record));
  }

  add(values) {
    const item = new ComputerEquipment(values);
    if (!item.id || !item.serial || !item.releaseDate || !item.purpose || !item.name || !item.manufacturerAddress) {
      throw new Error('Заполните все поля формы.');
    }
    if (this.records.has(item.id)) throw new Error('Запись с таким ID уже существует.');
    if (this.serialNumbers.has(item.serial)) throw new Error('Серийный номер уже зарегистрирован.');
    this.records.set(item.id, item);
    this.serialNumbers.add(item.serial);
    return item;
  }

  remove(id) {
    const item = this.records.get(String(id));
    if (!item) return false;
    this.records.delete(String(id));
    this.serialNumbers.delete(item.serial);
    return true;
  }

  get(id) {
    return this.records.get(String(id)) || null;
  }

  all() {
    return Array.from(this.records.values());
  }
}

if (typeof module !== 'undefined') module.exports = { ComputerEquipment, EquipmentCatalog };
