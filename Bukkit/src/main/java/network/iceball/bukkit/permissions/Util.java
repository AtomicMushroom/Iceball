package network.iceball.bukkit.permissions;

import network.iceball.bukkit.Iceball;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Floris on 27-10-15.
 */
public class Util {

    private static String P = Iceball.pluginPrefix;
    private static String S = Iceball.pluginSuffixNRML;
    private static String Z = Iceball.pluginSuffixMARK;

    public static HashMap<String, Boolean> stringToHashMap(String permissionsList) {
        if (permissionsList == null || permissionsList.isEmpty()){ return new HashMap<>(); }
        String[] list = permissionsList.split(",");
        HashMap<String, Boolean> map = new HashMap<String, Boolean>();
        for (int i = 0; i < list.length; i++) {
            String temp = list[i];
            temp = temp.trim();
            if (temp.contains("-")) {
                temp = temp.replace("-", "");
                map.put(temp, false);
            } else {
                map.put(temp, true);
            }
        }
        return map;
    }


    public static String hashMapToString(HashMap<String, Boolean> hm) {
        Set<Map.Entry<String, Boolean>> set = hm.entrySet();
        String list = "";

        for (Map.Entry<String, Boolean> me : set) {
            if (!me.getValue()){
                list = list + "-" + me.getKey() + ",";
            } else {
                list = list + me.getKey() + ",";
            }
        }
        return list;
    }
    public static void printHashMap(HashMap hm) {
        Set<Map.Entry<String, Boolean>> set = hm.entrySet();
        for (Map.Entry<String, Boolean> me : set) {
            System.out.println(me.getKey() + ":" + me.getValue());
        }
    }

    public static Set<String> parsePermssions(Set<String> list, String permissions){
        String[] groups = permissions.split(",");
        for (String s : groups){
            if (s.contains("-")){
                s = s.substring(1, s.length());
                list.add(Z + " - " + s);
            } else {
                list.add(Z + " + " + s);
            }
        }
        return list;
    }
    public static String formatName(String name){
        if (name.endsWith("s")){
            name = name + "'"  + S;
            return name;
        } else {
            name = name + "'s" + S;
            return name;
        }
    }

    public static String arrayToString(String[] strings){
        String object = "";
        if (strings == null){
            return object;
        }
        for (String s : strings){
            object = s +  "," + object;
        }
        return object;
    }
}
