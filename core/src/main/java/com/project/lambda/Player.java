package com.project.lambda;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.List;

public class Player extends BaseActor{

    public enum State {
        STATE_STANDING,
        STATE_JUMPING,
        STATE_DUCKING,
        STATE_DIVING
    }

    private float walkAcceleration;
    private float walkDeceleration;
    private float maxHorizontalSpeed;
    private float gravity;
    private float maxVerticalSpeed;
    private float dashTime = 0f;
    private Animation roboRun;
    private Animation dashAnimation;
    private Animation dashEffect;
    private Animation idle;
    private Animation jump;
    private Animation land;
    private Animation landEffect;
    private float jumpSpeed;
    private BaseActor belowSensor;
    private BaseActor landing;
    private BaseActor dashing;
    private float landFrame = 0;
    private List<Solid> solids = new ArrayList<>();
    /**
     * Set initial position of actor and add to stage.
     *
     * @param x
     * @param y
     * @param s
     */
    public Player(float x, float y, Stage s, List<Solid> solids) {
        super(x, y, s);
        roboRun = loadAnimationFromSheet("Player/run.png", 4, 1, 0.1f, true);
        jump = loadAnimationFromSheet( "Player/jump-up.png", 5, 1, 0.2f, false);
        idle = loadTexture("Player/idle.png");
        land = loadAnimationFromSheet("Player/land.png", 2, 1, 0.1f, false);
        dashEffect = loadAnimationFromFiles(new String[] {"Player/de1.png", "Player/de2.png",
                "Player/de3.png", "Player/de4.png"}, 0.1f, false);
        landEffect = loadAnimationFromFiles(new String[]{"Player/land_effect1.png", "Player/land_effect2.png"},
                0.1f, false);
        dashAnimation = loadAnimationFromSheet("Player/dash.png", 3, 1, 0.5f, false);
        jumpSpeed = 400;

        landing = new BaseActor(0, 0, s);
        landing.setAnimation(landEffect);
        landing.setScale(0.9f, 0.7f);
        landing.setVisible(false);

        dashing = new BaseActor(0, 0, s);
        dashing.setAnimation(dashEffect);
        dashing.setVisible(false);

        belowSensor = new BaseActor(0,0, s);
        belowSensor.loadTexture("Player/white.png");
        belowSensor.setSize( this.getWidth() - 8, 8 );
        belowSensor.setBoundaryRectangle();
        belowSensor.setVisible(true);
        this.solids = solids;
        // set after animation established
        setBoundaryPolygon(8);
        maxHorizontalSpeed = 300;
        walkAcceleration= 1800;
        walkDeceleration= 2000;
        gravity = 700;
        maxVerticalSpeed = 1000;
        setAcceleration(250);
        setMaxSpeed(400);
        setDeceleration(1000);

    }

    public void act(float dt)
    {
        super.act(dt);
        landFrame += dt;
        dashTime += dt;


        if ( this.isOnSolid(solids) && dashTime >= 0) {
            if(belowSensor.getColor().equals(Color.RED) ) {
                elapsedTime = 0; //reset the landing animation
                belowSensor.setColor( Color.GREEN );
                setAnimation(land);
                landing.setAnimation(landEffect);
                landing.elapsedTime = 0;
                landing.setVisible(true);
                landFrame = 0;
            }

            if(isAnimationFinished() || landFrame > 0.3) {
                landing.setVisible(false);
                belowSensor.setColor( Color.GREEN );
                if (velocityVec.x == 0)
                    setAnimation(idle);
                else
                    setAnimation(roboRun);
            }
        } else if (dashTime >= 0) { //when we dash we'll set negative to allow dashAnim to play
            belowSensor.setColor( Color.RED );
            setAnimation(jump);
        } else if (dashTime < 0) {
            velocityVec.add(90000000 * getScaleX() , 0);
        }
        //face correct dir
        if ( velocityVec.x > 0 )
            setScaleX(1);
        if ( velocityVec.x < 0 )
            setScaleX(-1);

        if (Gdx.input.isKeyPressed(Keys.LEFT)) accelerationVec.add( -walkAcceleration, 0 );
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) accelerationVec.add( walkAcceleration, 0 );
        accelerationVec.add(0, -gravity);
        velocityVec.add( accelerationVec.x * dt, accelerationVec.y * dt );

        if ( !Gdx.input.isKeyPressed(Keys.RIGHT) && !Gdx.input.isKeyPressed(Keys.LEFT) ) {
            float decelerationAmount = walkDeceleration * dt;
            float walkDirection;
            if ( velocityVec.x > 0 )
                walkDirection = 1;
            else
                walkDirection = -1;
            float walkSpeed = Math.abs( velocityVec.x );
            walkSpeed -= decelerationAmount;
            if (walkSpeed < 0)
                walkSpeed = 0;
            velocityVec.x = walkSpeed * walkDirection;
        }

        velocityVec.x = MathUtils.clamp( velocityVec.x, -maxHorizontalSpeed, maxHorizontalSpeed );
        velocityVec.y = MathUtils.clamp( velocityVec.y, -maxVerticalSpeed, maxVerticalSpeed );
        moveBy( velocityVec.x * dt, velocityVec.y * dt );
        accelerationVec.set(0,0);
        belowSensor.setPosition( getX() + 4, getY() - 8 );
        landing.setPosition(getX() - 30 , getY() - 4 );
        alignCamera();
        boundToWorld();
    }


    public void dash() {
        //elapsedTime = 0; //RESET JUMP ANIMATION TO 0TH FRAME
        dashTime = -0.3f;
        setAnimation(dashAnimation);
        dashing.setVisible(true);
        dashing.setAnimation(dashEffect);
        //this.moveBy(getScaleX() * 50, 0);
    }

    public void jump()
    {
        elapsedTime = 0; //RESET JUMP ANIMATION TO 0TH FRAME
        landing.setVisible(false);
        velocityVec.y = jumpSpeed;
    }

    public boolean isOnSolid(List<Solid> solids)
    {
        for (BaseActor actor : solids) {
            Solid solid = (Solid)actor;
            if ( belowOverlaps(solid))
                return true;
        }
        return false;
    }

    public boolean belowOverlaps(BaseActor actor)
    {
        return belowSensor.overlaps(actor);
    }
}
