package com.project.lambda;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.ArrayList;
import java.util.List;

public class LevelScreen extends BaseScreen
{

    int health;
    int coins;
    int arrows;
    boolean gameOver;

    DialogManager dialogManager;
    DialogBox dialogBox;

    private Player hero;
    List<Solid> solids;
    List<NPC> npcs;
    public void initialize() 
    {
        solids = new ArrayList<>();
        npcs = new ArrayList<>();
        dialogManager = new DialogManager();
        TilemapActor tma = new TilemapActor("/Users/ribackj/Documents/ProjectLambda/assets/map.tmx", mainStage);
        for (MapObject obj : tma.getRectangleList("Solid") )
        {
            MapProperties props = obj.getProperties();
            solids.add(new Solid( (float)props.get("x"), (float)props.get("y"),
                (float)props.get("width"), (float)props.get("height"), 
                mainStage ));
        }

        MapObject startPoint = tma.getRectangleList("Start").get(0);
        MapProperties startProps = startPoint.getProperties();
        hero = new Player( (float)startProps.get("x"), (float)startProps.get("y"), mainStage, solids);


        health = 3;
        coins = 5;
        arrows = 3;
        gameOver = false;



        dialogManager.initialize(uiStage, mainStage, uiTable);


        NPC testNPC = new NPC(hero.getX()+50, hero.getY()-50, mainStage, uiStage);
        npcs.add(testNPC);
        testNPC.setID("Scavenger");

        hero.toFront();
    }

    public void update(float dt)
    {
        if ( gameOver )
            return;
        int HERO_SPEED = 150;

        for (BaseActor actor : solids) {
            Solid solid = (Solid)actor;
            if ( hero.overlaps(solid) ) {
                Vector2 offset = hero.preventOverlap(solid);
                if (offset != null) {
                    // collided in X direction
                    if ( Math.abs(offset.x) > Math.abs(offset.y) )
                        hero.velocityVec.x = 0;
                    else  // collided in Y direction
                        hero.velocityVec.y = 0;
                } }
        }

        if ( !DialogManager.inConvo)
        {
//            // hero movement controls
//            if (Gdx.input.isKeyPressed(Keys.LEFT)) {
//                hero.setX((hero.getX() - (dt * HERO_SPEED)));
//                hero.setScaleX(-1);
//                hero.setAnimationPaused(false);
//
//            }
//            else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
//                hero.setX((hero.getX() + (dt * HERO_SPEED)));
//                hero.setScaleX(1);
//                hero.setAnimationPaused(false);
//            }
//            else if (Gdx.input.isKeyPressed(Keys.UP)) {
//                hero.setY((hero.getY() + (dt * HERO_SPEED)));
//                hero.setAnimationPaused(false);
//            }
//            else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
//                hero.setY((hero.getY() - (dt * HERO_SPEED)));
//                hero.setAnimationPaused(false);
//            } else {
//                hero.setAnimationPaused(true);
//            }
        }

        for (BaseActor solid : solids)
        {
            hero.preventOverlap(solid);
        }

        for ( BaseActor npcActor : npcs )
        {
            NPC npc = (NPC)npcActor;

            boolean nearby = hero.isWithinDistance(4, npc);

            if ( nearby && !npc.isViewing() )
            {
                //dialogBox.setVisible( true );
                npc.dialogueSprite.setVisible(true);
            } else if ( !nearby && !npc.isViewing()) {
                npc.dialogueSprite.setVisible(false);
            }

            if (!DialogManager.inConvo && nearby && (Gdx.input.isKeyPressed(Keys.C))) {
                //activate dialog manager
                dialogManager.initiateDialogue(npc);
                npc.setViewing(true);
                DialogManager.inConvo = true;
                npc.dialogueSprite.setVisible(false);
            }

            if (npc.isViewing() && !nearby)
            {

                npc.setViewing( false );
                npc.dialogueSprite.setVisible(false);
            }
        }

        //if health<0
    }

    // handle discrete input
    public boolean keyDown(int keycode)
    {
        if ( gameOver )
            return false;

        dialogManager.handlePlayerInput(keycode);

        if (keycode == Keys.SPACE) {
            if ( hero.isOnSolid(solids) ) {
                hero.jump(); }
        }

        if (keycode == Keys.D) {
            hero.dash();
        }
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

}