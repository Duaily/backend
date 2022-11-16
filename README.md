# 구해줘 홈즈

## How to start

1. 로컬 환경에서 원하는 위치에서 `git bash` 를 실행한다. 아래 명령어를 입력한다..

```bash
    git clone https://github.com/Duaily/backend.git
```

2. `Docker Desktop`을 실행한다. <br/>
3. 인텔리제이 IDEA를 실행한 뒤, 빌드가 끝나면 터미널에서 아래 명령어를 입력한다.

```bash
    docker-compose up
```

4. 인텔리제이 `run` 버튼을 눌러 프로젝트가 성공적으로 실행되는 지 확인한다.


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