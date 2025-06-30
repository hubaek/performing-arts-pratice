# Backend Design Guidelines

본 문서는 자바와 스프링을 활용한 백엔드 개발을 위한 설계 가이드라인입니다. 올바른 도메인 설계와 객체지향 원칙을 바탕으로 유지보수성과 확장성이 높은 코드를 작성하는 것을 목표로 합니다.

## 1. 기본 원칙

### 1.1 설계 원칙
- **약한 의존성, 강한 응집도**: 컴포넌트 간 결합도는 낮추고 내부 응집도는 높입니다.
- **객체지향 5원칙(SOLID)**: 단일 책임, 개방-폐쇄, 리스코프 치환, 인터페이스 분리, 의존성 역전 원칙을 준수합니다.
- **예외 처리**: 사용자 입력에 대한 모든 예외 상황을 처리합니다.
- **테스트 커버리지**: 모든 로직에 단위 테스트를 구현합니다.

### 1.2 객체지향 생활 체조 원칙
1. **한 메서드에 오직 한 단계의 들여쓰기만 허용**
2. **else 예약어 사용 금지**
3. **모든 원시값과 문자열을 포장**
4. **한 줄에 점(.) 하나만 사용**
5. **줄여쓰기 금지(축약 금지)**
6. **모든 엔티티를 작게 유지**
7. **3개 이상의 인스턴스 변수를 가진 클래스 사용 금지**
8. **일급 콜렉션 사용**
9. **게터/세터/프로퍼티 사용 자제**

## 2. 코드 스타일 가이드

### 2.1 Java 기본 규칙

#### 2.1.1 명명 규칙
- **클래스명**: UpperCamelCase 사용 (예: `UserService`, `PaymentManager`)
- **메서드명**: lowerCamelCase 사용하며 동사 또는 동사구 (예: `sendMessage`, `editCompanyList`)
- **상수명**: UPPER_SNAKE_CASE 사용 (예: `MAX_RETRY_COUNT`, `DEFAULT_TIMEOUT_MS`)
- **변수명**: lowerCamelCase 사용하며 명사 또는 명사구 (예: `maxAge`, `userEmail`)
- **패키지명**: 소문자와 숫자만 사용 (예: `com.example.deepspace`)

#### 2.1.2 파일 구조
```java
// 1. 라이선스/저작권 정보 (선택)
// 2. 패키지 선언
package com.example.service;

// 3. import 문 (정적 import 먼저, 일반 import 나중)
import static java.util.Collections.emptyList;
import java.util.List;
import com.example.domain.User;

// 4. 클래스 선언
public class UserService {
    // 클래스 내용
}
```

#### 2.1.3 코드 포맷팅
- **들여쓰기**: 스페이스 4개 사용
- **줄 길이**: 100자 제한
- **중괄호**: K&R 스타일 (한 줄 true brace style) 사용
- **공백**: 연산자 양쪽, 제어문 키워드 뒤에 공백 삽입

```java
// 올바른 예시
if (condition) {
    doSomething();
} else {
    doSomethingElse();
}

int result = 2 + 1 * (4 / 2) - 25;
```

### 2.2 어노테이션 순서

#### 2.2.1 클래스 레벨 어노테이션
```java
@Entity
@Table(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor  
@Getter
@Setter
public class User {
    // 클래스 내용
}
```

#### 2.2.2 필드 레벨 어노테이션
```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "user_id")
private Long id;

@Column(name = "email", nullable = false)
@JoinColumn(name = "user_id")
@OneToMany(mappedBy = "user")
@Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
@NotNull
@Email
private String email;
```

## 3. 아키텍처 설계

### 3.1 패키지 구조

#### 3.1.1 도메인 기반 패키지 구조 (권장)
```
src/main/java/
├── com.example.app/
│   ├── domain/
│   │   ├── user/
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── dto/
│   │   │   ├── entity/
│   │   │   └── exception/
│   │   └── product/
│   │       ├── controller/
│   │       ├── service/
│   │       ├── dto/
│   │       ├── entity/
│   │       └── exception/
│   ├── global/
│   │   ├── config/
│   │   ├── exception/
│   │   ├── type/
│   │   └── util/
│   └── infra/
│       ├── service/
│       ├── dto/
│       └── entity/
```

