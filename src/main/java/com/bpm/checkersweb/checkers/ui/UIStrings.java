package com.bpm.checkersweb.checkers.ui;

public class UIStrings {

    public static String MAIN_MENU = """
                                    Main Menu: \s
                                    1. New Game \s
                                    2. Quit
                                    """;
    public static String NEW_GAME_MENU = """
                                        1. Player vs Player
                                        2. Player vs Computer
                                        3. Back
                                        """;

    public static String OPTION = "Option: ";
    public static String ON_QUIT = " Are you sure? \n"
                                    + "(type yes or no): ";
    public static String CHOOSE_RIGHT_OPTION_ONE_TWO = "Please choose the option between 1 and 2.";
    public static String CHOOSE_RIGHT_OPTION_ONE_THREE = "Please choose the option between 1 and 3.";
    public static String WRONG_OPTION = "Wrong option!";
    public static String PRESS_ENTER = "(press enter to go back)";
    public static String COMPUTER_LEVEL = "Computer Level - EASY(1), HARD(2)";

    public static String PLAYER_ONE_NAME = "Please write first player name";
    public static String PLAYER_TWO_NAME = "Please write second player name";
    public static String PLAYER_NAME = "Please write your name";
    public static String CHOOSE_FIGURE = "Please choose color between WHITE and BLACK (B, W)";
    public static String PLAYER_VS_PLAYER = "%s vs %s \n";

    public static String PLAYER_TURN = "%s's turn \n";
    public static String PLACE_YOUR_MOVE = "Place your move (i.e. A6B5): ";
    public static String WRONG_MOVE_NAME = "Wrong move name!";
    public static String ILLEGAL_MOVE = "Illegal move!";
    public static String CONTINUE_MOVE = "Please continue your move from last point";
    public static String WINNER = "Player %s won!%n";
    public static String DRAW = "Draw!";
    public static String CAPTURE_AVAILABLE = "Capture is available!";
}
