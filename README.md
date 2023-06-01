안녕하세요.
팀/팀원 매칭 플랫폼 '아주좋은팀' 을 개발한 팀 HYGGE 입니다.
아주좋은팀의 백엔드 API 는 아래 주소에서 확인하실 수 있습니다.
API 명세 : http://ajougoodteam.com:8080/api-docs

전체적인 패키지 구조는 다음과 같습니다.
- admin.controller : 관리자 기능의 Controller 들을 저장
- controller : 회원 기능의 Controller 들을 저장
- dto : 서비스에 이용되는 DTO 들을 저장
- entity : JPA 를 통해 관리되는 엔티티 클래스들을 저장
- error :  예외와 예외를 처리하는 클래스들을 저장
- jwt : JWT 인증에 필요한 클래스들을 저장
- repository : Repository 클래스들을 저장
- service : 서비스 로직이 담긴 클래스들을 저장
