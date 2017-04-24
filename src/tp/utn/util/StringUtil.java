package tp.utn.util;

import java.util.List;

/**
 * Created by TATIANA on 24/4/2017.
 */
public class StringUtil {

    public static String join (List<String> lst, String joiner){
        String ret = "";
        for (String s: lst) {
            ret += s + joiner;
        }
        return replaceLast(ret, joiner);
    }

    private static String replaceLast(String ret, String joiner) {
        return ret.substring(0, ret.lastIndexOf(joiner)) +
                ret.substring(ret.lastIndexOf(joiner) + joiner.length());
    }

}
