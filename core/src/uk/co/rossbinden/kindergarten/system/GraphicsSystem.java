package uk.co.rossbinden.kindergarten.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import uk.co.rossbinden.kindergarten.component.BodyComponent;
import uk.co.rossbinden.kindergarten.component.TextureComponent;
import uk.co.rossbinden.kindergarten.screen.MainScreen;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static uk.co.rossbinden.kindergarten.component.ComponentMappers.BODY;
import static uk.co.rossbinden.kindergarten.component.ComponentMappers.TEXTURE;

public class GraphicsSystem extends EntitySystem {

    private Family renderables = Family.all(TextureComponent.class, BodyComponent.class).get();
    private Camera camera;
    private SpriteBatch spriteBatch;
    private Texture background;

    public GraphicsSystem(SpriteBatch spriteBatch, Camera camera, Texture background) {
        this.spriteBatch = spriteBatch;
        this.camera = camera;
        this.background = background;
    }

    @Override
    public void update(float deltaTime) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0);
        for (Entity entity : getEngine().getEntitiesFor(renderables)) {
            TextureRegion texture = TEXTURE.get(entity).getTexture();
            Body body = BODY.get(entity).getBody();
            spriteBatch.draw(
                    texture,
                    body.getPosition().x * MainScreen.SCALE, body.getPosition().y * MainScreen.SCALE,
                    0, 0,
                    texture.getRegionWidth(), texture.getRegionHeight(),
                    1f, 1f,
                    body.getAngle() * MathUtils.radDeg
            );
        }
        spriteBatch.end();
    }

}
