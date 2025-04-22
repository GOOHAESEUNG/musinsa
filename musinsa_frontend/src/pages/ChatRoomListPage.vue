<template>
  <div>
    <h1>채팅방 목록</h1>
    <div class="chat-room-list">
      <div
        class="chat-room-card"
        v-for="room in chatRooms"
        :key="room.roomId"
        @click="goToRoom(room.roomId)"
      >
        <div class="chat-room-label" :class="room.isGroupChat === 'Y' ? 'group' : 'private'">
          {{ room.isGroupChat === 'Y' ? '단체 채팅방' : '1:1 채팅방' }}
        </div>
        <div class="chat-room-name">{{ room.roomName }}</div>
        <div class="chat-room-type">({{ room.chatRoomType ?? room.chatRoomType?.name }})</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()
const chatRooms = ref([])

const goToRoom = (roomId) => {
  router.push(`/chat/room/${roomId}`)
}

onMounted(async () => {
  try {
    const response = await axios.get('/api/chat/rooms/my', {
        headers: {
         Authorization: `Bearer ${localStorage.getItem('token')}`
  }
})
    chatRooms.value = response.data
  } catch (error) {
    console.error('채팅방 목록을 불러오는 중 오류 발생:', error)
  }
})
</script>

<style scoped>
.chat-room-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-top: 16px;
}

.chat-room-card {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 12px 16px;
  background-color: #f9f9f9;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
  cursor: pointer;
}

.chat-room-label {
  font-size: 14px;
  font-weight: bold;
  margin-bottom: 4px;
}

.chat-room-label.group {
  color: #1e88e5;
}

.chat-room-label.private {
  color: #43a047;
}

.chat-room-name {
  font-size: 16px;
  font-weight: 600;
}

.chat-room-type {
  font-size: 13px;
  color: #888;
}
</style>