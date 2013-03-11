package fr.dr.parser.xls;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: drieu
 * Date: 07/03/13
 * Time: 12:34
 * To change this template use File | Settings | File Templates.
 */
public class XlsObject {

    private List<String> cols;


    public XlsObject(String... values) {
        for (String v : values) {
            cols.add(v);
        }


    }


    public XlsObject() {
    }

    private String maName;

    private String filePath;

    /**
     * @Table.
     */
    private String tableLine;

    public void setTableLine(String tableLine) {
        this.tableLine = tableLine;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setMaName(String maName) {
        this.maName = maName;
    }

    public String getMaName() {
        return maName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getTableLine() {
        return tableLine;
    }

    @Override
    public String toString() {
        return "XlsObject{" +
                "maName='" + maName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", tableLine='" + tableLine + '\'' +
                '}';
    }
}
