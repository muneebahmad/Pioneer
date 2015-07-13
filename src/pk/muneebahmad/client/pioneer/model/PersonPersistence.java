/**
 * copyright ©2013-2014 ®Algorithmi™.
 *
 * @author ¶muneebahmad¶ (ahmadgallian@yahoo.com) NetBeans IDE
 * http://www.netbeans.org
 *
 * For all entities this program is free software; you can redistribute it
 * and/or modify it under the terms of the 'MyGdxEngine' license with the
 * additional provision that 'MyGdxEngine' must be credited in a manner that can
 * be be observed by end users, for example, in the credits or during start up.
 * (please find MyGdxEngine logo in sdk's logo folder)
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
 * The following source - code IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 * KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO
 * EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES
 * OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 * *
 */
package pk.muneebahmad.client.pioneer.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import pk.muneebahmad.client.pioneer.util.Log;

/**
 *
 * @author muneebahmad
 */
public class PersonPersistence {

    public static ArrayList<PersonInfo> pInfoList = new ArrayList<>();
    public static ArrayList<PersonInfo> pInfoList2 = new ArrayList<>();

    public PersonPersistence() {

    }

    /**
     *
     * @param id
     * @param name
     * @param salary
     * @param location
     * @param attType
     * @param arrivalTime
     * @param tax
     * @param loan
     * @param days
     * @param inTime
     * @param outTime
     * @param notFromFile
     */
    public static void add(String id, String name, String salary, String location, String attType,
            String arrivalTime, String tax, String loan, String days, String inTime, String outTime, boolean notFromFile) {
        if (pInfoList.size() > 0) {
           PersonInfo p = new PersonInfo(id, name, salary, location, attType, arrivalTime, tax, loan, days, inTime, outTime);
           if (searchForName(id, name)) {
               Log.log(PersonPersistence.class.getName(), "The person with name: [" + name + "] and id: [" + id + "] already exist! "
                       + "Ignoring this one.");
           } else {
               pInfoList.add(p);
           }
        } else if (pInfoList.size() <= 0) {
            pInfoList.add(new PersonInfo(id, name, salary, location, attType,
                            arrivalTime, tax, loan, days, inTime, outTime));
        }
    }

    /**
     *
     * @param id
     * @param name
     * @return pInfo {@link #PersonInfo}
     * @throws java.lang.NullPointerException
     */
    public static PersonInfo search(String id, String name) throws java.lang.NullPointerException {
        PersonInfo p = null;
        for (int i = 0; i < pInfoList.size(); i++) {
            if ((id.equals(pInfoList.get(i).getId())) && (name.equals(pInfoList.get(i).getName()))) {
                p = pInfoList.get(i);
                break;
            } else {
                p = null;
            }
        }
        return p;
    }
    
    /**
     * 
     * @param id {@link #String}
     * @param name
     * @return boolean
     */
    public static boolean searchForName(String id, String name) {
        boolean found = false;
        if (pInfoList.size() > 0) {
            for (int i = 0; i < pInfoList.size(); i++) {
                if ((id.equals(pInfoList.get(i).getId())) && (pInfoList.get(i).isNameEqualTo(name))) {
                    found = true;
                    break;
                } else {
                    found = false;
                }
            }
        }
        return found;
    }

    /**
     *
     * @param name
     * @return
     */
    public static boolean isContainedInList(String name) {
        boolean contains = false;
        for (int i = 0; i < pInfoList.size(); i++) {
            if (pInfoList.get(i).isNameEqualTo(name)) {
                contains = true;
                break;
            } else {
                contains = false;
            }
        }

        return contains;
    }

    /**
     *
     * @param name
     * @return
     * @throws java.lang.NullPointerException
     */
    public static String countDays(String name) throws java.lang.NullPointerException {
        String days = "0";
        int d = 0;
        for (int i = 0; i < pInfoList.size(); i++) {
            if (pInfoList.get(i).isNameEqualTo(name)) {
                d++;
            }
        }
        days = d + "";
        return days;
    }

    /**
     *
     * @param name
     * @return
     * @throws java.lang.NullPointerException
     */
    public static String countHours(String name) throws java.lang.NullPointerException {
        String tokens[] = null;
        String hours = "0";
        String hour = null;
        String mins = null;
        int hrs = 0;
        int finalHrs = 0;
        int m = 0;
        int finalm = 0;
        for (int i = 0; i < pInfoList.size(); i++) {
            if (pInfoList.get(i).isNameEqualTo(name)) {
                tokens = pInfoList.get(i).getHours().split(":");
                hour = tokens[0];
                mins = tokens[1];
                hrs = Integer.parseInt(hour);
                m = Integer.parseInt(mins);
                finalHrs += hrs;
                finalm += m;
                hours = finalHrs + " : " + finalm;
            } else {
                hours = "0";
            }
        }
        return hours;
    }

    /**
     *
     * @param id
     * @param name
     */
    public static void delete(String id, String name) {
        for (int i = 0; i < pInfoList.size(); i++) {
            if ((id.equals(pInfoList.get(i).getId())) && (name.equals(pInfoList.get(i).getName()))) {
                pInfoList.remove(i);
                break;
            }
        }
    }

    /**
     *
     * @param fileName
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public static void save(String fileName) throws FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream out = new ObjectOutputStream(fos);

        for (int i = 0; i < pInfoList.size(); i++) {
            out.writeObject(pInfoList.get(i));
        }

        out.close();
        fos.close();
    }

    /**
     *
     * @param fileName
     * @throws FileNotFoundException
     * @throws IOException
     * @throws java.lang.ClassNotFoundException
     */
    public static void load(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(fileName);
        ObjectInputStream in = new ObjectInputStream(fis);

        PersonInfo p = (PersonInfo) in.readObject();

        while (p != null) {
            add(p.getId(), p.getName(), p.getSalary(), p.getLocation(), p.getAttType(), 
                    p.getArrivalTime(), p.getTax(), p.getLoan(), p.getDays(), p.getInTime(), p.getOutTime(), true);
            pInfoList2.add(p);
            p = (PersonInfo) in.readObject();
        }
        in.close();
        fis.close();
    }

}
/**
 * end class.
 */
