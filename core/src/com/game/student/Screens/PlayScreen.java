package com.game.student.Screens;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.game.student.Const;
import com.game.student.Scenes.Hud;
import com.game.student.Scenes.Options;
import com.game.student.Scenes.Tutorial;
import com.game.student.Simulator;
import com.game.student.Sprites.Entity.Player;
import com.game.student.Sprites.Items.AppleSpawner;
import com.game.student.Tools.CollisionDetectors;
import com.game.student.Tools.WorldGenerator;


public class PlayScreen implements Screen, GestureDetector.GestureListener, ApplicationListener, InputProcessor {

    //------------------------------------------------ INITIALIZE VARIABLES -------------------------------------//
    //OBJECTS
    public static Simulator game;
    public static Player player;
    public Vector2[] path_arr;

    public AppleSpawner appleSpawner;
    public static MapObject mapObject;

    //CAMERA
    private OrthographicCamera b2dCam;
    private OrthographicCamera gameCam;
    public Viewport gamePort;

    //Tiled map variables
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer; //Graphics for tiled map
    private OrthographicCamera tile_cam;
    private Viewport tile_viewport;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private WorldGenerator creator;

    //MUSIC
    private AssetManager manager;

    //OTHER
    public static Array<Body> destroyer;
    public Array<PlayScreen> objectsToSpawn = new Array<PlayScreen>();
    private boolean gameOver;

    public static int enemies_count;

    //STAGES FOR ACTORS
    public static Stage stage1, stage2;

    //Gesture variable for input
    public static InputMultiplexer inputMultiPlex; //Add gestures and buttons
    GestureDetector gestureDetector;

    //TIMER
    private Integer worldTimer;
    private float timeCount;
    private float deathTimer;
    private float accumulator = 0;

    private Rectangle rectangles[];

    //OBJECTS
    private Hud hud;
    public static Options options = SelectScreen.options;
    public static Tutorial tutorial = SelectScreen.tutorial;

    //PRESTART GAME BOOLEANS
    public static boolean newPlayer;
    public static boolean beginGame;

    private boolean filterAdded;

//--------------------------------------------------------- CONSTRUCTOR -------------------------------------------//

    public PlayScreen(Simulator game) {
        //Initialize timer
        worldTimer = 0;
        timeCount = 0;
        deathTimer = 0;

        filterAdded = false;

        this.game = game;

        //--------//
        // Camera //
        //--------//


        // Create camera for sprites
        gameCam = new OrthographicCamera();
        gameCam.setToOrtho(false, Const.V_WIDTH, Const.V_HEIGHT);

        //create a FitViewport to maintain virtual aspect ratio despite screen size
        gamePort = new ExtendViewport(Const.V_WIDTH, Const.V_HEIGHT, gameCam);

        //-------//
        // Box2D //
        //-------//

        // Create Box2d camera
        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, Const.V_WIDTH / Const.PPM, Const.V_HEIGHT / Const.PPM);

        //allows for debug lines of our box2d world.
        b2dr = new Box2DDebugRenderer();

        //create our Box2D world, setting no gravity in X, -10 gravity in Y, and allow bodies to sleep
        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new CollisionDetectors());

        //-----//
        // Map //
        //-----//

        //Load our map and setup our map renderer
        maploader = new TmxMapLoader();

        map = maploader.load("UniversityMap.tmx");

        renderer = new OrthogonalTiledMapRenderer(map, 1f);

        tile_cam = new OrthographicCamera();
        tile_cam.setToOrtho(false, Const.V_WIDTH, Const.V_HEIGHT);
        tile_cam.position.x = Const.V_WIDTH / 2f - Const.offset_x;
        tile_cam.position.y = Const.V_HEIGHT / 2f - Const.offset_y;
        tile_cam.update();

        tile_viewport = new ExtendViewport(Const.V_WIDTH, Const.V_HEIGHT, tile_cam);

        game.shapeRenderer.setProjectionMatrix(gameCam.combined);
        game.shapeRenderer.setColor(0.06f, 0.1f, 0.06f, 1.0f);

        int border_size = Const.offset_x + Const.TILE_WIDTH;

        rectangles = new Rectangle[4];
        rectangles[0] = new Rectangle(0, 0, border_size, Const.TILE_HEIGHT * Const.MAP_HEIGHT + border_size * 2);
        rectangles[1] = new Rectangle(Const.MAP_WIDTH * Const.TILE_WIDTH + border_size, 0, border_size, Const.TILE_HEIGHT * Const.MAP_HEIGHT + border_size * 2);
        rectangles[2] = new Rectangle(0, Const.TILE_HEIGHT * Const.MAP_HEIGHT + border_size, Const.MAP_WIDTH * Const.TILE_WIDTH + border_size * 2, border_size);
        rectangles[3] = new Rectangle(0, 0, Const.MAP_WIDTH * Const.TILE_WIDTH + border_size * 2, border_size);

        enemies_count = 0;

        path_arr = new Vector2[185];
        creator = new WorldGenerator(this);

        //--------------//
        // Game Objects //
        //--------------//
        player = new Player(this);


        //appleSpawner = new AppleSpawner(world, path_arr);

        //------------//
        // Utilities //
        //-----------//

        destroyer = new Array<Body>();

        gestureDetector = new GestureDetector(this);

        inputMultiPlex = new InputMultiplexer();


        //Declarations for beginning game
        beginGame = false;
        stage1 = new Stage(new ExtendViewport(Const.V_WIDTH, Const.V_HEIGHT));

        checkNewPlayer();
    }

