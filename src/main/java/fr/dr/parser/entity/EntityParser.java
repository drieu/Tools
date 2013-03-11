package fr.dr.parser.entity;

import fr.dr.parser.xls.ExcelBuilder;
import fr.dr.parser.xls.XlsObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parse java files and retrieve all entity with command :
 * find ./ -iname *.java | grep -R "@Entity" ma* | grep -v svn
 *
 * User: drieu
 * Date: 07/03/13
 * Time: 12:02
 */
public class EntityParser {

    /**
     * Directory which contains all maven projects.
     */
    private String projectDir;


    private List<XlsObject> lstXls = new ArrayList<XlsObject>();

    private static final Logger log = LoggerFactory.getLogger(EntityParser.class);

    public static void main(String[] args) {

        EntityParser entityParser = new EntityParser("/home/drieu/Repos/Palier/");
        List<XlsObject> lstXls = entityParser.extractFromLst("/home/drieu/Investigations/entity.txt");
        ExcelBuilder.saveInXls(lstXls);
    }

    /**
     *
     * @param directory Directory which contains all maven projects.
     */
    public EntityParser(String directory) {
         projectDir = directory;
    }

    /**
     * Parse line from result of find command
     * find ./ -iname *.java | grep -R "@Entity" ma* | grep -v svn
     * @param path
     */
    public List<XlsObject> extractFromLst(String path) {
        lstXls = new ArrayList<XlsObject>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(path)));
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {

                String ma = extractMA(line);
                String tbl = extractTbl(line);
                String filePath = extractPath(line);

                log.debug(ma + " " + tbl);
                XlsObject xls = new XlsObject();
                xls.setMaName(ma);
                xls.setTableLine(tbl);
                xls.setFilePath(filePath);
                lstXls.add(xls);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return lstXls;
    }


    public  List<XlsObject> extractXlsObject(String maName, String javaFilePath) {
        List<XlsObject> lstXls = new ArrayList<XlsObject>();
        String tbl = extractTblLineAnnot(javaFilePath);

        XlsObject xls = new XlsObject();
        xls.setMaName(maName);
        xls.setTableLine(tbl);
        xls.setFilePath(javaFilePath);
        lstXls.add(xls);

        return lstXls;
    }

    /**
     * Extract MA name between 0 and '/' in line string
     * @param line  MAname/....
     * @return  MA name
     */
    private String extractMA(String line) {
        String result = null;
        int pos = line.indexOf('/');
        if (pos != -1) {
            result = line.substring(0, pos);
        }
        return result;
    }

    /**
     * Extract table name.
     * @param grepLine
     * @return
     */
    private String extractTbl(String grepLine) {
        String result = null;
        int pos = grepLine.indexOf(":");
        if (pos != -1) {
            result = extractTblLineAnnot(grepLine.substring(0, pos));
        }
        return result;
    }

    /**
     * Extract path from grep line.
     * @param grepLine String.
     * @return
     */
    private String extractPath(String grepLine) {
        String result = null;
        int pos = grepLine.indexOf(":");
        if (pos != -1) {
            result = grepLine.substring(0, pos);
        }
        return result;
    }

    /**
     * Read java file and extract table name if possible contains in @table( name="tbl" ).
     * @param filename String
     * @return
     */
    private String extractTblLineAnnot(String filename) {
        String result = null;
        BufferedReader bufferedReader = null;
        File file = null;
        try {
            file = new File(projectDir + filename);
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

    private String extractTblName(String line) {
       String result = null;
        int pos = line.indexOf("\"");
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