### 3.2 레이어 역할 정의

#### 3.2.1 Controller Layer
- **책임**: API 엔드포인트 정의 및 요청/응답 처리
- **명명 규칙**: 
  - REST API: `{Domain}ApiController`
  - 일반 Controller: `{Domain}Controller`

```java
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@RestController
public class UserApiController {
    
    private final UserService userService;
    
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(
            @Valid @RequestBody UserCreateRequestDto requestDto) {
        UserResponseDto response = userService.createUser(requestDto);
        return ResponseEntity.ok(response);
    }
}
```

#### 3.2.2 Service Layer
- **책임**: 비즈니스 로직 및 트랜잭션 관리
- **명명 규칙**: `{Domain}Service`
- **원칙**: 단일 책임 원칙에 따라 적절한 크기 유지

```java
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {
    
    private final UserRepository userRepository;
    
    @Transactional
    public UserResponseDto createUser(UserCreateRequestDto requestDto) {
        validateUserRequest(requestDto);
        
        User user = User.builder()
                .email(requestDto.getEmail())
                .name(requestDto.getName())
                .build();
                
        User savedUser = userRepository.save(user);
        return UserResponseDto.from(savedUser);
    }
    
    private void validateUserRequest(UserCreateRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new EmailDuplicateException("Email already exists: " + requestDto.getEmail());
        }
    }
}
```

#### 3.2.3 Repository Layer
- **책임**: 데이터 접근 및 영속성 관리
- **명명 규칙**: `{Entity}Repository`

```java
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
```

### 3.3 DTO 설계 원칙

#### 3.3.1 명명 규칙
- 형식: `{Name}{Behavior}{Request/Response}Dto`
- 예시: `UserCreateRequestDto`, `UserEditResponseDto`

```java
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequestDto {
    
    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String email;
    
    @NotBlank(message = "이름은 필수입니다")
    @Size(min = 2, max = 50, message = "이름은 2-50자 사이여야 합니다")
    private String name;
}

@Getter
@Builder
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String email;
    private String name;
    private LocalDateTime createdAt;
    
    public static UserResponseDto from(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
```

## 4. 예외 처리 전략

### 4.1 예외 계층 구조

```java
// 최상위 비즈니스 예외
public abstract class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;
    
    protected BusinessException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}

// 도메인별 예외
public class EmailDuplicateException extends BusinessException {
    public EmailDuplicateException(String message) {
        super(message, ErrorCode.EMAIL_DUPLICATE);
    }
}

public class EntityNotFoundException extends BusinessException {
    public EntityNotFoundException(String message) {
        super(message, ErrorCode.ENTITY_NOT_FOUND);
    }
}
```

### 4.2 에러 코드 관리

```java
@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE(400, "C001", "잘못된 입력값입니다"),
    METHOD_NOT_ALLOWED(405, "C002", "지원하지 않는 HTTP 메서드입니다"),
    ENTITY_NOT_FOUND(404, "C003", "엔터티를 찾을 수 없습니다"),
    INTERNAL_SERVER_ERROR(500, "C004", "서버 에러입니다"),
    
    // User Domain
    EMAIL_DUPLICATE(400, "U001", "이미 사용중인 이메일입니다"),
    USER_NOT_FOUND(404, "U002", "사용자를 찾을 수 없습니다");
    
    private final int status;
    private final String code;
    private final String message;
}
```

### 4.3 통합 예외 처리

```java
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.error("BusinessException", e);
        final ErrorResponse response = ErrorResponse.of(e.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(e.getErrorCode().getStatus()));
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}

@Getter
@Builder
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private int status;
    private String code;
    private List<FieldError> errors;
    
    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .message(errorCode.getMessage())
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .errors(Collections.emptyList())
                .build();
    }
    
    public static ErrorResponse of(ErrorCode errorCode, BindingResult bindingResult) {
        return ErrorResponse.builder()
                .message(errorCode.getMessage())
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .errors(FieldError.of(bindingResult))
                .build();
    }
}
```

## 5. 테스트 전략

### 5.1 테스트 계층

