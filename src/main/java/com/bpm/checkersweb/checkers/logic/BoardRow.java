package com.bpm.checkersweb.checkers.logic;

import com.bpm.checkersweb.checkers.figures.Figure;
import com.bpm.checkersweb.checkers.figures.None;

import java.util.ArrayList;
import java.util.List;

public class BoardRow {

    private final List<Figure> cols = new ArrayList<>();

    public BoardRow() {
        for (int col = 0; col < 8; col++)
            cols.add(new None());
    }

    public List<Figure> getCols() {
        return cols;
    }

    public String toString(int row) {
        String s = row + " |";
        for (int col = 0; col < 8; col++)
            s += cols.get(col) + "|";
        s += " " + row + "\n";
        return s;
    }
}
