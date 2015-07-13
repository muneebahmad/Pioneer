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
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import pk.muneebahmad.client.pioneer.ui.MainWindow;


/**
 *
 * @author muneebahmad
 */
public class WorkbookLoader {

    private static WorkbookLoader sInstance = null;
    private WritableWorkbook wr;
    private WritableSheet sheet1;
    
    private Sheet loadingSheet;
    private int rowNos;
    private int colNos;
    
    public WorkbookLoader() {
    }
    
    public void prepareWorkbookForWriting() {
        try {
            wr = Workbook.createWorkbook(new File(MainWindow.WORKBOOK_FILLENAME));
            sheet1 = wr.createSheet("Employees", 0);
        } catch (IOException ex) {
            Logger.getLogger(WorkbookLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 
     * @param col int
     * @param row int
     * @param content 
     * @throws jxl.write.WriteException 
     * @throws java.io.IOException 
     */
    public void writeTitle(int col, int row, String content) throws WriteException, IOException {
        Label l = new Label(col, row, content);
        sheet1.addCell(l);
    }

    public Sheet getLoadingSheet() {
        return loadingSheet;
    }

    public int getRowNos() {
        return rowNos;
    }

    public int getColNos() {
        return colNos;
    }
    
    /**
     * 
     * @throws IOException
     * @throws WriteException 
     */
    public void persist() throws IOException, WriteException {
        wr.write();
        wr.close();
    }
    
    /**
     * 
     * @throws java.io.IOException 
     * @throws jxl.read.biff.BiffException 
     */
    public void loadWorkbook() throws IOException, BiffException {
        File file = new File(MainWindow.WORKBOOK_FILLENAME);
        Workbook work = Workbook.getWorkbook(file);
        
        loadingSheet = work.getSheet("Employees");
        colNos = loadingSheet.getColumns();
        rowNos = loadingSheet.getRows();
    }
    
    /**
     * 
     * @return new instance of {@link #WorkbookLoader} {@code class}
     */
    public static WorkbookLoader getInstance() {
        synchronized(WorkbookLoader.class) {
            if (sInstance == null) {
                sInstance = new WorkbookLoader();
            }
            return sInstance;
        }
    }
    
}/** end class. */
