package com.bpm.checkersweb.checkers.ui;

import com.bpm.checkersweb.checkers.figures.FigureColor;
import com.bpm.checkersweb.checkers.logic.Move;

import java.awt.*;
import java.util.Scanner;

public class UserInterface {

    private static Scanner scanner = new Scanner(System.in);

    public static MenuEnum.MainMenuOption mainMenuChoice() {
        System.out.println(UIStrings.MAIN_MENU);
        while (true) {
            try {
                System.out.print(UIStrings.OPTION);
                String input = scanner.nextLine().trim();
                switch (input) {
                    case "1": return MenuEnum.MainMenuOption.NEW_GAME;
                    case "2": return MenuEnum.MainMenuOption.EXIT;
                    default: System.out.println(UIStrings.CHOOSE_RIGHT_OPTION_ONE_TWO);
                }
            } catch (NumberFormatException e) {
                System.out.println(UIStrings.CHOOSE_RIGHT_OPTION_ONE_TWO);
            }
        }
    }

    public static boolean onExit() {
        while (true) {
            System.out.print(UIStrings.ON_QUIT);
            String input = scanner.nextLine().trim();
            switch (input) {
                case "yes": return true;
                case "no": return false;
                default: System.out.println(UIStrings.CHOOSE_RIGHT_OPTION_ONE_TWO);
            }
        }
    }

    public static MenuEnum.NewGameMenuOption newGameMenu() {
        System.out.println(UIStrings.NEW_GAME_MENU);
        while (true) {
            try {
                System.out.print(UIStrings.OPTION);
                String input = scanner.nextLine().trim();
                switch (input) {
                    case "1": return MenuEnum.NewGameMenuOption.PLAYER_VS_PLAYER;
                    case "2": return MenuEnum.NewGameMenuOption.PLAYER_VS_COMPUTER;
                    case "3": return MenuEnum.NewGameMenuOption.BACK;
                    default: System.out.println(UIStrings.CHOOSE_RIGHT_OPTION_ONE_THREE);
                }
            } catch (NumberFormatException e) {
                System.out.println(UIStrings.CHOOSE_RIGHT_OPTION_ONE_THREE);
            }
        }
    }

    public static String choosePlayerName(Settings.PLAYER player) {
        System.out.println(player == Settings.PLAYER.FIRST ? UIStrings.PLAYER_ONE_NAME : UIStrings.PLAYER_TWO_NAME);
        return scanner.nextLine().trim();
    }

    public static FigureColor chooseFigureColor() {
        while (true) {
            System.out.println(UIStrings.CHOOSE_FIGURE);
            String input = scanner.nextLine().trim().toUpperCase();
            switch (input) {
                case "B": return FigureColor.BLACK;
                case "W": return FigureColor.WHITE;
                default: System.out.println(UIStrings.WRONG_OPTION);
            }
        }
    }

    public static void displayPlayers(String playerOne, String playerTwo) {
        System.out.printf(UIStrings.PLAYER_VS_PLAYER, playerOne, playerTwo);
    }


    public static MenuEnum.ComputerLevelEnum chooseComputerLevel() {
        while (true) {
            System.out.println(UIStrings.COMPUTER_LEVEL);
            System.out.print(UIStrings.OPTION);
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1": return MenuEnum.ComputerLevelEnum.EASY;
                case "2": return MenuEnum.ComputerLevelEnum.HARD;
                default:
                    System.out.println(UIStrings.CHOOSE_RIGHT_OPTION_ONE_TWO);
            }
        }
    }

    public static void showWhichPlayerTurn(String playerName) {
        System.out.printf(UIStrings.PLAYER_TURN, playerName);
    }

    public static Move getPlayerMove() {
        while (true) {
            System.out.print(UIStrings.PLACE_YOUR_MOVE);
            String input = scanner.nextLine().trim();
            if (isCorrectMoveName(input)) {
                return takeMove(input);
            }
            else System.out.println(UIStrings.WRONG_MOVE_NAME);
        }
    }

    public static void showWinner(String playerName) {
        System.out.printf(UIStrings.WINNER, playerName);
    }

    public static void illegalMove() {
        System.out.println(UIStrings.ILLEGAL_MOVE);
    }

    public static void continueMove() {
        System.out.println(UIStrings.CONTINUE_MOVE);
    }

    private static boolean isCorrectMoveName(String move) {
        if (move == null || move.length() < 4) {
            return false;
        }
        return move.matches("^[A-H][1-8][A-H][1-8]$");
    }

    public static Point findPoint(String position) {
        char colChar = position.charAt(0);
        int col = colChar - 'A';
        int row = Character.getNumericValue(position.charAt(1)) - 1;
        return new Point(col, row);
    }

    public static Move takeMove(String move) {
        char fromColChar = move.charAt(0);
        int fromCol = fromColChar - 'A';
        int fromRow = Character.getNumericValue(move.charAt(1)) - 1;

        char toColChar = move.charAt(2);
        int toCol = toColChar - 'A';
        int toRow = Character.getNumericValue(move.charAt(3)) - 1;
        return new Move(new Point(fromCol, fromRow), new Point(toCol, toRow));
    }

    public static void captureAvailable() {
        System.out.println(UIStrings.CAPTURE_AVAILABLE);
    }
}
