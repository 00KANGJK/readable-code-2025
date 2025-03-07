package cleancode.minesweeper.tobe.mission;

import java.util.List;

public class Order {

  private final List<Integer> items;
  private final int totalPrice;
  private final int customer;

  public Order(List<Integer> items, int totalPrice, int customer) {
    this.items = items;
    this.totalPrice = totalPrice;
    this.customer = customer;
  }

  public boolean isEmpty() {
    return items.isEmpty();
  }

  public boolean hasCustomerInfo() {
    return customer != 0;
  }

  public boolean moreThanZeroTotalPrice() {
    return totalPrice > 0;
  }
}

