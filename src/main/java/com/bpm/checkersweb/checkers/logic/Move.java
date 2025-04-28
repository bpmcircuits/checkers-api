package com.bpm.checkersweb.checkers.logic;

import java.awt.*;
import java.util.Objects;

public class Move {

    private final Point from;
    private final Point to;

    public Move(Point from, Point to) {
        this.from = from;
        this.to = to;
    }

    public Point getFromPoint() {
        return from;
    }

    public Point getToPoint() {
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return Objects.equals(from, move.from) && Objects.equals(to, move.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}

