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

- 상품 작성, 상품 리스트
- 상품 조회, 상품 삭제
- 상품 수정

---

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

### [Refactoring]
[`HashTag`, `Keyword`]
> 강사님의 [강의 코드](https://github.com/jhs512/sb_exam_2022_09_05__app10)를 보며 기능 구현을 구현했다.<br>
> 구현을 하는 과정에서 처음보는 `Stream`도 있었고, `extra.get("hashTags")`이라는 함수의 용도를 제대로 이해하지 못했다.<br>
> 너무 강사님 코드에 의존한 문제도 있어, 추후 리팩토링을 진행하면서 나만의 방식으로 변경을 해볼 예정이다.

[`Empty encoded password`]
> 위에서 기술한 이슈 중 `Empty encoded password`라는 에러가 있었다.<br>
> 해당 에러구문이 발생한 이유를 조금 더 자세히 살펴보고, 문제되는 코드를 수정할 예정이다.

[`Design`]
> 기능 개발 때문에 디자인적 부분을 미처 제대로 개발하지 못했다.<br>
> 모바일 환경에서 `header`가 깨지거나, `width`가 좁아서 제대로 보여지지 않는 UI 문제가 있는 상태이다.<br>
> 코드 리팩터링을 진행하면서 이와 관련된 문제와 디자인도 신경을 써야할 것 같다.

[`JavaDoc`]
> 대부분의 `Service`에는 `JavaDoc`을 작성해놓았지만, 이외 부분에는 제대로 작성되지 않았다.<br>
> 추후 리팩터링을 진행하면서 작성되지 않은 메소드에 `JavaDoc`을 추가할 예정이다.