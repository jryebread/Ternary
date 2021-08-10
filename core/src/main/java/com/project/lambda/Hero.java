package com.project.lambda;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Hero extends BaseActor
{
    Animation north;
    Animation south;
    Animation east;
    Animation west;

    Animation roboRun;
    
    float facingAngle;

    public Hero(float x, float y, Stage s)
    {
        super(x,y,s);
        roboRun = loadAnimationFromSheet("/Users/ribackj/Documents/ProjectLambda/assets/droid/run.png", 6, 1, 0.1f, true);

//        String fileName = "assets/hero.png";
//        int rows = 4;
//        int cols = 4;
//        Texture texture = new Texture(Gdx.files.internal(fileName), true);


        // set after animation established
        setBoundaryPolygon(8);

        setAcceleration(250);
        setMaxSpeed(300);
        setDeceleration(1000);

    }

    public void act(float dt)
    {
        super.act(dt);
        
        // pause animation when character not moving
        if ( getSpeed() == 0 )
            setAnimationPaused(true);
        else
        {
            setAnimationPaused(false);

//            // set direction animation
//            float angle = getMotionAngle();
//            if (angle >= 45 && angle <= 135)
//            {
//                facingAngle = 90;
//                setAnimation(north);
//            }
//            else if (angle > 135 && angle < 225)
//            {
//                facingAngle = 180;
//                setAnimation(west);
//            }
//            else if (angle >= 225 && angle <= 315)
//            {
//                facingAngle = 270;
//                setAnimation(south);
//            }
//            else
//            {
//                facingAngle = 0;
//                setAnimation(east);
//            }
        }

        alignCamera();
        boundToWorld();
        applyPhysics(dt);
    }

    public float getFacingAngle()
    {
        return facingAngle;
    }

}