# 📚 일정 관리 앱
## 프로젝트 소개
- 일정(Schedule) CRUD API를 구현합니다.
- 도전 기능으로 댓글(Comment) 작성 및 일정 단건 조회 시 댓글 포함 응답을 구현합니다.
- Spring Boot + JPA + MySQL을 사용합니다.
---
## 기술 스택
- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- MySQL
- Gradle
---
## API 명세
### Schedule
- POST /schedules : 일정 생성
- GET /schedules : 전체 일정 조회(작성자명 선택 필터)
- GET /schedules/{id} : 선택 일정 조회
- PATCH /schedules/{id} : 선택 일정 수정(비밀번호 필요)
- DELETE /schedules/{id} : 선택 일정 삭제(비밀번호 필요)
