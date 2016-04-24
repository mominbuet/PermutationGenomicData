/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author azizmma
 */
public class Utils {

    public static int getDistance(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();
        // i == 0
        int[] costs = new int[b.length() + 1];
        for (int j = 0; j < costs.length; j++) {
            costs[j] = j;
        }
        for (int i = 1; i <= a.length(); i++) {
            // j == 0; nw = lev(i - 1, j)
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);//ThreadLocalRandom.current().nextInt(2, 5)
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }

    public static String getMessage(String type, JsonObjectBuilder message) {
        return Json.createObjectBuilder()
                .add("type", type)
                .add("msg", message.build())
                .build()
                .toString();
    }

    public static String getMessage(String type, Integer message) {
        return Json.createObjectBuilder()
                .add("type", type)
                .add("msg", message)
                .build()
                .toString();
    }

    public static String getMessage(String type, String message) {
        return Json.createObjectBuilder()
                .add("type", type)
                .add("msg", message)
                .build()
                .toString();
    }
}
