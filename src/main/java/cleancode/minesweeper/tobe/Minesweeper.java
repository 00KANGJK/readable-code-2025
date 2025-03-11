package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.io.ConsoleInputHadler;
import cleancode.minesweeper.tobe.io.ConsoleOutputHadler;

import java.util.Arrays;
import java.util.Random;

public class Minesweeper {

  public static final int BOARD_ROW_SIZE = 8;
  public static final int BOARD_COL_SIZE = 10;
  private static final Cell[][] BOARD = new Cell[BOARD_ROW_SIZE][BOARD_COL_SIZE];
  public static final int LAND_MINE_COUNT = 10;

  private final ConsoleInputHadler consoleInputHadler = new ConsoleInputHadler();
  private final ConsoleOutputHadler consoleOutputHadler = new ConsoleOutputHadler();

  private int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배



  public void run() {
    consoleOutputHadler.showGameStartComments();
    initializeGame();

    while (true) {
      try {
        consoleOutputHadler.showBoard(BOARD);

        if (doesUserWinTheGame()) {
          consoleOutputHadler.printGameClear();
          break;
        }
        if (doesUserLoseTheGame()) {
          consoleOutputHadler.printGameOver();
          break;
        }
        System.out.println();

        String cellInput = getCellInputFromUser();
        String userActionInput = getUserActionInputFromUser();
        actOnCell(cellInput, userActionInput);
      } catch (GameException e) {
        consoleOutputHadler.printExceptionMessage(e);
      } catch (Exception e) {
        consoleOutputHadler.printSimpleExceptionMessage("알 수 없는 오류가 발생했습니다.");
      }
    }
  }

  private void actOnCell(String cellInput, String userActionInput) {
    int selectedColIndex = getSelectedColIndex(cellInput);
    int selectedRowIndex = getSelectedRowIndex(cellInput);

    if (doesUserChooseToPlantFlag(userActionInput)) {
      BOARD[selectedRowIndex][selectedColIndex].flag();
      checkIfGameIsOver();
      return;
    }

    if (doseUserChooseToOpenCell(userActionInput)) {
      if (isLandMineCell(selectedRowIndex, selectedColIndex)) {
        BOARD[selectedRowIndex][selectedColIndex].open();
        changeGameStatusToLose();
        return;
      }

      open(selectedRowIndex, selectedColIndex);
      checkIfGameIsOver();
      return;
    }
    throw new GameException("잘못된 번호를 선택하셨습니다.");
  }

  private void changeGameStatusToLose() {
    gameStatus = -1;
  }

  private boolean isLandMineCell(int selectedRowIndex, int selectedColIndex) {
    return BOARD[selectedRowIndex][selectedColIndex].isLandMine();
  }

  private boolean doseUserChooseToOpenCell(String userActionInput) {
    return userActionInput.equals("1");
  }

  private boolean doesUserChooseToPlantFlag(String userActionInput) {
    return userActionInput.equals("2");
  }

  private int getSelectedRowIndex(String cellInput) {
    char cellInputRow = cellInput.charAt(1);
    return convertRowFrom(cellInputRow);
  }

  private int getSelectedColIndex(String cellInput) {
    char cellInputCol = cellInput.charAt(0);
    return convertColFrom(cellInputCol);
  }

  private String getUserActionInputFromUser() {
    consoleOutputHadler.printCommentForSelectingAction();
    return consoleInputHadler.getUserInput();
  }

  private String getCellInputFromUser() {
    consoleOutputHadler.printCommentForSelectingCell();
    return consoleInputHadler.getUserInput();
  }

  private boolean doesUserLoseTheGame() {
    return gameStatus == -1;
  }

  private boolean doesUserWinTheGame() {
    return gameStatus == 1;
  }

