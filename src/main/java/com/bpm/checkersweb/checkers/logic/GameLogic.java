package com.bpm.checkersweb.checkers.logic;

import com.bpm.checkersweb.checkers.figures.FigureColor;
import com.bpm.checkersweb.checkers.player.Player;
import com.bpm.checkersweb.checkers.player.PlayerFactory;
import com.bpm.checkersweb.checkers.ui.MenuEnum;
import com.bpm.checkersweb.checkers.ui.UserInterface;

public class GameLogic {

    public void run() {
        while (true) {
            MenuEnum.MainMenuOption mainMenuOption = UserInterface.mainMenuChoice();
            switch (mainMenuOption) {
                case NEW_GAME -> newGameMenu();
                case EXIT -> {
                    if (UserInterface.onExit()) return;
                }
            }
        }
    }

    private void newGameMenu() {
        while (true) {
            MenuEnum.NewGameMenuOption newGameMenuOption = UserInterface.newGameMenu();
            switch (newGameMenuOption) {
                case PLAYER_VS_PLAYER -> playGameWith(MenuEnum.HumanOrComputerEnum.HUMAN);
                case PLAYER_VS_COMPUTER -> playGameWith(MenuEnum.HumanOrComputerEnum.COMPUTER);
                case BACK -> {
                    return;
                }
            }
        }
    }

    private void playGameWith(MenuEnum.HumanOrComputerEnum playerOfChoice) {
        Player[] players = PlayerFactory.createPlayers(playerOfChoice);

        Player playerOne = players[0];
        Player playerTwo = players[1];

        UserInterface.displayPlayers(playerOne.getName(), playerTwo.getName());

        Board board = new Board(playerOne, playerTwo).init();
        FigureColor winner = null;

        while (winner == null) {
            System.out.println(board);
            UserInterface.showWhichPlayerTurn(board.getCurrentPlayer().getName());

            Move move = board.getCurrentPlayer().getMove(board);
            boolean moveCompleted = board.moveFigure(move);

            if (!moveCompleted) {
                continue;
            }

            winner = board.checkWinner();
        }

        System.out.println(board);
        UserInterface.showWinner(getWinningColor(winner, playerOne, playerTwo));
    }

    private static String getWinningColor(FigureColor winner, Player playerOne, Player playerTwo) {
        return winner == playerOne.getFigureColor() ? playerOne.getName() : playerTwo.getName();
    }
}
