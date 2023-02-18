package com.game.student.Sprites.Items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class AppleSpawner {
    private final Array<Apple> apples;
    private Array<Apple> apples_to_remove;

    private final Vector2[] path_arr;
    private final World world;

    public AppleSpawner(World world, Vector2[] path_arr) {
        apples = new Array<>();
        apples_to_remove = new Array<>();
        this.path_arr = path_arr;
        this.world = world;

        spawnApple();
    }

    public void update(float dt) {
        for (Apple apple : apples) {
            apple.update(dt);
            if (apple.destroyed) {
                world.destroyBody(apple.getBody());
                apples_to_remove.add(apple);
                spawnApple();
            }
        }
        apples.removeAll(apples_to_remove, true);
        apples_to_remove.clear();
    }

    public void spawnApple() {
        Random random = new Random();
        int index = random.nextInt(path_arr.length - 1);
        Vector2 pos = path_arr[index];

        Apple apple = new Apple(world, pos);
        apples.add(apple);
    }

    public void draw(SpriteBatch batch) {
        for (Apple apple : apples) {
            apple.draw(batch);
        }
    }

    public Array<Apple> getApples() {
        return apples;
    }


}
