# 산학협력캡스톤설계
## Client
- Android, java
- Glide(이미지 로드)
- Retrofit2 (서버와 통신)
- Gson(json 파싱)
- css, jquery, javascript (설문지를 받는 사용자 화면 구성)

## Server
- Node js
- Socket io 모듈 사용
- MySQL

## 사용된 디자인 패턴
- Singleton pattern
  - 로컬 저장소 사용시 connection 관리
  - 서버 연동을 관리하는 클래스등에 사용
- Factory pattern
  - 설문 생성할때 (긴글,짧은글,그리드,체크박스,이미지) 등의 객체 생성에 사용되는 클래스에 적용
- builder pattern
  - 그리드의 열과 행 , 각종 설문의 동적인 뷰들을 생성할때 

## 개발 기간
- 2019.12.14~2020.5.28

## 프로젝트 소개 및 기능
- 모바일 리서치 & 소셜 커넥팅 <br>
설문을 통해 개인이 궁금한 부분이나 설문을 제작하여 간편하게 만들고 결과를 받아 볼 수 있습니다
특정 분야에 관심있는 사람들을 모을 수 있으며 채팅또는 간편한 그룹 만들기를 통해 쉽게 관심사가 비슷한 사람들의
커뮤니티를 생성할 수 있습니다.
  * 로그인
    - 구글 로그인 연동
    
  * 모바일 리서치
    - 개인에게 설문작성 툴을 제공합니다.
    - url을 통해 설문을 배포할 수 있습니다.
    - 개개인의 설문결과를 받아볼 수 있으며 결과분석 기능을 제공합니다
   
  * 소셜 커넥팅
    - 관심있는 설문과 관련된 제목들이 워드클라우드 형태로 표시되어 설문에 참여할 수 있습니다.
    - 프로필 설정 및 친구 추가 가능
    - 채팅방을 생성하고 자신의 친구들을 초대하여 채팅 가능
    - 그룹생성가능(비공개/공개 선택가능)
    - 그룹에서 게시물을 생성및 이미지 업로드 가능
  
  ## 동작화면
  
  ## 로그인 및 시작화면
 
  <div>
    <img width="200" src="https://user-images.githubusercontent.com/37110261/79632648-a6687900-819b-11ea-8c16-d14186d3f0e7.png">
    <img width="200" src="https://user-images.githubusercontent.com/37110261/79632593-4e317700-819b-11ea-94b0-e38760db4e30.png">
  </div>
  
  ## 설문 생성
  
  <div>
    <img width="200" src="https://user-images.githubusercontent.com/37110261/79632748-3c040880-819c-11ea-9a1d-cf37a3c1831f.png">
    <img width="200" src="https://user-images.githubusercontent.com/37110261/79632769-4faf6f00-819c-11ea-892e-368486366fdb.png">
    <img width="200" src="https://user-images.githubusercontent.com/37110261/79632788-7bcaf000-819c-11ea-9edc-e3e775ee0fc7.png">
  </div>
  
  <div>
    <img width="200" src="https://user-images.githubusercontent.com/37110261/79632862-fc89ec00-819c-11ea-9271-7f582a49278e.png">
    <img width="200" src="https://user-images.githubusercontent.com/37110261/79632864-fdbb1900-819c-11ea-8506-7b384e01a1b1.png">
  </div>
  
  ## 설문 편집, 삭제, 미리보기, 공유
  
  <div>
    <img width="200" src="https://user-images.githubusercontent.com/37110261/79632929-525e9400-819d-11ea-844d-fd51cfa90a59.png">
    <img width="200" src="https://user-images.githubusercontent.com/37110261/79632931-538fc100-819d-11ea-9fcd-cd9b6ff0c1d9.png">
  </div>
  
  ## 설문 결과 분석
  
  <div>
    <img width="200" src="https://user-images.githubusercontent.com/37110261/79632997-ac5f5980-819d-11ea-91d6-a78c594e7af6.png">
    <img width="200" src="https://user-images.githubusercontent.com/37110261/79632999-ae291d00-819d-11ea-9a58-068885b0cc93.png">
    <img width="200" src="https://user-images.githubusercontent.com/37110261/79633002-b08b7700-819d-11ea-9505-8316e2d8532a.png">
  </div>
  
  ## 그룹 생성
  
  <div>
    <img width="200" src="https://user-images.githubusercontent.com/37110261/79633138-9c944500-819e-11ea-92dc-8e1bf3bc3ccf.png">
    <img width="200" src="https://user-images.githubusercontent.com/37110261/79633140-9e5e0880-819e-11ea-824c-d20c9276d34f.png">
    <img width="200" src="https://user-images.githubusercontent.com/37110261/79633142-a027cc00-819e-11ea-84c3-3b37d26b5729.png">
  </div>
  
  ## 채팅방 생성 및 채팅
  
  <div>
    <img width="200" src="https://user-images.githubusercontent.com/37110261/79633289-70c58f00-819f-11ea-82e2-5cfbc0c5a1d8.png">
    <img width="200" src="https://user-images.githubusercontent.com/37110261/79633282-6dca9e80-819f-11ea-88d8-c84d675db0c8.png">
    <img width="200" src="https://user-images.githubusercontent.com/37110261/79633285-6f946200-819f-11ea-9bb5-fbc2272b1045.png">
    <img width="200" src="https://user-images.githubusercontent.com/37110261/79633287-702cf880-819f-11ea-98d3-e99c8aa9a067.png">
  </div>
  
  ## 워드 클라우드 및 위젯
  
  <div>
    <img width="200" src="https://user-images.githubusercontent.com/37110261/79633342-c732cd80-819f-11ea-87f6-b6327d72878e.png">
    <img width="200" src="https://user-images.githubusercontent.com/37110261/79633343-c8fc9100-819f-11ea-9892-0a2f8696be2b.png">
  </div>
  
  ## 친구 초대 및 친구 목록
  
   <div>
    <img width="200" src="https://user-images.githubusercontent.com/37110261/79633361-ee899a80-819f-11ea-9a8f-dbd356f6c739.png">
    <img width="200" src="https://user-images.githubusercontent.com/37110261/79633362-f0535e00-819f-11ea-9600-93df47e3d611.png">
  </div>
  
