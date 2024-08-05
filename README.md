# 강의로 습득한 기술을 직접 구현 - Spring boot REST API
## 구현 기술
- Spring boot 환경
  - Exception Handler 구현
  - Response Entity 객체 공통화
- REST API
  - Rest(GET, POST, PUT, DELETE, UPDATE)를 활용하여 BackEnd 로직 구현
  - 변환을 위한 Mapper Lib 선택 및 구현
    - **비교 사례**
      - ObjectMapper(Spring default) **-> 부분 선택**
        - RunTime 시에 객체를 변환
        - 런타임 변환으로 속도가 느리다
        - 주 사용 사례 :: Json <-> Java 객체 간 변환
      - ModelMapper(Lib)
        - RunTime 시에 객체를 변환
        - 리플렉션 개념으로 접근, 타입 접근성은 좋지만 소모성이 많다
        - 런타임 변환으로 속도가 느리다
        - 주 사용 사례 :: Java 객체간 변환
      - MapStruct (Lib) **-> 선택**
        - Compile 시에 객체를 변환
        - 컴파일 변환 타입 -> 런타임 방식 보다 속도 보장
        - 컴파일 단계에서 에러를 발견 가능 -> 안전성 보장
        - Mapper Interface 를 통한 변환 -> 코드 가독성 및 유지보수성 향상
        - 주 사용 사례 :: Dto, Entity, Vo 등 객체간 변환
- JPA
  - Entity 설계
  - 복잡한 조건 쿼리 Query Dsl 구현
    - **비교 사례**
      - JPA @Query 어노테이션
        - 쿼리를 문자열로 작성하여 컴파일 시점에 오류를 확인 불가
        - 정적 쿼리에 적합
        - 복잡한 쿼리는 가독성이 떨어질 수 있다
      - Query Dsl **-> 선택**
        - 컴파일 시점에 쿼리 오류를 확인 가능
        - IDE 자동완성을 통해 쿼리 작성 가능
        - 쿼리를 객체화하여 가독성이 높아진다
        - 쿼리를 재사용하기 용이하다
- Spring Security
  - Custom Filter 구현
  - Authentication 객체 및 인증 Provider 구현
  - Filter를 통한 Login 처리 및 토큰 발급
- JWT
  - JWT Lib 를 통한 로그인 시 토큰 발급 과정 구현
    - **비교 사례**
      - Spring Security Basic Token (Spring Security)
        - Spring Security 프레임워크 내장 
        - 제한적 - Security 설정에 의존
        - 기본적인 Base64 인코딩 토큰
      - JJWT (Lib) **-> 선택**
        - JWT 형식의 토큰을 발급
        - 유연성 - 여러 설정 가능(만료 시간, 암호화 알고리즘, 다양한 Claim 등)
        - 토큰의 유효성 검사 가능 - 만료, 형식 등 Exception 처리 가능
  - Refresh 토큰을 이용한 토큰 재 발급 구현
- Test Code
  - Junit 활용 테스트 코드 작성
  - Mock 객체 활용
  - Document 작성(Spring Restdocs)
    - **비교 사례**
      - Swagger
        - API 문서화 및 테스트 가능
        - 문서화를 위한 코드 작성이 필요 (별도 어노테이션)
        - 실제 코드와 동기화가 안될 수 있다.
        - 코드 변경 시 자동으로 업데이트 되지 않는다
      - Spring Restdocs **-> 선택**
        - 테스트 코드 기반 API 문서화
        - 실제 API 코드와 동기화
        - 테스트 코드 작성 시 문서 동시 작성(세부적인 커스텀 가능)
        - 테스트 코드 실행 성공 시 문서화 생성
