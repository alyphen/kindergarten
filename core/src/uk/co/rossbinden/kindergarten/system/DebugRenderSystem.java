package uk.co.rossbinden.kindergarten.system;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;

public class DebugRenderSystem extends EntitySystem {

    private Box2DDebugRenderer debugRenderer;
    private World world;
    private OrthographicCamera camera;

    public DebugRenderSystem(World world, OrthographicCamera camera) {
        debugRenderer = new Box2DDebugRenderer();
        this.world = world;
        this.camera = camera;
    }

    @Override
    public void update(float deltaTime) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
        debugRenderer.render(world, camera.combined);
    }
}
