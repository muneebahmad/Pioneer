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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import pk.muneebahmad.client.pioneer.ui.MainWindow;

/**
 *
 * @author muneebahmad
 */
public class EmployeeDataPersistence {

    public static ArrayList<EmployeeData> employeeDataList = new ArrayList<>();
    
    public EmployeeDataPersistence() {}
    
    /**
     * 
     * @param name
     * @param id
     * @param salary
     * @param location
     * @param attType
     * @param arrivalTime
     * @param tax
     * @param loan
     * @param days 
     */
    public static void add(String name, String id, String salary, String location, String attType, 
            String arrivalTime, String tax, String loan, String days) {
        EmployeeData emp = new EmployeeData(name, id, salary, location, attType, arrivalTime, tax, loan, days);
        employeeDataList.add(emp);
    }
    
    /**
     * 
     * @param name 
     */
    public static void delete(String name) {
        for (int i = 0; i < employeeDataList.size(); i++) {
            if (employeeDataList.get(i).getName().equals(name))
                employeeDataList.remove(i);
        }
    }
    
    /**
     * 
     * @param name
     * @return 
     */
    public static EmployeeData search(String name) {
        EmployeeData ser = null;
        for (int i = 0; i < employeeDataList.size(); i++) {
            if (employeeDataList.get(i).getName().equals(name))
                ser = employeeDataList.get(i);
        }
        return ser;
    }
    
    /**
     * 
     * @param fileName 
     */
    public static void write(String fileName) {
       try {
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            
            for (int i = 0; i < employeeDataList.size(); i++) {
                out.writeObject(employeeDataList.get(i));
            }
            out.close();
            fos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EmpPersistence.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EmpPersistence.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void load() {
        try {
            FileInputStream fis = new FileInputStream(MainWindow.EMP_DATA_FILENAME);
            ObjectInputStream in = new ObjectInputStream(fis);
            
            EmployeeData p = (EmployeeData) in.readObject();
            while (p != null) {
                employeeDataList.add(p);
                p = (EmployeeData) in.readObject();
            }
            
            in.close();
            fis.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EmpPersistence.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(EmpPersistence.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}/** end class. */
