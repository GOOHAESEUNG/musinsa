<template>
  <div class="chat-room-container">
    <header class="chat-header">
      <button @click="goBack">뒤로가기</button>
      <h2>{{ roomId }}번 채팅방</h2>
    </header>

    <div class="chat-messages">
      <div v-for="(msg, index) in messages" :key="index">
        <strong>{{ msg.senderEmail }}:</strong> {{ msg.message }}
      </div>
    </div>

    <form @submit.prevent="sendMessage" class="chat-input-area">
      <input
        v-model="newMessage"
        placeholder="메시지를 입력하세요"
        class="chat-input"
      />
      <button type="submit" class="send-button">전송</button>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import {
  connectStompClient,
  disconnectStompClient,
  sendMessage as sendStompMessage
} from '@/utils/stomp'

const route = useRoute()
const router = useRouter()
const roomId = route.params.roomId
const newMessage = ref('')
const messages = ref([])

const sendMessage = () => {
  if (newMessage.value.trim() === '') return

  const messageDto = {
    message: newMessage.value,
    senderEmail: localStorage.getItem('email'), 
    messageType: "GENERAL",
  }

  sendStompMessage(roomId, messageDto)
  newMessage.value = ''
}

const onMessageReceived = (message) => {
  messages.value.push(message)
}

const goBack = () => {
  router.back()
}

onMounted(async () => {
  try {
    const response = await axios.get(`/api/chat/rooms/${roomId}/messages`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`
      }
    })
    messages.value = response.data
  } catch (error) {
    console.error('기존 메시지 로드 실패:', error)
  }

  connectStompClient(onMessageReceived, roomId)
})

onUnmounted(() => {
  disconnectStompClient()
})
</script>

<style scoped>
.chat-room-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  padding: 16px;
}

.chat-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.chat-messages {
  flex: 1;
  border: 1px solid #ddd;
  padding: 12px;
  background-color: #fafafa;
  margin-bottom: 12px;
}

.chat-input-area {
  display: flex;
  gap: 8px;
}

.chat-input {
  flex: 1;
  padding: 8px;
}

.send-button {
  padding: 8px 12px;
}
</style>