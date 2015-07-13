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
public class EmpInfo extends Employee implements java.io.Serializable {
    private static final long serialVersionUID = 6504992025239928528L;

    private final String dateIn;
    private final String timeIn;
    private final String timeOut;
    private final String present;
    private final String late;
    private final String loan;
    private final String advance;
    private final String arrivalTime;
    
    /**
     * ALL STRINGS.
     * @param date
     * @param name
     * @param id
     * @param location
     * @param salary 
     * @param attType 
     * @param dateIn 
     * @param timeIn 
     * @param timeOut 
     * @param present 
     * @param late 
     * @param loan 
     * @param advance 
     * @param arrivalTime 
     */
    public EmpInfo(String date, String name, String id, String location, String salary, String attType,
            String dateIn, String timeIn, String timeOut, String present, String late,
            String loan, String advance, String arrivalTime) {
        super(date, name, id, location, salary, attType);
        this.dateIn = dateIn;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.present = present;
        this.late = late;
        this.loan = loan;
        this.advance = advance;
        this.arrivalTime = arrivalTime;
    }

    public String getDateIn() {
        return dateIn;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public String getPresent() {
        return present;
    }

    public String getLate() {
        return late;
    }

    public String getLoan() {
        return loan;
    }

    public String getAdvance() {
        return advance;
    }
    
    public String getArrivalTime() {
        return this.arrivalTime;
    }
    
    /**
     * 
     * @return is present or absent boolean
     */
    public boolean isAbsent() {
        return (present != null || present.equals(""));
    }
    
    /**
     * 
     * @return 
     */
    public boolean isLate() {
        return Integer.parseInt(arrivalTime) > Integer.parseInt(timeIn);
    }
    
    /**
     * 
     * @return get time of day spent on work int
     */
    public int getTime() {
        int in = Integer.parseInt(timeIn);
        int out = Integer.parseInt(timeOut);
        return out - in;
    }
}/** end class. */
