<script setup>
import { ref, onMounted } from 'vue'

const items = ref([])
const selectedItemIds = ref([])
const newItemName = ref('')
const voterName = ref('')

const API_BASE = 'http://localhost:8080/api'

const fetchItems = async () => {
  try {
    const response = await fetch(`${API_BASE}/items`)
    if (!response.ok) throw new Error('Failed to fetch items')
    items.value = await response.json()
  } catch (error) {
    alert('無法連線到後端 API，請確認後端服務是否已啟動。')
  }
}

const handleAddItem = async () => {
  if (!newItemName.value.trim()) {
    alert('請輸入想要新增的項目名稱！')
    return
  }

  try {
    const response = await fetch(`${API_BASE}/items`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ itemName: newItemName.value })
    })
    if (!response.ok) throw new Error('Failed to add item')
    newItemName.value = ''
    await fetchItems()
  } catch (error) {
    alert('項目新增失敗')
  }
}

const handleVote = async () => {
  if (!voterName.value.trim()) {
    alert('請先輸入投票人姓名。')
    return
  }
  if (selectedItemIds.value.length === 0) {
    alert('請至少選擇一個投票項目。')
    return
  }

  try {
    const response = await fetch(`${API_BASE}/votes`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        voterName: voterName.value,
        itemIds: selectedItemIds.value
      })
    })
    if (!response.ok) throw new Error('Failed to vote')
    selectedItemIds.value = []
    await fetchItems()
  } catch (error) {
    alert('投票失敗')
  }
}

const handleDeleteItem = async (itemId) => {
  if (!confirm('確定要刪除這個投票項目嗎？相關的投票紀錄也會一併消失喔！')) return

  try {
    const response = await fetch(`${API_BASE}/items/${itemId}`, {
      method: 'DELETE'
    })
    if (!response.ok) throw new Error('Failed to delete item')
    selectedItemIds.value = selectedItemIds.value.filter(id => id !== itemId)
    await fetchItems()
  } catch (error) {
    alert('項目刪除失敗')
  }
}

onMounted(() => {
  fetchItems()
})
</script>

<template>
  <div class="voting-container">
    <h2>線上投票系統</h2>
    
    <div class="voter-section">
      <label>投票人姓名：</label>
      <input v-model="voterName" type="text" placeholder="請先輸入您的名字再開始投票..." />
    </div>

    <table class="voting-table">
      <thead>
        <tr>
          <th>項目 ID</th>
          <th>投票項目名稱</th>
          <th>目前累計票數</th>
          <th>投票選擇</th>
          <th>操作動作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in items" :key="item.itemId">
          <td>{{ item.itemId }}</td>
          <td class="item-name">{{ item.itemName }}</td>
          <td class="vote-count">{{ item.voteCount }} 票</td>
          <td>
            <input
              v-model="selectedItemIds"
              :value="item.itemId"
              type="checkbox"
              class="vote-checkbox"
              aria-label="選擇投票項目"
            />
          </td>
          <td>
            <button @click="handleDeleteItem(item.itemId)" class="btn-delete">刪除</button>
          </td>
        </tr>
        <tr v-if="items.length === 0">
          <td colspan="5" style="text-align: center; color: #888;">目前沒有任何投票項目，請在下方新增。</td>
        </tr>
      </tbody>
    </table>

    <div class="vote-actions">
      <button @click="handleVote" class="btn-vote">送出投票</button>
      <span>已選擇 {{ selectedItemIds.length }} 個項目</span>
    </div>

    <div class="add-section">
      <h3>新增候選投票項目</h3>
      <div class="input-group">
        <input v-model="newItemName" type="text" placeholder="請輸入新項目名稱（如：鍵盤、螢幕）..." />
        <button @click="handleAddItem" class="btn-add">確認新增</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.voting-container {
  max-width: 800px;
  margin: 40px auto;
  padding: 30px;
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.08);
  font-family: 'Helvetica Neue', Arial, sans-serif;
  color: #333;
}
h2 {
  text-align: center;
  color: #13794c;
  margin-bottom: 30px;
}
.voter-section {
  background: #f4fbf7;
  padding: 15px;
  border-radius: 8px;
  border-left: 5px solid #13794c;
  margin-bottom: 25px;
}
.voter-section input {
  padding: 8px 12px;
  width: 250px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.voting-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 30px;
}
.voting-table th, .voting-table td {
  border: 1px solid #e0e0e0;
  padding: 12px 15px;
  text-align: left;
}
.voting-table th {
  background-color: #f5f5f5;
  color: #555;
}
.item-name {
  font-weight: bold;
}
.vote-count {
  color: #e67e22;
  font-weight: bold;
}
.vote-checkbox {
  width: 18px;
  height: 18px;
  cursor: pointer;
}
button {
  padding: 6px 12px;
  margin-right: 8px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
}
.btn-vote { background-color: #2ecc71; color: white; }
.btn-vote:hover { background-color: #27ae60; }
.btn-delete { background-color: #e74c3c; color: white; }
.btn-delete:hover { background-color: #c0392b; }
.btn-add { background-color: #13794c; color: white; padding: 10px 20px; }
.btn-add:hover { background-color: #0f623d; }
.vote-actions {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 30px;
}
.vote-actions span {
  color: #666;
}
.add-section {
  background: #f9f9f9;
  padding: 20px;
  border-radius: 8px;
}
.input-group {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}
.input-group input {
  flex: 1;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
</style>
