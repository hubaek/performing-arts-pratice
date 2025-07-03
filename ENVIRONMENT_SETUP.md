# 환경 설정 가이드

## 초기 설정

프로젝트를 처음 실행하기 전에 설정 파일을 복사하고 환경에 맞게 수정해야 합니다.

```bash
# 설정 파일 복사
cp src/main/resources/application.yml.sample src/main/resources/application.yml
cp src/main/resources/application-dev.yml.sample src/main/resources/application-dev.yml
cp src/main/resources/application-prod.yml.sample src/main/resources/application-prod.yml
cp src/main/resources/application-test.yml.sample src/main/resources/application-test.yml

# application-dev.yml에서 YOUR_DB_PASSWORD를 실제 비밀번호로 변경
```

⚠️ **중요**: 실제 설정 파일(.yml)은 민감정보가 포함되므로 Git에 커밋하지 마세요!

## 환경별 프로필 설정

이 프로젝트는 환경별로 설정을 분리하여 보안을 강화했습니다.

### 개발 환경 (dev)

기본적으로 `dev` 프로필이 활성화됩니다.

**특징:**
- MySQL 데이터베이스 사용
- DEBUG 레벨 로깅
- SQL 쿼리 로그 출력
- 콘솔과 파일 모두 로그 출력

### 테스트 환경 (test)

테스트 실행 시 자동으로 활성화되는 프로필입니다.

**특징:**
- H2 인메모리 데이터베이스 사용
- 테스트마다 스키마 재생성 (create-drop)
- H2 콘솔 활성화 (/h2-console)
- 테스트 전용 로깅 설정

```bash
./gradlew bootRun
# 또는
java -jar build/libs/performing-arts-practice-0.0.1-SNAPSHOT.jar
```

### 운영 환경 (prod)

운영 환경에서는 환경변수를 통해 설정을 주입해야 합니다.

#### 필수 환경변수

```bash
export DB_URL=jdbc:mysql://your-db-host:3306/performing_arts
export DB_USERNAME=your-db-username
export DB_PASSWORD=your-db-password
export PORT=8080
```

#### 실행 방법

```bash
# 환경변수 설정 후
java -jar -Dspring.profiles.active=prod build/libs/performing-arts-practice-0.0.1-SNAPSHOT.jar

# 또는 환경변수를 직접 전달
java -jar -Dspring.profiles.active=prod \
  -DDB_URL=jdbc:mysql://localhost:3306/performing_arts \
  -DDB_USERNAME=root \
  -DDB_PASSWORD=your-password \
  build/libs/performing-arts-practice-0.0.1-SNAPSHOT.jar
```

### Docker 환경

Docker를 사용하는 경우 환경변수를 .env 파일이나 docker-compose.yml에서 설정할 수 있습니다.

```dockerfile
# Dockerfile 예시
FROM openjdk:17-jre-slim
COPY build/libs/performing-arts-practice-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/app.jar"]
```

```yaml
# docker-compose.yml 예시
version: '3.8'
services:
  app:
    image: your-app:latest
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - DB_URL=jdbc:mysql://mysql:3306/performing_arts
      - DB_USERNAME=root
      - DB_PASSWORD=${DB_PASSWORD}
      - PORT=8080
    ports:
      - "8080:8080"
    depends_on:
      - mysql
  
  mysql:
    image: mysql:8.0
    environment:
      - MYSQL_ROOT_PASSWORD=${DB_PASSWORD}
      - MYSQL_DATABASE=performing_arts
    ports:
      - "3306:3306"
```

## 로깅 설정

### 개발 환경
- 콘솔과 파일에 모두 로그 출력
- DEBUG 레벨로 상세한 로그 확인 가능
- SQL 쿼리 로그 활성화

### 운영 환경
- JSON 구조화 로그 출력
- 비동기 로깅으로 성능 최적화
- 로그 파일 압축 및 로테이션
- 민감정보 마스킹 적용

### 로그 파일 위치
- 개발환경: `logs/application.log`
- 운영환경: `logs/application.YYYY-MM-DD.log.gz`

## 보안 고려사항

1. **민감정보 보호**: 
   - 모든 설정 파일(.yml)은 `.gitignore`에 추가됨
   - 템플릿 파일(.sample)만 저장소에 포함
   - 데이터베이스 연결 정보는 환경변수로 관리
2. **로그 보안**: 개인정보 자동 마스킹 적용
3. **프로필 분리**: 환경별 설정 완전 분리

## 설정 파일 구조

```
src/main/resources/
├── application.yml.sample              # 기본 설정 템플릿
├── application-dev.yml.sample          # 개발환경 템플릿  
├── application-prod.yml.sample         # 운영환경 템플릿
├── application-test.yml.sample         # 테스트환경 템플릿
├── logback-spring.xml                  # 로깅 설정
├── application.yml                     # 실제 기본 설정 (Git 제외)
├── application-dev.yml                 # 실제 개발 설정 (Git 제외)
├── application-prod.yml                # 실제 운영 설정 (Git 제외)
└── application-test.yml                # 실제 테스트 설정 (Git 제외)
```

## 문제 해결

### 프로필 확인
애플리케이션 시작 시 다음과 같은 로그로 활성 프로필을 확인할 수 있습니다:

```
INFO c.c.p.PerformingArtsPracticeApplication : The following profiles are active: dev
```

### 데이터베이스 연결 오류
- 설정 파일이 올바르게 복사되었는지 확인
- 비밀번호가 올바르게 설정되었는지 확인
- 데이터베이스 서버가 실행 중인지 확인
- 방화벽 설정 확인

### 설정 파일 누락 오류
```bash
# 설정 파일이 없는 경우
cp src/main/resources/application*.sample src/main/resources/
# 파일명에서 .sample 제거 후 내용 수정
```