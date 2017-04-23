package uk.co.rossbinden.kindergarten.screen;

import aurelienribon.bodyeditor.BodyEditorLoader;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import net.dermetfan.gdx.physics.box2d.ContactAdapter;
import uk.co.rossbinden.kindergarten.Kindergarten;
import uk.co.rossbinden.kindergarten.component.BodyComponent;
import uk.co.rossbinden.kindergarten.component.TextureComponent;
import uk.co.rossbinden.kindergarten.level.Level;
import uk.co.rossbinden.kindergarten.system.CameraSystem;
import uk.co.rossbinden.kindergarten.system.GraphicsSystem;
import uk.co.rossbinden.kindergarten.system.PhysicsSystem;

import static com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody;
import static com.badlogic.gdx.physics.box2d.BodyDef.BodyType.StaticBody;
import static uk.co.rossbinden.kindergarten.component.ComponentMappers.TEXTURE;

public class MainScreen extends ScreenAdapter implements InputProcessor {

    private static final float MIN_ZOOM =
            /*/
            2.5F;
            /*/
            0.0001F;
            //*/
    private static final float MAX_ZOOM = 5F;
    public static final float SCALE = 8F;

    private Kindergarten game;
    private World world;
    private Engine engine;
    private BodyEditorLoader bodyLoader;
    private SpriteBatch spriteBatch;
    private OrthographicCamera camera;
    private Texture background;
    private Sound boxKnock;
    private Array<Level> levels;
    private int levelIndex;
    private Array<Entity> collidingEntities;
    private boolean levelComplete = false;

    private Body selection;
    private int prevMouseX;
    private int prevMouseY;

