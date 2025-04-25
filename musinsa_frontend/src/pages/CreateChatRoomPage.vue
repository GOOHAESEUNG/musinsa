<template>
  <div class="create-chat-room-page">
    <h1>채팅방 생성</h1>
    <form @submit.prevent="submitForm">
      <label>채팅방 이름:</label>
      <input v-model="form.roomName" type="text" required />

      <label>채팅방 유형:</label>
      <select v-model="form.chatRoomType" required>
        <option disabled value="">선택하세요</option>
        <option value="FLOOR">층별 채팅방</option>
        <option value="NOTICE">공지방</option>
        <option value="PROREQ">상품 요청방</option>
        <option value="PRIVATE">사담방</option>
      </select>

      <label v-if="form.chatRoomType === 'FLOOR'">층 정보:</label>
      <div v-if="form.chatRoomType === 'FLOOR'" class="floor-buttons">
        <button v-for="floor in ['지하1층', '1층', '2층', '3층', '4층']"
                :key="floor"
                type="button"
                @click="form.floor = floor"
                :class="{ selected: form.floor === floor }">
          {{ floor }}
        </button>
      </div>

      <button type="submit">생성하기</button>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()

const form = ref({
  roomName: '',
  chatRoomType: '',
  floor: ''
})

const submitForm = async () => {
  try {
    await axios.post('/api/chat/rooms/group', form.value, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`
      }
    })
    alert('채팅방이 생성되었습니다.')
    router.push('/chat/rooms')
  } catch (error) {
    console.error('채팅방 생성 실패:', error)
    alert('채팅방 생성에 실패했습니다.')
  }
}
</script>

<style scoped>
.create-chat-room-page {
  max-width: 500px;
  margin: 40px auto;
  padding: 20px;
  background-color: #ffffff;
  border-radius: 10px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.create-chat-room-page h1 {
  text-align: center;
  margin-bottom: 24px;
  color: #333;
}

.create-chat-room-page form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.create-chat-room-page label {
  font-weight: bold;
  margin-bottom: 4px;
  color: #444;
}

.create-chat-room-page input,
.create-chat-room-page select {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 6px;
  font-size: 14px;
}

.create-chat-room-page button {
  background-color: #333;
  color: #fff;
  border: none;
  padding: 12px;
  font-size: 15px;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.create-chat-room-page button:hover {
  background-color: #222;
}

.create-chat-room-page .floor-buttons {
  display: flex;
  gap: 16px; /* 버튼 사이 간격 늘림 */
  flex-wrap: wrap;
  justify-content: center; /* 가운데 정렬 */
}

.create-chat-room-page .floor-buttons button {
  background-color: #9a9a9a;
  border: 1px solid #ccc;
  padding: 6px 12px; /* 크기 줄임 */
  border-radius: 6px;
  font-size: 12px; /* 글자 크기 줄임 */
  cursor: pointer;
  transition: background-color 0.2s ease;
  max-width: 100px; /* 최대 너비 지정 */
}

.create-chat-room-page .floor-buttons button:hover {
  background-color: #ddd;
}

.create-chat-room-page .floor-buttons button.selected {
  background-color: #333;
  color: white;
}
</style>