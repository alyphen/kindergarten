package uk.co.rossbinden.kindergarten.system;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraSystem extends EntitySystem {

    private OrthographicCamera camera;

    public CameraSystem(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    public void update(float deltaTime) {
        camera.update();
    }

}
