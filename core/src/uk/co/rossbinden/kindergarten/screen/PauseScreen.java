package uk.co.rossbinden.kindergarten.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import uk.co.rossbinden.kindergarten.Kindergarten;

public class PauseScreen extends ScreenAdapter implements InputProcessor {

    private Kindergarten game;
    private Texture pauseScreenTexture;
    private SpriteBatch spriteBatch;

    public PauseScreen(Kindergarten game) {
        this.game = game;
        spriteBatch = new SpriteBatch();
        pauseScreenTexture = new Texture(Gdx.files.internal("title.png"));
    }

    @Override
    public void render(float delta) {
        spriteBatch.begin();
        spriteBatch.draw(pauseScreenTexture, 100, 0, 600, 600);
        spriteBatch.end();
    }

    @Override
    public void dispose() {
        pauseScreenTexture.dispose();
        spriteBatch.dispose();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        game.setScreen(game.getMainScreen());
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
