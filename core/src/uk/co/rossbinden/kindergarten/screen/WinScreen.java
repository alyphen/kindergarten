package uk.co.rossbinden.kindergarten.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class WinScreen extends ScreenAdapter {

    private SpriteBatch spriteBatch;
    private BitmapFont font;

    public WinScreen() {
        spriteBatch = new SpriteBatch();
        font = new BitmapFont();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();
        spriteBatch.setColor(Color.WHITE);
        font.draw(spriteBatch, "Win!", 32, 32);
        spriteBatch.end();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        font.dispose();
    }
}
