import { createRouter, createWebHistory } from 'vue-router'

// 페이지 컴포넌트 import
import SignupPage from '@/pages/SignupPage.vue'
import HomePage from '@/pages/HomePage.vue'
import LoginPage from '@/pages/LoginPage.vue'
import BaseLayout from '@/layouts/BaseLayout.vue'

const routes = [
  {
    path: '/',
    component: BaseLayout,
    children: [
      {
        path: 'myInfo',
        name: 'MyInfo',
        component: () => import('@/pages/MyInfoPage.vue')
      },
      {
        path: 'chat/rooms',
        name: 'ChatRoomList',
        component: () => import('@/pages/ChatRoomListPage.vue')
      },
      {
        path: 'chat/create',
        name: 'CreateChatRoom',
        component: () => import('@/pages/CreateChatRoomPage.vue')
      },
      {
        path: 'chat/room/:roomId',
        name: 'ChatRoomDetail',
        component: () => import('@/pages/ChatRoomDetailPage.vue')
      }
    ]
  },
  {
    path: '',
    name: 'Home',
    component: HomePage
  },
  {
    path: '/signup',
    name: 'Signup',
    component: SignupPage
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginPage
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router