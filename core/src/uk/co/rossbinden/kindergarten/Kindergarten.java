package uk.co.rossbinden.kindergarten;

import com.badlogic.gdx.Game;
import uk.co.rossbinden.kindergarten.screen.MainScreen;
import uk.co.rossbinden.kindergarten.screen.PauseScreen;

public class Kindergarten extends Game {

    private MainScreen mainScreen;
    private PauseScreen pauseScreen;

    @Override
    public void create() {
        mainScreen = new MainScreen(this);
        pauseScreen = new PauseScreen(this);
        setScreen(pauseScreen);
    }

    @Override
    public void dispose() {
        mainScreen.dispose();
        pauseScreen.dispose();
    }

    public MainScreen getMainScreen() {
        return mainScreen;
    }

    public PauseScreen getPauseScreen() {
        return pauseScreen;
    }

}
