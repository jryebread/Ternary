package com.project.lambda;

import com.badlogic.gdx.scenes.scene2d.Action;

public class SetTextAction extends Action
{
    protected String textToDisplay;
    public SetTextAction(String t)
    {
        textToDisplay = t;
    }
    public boolean act(float dt)
    {
        DialogBox db = (DialogBox)target;
        db.setText( textToDisplay );
        return true;
    }
}