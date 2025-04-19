<template>
  <div class="signup-container">
    <h2>직원 회원가입</h2>

    <form @submit.prevent="submitForm">
      <label>이름</label>
      <input v-model="form.name" type="text" required />

      <label>이메일</label>
      <input v-model="form.email" type="email" required />

      <label>비밀번호</label>
      <input v-model="form.password" type="password" required />

      <label>직책</label>
      <select v-model="form.position" required>
        <option disabled value="">선택</option>
        <option>LD</option>
        <option>SM</option>
        <option>FM</option>
        <option>VMD</option>
        <option>ASM</option>
        <option>PT</option>
        <option>FT</option>
      </select>

      <label>층</label>
      <input v-model="form.floor" type="text" required />

      <label>입사일</label>
      <input v-model="form.join_date" type="date" required />

      <button type="submit">회원가입</button>
    </form>

    <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()

const form = ref({
  name: '',
  email: '',
  password: '',
  position: '',
  floor: '',
  join_date: '',
})

const errorMessage = ref('')

const submitForm = async () => {
  try {
    await axios.post('/employee/create', form.value)
    router.push('/login') // 회원가입 성공 시 로그인 페이지로 이동
  } catch (error) {
    errorMessage.value = error.response?.data?.message || '회원가입에 실패했습니다.'
  }
}
</script>

<style scoped>
.signup-container {
  max-width: 400px;
  margin: auto;
  padding: 20px;
}
input, select {
  width: 100%;
  margin-bottom: 10px;
}
.error {
  color: red;
}
</style>