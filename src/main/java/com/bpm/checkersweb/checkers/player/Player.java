package com.bpm.checkersweb.checkers.player;

import com.bpm.checkersweb.checkers.figures.FigureColor;
import com.bpm.checkersweb.checkers.logic.Board;
import com.bpm.checkersweb.checkers.logic.Move;

public interface Player {
    Move getMove(Board board);
    String getName();
    FigureColor getFigureColor();
}
