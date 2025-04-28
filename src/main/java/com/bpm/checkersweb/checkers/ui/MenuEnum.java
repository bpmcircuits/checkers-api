package com.bpm.checkersweb.checkers.ui;

import java.util.HashMap;
import java.util.Map;

public class MenuEnum {

    public enum MainMenuOption {
        NEW_GAME(1),
        EXIT(2);

        private static final Map<Integer, MainMenuOption> BY_VALUE = new HashMap<>();

        static {
            for (MainMenuOption option : values()) {
                BY_VALUE.put(option.value, option);
            }
        }

        private final int value;

        MainMenuOption(int value) {
            this.value = value;
        }

        public static MainMenuOption fromValue(int value) {
            return BY_VALUE.get(value);
        }
    }

    public enum NewGameMenuOption {
        PLAYER_VS_PLAYER(1),
        PLAYER_VS_COMPUTER(2),
        BACK(3);

        private static final Map<Integer, NewGameMenuOption> BY_VALUE = new HashMap<>();

        static {
            for (NewGameMenuOption option : values()) {
                BY_VALUE.put(option.value, option);
            }
        }

        private final int value;

        NewGameMenuOption(int value) {
            this.value = value;
        }

        public static NewGameMenuOption fromValue(int value) {
            return BY_VALUE.get(value);
        }
    }

    public enum HumanOrComputerEnum {
        HUMAN,
        COMPUTER
    }

    public enum ComputerLevelEnum {
        EASY(1),
        HARD(3);

        public final int level;

        ComputerLevelEnum(int level) {
            this.level = level;
        }
    }
}
