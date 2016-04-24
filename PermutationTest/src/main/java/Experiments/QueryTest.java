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
public class QueryTest {

    static String destUri;
    static ChatClientEndpoint clientEndPoint;
    static Paillier paillier;
    static Date d1;
    static int count = 10;
    static int highest = 40;

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
        destUri = "ws://" + config.getString("Host") + ":" + config.getString("SocketPort") + "/"
                + config.getString("SocketEndpoint") + "/endpoint_count";
        Date d11 = new Date();
        paillier = new Paillier(true);
        QueryDB queryDB = new QueryDB();
        clientEndPoint = new ChatClientEndpoint(new URI(destUri));
        clientEndPoint.addMessageHandler(new ChatClientEndpoint.MessageHandler() {
            @Override
            public void handleMessage(String message) {
//                System.out.println("message " + message);
                JsonObject jsonObject = Json.createReader(new StringReader(message)).readObject();
                if ((jsonObject.getString("type")).equals("ack")) {
                    Date d2 = new Date();
                    System.out.println(count + "-" + jsonObject.getInt("iter") + ":" + ((double) 0 + (double) ((d2.getTime() - d1.getTime())) / 1000));// + " res " + jsonObject.getString("result"));
                    //paillier.Decryption(new BigInteger(jsonObject.getString("result"))));

//                    for (int i = 20; i < 61; i += i + 10) {
                    try {
                        if (jsonObject.getInt("iter") == 35000) {
                            count = jsonObject.getInt("msg") + 10;
                            if (count < highest + 1) {
                                sendQuery(count, clientEndPoint);
                            }
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(QueryTest.class.getName()).log(Level.SEVERE, null, ex);
                    }
//                    }
                }
            }
        });
        sendQuery(count, clientEndPoint);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        clientEndPoint.sendMessage(Utils.getMessage("ping", ""));
                        Thread.sleep(800);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(QueryTest.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                    } catch (NullPointerException ex) {
                        try {
                            clientEndPoint = new ChatClientEndpoint(new URI(destUri));
                        } catch (URISyntaxException ex1) {
                            Logger.getLogger(QueryTest.class.getName()).log(Level.SEVERE, null, ex1);
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(QueryTest.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                    }
                }
            }
        });
//        t.start();

        while (count < highest + 1) {
            Thread.sleep(500);
            if (clientEndPoint == null) {
                clientEndPoint = new ChatClientEndpoint(new URI(destUri));
            } else {
                clientEndPoint.sendMessage(Utils.getMessage("ping", ""));
            }
        }
        Date d22 = new Date();
        System.out.println("Total Running time " + (double) (d22.getTime() - d11.getTime()) / 1000);

    }

    private static void sendQuery(int count, ChatClientEndpoint clientEndPoint) throws IOException {
        List<Integer> sentList = new ArrayList<>();
        JsonArrayBuilder array = Json.createArrayBuilder();
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("hasCancer", (ThreadLocalRandom.current().nextInt(0, 2) % 2 == 0) ? "case" : "control");

        jsonObjectBuilder.add("queryNo", count);
//        for (int i = 10; i < 61; i = i + 10) {
        JsonObjectBuilder param = Json.createObjectBuilder();
        for (int j = 0; j < count; j++) {
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
        String sending = Utils.getMessage("queryCountReg", jsonObjectBuilder);//queryCountReg,queryCountPerm
//        System.out.println(sending);
        clientEndPoint.sendMessage(sending);
        d1 = new Date();
    }

}
