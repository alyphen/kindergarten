package uk.co.rossbinden.kindergarten.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;

public interface Level {

    void createSilhouette();
    Entity getSilhouette();
    void createShapes();
    Array<Entity> getShapes();
    boolean validate();

}
