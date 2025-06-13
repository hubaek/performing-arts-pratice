# CLAUDE.md

이 파일은 Claude Code (claude.ai/code)가 이 저장소에서 작업할 때 필요한 가이드라인을 제공합니다.

## 개발 명령어

### 백엔드 (Spring Boot)
- **빌드**: `./gradlew build` (Linux/Mac) 또는 `gradlew.bat build` (Windows)
- **실행**: `./gradlew bootRun`
- **테스트**: `./gradlew test`
- **클린 빌드**: `./gradlew clean build`

### 프론트엔드 (React)
- **의존성 설치**: `cd frontend && npm install`
- **개발 서버 시작**: `cd frontend && npm start` (http://localhost:3000에서 실행)
- **빌드**: `cd frontend && npm run build`
- **테스트**: `cd frontend && npm test`

### 데이터베이스 설정
- localhost:3306의 MySQL 사용, 데이터베이스명: `performing_arts`
- 기본 인증정보: 사용자명 `root`, 비밀번호 `12341234`
- 테스트용 H2 데이터베이스도 설정됨

## 아키텍처 개요

### 프로젝트 구조
백엔드와 프론트엔드가 분리된 풀스택 애플리케이션:
- **백엔드**: Spring Boot REST API (Java 17, 포트 8080)
- **프론트엔드**: React TypeScript SPA (포트 3000)

### 백엔드 아키텍처 (Spring Boot)
- **도메인 주도 설계**: 비즈니스 도메인별로 코드 구조화 (`auth`, `member`, `practice`, `team` 등)
- **보안**: 리프레시 토큰을 사용한 JWT 기반 인증
- **데이터**: MySQL과 JPA/Hibernate, `@EnableJpaAuditing`을 통한 감사 타임스탬프
- **API**: `/api` 접두사 하위의 RESTful 엔드포인트

주요 백엔드 도메인:
- `auth`: 인증, 로그인, 회원가입, 토큰 관리
- `member`: 역할 기반 사용자 프로필 관리 (USER/ADMIN)
- `practice`: 연습 세션 관리 및 완료 추적
- `team`: 팀/그룹 관리
- `practiceParticipation`: 연습 참석 추적

### 프론트엔드 아키텍처 (React)
- **도메인 구조**: `src/domains/` 내 비즈니스 도메인별 기능 구성
- **공유 리소스**: `src/shared/` 내 공통 컴포넌트, 훅, 타입, API 클라이언트
- **UI 프레임워크**: 일관된 디자인 시스템을 위한 Material-UI (MUI)
- **인증**: 보호된 라우트와 컨텍스트 기반 인증 상태 관리
- **API 통합**: 자동 토큰 갱신이 포함된 중앙집중식 Axios 클라이언트

프론트엔드 도메인 구조:
- `domains/auth/`: 로그인, 회원가입, 인증 컴포넌트
- `domains/practice/`: 연습 관리 (목록, 상세, 폼, 완료)
- `domains/team/`: 팀 관리 컴포넌트
- `domains/admin/`: 관리자 인터페이스 컴포넌트

### 타입 시스템 및 데이터 흐름
- **백엔드**: API 경계를 위한 Entity → DTO 패턴
- **프론트엔드**: TypeScript 인터페이스로 강타입 지정
- **타임스탬프 일관성**: 두 가지 타임스탬프 패턴:
  - `LegacyTimestampedEntity`: `modifiedAt` 사용 (auth/member 도메인)
  - `TimestampedEntity`: `updatedAt` 사용 (practice/team 등 신규 도메인)

### 인증 흐름
1. `/api/auth/login`을 통한 로그인으로 `accessToken`과 `refreshToken` 반환
2. 프론트엔드에서 토큰을 localStorage에 저장
3. Axios 인터셉터가 요청에 Bearer 토큰 자동 첨부
4. 401 응답 시 자동 토큰 갱신
5. `ProtectedRoute`와 `AdminRoute` 컴포넌트를 통한 보호된 라우트 인증 강제

### 주요 통합 지점
- API 기본 URL: `http://localhost:8080/api`
- 토큰 저장 키: `accessToken`, `refreshToken`
- 관리자 전용 기능은 `@AdminOnly` 어노테이션(백엔드)과 `AdminRoute`(프론트엔드)로 보호

### 코드 품질 표준
`FRONTEND_GUIDELINES.md`의 포괄적인 프론트엔드 가이드라인을 따름:
- **가독성**: 명명된 상수, 추상화된 로직, 명확한 조건부 렌더링
- **예측 가능성**: 일관된 반환 타입, 단일 책임 함수
- **응집도**: 도메인 기반 구성, 폼 검증 패턴
- **낮은 결합도**: 집중된 훅, 프롭 드릴링보다 컴포넌트 컴포지션

### 개발 워크플로우
- 새로운 기능에 도메인별 폴더 사용
- 기존 TypeScript 인터페이스 및 명명 규칙 준수
- 적절한 에러 처리 및 폼 검증 구현
- 성능 최적화를 위한 `useCallback` 사용
- 접근성을 위한 `aria-label` 속성 추가
- 입력 형식 검증 포함 (날짜, 시간, 전화번호)