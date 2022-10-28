## Title: [2Week] 가준영

## 미션 요구사항 분석 & 체크리스트

### 필수과제
- [x] 상품 장바구니 추가
- [x] 상품 결제
- [x] PG 연동

### 추가과제
- [x] 환불

## 2주차 미션 요약

## **[🚥 접근 방법]**
구현하기 앞서 우선 아래와 같은 순서로 개발 일정을 잡았습니다.

> Cart ➡️ Order ➡️ CashLog ➡️ MyBook

### 1. `Cart`
- 장바구니 리스트
- 상품 담기, 제거

### 2. `Order`
- 주문 생성, 상세 페이지
- 주문 리스트

### 3. `CashLog`
- 결제 시 `CashLog` 추가

### 4. `MyBook`
- 결제 후 `MyBook` 추가

> 참고 레퍼런스<br>
> [Toss 결제 강의 Repository](https://github.com/jhs512/app_2022_10_11)<br>
> [Toss API Docs](https://docs.tosspayments.com/guides/overview)<br>

---

## **[😦 발생한 이슈]**

### `LocalDateTime`

💬 상황 설명
> 환불 엔드 포인트를 정하기 위해 날을 비교하기 위해 **비교연산자**를 사용하였다. <br>
> 하지만 `LocalDateTime`은 비교연산자 대신 전용 `method`를 제공하는 것을 알았다.

```java
//  구매날짜가 인자보다 *전*인경우 return true
if(item.getPayDate().isBefore(item.getPayDate().plusMinutes(10)))

//  구매날짜가 인자보다 *후*인경우 return true
if(item.getPayDate().isAfter(item.getPayDate().plusMinutes(10)))

//  구매날짜가 인자와 *동일*할 경우 return true
if(item.getPayDate().isEqual(item.getPayDate().plusMinutes(10)))
```

✅ 해결 과정
> [참고 블로그](https://codechacha.com/ko/java-compare-date-and-time/)

### `@Lob`

💬 상황 설명
> 이번주 과제에 포함되지 않지만, 이전 `Post` 개발할 때 글이 정상적으로 저장이 되며, `Markdown` 형식으로 출력되는 것을 확인했다. <br>
> 개발을 다 마친 후 잘 등록되는지 다시 확인해볼겸 1주차 `record`를 추가했는데 아래와 같은 에러가 발생했다.

```java
Too long string in column 1
```

✅ 해결 과정
> Google 검색을 통해 확인해보니 기본적으로 `varchar(255)`로 저장되기 때문에 블로그 정도의 글을 저장할 수 없는 것이었다.<br>
> 이를 해결하기 위해 `content` 상단에 `@Lob` 어노테이션을 추가해주었다.<br>
> [참고 블로그](https://hyeonic.tistory.com/208)

---

### **[특이사항]**
> 개인 사정으로 저번주와 동일하게 2일간 개발을 진행해 조금 빠듯하게 느껴졌다.<br>
> 강사님의 코드를 가져와 사용하면서도 코드를 이해하기 위해 `logging`을 통해 흐름을 이해했다.<br>
> 처음에는 이게 무슨 코드인가 싶었지만, 하나하나 직접 구현하면서 사용하다보니 어느 부분에 사용하고, 어떻게 사용하는지 이해할 수 있었다.<br>
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

- [x] `JavaDoc`
> 급하게 개발을 진행하다보니 JavaDoc에 신경을 쓰지 못했다.<br>
> 추후 리팩터링을 진행하면서 작성되지 않은 메소드에 `JavaDoc`을 추가할 예정이다.

- [x] `User Convenience`
> 환불과 같이 내역이 삭제되지 않았을 때 `Redirect` 되는 **사용자 편의성**을 제대로 구현하지 못했다.<br>
> 기능을 다시 확인해보면서 제대로 `Redirect` 되지 않거나, 메세지가 제대로 나오는지 다시 확인해봐야할 것 같다.

- [ ] `Select Order`
> 장바구니에서 선택된 상품만 결제를 진행할 수 있도록 변경하기

- [x] `Secret Key`
> Toss와 관련된 Key는 application.yml 파일에서 보관해야할 것 같다.
```yaml
custom: 
    toss: 
        secretKey: abcdefghijklmnopqrstuvwxyz
```

---

## [😎 Review]

### `findBy`
> `find`랑 `findAll`이랑 차이점은 무엇인가요?<br>
> 또는 `Buyer`랑 `BuyerId`로 찾는 차이는 무엇인지 알 수 있을까요?

`JPA`로 `List<Object>`로 반환하는 방법은 아래와 같은 방식이 있다.

```java
List<Order> findByBuyerId(long buyerId)
List<Order> findByBuyer(Member buyer)
List<Order> findAllByBuyerId(long buyerId)
List<Order> findAllByBuyer(Member buyer)
```
- `findBy`, `findAllBy`의 차이

[StackOverFlow](https://stackoverflow.com/questions/37253571/spring-data-jpa-difference-between-findby-findallby)의 말을 살펴보니 `findBy`, `findAllBy`의 차이는 없다고 한다.<br>
`All`이라는 키워드는 쿼리를 실행할 때 실제로 무시되어 진행된다. 즉, `findBy == findAllBy` 라고 표현할 수 있다.

- `findByMemberId`, `findByMember`의 차이

강의 내용 중에 `DB`에는 `Table` 자체가 `Column`으로 저장되지 않고, `FK`키를 통해 저장된다는 것을 배운 기억이 있다.<br>
그렇기 때문에 `Member`로 검색을 하던, `MemberId`로 검색을 하던 결국 `MemberId`를 통해 검색이 된다.

그래도 혹시 몰라 테스트를 통해 확인해봤다.<br>
우선 시간적 차이가 있는지 궁금해서 **100건**의 테스트 케이스를 통해 확인해보았다.

![스크린샷 2022-10-28 11 39 11](https://user-images.githubusercontent.com/82663161/198474383-ade317b9-3758-4fc7-83e6-e8b2118b2844.png)

시간상 차이는 대략 **600ms** 차이가 났다.<br>
`Member`에서 `id`를 꺼내오는 과정 때문인지는 몰라도 생각보다 차이가 있는 것 같다.

쿼리문을 살펴보면 아래와 같다.
```bash
# findByMember
Hibernate: 
    /* select
        generatedAlias0 
    from
        
    Order as generatedAlias0 where
        generatedAlias0.buyer=:param0 */ select
            order0_.id as id1_9_,
            order0_.create_date as create_d2_9_,
            order0_.modify_date as modify_d3_9_,
            order0_.buyer_id as buyer_id8_9_,
            order0_.is_canceled as is_cance4_9_,
            order0_.is_paid as is_paid5_9_,
            order0_.is_refunded as is_refun6_9_,
            order0_.name as name7_9_ 
        from
            product_order order0_ 
        where
            order0_.buyer_id=?
2022-10-28 11:37:32.377 TRACE 80338 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [BIGINT] - [3]
2022-10-28 11:37:32.379 TRACE 80338 --- [           main] o.h.type.descriptor.sql.BasicExtractor   : extracted value ([id1_9_] : [BIGINT]) - [3]
```
```bash
# findByMemberId
Hibernate: 
    /* select
        generatedAlias0 
    from
        
    Order as generatedAlias0 left join
        generatedAlias0.buyer as generatedAlias1 
    where
        generatedAlias1.id=:param0 */ select
            order0_.id as id1_9_,
            order0_.create_date as create_d2_9_,
            order0_.modify_date as modify_d3_9_,
            order0_.buyer_id as buyer_id8_9_,
            order0_.is_canceled as is_cance4_9_,
            order0_.is_paid as is_paid5_9_,
            order0_.is_refunded as is_refun6_9_,
            order0_.name as name7_9_ 
        from
            product_order order0_ 
        left outer join
            member member1_ 
                on order0_.buyer_id=member1_.id 
        where
            member1_.id=?
2022-10-28 11:37:24.484 TRACE 80338 --- [           main] o.h.type.descriptor.sql.BasicBinder      : binding parameter [1] as [BIGINT] - [2]
2022-10-28 11:37:24.485 TRACE 80338 --- [           main] o.h.type.descriptor.sql.BasicExtractor   : extracted value ([id1_9_] : [BIGINT]) - [2]
```

둘의 차이는 바로 `Join`이다.<br>
하지만 이에 대한 차이가 명확하지 않아서 [링크](https://jojoldu.tistory.com/520)를 통해 다시 확인해볼 예정이다.

### `@Transactional`
> `@Transactional`의 가장 큰 목적은 작업을 처리하던 중 오류가 발생했을 때 다시 원상태로 돌리는 것이다.
```java
/**
* Product를 수정하는 메소드
* @param currentProduct 수정할 Product
* @param modifyProductDto 수정된 내용을 가지고 있는 DTO
*/
@Transactional
public void modifyProduct(Product currentProduct, ModifyProductDto modifyProductDto) {
    currentProduct.modifyProduct(modifyProductDto.getSubject(),modifyProductDto.getPrice());
}
```
위 상태에서 정상적으로 수정이 된다면 `productRepository.save(product)`가 없어도 변경 감지를 통해 저장이 된다.<br>
만약 `@Transcational` 어노테이션을 붙이지 않았다면 `save()`하는 과정을 추가해주어야 한다.

---

## [🤔 Retrospect]

선택 사항