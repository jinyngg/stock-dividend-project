
## Development Environment

- <img src="https://img.shields.io/badge/Windows-blue?style=flat&logo=windows&logoColor=white"/> 
- <img src="https://img.shields.io/badge/intellij-red?style=flat&logo=intellijidea&logoColor=white"/> 
- <img src="https://img.shields.io/badge/JDK_1.8-red?style=flat&logo=&logoColor=white"/>
- <img src="https://img.shields.io/badge/H2-blue?style=flat&logo=&logoColor=white"/>
- <img src="https://img.shields.io/badge/Gradle-skyblue?style=flat&logo=gradle&logoColor=white"/>
- <img src="https://img.shields.io/badge/Github-grey?style=flat&logo=github&logoColor=white"/>

## Dependencies
- ````Spring Web````
- ````Spring Security````
- ````Spring Data JPA````
- ````Spring Data REDIS````
- ````H2 Database````
- ````Jsoup````
- ````JsonWebToken````
- ````Lombok````
- ````apache commons````


## 1️⃣ 구현 API 리스트

✅ GET - finance/dividend/{companyName}
- 회사 이름을 인풋으로 받아서 해당 회사의 메타 정보와 배당금 정보를 반환
- 잘못된 회사명이 입력으로 들어온 경우 400 status 코드와 에러메시지 반환

![회사정보 및 배당금 조회](https://github.com/jinyngg/stock-dividend-project/assets/96164211/a24d28b0-8b68-4bce-b383-08d404970c97)

✅ GET - company/autocomplete
- 자동완성 기능을 위한 API
- 검색하고자 하는 prefix 를 입력으로 받고, 해당 prefix 로 검색되는 회사명 리스트 중 10개 반환

![자동완성](https://github.com/jinyngg/stock-dividend-project/assets/96164211/7d1cda77-0c5a-4448-b04c-abe6a135297f)

3) ✅ GET - company
- 서비스에서 관리하고 있는 모든 회사 목록을 반환
- 반환 결과는 Page 인터페이스 형태

![페이징_회사목록조회](https://github.com/jinyngg/stock-dividend-project/assets/96164211/80081a96-f42a-47f6-9584-f30b86db6a8a)

✅ POST - company
- 새로운 회사 정보 추가
- 추가하고자 하는 회사의 ticker 를 입력으로 받아 해당 회사의 정보를 스크래핑하고 저장
- 이미 보유하고 있는 회사의 정보일 경우 400 status 코드와 적절한 에러 메시지 반환
- 존재하지 않는 회사 ticker 일 경우 400 status 코드와 적절한 에러 메시지 반환

![회사_정보_추가](https://github.com/jinyngg/stock-dividend-project/assets/96164211/7037cfa5-bc43-40a5-9507-43e5b14d46c2)

✅ DELETE - company/{ticker}
- ticker 에 해당하는 회사 정보 삭제
- 삭제시 회사의 배당금 정보와 캐시도 모두 삭제되어야 함 ````Redis````

![회사 정보 삭제_Redis정보도삭제](https://github.com/jinyngg/stock-dividend-project/assets/96164211/1ec9fdf2-b6f5-49f0-b3c8-f880c1e4d1c5)

✅ POST - auth/signup
- 회원가입 API
- 중복 ID 는 허용하지 않음
- 패스워드는 암호화된 형태로 저장되어야함

![image](https://github.com/jinyngg/stock-dividend-project/assets/96164211/fbff452b-91a2-4374-8851-952d7a6f14d6)

✅ POST - auth/signin
- 로그인 API
- 회원가입이 되어있고, 아이디/패스워드 정보가 옳은 경우 JWT 발급

![image](https://github.com/jinyngg/stock-dividend-project/assets/96164211/d8012e3e-db57-4000-a9fa-05a3218166e6)
