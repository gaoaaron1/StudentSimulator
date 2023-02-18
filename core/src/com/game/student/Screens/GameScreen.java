package com.game.student.Screens;

import static com.game.student.Simulator.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.game.student.Const;
import com.game.student.Scenes.Options;
import com.game.student.Scenes.Tutorial;
import com.game.student.Simulator;

public class GameScreen implements Screen {
    /*===================================== METHODS ================================================*/
    public static Simulator game;

    private OrthographicCamera gameCam;
    private Viewport gamePort;

    private Music musics;
    private Sound sounds;

    public static InputMultiplexer multiplexer;

    //Buttons
    private Skin skin;
    public TextButton startButton;
    public TextButton.TextButtonStyle startStyle;

    //Image Buttons
    private ImageButton tutorialButton;
    private ImageButton.ImageButtonStyle tutorialStyle;


    //Stage variables
    public static Stage stage;
    Table table = new Table();

    //Background texture
    private Texture background;

    //Object
    public static Options options = new Options();
    public static Tutorial tutorial = new Tutorial();

    /*===================================== CONSTRUCTOR ================================================*/

    public GameScreen(final Simulator game) {

        this.game = game;

        // Create camera for sprites
        gameCam = new OrthographicCamera();
        gameCam.setToOrtho(false, Const.V_WIDTH, Const.V_HEIGHT);
        gamePort = new ExtendViewport(Const.V_WIDTH, Const.V_HEIGHT, gameCam);
        gameCam.position.set(gamePort.getWorldWidth(), gamePort.getWorldHeight(), 0);

        //Create stage
        stage = new Stage(new ExtendViewport(Const.V_WIDTH, Const.V_HEIGHT));
        table.setFillParent(true);

        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(options.stage);
        multiplexer.addProcessor(tutorial.inputMultiPlex);
        options.setSettingButtonVisible(true);

        Gdx.input.setInputProcessor(multiplexer);


        //------------------------ BACKGROUND AND MUSIC SETTINGS --------------//

        //Sets up our entire menu
        setBackground();

        //Set music
        musics = options.getMusic();
        sounds = options.getSound();


        //Creates all our buttons
        startStyle = new TextButton.TextButtonStyle();

        //Sets up atlas for buttons
        skin = options.getSkin();

//-------------------------- BUTTON PARAMETERS AND FUNCTIONALITY ---------------------//
        //Initialize buttons
        startButton = new TextButton("", Simulator.playButtonStyle);

        //Set button parameters
        setButtonParameters();

        //Add button functionality
        createButtonFunctionality();

    }


    /*===================================== METHODS ================================================*/

    private void createButtonFunctionality() {
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                options.playButtonClick();
                options.musics.stop();
                options.setSettingButtonVisible(false);
                dispose();
                game.setScreen(new SelectScreen(game));
                options.musics = manager.get("audio/music/IndianRain.mp3", Music.class);
                if (options.enableMusic == true) {
                    options.musics.setLooping(true);
                    options.musics.setVolume(options.slider.getValue() / 100);
                    options.musics.play();
                }
            }
        });

        tutorialButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                options.playButtonClick();
                tutorial.tutorialHandler();
            }
        });


    }

    private void setButtonParameters() {
        startButton.setText("PLAY");
        startButton.getLabel().setFontScale(1.2f);
        startButton.setWidth(Const.V_WIDTH / 1.5f);
        startButton.setHeight(Const.V_HEIGHT / 9f);
        startButton.setPosition(Const.V_WIDTH / 1.5f, Const.V_HEIGHT);
        stage.addActor(startButton);

        //Create tutorial button
        tutorialStyle = new ImageButton.ImageButtonStyle();
        tutorialStyle.up = Simulator.buttonSkin.getDrawable("tutorial_up");
        tutorialStyle.down = Simulator.buttonSkin.getDrawable("tutorial_down");
        tutorialButton = new ImageButton(tutorialStyle);
        ImageButton.ImageButtonStyle tutorialStyle = new ImageButton.ImageButtonStyle(Simulator.imageButtonStyle);
        tutorialStyle.imageUp = Simulator.buttonSkin.getDrawable("tutorial_up");
        tutorialStyle.imageDown = Simulator.buttonSkin.getDrawable("tutorial_down");
        tutorialButton.setWidth(Const.V_WIDTH / 5f);
        tutorialButton.setHeight(Const.V_WIDTH / 5f);
        tutorialButton.setPosition(Const.V_WIDTH / 1.8f, Const.V_HEIGHT / 1.8f);
        stage.addActor(tutorialButton);



        Label titleLabel1 = new Label("Hungry", new Label.LabelStyle(manager.get("fonts/junegull.ttf", BitmapFont.class), Color.ORANGE));
        titleLabel1.setWidth(Const.V_WIDTH / 12f);
        titleLabel1.setHeight(Const.V_HEIGHT / 12f);
        titleLabel1.setFontScale(2.2f);
        titleLabel1.setPosition(Const.V_WIDTH / 1.4f, Const.V_HEIGHT / 0.8f);

        Label titleLabel2 = new Label("Caterpillars", new Label.LabelStyle(manager.get("fonts/junegull.ttf", BitmapFont.class), Color.ORANGE));
        titleLabel2.setWidth(Const.V_WIDTH / 12f);
        titleLabel2.setHeight(Const.V_HEIGHT / 12f);
        titleLabel2.setFontScale(1.2f);
        titleLabel2.setPosition(Const.V_WIDTH / 1.4f, Const.V_HEIGHT / 0.85f);

        stage.addActor(titleLabel1);
        stage.addActor(titleLabel2);
    }

    private void setBackground() {
        //Background texture
        background = manager.get("backgrounds/bg_menu.png", Texture.class);
    }



    /*============================================ OVERRIDE METHODS ===================================================== */

    @Override
    public void show() {

    }


    public void update(float dt) {
        gameCam.update();
        tutorial.update();
    }

    @Override
    public void render(float delta) {
        tutorial.elapsed += Gdx.graphics.getDeltaTime();
        update(delta);

        Gdx.gl.glClearColor(0f, 0f, 0, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        game.batch.draw(background, -(Const.V_WIDTH) / 2f, -(Const.V_HEIGHT) / 2f, Const.V_WIDTH, Const.V_HEIGHT);
        game.batch.end();

        //renderer.render();
        stage.act(delta);
        stage.draw();

        options.stage.act(delta);
        options.stage.draw();

        tutorial.stage.act(delta);
        tutorial.stage.draw();

        //HANDLES TUTORIAL
        game.batch.begin();
        if (tutorial.enableTutorial) {
            if (tutorial.animation != null) {
                game.batch.draw(tutorial.animation.getKeyFrame(tutorial.elapsed), -(Const.V_WIDTH) / 3.25f, -(Const.V_HEIGHT) / 12f, Const.V_WIDTH / 1.64736165f, Const.V_HEIGHT / 4.7761194f);
            } else {
                tutorial.loading_circle.draw(game.batch);
            }
        }
        game.batch.end();

    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
        stage.getViewport().update(width, height);
        stage.getCamera().position.set(Const.V_WIDTH, Const.V_HEIGHT, 0);
        options.stage.getViewport().update(width, height);
        options.stage.getCamera().position.set(Const.V_WIDTH, Const.V_HEIGHT, 0);
        tutorial.stage.getViewport().update(width, height);
        tutorial.stage.getCamera().position.set(Const.V_WIDTH, Const.V_HEIGHT, 0);
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
        stage.dispose();
    }
}
