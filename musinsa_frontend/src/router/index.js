import { createRouter, createWebHistory } from 'vue-router'

// 페이지 컴포넌트 import
import SignupPage from '@/pages/SignupPage.vue'
// 나중에 LoginPage도 추가 가능

const routes = [
  {
    path: '/signup',
    name: 'Signup',
    component: SignupPage
  },
  // {
  //   path: '/login',
  //   name: 'Login',
  //   component: LoginPage
  // }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router