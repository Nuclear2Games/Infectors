package com.xpto.infectors;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.xpto.infectors.screens.MainMenu;

public class Global extends Game {
    private OrthographicCamera camera;
    private SpriteBatch batch;

    public SpriteBatch batch() {
        return batch;
    }

    private long bgStart;
    private long bgDuration;

    // Current color
    private float r;
    private float g;
    private float b;
    private float a;

    // Initial color
    private float r_0;
    private float g_0;
    private float b_0;
    private float a_0;

    // Target color
    private float r_1;
    private float g_1;
    private float b_1;
    private float a_1;

    public void setBackground(float _red, float _green, float _blue, float _alpha) {
        setBackground(_red, _green, _blue, _alpha, 0);
    }

    public void setBackground(float _red, float _green, float _blue, float _alpha, long _duration) {
        if (_duration <= 0) {
            r_1 = r_0 = r = _red;
            g_1 = g_0 = g = _green;
            b_1 = b_0 = b = _blue;
            a_1 = a_0 = a = _alpha;

            bgDuration = 0;
        } else {
            r_0 = r;
            g_0 = g;
            b_0 = b;
            a_0 = a;

            r_1 = _red;
            g_1 = _green;
            b_1 = _blue;
            a_1 = _alpha;

            bgDuration = _duration;
        }

        bgStart = System.currentTimeMillis();
    }

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        batch = new SpriteBatch();

        r_1 = r_0 = r = 0;
        g_1 = g_0 = g = 0;
        b_1 = b_0 = b = 0;
        a_1 = a_0 = a = 1;

        // Start with main menu
        this.setScreen(new MainMenu(this));
    }

    @Override
    public void dispose() {
        if (getScreen() != null)
            getScreen().dispose();

        batch.dispose();

        super.dispose();
    }

    @Override
    public void render() {
        // Backgroud color transition
        if (bgDuration > 0) {
            long elapsed = System.currentTimeMillis() - bgStart;

            if (elapsed >= bgDuration) {
                bgDuration = 0;

                r = r_0 = r_1;
                g = g_0 = g_1;
                b = b_0 = b_1;
                a = a_0 = a_1;
            } else {
                // Index of target color
                float i1 = (float) elapsed / (float) bgDuration;

                // Index of initial color
                float i0 = 1 - i1;

                r = r_0 * i0 + r_1 * i1;
                g = g_0 * i0 + g_1 * i1;
                b = b_0 * i0 + b_1 * i1;
                a = a_0 * i0 + a_1 * i1;
            }
        }

        Gdx.gl.glClearColor(r, g, b, a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        super.render();
        batch.end();
    }
}
