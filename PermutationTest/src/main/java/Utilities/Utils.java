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
