/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs.umanitoba.permutationproxy;

import Database.QueryDB;
import Database.Sequence;
import Utilities.Paillier;
import Utilities.Utils;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author azizmma
 */
@ServerEndpoint("/endpoint_permutation_insert")
public class InsertApi {

    @OnMessage

    public void onMessage(String message, Session session) {
        QueryDB queryDB = new QueryDB();
//        System.out.println("Message " + message);
        JsonObject jsonObject = Json.createReader(new StringReader(message)).readObject();
        String type = jsonObject.getString("type");
        String cctype = jsonObject.getString("casecontrol");
        if (type.equals("insert")) {
            Map<Integer, String> snpMapOriginal = new TreeMap<>();
            JsonObject msg = jsonObject.getJsonObject("msg").getJsonObject("snps");
//            System.out.println(msg.toString());
            int count = jsonObject.getJsonObject("msg").getInt("count", -1);
            Map<String, String> snpMap = new TreeMap<>();
            for (String keys : msg.keySet()) {
//                System.out.println(keys + ":" + msg.getString(keys));

//                String tmp = "";
//                List<SnpRatio> snp = queryDB.getFromSnipType(parts[0], parts[1]);
//                if (snp.size() > 0) {
//                    if (snp.get(0).getValidPermutation() != null) {
//                        tmp = snpMap.get(snp.get(0).getValidPermutation().getSnipId() + "-" + snp.get(0).getValidPermutation().getType());
//                        snpMap.put(keys, tmp);
//                        snpMap.put(snp.get(0).getValidPermutation().getSnipId() + "-" + snp.get(0).getValidPermutation().getType(), msg.getString(keys));
//                    }
//                } else {
                snpMap.put(keys, msg.getString(keys));
//                System.out.println(keys.substring(2, keys.length()));
                snpMapOriginal.put(Integer.parseInt(keys.substring(2, keys.length())), msg.getString(keys));
//                }
            }
//            Map<Integer, String> snpMapFakePermuted = new TreeMap<>();
//            Map<Integer, String> snpMapPermuted = new TreeMap<>();
//            String tmp = "";
//            for (Map.Entry<String, String> entrySet : snpMap.entrySet()) {
//                List<SnpRatio> snp = queryDB.getFromSnipType(entrySet.getKey(), cctype);
//                snpMapPermuted.put(Integer.parseInt(entrySet.getKey().substring(2, entrySet.getKey().length())), msg.getString(entrySet.getKey()));
//                snpMapFakePermuted.put(Integer.parseInt(entrySet.getKey().substring(2, entrySet.getKey().length())), msg.getString(entrySet.getKey()));
//                if (snp.size() > 0) {
//                    if (snp.get(0).getValidPermutation() != null) {
//                        tmp = snpMap.get(snp.get(0).getValidPermutation().getSnipId());
//                        snpMapPermuted.put(Integer.parseInt(snp.get(0).getValidPermutation().getSnipId()
//                                .substring(2, snp.get(0).getValidPermutation().getSnipId().length())),
//                                msg.getString(entrySet.getKey()));
//                        snpMap.put(snp.get(0).getValidPermutation().getSnipId(), msg.getString(entrySet.getKey()));
//                        snpMap.put(entrySet.getKey(), tmp);
//                        snpMapPermuted.put(Integer.parseInt(entrySet.getKey().replace("rs", "")), tmp);
//                    }
//                    if (snp.get(0).getAllPermutation() != null && ThreadLocalRandom.current().nextInt(1, 3) % 2 == 0) {
//                        tmp = snpMap.get(snp.get(0).getAllPermutation().getSnipId());
//                        snpMapFakePermuted.put(Integer.parseInt(snp.get(0).getAllPermutation().getSnipId()
//                                .substring(2, snp.get(0).getAllPermutation().getSnipId().length())),
//                                msg.getString(entrySet.getKey()));
//                        snpMapFakePermuted.put(Integer.parseInt(entrySet.getKey().replace("rs", "")), tmp);
//                    }
//                }
//            }
            String general = "", permuted = "", fake = "";
            for (Map.Entry<Integer, String> entrySet : snpMapOriginal.entrySet()) {
//                permuted += entrySet.getValue();
                general += snpMapOriginal.get(entrySet.getKey());
//                fake += snpMapFakePermuted.get(entrySet.getKey());
            }
//            System.out.println(general);
//            System.out.println(permuted);
//            System.out.println(fake);
            //original
            Sequence sequence = new Sequence();
            sequence.setType(new Paillier(true).Encryption(new BigInteger("1")).toString());
            
            sequence.setSequence(general);
            sequence.setType(cctype);
            sequence.setInserted(new Date());
            queryDB.insertGeneric(sequence);
            //fake
//            sequence = new Permu();
//            sequence.setTag(new Paillier(true).Encryption(new BigInteger("0")).toString());
//            sequence.setInserted(new Date());
//            sequence.setPermutatedSequence(fake);
//            sequence.setSequence(general);
//            sequence.setType(cctype);
//            queryDB.insertGeneric(sequence);
            try {
                System.out.println(count);
                if (count > -1) {
                    session.getBasicRemote().sendText(Utils.getMessage("ack", count));
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
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
