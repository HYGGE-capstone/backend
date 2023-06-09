# 프로젝트 소개
## 아주좋은팀
안녕하세요.<br/>
팀/팀원 매칭 플랫폼 '아주좋은팀' 을 개발한 팀 HYGGE 입니다.<br/>
서비스 이용 : ajougoodteam.com

## HYGGE 백엔드 팀
| role | name | part | email |
| :--: | :--: | :-- | :-- |
| 팀원 | 이지수 | 백엔드 API 개발, 서버 관리, CI/CD | itis821@ajou.ac.kr |
| 팀원 | 권성학 | 백엔드 API 개발, 데이터 관리 | rainbow8956@ajou.ac.kr |
</br>

# 사용 기술 스택
아주좋은팀의 백엔드 API 전체적으로 Spring Boot 를 이용하여 개발하였습니다.<br/>
보안을 위해 Spring Security 와 JWT 를 이용하였습니다.<br/>
데이터베이스로는 전체적으로 RDBMS 인 MySQL 을 이용하였고,<br/> 
JWT 를 효율적으로 관리하기 위해 Redis 를 이용하였습니다.<br/>
API 를 관리하기 위해 Swagger 를 이용하였습니다. <br/>
현재 AWS EC2 를 이용해 백엔드 API 서버를 배포하고 있는 중이며,  
개발한 API 는 아래 주소에서 확인하실 수 있습니다.<br/>  
API 명세 : http://ajougoodteam.com/api-docs
<br/>  

# 패키지 구조
전체적인 패키지 구조는 다음과 같습니다.<br/>
- admin.controller : 관리자 기능의 Controller 들을 저장
- controller : 회원 기능의 Controller 들을 저장
- dto : 서비스에 이용되는 DTO 들을 저장
- entity : JPA 를 통해 관리되는 엔티티 클래스들을 저장
- error :  예외와 예외를 처리하는 클래스들을 저장
- jwt : JWT 인증에 필요한 클래스들을 저장
- repository : Repository 클래스들을 저장
- service : 서비스 로직이 담긴 클래스들을 저장

# 서비스 설명
![팜플렛_3 0](https://github.com/HYGGE-capstone/backend/assets/44251670/358e7dd2-f979-48da-8f9e-12caa341bcc2)

## 프론트엔드 
https://github.com/HYGGE-capstone/frontend
