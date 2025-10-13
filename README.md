# 🧩 MUO – Musinsa Offline Chat Service (MVP)

<img width="89" height="89" alt="image" src="https://github.com/user-attachments/assets/13ab297d-5ceb-44bc-b9c4-ebdbe9d9df6e" />


> 무신사 오프라인 스토어 직원 간 **실시간 소통과 상품 요청 처리를 위한 내부 전용 채팅 서비스**

---

## 📘 프로젝트 개요

MUO(Musinsa Offline Chat Service)는 무신사 오프라인 스토어에서 근무하며 경험한  
**매장 간 비효율적인 커뮤니케이션 문제**를 해결하기 위해 기획된 개인 프로젝트입니다.  

매장 내 직원들이 전화나 무전기 대신,  
**실시간 채팅으로 상품 요청과 응답을 주고받을 수 있도록** 설계된 MVP(Minimum Viable Product) 버전입니다.

---

## 🎯 프로젝트 목표

- 매장 내 직원 간 **실시간 커뮤니케이션 구축**
- 상품 요청 → 응답 과정을 **메시지 기반으로 구조화**
- **확장 가능한 실시간 서버 아키텍처** 설계 (Redis Pub/Sub)

---

## ⚙️ 기술 스택

| 구분 | 기술 |
|------|------|
| **Language** | Java 17 |
| **Framework** | Spring Boot |
| **Real-time** | STOMP, WebSocket |
| **Message Broker** | Redis Pub/Sub |
| **Build Tool** | Gradle |
| **Infra (예정)** | AWS EC2, Docker |
| **DB (MVP)** | In-memory or Redis-based |

---

## 🧠 주요 기능 (1차 MVP)

| 기능 | 설명 |
|------|------|
| 💬 **실시간 채팅** | STOMP over WebSocket 기반 메시지 송수신 |
| 🏠 **채팅방 관리** | 직급/팀 단위 채팅방 생성 및 참여 (`ChatRoom`, `ChatParticipant`) |
| 📦 **상품 요청 처리** | `MessageComplete` 엔티티를 통해 요청/응답 상태 관리 |
| 🔔 **실시간 알림** | Redis Pub/Sub 구조로 새로운 메시지 즉시 반영 |
| 👤 **사용자 역할 구분** | `role`(STAFF, MANAGER, ADMIN)에 따른 접근 제어 |

---

## 🏗️ IA 설계
<img width="1282" height="562" alt="image" src="https://github.com/user-attachments/assets/c9c8c6ca-f51f-4ac5-92c1-e5e2fb20907e" />

---

## 🕸️ ERD 설계
<img width="860" height="455" alt="image" src="https://github.com/user-attachments/assets/77a7bfdd-1b89-4f93-bf03-599fe400e9c9" />

---

## 💡 주요 화면 구성

### 1️⃣ 로그인 / 회원가입 화면
<img width="1235" height="528" alt="image" src="https://github.com/user-attachments/assets/353ba7ec-cf0e-45ba-bf52-81bb284346a1" />

> 본인의 직급과 근무 층을 선택하여 회원가입 및 로그인 진행  
- 로그인 시 직급(Role: STAFF / MANAGER / ADMIN)과 근무층 선택  
- 인증 성공 시 WebSocket 연결 생성  
- 추후 JWT 기반 인증으로 확장 예정  

---

### 2️⃣ 채팅방 목록 화면
<img width="736" height="544" alt="image" src="https://github.com/user-attachments/assets/146c1486-51d4-4648-a8e4-fedaac3985c2" />

> 직원이 참여 중이거나 참여 가능한 채팅방을 확인하고 입장  
- 내 채팅방 / 참여 가능한 채팅방 분리 표시  
- Redis 구독 기반으로 실시간 갱신  
- 각 방의 마지막 메시지 및 읽지 않은 메시지 표시  

---

### 3️⃣ 채팅방 생성 화면
<img width="1055" height="526" alt="image" src="https://github.com/user-attachments/assets/72503531-7cbd-47be-a709-90a7be01d4ba" />

> 새로운 채팅방 생성 – 관리자 직급만 생성 가능  
- `ChatRoomType`(ROLE, TEAM, STORE, PRIVATE)에 따라 방 유형 선택  
- 초기 참여자 선택 가능  
- 생성 후 자동 입장 또는 목록으로 이동  

---

### 4️⃣ 채팅방 상세 화면
<img width="1006" height="490" alt="image" src="https://github.com/user-attachments/assets/8fb4c0cd-6807-4b62-a50b-e5f39cf56e04" />

> 실시간 채팅 + 상품 요청/응답을 주고받는 핵심 화면  
- STOMP 기반 실시간 메시지 송수신  
- 상품 요청(`REQUEST`) 메시지 발송 및 처리(`COMPLETE`) 상태 변경  
- 메시지별 상태 표시 (요청 대기 / 완료 / 거절)  
- Redis Pub/Sub으로 모든 참여자에게 실시간 반영  

---

### 5️⃣ 나의 정보 화면
<img width="422" height="521" alt="image" src="https://github.com/user-attachments/assets/d09fce4c-d300-46d7-9908-0e03e4237c77" />

> 사용자 정보 표시 (이름, 역할, 근무 층)  
- 사용자 프로필 및 로그인 정보 표시  
- 로그아웃 및 설정 관리  
- 향후 알림 설정 및 프로필 수정 기능 추가 예정  

---


