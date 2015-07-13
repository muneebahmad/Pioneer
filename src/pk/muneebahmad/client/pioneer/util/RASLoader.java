/**
 * copyright ©2013-2014 ®Algorithmi™.
 *
 * @author ¶muneebahmad¶ (ahmadgallian@yahoo.com) 
 * NetBeans IDE http://www.netbeans.org
 *
 * For all entities this program is free software; you can redistribute
 * it and/or modify it under the terms of the 'MyGdxEngine' license with
 * the additional provision that 'MyGdxEngine' must be credited in a manner
 * that can be be observed by end users, for example, in the credits or during
 * start up. (please find MyGdxEngine logo in sdk's logo folder)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * The following source - code IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * **/

package pk.muneebahmad.client.pioneer.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import pk.muneebahmad.client.pioneer.ui.LoaderErrorDialog;

/**
 *
 * @author muneebahmad
 */
public class RASLoader {

    private static RASLoader sInstance = null;
    private final String sheetFilePath;
    private File file;
    private Workbook workBook;
    private Sheet sheet;
    private int sheetCols;
    private int sheetRows;
    
    /**
     * 
     * @param sheetFilePath 
     */
    public RASLoader(String sheetFilePath) {
       this.sheetFilePath = sheetFilePath; 
       Log.log(ExcelLoader.class.getName(), "Trying to load data");
       load();
    }
    
    private void load() {
        try {
            file = new File(sheetFilePath);
            workBook = Workbook.getWorkbook(file);
            sheet = workBook.getSheet("Sheet1");
            
            //get sheets cols and rows
            sheetCols = sheet.getColumns();
            sheetRows = sheet.getRows();
            
            
        } catch (IOException | BiffException ex) {
            Logger.getLogger(ExcelLoader.class.getName()).log(Level.SEVERE, null, ex);
            LoaderErrorDialog dia = new LoaderErrorDialog(null, true, ex.toString());
            dia.pack();
            dia.setVisible(true);
        }
    }
    
    /**
     * 
     * @return {@link #String} filePath of RAS file.
     */
    public String getRasFilePath() {
        return this.sheetFilePath;
    }
    
    /**
     * GET SHEET COLUMN NO.
     * @return sheetCols int
     */
    public int getSheetColumnNo() {
        return this.sheetCols;
    }
    
    /**
     * GET SHEET ROW NO.
     * @return sheetRows int
     */
    public int getSheetRowNo() {
        return this.sheetRows;
    }
    
    /**
     * 
     * @return 
     */
    public String getSheetName() {
        return sheet.getName();
    }
    
    /**
     * 
     * @return {@link #Sheet}
     */
    public Sheet getSheet() {
        return this.sheet;
    }
    
    /**
     * 
     * @param sheetFilePath
     * @return {@link #ExcelLoader(java.lang.String)} 
     */
    public static RASLoader getInstance(String sheetFilePath) {
        synchronized(RASLoader.class) {
            if (sInstance == null) {
                sInstance = new RASLoader(sheetFilePath);
            }
            return sInstance;
        } 
    }
    
}/** end class. */