  private void initializeGame() {
    for (int row = 0; row < BOARD_ROW_SIZE; row++) {
      for (int col = 0; col < BOARD_COL_SIZE; col++) {
        BOARD[row][col] = Cell.create();
      }
    }
    for (int i = 0; i < LAND_MINE_COUNT; i++) {
      int col = new Random().nextInt(BOARD_COL_SIZE);
      int row = new Random().nextInt(BOARD_ROW_SIZE);
      BOARD[row][col].turnOnLandMine();
    }
    for (int row = 0; row < BOARD_ROW_SIZE; row++) {
      for (int col = 0; col < BOARD_COL_SIZE; col++) {
        if (isLandMineCell(row, col)) {
          continue;
        }
        int count = countNearbyLandMines(row, col);
        BOARD[row][col].updateNearbyLandMineCount(count);
      }
    }
  }

  private int countNearbyLandMines(int row, int col) {
    int count = 0;
    if (row - 1 >= 0 && col - 1 >= 0 && isLandMineCell(row - 1, col - 1)) {
      count++;
    }
    if (row - 1 >= 0 && isLandMineCell(row - 1, col)) {
      count++;
    }
    if (row - 1 >= 0 && col + 1 < BOARD_COL_SIZE && isLandMineCell(row - 1, col + 1)) {
      count++;
    }
    if (col - 1 >= 0 && isLandMineCell(row, col - 1)) {
      count++;
    }
    if (col + 1 < BOARD_COL_SIZE && isLandMineCell(row, col + 1)) {
      count++;
    }
    if (row + 1 < BOARD_ROW_SIZE && col - 1 >= 0 && isLandMineCell(row + 1, col - 1)) {
      count++;
    }
    if (row + 1 < BOARD_ROW_SIZE && isLandMineCell(row + 1, col)) {
      count++;
    }
    if (row + 1 < BOARD_ROW_SIZE && col + 1 < BOARD_COL_SIZE && isLandMineCell(row + 1, col + 1)) {
      count++;
    }
    return count;
  }

  private void checkIfGameIsOver() {
    boolean isAllChecked = isAllCellChecked();
    if (isAllChecked) {
      changeGameStatusToWin();
    }
  }

  private void changeGameStatusToWin() {
    gameStatus = 1;
  }

//  private boolean isAllCellOpenedOld() {
//    boolean isAllOpened = true;
//    for (int row = 0; row < BOARD_ROW_SIZE; row++) {
//      for (int col = 0; col < BOARD_COL_SIZE; col++) {
//        if (BOARD2[row][col].equals(CLOSED_CELL_SIGN)) {
//          isAllOpened = false;
//        }
//      }
//    }
//    return isAllOpened;
//  }

  private boolean isAllCellChecked() {
    return Arrays.stream(BOARD)
      .flatMap(Arrays::stream)
      .allMatch(Cell::isChecked);
  }

  private int convertRowFrom(char cellInputRow) {
    int rowIndex = Character.getNumericValue(cellInputRow) -1 ;
    if(rowIndex > BOARD_ROW_SIZE){
      throw new GameException("잘못된 입력입니다.");
    }
    return rowIndex;
  }

  private int convertColFrom(char cellInputCol) {
    switch (cellInputCol) {
      case 'a':
        return  0;
      case 'b':
        return 1;
      case 'c':
        return 2;
      case 'd':
        return 3;
      case 'e':
        return 4;
      case 'f':
        return 5;
      case 'g':
        return 6;
      case 'h':
        return 7;
      case 'i':
        return 8;
      case 'j':
        return 9;
      default:
        throw new GameException("잘못된 입력입니다.");
    }
  }

  private void open(int row, int col) {
    if (row < 0 || row >= BOARD_ROW_SIZE || col < 0 || col >= BOARD_COL_SIZE) {
      return;
    }
    if (BOARD[row][col].isOpened()) {
      return;
    }
    if (isLandMineCell(row, col)) {
      return;
    }

    BOARD[row][col].open();

    if (BOARD[row][col].hasLandMineCount()) {
      return;
    }

    open(row - 1, col - 1);
    open(row - 1, col);
    open(row - 1, col + 1);
    open(row, col - 1);
    open(row, col + 1);
    open(row + 1, col - 1);
    open(row + 1, col);
    open(row + 1, col + 1);
  }

}
