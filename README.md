## 스파르타 내일배움캠프 3차 프로젝트 with Java/Spring - 피버타임

### 🔗 링크
- 피버타임 사이트 링크 : <a href="https://www.fevertime.shop">https://www.fevertime.shop</a>
- 3차 프로젝트 with Java/Spring - <a href="https://faint-clef-9ea.notion.site/3-Spring-8a33a098b255452894f6a67ca74f4381">개발문서</a> (API 설계, DB 설계, 화면 설계, Convention)
- 3차 프로젝트 with Java/Spring - <a href="https://github.com/fever-time/challenge-frontend">Frontend Github Repo</a>
- 3차 프로젝트 with Java/Spring - <a href="https://github.com/fever-time/challenge-backend">Backend Github Repo</a>
- 1차, 2차 프로젝트 with Python/Flask - <a href="https://github.com/fever-time/challenge-with-flask">Project Github Repo</a>
- 1차, 2차 프로젝트 with Python/Flask - <a href="https://faint-clef-9ea.notion.site/5b174114b93b4eb0ad70c27fd0853910">개발문서</a> (API 설계, DB 설계, 화면 설계, Convention)

### 🏠 소개
- 챌린지를 설정하고 달성하기 위해 다른 참여자들과 함께 도전하고 인증하는 서비스입니다.

### ⏲️ 개발기간
- [1차 프로젝트 with Python/Flask] 2021-09-23 ~ 2021-09-30 (<a href="https://github.com/fever-time/challenge-with-flask/wiki/4.-1%EC%B0%A8-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%ED%9A%8C%EA%B3%A0">1차 프로젝트 KPT - WIKI</a>)
- [2차 프로젝트 with Python/Flask] 2021-10-05 ~ 2021-10-19 (<a href="https://github.com/fever-time/challenge-with-flask/wiki/5.-2%EC%B0%A8-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%ED%9A%8C%EA%B3%A0">2차 프로젝트 KPT - WIKI</a>)
- [3차 프로젝트 with Java/Spring] 2021-11-19 ~ 2021-12-09 (<a href="https://github.com/fever-time/challenge-backend/wiki/7.-3%EC%B0%A8-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%ED%9A%8C%EA%B3%A0">3차 프로젝트 KPT - WIKI</a>)

### 🧙 맴버구성
- <a href="https://github.com/kkyu8925">강현규</a>
- <a href="https://github.com/JunHo-YH">강준호</a>
- <a href="https://github.com/suubinkim">김수빈</a>

### 📌 아키텍처
![image](https://user-images.githubusercontent.com/78840035/145398320-00c5b5fb-1104-4460-9f71-2b8a8c4f9cb5.png)

### 📌 3차 프로젝트 기술 스택&선택 이유 - <a href="https://github.com/fever-time/challenge-backend/wiki/1.-%EA%B8%B0%EC%88%A0-%EC%84%A0%ED%83%9D-%EC%9D%B4%EC%9C%A0!">WIKI 이동</a>
- Java - 개발 언어
- Junit, Mockito - 자바 테스트 프레임워크
- Spring boot - 웹 프레임워크
- Spring Data JPA - JPA(Hibernate) : 자바 ORM 기술 표준
- Spring Security - 인증&인가 프레임워크
- Spring Rest Docs - API 자동 문서화
- MySQL(AWS RDS) - 데이터베이스
- Git - 형상관리
- Github Actions - CI/CD
- AWS S3, CloudFront - 프론트엔드 서버 인프라
- AWS EBS, ECR(Docker) - 백엔드 서버 인프라

### 📌 주요 기능 - <a href="https://github.com/fever-time/challenge-backend/wiki/2.-%EC%A3%BC%EC%9A%94-%EA%B8%B0%EB%8A%A5">WIKI 이동</a>
### User
- Spring Security + JWT 로그인
- OAuth 2.0 기반 카카오 로그인
### Challenge
- 로그인 유저만 챌린지 생성/참여
- 오프라인 챌린지 장소 Kakao Map 키워드 검색 및 위치 설정
- 챌린지 카테고리/제목 검색
- 스케줄러를 활용한 챌린지 마감
- 챌린지별 하루에 인증 한 번만 가능
### Feed
- 피드, 댓글 기능

### 📌 ERD
![FEVER-TIME](https://user-images.githubusercontent.com/64997245/144733570-f9fd211d-525c-4640-a228-b56da5ef27d4.png)

### 📌 문제를 이렇게 해결했어요! - <a href="https://github.com/fever-time/challenge-backend/wiki/3.-%EB%AC%B8%EC%A0%9C%EB%A5%BC-%EC%9D%B4%EB%A0%87%EA%B2%8C-%ED%95%B4%EA%B2%B0%ED%96%88%EC%96%B4%EC%9A%94!">WIKI 이동</a>

### 📌 Project History - <a href="https://github.com/fever-time/challenge-backend/wiki/4.-Project-History">WIKI 이동</a>

### 📌 팀 문화 - <a href="https://github.com/fever-time/challenge-backend/wiki/5.-%ED%8C%80-%EB%AC%B8%ED%99%94">WIKI 이동</a>

### 📌 매일 회고록 - <a href="https://github.com/fever-time/challenge-backend/wiki/6.-%EB%A7%A4%EC%9D%BC-%ED%9A%8C%EA%B3%A0%EB%A1%9D">WIKI 이동</a>
