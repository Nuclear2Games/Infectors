package com.xpto.infectors.engine;

import com.badlogic.gdx.graphics.Color;

public abstract class Team {
    public float getA() {
        return getColor().a;
    }

    public float getR() {
        return getColor().r;
    }

    public float getG() {
        return getColor().g;
    }

    public float getB() {
        return getColor().b;
    }

    public abstract Color getColor();

    public abstract int getId();

    public static class Gray extends Team {
        @Override
        public Color getColor() {
            return Color.GRAY;
        }

        @Override
        public int getId() {
            return 0;
        }
    }

    public static class Blue extends Team {
        @Override
        public Color getColor() {
            return Color.BLUE;
        }

        @Override
        public int getId() {
            return 1;
        }
    }

    public static class Red extends Team {
        @Override
        public Color getColor() {
            return Color.RED;
        }

        @Override
        public int getId() {
            return 2;
        }
    }

    public static class Green extends Team {
        @Override
        public Color getColor() {
            return Color.GREEN;
        }

        @Override
        public int getId() {
            return 3;
        }
    }
}
