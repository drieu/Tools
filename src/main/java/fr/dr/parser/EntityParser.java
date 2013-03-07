package fr.dr.parser;

import fr.dr.parser.xls.ExcelBuilder;
import fr.dr.parser.xls.XlsObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: drieu
 * Date: 07/03/13
 * Time: 12:02
 * To change this template use File | Settings | File Templates.
 */
public class EntityParser {

    public static void main(String[] args) {

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("/home/drieu/Investigations/entity.txt")));
            String line = null;
            List<XlsObject> lstXls = new ArrayList<XlsObject>();
            while ((line = bufferedReader.readLine()) != null) {

                String ma = extractMA(line);
                String tbl = extractMsg(line);
                String filePath = extractPath(line);

                System.out.println(ma + " " + tbl);
                XlsObject xls = new XlsObject();
                xls.setMaName(ma);
                xls.setTableLine(tbl);
                xls.setFilePath(filePath);
                lstXls.add(xls);
            }

//            for(XlsObject xls : lstXls) {
//
//            }
            ExcelBuilder.saveInXls(lstXls);
//            for(String key : map.keySet()) {
//                String val = map.get(key);
//                System.out.println(key + ";" + val);
//            }
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
        int pos = line.indexOf(":");
        //System.out.println("====>line:" + line);
        if (pos != -1) {
            result = extractTblLineAnnot(line.substring(0, pos));
        }
        return result;
    }

    public static String extractPath(String line) {
        String result = null;
        int pos = line.indexOf(":");
        //System.out.println("====>line:" + line);
        if (pos != -1) {
            result = line.substring(0, pos);
        }
        return result;
    }

    public static String extractTblLineAnnot(String filename) {
        String result = null;
        BufferedReader bufferedReader = null;
        File file = null;
        try {
            file = new File("/home/drieu/Repos/Palier/" + filename);
            bufferedReader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        String line = null;
        Map<String, String> map = new HashMap<String, String>();
        try {
            while ((line = bufferedReader.readLine()) != null) {

                if (line.contains("@Table")) {
                    result = extractTblName(line);
                    if (result == null) {
                        result = file.getName();
                    }
                    break;
                }


            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return result;
    }

    private static String extractTblName(String line) {
       String result = null;
        int pos = line.indexOf("\"");
        //System.out.println("====>line:" + line);
        if (pos != -1) {
            result = line.substring(pos + 1, line.length());
            pos = result.indexOf("\"");
            if (pos != -1) {
                result = result.substring(0, pos);
            }
        }

        return result;

    }

}
