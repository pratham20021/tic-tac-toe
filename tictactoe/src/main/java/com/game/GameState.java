package com.game;

public class GameState {
    private String[] board = new String[9];
    private String currentPlayer = "X";
    private String winner = null;
    private boolean draw = false;

    public GameState() {
        for (int i = 0; i < 9; i++) board[i] = "";
    }

    public String[] getBoard() { return board; }
    public String getCurrentPlayer() { return currentPlayer; }
    public String getWinner() { return winner; }
    public boolean isDraw() { return draw; }

    public boolean makeMove(int index) {
        if (index < 0 || index > 8 || !board[index].isEmpty() || winner != null || draw) return false;
        board[index] = currentPlayer;
        checkResult();
        if (winner == null && !draw) currentPlayer = currentPlayer.equals("X") ? "O" : "X";
        return true;
    }

    private void checkResult() {
        int[][] wins = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
        for (int[] w : wins) {
            if (!board[w[0]].isEmpty() && board[w[0]].equals(board[w[1]]) && board[w[1]].equals(board[w[2]])) {
                winner = board[w[0]];
                return;
            }
        }
        draw = true;
        for (String cell : board) if (cell.isEmpty()) { draw = false; return; }
    }

    public void reset() {
        for (int i = 0; i < 9; i++) board[i] = "";
        currentPlayer = "X";
        winner = null;
        draw = false;
    }
}
