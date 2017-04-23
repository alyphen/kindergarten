package uk.co.rossbinden.kindergarten.system;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.physics.box2d.World;

public class PhysicsSystem extends EntitySystem {

    private World world;

    public PhysicsSystem(World world) {
        this.world = world;
    }

    @Override
    public void update(float deltaTime) {
        world.step(deltaTime * 10f, 600, 200);
    }
}
