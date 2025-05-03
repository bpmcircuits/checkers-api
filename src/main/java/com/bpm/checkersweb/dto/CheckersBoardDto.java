package com.bpm.checkersweb.dto;
import java.util.List;

public record CheckersBoardDto (String gameId,
                                List<BoardRowDto> rows,
                                String playerOne,
                                String playerTwo,
                                String currentPlayer,
                                List<String> capturedWhiteFigures,
                                List<String> capturedBlackFigures) {}
