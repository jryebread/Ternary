package com.project.lambda;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Thumbnail extends BaseActor
{

    public void setTextureAsset(String textureAsset) {
        this.textureAsset = textureAsset;
        animation = loadTexture(textureAsset);
    }

    public String textureAsset;
    private Animation animation;
    
    public Thumbnail(float x, float y, Stage s)
    {
       super(x,y,s);
       setSize(700, 700);
    }
}