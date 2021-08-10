package com.project.lambda;

import java.util.Map;

public class DialogueNode {
    private String title;

    private String npcText;

    private Map<String, String> responseStateMap;

    private String tags;

    public String getNpcText() {
        return npcText;
    }

    public void setNpcText(String npcText) {
        this.npcText = npcText;
    }

    public Map<String, String> getResponseStateMap() {
        return responseStateMap;
    }

    public void setResponseStateMap(Map<String, String> responseStateMap) {
        this.responseStateMap = responseStateMap;
    }

    public DialogueNode() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
