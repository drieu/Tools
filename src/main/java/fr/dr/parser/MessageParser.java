package fr.dr.parser;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: drieu
 * Date: 05/03/13
 * Time: 10:02
 * To change this template use File | Settings | File Templates.
 */
public class MessageParser {

    public static void main(String[] args) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("/home/drieu/Documents/evenement_xml.txt")));
            String line = null;
            Map<String, String> map = new HashMap<String, String>();
            while ((line = bufferedReader.readLine()) != null) {

                String ma = extractMA(line);
                String msg = extractMsg(line);
                if ((ma != null) && (msg != null)) {
                    String tmpMsg = map.get(ma);
                    if (null == tmpMsg) {
                        map.put(ma, msg);
                    }
                    if ( (null != tmpMsg) && !tmpMsg.contains(msg)) {
                        map.put(ma, msg);
                    }
                }

            }

            for(String key : map.keySet()) {
                String val = map.get(key);
                System.out.println(key + ";" + val);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static  String extractMA(String line) {
        String result = null;
        int pos = line.indexOf('/');
        result = line.substring(0, pos);

        return result;
    }
    public static  String extractMsg(String line) {
        String result = null;
        int pos = line.indexOf("<?");
        //System.out.println("====>line:" + line);
        if (pos != -1) {
            result = line.substring(pos, line.length());
        }
        return result;
    }
}