    public MainScreen(Kindergarten game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.zoom = 5F;

        spriteBatch = new SpriteBatch();

        collidingEntities = new Array<>();

        Box2D.init();
        world = new World(new Vector2(0, -10), false);
        world.setContactListener(new ContactAdapter() {
            @Override
            public void beginContact(Contact contact) {
                if (contact.getFixtureA().getBody().getUserData() != null && contact.getFixtureB().getBody().getUserData() != null && !contact.getFixtureA().isSensor() && !contact.getFixtureB().isSensor()) {
                    boxKnock.play();
                }
                if (contact.getFixtureA().getBody().getUserData() == levels.get(levelIndex).getSilhouette()) {
                    collidingEntities.add((Entity) contact.getFixtureB().getBody().getUserData());
                }
                if (contact.getFixtureB().getBody().getUserData() == levels.get(levelIndex).getSilhouette()) {
                    collidingEntities.add((Entity) contact.getFixtureA().getBody().getUserData());
                }
                if (levels.get(levelIndex).validate()) {
                    if (levelIndex < levels.size - 1) {
                        levelComplete = true;
                    } else {
                        Gdx.app.log(getClass().getName(), "WIN");
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {
                if (contact.getFixtureA().getBody().getUserData() == levels.get(levelIndex).getSilhouette()) {
                    collidingEntities.removeValue((Entity) contact.getFixtureB().getBody().getUserData(), true);
                }
                if (contact.getFixtureB().getBody().getUserData() == levels.get(levelIndex).getSilhouette()) {
                    collidingEntities.removeValue((Entity) contact.getFixtureA().getBody().getUserData(), true);
                }
            }
        });

        background = new Texture(Gdx.files.internal("bg.png"));
        boxKnock = Gdx.audio.newSound(Gdx.files.internal("Box Knock.wav"));

        camera.position.set(background.getWidth() / 2f, background.getHeight() / 2f, 0f);
        engine = new Engine();

        engine.addSystem(new PhysicsSystem(world));
        engine.addSystem(new CameraSystem(camera));
        /*/
        engine.addSystem(new DebugRenderSystem(world, camera));
        /*/
        engine.addSystem(new GraphicsSystem(spriteBatch, camera, background));
        //*/

        bodyLoader = new BodyEditorLoader(Gdx.files.internal("bodies.json"));

        levels = new Array<>();
        levels.add(new Level() {

            private Entity silhouette;
            private Array<Entity> requiredShapes;

            @Override
            public void createSilhouette() {
                silhouette = loadSilhouette("IMG_0332.PNG", 950, 1296, "IMG_0332");
            }

            @Override
            public Entity getSilhouette() {
                return silhouette;
            }

            @Override
            public void createShapes() {
                requiredShapes = new Array<>();
                requiredShapes.add(loadShape("IMG_0317.PNG", 600, 600, "Circle 7"));
                requiredShapes.add(loadShape("IMG_0317.PNG", 600, 600, "Circle 7"));
                requiredShapes.add(loadShape("IMG_0317.PNG", 600, 600, "Circle 7"));
                requiredShapes.add(loadShape("IMG_0321.PNG", 100, 700, "Rect.2 11"));
                requiredShapes.add(loadShape("IMG_0316.PNG", 500, 600, "Triangle Pink 6"));
                requiredShapes.add(loadShape("IMG_0318.PNG", 700, 600, "Square Orange.2 8"));
                requiredShapes.add(loadShape("image3.PNG", 800, 700, "image3"));
                loadShape("IMG_0311.PNG", 0, 600, "Square");
            }

            @Override
            public Array<Entity> getShapes() {
                return requiredShapes;
            }

            @Override
            public boolean validate() {
                for (Entity entity : requiredShapes) {
                    if (!collidingEntities.contains(entity, true)) {
                        return false;
                    }
                }
                return true;
            }

        });
        levels.add(new Level() {

            private Entity silhouette;
            private Array<Entity> requiredShapes;

            @Override
            public void createSilhouette() {
                silhouette = loadSilhouette("IMG_0333.PNG", 950, 1296, "lug 2");
            }

            @Override
            public Entity getSilhouette() {
                return silhouette;
            }

            @Override
            public void createShapes() {
                requiredShapes = new Array<>();
                requiredShapes.add(loadShape("IMG_0325.PNG", 500, 700, "Bridge Half 15"));
                requiredShapes.add(loadShape("IMG_0325.PNG", 500, 700, "Bridge Half 15"));
                requiredShapes.add(loadShape("IMG_0314.PNG", 300, 600, "SemiCircle Empty 4"));
                requiredShapes.add(loadShape("teacup.PNG", 900, 700, "teacup"));
                loadShape("IMG_0311.PNG", 0, 600, "Square");
                loadShape("IMG_0311.PNG", 0, 600, "Square");
                loadShape("IMG_0318.PNG", 700, 600, "Square Orange.2 8");
                loadShape("IMG_0318.PNG", 700, 600, "Square Orange.2 8");
                loadShape("IMG_0315.PNG", 400, 600, "SemiCircly Full 5");
                loadShape("IMG_0322.PNG", 200, 700, "Trap.2 12");
                loadShape("IMG_0323.PNG", 300, 700, "Trap.3 13");
                loadShape("IMG_0320.PNG", 0, 700, "Trap 10");
                loadShape("IMG_0313.PNG", 200, 600, "Triangle");
                loadShape("image2.PNG", 700, 700, "image2");
            }

            @Override
            public Array<Entity> getShapes() {
                return requiredShapes;
            }

            @Override
            public boolean validate() {
                for (Entity entity : requiredShapes) {
                    if (!collidingEntities.contains(entity, true)) {
                        return false;
                    }
                }
                return true;
            }

        });
        levels.add(new Level() {

            private Entity silhouette;
            private Array<Entity> requiredShapes;

            @Override
            public void createSilhouette() {
                silhouette = loadSilhouette("IMG_0334.PNG", 950, 1296, "lugflo 3");
            }

            @Override
            public Entity getSilhouette() {
                return silhouette;
            }

            @Override
            public void createShapes() {
                requiredShapes = new Array<>();
                requiredShapes.add(loadShape("IMG_0325.PNG", 500, 700, "Bridge Half 15"));
                requiredShapes.add(loadShape("IMG_0325.PNG", 500, 700, "Bridge Half 15"));
                requiredShapes.add(loadShape("IMG_0314.PNG", 300, 600, "SemiCircle Empty 4"));
                loadShape("teacup.PNG", 900, 700, "teacup");
                loadShape("IMG_0311.PNG", 0, 600, "Square");
                loadShape("IMG_0311.PNG", 0, 600, "Square");
                loadShape("IMG_0318.PNG", 700, 600, "Square Orange.2 8");
                loadShape("IMG_0318.PNG", 700, 600, "Square Orange.2 8");
                loadShape("IMG_0315.PNG", 400, 600, "SemiCircly Full 5");
                loadShape("IMG_0322.PNG", 200, 700, "Trap.2 12");
                loadShape("IMG_0323.PNG", 300, 700, "Trap.3 13");
                loadShape("IMG_0320.PNG", 0, 700, "Trap 10");
                loadShape("IMG_0313.PNG", 200, 600, "Triangle");
                requiredShapes.add(loadShape("image2.PNG", 700, 700, "image2"));
            }

            @Override
            public Array<Entity> getShapes() {
                return requiredShapes;
            }

            @Override
            public boolean validate() {
                for (Entity entity : requiredShapes) {
                    if (!collidingEntities.contains(entity, true)) {
                        return false;
                    }
                }
                return true;
            }

        });
        loadLevel();

//        loadShape("IMG_0311.PNG", 0, 600, "Square");
//        loadShape("IMG_0312.PNG", 100, 600, "rect");
//        loadShape("IMG_0313.PNG", 200, 600, "Triangle");
//        loadShape("IMG_0314.PNG", 300, 600, "SemiCircle Empty 4");
//        loadShape("IMG_0315.PNG", 400, 600, "SemiCircly Full 5");
//        loadShape("IMG_0316.PNG", 500, 600, "Triangle Pink 6");
//        loadShape("IMG_0317.PNG", 600, 600, "Circle 7");
//        loadShape("IMG_0318.PNG", 700, 600, "Square Orange.2 8");
//        loadShape("IMG_0319.PNG", 800, 600, "Semi Circle empty half 9");
//        loadShape("IMG_0320.PNG", 0, 700, "Trap 10");
//        loadShape("IMG_0321.PNG", 100, 700, "Rect.2 11");
//        loadShape("IMG_0322.PNG", 200, 700, "Trap.2 12");
//        loadShape("IMG_0323.PNG", 300, 700, "Trap.3 13");
//        loadShape("IMG_0324.PNG", 400, 700, "Rect.3 14");
//        loadShape("IMG_0325.PNG", 500, 700, "Bridge Half 15");
//        loadShape("image1.PNG", 600, 700, "image1");
//        loadShape("image2.PNG", 700, 700, "image2");
//        loadShape("image3.PNG", 800, 700, "image3");
//        loadShape("teacup.PNG", 900, 700, "teacup");
    }

    private Entity loadShape(String textureName, int x, int y, String fixtureName) {
        Entity entity = new Entity();
        Texture texture = new Texture(Gdx.files.internal(textureName));
        entity.add(new TextureComponent(new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight())));
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = DynamicBody;
        bodyDef.position.set(x / SCALE, y / SCALE);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 5f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.1f;
        Body body = world.createBody(bodyDef);
        bodyLoader.attachFixture(body, fixtureName, fixtureDef, texture.getWidth() / SCALE);
        body.setUserData(entity);
        entity.add(new BodyComponent(body));
        engine.addEntity(entity);
        return entity;
    }

    private Entity loadSilhouette(String textureName, int x, int y, String fixtureName) {
        Entity entity = new Entity();
        Texture texture = new Texture(Gdx.files.internal(textureName));
        entity.add(new TextureComponent(new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight())));
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = StaticBody;
        bodyDef.position.set(x / SCALE, y / SCALE);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 0f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f;
        fixtureDef.isSensor = true;
        Body body = world.createBody(bodyDef);
        bodyLoader.attachFixture(body, fixtureName, fixtureDef, texture.getWidth() / SCALE);
        body.setUserData(entity);
        entity.add(new BodyComponent(body));
        engine.addEntity(entity);
        return entity;
    }

    private void loadLevel() {
        engine.removeAllEntities();
        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);
        for (Body body : bodies) {
            world.destroyBody(body);
        }

        Entity floor = new Entity();
        BodyDef floorBodyDef = new BodyDef();
        floorBodyDef.type = StaticBody;
        Body floorBody = world.createBody(floorBodyDef);
        PolygonShape floorShape = new PolygonShape();
        floorShape.setAsBox(4096/ SCALE, 80/ SCALE);
        floorBody.createFixture(floorShape, 0.0f);
        floorShape.dispose();
        floorBody.setTransform(0 / SCALE, 0 / SCALE, floorBody.getAngle() * MathUtils.radDeg);
        floor.add(new BodyComponent(floorBody));
        engine.addEntity(floor);

        Entity leftWall = new Entity();
        BodyDef leftWallBodyDef = new BodyDef();
        leftWallBodyDef.type = StaticBody;
        Body leftWallBody = world.createBody(leftWallBodyDef);
        PolygonShape leftWallShape = new PolygonShape();
        leftWallShape.setAsBox(1 / SCALE, 2048 / SCALE);
        leftWallBody.createFixture(leftWallShape, 0.0f);
        leftWallShape.dispose();
        leftWallBody.setTransform(0 / SCALE, 2048 / SCALE, leftWallBody.getAngle() * MathUtils.radDeg);
        leftWallBody.getPosition().set(0 / SCALE, 2048 / SCALE);
        leftWall.add(new BodyComponent(leftWallBody));
        engine.addEntity(leftWall);

        Entity rightWall = new Entity();
        BodyDef rightWallBodyDef = new BodyDef();
        rightWallBodyDef.type = StaticBody;
        Body rightWallBody = world.createBody(rightWallBodyDef);
        PolygonShape rightWallShape = new PolygonShape();
        rightWallShape.setAsBox(1 / SCALE, 2048 / SCALE);
        rightWallBody.createFixture(rightWallShape, 0.0f);
        rightWallShape.dispose();
        rightWallBody.setTransform(4096 / SCALE, 2048 / SCALE, rightWallBody.getAngle() * MathUtils.radDeg);
        rightWall.add(new BodyComponent(rightWallBody));
        engine.addEntity(rightWall);

        Entity table = new Entity();
        BodyDef tableBodyDef = new BodyDef();
        tableBodyDef.type = StaticBody;
        Body tableBody = world.createBody(tableBodyDef);
        PolygonShape tableShape = new PolygonShape();
        tableShape.setAsBox(1100 / SCALE, 37 / SCALE);
        tableBody.createFixture(tableShape, 0.0f);
        tableShape.dispose();
        tableBody.setTransform(2100 / SCALE, 1250 / SCALE, tableBody.getAngle() * MathUtils.radDeg);
        table.add(new BodyComponent(tableBody));
        engine.addEntity(table);
        levels.get(levelIndex).createSilhouette();
        levels.get(levelIndex).createShapes();
    }

