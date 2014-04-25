package com.xpto.infectors.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.xpto.infectors.Global;

public class MainMenu extends ScreenAdapter {
    private Global game;

    private Texture img;
    private BitmapFont font;

    public MainMenu(Global _game) {
        game = _game;
        game.setBackground(0, 1, 0, 1);
        game.setBackground(1, 0, 0, 1, 3000);

        img = new Texture("badlogic.jpg");
        font = new BitmapFont();
        font.setColor(1, 1, 1, 1);
    }

    @Override
    public void render(float delta) {
        game.batch().draw(img, 0, 0);
        font.draw(game.batch(), "Test", 10, 400);
    }
}
