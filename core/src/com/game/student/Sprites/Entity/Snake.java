package com.game.student.Sprites.Entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.game.student.Const;
import com.game.student.Scenes.Hud;
import com.game.student.Screens.PlayScreen;

import java.util.LinkedList;

/**
 * The parent class for the player and enemies.
 * <p>
 *     Defines the common functions and variables used by the player and enemy
 * </p>
 * @since version 1.0
 */
public abstract class Snake {

    // Snake attributes
    protected Vector2 dir;
    protected Vector2 new_dir;
    public boolean alive;
    protected float xPosition;
    protected float yPosition;
    private int tail_size;
    private int spawnPosition;

    public enum Entity {
        PLAYER,
        ENEMY
    }
    protected Entity entity;

    // Box2d
    public Body b2body;
    protected int walls_to_left;
    protected int walls_to_right;

    private boolean cutting_tail = false;
    private boolean adding_tail = false;
    private int tail_cut_index = 0;

    // Sprites & Textures
    protected Sprite sprite;
    protected TextureRegion head_tex;
    protected TextureRegion body_tex;
    protected TextureRegion tail_tex;

    // Reference to the world
    protected World world;


    protected LinkedList<Vector3> trail_pos;

    /**
     * Constructor for the abstract snake class
     * @param screen Reference to the play screen
     * @param xPosition The x-position of the head
     * @param yPosition The y-position of the head
     * @param entity The type of entity which can be either "player" or "enemy"
     */
    public Snake(PlayScreen screen, float xPosition, float yPosition, int tail_size, int spawnPosition, Entity entity){
        this.world = screen.getWorld();
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.tail_size = tail_size;
        this.spawnPosition = spawnPosition;
        this.entity = entity;
        dir = new Vector2(0, 0);
        new_dir = new Vector2(0, 0);
        alive = true;
        walls_to_left = 0;
        walls_to_right = 0;

        trail_pos = new LinkedList<>();

        // Creates box2d body of the head and store it in b2body
        defineSnake();
    }

    /**
     * Update function
     * @param dt Delta time
     */
    public void update(float dt){
        if(cutting_tail){
            cutting_tail = false;
        }
        if(adding_tail){

            adding_tail = false;
        }

        sprite.setPosition(b2body.getPosition().x * Const.PPM - sprite.getWidth() / 2, b2body.getPosition().y * Const.PPM - sprite.getHeight() / 2);


        if (b2body.getLinearVelocity().x != 0 || b2body.getLinearVelocity().y != 0)
            trail_pos.addFirst(new Vector3(sprite.getX(), sprite.getY(), sprite.getRotation()));

        //BODY POSITION
        if ((b2body.getLinearVelocity().x < 0)){
            sprite.setRotation(90);
            b2body.setTransform(b2body.getPosition().x, b2body.getPosition().y, (float)Math.PI / 2.0f);
            sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        }
        else if ((b2body.getLinearVelocity().x > 0)) {
            sprite.setRotation(-90);
            b2body.setTransform(b2body.getPosition().x, b2body.getPosition().y, (float)-Math.PI / 2.0f);
            sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        }

        if ((b2body.getLinearVelocity().y < 0)){
            sprite.setRotation(180);
            b2body.setTransform(b2body.getPosition().x, b2body.getPosition().y, (float)Math.PI);
            sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        }
        else if ((b2body.getLinearVelocity().y > 0)) {
            sprite.setRotation(0);
            b2body.setTransform(b2body.getPosition().x, b2body.getPosition().y, 0);
            sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        }

        xPosition = b2body.getPosition().x * Const.PPM;
        yPosition = b2body.getPosition().y * Const.PPM;
    }

    /**
     * Grows the tail by one
     */
    public void add_tail(){
        adding_tail = true;
        if(entity == Entity.PLAYER)
            Hud.updateScore((int)Math.ceil((tail_size / 3.0f)));
    }




    /**
     * Removes the whole snake
     */

    /**
     * Draw function
     * @param batch SpriteBatch
     */
    public void draw(SpriteBatch batch) {
        batch.draw(sprite, sprite.getX(), sprite.getY(), sprite.getOriginX(), sprite.getOriginY(), sprite.getWidth(), sprite.getHeight(), 1, 1, sprite.getRotation());
        //Draws body and tail sprites
        drawBodyTail(batch);
    }

