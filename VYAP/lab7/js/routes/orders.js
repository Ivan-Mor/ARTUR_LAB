const express = require('express');
const controller = require('../controllers/orders');

const router = express.Router();
router.get('/price-range', controller.brandsInPriceRange);
router.get('/', controller.list);
router.post('/', controller.create);
router.get('/:number', controller.getByNumber);
router.put('/:number', controller.update);
router.delete('/:number', controller.remove);
router.post('/:number/replace', controller.replaceByNumber);

module.exports = router;
