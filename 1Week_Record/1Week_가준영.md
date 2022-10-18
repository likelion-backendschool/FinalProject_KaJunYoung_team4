## Title: [1Week] 가준영

## 미션 요구사항 분석 & 체크리스트

---

### 필수과제
- [ ] 회원가입, 회원 정보 수정, 로그인, 로그아웃, 아이디 찾기
- [ ] 글 작성, 글 수정, 글 리스트, 글 삭제

### 추가과제
- [ ] 비밀번호 찾기
- [ ] 상품 등록
- [ ] 상품 수정
- [ ] 상품 리스트
- [ ] 상품 상세 페이지

## 1주차 미션 요약

### **[접근 방법]**
<!-- 
체크리스트를 중심으로 각각의 기능을 구현하기 위해 어떤 생각을 했는지 정리합니다.

- 무엇에 중점을 두고 구현하였는지, 어떤 공식문서나 예제를 참고하여 개발하였는지 뿐만 아니라 미션을 진행하기 전 개인적으로 실습한 것도 포함하여 작성해주시기 바랍니다.
- 실제 개발 과정에서 목표하던 바가 무엇이었는지 작성해주시기 바랍니다.
- 구현 과정에 따라 어떤 결과물이 나오게 되었는지 최대한 상세하게 작성해주시기 바랍니다.
-->
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
> 참고 레퍼런스

- 게시글 작성, 게시글 리스트
- 게시글 조회, 게시글 삭제
- 게시글 수정

### **[발생한 이슈]**

### `534-5.7.9 application-specific password required.`

[상황 설명]
> 비밀번호 찾기 기능을 구현하던 중 이메일 발송 과정에서 아래와 같은 에러 구문 확인<br>
> 검색을 해보니 Google 앱 비밀번호를 설정해야 해결된다고 나와 있어 설정한 뒤 해결<br>
> [참고 블로그](https://dev-monkey-dugi.tistory.com/m/51)

```java
534-5.7.9 application-specific password required.
learn more at 534 5.7.9 https://support.google.com/mail/?p=invalidsecondfactor h5-20020a63c005000000b004639c772878sm6868282pgg.48 - gsmtp
```

### `Empty encoded password`
[상황 설명]
> 비밀번호 변경 기능을 구현하던 중 아래와 같은 에러 구문 확인<br>
> log도 없이 단순히 아래 구문만 뜬게 이상하여 `Bean` 등록이 제대로 되어있는지, 로직에는 문제가 없는지 확인을 했지만 아무런 문제가 없었다.<br>
> 문제를 좁혀가며 로깅을 진행하였고, `MemberContext`, `Member`의 `getPassword()`에서 `null`값이 찍히는 것을 확인하였다.<br>
> 아직 정확한 원인이 무엇인진 모르지만, DB에는 비밀번호가 정상적으로 저장되어 있어 `Repository`에서 `Member`를 찾아 변경하는 방식으로 개발을 완료했다.<br>
> [참고 블로그](https://java8.tistory.com/m/509)

```java
Empty encoded password
```
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
> 해당 버그가 발생한 이유는 천천히 다시 조사해봐야할 것 같다!

**[특이사항]**

<!--
구현 과정에서 아쉬웠던 점 / 궁금했던 점을 정리합니다.

- 추후 리팩토링 시, 어떤 부분을 추가적으로 진행하고 싶은지에 대해 구체적으로 작성해주시기 바랍니다.
    
    **참고: [Refactoring]**
    
    - Refactoring 시 주로 다루어야 할 이슈들에 대해 리스팅합니다.
    - 1차 리팩토링은 기능 개발을 종료한 후, 스스로 코드를 다시 천천히 읽어보면서 진행합니다.
    - 2차 리팩토링은 피어리뷰를 통해 전달받은 다양한 의견과 피드백을 조율하여 진행합니다.
-->