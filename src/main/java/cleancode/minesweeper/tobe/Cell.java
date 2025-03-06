package cleancode.minesweeper.tobe;

public class Cell {

  private static final String FLAG_SIGN = "⚑";
  private static final String LAND_MINE_SIGN = "☼";
  private static final String UNCHECKED_SIGN = "□";
  private static final String EMPTY_SIGN = "■";

  private int nearbyLandMineCounts;
  private boolean isLandMine;
  private boolean isFlag;
  private boolean isOpened;


  public Cell(int nearbyLandMineCounts, boolean isLandMine, boolean isFlag, boolean isOpened) {
    this.nearbyLandMineCounts = nearbyLandMineCounts;
    this.isLandMine = isLandMine;
    this.isFlag = isFlag;
    this.isOpened = isOpened;
  }

  public static Cell of(int nearbyLandMineCounts, boolean isLandMine, boolean isFlag, boolean isOpened) {
    return new Cell( nearbyLandMineCounts, isLandMine, isFlag, isOpened);
  }

  public static Cell create() {
    return of(0, false, false, false);
  }

  public void flag() {
    this.isFlag = true;
  }

  public void turnOnLandMine() {
    this.isLandMine = true;
  }

  public void updateNearbyLandMineCount(int count) {
    this.nearbyLandMineCounts = count;
  }


  public boolean isChecked() {
    return isFlag || isOpened;
  }

  public boolean isLandMine() {
    return isLandMine;
  }

  public void open() {
    this.isOpened = true;
  }

  public boolean isOpened() {
    return isOpened;
  }

  public boolean hasLandMineCount() {
    return this.nearbyLandMineCounts != 0;
  }

  public String getSign() {
    if(isOpened){
      if (isLandMine) {
        return LAND_MINE_SIGN;
      }
      if (hasLandMineCount()) {
        return String.valueOf(nearbyLandMineCounts);
      }
      return EMPTY_SIGN;
    }

    if (isFlag) {
      return FLAG_SIGN;
    }

    return UNCHECKED_SIGN;
  }

  //getter는 만들지 말지 고민하고, setter는 되도록 안만들기 위해 고민하자.
}
