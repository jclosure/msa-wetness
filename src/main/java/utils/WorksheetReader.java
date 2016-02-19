package utils;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by joel on 2/16/16.
 */
public class WorksheetReader {


    public static Map<String, String> read(String xlsFilePath, String worksheetName) {

        Map<String, String> map = new ConcurrentHashMap<>();

        try {
            FileInputStream fileInputStream = new FileInputStream(xlsFilePath);
            HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);
            HSSFSheet worksheet = workbook.getSheet(worksheetName);
            HSSFRow headerRow = worksheet.getRow(2);

            int begin = 3;
            int end = worksheet.getLastRowNum();

            for (int i=begin; i<=end; i++)
            {
                try {
                    HSSFRow row = worksheet.getRow(i);
                    String CBSA_Title = row.getCell((short) 1).getStringCellValue();
                    String FIPS_Place_Code = row.getCell((short) 5).getStringCellValue();
                    map.put(CBSA_Title, FIPS_Place_Code);
                }
                catch (NullPointerException ex) {
                    System.out.println(ex);
                    System.out.println("Line: " + i);
                    return map;
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }
}