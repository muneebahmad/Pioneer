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

/**
 *
 * @author muneebahmad
 */
public class EmployeeData {

    private String name;
    private String id;
    private String salary;
    private String location;
    private String attType;
    private String arrivalTime;
    private String tax;
    private String loan;
    private String days;
    
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
    public EmployeeData(String name, String id, String salary, String location, String attType, 
            String arrivalTime, String tax, String loan, String days) {
        this.name = name;
        this.id = id;
        this.salary = salary;
        this.location = location;
        this.attType = attType;
        this.arrivalTime = arrivalTime;
        this.tax = tax;
        this.loan = loan;
        this.days = days;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
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

    public String getLocation() {
        return location;
    }

    public String getAttType() {
        return attType;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setAttType(String attType) {
        this.attType = attType;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    
    
}/** end class. */
