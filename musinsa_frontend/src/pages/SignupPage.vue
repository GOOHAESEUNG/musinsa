<template>
  <div class="signup-container">
    <h2>MUSINSA CREW 회원가입</h2>

    <form @submit.prevent="submitForm">
      <div class="form-fields">
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
        <div class="floor-button-group">
          <button
            v-for="option in floorOptions"
            :key="option"
            type="button"
            :class="{ selected: form.floor === option }"
            @click="form.floor = option"
          >
            {{ option }}
          </button>
        </div>
 
        <label>입사일</label>
        <input v-model="form.join_date" type="date" required />
      </div>
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

const floorOptions = ['지하1층', '1층', '2층', '3층', '4층', '관리자']

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
  min-height: 100vh;
  background-color: #000;
  color: white;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 20px;
  box-sizing: border-box;
}
.form-fields {
  padding: 16px;
  border-radius: 12px;
  box-sizing: border-box;
  width: 100%;
  max-width: 400px;
}
input, select {
  width: 100%;
  margin-bottom: 12px;
  background-color: white; /* input box is white */
  color: black;
  border: 1px solid #ccc; /* subtle light gray border */
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
  color: white;
}
button[type="submit"] {
  width: 100%;
  background-color: #555; /* medium gray */
  color: white;
  border: none;
  padding: 10px;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

button[type="submit"]:hover {
  background-color: #444; /* slightly darker gray */
}
.floor-button-group {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  margin-bottom: 12px;
}

.floor-button-group button {
  background-color: #eee;
  color: #333;
  border: 1px solid #ccc;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.floor-button-group button.selected {
  background-color: #333;
  color: white;
  border-color: #333;
}
</style>