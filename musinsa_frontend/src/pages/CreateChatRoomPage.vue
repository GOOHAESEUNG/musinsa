

<template>
  <div>
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
      
      <label>층 정보:</label>
      <input v-model="form.floor" type="text" placeholder="예: 2F" />
      
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