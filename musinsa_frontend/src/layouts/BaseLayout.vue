<template>
  <div>
    <header class="app-header">
      <button @click="goTo('/chat/rooms')">채팅방 목록</button>
      <button @click="goTo('/chat/create')">채팅방 생성</button>
      <button @click="goTo('/my-info')">내 정보</button>
    </header>

    <main>
      <router-view /> <!-- 여기에 각 페이지 컴포넌트가 로딩됨 -->
    </main>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()

const goTo = (path) => {
  router.push(path)
}
</script>

<style scoped>
.app-header {
  background-color: black;
  color: white;
  padding: 12px;
  display: flex;
  justify-content: space-around;
}
button {
  background: none;
  border: none;
  color: white;
  cursor: pointer;
  font-size: 16px;
}
</style>