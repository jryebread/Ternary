package com.project.lambda;

import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;

public class Scene extends Actor
{
    private ArrayList<SceneSegment> segmentList;
    private int index;

    public Scene() {
        super();
        segmentList = new ArrayList<SceneSegment>();
        index = -1;
    }

    public void addSegment(SceneSegment segment)
    {
        segmentList.add(segment);
    }

    public void clearSegments() {
        segmentList.clear();
        //index = 0;
    }

    public void start() {
        index = 0;
        segmentList.get(index).start();
    }

    public void act(float dt) {
        if (isSegmentFinished() && !isLastSegment())
            loadNextSegment();
    }

    public boolean isSegmentFinished() {
        if (segmentList.size() != 0)
            return segmentList.get(index).isFinished();
        else
            return true;
    }

    public boolean isLastSegment() {
        return (index >= segmentList.size() - 1);
    }

    public void loadNextSegment() {
        if ( isLastSegment() )
            return;

        segmentList.get(index).finish();
        index++;
        segmentList.get(index).start();
    }

    public boolean isSceneFinished() {
        return ( index < segmentList.size() && isLastSegment() && isSegmentFinished() );
    }
}
