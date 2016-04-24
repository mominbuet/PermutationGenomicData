/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Experiments;

import Database.QueryDB;
import Utilities.ChatClientEndpoint;
import Utilities.ConfigParser;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import javax.json.Json;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author azizmma
 */
public class QueryTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws URISyntaxException {
        final ConfigParser config = new ConfigParser("Config.conf");
        String destUri = "ws://" + config.getString("Host") + ":" + config.getString("SocketPort") + "/" + config.getString("SocketEndpoint");
        Date d11 = new Date();

        QueryDB queryDB = new QueryDB();
        final ChatClientEndpoint clientEndPoint = new ChatClientEndpoint(new URI(destUri));
        clientEndPoint.addMessageHandler(new ChatClientEndpoint.MessageHandler() {
            @Override
            public void handleMessage(String message) {
            }
        });
        
        JsonObjectBuilder param = Json.createObjectBuilder();
        param.add("position", 1);
        param.add("snp", "A");
        
    }

}
