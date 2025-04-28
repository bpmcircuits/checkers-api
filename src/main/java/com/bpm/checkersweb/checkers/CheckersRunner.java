package com.bpm.checkersweb.checkers;

import com.bpm.checkersweb.checkers.logic.GameLogic;

public class CheckersRunner {
    public static void main(String[] args) {
        GameLogic logic = new GameLogic();
        logic.run();
    }
}
