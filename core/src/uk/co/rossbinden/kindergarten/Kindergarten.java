package uk.co.rossbinden.kindergarten;

import com.badlogic.gdx.Game;
import uk.co.rossbinden.kindergarten.screen.MainScreen;
import uk.co.rossbinden.kindergarten.screen.PauseScreen;
import uk.co.rossbinden.kindergarten.screen.WinScreen;

public class Kindergarten extends Game {

    private MainScreen mainScreen;
    private PauseScreen pauseScreen;
    private WinScreen winScreen;

    @Override
    public void create() {
        mainScreen = new MainScreen(this);
        pauseScreen = new PauseScreen(this);
        winScreen = new WinScreen();
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

    public WinScreen getWinScreen() {
        return winScreen;
    }

}
