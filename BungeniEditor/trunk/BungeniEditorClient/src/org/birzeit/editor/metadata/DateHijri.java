/*
 * Copyright (C) 2012 bzuadmin
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.birzeit.editor.metadata;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author bzuadmin
 */
public class DateHijri { 
      
    private static double gmod(double n,double  m) {  
        return ((n % m) + m) % m;  
    }  
    
    private static double[] kuwaiticalendar(Date dayDate) {  
        boolean adjust = true;
        Calendar cal = Calendar.getInstance(); 
        cal.setTime(dayDate);
        
        int adj=0;  
        if(adjust){  
            adj=0;  
        }else{  
            adj=1;  
        }  
          
        if (adjust) {  
            int adjustmili = 1000 * 60 * 60 * 24 * adj;  
            long todaymili = cal.getTimeInMillis() + adjustmili;  
            cal.setTimeInMillis(todaymili);  
        }  
        double day = cal.get(Calendar.DAY_OF_MONTH);  
        double  month = cal.get(Calendar.MONTH);  
        double  year = cal.get(Calendar.YEAR);  
          
        double  m = month + 1;  
        double  y = year;  
        if (m < 3) {  
            y -= 1;  
            m += 12;  
        }  
  
        double a = Math.floor(y / 100.);  
        double b = 2 - a + Math.floor(a / 4.);  
      
        if (y < 1583)  
            b = 0;  
        if (y == 1582) {  
            if (m > 10)  
                b = -10;  
            if (m == 10) {  
                b = 0;  
                if (day > 4)  
                    b = -10;  
            }  
        }  
  
        double jd = Math.floor(365.25 * (y + 4716)) + Math.floor(30.6001 * (m + 1)) + day  
                + b - 1524;  
          
        b = 0;  
        if (jd > 2299160) {  
            a = Math.floor((jd - 1867216.25) / 36524.25);  
            b = 1 + a - Math.floor(a / 4.);  
        }  
        double bb = jd + b + 1524;  
        double cc = Math.floor((bb - 122.1) / 365.25);  
        double dd = Math.floor(365.25 * cc);  
        double ee = Math.floor((bb - dd) / 30.6001);  
        day = (bb - dd) - Math.floor(30.6001 * ee);  
        month = ee - 1;  
        if (ee > 13) {  
            cc += 1;  
            month = ee - 13;  
        }  
        year = cc - 4716;  
  
        double wd = gmod(jd + 1, 7) + 1;  
  
        double iyear = 10631. / 30.;  
        double epochastro = 1948084;  
        double epochcivil = 1948085;  
  
        double shift1 = 8.01 / 60.;  
  
        double z = jd - epochastro;  
        double cyc = Math.floor(z / 10631.);  
        z = z - 10631 * cyc;  
        double j = Math.floor((z - shift1) / iyear);  
        double iy = 30 * cyc + j;  
        z = z - Math.floor(j * iyear + shift1);  
        double im = Math.floor((z + 28.5001) / 29.5);  
        if (im == 13)  
            im = 12;  
        double id = z - Math.floor(29.5001 * im - 29);  
  
        double[]  myRes = new double[8];  
  
        myRes[0] = day; // calculated day (CE)  
        myRes[1] = month - 1; // calculated month (CE)  
        myRes[2] = year; // calculated year (CE)  
        myRes[3] = jd - 1; // julian day number  
        myRes[4] = wd - 1; // weekday number  
        myRes[5] = id; // islamic date  
        myRes[6] = im - 1; // islamic month  
        myRes[7] = iy; // islamic year  
  
        return myRes;  
    }  
    public static String writeIslamicDate(Date dayDate) {  
        String[] wdNames = {"Ahad", "Ithnin", "Thulatha", "Arbaa", "Khams",  
                "Jumuah", "Sabt"};  
        String[] iMonthNames = {"Muharram", "Safar", "Rabi'ul Awwal",  
                "Rabi'ul Akhir", "Jumadal Ula", "Jumadal Akhira", "Rajab",  
                "Sha'ban", "Ramadan", "Shawwal", "Dhul Qa'ada", "Dhul Hijja"};  
        // This Value is used to give the correct day +- 1 day  
         
        double[] iDate = kuwaiticalendar(dayDate);  
        String outputIslamicDate = wdNames[(int) iDate[4]] + ", " + iDate[5] + " "  
                + iMonthNames[(int) iDate[6]] + " " + iDate[7] + " AH";  
          
        int year = (int)iDate[7];
        int month = (int)iDate[6];
        int day = (int)iDate[5];
        
        String hijriDate = year + "-" + month + "-" + day;
        return hijriDate;  
    }  
}
