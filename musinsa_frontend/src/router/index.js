import { createRouter, createWebHistory } from 'vue-router'


// 페이지 컴포넌트 import
import SignupPage from '@/pages/SignupPage.vue'
// 나중에 LoginPage도 추가 가능
import HomePage from '@/pages/HomePage.vue'
import LoginPage from '@/pages/LoginPage.vue'
import MyInfoPage from '@/pages/MyInfoPage.vue'

const routes = [
  {
    path: '/signup',
    name: 'Signup',
    component: SignupPage
  },
    {
      path: '/',
      name: 'Home',
      component: HomePage
    },
    { path: '/login', 
    name: 'Login', 
    component: LoginPage },
  {
    path: '/myInfo',
    name: 'MyInfo',
    component: MyInfoPage
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router