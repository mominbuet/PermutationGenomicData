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
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
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
@ServerEndpoint("/endpoint_count")
public class CountQueryApi {

    static int limiting = 35000;
    static int stepsize = 1000;
    static int start = 0;

    @OnMessage
    public void onMessage(String message, Session session) {
        QueryDB queryDB = new QueryDB();
        Paillier paillier = new Paillier(true);
//        System.out.println("Message " + message);
        JsonObject jsonObject = Json.createReader(new StringReader(message)).readObject();
        if (jsonObject.getString("type").equals("ping")) {
            try {
                String output = Json.createObjectBuilder()
                        .add("type", "ping")
                        .build().toString();
//                                System.out.println("Outputted for " + queryNo + " res " + paillier.Decryption(res));
                session.getBasicRemote().sendText(output);
            } catch (IOException ex) {
                Logger.getLogger(CountQueryApi.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (jsonObject.getString("type").contains("queryCount")) {
                JsonObject msg = jsonObject.getJsonObject("msg");
                JsonArray jsonArray = msg.getJsonArray("query");
                int queryNo = msg.getInt("queryNo");
                String hasCancer = msg.getString("hasCancer");
                System.out.println("query no " + queryNo);
                Map<Integer, String> queryMap = new TreeMap<>();
                for (JsonValue jsonValue : jsonArray) {
                    JsonObject jsonObjectTmp = Json.createReader(new StringReader(jsonValue.toString())).readObject();
                    queryMap.put(jsonObjectTmp.getInt("p"), jsonObjectTmp.getString("s"));
                    //System.out.println(jsonValue.toString());
                    //System.out.println(.getInt("p"));
                }
                Map<Integer, String> queryMapPerm = queryMap;
                List<SnpRatio> allRatios = queryDB.getAllSnipRatios(hasCancer);
                if (jsonObject.getString("type").equals("queryCountPerm")) {
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

                if (jsonObject.getString("type").equals("queryCountPerm")) {
                    System.out.println("permutaion method");
                    BigInteger res = paillier.Encryption(new BigInteger("0"));
                    for (int i = start; i < (2 * limiting) + 1; i += stepsize) {

                        List<SequencePermuted> sequencePermuteds = queryDB.getPermutatedSequence(stepsize, i);
                        System.out.print(i + ":" + sequencePermuteds.size() + ",");
                        if (sequencePermuteds.size() == 0) {
                            break;
                        }
                        for (SequencePermuted sequencePermuted : sequencePermuteds) {
                            boolean flag = false;
                            for (Map.Entry<Integer, String> entrySet : queryMapPerm.entrySet()) {

                                SnpRatio ratio = queryDB.getAllSnipRatios(hasCancer, entrySet.getKey());
                                if (ratio != null) {
                                    int pos = ratio.getPosition() * 2;
                                    if ((sequencePermuted.getPermutatedSequence().toCharArray())[pos] == entrySet.getValue().toCharArray()[0]) {
                                        flag = true;
                                    } else {
                                        flag = false;
                                        break;
                                    }
                                }
                            }
                            if (flag) {
                                res = paillier.add(res, new BigInteger(sequencePermuted.getTag()));
                            }
                        }
                        if (i % 5000 == 0 && i != start) {
                            try {
                                String output = Json.createObjectBuilder()
                                        .add("type", "ack")
                                        .add("msg", queryNo)
                                        .add("iter", i)
                                        .add("result", res.toString())
                                        .build().toString();
                                System.out.println("Outputted for " + queryNo + " res " + paillier.Decryption(res));
                                session.getBasicRemote().sendText(output);
                            } catch (IOException ex) {
                                Logger.getLogger(CountQueryApi.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else if (i != start) {
                            try {
                                String output = Json.createObjectBuilder()
                                        .add("type", "fakeack")
                                        //                                        .add("iter", i)
                                        //                                        .add("msg", queryNo)
                                        //                                        .add("result", res.toString())
                                        .build().toString();
//                                System.out.println("Outputted for " + queryNo + " res " + paillier.Decryption(res));
                                session.getBasicRemote().sendText(output);
                            } catch (IOException ex) {
                                Logger.getLogger(CountQueryApi.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }

                } else {
                    System.out.println("Regular method");
                    BigInteger res = new BigInteger("0");
                    for (int i = start; i < limiting + 1; i += stepsize) {
                        List<Sequence> sequencePermuteds = queryDB.getRegularSequence(stepsize, i);
                        System.out.print(i + ":" + sequencePermuteds.size() + ",");
                        if (sequencePermuteds.size() == 0) {
                            break;
                        }
                        for (Sequence sequencePermuted : sequencePermuteds) {
                            boolean flag = false;
                            for (Map.Entry<Integer, String> entrySet : queryMapPerm.entrySet()) {
                                SnpRatio ratio = queryDB.getAllSnipRatios(hasCancer, entrySet.getKey());
                                if (ratio != null) {
                                    int pos = ratio.getPosition() * 2;
                                    if ((sequencePermuted.getSequence().toCharArray())[pos] == entrySet.getValue().toCharArray()[0]) {
                                        flag = true;
//                                    System.out.println("res "+res);
                                    } else {
                                        flag = false;
                                        break;
                                    }
                                }
                            }
                            if (flag) {
                                res = res.add(new BigInteger("1"));
                            }
                        }
                        if (i % 5000 == 0 && i != start) {
                            try {
                                String output = Json.createObjectBuilder()
                                        .add("type", "ack")
                                        .add("iter", i)
                                        .add("msg", queryNo)
                                        .add("result", res.toString())
                                        .build().toString();
                                System.out.println("Outputted for " + queryNo + "res " + res);
                                session.getBasicRemote().sendText(output);
                            } catch (IOException ex) {
                                Logger.getLogger(CountQueryApi.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else if (i != start) {
                            try {
                                String output = Json.createObjectBuilder()
                                        .add("type", "fakeack")
                                        //                                        .add("iter", i)
                                        //                                        .add("msg", queryNo)
                                        //                                        .add("result", res.toString())
                                        .build().toString();
//                                System.out.println("Outputted for " + queryNo + "res " + res);
                                session.getBasicRemote().sendText(output);
                            } catch (IOException ex) {
                                Logger.getLogger(CountQueryApi.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }

                }

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