//-------------------------------------- METHODS ------------------------------------------//

    public void displayEatLabel(String label, String entity, float xPosition, float yPosition) {
        hud.handleEatLabels(label, entity, xPosition, yPosition);
    }

    private void checkNewPlayer() {
        //Declare if new player
        newPlayer = Hud.prefs.getBoolean("newPlayer:", true);
        ;

        if (newPlayer) {
            Gdx.input.setInputProcessor(tutorial.inputMultiPlex);
            tutorial.tutorialHandler();
            Hud.prefs.putBoolean("newPlayer:", false);
            Hud.prefs.flush();
        }
    }


    @Override
    public void show() {
        hud = new Hud();
        stage2 = hud.getStage2();
        inputMultiPlex.addProcessor(gestureDetector);
        inputMultiPlex.addProcessor(hud.getStage1());
    }

    public void handleInput(float dt) {
        //control our spawned using immediate impulses
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.b2body.applyLinearImpulse(new Vector2(0.05f, 0), player.b2body.getWorldCenter(), true);
            if (beginGame == false) {
                beginGame = true;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.b2body.applyLinearImpulse(new Vector2(-0.05f, 0), player.b2body.getWorldCenter(), true);
            if (beginGame == false) {
                beginGame = true;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.b2body.applyLinearImpulse(new Vector2(0f, 0.05f), player.b2body.getWorldCenter(), true);
            if (beginGame == false) {
                beginGame = true;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player.b2body.applyLinearImpulse(new Vector2(0, -0.05f), player.b2body.getWorldCenter(), true);
            if (beginGame == false) {
                beginGame = true;
            }
        }
    }

    public void update(float dt) {
        //takes 1 step in the physics simulation(60 times per second)

        accumulator += Math.min(dt, 0.25f);
        while (accumulator >= Const.STEP_TIME) {
            accumulator -= Const.STEP_TIME;

            world.step(Const.STEP_TIME, 6, 2);
        }

        //handle user input first
        //handleInput(dt);

        hud.highscore = Hud.prefs.getInteger("highscore0:", 0);
        hud.update(dt);

        for (PlayScreen i : objectsToSpawn) {
            i.createInstance(world);
        }

        //TIMER TO SPAWN ENEMIES
        timeCount += dt;

        if ((timeCount >= 2 && enemies_count < Const.MAX_ENEMIES) || enemies_count == 0) {
            worldTimer++;

            timeCount = 0;
        }

        objectsToSpawn.clear();

        for (Body destroy : destroyer) {
            world.destroyBody(destroy);
        }

        destroyer.clear();

        player.update(dt);

        //appleSpawner.update(dt);



        hud.update(dt);

        //update our gamecam with correct coordinates after changes
        gameCam.update();
        renderer.setView(gameCam);
        b2dCam.update();
    }


    public void createInstance(World world) {
        BodyDef bdef = new BodyDef();
    }


    @Override
    public void resize(int width, int height) {
        //updated our game viewport
        gamePort.update(width, height);
        tile_viewport.update(width, height);
        game.shapeRenderer.setProjectionMatrix(gameCam.combined);

        //Stage fits for all resizes
        stage1.getViewport().update(width, height);
        stage1.getCamera().position.set(Const.V_WIDTH, Const.V_HEIGHT, 0);
        hud.getStage1().getViewport().update(width, height);
        hud.getStage1().getCamera().position.set(Const.V_WIDTH, Const.V_HEIGHT, 0);
        hud.getStage2().getViewport().update(width, height);
        hud.getStage2().getCamera().position.set(Const.V_WIDTH, Const.V_HEIGHT, 0);
        options.stage.getViewport().update(width, height);
        options.stage.getCamera().position.set(Const.V_WIDTH, Const.V_HEIGHT, 0);
        tutorial.stage.getViewport().update(width, height);
        tutorial.stage.getCamera().position.set(Const.V_WIDTH, Const.V_HEIGHT, 0);
    }

    @Override
    public void render(float delta) {

        //Time elapse for tutorial
        tutorial.elapsed += Gdx.graphics.getDeltaTime();
        //handle user input first
        handleInput(delta);

        //separate our update logic from render
        if (!Hud.pauseGame && !gameOver()) {

            if (tutorial.enableTutorial == true) {
                hud.stage2.addActor(hud.filterScreen);
                Gdx.input.setInputProcessor(tutorial.inputMultiPlex);
            } else {
                player.update(delta);
                Gdx.input.setInputProcessor(inputMultiPlex);

                hud.filterScreen.remove();

                if (!beginGame) {
                    hud.update(delta);
                    //hud.stage2.addActor(hud.beginGameLabel);
                } else {
                    hud.beginGameLabel.remove();
                    update(delta);
                }
            }
        } else {

            if (!options.enableSetting) {
                Gdx.input.setInputProcessor(stage2);
            } else if (options.enableSetting) {
                Gdx.input.setInputProcessor(options.getStage());
            }

        }

        //Clear the game screen with Black
        Gdx.gl.glClearColor(0.06f, 0.1f, 0.06f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.setView(tile_cam);
        //render map
        renderer.render();

        //renderer our Box2DDebugLines
        if (Const.DEBUG_MODE) {
            b2dr.render(world, b2dCam.combined);
        }

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.enableBlending();
        game.batch.begin();

        player.draw(game.batch);

        //appleSpawner.draw(game.batch);

        game.batch.end();

        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Rectangle rect : rectangles) {
            game.shapeRenderer.rect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
        }
        game.shapeRenderer.end();

        game.batch.begin();

        // BOX2D DEBUG
        /*b2dCam.position.set(gameCam.position.x / Const.PPM, gameCam.position.y / Const.PPM, 0);
        b2dCam.update();
        b2dr.render(world, b2dCam.combined);*/

        game.batch.end();

        //Set our batch to now draw what the Hud camera sees.
        game.batch.setProjectionMatrix(hud.stage1.getCamera().combined);

        hud.getStage1().act(delta);
        hud.getStage1().draw();
        hud.getStage2().act(delta);
        hud.getStage2().draw();
        options.stage.act(delta);
        options.stage.draw();
        tutorial.stage.act(delta);
        tutorial.stage.draw();

        tutorial.update();

        //HANDLES TUTORIAL
        game.batch.begin();


        if (tutorial.enableTutorial) {
            if (tutorial.animation != null) {
                game.batch.draw(tutorial.animation.getKeyFrame(tutorial.elapsed), (Const.V_WIDTH / 1.42f), (Const.V_HEIGHT / 1.08f), Const.V_WIDTH / 1.64736165f, Const.V_HEIGHT / 4.7761194f);
            } else {
                tutorial.loading_circle.setPosition((Const.V_WIDTH) / 1.12f, (Const.V_HEIGHT) / 1.05f);
                tutorial.loading_circle.draw(game.batch);
            }
        }


        game.batch.end();
    }

    public boolean gameOver() {
        return gameOver;
    }


    @Override
    public boolean keyDown(int keycode) {
        return false;
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
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public void create() {
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);
    }


    @Override
    public void render() {

    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        //dispose of all our opened resources
        map.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
        //options.dispose();
        stage1.dispose();
        stage2.dispose();
        renderer.dispose();
    }

    public Hud getHud() {
        return hud;
    }

//------------------------------------------------- GESTURE LISTENERS --------------//

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        if (beginGame == false) {
            beginGame = true;
        }


        return true;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }

    public Player getPlayer() {
        return player;
    }



}
