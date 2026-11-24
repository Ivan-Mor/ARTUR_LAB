(() => {
  const status = document.querySelector('#connection-status');
  const dot = document.querySelector('#connection-dot');
  const toast = document.querySelector('#toast');
  const ordersList = document.querySelector('#orders-list');
  let selectedNumber = '';

  async function api(url, options = {}) {
    const response = await fetch(url, { ...options, headers: { 'Content-Type': 'application/json', ...(options.headers || {}) } });
    const payload = await response.json();
    if (!response.ok) throw new Error(payload.message || 'Запрос не выполнен.');
    return payload;
  }

  function notify(message, isError = false) {
    toast.textContent = message;
    toast.classList.toggle('error', isError);
    toast.classList.add('visible');
    window.setTimeout(() => toast.classList.remove('visible'), 3600);
  }

  function dateValue(value) {
    const date = new Date(value);
    return new Intl.DateTimeFormat('ru-BY', { day: '2-digit', month: 'short', year: 'numeric', hour: '2-digit', minute: '2-digit' }).format(date);
  }

  function renderOrders(orders) {
    if (!orders.length) {
      ordersList.innerHTML = '<div class="placeholder">Записей пока нет. Добавьте заказ в форме управления.</div>';
      return;
    }
    ordersList.innerHTML = orders.map(order => `<article class="order-row"><div class="order-mark"><span>№</span>${escapeHtml(order.orderNumber)}</div><div class="order-car"><strong>${escapeHtml(order.carBrand)}</strong><small>${dateValue(order.orderedAt)}</small></div><div class="order-price">${Number(order.price).toFixed(2)} <small>BYN</small></div></article>`).join('');
  }

  function escapeHtml(value) {
    return String(value).replace(/[&<>"']/g, character => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' })[character]);
  }

  async function refreshOrders() {
    try {
      const orders = await api('/api/orders');
      renderOrders(orders);
      status.textContent = 'MONGODB ПОДКЛЮЧЕНА';
      dot.classList.add('online');
    } catch (error) {
      status.textContent = 'БАЗА НЕ ДОСТУПНА';
      dot.classList.remove('online');
      renderOrders([]);
      notify(error.message, true);
    }
  }

  document.querySelector('#range-form').addEventListener('submit', async event => {
    event.preventDefault();
    const values = new FormData(event.currentTarget);
    try {
      const result = await api(`/api/orders/price-range?min=${encodeURIComponent(values.get('min'))}&max=${encodeURIComponent(values.get('max'))}`);
      document.querySelector('#range-results').innerHTML = result.length
        ? result.map(order => `<div class="range-result"><span>${escapeHtml(order.carBrand)}</span><strong>${Number(order.price).toFixed(2)} BYN</strong></div>`).join('')
        : '<div class="placeholder">В указанном диапазоне заказов нет</div>';
      notify(`Найдено заказов: ${result.length}.`);
    } catch (error) {
      notify(error.message, true);
    }
  });

  document.querySelector('#replace-form').addEventListener('submit', async event => {
    event.preventDefault();
    const values = Object.fromEntries(new FormData(event.currentTarget));
    const oldNumber = values.oldNumber;
    delete values.oldNumber;
    values.price = Number(values.price);
    values.orderedAt = new Date(values.orderedAt).toISOString();
    try {
      const result = await api(`/api/orders/${encodeURIComponent(oldNumber)}/replace`, { method: 'POST', body: JSON.stringify(values) });
      notify(result.message);
      event.currentTarget.reset();
      await refreshOrders();
    } catch (error) {
      notify(error.message, true);
    }
  });

  document.querySelector('#refresh-orders').addEventListener('click', refreshOrders);

  document.querySelector('#lookup-form').addEventListener('submit', async event => {
    event.preventDefault();
    const number = new FormData(event.currentTarget).get('orderNumber').trim();
    try {
      const order = await api(`/api/orders/${encodeURIComponent(number)}`);
      selectedNumber = order.orderNumber;
      const form = document.querySelector('#edit-form');
      form.elements.carBrand.value = order.carBrand;
      form.elements.orderedAt.value = new Date(order.orderedAt).toISOString().slice(0, 16);
      form.elements.price.value = order.price;
      form.hidden = false;
      notify(`Заказ ${number} найден и готов к редактированию.`);
    } catch (error) {
      selectedNumber = '';
      document.querySelector('#edit-form').hidden = true;
      notify(error.message, true);
    }
  });

  document.querySelector('#edit-form').addEventListener('submit', async event => {
    event.preventDefault();
    const values = Object.fromEntries(new FormData(event.currentTarget));
    values.price = Number(values.price);
    values.orderedAt = new Date(values.orderedAt).toISOString();
    try {
      await api(`/api/orders/${encodeURIComponent(selectedNumber)}`, { method: 'PUT', body: JSON.stringify(values) });
      notify(`Заказ ${selectedNumber} обновлён.`);
      await refreshOrders();
    } catch (error) {
      notify(error.message, true);
    }
  });

  document.querySelector('#create-form').addEventListener('submit', async event => {
    event.preventDefault();
    const values = Object.fromEntries(new FormData(event.currentTarget));
    values.price = Number(values.price);
    values.orderedAt = new Date(values.orderedAt).toISOString();
    try {
      await api('/api/orders', { method: 'POST', body: JSON.stringify(values) });
      event.currentTarget.reset();
      notify('Новый заказ добавлен в базу.');
      await refreshOrders();
    } catch (error) {
      notify(error.message, true);
    }
  });

  document.querySelector('#delete-order').addEventListener('click', async () => {
    if (!selectedNumber) return notify('Сначала найдите заказ по номеру.', true);
    try {
      const result = await api(`/api/orders/${encodeURIComponent(selectedNumber)}`, { method: 'DELETE' });
      notify(result.message);
      selectedNumber = '';
      document.querySelector('#edit-form').hidden = true;
      await refreshOrders();
    } catch (error) {
      notify(error.message, true);
    }
  });

  refreshOrders();
})();