    @Override
    public void render(float delta) {
        if (levelComplete) {
            levelIndex++;
            loadLevel();
            levelComplete = false;
        }
        engine.update(delta);
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
    public void dispose() {
        world.dispose();
        spriteBatch.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        game.setScreen(game.getPauseScreen());
        return true;
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
        prevMouseX = screenX;
        prevMouseY = screenY;
        Vector3 worldPosition = camera.unproject(new Vector3(screenX, screenY, 0f));
        Gdx.app.log("x", Float.toString(worldPosition.x));
        Gdx.app.log("y", Float.toString(worldPosition.y));
        worldPosition.scl(1 / SCALE);
        Array<Fixture> fixtures = new Array<>();
        world.getFixtures(fixtures);
        for (Fixture fixture : fixtures) {
            if (fixture.testPoint(worldPosition.x, worldPosition.y)) {
                Object userData = fixture.getBody().getUserData();
                if (userData != null) {
                    if (!fixture.isSensor()) {
                        selection = fixture.getBody();
                        selection.setActive(false);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (selection != null) {
            selection.setActive(true);
        }
        selection = null;
        return true;
    }

    @Override
    public boolean touchDragged(final int screenX, final int screenY, int pointer) {
        if (selection == null) {
            camera.position.set(camera.position.x + ((prevMouseX - screenX) * camera.zoom), camera.position.y - ((prevMouseY - screenY) * camera.zoom), 0f);
            if (camera.position.x - (Gdx.graphics.getWidth() / 2 * camera.zoom) < 0) {
                camera.position.x = Gdx.graphics.getWidth() / 2 * camera.zoom;
            } else if (camera.position.x + (Gdx.graphics.getWidth() / 2 * camera.zoom) > 4096) {
                camera.position.x = 4096 - (Gdx.graphics.getWidth() / 2 * camera.zoom);
            }
            if (camera.position.y - (Gdx.graphics.getHeight() / 2 * camera.zoom) < 0) {
                camera.position.y = Gdx.graphics.getHeight() / 2 * camera.zoom;
            } else if (camera.position.y + (Gdx.graphics.getHeight() / 2 * camera.zoom) > 4096) {
                camera.position.y = 4096 - (Gdx.graphics.getHeight() / 2 * camera.zoom);
            }
        } else {
            Vector3 worldPosition = camera.unproject(new Vector3(screenX, screenY, 0f));
            worldPosition.scl(1 / SCALE);
            Object userData = selection.getUserData();
            TextureRegion texture = TEXTURE.get(((Entity) userData)).getTexture();
            Vector2 offset = new Vector2(texture.getRegionWidth() / (2 * SCALE), texture.getRegionHeight() / (2 * SCALE));
            offset.rotateRad(selection.getAngle());
            selection.setTransform(worldPosition.x - offset.x, worldPosition.y - offset.y, selection.getAngle());
        }
        prevMouseX = screenX;
        prevMouseY = screenY;
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        if (selection == null) {
            if (amount > 0) {
                camera.zoom *= 1.1;
            } else if (amount < 0) {
                camera.zoom /= 1.1;
            }
            if (camera.zoom > MAX_ZOOM) {
                camera.zoom = MAX_ZOOM;
            }
            if (camera.zoom < MIN_ZOOM) {
                camera.zoom = MIN_ZOOM;
            }
            if (camera.position.x - (Gdx.graphics.getWidth() / 2 * camera.zoom) < 0) {
                camera.position.x = Gdx.graphics.getWidth() / 2 * camera.zoom;
            } else if (camera.position.x + (Gdx.graphics.getWidth() / 2 * camera.zoom) > 4096) {
                camera.position.x = 4096 - (Gdx.graphics.getWidth() / 2 * camera.zoom);
            }
            if (camera.position.y - (Gdx.graphics.getHeight() / 2 * camera.zoom) < 0) {
                camera.position.y = Gdx.graphics.getHeight() / 2 * camera.zoom;
            } else if (camera.position.y + (Gdx.graphics.getHeight() / 2 * camera.zoom) > 4096) {
                camera.position.y = 4096 - (Gdx.graphics.getHeight() / 2 * camera.zoom);
            }
        } else {
            if (amount > 0) {
                Vector3 worldPosition = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f));
                worldPosition.scl(1 / SCALE);
                Object userData = selection.getUserData();
                TextureRegion texture = TEXTURE.get(((Entity) userData)).getTexture();
                Vector2 offset = new Vector2(texture.getRegionWidth() / (2 * SCALE), texture.getRegionHeight() / (2 * SCALE));
                offset.rotateRad(selection.getAngle());
                selection.setTransform(worldPosition.x - offset.x, worldPosition.y - offset.y, selection.getAngle() + 0.1f);
            } else if (amount < 0) {
                Vector3 worldPosition = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f));
                worldPosition.scl(1 / SCALE);
                Object userData = selection.getUserData();
                TextureRegion texture = TEXTURE.get(((Entity) userData)).getTexture();
                Vector2 offset = new Vector2(texture.getRegionWidth() / (2 * SCALE), texture.getRegionHeight() / (2 * SCALE));
                offset.rotateRad(selection.getAngle());
                selection.setTransform(worldPosition.x - offset.x, worldPosition.y - offset.y, selection.getAngle() - 0.1f);
            }
        }
        return true;
    }
}
