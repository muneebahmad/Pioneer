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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import pk.muneebahmad.client.pioneer.ui.MainWindow;

/**
 *
 * @author muneebahmad
 */
public class EmpPersistence implements Serializable {
    private static final long serialVersionUID = -8432621272342591695L;

    public static ArrayList<Employee> employeeList = new ArrayList<>();
    
    public EmpPersistence() {}
    
    /**
     * 
     * @param date
     * @param name
     * @param id
     * @param location
     * @param salary 
     * @param attType 
     */
    public static void add(String date, String name, String id, String location, String salary, String attType) {
        Employee e = new Employee(date, name, id, location, salary, attType);
        employeeList.add(e);
    }
    
    /**
     * 
     * @param name
     * @return 
     */
    public static Employee search(String name) {
        Employee ser = null;
        for (int i = 0; i < employeeList.size(); i++) {
            if (employeeList.get(i).getName().equals(name))
                ser = employeeList.get(i);
        }
        return ser;
    }
    
    public static void load() {
        try {
            FileInputStream fis = new FileInputStream(MainWindow.EMP_LIST_FILENAME);
            ObjectInputStream in = new ObjectInputStream(fis);
            
            Employee p = (Employee) in.readObject();
            while (p != null) {
                employeeList.add(p);
                p = (Employee) in.readObject();
            }
            
            in.close();
            fis.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EmpPersistence.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(EmpPersistence.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 
     * @param fileName 
     */
    public static void write(String fileName) {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            
            for (int i = 0; i < employeeList.size(); i++) {
                out.writeObject(employeeList.get(i));
            }
            out.close();
            fos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EmpPersistence.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EmpPersistence.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}/** end class. */
