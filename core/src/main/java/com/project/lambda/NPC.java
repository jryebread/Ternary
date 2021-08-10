package com.project.lambda;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class NPC extends BaseActor
{
    // the text to be displayed
    private String text;

    public DialogueLoader getDialogueLoader() {
        return dialogueLoader;
    }

    private DialogueLoader dialogueLoader;


    public BaseActor dialogueSprite;


    public Thumbnail thumbnail;
    
    // used to determine if dialog box text is currently being displayed
    private boolean viewing;

    // ID used for specific graphics
    //   and identifying NPCs with dynamic messages 
    private String ID;
    private Stage uiStage;

    public NPC(float x, float y, Stage mainStage, Stage uiStage)
    {

        super(x,y,mainStage);
        this.uiStage = uiStage;
        text = " ";
        viewing = false;
        dialogueSprite = new BaseActor(x, y+50, mainStage);
        dialogueSprite.loadTexture("key-C.png");
        dialogueSprite.setSize(20, 20);
        dialogueSprite.setVisible(false);
        thumbnail = new Thumbnail(30, 0,
                uiStage);
        thumbnail.setVisible(false);

    }

    public void setText(String t)
    {  text = t;  }

    public String getText()
    {  return text;  }


    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setViewing(boolean v)
    {  viewing = v;  }

    public boolean isViewing()
    {  return viewing;  }  

    public void setID(String id)
    {  
        ID = id;  
        
        if ( ID.equals("Gatekeeper") )
            loadTexture("npc-1.png");
        else if (ID.equals("Shopkeeper"))
            loadTexture("npc-2.png");
        else if (ID.equals("Scavenger")) {
            loadAnimationFromSheet("merchant/idle.png", 1, 4, 0.2f, true);
            thumbnail.setAnimation(loadTexture("merchant/merchant_face.png"));
            dialogueLoader = new DialogueLoader(this.getID());
        }
        else // default image
            loadTexture("npc-3.png");
    }

    public String getID()
    {  return ID;  }

    public void setThumbnailVisible() {
        thumbnail.setVisible(true);
        thumbnail.setScale(0.7f, 0.5f);
    }

    public void unsetThumbnailVisible() {
        thumbnail.setVisible(false);
    }
}