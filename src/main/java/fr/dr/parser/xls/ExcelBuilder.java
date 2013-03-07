package fr.dr.parser.xls;

import org.apache.poi.hssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: drieu
 * Date: 07/03/13
 * Time: 12:33
 * To change this template use File | Settings | File Templates.
 */
public class ExcelBuilder {

    public static void saveInXls(List<XlsObject> lst) {
        System.out.println("Save xls ...");
        //
        HSSFWorkbook workbook = new HSSFWorkbook();


        HSSFSheet firstSheet = workbook.createSheet("FIRST SHEET");
        HSSFSheet secondSheet = workbook.createSheet("SECOND SHEET");

        int row = 0;
        for(XlsObject obj : lst) {
            //
            obj.toString();
            HSSFRow rowA = firstSheet.createRow(row);
            HSSFCell cellA = rowA.createCell(0);
            cellA.setCellValue(new HSSFRichTextString(obj.getMaName()));

            HSSFCell cellB = rowA.createCell(1);
            cellB.setCellValue(new HSSFRichTextString(obj.getTableLine()));

            HSSFCell cellC= rowA.createCell(2);
            cellC.setCellValue(new HSSFRichTextString(obj.getFilePath()));
            row++;
        }


        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File("/home/drieu/Investigations/entity.xls"));
            workbook.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
