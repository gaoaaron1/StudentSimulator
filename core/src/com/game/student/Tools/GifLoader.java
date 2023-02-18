package com.game.student.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;

public class GifLoader extends AsynchronousAssetLoader<GifDecoder.GIFAnimation, GifLoader.Parameter> {
    public GifLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    GifDecoder.GIFAnimation gif;

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, Parameter parameter) {
        gif = null;
        System.out.println("Loading " + file.file().getPath());
        gif = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal(file.file().getPath()).read());
    }

    @Override
    public GifDecoder.GIFAnimation loadSync(AssetManager manager, String fileName, FileHandle file, Parameter parameter) {
        GifDecoder.GIFAnimation gif = this.gif;
        this.gif = null;
        return gif;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, Parameter parameter) {
        return null;
    }

    static public class Parameter extends AssetLoaderParameters<GifDecoder.GIFAnimation> {
    }
}