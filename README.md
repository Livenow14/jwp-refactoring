# 키친포스
치킨 포스기를 리랙토링 해보자!!

## 테이블 연관관계 
![image](https://user-images.githubusercontent.com/48986787/138720712-c7aa5ed6-900e-4449-b36d-d3794936a556.png)

## 용어 사전

| 한글명 | 영문명 | 설명 |
| --- | --- | --- |
| 상품 | product | 메뉴를 관리하는 기준이 되는 데이터 |
| 메뉴 그룹 | menu group | 메뉴 묶음, 분류 |
| 메뉴 | menu | 메뉴 그룹에 속하는 실제 주문 가능 단위 |
| 메뉴 상품 | menu product | 메뉴에 속하는 수량이 있는 상품 |
| 금액 | amount | 가격 * 수량 |
| 주문 테이블 | order table | 매장에서 주문이 발생하는 영역 |
| 빈 테이블 | empty table | 주문을 등록할 수 없는 주문 테이블 |
| 주문 | order | 매장에서 발생하는 주문 |
| 주문 상태 | order status | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정 | table group | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목 | order line item | 주문에 속하는 수량이 있는 메뉴 |
| 매장 식사 | eat in | 포장하지 않고 매장에서 식사하는 것 |

## 요구 사항
## 상품(Product)
### 저장
- [x] 상품이 DB에 저장된다.
- [x] id의 존재에 상관없에 저장시 DB의 AutoIncrement를 통해 id가 DB에 저장된다.
- [x] 동일한 이름의 상품을 저장할 수 있다.
- [x] 동일한 이름과 가격을 가지는 상품을 저장할 수 있다. 
#### 제약사항
- [x] 상품의 가격의 null이면 `예외`가 발생한다.
- [x] 삼품의 가격이 음수면 `예외`가 발생한다.
- [x] DB에서 name은 null을 허용하지 않는다.
- [x] DB에서 name은 255자 이하로 제한된다.
- [x] DB에서 prica는 null을 허용하지 않는다.
- [x] DB에서 prica는 소수자리 포함 19바이트 이하로 제한된다. 
### 조회
- [x] id로 DB의 상품을 찾아 반환한다.
  - [x] DB에 저장된 상품이 없다면 빈 Optional 객체를 반환한다.
- [x] DB에 저장된 모든 상품을 반환한다.
  - [ ] DB에 저장된 상품이 없으면 빈 배열을 반환한다.

## 메뉴그룹 (MenuGroup)
### 저장
- [x] 메뉴그룹이 DB에 저장된다.
- [x] 동일한 이름의 메뉴그룹을 저장할 수 있다.
#### 제약사항
- [x] DB에서 name은 255자 이하로 제한된다.
- [x] DB에서 name은 null을 허용하지 않는다.
### 조회
- [x] id로 DB의 메뉴그룹을 찾아 반환한다.
  - [x] DB에 저장된 메뉴그룹이 없다면 빈 Optional 객체를 반환한다.
- [x] id로 DB에 존재하는 메뉴그룹인지 boolean값을 반환한다. 
- [x] DB에 저장된 모든 메뉴그룹을 반환한다. 
  - [ ] DB에 저장된 메뉴그룹이 없으면 빈 배열을 반환한다. 
  
# 메뉴(Menu)
## 저장
- [x] 메뉴가 DB에 저장된다.
- [x] 동일한 이름의 메뉴를 저장할 수 있다.
- [x] 동일한 이름과 가격의 메뉴를 저장할 수 있다.
- [x] 동한 이름과 가격, 메뉴그룹을 가지는 메뉴를 저장할 수 있다.
### 제약사항
- [x] 저장하려는 메뉴의 가격의 null이면 `예외`가 발생한다.
- [x] 저장하려는 메뉴의 가격이 음수면 `예외`가 발생한다.
- [x] 저장하려는 메뉴의 메뉴그룹이 존재하지 않으면 `예외`가 발생한다.
- [x] 저장하려는 메뉴의 메뉴상품에 포함된 상품이 존재하지 않으면 `예외`가 발생한다. 
- [x] 저장하려는 메뉴의 가격이 (상품의 가격 * 수량) 보다 높으면 `예외`가 발생한다.
- [x] DB에서 name은 255자 이하로 제한된다.
- [x] DB에서 name은 null을 허용하지 않는다.
- [x] DB에서 prica는 19바이트 이하로 제한된다.
- [x] DB에서 prica는 null을 허용하지 않는다.
- [x] DB에서 menu_group_id를 가지는 메뉴그룹이 존재해야한다. 
- [x] DB에서 menu_group_id는 null을 허용하지 않는다. 
## 조회 
- [x] id로 DB의 메뉴을 찾아 반환한다.
  - [x] DB에 저장된 메뉴가 없다면 빈 Optional 객체를 반환한다.
- [ x DB에 저장된 모든 메뉴를 반환한다.
  - [ ] DB에 저장된 메뉴이 없으면 빈 배열을 반환한다.
- [x] 메뉴 id 컬렉션으로 DB의 저장된 메뉴 개수를 반환한다.
  - [x] DB에 저장된 메뉴가 없다면 0을 반환한다.

# 메뉴상품(MenuProduct)
## 저장 
- [x] 메뉴상품이 DB에 저장된다.
- [x] 동일한  menuid, productId, quantity의 메뉴상품을 저장 할 수 있다.  
### 제약사항
- [x] DB에서 menu_id를 가지는 메뉴가 존재해야한다.
- [x] DB에서 menu_id는 null을 허용하지 않는다.
- [x] DB에서 product_id를 가지는 상품이 존재해야한다.
- [x] DB에서 product_id는 null을 허용하지 않는다.
## 조회 
- [x] id로 DB의 메뉴상품을 찾아 반환한다.
  - [x] DB에 저장된 메뉴상품이 없다면 빈 Optional 객체를 반환한다.
- [x] DB에 저장된 모든 메뉴상품을 반환한다.
  - [ ] DB에 저장된 메뉴상품이 없으면 빈 배열을 반환한다.
- [x] menuId로 모든 menuProducts를 반환한다. 
  - [x] DB의 메뉴상품에 저장된 menuId가 없다면 빈 배열을 반환한다. 

# 주문테이블(OrderTable)
## 저장
- [x] 주문테이블이 DB에 저장된다.
### 제약사항
- [x] DB에서 table_group_id는 null을 허용한다.
## 조회 
- [x] DB에 저장된 모든 주분테이을를 반환한다.
  - [ ] DB에 저장된 주문테이블이 없으면 빈 배열을 반환한다.
## 수정 - empty 값 변경
- [x] 요청한 empty 값으로 주문 테이블의 empty 값을 변경한다.
### 제약사항
- [x] empty 값을 변경할 주문테이블이 존재하지 않다면 `예외`가 발생한다. 
- [x] empty 값을 변경할 주문테이블에 단체지정이 지정되어 있다면 `예외`가 발생한다.
- [x] empty 값을 변경할 주문테이블에 할당된 주문의 주문상테가 COMPLETION이 아니면 `예외`가 발생한다.
## 수정 - numberOfGuests 값 변경
- [x] 요청한 numberOfGuests 값으로 주문 테이블의 numberOfGuests 값을 변경한다.
### 제약사항
- [x] numberOfGuests 값을 변경할 주문테이블이 존재하지 않다면 `예외`가 발생한다.
- [x] 변경 할 numberOfGuests의 값이 0 미만이면 `예외`가 발생한다.
- [x] numberOfGuests 값을 변경할 주문 테이블의 empty 값이 true면 `예외`가 발생한다.

# 단체지정(TableGroup)
## 저장
- [ ] 단체지정이 DB에 저장된다.
- [ ] 단체지정이 완료된 주문테이블의 empty 값은 false로 변경된다.
- [ ] 단체지정이 완료된 주문테이블의 table_group_id 값은 저장된 단체지정의 id값으로 변경된다.
### 제약사항 
- [ ] 저장하려는 단체지정이 null이면 `예외`가 발생한다.
- [ ] 단체지정을 하려는 주문테이블의 수가 2 이상이 아니면 `예외`가 발생한다. 
- [ ] 단체지정을 하려는 주문테이블들의 id로 찾은 주문테이블의 수와 요청했던 주문테이블의 수가 다르면 `예외`가 발생한다.
  - ex) 단체지정 요청 주문테이블 ids = [1,2], 찾은 주문테이블 [2] 이면 예외 발생
- [ ] 단체지정을 하려는 주문테이블의 empty 값이 false이면 `예외`가 발생한다.
- [ ] 단체지정을 하려는 주문테이블에 이미 단체지정이 되어있다면 `예외`가 발생한다.
- [ ] DB에서 id는 20바이트 이하로 제한된다.
- [ ] DB에서 id는 null을 허용하지 않는다.
- [ ] DB에서 created_date는 null을 허용하지 않는다.
## 수정 - 단체지정 해제
- [ ] 단체지정 해제가 완료된 주문테이블의 empty 값은 false로 변경된다.
- [ ] 단체지정 해제가 완료된 주문테이블의 table_group_id 값은 null 변경된다.
- [ ] 그룹이 해제 됐을 때, tableGroup의 row가 삭제되면 안된다.
### 제약사항
- [ ] 해제하려는 테이블의 주문의 주문상태가 COMPLETION이 아니면 `예외`가 발생한다.

# 주문(Order)
## 저장
- [ ] 주문이 DB에 저장된다.
- [ ] 저장 요청시 OrderStatus는 COOOKING으로 지정된다.
- [ ] 저장 요청시 OrderedTime은 현재 시간 으로 지정된다.
### 제약사항
- [ ] 저장하려는 주문의 주문항목이 비어잇으면 `예외`가 발생한다.
- [ ] 저장하려는 주문의 주문항목들의 menuId로 찾은 메뉴 수와 요청했던 주문항목의 수가 다르면 `예외`가 발생한다.
  - ex) 주문 요청 주문항목의 menuIds = [1,2], 찾은 메뉴 [2] 이면 예외 발생
