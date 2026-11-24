const Order = require('../models/order');

async function list(request, response, next) {
  try {
    response.json(await Order.find().sort({ orderedAt: -1, orderNumber: 1 }).lean());
  } catch (error) {
    next(error);
  }
}

async function create(request, response, next) {
  try {
    const order = await Order.create(request.body);
    response.status(201).json(order);
  } catch (error) {
    next(error);
  }
}

async function getByNumber(request, response, next) {
  try {
    const order = await Order.findOne({ orderNumber: request.params.number }).lean();
    if (!order) return response.status(404).json({ message: 'Заказ не найден.' });
    response.json(order);
  } catch (error) {
    next(error);
  }
}

async function update(request, response, next) {
  try {
    const order = await Order.findOneAndUpdate({ orderNumber: request.params.number }, request.body, { new: true, runValidators: true });
    if (!order) return response.status(404).json({ message: 'Заказ не найден.' });
    response.json(order);
  } catch (error) {
    next(error);
  }
}

async function remove(request, response, next) {
  try {
    const result = await Order.deleteOne({ orderNumber: request.params.number });
    if (!result.deletedCount) return response.status(404).json({ message: 'Заказ не найден.' });
    response.json({ message: `Заказ ${request.params.number} удалён.` });
  } catch (error) {
    next(error);
  }
}

async function brandsInPriceRange(request, response, next) {
  try {
    const min = Number(request.query.min);
    const max = Number(request.query.max);
    if (!Number.isFinite(min) || !Number.isFinite(max) || min < 0 || max < min) {
      return response.status(400).json({ message: 'Укажите корректный диапазон цены.' });
    }
    const orders = await Order.find({ price: { $gte: min, $lte: max } }).select('orderNumber carBrand orderedAt price -_id').sort({ price: 1 }).lean();
    response.json(orders);
  } catch (error) {
    next(error);
  }
}

async function replaceByNumber(request, response, next) {
  try {
    const oldNumber = String(request.params.number).trim();
    const replacement = new Order(request.body);
    await replacement.validate();
    const result = await Order.replaceOne({ orderNumber: oldNumber }, replacement.toObject(), { upsert: true, runValidators: true });
    response.json({ message: result.matchedCount ? `Заказ ${oldNumber} заменён.` : `Добавлен новый заказ вместо ${oldNumber}.`, order: replacement });
  } catch (error) {
    next(error);
  }
}

module.exports = { list, create, getByNumber, update, remove, brandsInPriceRange, replaceByNumber };