    /**
     * Creates the bodies for the tail, and initializes their positions
     * @param entity The type of entity which can be either "player" or "enemy"
     */
    protected void spawnSnake(Entity entity){
        for(int i = 1; i < tail_size - 1; i++){
            float x = b2body.getPosition().x;
            float y = b2body.getPosition().y - (i * Const.TAIL_GAP) / Const.PPM;

        }

        if(tail_size > 1) {
            float x = b2body.getPosition().x;
            float y = b2body.getPosition().y - ((tail_size-1) * Const.TAIL_GAP) / Const.PPM;

        }

        float startY = b2body.getPosition().y - (sprite.getHeight() / 2.0f) / Const.PPM;
        float endY = b2body.getPosition().y - (tail_size * Const.TAIL_GAP) / Const.PPM;

        for(float y = startY * Const.PPM; y > endY * Const.PPM - 1.0f; y--){
            trail_pos.addLast(new Vector3(b2body.getPosition().x * Const.PPM - sprite.getWidth() / 2.0f, y, 0));
        }
    }

    /**
     * Creates the Box2d body for the head of the snake
     */
    protected void defineSnake() {
        //Body collision body creation
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.position.set(xPosition / Const.PPM, yPosition / Const.PPM);  // position caterpillar
        shape.setAsBox(10 / Const.PPM, 25 / Const.PPM);

        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        switch (entity){
            case PLAYER:
                fdef.filter.categoryBits = Const.PLAYER_HEAD_BIT;
                fdef.filter.maskBits = Const.WALL_BIT | Const.SPAWNER_BIT | Const.ENEMY_BIT | Const.ENEMY_HEAD_BIT | Const.ENEMY_MOUTH_BIT;
                break;
            case ENEMY:
                fdef.filter.categoryBits = Const.ENEMY_HEAD_BIT;
                fdef.filter.maskBits = Const.WALL_BIT | Const.PLAYER_BIT | Const.PLAYER_HEAD_BIT | Const.PLAYER_MOUTH_BIT;
                break;
        }

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        //---------------------------------
        // Create left wall sensor
        PolygonShape box_shape = new PolygonShape();
        box_shape.setAsBox(10 / Const.PPM, 23 / Const.PPM, new Vector2(-35 / Const.PPM, 0), 0);

        fdef.shape = box_shape;
        fdef.isSensor = true;
        fdef.filter.categoryBits = Const.LEFT_BIT;
        fdef.filter.maskBits = (Const.WALL_BIT | Const.SPAWNER_BIT | Const.ENEMY_BIT);

        b2body.createFixture(fdef).setUserData(this);

        //---------------------------------
        // Create right wall sensor
        box_shape = new PolygonShape();
        box_shape.setAsBox(10 / Const.PPM, 24 / Const.PPM, new Vector2(35 / Const.PPM, 0), 0);

        //Create the body
        fdef.shape = box_shape;
        fdef.isSensor = true;
        fdef.filter.categoryBits = Const.RIGHT_BIT;

        //INITIALIZE THE COLLISION WITH OTHER BITS
        fdef.filter.maskBits = (Const.WALL_BIT | Const.SPAWNER_BIT | Const.ENEMY_BIT);

        b2body.createFixture(fdef).setUserData(this);

        //----------------------------------
        // Create edge fixture for the mouth
        box_shape = new PolygonShape();
        box_shape.setAsBox(10 / Const.PPM, 5 / Const.PPM, new Vector2(0, 20 / Const.PPM), 0);

        fdef.shape = box_shape;
        fdef.isSensor = true;

        switch (entity){
            case PLAYER:
                fdef.filter.categoryBits = Const.PLAYER_MOUTH_BIT;
                fdef.filter.maskBits = Const.ENEMY_HEAD_BIT;
                b2body.createFixture(fdef).setUserData(this);
                break;
            case ENEMY:
                fdef.filter.categoryBits = Const.ENEMY_MOUTH_BIT;
                fdef.filter.maskBits = Const.PLAYER_HEAD_BIT;
                b2body.createFixture(fdef).setUserData(this);
                break;
        }

        //----------------------------------
        // Cleanup
        shape.dispose();
    }

