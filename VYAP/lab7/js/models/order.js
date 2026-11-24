const mongoose = require('mongoose');

const orderSchema = new mongoose.Schema({
  orderNumber: { type: String, required: true, trim: true, unique: true },
  carBrand: { type: String, required: true, trim: true },
  orderedAt: { type: Date, required: true },
  price: { type: Number, required: true, min: 0 }
}, { timestamps: true, versionKey: false });

module.exports = mongoose.model('Order', orderSchema);
