package cleancode.minesweeper.tobe.mission;

public class MissionDay4 {
//    ✔️ 사용자가 생성한 '주문'이 유효한지를 검증하는 메서드.
//    ✔️ Order는 주문 객체이고, 필요하다면 Order에 추가적인 메서드를 만들어도 된다. (Order 내부의 구현을 구체적으로 할 필요는 없다.)
//    ✔️ 필요하다면 메서드를 추출할 수 있다.

  public boolean validateOrder(Order order) {
    if (order.isEmpty()) {
      log.info("주문 항목이 없습니다.");
      return false;
    }

    if (order.moreThanZeroTotalPrice()) {
      if (order.hasCustomerInfo()) {
        return true;
      }
      log.info("사용자 정보가 없습니다.");
      return false;
    }

    log.info("올바르지 않은 총 가격입니다.");
    return false;
  }
}