#### 5.1.1 통합 테스트
```java
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public abstract class IntegrationTest {
    
    @Autowired
    protected MockMvc mockMvc;
    
    @Autowired
    protected ObjectMapper objectMapper;
}

public class UserApiTest extends IntegrationTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    @DisplayName("사용자 생성 성공")
    void createUser_Success() throws Exception {
        // given
        UserCreateRequestDto requestDto = UserCreateRequestDto.builder()
                .email("test@example.com")
                .name("테스트 사용자")
                .build();
        
        // when & then
        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.name").value("테스트 사용자"));
    }
}
```

#### 5.1.2 서비스 테스트
```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @InjectMocks
    private UserService userService;
    
    @Mock
    private UserRepository userRepository;
    
    @Test
    @DisplayName("사용자 생성 성공")
    void createUser_Success() {
        // given
        UserCreateRequestDto requestDto = UserCreateRequestDto.builder()
                .email("test@example.com")
                .name("테스트 사용자")
                .build();
        
        User savedUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .name("테스트 사용자")
                .build();
        
        given(userRepository.existsByEmail(anyString())).willReturn(false);
        given(userRepository.save(any(User.class))).willReturn(savedUser);
        
        // when
        UserResponseDto result = userService.createUser(requestDto);
        
        // then
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        assertThat(result.getName()).isEqualTo("테스트 사용자");
    }
    
    @Test
    @DisplayName("중복 이메일로 인한 사용자 생성 실패")
    void createUser_EmailDuplicate_ThrowsException() {
        // given
        UserCreateRequestDto requestDto = UserCreateRequestDto.builder()
                .email("test@example.com")
                .name("테스트 사용자")
                .build();
        
        given(userRepository.existsByEmail(anyString())).willReturn(true);
        
        // when & then
        assertThatThrownBy(() -> userService.createUser(requestDto))
                .isInstanceOf(EmailDuplicateException.class)
                .hasMessage("Email already exists: test@example.com");
    }
}
```

### 5.2 Repository 테스트
```java
@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    @DisplayName("이메일로 사용자 존재 여부 확인")
    void existsByEmail() {
        // given
        User user = User.builder()
                .email("test@example.com")
                .name("테스트 사용자")
                .build();
        entityManager.persist(user);
        entityManager.flush();
        
        // when
        boolean exists = userRepository.existsByEmail("test@example.com");
        boolean notExists = userRepository.existsByEmail("notexist@example.com");
        
        // then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}
```

## 6. 코드 품질 관리

### 6.1 코드 리뷰 체크리스트

#### 6.1.1 기본 원칙 준수
- [ ] 단일 책임 원칙 준수
- [ ] 적절한 예외 처리
- [ ] 테스트 코드 작성
- [ ] 명명 규칙 준수

#### 6.1.2 성능 고려사항
- [ ] N+1 쿼리 문제 확인
- [ ] 트랜잭션 범위 최적화
- [ ] 적절한 인덱스 사용
- [ ] 불필요한 객체 생성 최소화

#### 6.1.3 보안 고려사항
- [ ] 입력값 검증
- [ ] SQL 인젝션 방지
- [ ] 민감 정보 로깅 금지
- [ ] 적절한 권한 검사

### 6.2 코드 품질 도구 활용
- **정적 분석**: SonarQube, SpotBugs
- **코드 포맷팅**: Google Java Format
- **테스트 커버리지**: JaCoCo
- **의존성 관리**: OWASP Dependency Check

## 7. 마무리

본 가이드라인은 지속적으로 업데이트되며, 팀의 합의를 통해 개선해 나갑니다. 모든 개발자는 이 가이드라인을 숙지하고 준수하여 일관성 있고 품질 높은 코드를 작성해야 합니다.

### 7.1 참고 자료
- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- [Spring Boot Best Practices](https://spring.io/guides)
- [Clean Code by Robert C. Martin](https://www.amazon.com/Clean-Code-Handbook-Software-Craftsmanship/dp/0132350882)
- [Effective Java by Joshua Bloch](https://www.amazon.com/Effective-Java-Joshua-Bloch/dp/0134685997)

### 7.2 주석 템플릿
```java
/**
 * @date : 2025-06-30
 * @author : [작성자명]
 * @description : [클래스 설명]
 */
```