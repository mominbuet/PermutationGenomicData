/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Experiments;

import Database.QueryDB;
import Database.Snps;
import Utilities.ChatClientEndpoint;
import Utilities.ConfigParser;
import Utilities.Utils;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author azizmma
 */
public class InsertionTest {

    static Date d1;
    static int iterations = 0;
    static int count = 0;
    static String TYPE = "control";
    static List<Snps> snips;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws URISyntaxException, InterruptedException {
        final ConfigParser config = new ConfigParser("Config.conf");
        String destUri = "ws://" + config.getString("Host") + ":" + config.getString("SocketPort") + "/" + config.getString("SocketEndpoint") + 
                "/endpoint_permutation_insert";//endpoint_permutation_insert,endpoint_permutation_insertperm
        Date d11 = new Date();

        QueryDB queryDB = new QueryDB();
        snips = queryDB.getAllSnip(TYPE,600,622);
        iterations = snips.get(0).getDescription().split(" ").length;

        System.out.println("Endpoint " + destUri);
        final ChatClientEndpoint clientEndPoint = new ChatClientEndpoint(new URI(destUri));
        clientEndPoint.addMessageHandler(new ChatClientEndpoint.MessageHandler() {
            @Override
            public void handleMessage(String message) {
//                Map<String,String> test = new HashMap<>();
//                System.out.println("Message from server " + message);
                JsonObject jsonObject = Json.createReader(new StringReader(message)).readObject();
                if (jsonObject.getString("type").equals("ack")) {
                    Date d2 = new Date();
                    System.out.println(count + ":" + ((d2.getTime() - d1.getTime())));
                    d1 = new Date();
                    if (count < iterations) {
                        count++;
                        sendRow(count, clientEndPoint);
                    }
                }

            }
        });
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        clientEndPoint.sendMessage(Utils.getMessage("ping", ""));
                        Thread.sleep(4000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(InsertionTest.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                    } catch (Exception ex) {
                        Logger.getLogger(InsertionTest.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                    }
                }
            }
        });
//        t.start();
        sendRow(0, clientEndPoint);

        while (count < iterations) {
//            System.out.println("iteration "+iterations);
            Thread.sleep(500);
        }
        t.join();
        Date d22 = new Date();
        System.out.println("Total Running time " + ((d22.getTime() - d11.getTime())));
    }

    private static void sendRow(int i, ChatClientEndpoint clientEndPoint) {
        try {
            JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
            JsonObjectBuilder snps = Json.createObjectBuilder();
//            String[] types = {"case", "control"};
//
//            for (String type : types) {

//                for (int i = 0; i < length; i++) {
            for (int j = 0; j < snips.size(); j++) {
                String[] parts = snips.get(j).getDescription().split(" ");
                snps.add(snips.get(j).getSnip(), parts[i]);
            }
            jsonObjectBuilder.add("count", count);
            jsonObjectBuilder.add("snps", snps);
            String sending = Json.createObjectBuilder()
                    .add("type", "insert")
                    .add("casecontrol", TYPE)
                    .add("msg", jsonObjectBuilder.build())
                    .build()
                    .toString();
//            System.out.println(count + "::" + sending);
            clientEndPoint.sendMessage(sending);
            d1 = new Date();
//                }
//            }

        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(InsertionTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
