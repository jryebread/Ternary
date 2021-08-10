package com.project.lambda;



import org.json.simple.JSONArray;
import org.json.simple.parser.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * loads dialogue from json file directory
 */
public class DialogueLoader {

    File filePath;
    String npcName;
    int dialogueIndex = 0;
    Object obj;
    Map<String, DialogueNode> dialogueNodeMap = new HashMap<>();

    DialogueLoader(String npcName) {
        filePath = new File("/Users/ribackj/Documents/ProjectLambda/core/src/main/java/com/project/lambda/dialogue/"+ npcName + ".json");
        this.npcName = npcName;
        obj = null;
        try {
            obj = new JSONParser().parse(new FileReader(filePath));
        } catch (IOException | org.json.simple.parser.ParseException ioException) {
            System.out.println("ERROR PARSING AND/OR I/O!" + ioException);
        }
        // typecasting obj to JSONObject
        if (obj == null)
            return;
        JSONArray ja = (JSONArray) obj;
        Iterator iter2 = ja.iterator();

        //load DialogueNode nodeMap
        while (iter2.hasNext())
        {
            DialogueNode dialogueNode = new DialogueNode();
            String nodeTitle = "";

            Map responseStateMap = null;
            Set entrySet = ((Map) iter2.next()).entrySet();
            //find the tags to set tags for this node first since parsing relies on tag info
            for (Object o1 : entrySet) {
                Map.Entry pair1 = (Map.Entry) o1;
                if (pair1.getKey().equals("tags")) {
                    dialogueNode.setTags(pair1.getValue().toString());
                }
            }
            //then parse the rest of the dialogueNode info
            for (Object o : entrySet) {
                Map.Entry pair = (Map.Entry) o;
                if (pair.getKey().equals("title")) {
                    dialogueNode.setTitle(pair.getValue().toString());
                    nodeTitle = pair.getValue().toString();
                } else if (pair.getKey().equals("body")) {
                    String npcText;
                    if (dialogueNode.getTags().equals("X")) {
                        npcText = pair.getValue().toString();
                    } else if (dialogueNode.getTags().equals("!")){
                        npcText = parseNpcText(pair.getValue().toString());
                        responseStateMap = parseStateFromJumpNode(pair.getValue().toString());
                    } else {
                        npcText = parseNpcText(pair.getValue().toString());
                        responseStateMap = parseResponseStateMappings(new HashMap<String, String>(),
                                pair.getValue().toString());
                    }
                    dialogueNode.setNpcText(npcText);


                }
            }
            if (responseStateMap != null)
                dialogueNode.setResponseStateMap(responseStateMap);
            dialogueNodeMap.put(nodeTitle, dialogueNode);
        }
    }

    private Map parseStateFromJumpNode(String body) {
        String state = body.substring(body.indexOf("[[") + 2, body.indexOf("]]"));
        Map<String, String> map = new HashMap<String, String>();
        map.put("jump", state);
        return map;
    }

    private Map<String, String> parseResponseStateMappings(HashMap<String, String> map, String body) {
        body = body.substring(body.indexOf("[["));
        Scanner scanner = new Scanner(body);
        String pcResponse = "";
        String nextState = "";
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println("BODYDEBUG: " + line);
            // process the line
            pcResponse = line.substring(line.indexOf("[[") + 2, line.indexOf("|"));
            nextState = line.substring(line.indexOf("|") + 1, line.indexOf("]]"));
            map.put(pcResponse, nextState);

        }
        scanner.close();


        return map;
    }

    private String parseNpcText(String body) {
        return body.substring(0, body.indexOf("[["));
    }


    public DialogueNode getDialogueNode(String nodeName) {
        if (dialogueIndex == 0) {
            //start node
            dialogueIndex++;
            nodeName = "Start";
        }
        DialogueNode dialogueNode = dialogueNodeMap.get(nodeName);

        return dialogueNode;
    }
}
