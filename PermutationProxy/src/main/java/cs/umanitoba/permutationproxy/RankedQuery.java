/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs.umanitoba.permutationproxy;

import Database.QueryDB;
import Database.Sequence;
import Database.SequencePermuted;
import Database.SnpRatio;
import Utilities.Paillier;
import Utilities.Utils;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author azizmma
 */
@ServerEndpoint("/endpoint_ranked")
public class RankedQuery {

    static int fetchRecords = 1000;
    static int totalLimit = 40000;

    @OnMessage
    public void onMessage(String message, Session session) {
        QueryDB queryDB = new QueryDB();
        Paillier paillier = new Paillier(true);
//        System.out.println("Message " + message);
        JsonObject jsonObject = Json.createReader(new StringReader(message)).readObject();
        if (jsonObject.getString("type").contains("queryRanked")) {
            JsonObject msg = jsonObject.getJsonObject("msg");
            JsonArray jsonArray = msg.getJsonArray("query");
            int limit = msg.getInt("limit");
            int offset = msg.getInt("offset");
            String hasCancer = msg.getString("hasCancer");
            System.out.println("offset-limit " + offset + "-" + limit);
            Map<Integer, String> queryMap = new TreeMap<>();
            for (JsonValue jsonValue : jsonArray) {
                JsonObject jsonObjectTmp = Json.createReader(new StringReader(jsonValue.toString())).readObject();
                queryMap.put(jsonObjectTmp.getInt("p"), jsonObjectTmp.getString("s"));
                //System.out.println(jsonValue.toString());
                //System.out.println(.getInt("p"));
            }
            Map<Integer, String> queryMapPerm = queryMap;
            List<SnpRatio> allRatios = queryDB.getAllSnipRatios(hasCancer);
            if (jsonObject.getString("type").equals("queryRankedPerm")) {
                for (Map.Entry<Integer, String> entrySet : queryMap.entrySet()) {
//                    SnpRatio ratio = queryDB.getAllSnipRatios(hasCancer, entrySet.getKey() + "").get(0);
                    for (SnpRatio allRatio : allRatios) {
                        if (allRatio.getValidPermutation() != null && queryMap.containsKey(allRatio.getPosition())) {
                            String tmp = queryMapPerm.get(entrySet.getKey());
                            queryMapPerm.put(entrySet.getKey(), queryMap.get(allRatio.getPosition()));
                            queryMapPerm.put(allRatio.getPosition(), tmp);
                        }
                    }
                }
            }
            int i = 0;
            BigInteger res = paillier.Encryption(new BigInteger("0"));
            Map<Integer, List<String>> result = new TreeMap<>();
            String query = "";
            if (jsonObject.getString("type").contains("Perm")) {
                for (Map.Entry<Integer, String> entrySet : queryMapPerm.entrySet()) {
                    query += entrySet.getValue();
                }
                while (true) {
                    List<SequencePermuted> sequences = queryDB.getPermutatedSequence(fetchRecords, i);
                    if (sequences.isEmpty()) {
                        break;
                    }
                    for (SequencePermuted sequence : sequences) {
                        if (paillier.Decryption(new BigInteger(sequence.getTag())).equals(new BigInteger("1"))) {
                            int dist = Utils.getDistance(sequence.getPermutatedSequence(), query);
                            if (result.get(dist) == null) {
                                List<String> tmp = new ArrayList<>();
                                tmp.add(sequence.getPermutatedSequence().substring(0, 3) + "..");
                                result.put(dist, tmp);

                            } else {
                                result.get(dist).add(sequence.getPermutatedSequence().substring(0, 3) + "..");
                            }
//                            resCount++;
                        }
                    }
                    System.out.println("fetching " + i);
                    i += fetchRecords;
                    if (i > totalLimit) {
                        break;
                    }
                    try {
                        String output = Json.createObjectBuilder()
                                .add("type", "faceack")
                                .build().toString();
                        session.getBasicRemote().sendText(output);
                    } catch (IOException ex) {
                        Logger.getLogger(CountQueryApi.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            } else {  
                for (Map.Entry<Integer, String> entrySet : queryMap.entrySet()) {
                    query += entrySet.getValue();
                }
                while (true) {
                    List<Sequence> sequences = queryDB.getRegularSequence(fetchRecords, i);
                    if (sequences.size() == 0) {
                        break;
                    }
                    for (Sequence sequence : sequences) {
                        int dist = Utils.getDistance(sequence.getSequence(), query);
                        if (result.get(dist) == null) {
                            List<String> tmp = new ArrayList<>();
                            tmp.add(sequence.getSequence().substring(0, 3) + "..");
                            result.put(dist, tmp);
                        } else {
                            result.get(dist).add(sequence.getSequence().substring(0, 3) + "..");
                        }
                    }
                    System.out.println("fetching " + i);
                    i += fetchRecords;
                    if (i > (totalLimit / 2)) {
                        break;
                    }
                    try {
                        String output = Json.createObjectBuilder()
                                .add("type", "faceack")
                                .build().toString();
                        session.getBasicRemote().sendText(output);
                    } catch (IOException ex) {
                        Logger.getLogger(RankedQuery.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
            JsonArrayBuilder array = Json.createArrayBuilder();
            JsonObjectBuilder param = Json.createObjectBuilder();

            int resCount = 0;

            for (Map.Entry<Integer, List<String>> entrySet : result.entrySet()) {

                for (int ind = 0; ind < entrySet.getValue().size(); ind++) {
                    if (resCount >= limit + offset) {
                        break;
                    }
                    resCount++;
                    if (resCount < offset) {
                        continue;
                    }
                    param.add(entrySet.getKey() + "-" + ind, entrySet.getValue().get(ind));
                }
                array.add(param);
            }
//            System.out.println(result.size());
            try {
                String output = Json.createObjectBuilder()
                        .add("type", "ack")
                        .add("msg", resCount)
                        .add("result", array.build().toString())
                        .build().toString();
                System.out.println("Outputted for " + offset + "-" + limit);
                System.out.println(output);
                session.getBasicRemote().sendText(output);
            } catch (IOException ex) {
                Logger.getLogger(CountQueryApi.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    @OnOpen
    public void onOpen(Session peer) {
//        peers.add(peer);

    }

    @OnClose
    public void onClose(Session peer) {

    }
}
