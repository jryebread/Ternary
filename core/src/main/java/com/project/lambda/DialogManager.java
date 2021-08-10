package com.project.lambda;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * This class will manage the Dialog Loader (loading dialogue from the file) and
 * handle player input during a dialogue event.
 */
public class DialogManager {
    public static boolean inConvo = false;

    private DialogBox dialogBox;
    private NPC npc;

    private BaseActor continueKey;
    private Table buttonTable;
    private Table uiTable;
    private Scene scene;
    private String currentState = "";

    public void initialize(Stage uiStage, Stage mainStage, Table ui) {

        this.uiTable = ui;
        continueKey = new BaseActor(0,0,uiStage);
        continueKey.loadTexture("key-C.png");
        continueKey.setSize(32,32);
        continueKey.setVisible(false);

        dialogBox = new DialogBox(0,0, uiStage);
        dialogBox.setBackgroundColor( Color.TAN );
        dialogBox.setFontColor( Color.BLACK );
        dialogBox.setDialogSize(800, 200);
        dialogBox.setFontScale(0.80f);
        dialogBox.alignTopRight();

        dialogBox.setVisible(false);

        dialogBox.addActor(continueKey);
        continueKey.setPosition(dialogBox.getWidth() - continueKey.getWidth(), 0);

        buttonTable = new Table();
        buttonTable.setVisible(false);

        uiTable.add().expandY();
        uiTable.row();
        uiTable.add(buttonTable);
        uiTable.row();
        //uiTable.add(portraitBox);
        //thumbnail needs to be part of the reddit
        uiTable.add(dialogBox).expandX();
        uiTable.setDebug(true);

        scene = new Scene();
        mainStage.addActor(scene);
        dialogBox.setText("");

    }

    public void initiateDialogue(NPC npc) {
        this.npc = npc;
        Thumbnail thumbnail = npc.getThumbnail();
        dialogBox.addActor(thumbnail);
        thumbnail.setPosition((dialogBox.getWidth() - thumbnail.getWidth()) + 20, 10);
        thumbnail.setVisible(true);
        dialogBox.setVisible(true);
        inConvo = true;

        dialogBox.setText("");
        continueDialogue("Start");
    }

    public void continueDialogue(String nodeTitle) {
        if (currentState.isEmpty()) {
            scene.clearSegments();
        } else {
            currentState = ""; //reset currentState to nil for jump! state
        }
        DialogueNode dialogueNode = npc.getDialogueLoader().getDialogueNode(nodeTitle);
        if (dialogueNode.getTags().equals("X")) {
            addTextSequence(dialogueNode.getNpcText());
            buttonTable.clearChildren();
            buttonTable.setVisible(false);
            return;
        } else if (dialogueNode.getTags().equals("!")) {
            addTextSequence(dialogueNode.getNpcText());
            String jumpState = dialogueNode.getResponseStateMap().get("jump");
            currentState = jumpState;
            continueDialogue(jumpState);
            return;
        }
        //otherwise its a ? state:
        String npcText = dialogueNode.getNpcText();
        addTextSequence(npcText);

        //show button table
        scene.addSegment( new SceneSegment( buttonTable, Actions.show() ));
        //set up player responses/buttons
        buttonTable.clearChildren();
        for (String response : dialogueNode.getResponseStateMap().keySet()) {
            TextButton button = new TextButton(response,
                    BaseGame.textButtonStyle);
            buttonTable.add(button);
            buttonTable.row();
            button.addListener(
                    (Event e) ->
                    {
                        if ( !(e instanceof InputEvent) ||
                                !((InputEvent)e).getType().equals(InputEvent.Type.touchDown) )
                            return false;

                        buttonTable.setVisible(false);
                        String stateForResponse = dialogueNode.getResponseStateMap().get(response);
                        continueDialogue(stateForResponse);

                        scene.start();
                        return false;
                    }
            );
        }
        //TODO: possibly duplicated actor actions added since we have two scene.starts
        scene.start();
    }

    public void addTextSequence(String s) {
        scene.addSegment( new SceneSegment( dialogBox, SceneActions.typewriter(s) ));
        scene.addSegment( new SceneSegment( continueKey, Actions.show() ));
        scene.addSegment( new SceneSegment( npc, SceneActions.pause() ));
        scene.addSegment( new SceneSegment( continueKey, Actions.hide() ));
    }

    public void handlePlayerInput(int keycode) {
        if (keycode == Input.Keys.C && DialogManager.inConvo) {
            loadNextSegment();
        } else if (DialogManager.inConvo && scene.isSceneFinished()) {
            DialogManager.inConvo = false;
            this.npc.unsetThumbnailVisible();
            dialogBox.setText("");
            dialogBox.setVisible(false);
            npc.thumbnail.setVisible(false);
            scene.clearSegments();
        } else if (inConvo) {
            //continueDialogue();
        }
    }

    public void loadNextSegment() {
        scene.loadNextSegment();
    }
}
