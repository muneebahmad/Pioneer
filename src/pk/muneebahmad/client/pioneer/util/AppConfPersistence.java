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
import java.util.logging.Level;
import java.util.logging.Logger;
import pk.muneebahmad.client.pioneer.ui.ArrivalSetter;
import pk.muneebahmad.client.pioneer.ui.MainWindow;

/**
 *
 * @author muneebahmad
 */
public class AppConfPersistence {

    private static String arrivalTime = "";
    private static String salary = "";
    private static String tax = "";
    
    static AppConfigurations conf;
    
    public AppConfPersistence() {}
    
    /**
     * 
     * @param arrTime
     * @param sal
     * @param tx 
     */
    public static void change(String arrTime, String sal, String tx) {
        conf = new AppConfigurations(arrivalTime, salary, tax);
    }

    public static String getArrivalTime() {
        if (arrivalTime != null && !(arrivalTime.equals(""))) {
            return conf.getArrivalTime();
        } else {
            return "08.00";
        }
    }

    public static void setArrivalTime(String arrivalTime) {
        AppConfPersistence.arrivalTime = arrivalTime;
        conf.setArrivalTime(arrivalTime);
    }
    
    /**
     * 
     * @param fileName 
     */
    public static void write(String fileName) {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream ob = new ObjectOutputStream(fos);
            
            ob.writeObject(new AppConfigurations(getArrivalTime(), getSalary(), getTax()));
            
            ob.close();
            fos.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AppConfPersistence.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AppConfPersistence.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String getSalary() {
        if (salary != null && !(salary.equals(""))) {
            return conf.getSalary();
        } else {
            return "1000";
        }
    }

    public static void setSalary(String salary) {
        AppConfPersistence.salary = salary;
        conf.setSalary(salary);
    }

    public static String getTax() {
        if (tax != null && !(tax.equals(""))) {
            return conf.getTax();
        } else {
            return "100";
        }
    }

    public static void setTax(String tax) {
        AppConfPersistence.tax = tax;
        conf.setTax(tax);
    }
    
    public static void loadConfs() {
        try {
            FileInputStream fis = new FileInputStream(MainWindow.CONF_FILE_NAME);
            ObjectInputStream in = new ObjectInputStream(fis);
            
            AppConfigurations p = (AppConfigurations) in.readObject();
            
            arrivalTime = p.getArrivalTime();
            salary = p.getSalary();
            tax = p.getTax();
            
            if (arrivalTime != null || !(arrivalTime.equals(""))) {
                AppConfPersistence.setArrivalTime(arrivalTime);
            }
            
            if (salary != null || !(salary.equals(""))) {
                AppConfPersistence.setSalary(salary);
            }
            
            if (tax != null || !(tax.equals(""))) {
                AppConfPersistence.setTax(tax);
            }
            
            in.close();
            fis.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ArrivalSetter.class.getName()).log(Level.SEVERE, null, ex);
            Log.log(AppConfPersistence.class.getName(), ex.toString());
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ArrivalSetter.class.getName()).log(Level.SEVERE, null, ex);
            Log.log(AppConfPersistence.class.getName(), ex.toString());
        }
    }
    
}/** end class. */
