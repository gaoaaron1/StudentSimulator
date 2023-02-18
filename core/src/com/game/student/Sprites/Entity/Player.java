package com.game.student.Sprites.Entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.game.student.Const;
import com.game.student.Screens.PlayScreen;

public class Player extends Snake {

    //------------------------------- CONSTRUCTOR -------------------------------//

    public Player(PlayScreen screen) {
        super(screen, Const.V_WIDTH / 2.0f, Const.V_HEIGHT / 2.0f, Const.PLAYER_TAIL_SIZE, -1, Entity.PLAYER);

        // Create the textures for the different parts of the snake
        TextureAtlas myAtlas = new TextureAtlas("CaterpillarAtlas.atlas");
        head_tex = new TextureRegion(myAtlas.findRegion("player_head"), 0, 0, 64, 64);

        // Creates the sprite for the head
        sprite = new Sprite();
        sprite = new Sprite(head_tex);

        // Creates and sets the positions for the tail of the snake
        spawnSnake(Entity.PLAYER);
    }

    //--------------------------------------------- METHODS ---------------------------------------------------------------//
    public void update(float dt) {
        super.update(dt);
    }

    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }

}