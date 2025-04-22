<template>
  <div class="chat-room-container">
    <header class="chat-header">
      <button @click="goBack" class="back-button">←</button>
      <h2>{{ roomId }}번 채팅방</h2>
    </header>

    <div class="chat-messages" ref="chatMessagesRef">
      <div
        v-for="(msg, index) in messages"
        :key="index"
        class="chat-message-wrapper"
        :class="msg.senderEmail === myEmail ? 'sent-wrapper' : 'received-wrapper'"
      >
        <div v-if="msg.senderEmail !== myEmail" class="sender-name">{{ msg.senderName }}</div>
        <div class="message-line" :class="msg.senderEmail === myEmail ? 'sent-line' : 'received-line'">
          <div
            :class="[
              'chat-message',
              msg.senderEmail === myEmail ? 'sent' : 'received'
            ]"
          >
            <div class="message-text">{{ msg.message }}</div>
          </div>
          <div class="message-time-inline aligned">{{ formatTime(msg.sendDate) }}</div>
        </div>
        <!--
        <div
          v-if="index === messages.length - 1 || !isSameSendTime(msg.sendDate, messages[index + 1]?.sendDate)"
          class="message-time"
        >
          {{ formatTime(msg.sendDate) }}
        </div>
        -->
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
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
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

const chatMessagesRef = ref(null)

const myEmail = localStorage.getItem('email') || ''

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

const scrollToBottom = () => {
  nextTick(() => {
    if (chatMessagesRef.value) {
      chatMessagesRef.value.scrollTop = chatMessagesRef.value.scrollHeight
    }
  })
}

const onMessageReceived = (message) => {
  messages.value.push(message)
  scrollToBottom()
}

const goBack = () => {
  router.back()
}

const formatTime = (datetime) => {
  if (!datetime) {
    console.warn('formatTime: datetime is undefined or null')
    return ''
  }
  console.log('Formatting time for:', datetime)
  const date = new Date(datetime)
  return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', hour12: false })
}


onMounted(async () => {
  try {
    const response = await axios.get(`/api/chat/history/${roomId}`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`
      }
    })
    messages.value = response.data
    scrollToBottom()
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
  display: flex;
  flex-direction: column;
  height: 400px;
  border: 1px solid #ddd;
  padding: 12px;
  background-color: #fafafa;
  margin-bottom: 12px;
  overflow-y: auto;
}

.chat-input-area {
  display: flex;
  gap: 8px;
  padding: 12px;
  background-color: #ffffff;
  border-top: 1px solid #ddd;
}

.chat-input {
  flex: 1;
  padding: 10px 14px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 20px;
  outline: none;
}

.chat-input:focus {
  border-color: #222;
}

.send-button {
  padding: 10px 16px;
  font-size: 14px;
  border: none;
  background-color: #444;
  color: #fff;
  border-radius: 20px;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.send-button:hover {
  background-color: #222;
}

.chat-message {
  padding: 10px 14px;
  border-radius: 12px;
  max-width: 60%;
  font-size: 13px;
  line-height: 1.6;
  box-shadow: 0 1px 2px rgba(0,0,0,0.06);
}

.sent {
  background-color: #e6f0ff; /* 연파랑 */
}

.received {
  background-color: #f1f1f1; /* 연회색 */
}

.chat-message-wrapper {
  display: flex;
  flex-direction: column;
  margin-bottom: 8px;
}

.sender-name {
  font-size: 0.75rem;
  color: #666;
  margin: 0 8px 2px;
}

.sent-wrapper {
  align-items: flex-end;
}

.received-wrapper {
  align-items: flex-start;
}

.message-text {
  white-space: pre-wrap;
}

.message-line {
  display: flex;
  align-items: flex-end;
  gap: 6px;
}

.sent-line {
  justify-content: flex-end;
  flex-direction: row-reverse;
}

.received-line {
  justify-content: flex-start;
  flex-direction: row;
}

 .message-time-inline {
   font-size: 0.7rem;
   color: #999;
   white-space: nowrap;
   margin: 0 6px;
 }

.message-time-inline.aligned {
  align-self: flex-end;
  margin-bottom: 2px;
}

/*
.sent-time {
  align-self: flex-end;
}

.received-time {
  align-self: flex-start;
}
*/

/*
.message-time {
  font-size: 0.7rem;
  color: #999;
  margin-top: 4px;
  text-align: right;
}
*/

.back-button {
  padding: 6px 12px;
  font-size: 14px;
  background-color: #f2f2f2;
  border: 1px solid #ccc;
  color: #333;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.back-button:hover {
  background-color: #e0e0e0;
}
</style> 