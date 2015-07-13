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

package pk.muneebahmad.client.pioneer.model;

import java.io.Serializable;

/**
 *
 * @author muneebahmad
 */
public class PersonInfo implements Serializable {
    private static final long serialVersionUID = 2511998018738378958L;

    
    private String id;
    private String name;
    private String salary;
    private String location;
    private String attType;
    private String arrivalTime;
    private String tax;
    private String loan;
    private String days;
    private String inTime;
    private String outTime;
    
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
     */
    public PersonInfo(String id, String name, String salary, String location, String attType,
            String arrivalTime, String tax, String loan, String days, String inTime, String outTime) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.location = location;
        this.attType = attType;
        this.arrivalTime = arrivalTime;
        this.tax = tax;
        this.loan = loan;
        this.days = days;
        this.inTime = inTime;
        this.outTime = outTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAttType() {
        return attType;
    }

    public void setAttType(String attType) {
        this.attType = attType;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getLoan() {
        return loan;
    }

    public void setLoan(String loan) {
        this.loan = loan;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getInTime() {
        return inTime;
    }
    
    public void setIntime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }
    
    /**
     * 
     * @param date {@link #String}
     * @return if this Objects date is greater than the one provided in parameter.
     * 
     * Date formate = yyyy/mm/dd
     */
    public boolean isDateGreaterThan(String date) {
        int d = 0;
        int m = 0;
        int y = 0;
        if (this.days != null) {
            String tokens[] = this.days.split("/");
            y = Integer.parseInt(tokens[0]);
            m = Integer.parseInt(tokens[1]);
            d = Integer.parseInt(tokens[2]);
        }
        
        int dd = 0;
        int mm = 0;
        int yy = 0;
        
        if (date != null) {
            String tokens[] = date.split("/");
            yy = Integer.parseInt(tokens[0]);
            mm = Integer.parseInt(tokens[1]);
            dd = Integer.parseInt(tokens[2]);
        }
        
        return (d > dd) && (m > mm)  && (y > yy);
    }
    
    /**
     * 
     * @param names
     * @return boolean wether the name of this object is equal to given name.
     */
    public boolean isNameEqualTo(String names) {
        if (this.name != null)
            return this.name.equals(names);
        else 
            return false;
    }
    
    /**
     * 
     * @return final Salary in {@link #String} after deduction tax and loan.
     */
    public String getFinalSalary() {
        int finSal = 0;
        String finalSal = "0";
        if ((this.tax != null) && (this.salary != null) && (this.loan != null)) {
            finSal = Integer.parseInt(this.salary) - Integer.parseInt(this.tax) - Integer.parseInt(this.loan);
            finalSal = finSal + "";
        } else {
            finalSal = "0";
        }
        return finalSal;
    }
    
    /**
     * 
     * @return {@link #String} wether this object is late or not. Set arrival time and in time before accessing method.
     */
    public String getLate() {
        String finalArl = "Arrived Late";
        String tokens1[] = null;
        String tokens2[] = null;
        String tm1 = "0";
        String tm2 = "0";
        String in1 = "0";
        String in2 = "0";
        if ((this.arrivalTime != null) && (this.inTime != null)) {
            tokens1 = arrivalTime.split(":");
            tokens2 = inTime.split(":");
            
            tm1 = tokens1[0];
            tm2 = tokens1[1];
            in1 = tokens2[0];
            in2 = tokens2[1];
            
            if (((Integer.parseInt(tm1) - Integer.parseInt(in1)) > 0) && 
                    ((Integer.parseInt(tm2) - Integer.parseInt(in2)) > 0)) {
                finalArl = "Arrived at time";
            } else {
                finalArl = "Late";
            }
        }
        return finalArl;
    }
    
    /**
     * 
     * @return 
     */
    public String getHours() {
        String hours = "0";
        String minutes = "0";
        
        int hrs = 0;
        int mins = 0;
        String tokens1[] = null;
        String tokens2[] = null;
        String tm1 = "0";
        String tm2 = "0";
        String in1 = "0";
        String in2 = "0";
        if ((this.outTime != null) && (this.inTime != null)) {
            tokens1 = outTime.split(":");
            tokens2 = inTime.split(":");
            
            tm1 = tokens1[0];
            tm2 = tokens1[1];
            in1 = tokens2[0];
            in2 = tokens2[1];
            
            hrs = Integer.parseInt(tm1) - Integer.parseInt(in1); 
            mins =Integer.parseInt(tm2) - Integer.parseInt(in2);
            
            hours = hrs + "";
            minutes = mins + "";
        }
        return hours + ":" + minutes;
    }
    
    public String getAttendence() {
        int it = 0;
        String att = "Absent";
        String itm = null;
        String its = null;
        String otm = null;
        String ots = null;
        String tokens[] = null;
        String tokens2[] = null;
        
        int itms = 0;
        int itss = 0;
        int otms = 0;
        int otss = 0;
        if (this.inTime != null && this.outTime != null) {
            tokens = this.inTime.split(":");
            itm = tokens[0];
            its = tokens[1];
            
            tokens2 = outTime.split(":");
            otm = tokens2[0];
            ots = tokens2[1];
            
            itms = Integer.parseInt(itm);
            itss = Integer.parseInt(its);
            otms = Integer.parseInt(otm);
            otss = Integer.parseInt(ots);
            
            if (itms != 0 && itms > 0) {
                att = "Present";
            } 
        }
        return att;
    }
    
}/** end class. */
