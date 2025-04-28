<template>
  <div class="login-container">
    <h2 style="text-align: center">MUSINSA CREW 로그인</h2>

    <form @submit.prevent="submitForm">
      <label>이메일</label>
      <input v-model="form.email" type="email" required placeholder="이메일을 입력하세요" />

      <label>비밀번호</label>
      <input v-model="form.password" type="password" required placeholder="비밀번호를 입력하세요" />

      <button type="submit">로그인</button>
    </form>

    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import { jwtDecode } from 'jwt-decode'

const router = useRouter()

const form = ref({
  email: '',
  password: '',
})

const errorMessage = ref('')

const submitForm = async () => {
  try {
    const response = await axios.post(`${process.env.VUE_APP_API_BASE_URL}/employee/doLogin`, form.value)
    const token = response.data.token
    const email = jwtDecode(token).sub // 이메일을 JWT에서 추출
    localStorage.setItem('token', token)
    localStorage.setItem('email', email)
    router.push('/myInfo')
  } catch (error) {
    errorMessage.value = error.response?.data?.message || '로그인에 실패했습니다.'
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  background-color: #000;
  color: white;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 20px;
  box-sizing: border-box;
}

input {
  width: 100%;
  margin-bottom: 1px; /* was 12px */
  background-color: white;
  color: black;
  border: 1px solid #ccc;
  padding: 10px;
  border-radius: 6px;
  box-sizing: border-box;
}

.error {
  color: red;
}

label {
  margin-top: 8px;
  display: block;
  color: black;
}

button[type="submit"] {
  width: 100%;
  background-color: #555;
  color: white;
  border: none;
  padding: 10px;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  margin-top: 20px; /* added spacing below inputs */
}

button[type="submit"]:hover {
  background-color: #444;
}
</style>