package com.game.student;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.game.student.Screens.SelectScreen;
import com.game.student.Tools.AndroidController;
import com.game.student.Tools.GifDecoder;
import com.game.student.Tools.GifLoader;

public class Simulator extends Game {
	public SpriteBatch batch;
	public ShapeRenderer shapeRenderer;

	/* WARNING Using AssetManager in a static way can cause issues, especially on Android.
    Instead you may want to pass around Assetmanager to those the classes that need it.
    We will use it in the static context to save time for now. */
	public static AssetManager manager;
	public static AndroidController androidController;

	public static TextButton.TextButtonStyle textButtonStyle, textButtonStyle2, playButtonStyle;
	public static TextButton.TextButtonStyle imageButtonStyle;
	public static Skin buttonSkin;

	public Simulator(AndroidController androidController) {
		Simulator.androidController = androidController;
	}

	public Simulator() {
	}

	@Override
	public void create() {
		Gdx.input.setCatchBackKey(true);

		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		manager = new AssetManager();
		manager.setLoader(GifDecoder.GIFAnimation.class, new GifLoader(new InternalFileHandleResolver()));

		manager.load("audio/music/village.mp3", Music.class);
		manager.load("audio/music/IndianRain.mp3", Music.class);
		manager.load("audio/music/button.wav", Sound.class);
		manager.load("audio/music/click.wav", Sound.class);
		manager.load("audio/music/yum.wav", Sound.class);
		manager.load("audio/music/tasty.wav", Sound.class);
		manager.load("audio/music/delicious.wav", Sound.class);
		manager.load("audio/music/Munch.wav", Sound.class);
		manager.load("audio/music/FreshMeat.wav", Sound.class);
		manager.load("ui/button.json", Skin.class, new SkinLoader.SkinParameter("ui/button.atlas"));
		manager.load("CaterpillarAtlas.atlas", TextureAtlas.class);
		manager.load("backgrounds/bg_menu.png", Texture.class);
		loadFont();

		manager.finishLoading();
		manager.load("gifs/fling.gif", GifDecoder.GIFAnimation.class);
		manager.load("gifs/tutorial_item.gif", GifDecoder.GIFAnimation.class);
		manager.load("gifs/tutorial_enemytypes.gif", GifDecoder.GIFAnimation.class);
		manager.load("gifs/tutorial_headon.gif", GifDecoder.GIFAnimation.class);
		manager.load("gifs/tutorial_tail.gif", GifDecoder.GIFAnimation.class);
		manager.load("gifs/tutorial_cuttail.gif", GifDecoder.GIFAnimation.class);
		manager.load("gifs/tutorial_head.gif", GifDecoder.GIFAnimation.class);
		manager.load("gifs/tutorial_end.gif", GifDecoder.GIFAnimation.class);

		initiateContent();

		setScreen(new SelectScreen(this));
	}

	private void initiateContent() {
		buttonSkin = manager.get("ui/button.json", Skin.class);

		textButtonStyle = new TextButton.TextButtonStyle();
		textButtonStyle.up = buttonSkin.getDrawable("button_rounded_up");
		textButtonStyle.down = buttonSkin.getDrawable("button_rounded_down");

		textButtonStyle.fontColor = new Color(Color.WHITE);
		textButtonStyle.downFontColor = new Color(Color.BLACK);
		textButtonStyle.font = manager.get("fonts/junegull.ttf", BitmapFont.class);
		textButtonStyle.pressedOffsetX = 1;
		textButtonStyle.pressedOffsetY = -1;

		textButtonStyle2 = new TextButton.TextButtonStyle();
		textButtonStyle2.fontColor = new Color(Color.WHITE);
		textButtonStyle2.downFontColor = new Color(0.7f, 0.38f, 0, 1.0f);
		textButtonStyle2.font = manager.get("fonts/junegull.ttf", BitmapFont.class);
		textButtonStyle2.pressedOffsetX = 1;
		textButtonStyle2.pressedOffsetY = -1;

		playButtonStyle = new TextButton.TextButtonStyle();
		playButtonStyle.up = buttonSkin.getDrawable("button9patch");
		playButtonStyle.down = buttonSkin.getDrawable("button9patch");

		playButtonStyle.fontColor = new Color(Color.WHITE);
		playButtonStyle.downFontColor = new Color(Color.BLACK);
		playButtonStyle.font = manager.get("fonts/junegull.ttf", BitmapFont.class);
		playButtonStyle.pressedOffsetX = 1;
		playButtonStyle.pressedOffsetY = -1;


		imageButtonStyle = new TextButton.TextButtonStyle();
		imageButtonStyle.up = buttonSkin.getDrawable("button_rounded_up");
		imageButtonStyle.down = buttonSkin.getDrawable("button_rounded_down");

		imageButtonStyle.fontColor = new Color(Color.WHITE);
		imageButtonStyle.downFontColor = new Color(Color.BLACK);
		imageButtonStyle.pressedOffsetX = 1;
		imageButtonStyle.pressedOffsetY = -1;
	}

	private void loadFont() {
		FileHandleResolver resolver = new InternalFileHandleResolver();
		manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

		FreetypeFontLoader.FreeTypeFontLoaderParameter font = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
		font.fontFileName = "fonts/junegull.ttf";
		font.fontParameters.size = 100;
		font.fontParameters.color = Color.WHITE;
		font.fontParameters.minFilter = Texture.TextureFilter.Linear;
		font.fontParameters.magFilter = Texture.TextureFilter.Linear;
		font.fontParameters.borderColor = Color.BLACK;
		font.fontParameters.borderWidth = 5f;
		font.fontParameters.shadowColor = Color.BLACK;
		font.fontParameters.shadowOffsetY = 5;
		font.fontParameters.shadowOffsetX = 2;

		manager.load("fonts/junegull.ttf", BitmapFont.class, font);
	}


	@Override
	public void dispose() {
		super.dispose();
		manager.dispose();
		batch.dispose();
	}

	@Override
	public void render() {
		manager.update(17);
		super.render();
	}
}
