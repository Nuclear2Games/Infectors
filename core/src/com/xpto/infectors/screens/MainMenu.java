package com.xpto.infectors.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.xpto.infectors.Global;
import com.xpto.infectors.engine.Board;

public class MainMenu extends ScreenAdapter {
    private Global game;

    private BitmapFont font;

    public MainMenu(Global _game) {
        game = _game;
        game.setBackground(0, 1, 0, 1);
        game.setBackground(1, 0, 0, 1, 3000);

        font = new BitmapFont();
        font.setColor(1, 1, 1, 1);
    }

    @Override
    public void render(float delta) {
        font.draw(game.batch(), "Test", 10, 400);

        if (Gdx.input.isTouched()) {
            game.setScreen(new Board(game));
            dispose();
        }
    }
}
