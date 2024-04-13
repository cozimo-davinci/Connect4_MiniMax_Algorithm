// Teimur Terchyyev 101412670
// Ivan Zakrevskyi 101419665
// Akorede Daniel Osunkoya 101477407
// Miguel Angel Gutierrez Serrano 101449899

import java.util.Scanner;

public class Main {
    private static final int ROWS = 6;
    private static final int COLUMNS = 7;
    private static final char EMPTY = '-';
    private static char PLAYER1_SYMBOL;
    private static char PLAYER2_SYMBOL;
    private static String player1Name;
    private static String player2Name;
    private static final char[][] board = new char[ROWS][COLUMNS];

    public static void main(String[] args) {
        initializeBoard();


        Scanner scanner = new Scanner(System.in);
        boolean gameEnd = false;

        System.out.println("Choose the game mode:");
        System.out.println("1. Human vs Human");
        System.out.println("2. Human vs AI");

        int gameMode = scanner.nextInt();

        if (gameMode == 1) {
            System.out.print("Enter Player 1's name: ");
            player1Name = scanner.next();
            System.out.print("Enter Player 1's symbol (R/Y): ");
            PLAYER1_SYMBOL = scanner.next().charAt(0);
            PLAYER2_SYMBOL = (PLAYER1_SYMBOL == 'R') ? 'Y' : 'R';
            System.out.print("Enter Player 2's name: ");
            player2Name = scanner.next();
        } else {
            System.out.print("Enter your name: ");
            player1Name = scanner.next();
            System.out.print("Choose your symbol (R/Y): ");
            PLAYER1_SYMBOL = scanner.next().charAt(0);
            PLAYER2_SYMBOL = (PLAYER1_SYMBOL == 'R') ? 'Y' : 'R';
            System.out.println("Do you want to play first? (Y/N)");
            String firstTurn = scanner.next();
            if (firstTurn.equalsIgnoreCase("Y")) {
                System.out.println("You start first.");
            } else {
                System.out.println("AI starts first.");
                getAIMove();
                printBoard();
            }
        }

        boolean player1Turn = true;

        while (!gameEnd) {
            int column;
            if (player1Turn) {
                System.out.println(player1Name + "'s turn (" + PLAYER1_SYMBOL + "). Enter column number (1-7):");
                column = scanner.nextInt() - 1;
                while (!isValidMove(column)) {
                    System.out.println("Invalid move. Please enter a valid column number (1-7):");
                    column = scanner.nextInt() - 1;
                }
                makeMove(column, PLAYER1_SYMBOL);
            } else {
                if (gameMode == 1) {
                    System.out.println(player2Name + "'s turn (" + PLAYER2_SYMBOL + "). Enter column number (1-7):");
                    column = scanner.nextInt() - 1;
                } else {
                    column = getAIMove();
                    System.out.println("AI chose column: " + (column + 1));
                }
                while (!isValidMove(column)) {
                    System.out.println("Invalid move. Please enter a valid column number (1-7):");
                    column = scanner.nextInt() - 1;
                }
                makeMove(column, PLAYER2_SYMBOL);
            }
            printBoard();
            if (checkWin(PLAYER1_SYMBOL)) {
                System.out.println(player1Name + " wins!");
                gameEnd = true;
            } else if (checkWin(PLAYER2_SYMBOL)) {
                if (gameMode == 2) {
                    System.out.println("AI wins! :DDD");
                } else {
                    System.out.println(player2Name + " wins!");
                }
                gameEnd = true;
            } else if (checkDraw()) {
                System.out.println("The game ends in a draw!");
                gameEnd = true;
            }
            player1Turn = !player1Turn;
        }
        scanner.close();
    }

    private static void initializeBoard() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

