
// New file: src/utils/stomp.js
import SockJS from 'sockjs-client'
import { Stomp } from '@stomp/stompjs'

let stompClient = null

export const connectStompClient = (onMessageReceived, roomId) => {
  const socket = new SockJS(`${process.env.VUE_APP_API_BASE_URL}/connect`)// 서버 주소에 따라 수정 필요
  stompClient = Stomp.over(socket)

  const token = localStorage.getItem('token')

  stompClient.connect(
    {
      Authorization: `Bearer ${token}`
    },
    () => {
      console.log('🟢 STOMP connected')
      stompClient.subscribe(`/topic/${roomId}`, (message) => {
        onMessageReceived(JSON.parse(message.body))
      })
    },
    (error) => {
      console.error('🔴 STOMP connection error:', error)
    }
  )
}

export const sendMessage = (roomId, messageDto) => {
  if (stompClient && stompClient.connected) {
    stompClient.send(`/publish/${roomId}`, {}, JSON.stringify(messageDto))
  }
}

export const disconnectStompClient = () => {
  if (stompClient !== null) {
    stompClient.disconnect(() => {
      console.log('🔌 STOMP disconnected')
    })
  }
}