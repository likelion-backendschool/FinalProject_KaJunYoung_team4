## Title: [1Week] 가준영

### 미션 요구사항 분석 & 체크리스트

---

## 필수과제
- [ ] 회원가입, 회원 정보 수정, 로그인, 로그아웃, 아이디 찾기
- [ ] 글 작성, 글 수정, 글 리스트, 글 삭제

## 추가과제
- [ ] 비밀번호 찾기
- [ ] 상품 등록
- [ ] 상품 수정
- [ ] 상품 리스트
- [ ] 상품 상세 페이지

### 1주차 미션 요약

---

**[접근 방법]**
<!-- 
체크리스트를 중심으로 각각의 기능을 구현하기 위해 어떤 생각을 했는지 정리합니다.

- 무엇에 중점을 두고 구현하였는지, 어떤 공식문서나 예제를 참고하여 개발하였는지 뿐만 아니라 미션을 진행하기 전 개인적으로 실습한 것도 포함하여 작성해주시기 바랍니다.
- 실제 개발 과정에서 목표하던 바가 무엇이었는지 작성해주시기 바랍니다.
- 구현 과정에 따라 어떤 결과물이 나오게 되었는지 최대한 상세하게 작성해주시기 바랍니다.
-->
구현하기 앞서 우선 아래와 같은 순서로 개발 일정을 잡았습니다.
> `Member` ➡️ `Post` ➡️ `Product`
1. 회원가입, 로그인 및 로그아웃
2. 회원 정보 수정
3. 아이디 및 비밀번호 찾기, 회원가입 메일 발송
4. `Post` CRUD
5. `Product` CURD

**발생한 이슈**

### 534-5.7.9 application-specific password required.
[상황 설명]
> 비밀번호 찾기 메일을 보내는 것을 시도하던 중 아래와 같은 에러 구문을 마주쳤다.<br>
> `application.yml`은 정상적으로 작성한 것을 확인하고, 다시 시도해보니 에러 코드가 바뀐 것을 확인하였다.<br>
> 검색을 해보니 Google 앱 비밀번호를 설정해야 해결된다고 나와 있어 설정한 뒤 해결하였다.

```java
534-5.7.9 application-specific password required.
learn more at 534 5.7.9 https://support.google.com/mail/?p=invalidsecondfactor h5-20020a63c005000000b004639c772878sm6868282pgg.48 - gsmtp
```

- [참고 블로그](https://dev-monkey-dugi.tistory.com/m/51)

### Empty encoded password
[상황 설명]
> 비밀번호 변경을 구현하면서 위와 같은 에러 구문을 확인했다.<br>
> 여러 블로그를 확인하면서 `Security`에 대한 문제라고 했지만, 나의 `SecurityConfig`에는 크게 문제될게 없어보였다.<br>
> 로깅을 하며 확인해보니 `@AuthenticationPrincipal AuthMember authMember`로 불러온 `authMember`에서 `getPassword()`에 `null`이 찍히는 문제가 발생한 것이다.<br>
> `password` 이외 다른 `filed`는 정상적으로 나오는 것을 확인하였고, `Repository`에서 `username`으로 조회한 `Member`에서는 `password`가 잘 나오는 것을 확인하였다.<br>
> 때문에 아래와 같이 코드를 수정하였다.
```java
// 기존 코드
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
> 해당 버그가 발생한 이유는 천천히 다시 조사해봐야할 것 같다!
- [참고 블로그](https://java8.tistory.com/m/509)

**[특이사항]**

<!--
구현 과정에서 아쉬웠던 점 / 궁금했던 점을 정리합니다.

- 추후 리팩토링 시, 어떤 부분을 추가적으로 진행하고 싶은지에 대해 구체적으로 작성해주시기 바랍니다.
    
    **참고: [Refactoring]**
    
    - Refactoring 시 주로 다루어야 할 이슈들에 대해 리스팅합니다.
    - 1차 리팩토링은 기능 개발을 종료한 후, 스스로 코드를 다시 천천히 읽어보면서 진행합니다.
    - 2차 리팩토링은 피어리뷰를 통해 전달받은 다양한 의견과 피드백을 조율하여 진행합니다.
-->