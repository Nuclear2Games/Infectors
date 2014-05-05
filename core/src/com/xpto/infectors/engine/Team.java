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

    public Team(float re, float ar, float da, float sp) {
        regenerate = re;
        armor = ar;
        damage = da;
        speedy = sp;
    }

    public abstract Color getColor();

    public abstract int getId();

    private float regenerate;

    public float getRegenerate() {
        return regenerate;
    }

    private float armor;

    public float getArmor() {
        return armor;
    }

    private float damage;

    public float getDamage() {
        return damage;
    }

    private float speedy;

    public float getSpeedy() {
        return speedy;
    }

    public static class Gray extends Team {
        public Gray() {
            super(100, 100, 100, 100);
        }

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
        public Blue() {
            super(100, 100, 100, 100);
        }

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
        public Red() {
            super(100, 100, 100, 100);
        }

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
        public Green() {
            super(100, 100, 100, 100);
        }

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
