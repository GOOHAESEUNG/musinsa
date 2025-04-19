<template>
  <div class="myinfo-container">
    <h2>내 정보</h2>
    <div v-if="myInfo" class="info-box">
      <p><strong>이름:</strong> {{ myInfo.name }}</p>
      <p><strong>이메일:</strong> {{ myInfo.email }}</p>
      <p><strong>직책:</strong> {{ myInfo.position }}</p>
      <p><strong>층:</strong> {{ myInfo.floor }}</p>
    </div>
    <p v-else>정보를 불러오는 중...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const myInfo = ref(null)
const router = useRouter()

onMounted(async () => {
  try {
    const token = localStorage.getItem('token')
    console.log('📦 로컬 스토리지 토큰:', token)

    const response = await axios.get('/employee/myInfo', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })

    console.log('✅ 사용자 정보 응답:', response.data)
    myInfo.value = response.data
  } catch (error) {
    console.error('❌ 사용자 정보 조회 실패:', error)
    router.push('/login') // 실패 시 로그인으로 리다이렉트
  }
})
</script>

<style scoped>
.myinfo-container {
  min-height: 100vh;
  background-color: #000;
  color: white;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 40px 20px;
  box-sizing: border-box;
}

.info-box {
  background-color: #111;
  padding: 24px;
  border-radius: 10px;
  width: 100%;
  max-width: 400px;
  margin-top: 20px;
}

.info-box p {
  margin-bottom: 12px;
  font-size: 16px;
}
</style>