- [ ] 저장하려는 주문의 주문테이블의 empty 값이 true이면 `예외`가 발생한다.
- [ ] DB에서 id는 20바이트 이하로 제한된다.
- [ ] DB에서 id는 null을 허용하지 않는다.
- [ ] DB에서 order_table_id를 가지는 주문테이블이 존재해야한다.
- [ ] DB에서 order_table_id는 null을 허용하지 않는다.
- [ ] DB에서 order_status는 null을 허용하지 않는다.
- [ ] DB에서 ordered_time는 null을 허용하지 않는다.
## 조회 
- [ ] DB에 저장된 모든 주문을 반환한다.
  - [ ] DB에 저장된 주문이 없으면 빈 배열을 반환한다.
  - [ ] 주문항목도 함께 반환한다.
- [ ] 주문 테이블의 id와, 주문상태로 존재 여부를 boolean으로 반환한다.
- [ ] 주문 테이블들의 id와, 주문상태로 존재 여부를 boolean으로 반환한다.
## 수정
- [ ] 수정 후 반환 시 주문항복이 포함된 주문을 반환한다.
### 제약사항
- [ ] 수정하려는 주문이 없다면 `예외`가 발생한다.
- [ ] 수정하려는 주문의 주문상태가 COMPLETITON이면 `예외`가 발생한다.

# 주문 항목(OrderLineItem)
## 저장 
- [ ] 주문항목이 저장된다. 
### 제약사항 
- [ ] DB에서 id는 20바이트 이하로 제한된다.
- [ ] DB에서 id는 null을 허용하지 않는다.
- [ ] DB에서 order_id를 가지는 주문이 존재해야한다.
- [ ] DB에서 order_id는 null을 허용하지 않는다.
- [ ] DB에서 menu_id를 가지는 주문이 존재해야한다.
- [ ] DB에서 menu_id는 null을 허용하지 않는다.
- [ ] DB에서 quantity는 20바이트로 제한된다. 
- [ ] DB에서 quantity는 null을 허용하지 않는다.
## 조회
- [ ] id로 DB의 주문항목을 찾아 반환한다.
  - [ ] DB에 저장된 주문항목품이 없다면 빈 Optional 객체를 반환한다.
- [ ] DB에 저장된 모든 주문항목을 반환한다.
  - [ ] DB에 저장된 주문항목이 없으면 빈 배열을 반환한다.
- [ ] orderId로 모든 주문항목를 반환한다.
  - [ ] DB에 orderId를 포함하는 주문항목이 없다면 빈 배열을 반환한다.
