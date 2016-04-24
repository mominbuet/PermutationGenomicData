/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Experiments;

import Database.QueryDB;
import Utilities.ChatClientEndpoint;
import Utilities.ConfigParser;
import Utilities.Paillier;
import Utilities.Utils;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author azizmma
 */
public class RankedQueryTest {

    static Paillier paillier;
    static Date d1;
    static int count = 10;
    static int kResults = 100;
    static int highest = 60;

    public static String getRandomSeq(int length) {
        String seq = "";
        for (int j = 0; j < length; j++) {
            int rand = ThreadLocalRandom.current().nextInt(0, 3 + 1);
            switch (rand) {
                case 0:
                    seq += "A";
                    break;
                case 1:
                    seq += "T";
                    break;
                case 2:
                    seq += "G";
                    break;
                case 3:
                    seq += "C";
                    break;
            }
        }
        return seq;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        final ConfigParser config = new ConfigParser("Config.conf");
        String destUri = "ws://" + config.getString("Host") + ":" + config.getString("SocketPort") + "/"
                + config.getString("SocketEndpoint") + "/endpoint_ranked";
        Date d11 = new Date();
        paillier = new Paillier(true);
        QueryDB queryDB = new QueryDB();
        final ChatClientEndpoint clientEndPoint = new ChatClientEndpoint(new URI(destUri));
        clientEndPoint.addMessageHandler(new ChatClientEndpoint.MessageHandler() {
            @Override
            public void handleMessage(String message) {
//                System.out.println("message " + message);
                JsonObject jsonObject = Json.createReader(new StringReader(message)).readObject();
                if ((jsonObject.getString("type")).equals("ack")) {

                    //paillier.Decryption(new BigInteger(jsonObject.getString("result"))));
                    int sent = jsonObject.getInt("msg");
//                    for (int i = 20; i < 61; i += i + 10) {
                    try {
                        if (sent < kResults) {
                            sendQuery(count, sent, clientEndPoint);
                        } else {
                            Date d2 = new Date();
                            System.out.println(count + ":" + (double) ((d2.getTime() - d1.getTime())) / 1000);
                            if (count < highest+1) {
                                count += 10;
                                sendQuery(count, 0, clientEndPoint);
                            }
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(QueryTest.class.getName()).log(Level.SEVERE, null, ex);
                    }
//                    }
                }else{
//                    System.out.println(jsonObject.getString("type"));
                    try {
                        clientEndPoint.sendMessage(Utils.getMessage("ping", "nothing"));
                    } catch (IOException ex) {
                        Logger.getLogger(RankedQueryTest.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        sendQuery(count, 0, clientEndPoint);
        while (count < highest + 1) {
            Thread.sleep(500);
        }
        Date d22 = new Date();
        System.out.println("Total Running time " + (double) ((d22.getTime() - d11.getTime())) / 1000);

    }

    private static void sendQuery(int limit, int offset, ChatClientEndpoint clientEndPoint) throws IOException {
        List<Integer> sentList = new ArrayList<>();
        JsonArrayBuilder array = Json.createArrayBuilder();
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("hasCancer", (ThreadLocalRandom.current().nextInt(0, 2) % 2 == 0) ? "case" : "control");

        jsonObjectBuilder.add("limit", limit);
        jsonObjectBuilder.add("offset", offset);
//        for (int i = 10; i < 61; i = i + 10) {
        JsonObjectBuilder param = Json.createObjectBuilder();
        for (int j = 0; j < 100; j++) {
            int tmp = ThreadLocalRandom.current().nextInt(0, count + j);
            while (sentList.contains(tmp)) {
                tmp = ThreadLocalRandom.current().nextInt(0, count + j);
            }
            sentList.add(tmp);
            param.add("p", tmp);
            param.add("s", getRandomSeq(1));
            array.add(param);
        }

//            break;
//        }
        jsonObjectBuilder.add("query", array);
        String sending = Utils.getMessage("queryRankedPerm", jsonObjectBuilder);//queryRankedReg,queryRankedPerm
//        System.out.println(sending);
        clientEndPoint.sendMessage(sending);
        if (offset == 0) {
            d1 = new Date();
        }
    }

}
