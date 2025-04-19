<template>
  <div class="home-container">
    <transition name="fade">
      <h1 class="splash-text" :class="{ hidden: !showSplash }">MUSINSA</h1>
    </transition>

    <transition name="fade">
      <div v-if="showButtons" class="button-group">
        <router-link to="/signup" class="home-button">회원가입</router-link>
        <router-link to="/login" class="home-button">로그인</router-link>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const showSplash = ref(true)
const showButtons = ref(false)

onMounted(() => {
  setTimeout(() => {
    showSplash.value = false
    showButtons.value = true
  }, 5000)
})
</script>

<style scoped>
.home-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background-color: black;
  color: white;
}

.splash-text {
  font-size: 48px;
  font-weight: bold;
  transition: opacity 1s ease;
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
}

.hidden {
  opacity: 0;
  pointer-events: none;
}

.button-group {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.home-button {
  background-color: #555;
  color: white;
  padding: 10px 24px;
  border-radius: 6px;
  text-decoration: none;
  text-align: center;
  font-size: 16px;
  transition: background-color 0.3s ease;
}

.home-button:hover {
  background-color: #444;
}

.fade-enter-active, .fade-leave-active {
  transition: opacity 1s ease;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
}
</style>
