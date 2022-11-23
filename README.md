# Duaily - 빈집 거래 중개 플랫폼
<img width="450" alt="image" src="https://user-images.githubusercontent.com/61505572/203564675-616e80f0-4e87-493c-8aaa-a0ef3b9dd4bf.png">

## 🎙 프로젝트 설명

듀얼리는 흩어져 있던 듀얼 라이프 정보를 제공할뿐만 아니라 빈집 거래를 통해 
듀얼 라이프 실현까지 도우며 빈집 문제까지도 해결하는 것을 목표로 하는 플랫폼입니다.

핵심 기능은 다음과 같습니다.

- 빈 집 게시글을 작성하고 조회
- 빈 집 게시글을 통한 거레
- 듀얼 라이프에 관심 있는 사용자들간의 커뮤니티

## 🛠 기술 스택

`Springboot(Java)`를 이용한 API 서버를 개발하였고, `JPA(ORM)`과 `MySQL`을 이용한 CRUD 구현하였습니다.
<br/> 
`AWS EC2`를 이용해 서버를 배포하였고, `AWS RDS`를 이용해 DB 서버를 구성하였습니다. <br/>
`AWS S3`를 이용해 이미지 데이터를 관리하였습니다. <br/>
`Redis`를 이용해 인증을 위한 토큰 정보와 인증코드 정보를 관리하였습니다. <br/>
`Prometheus`를 이용해 서버의 데이터 로그를 수집하고, `Grafana`를 이용해 데이터를 시각화하였습니다. <br/>
`Docker`를 이용해 EC2 서버 내에서 API 서버, Redis, Prometheus, Grafana를 컨테이너화하여 구축하였습니다.
협업 및 코드 버전 관리를 위해 `github`을 이용하였으며, API 문서화 도구로 `Spring Rest Docs`를 사용하였습니다. <br/>
자동 배포를 위해 `github actions`를 이용하였습니다.

## 📄 개발 내용
- JWT 토큰 + Spring Security 를 이용한 소셜 로그인 ( kakao )
- 빈 집 게시글 작성|상세조회|목록조회
- 후기 게시글 작성|상세조회|목록조회
- 지역 게시글 조회
- 사용자 정보 조회
- 전화번호 인증
- 이미지 업로드|삭제
- 거래 생성|거래 완료

## 시스템 아키텍처
![Group 954](https://user-images.githubusercontent.com/61505572/203553960-81cc4755-aebd-4c25-b5fb-1ea53d1b40a5.png)


## CI/CD Workflow
![밋업데이-아키텍처-cicd-flow drawio (2)](https://user-images.githubusercontent.com/61505572/203554103-523c4306-3dd3-46c0-acf9-2d371c742628.png)


1.	로컬 `feature` 브랜치 ( Dual-# )에서 origin의 `feature`로 push
2.	origin의 `feature` 브랜치에서 origin의 `develop` 브랜치로 push
3.	github actions CI/CD 실행 <br/>
      A.	Java 11 버전 설치 <br/>
      B.	Gradle 캐싱 <br/>
      C.	Gradle 권한 부여 <br/>
      D.	DB, Redis, aws 등 정보를 담은 application-secret.yml 파일 값 세팅 <br/>
      E.	jar 파일 생성 <br/>
      F.	Dockerfile 빌드 및 Docker hub 로 push <br/>
      G.	AWS EC2에 deploy <br/>
가장 최근 배포 시간 기준, `1분 19초` 소요. <br/>

## Runtime View
![밋업데이-아키텍처-runtime-view drawio](https://user-images.githubusercontent.com/61505572/203554050-b27a734c-46a7-4ac5-bc41-a028bf7ea35e.png)

실행시점에 시스템 구성을 나타낸 그림입니다. 실제 사용자에서부터 데이터가 흘러가는 흐름을 파악할 수 있습니다.

## Pacakge View
![밋업데이-아키텍처-allocation-view drawio (1)](https://user-images.githubusercontent.com/61505572/203555516-fb6703ff-ce0f-4442-8c45-d7f9c3f69f97.png)

Backend 내 프로젝트 구성입니다. 그림과 같이 Security config, Controller, Service, Repository, Domain 구성을 갖고 부가적인 요소들은 따로 표기하지 않았습니다.

## Infra Architecture ( AS-IS )
![밋업데이-아키텍처-infra-acrhi drawio](https://user-images.githubusercontent.com/61505572/203554074-517606fc-35b4-45ea-b053-a0443ebe9417.png)


## Infra Architecture ( TO-BE )
![infra-tobe](https://user-images.githubusercontent.com/61505572/203554243-777cdc51-620e-4367-86d2-9a32681db6a6.png)

## Server Metric Monitoring
<img width="801" alt="image" src="https://user-images.githubusercontent.com/61505572/203554603-2fd2033f-8eca-4970-944c-58a86bd0b5e3.png">

해당 그림은 1.0.0 release 전 QA 단계의 Grafana 대시보드입니다. Prometheus와 Grafana JVM ( Micrometer )를 이용해 성능 지표를 확인합니다. <br/>
요청을 처리하는 시간대, 발생 로그, 스레드의 상태 등을 확인하는 데 사용합니다.

## Data Logging & Visualization
<img width="807" alt="image" src="https://user-images.githubusercontent.com/61505572/203554452-5524b34c-83d0-498c-80f7-00f6817d6ec2.png">


해당 그림은 `1.0.0` release 전 QA 단계의 대시보드입니다. 파란색 포인트는 소셜 로그인 시 요청되는 API 시간대와 요청 수, 응답코드를 보여줍니다. <br/>
메인 페이지 로딩 이후, 소셜 로그인을 통한 회원가입 혹은 사용자의 활동을 확인할 수 있는 지표로 메인 페이지 로딩이후 로그인까지 가는 사용자의 수와 걸린 시간을 확인합니다. 이를 통해 메인 페이지에서 사용자가 매력을 느끼고 로그인까지 가는 시간과 그 수를 통해 디자인이나 제공되는 데이터의 만족도를 짐작할 수 있습니다. 이는 서비스 초창기에 가장 중요하게 확인해야 하는 포인트라고 생각합니다.


## Commit Convention

### Structure
    // 예시
    feat: Create get all region posts API

### Type
- feat: 기능 구현 시
- fix: 버그 수정 시
- refactor: 리펙토링 시
- docs: API 문서 작업 시
- test: 테스트 코드 작성 시
- chore: 환경 세팅 작업 시

### How to use
- 타입의 첫 글자는 대문자로 시작하기
- 메세지 내용은 50자 이내로 작성하기
- 문장을 동사 원형으로 시작하는 명령문 형태로 작성하기
