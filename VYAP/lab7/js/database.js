const mongoose = require('mongoose');
const Order = require('./models/order');

async function connectDatabase() {
  const uri = process.env.MONGODB_URI || 'mongodb://127.0.0.1:27017/vyap_lab7';
  await mongoose.connect(uri, { serverSelectionTimeoutMS: 4000 });
  if (process.env.SEED_DEMO === 'false') return;
  const count = await Order.countDocuments();
  if (count > 0) return;
  await Order.insertMany([
    { orderNumber: 'AR-2401', carBrand: 'Volkswagen Passat', orderedAt: new Date('2026-04-12T10:00:00Z'), price: 125 },
    { orderNumber: 'AR-2402', carBrand: 'Skoda Octavia', orderedAt: new Date('2026-04-13T08:30:00Z'), price: 82 },
    { orderNumber: 'AR-2403', carBrand: 'Toyota Corolla', orderedAt: new Date('2026-04-14T15:45:00Z'), price: 96 },
    { orderNumber: 'AR-2404', carBrand: 'BMW 3 Series', orderedAt: new Date('2026-04-15T12:00:00Z'), price: 210 }
  ]);
}

module.exports = connectDatabase;