    private static void printBoard() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("1 2 3 4 5 6 7");
    }

    private static boolean isValidMove(int column) {
        return column >= 0 && column < COLUMNS && board[0][column] == EMPTY;
    }

    private static void makeMove(int column, char symbol) {
        for (int i = ROWS - 1; i >= 0; i--) {
            if (board[i][column] == EMPTY) {
                board[i][column] = symbol;
                break;
            }
        }
    }

    private static boolean checkWin(char symbol) {
        // Check rows
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col <= COLUMNS - 4; col++) {
                if (board[row][col] == symbol && board[row][col + 1] == symbol &&
                        board[row][col + 2] == symbol && board[row][col + 3] == symbol) {
                    return true;
                }
            }
        }
        for (int col = 0; col < COLUMNS; col++) {
            for (int row = 0; row <= ROWS - 4; row++) {
                if (board[row][col] == symbol && board[row + 1][col] == symbol &&
                        board[row + 2][col] == symbol && board[row + 3][col] == symbol) {
                    return true;
                }
            }
        }
        for (int row = 0; row <= ROWS - 4; row++) {
            for (int col = 0; col <= COLUMNS - 4; col++) {
                if (board[row][col] == symbol && board[row + 1][col + 1] == symbol &&
                        board[row + 2][col + 2] == symbol && board[row + 3][col + 3] == symbol) {
                    return true;
                }
            }
        }
        for (int row = 0; row <= ROWS - 4; row++) {
            for (int col = 3; col < COLUMNS; col++) {
                if (board[row][col] == symbol && board[row + 1][col - 1] == symbol &&
                        board[row + 2][col - 2] == symbol && board[row + 3][col - 3] == symbol) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean checkDraw() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (board[i][j] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    private static int getAIMove() {
        int[] result = minimax(3, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        return result[1];
    }

    private static int[] minimax(int depth, int alpha, int beta, boolean maximizingPlayer) {
        int bestColumn = -1;
        int bestScore = maximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        if (depth == 0 || checkDraw() || checkWin(PLAYER1_SYMBOL) || checkWin(PLAYER2_SYMBOL)) {
            bestScore = evaluateBoard();
        } else {
            for (int col = 0; col < COLUMNS; col++) {
                if (isValidMove(col)) {
                    int row = getNextEmptyRow(col);
                    board[row][col] = maximizingPlayer ? PLAYER2_SYMBOL : PLAYER1_SYMBOL;
                    int[] currentScore = minimax(depth - 1, alpha, beta, !maximizingPlayer);
                    board[row][col] = EMPTY;

                    if (maximizingPlayer) {
                        if (currentScore[0] > bestScore) {
                            bestScore = currentScore[0];
                            bestColumn = col;
                        }
                        alpha = Math.max(alpha, bestScore);
                    } else {
                        if (currentScore[0] < bestScore) {
                            bestScore = currentScore[0];
                            bestColumn = col;
                        }
                        beta = Math.min(beta, bestScore);
                    }
                    if (alpha >= beta) {
                        break;
                    }
                }
            }
        }
        return new int[]{bestScore, bestColumn};
    }

    private static int evaluateBoard() {
        int score = 0;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col <= COLUMNS - 4; col++) {
                score += evaluateWindow(board[row][col], board[row][col + 1], board[row][col + 2], board[row][col + 3]);
            }
        }
        for (int col = 0; col < COLUMNS; col++) {
            for (int row = 0; row <= ROWS - 4; row++) {
                score += evaluateWindow(board[row][col], board[row + 1][col], board[row + 2][col], board[row + 3][col]);
            }
        }
        for (int row = 0; row <= ROWS - 4; row++) {
            for (int col = 0; col <= COLUMNS - 4; col++) {
                score += evaluateWindow(board[row][col], board[row + 1][col + 1], board[row + 2][col + 2], board[row + 3][col + 3]);
            }
        }
        for (int row = 0; row <= ROWS - 4; row++) {
            for (int col = 3; col < COLUMNS; col++) {
                score += evaluateWindow(board[row][col], board[row + 1][col - 1], board[row + 2][col - 2], board[row + 3][col - 3]);
            }
        }

        return score;
    }

    private static int evaluateWindow(char a, char b, char c, char d) {
        int score = 0;
        // Player 2
        if (a == PLAYER2_SYMBOL) {
            if (b == PLAYER2_SYMBOL) {
                if (c == PLAYER2_SYMBOL) {
                    if (d == PLAYER2_SYMBOL) {
                        score += 1000;
                    } else if (d == EMPTY) {
                        score += 100;
                    }
                } else if (c == EMPTY && d == PLAYER2_SYMBOL) {
                    score += 100;
                }
            } else if (b == EMPTY && c == PLAYER2_SYMBOL && d == PLAYER2_SYMBOL) {
                score += 100;
            }
        }
        // Player 1
        else if (a == PLAYER1_SYMBOL) {
            if (b == PLAYER1_SYMBOL) {
                if (c == PLAYER1_SYMBOL) {
                    if (d == PLAYER1_SYMBOL) {
                        score -= 1000;
                    } else if (d == EMPTY) {
                        score -= 100;
                    }
                } else if (c == EMPTY && d == PLAYER1_SYMBOL) {
                    score -= 100;
                }
            } else if (b == EMPTY && c == PLAYER1_SYMBOL && d == PLAYER1_SYMBOL) {
                score -= 100;
            }
        }
        return score;
    }

    private static int getNextEmptyRow(int column) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (board[row][column] == EMPTY) {
                return row;
            }
        }
        return -1;
    }
}