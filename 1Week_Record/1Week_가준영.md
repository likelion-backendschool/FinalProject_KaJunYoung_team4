## Title: [1Week] 가준영

## 미션 요구사항 분석 & 체크리스트

### 필수과제
- [x] 회원가입, 회원 정보 수정, 로그인, 로그아웃, 아이디 찾기
- [x] 글 작성, 글 수정, 글 리스트, 글 삭제

### 추가과제
- [x] 비밀번호 찾기
- [x] 상품 등록
- [x] 상품 수정
- [x] 상품 리스트
- [x] 상품 상세 페이지

## 1주차 미션 요약

## **[🚥 접근 방법]**
구현하기 앞서 우선 아래와 같은 순서로 개발 일정을 잡았습니다.

> Member ➡️ Post ➡️ Product

또한, 최대한 비슷한 기능 위주로 구현 우선 순위를 두고 개발을 진행했습니다.

### 1. `Member`
> 참고 레퍼런스<br>
> [메일 발송](https://victorydntmd.tistory.com/m/342)<br>
- 회원가입 / 로그인 및 로그아웃
- 회원 정보 수정 (필명, 이메일)
- 회원 정보 수정 (비밀번호)
- 아이디 찾기
- 비밀번호 찾기, 회원가입 이메일 발송

### 2. `Post`
> 참고 레퍼런스<br>
> [Markdown 렌더링](https://wikidocs.net/162799)<br>
> [ToastUI 예시](https://github.com/jhs512/comm__4th_crud)
- 게시글 작성, 게시글 리스트
- 게시글 조회, 게시글 삭제
- 게시글 수정

### 3. `Product`
- 상품 작성, 상품 리스트
- 상품 조회, 상품 삭제
- 상품 수정

---

## **[😦 발생한 이슈]**

### `534-5.7.9 application-specific password required.`

💬 상황 설명
> 비밀번호 찾기 기능을 구현하던 중 이메일 발송 과정에서 아래와 같은 에러 구문 확인<br>

```java
534-5.7.9 application-specific password required.
learn more at 534 5.7.9 https://support.google.com/mail/?p=invalidsecondfactor h5-20020a63c005000000b004639c772878sm6868282pgg.48 - gsmtp
```

✅ 해결 과정
> 검색을 해보니 Google 앱 비밀번호를 설정해야 해결된다고 나와 있어 설정한 뒤 해결 <br>
> [참고 블로그](https://dev-monkey-dugi.tistory.com/m/51)

### `Empty encoded password`
💬 상황 설명
> 비밀번호 변경 기능을 구현하던 중 아래와 같은 에러 구문 확인<br>
> `log`도 없이 단순히 아래 구문만 뜬게 이상하여 `Bean` 등록이 제대로 되어있는지, 로직에는 문제가 없는지 확인을 했지만 아무런 문제가 없었다.<br>
> 문제를 좁혀가며 로깅을 진행하였고, `MemberContext`의 `getPassword()`에서 `null`값이 찍히는 것을 확인하였다.

```java
Empty encoded password
```

✅ 해결 과정
> 해당 이슈는 `Spring Security` 내부 로직의 문제였다.<br>
> `Spring Security`는 인증을 수행한 후 `Authentication` 객체에서 암호를 바로 지우는 과정을 거치게 된다.<br>
> [참고 블로그1](https://java8.tistory.com/m/509), [참고 블로그2](https://javachoi.tistory.com/421)

```java
@PostMapping("...")
public String doModifyPassword(@AuthenticationPrincipal AuthMember authMember){
    // AuthMember == MemberContext
    if(checkMatchPassword(authMember.getMember(), modifyPasswordDto.getOldPassword())){
        ...
    }
}
```

> 즉, 위와 같이 `AuthMember`를 이용한다면 안에 포함되어 있는 `password`는 `null`값이 되버린다.<br>
> 이를 해결하기 위해서는 2가지 방안이 존재한다.<br>

1. SecurityConfig 수정
```java
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        ...
        auth.eraseCredentials(false);
    }
}
```
위와 같이 `WebSecurityConfigurerAdapter`를 상속 받은 뒤 `configure`에 `eraseCredentials(false)`를 해주면 인증 후에도 `password`를 지우지 않는다.

2. AuthMember의 username 활용
> 현재 상황으로 문제가 되는 것은 결국 `AuthMember`의 `getPassword()`이다.<br>
> 그렇기 때문에 `AuthMember.getUsername()`을 가져와 `Repository`에서 `Member` 객체를 조회하여 `password`를 가져오면 된다.

```java
// 레거시 코드
@Transactional(readOnly = true)
public boolean checkMatchPassword(Member member, String oldPassword) {
    return passwordEncoder.matches(oldPassword, member.getPassword());
}

// 수정 코드
@Transactional(readOnly = true)
public boolean checkMatchPassword(String username, String oldPassword) {
    Member currentMember = memberRepository.findByUsername(username).orElse(null);
    if (currentMember != null) {
        return passwordEncoder.matches(oldPassword, currentMember.getPassword());
    }
    return false;
}
```

---

### **[특이사항]**
> 대학교 업무로 수요일(10월 19일) 강의에 빠지게 되어 월요일, 화요일 총 이틀동안 개발을 진행한 점이 많이 아쉬웠다.<br>
> 시간을 최대한 활용해서 개발을 완료하긴 했지만, 놓친 부분이 많은 것 같다.<br>
> 리팩토링 기간에 요구사항을 천천히 다시 확인해보며 놓친 기능이 있는지 다시 확인하고, 수정할 예정이다.
<!--
구현 과정에서 아쉬웠던 점 / 궁금했던 점을 정리합니다.

- 추후 리팩토링 시, 어떤 부분을 추가적으로 진행하고 싶은지에 대해 구체적으로 작성해주시기 바랍니다.
    
    **참고: [Refactoring]**
    
    - Refactoring 시 주로 다루어야 할 이슈들에 대해 리스팅합니다.
    - 1차 리팩토링은 기능 개발을 종료한 후, 스스로 코드를 다시 천천히 읽어보면서 진행합니다.
    - 2차 리팩토링은 피어리뷰를 통해 전달받은 다양한 의견과 피드백을 조율하여 진행합니다.
-->

---

## [🛠 Refactoring]
- [ ] [`HashTag`, `Keyword`]
> 강사님의 [강의 코드](https://github.com/jhs512/sb_exam_2022_09_05__app10)를 보며 기능 구현을 구현했다.<br>
> 구현을 하는 과정에서 처음보는 `Stream`도 있었고, `extra.get("hashTags")`이라는 함수의 용도를 제대로 이해하지 못했다.<br>
> 너무 강사님 코드에 의존한 문제도 있어, 추후 리팩토링을 진행하면서 나만의 방식으로 변경을 해볼 예정이다.

- [x] `Empty encoded password`
> 위에서 기술한 이슈 중 `Empty encoded password`라는 에러가 있었다.<br>
> 해당 에러구문이 발생한 이유를 조금 더 자세히 살펴보고, 문제되는 코드를 수정할 예정이다.

- [ ] `Design`
> 기능 개발 때문에 디자인적 부분을 미처 제대로 개발하지 못했다.<br>
> 모바일 환경에서 `header`가 깨지거나, `width`가 좁아서 제대로 보여지지 않는 UI 문제가 있는 상태이다.<br>
> 코드 리팩터링을 진행하면서 이와 관련된 문제와 디자인도 신경을 써야할 것 같다.

- [x] `JavaDoc`
> 대부분의 `Service`에는 `JavaDoc`을 작성해놓았지만, 이외 부분에는 제대로 작성되지 않았다.<br>
> 추후 리팩터링을 진행하면서 작성되지 않은 메소드에 `JavaDoc`을 추가할 예정이다.

---

## [😎 Review]
### `유효성 검증`
- [x] 회원가입 시 Email 중복 체킹
- [x] Email을 이용한 아이디 찾기 -> 올바르지 않거나 null값 체킹

### `Bug`
- [x] `@CreatedDate`, `@LastModifiedDate` 미작동
> Application 클래스에 `@EnableJpaAuditing` 추가!

### `Code`
- [x] `WildCard` 수정
> IntelliJ의 설정 때문에 `import`가 되면서 자동으로 wildcard로 선언되는게 많아졌다.<br>
> wildcard를 사용할 때의 문제점은 명확한 import를 통해 실수를 줄일 수 있으며, 다른 패키지에 동일한 클래스가 존재할 경우 충돌이 발생할 수 있다.<br>
> [참고 블로그](https://blog.marcnuri.com/intellij-idea-how-to-disable-wildcard-imports)

```java
// BaseEntity
import lombok.*;
import javax.persistence.*;
```

- [x] `changeBasicInfo()` 메소드 오버로딩
> 비밀번호 변경에 대한 메소드 네이밍 또한 `nickname`, `email`을 변경하는 메소드와 같이 `changeBasicInfo()`으로 변경
- [x] `FindPwdDto` 빌더 적용
> 기존에 사용하기 위해 선언했던 `Builder`를 사용하지 않고 방치한 상태<br>
> `@Setter` 대신 `Builder`로 다시 적용
- [ ] 불필요한 정보 넘기지 않기
> `DTO` 혹은 `VO`를 활용해서 View에 불필요한 정보를 제외하기

---

## [🤔 Retrospect]
1주차 미션을 진행하면서 가장 신경을 많이 쓴 것은 유효성 검증이라 생각한다.<br>
`DB`에 가능하면 `null`이 들어가지 않도록 신경을 썼고, 대부분의 `PostMapping`에는 `@Valid`, `BindingResult`를 활용해 검증 처리를 진행했다.

가장 개발이 어려웠던 부분은 `Keyword`, `HashTag`인 것 같다.<br>
강사님이 직접 알려주신 부분이기도 했지만, 나 혼자 구현을 해보려고 하니 계속 강사님의 코드가 눈에 아른거렸다.<br>
시간이 더 많았다면 혼자서 개발을 했을 수 있을 것 같지만, 개인 사정으로 인해 3일 중 2일만 개발을 진행하여 아쉽게 강사님의 코드를 차용해 개발을 진행했다.

모든 개발이 끝나고 피어리뷰를 통해 많은 것을 배웠다.<br>
내가 발견하지 못한 버그 및 유효성 검증 등을 알 수 있었고, 팀원들이 공유해준 레퍼런스를 통해 새로운 지식들을 배울 수 있었다.<br>
가장 좋은 점은 나의 코드와 팀원의 코드를 비교하면서 개발 안목을 넓힐 수 있다는 것이었다. 