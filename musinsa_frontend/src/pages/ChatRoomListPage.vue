<template>
  <div class="chat-room-tabs">
    <div class="tab-header">
      <button
        :class="{ active: selectedTab === 'participated' }"
        @click="selectedTab = 'participated'"
      >
        참여한 채팅방
      </button>
      <button
        :class="{ active: selectedTab === 'notParticipated' }"
        @click="selectedTab = 'notParticipated'"
      >
        참여하지 않은 채팅방
      </button>
    </div>

    <div class="chat-room-list">
      <div
        class="chat-room-card"
        :class="{ 'non-participant': selectedTab === 'notParticipated' }"
        v-for="room in displayedRooms"
        :key="room.roomId"
        @click="selectedTab === 'participated' && goToRoom(room.roomId)"
      >
        <div class="chat-room-info-horizontal">
          <div class="chat-room-label" :class="room.isGroupChat === 'Y' ? 'group' : 'private'">
            {{ room.isGroupChat === 'Y' ? '단체 채팅방' : '1:1 채팅방' }}
          </div>
          <div class="chat-room-name">{{ room.roomName }}</div>
          <div class="chat-room-type">({{ getRoomTypeLabel(room.chatRoomType) }})</div>
        </div>
        <div class="join-button-wrapper" v-if="selectedTab === 'notParticipated'">
          <button
            class="join-button"
            @click.stop="joinChatRoom(room.roomId)"
          >
            참가
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()
const participatedRooms = ref([])
const notParticipatedRooms = ref([])

const selectedTab = ref('participated')

const displayedRooms = computed(() => {
  return selectedTab.value === 'participated'
    ? participatedRooms.value
    : notParticipatedRooms.value
})

const goToRoom = (roomId) => {
  router.push(`/chat/room/${roomId}`)
}

onMounted(async () => {
  try {
    const response = await axios.get('/api/chat/rooms/my', {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`
      }
    })
    const allRooms = response.data
    participatedRooms.value = allRooms.filter(r => r.isParticipant)
    notParticipatedRooms.value = allRooms.filter(r => !r.isParticipant)
  } catch (error) {
    console.error('채팅방 목록을 불러오는 중 오류 발생:', error)
  }
})

const getRoomTypeLabel = (type) => {
  switch (type) {
    case 'NOTICE':
      return '공지방'
    case 'PROREQ':
      return '상품 요청방'
    case 'FLOOR':
      return '층별방'
    case 'PRIVATE':
      return '사담방'
    default:
      return '기타'
  }
}

const joinChatRoom = async (roomId) => {
  try {
    await axios.post(`/api/chat/room/group/${roomId}/join`, {}, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem('token')}`
      }
    })
    router.push(`/chat/room/${roomId}`)
  } catch (error) {
    console.error('채팅방 참가 중 오류 발생:', error)
  }
}
</script>

<style scoped>
.chat-room-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-top: 16px;
  align-items: center;
}

.chat-room-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 12px 16px;
  background-color: #f9f9f9;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
  cursor: pointer;
  width: 80%;
  max-width: 520px;
  flex-wrap: nowrap;
  transition: all 0.2s ease-in-out;
}

.chat-room-card.non-participant {
  cursor: default;
  opacity: 0.8;
}

.chat-room-card:not(.non-participant):hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.2s ease-in-out;
}

/* .chat-room-info {
  display: flex;
  flex-direction: column;
} */

.chat-room-info-horizontal {
  display: grid;
  grid-template-columns: 1fr 2fr 1fr;
  align-items: center;
  justify-items: center;
  width: 100%;
}

.chat-room-info-horizontal > div {
  text-align: center;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.chat-room-label {
  font-size: 14px;
  font-weight: bold;
  margin-bottom: 4px;
}

.chat-room-label.group {
  color: #846c95;
}

.chat-room-label.private {
  color: #43a047;
}

.chat-room-name {
  font-size: 16px;
  font-weight: 600;
}

.chat-room-type {
  font-size: 13px;
  color: #888;
}

.chat-room-tabs {
  width: 100%;
}

.tab-header {
  display: flex;
  justify-content: center;
  width: fit-content;
  margin: 40px auto 20px;
  gap: 12px;
}

.tab-header button {
  padding: 8px 16px;
  border: none;
  background-color: #eee;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 600;
  font-size: 14px;
  width: 150px;
}

.tab-header button.active {
  background-color: #846c95;
  color: white;
}

.join-button-wrapper {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  margin-left: 12px;
}

.join-button {
  background-color: #e0e0e0;
  color: #333;
  border: none;
  padding: 6px 14px;
  border-radius: 16px;
  font-size: 13px;
  cursor: pointer;
  transition: background-color 0.2s ease-in-out;
  white-space: nowrap;
}

.join-button:hover {
  background-color: #ccc;
}
</style>