    protected void turn_and_move(float speed){
        // Set velocity to target direction only if there are no walls
        if(new_dir.x == -1){
            if(dir.y == 1 && walls_to_left == 0){
                move(new_dir.x, new_dir.y, speed);
            }
            else if(dir.y == -1 && walls_to_right == 0){
                move(new_dir.x, new_dir.y, speed);
            }
            else if(dir.y == 0){
                move(new_dir.x, new_dir.y, speed);
            }
        }
        else if(new_dir.x == 1){
            if(dir.y == -1 && walls_to_left == 0){
                move(new_dir.x, new_dir.y, speed);
            }
            else if(dir.y == 1 && walls_to_right == 0){
                move(new_dir.x, new_dir.y, speed);
            }
            else if(dir.y == 0){
                move(new_dir.x, new_dir.y, speed);
            }
        }
        else if(new_dir.y == -1){
            if(dir.x == -1 && walls_to_left == 0){
                move(new_dir.x, new_dir.y, speed);
            }
            else if(dir.x == 1 && walls_to_right == 0){
                move(new_dir.x, new_dir.y, speed);
            }
            else if(dir.x == 0){
                move(new_dir.x, new_dir.y, speed);
            }
        }
        else if(new_dir.y == 1){
            if(dir.x == -1 && walls_to_right == 0){
                move(new_dir.x, new_dir.y, speed);
            }
            else if(dir.x == 1 && walls_to_left == 0){
                move(new_dir.x, new_dir.y, speed);
            }
            else if(dir.x == 0) {
                move(new_dir.x, new_dir.y, speed);
            }
        }
    }

    protected void move(float x, float y, float speed){
        b2body.setLinearVelocity(x * speed, y * speed);
        dir.x = x;
        dir.y = y;
        new_dir = new Vector2(0, 0);
    }

    protected void move_left(float speed){
        if(dir.y == 1){
            move(-1, 0, speed);
        }
        else if(dir.y == -1){
            move(1, 0, speed);
        }
        else if(dir.x == -1){
            move(0, -1, speed);
        }
        else if(dir.x == 1){
            move(0, 1, speed);
        }
    }

    protected void move_right(float speed){
        if(dir.y == -1){
            move(-1, 0, speed);
        }
        else if(dir.y == 1){
            move(1, 0, speed);
        }
        else if(dir.x == 1){
            move(0, -1, speed);
        }
        else if(dir.x == -1){
            move(0, 1, speed);
        }
    }

    /**
     * Sets the positions and draw the tail sprites of the snake
     * @param batch SpriteBatch
     */
    private void drawBodyTail(SpriteBatch batch) {
        float distance = 0;                         // Distance traveled on the line
        float target_distance = Const.TAIL_GAP;     // Distance to travel before drawing the sprite
        int tail_idx = 0;                           // Index of the current tail sprite being rendered

        int pop_counter = 0;                        // How many positions to pop from the position list


        if(trail_pos.size() > 0) {
            Vector3 prev_pos = trail_pos.getFirst();    // Keeps track of the previous position in the position list

            for (Vector3 pos : trail_pos) {
                float dx = Math.abs(prev_pos.x - pos.x);
                float dy = Math.abs(prev_pos.y - pos.y);

                distance += dx + dy;



                prev_pos = pos;
            }

            // Removes every positions that are not needed
            for (int i = 0; i < pop_counter; i++) {
                trail_pos.removeLast();
            }
        }
    }

    /**
     * Called after the add_tail function is called
     */

    /**
     * Called after the cut_tail function is called
     */



    public void addWallToLeft(){
        walls_to_left++;
    }
    public void addWallToRight(){
        walls_to_right++;
    }
    public void removeWallToLeft(){
        walls_to_left--;
    }
    public void removeWallToRight(){
        walls_to_right--;
    }



    public Body getBody(){ return b2body; }
    public float getX(){ return sprite.getX(); }
    public float getY(){ return sprite.getY(); }
    public Vector2 getDir(){ return dir; }
    public int getTailSize(){ return tail_size; }

    public void setDir(int x, int y) { new_dir.x = x; new_dir.y = y; }
}