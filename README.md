# Duaily - 빈집 거래 중개 플랫폼

<p align="center">
<img alt="image" src="https://user-images.githubusercontent.com/61505572/203564675-616e80f0-4e87-493c-8aaa-a0ef3b9dd4bf.png">
</p>

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

## 📐 시스템 아키텍처
<p align="center">
<img alt="image" src="https://user-images.githubusercontent.com/61505572/203553960-81cc4755-aebd-4c25-b5fb-1ea53d1b40a5.png">
</p>

시스템 아키텍처는 `듀얼리` 웹 시스템에 대한 아키텍처입니다. 해당 아키텍처는 사용자가 시스템을 이용하는 데 사용 되는 핵심 컴포넌트만을 담았습니다. <br/> 
사용자는 HTTPS 프로토콜을 통해 요청과 응답을 주고 받으며, 프론트엔드는 버셀을 통해 배포하였습니다. 버셀은 REST API 서버인 Spring APP과 통신을 합니다. <br/>
API 서버를 비롯한 캐싱용 서버인 Redis, 데이터 로그 수집을 위한 Prometheus, 데이터 시각화를 위한 Grafana 가 Docker 컨테이너로 EC2 에서 구동 중입니다. <br/>
DB 서버는 AWS의 RDS를 통해 구축하였고, 외장 스토리지로 S3 버킷을 사용하였습니다.

## 📐 CI/CD Workflow
<p align="center">
<img alt="image" src="https://user-images.githubusercontent.com/61505572/203554103-523c4306-3dd3-46c0-acf9-2d371c742628.png">
</p>

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

## 📐 Runtime View
<p align="center">
<img alt="image" src="https://user-images.githubusercontent.com/61505572/203554050-b27a734c-46a7-4ac5-bc41-a028bf7ea35e.png">
</p>

실행시점에 시스템 구성을 나타낸 그림입니다. 실제 사용자에서부터 데이터가 흘러가는 흐름을 파악할 수 있습니다.

## 📐 Pacakge View
<p align="center">
<img alt="image" src="https://user-images.githubusercontent.com/61505572/203555516-fb6703ff-ce0f-4442-8c45-d7f9c3f69f97.png">
</p>

Backend 내 프로젝트 구성입니다. 그림과 같이 Security config, Controller, Service, Repository, Domain 구성을 갖고 부가적인 요소들은 따로 표기하지 않았습니다.

## 📐 Infra Architecture ( AS-IS )
<p align="center">
<img alt="image" src="https://user-images.githubusercontent.com/61505572/203554074-517606fc-35b4-45ea-b053-a0443ebe9417.png">
</p>

현재 듀얼리 시스템의 인프라 아키텍처입니다. 해당 아키텍처는 프리티어 계정으로 제한된 리소스만을 가지고 구축했다는 점을 미리 양해바랍니다. <br/>
사용자는 Route 53 ( DNS )를 통해 "duaily.net"으로 접속합니다. 해당 도메인으로 들어오는 요청에 대해 ALB ( Application Load Balancing )이 HTTPS로 forwarding 합니다. <br/> 
Public 서브넷에 존재하는 WAS 서버로 요청이 들어가면, 서버 내의 Spring boot ( 8080 ) 컨테이너가 요청을 받아 수행합니다. 해당 서버는 EC2 내부에서 도커 컨테이너 간의 네트워크로 Redis와 통신하며, RDS와는 별도의 보안 그룹을 형성하여 통신하도록 하였습니다.

S3 의 경우, 이미지 객체를 보관하는 용도로 구축하였습니다. 여기에 로깅 데이터를 추가로 담을 예정입니다.

## 📐 Infra Architecture ( TO-BE )
<p align="center">
<img alt="image" src="https://user-images.githubusercontent.com/61505572/203554243-777cdc51-620e-4367-86d2-9a32681db6a6.png">
</p>

해당 아키텍처는 앞으로 확장시켜 나갈 서비스를 위한 인프라 아키텍처입니다. <br/>
WEB/WAS/DB 3 Tier 형태로 구축하여 각 계층의 독립성을 보장하고, 보안성을 고려해 WAS, DB는 private 서브넷으로 구성할 예정입니다. 또한, 프리티어 계정인 점을 감안하여 Auto-scaling 옵션 대신 EC2 인스턴스를 1대 더 두어 WAS에 대해서 이중화를 할 예정입니다. RDS는 그대로 MySQL 인스턴스를 2대의 WAS가 같이 마주보는 식으로 구성할 예정입니다. <br/>

AS-IS에서 도커 컨테이너로 구축했던 Redis를 별도의 Elastic Cache for Redis 서비스를 이용할 예정입니다. ( 해당 서비스는 프리티어 기준 제한된 리소스를 제공한다는 점에서 처음부터 도입할 것인지에 대해 고민하였습니다. )

정리하면, EC2 인스턴스는 총 3개가 필요하며, RDS는 기존 그대로 유지하되 Redis만 별도의 서비스를 이용할 것입니다. <br/>
WEB 서버에 올라가는 엔진으로는 Ngnix를 선택할 예정이며 로드밸런싱, 리버스 프록시, 경량화 등의 이유로 선정하였습니다.

## 📈 Server Metric Monitoring
<p align="center">
<img width="801" alt="image" src="https://user-images.githubusercontent.com/61505572/203554603-2fd2033f-8eca-4970-944c-58a86bd0b5e3.png">
</p>

해당 그림은 1.0.0 release 전 QA 단계의 Grafana 대시보드입니다. Prometheus와 Grafana JVM ( Micrometer )를 이용해 성능 지표를 확인합니다. <br/>
요청을 처리하는 시간대, 발생 로그, 스레드의 상태 등을 확인하는 데 사용합니다.

## 📊 Data Logging & Visualization
<p align="center">
<img width="807" alt="image" src="https://user-images.githubusercontent.com/61505572/203554452-5524b34c-83d0-498c-80f7-00f6817d6ec2.png">
</p>

해당 그림은 `1.0.0` release 전 QA 단계의 대시보드입니다. 파란색 포인트는 소셜 로그인 시 요청되는 API 시간대와 요청 수, 응답코드를 보여줍니다. <br/>
메인 페이지 로딩 이후, 소셜 로그인을 통한 회원가입 혹은 사용자의 활동을 확인할 수 있는 지표로 메인 페이지 로딩이후 로그인까지 가는 사용자의 수와 걸린 시간을 확인합니다. 이를 통해 메인 페이지에서 사용자가 매력을 느끼고 로그인까지 가는 시간과 그 수를 통해 디자인이나 제공되는 데이터의 만족도를 짐작할 수 있습니다. 이는 서비스 초창기에 가장 중요하게 확인해야 하는 포인트라고 생각합니다.

## 🔁 로컬 개발 가이드 및 협업 방식

로컬 개발 시, [WiKi 페이지](https://github.com/Duaily/backend/wiki/%EB%A1%9C%EC%BB%AC-%EA%B0%9C%EB%B0%9C-%EA%B0%80%EC%9D%B4%EB%93%9C) 링크에 가이드된 내용을 바탕으로 진행했습니다. <br/>
개발 혹은 환경 세팅 중에 발생한 이슈 내용 및 레퍼런스를 각각의 issue에 comment로 기록하며 팀원들간의 정보 공유를 활성화하였습니다.

## 📌 Commit Convention

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
