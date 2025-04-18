#!/bin/bash

gh label create "🐛 버그" --description "버그 리포트용" --color d73a4a
gh label create "✨ 기능 추가" --description "새로운 기능 제안" --color 0e8a16
gh label create "🛠 개선" --description "기능 개선사항" --color c2e0c6
gh label create "🔥 긴급" --description "우선순위 높은 이슈" --color e11d48
gh label create "❓ 질문" --description "토론/문의/질문" --color d876e3
gh label create "🧹 리팩토링" --description "코드 정리/구조 개선" --color f9d0c4
gh label create "📚 문서" --description "문서 관련 작업" --color 0075ca
gh label create "🧪 테스트" --description "테스트 코드 관련" --color fef2c0
gh label create "✅ 완료됨" --description "완료된 이슈" --color 0e8a16
gh label create "🚧 진행 중" --description "현재 작업 중인 이슈" --color fbca04
gh label create "백엔드" --description "백엔드 관련 작업" --color 1d76db
gh label create "프론트엔드" --description "프론트엔드 관련 작업" --color 5319e7
gh label create "디자인" --description "디자인/피그마 관련" --color ff80cc