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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author muneebahmad
 */
public class SalaryInfo implements Serializable {
    private static final long serialVersionUID = 9114210218933968579L;
    
    public static HashMap<String, String> salaryMap = new HashMap<>();
    
    public SalaryInfo() {}
    
    /**
     * 
     * @param id
     * @param salary 
     */
    public static void add(String id, String salary) {
        salaryMap.put(id, salary);
    }
    
    /**
     * 
     * @param fileName 
     */
    public static void write(String fileName) {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            
            for (int i = 0; i < salaryMap.size(); i++) {
                out.writeObject(new Salary(i + "", salaryMap.get(i + "")));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SalaryInfo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SalaryInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}/** end class. */
