package SELabs.Util;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

/**
   Stores dates and perform date arithmetic.

   This is another date class, but more convenient that
   <tt>java.util.Date</tt> or <tt>java.util.Calendar</tt>

   @version 1.20 5 Oct 1998
   @author Cay Horstmann
*/

public class Day implements Cloneable, Serializable
{  /**
      Constructs today's date
    */

   public Day()
   {  GregorianCalendar todaysDate
         = new GregorianCalendar();
      year = todaysDate.get(Calendar.YEAR);
      month = todaysDate.get(Calendar.MONTH) + 1;
      day = todaysDate.get(Calendar.DAY_OF_MONTH);
   }

   /**
      Constructs a specific date

      @param yyyy year (full year, e.g., 1996,
         <i>not</i> starting from 1900)
      @param m month
      @param d day
      @exception IllegalArgumentException if yyyy m d not a
         valid date
    */

   public Day(int yyyy, int m, int d)
   {  year = yyyy;
      month = m;
      day = d;
      if (!isValid())
         throw new IllegalArgumentException();
   }

   public Day(int yyyy, int m, int d,boolean test)
     {  year = yyyy;
        month = m;
        day = d;
        if(test)
            if (!isValid())
               throw new IllegalArgumentException();
     }

   /**
      Advances this day by n days. For example.
      d.advance(30) adds thirdy days to d

      @param n the number of days by which to change this
         day (can be < 0)
    */

   public void advance(int n)
   {  fromJulian(toJulian() + n);
   }

   /**
      Gets the day of the month

      @return the day of the month (1...31)
    */

   public int getDay()
   {  return day;
   }

   /**
      Gets the month

      @return the month (1...12)
    */

   public int getMonth()

   { return month;
   }

   /**
      Gets the year

      @return the year (counting from 0, <i>not</i> from 1900)
    */

   public int getYear()
   { return year;
   }

   public void setDay(int m) {
        day = m;
    }

    public void setMonth(int m) {
        month = m;
    }

    public void setYear(int m) {
        year = m;
    }
   /**
      Gets the weekday
      @return the weekday ({@link Day#SUNDAY}, ...,
         {@link Day#SATURDAY})
    */

   public int weekday()
   {  return (toJulian() + 1) % 7 + 1;
   }

   /**
      The number of days between this and day parameter

      @param b any date
      @return the number of days between this and day parameter
         and b (> 0 if this day comes after b)
    */

   public int daysBetween(Day b)
   {  return toJulian() - b.toJulian();
   }

   /**
      A string representation of the day

      @return a string representation of the day
    */

   public String toString()
   {
                String s = new String();
                s = String.valueOf(year);
                s = s + "-";
                s = s + String.valueOf(month);
                s = s + "-";
                s = s + String.valueOf(day);
                return s;

   }

   /**
      Makes a bitwise copy of a Day object

      @return a bitwise copy of a Day object
    */

   public Object clone()
   {  try
      {  return super.clone();
      } catch (CloneNotSupportedException e)
      {  // this shouldn't happen, since we are Cloneable
         return null;
      }
   }

   /**
      Compares this Day against another object

      @param obj another object
      @return true if the other object is identical to this Day object
    */

   public boolean equals(Object obj)
   {  if (!getClass().equals(obj.getClass())) return false;
      Day b = (Day)obj;
      return day == b.day && month == b.month && year == b.year;
   }

   /**
      Computes the number of days between two dates

      @return true iff this is a valid date
    */

   private boolean isValid()
   {  Day t = new Day();
      t.fromJulian(this.toJulian());
      return t.day == day && t.month == month
         && t.year == year;
   }

   /**
      @return The Julian day number that begins at noon of
      this day
      Positive year signifies A.D., negative year B.C.
      Remember that the year after 1 B.C. was 1 A.D.

      A convenient reference point is that May 23, 1968 noon
      is Julian day 2440000.

      Julian day 0 is a Monday.

      This algorithm is from Press et al., Numerical Recipes
      in C, 2nd ed., Cambridge University Press 1992
    */

   private int toJulian()
   {  int jy = year;
      if (year < 0) jy++;
      int jm = month;
      if (month > 2) jm++;
      else
      {  jy--;
         jm += 13;
      }
      int jul = (int) (java.lang.Math.floor(365.25 * jy)
      + java.lang.Math.floor(30.6001*jm) + day + 1720995.0);

      int IGREG = 15 + 31*(10+12*1582);
         // Gregorian Calendar adopted Oct. 15, 1582

      if (day + 31 * (month + 12 * year) >= IGREG)
         // change over to Gregorian calendar
      {  int ja = (int)(0.01 * jy);
         jul += 2 - ja + (int)(0.25 * ja);
      }
      return jul;
   }
/*
   /**
      Converts a Julian day to a calendar date

      This algorithm is from Press et al., Numerical Recipes
      in C, 2nd ed., Cambridge University Press 1992

      @param j  the Julian date
    */

   private void fromJulian(int j)
   {  int ja = j;

      int JGREG = 2299161;
         /* the Julian date of the adoption of the Gregorian
            calendar
         */

      if (j >= JGREG)
      /* cross-over to Gregorian Calendar produces this
         correction
      */
      {  int jalpha = (int)(((float)(j - 1867216) - 0.25)
             / 36524.25);
         ja += 1 + jalpha - (int)(0.25 * jalpha);
      }
      int jb = ja + 1524;
      int jc = (int)(6680.0 + ((float)(jb-2439870) - 122.1)
          /365.25);
      int jd = (int)(365 * jc + (0.25 * jc));
      int je = (int)((jb - jd)/30.6001);
      day = jb - jd - (int)(30.6001 * je);
      month = je - 1;
      if (month > 12) month -= 12;
      year = jc - 4715;
      if (month > 2) --year;
      if (year <= 0) --year;
   }


    /***************************************************************************/
        //����****��**��**�ո�ʽ��String��ʱ��
        /***************************************************************************/
        public static String StringInChinese(Day t) {
                String s = new String();
                s = String.valueOf(t.getYear());
                s = s + "��";
                s = s + String.valueOf(t.getMonth());
                s = s + "��";
                s = s + String.valueOf(t.getDay());
                s = s + "��";
                return s;
        }
        /***************************************************************************/

    /***************************************************************************/
        //��Date���͵�ʱ�����ת����day����
        /***************************************************************************/
        public static Day ConvertFromDate(Date date) {
                Day my_date = null;
                if (date != null)
                {
                    my_date = new Day();
                    Calendar c = Calendar.getInstance();
                    c.setTime(date);
                    my_date.setYear(c.get(Calendar.YEAR));
                    my_date.setMonth(c.get(Calendar.MONTH)+1);
                    my_date.setDay(c.get(Calendar.DAY_OF_MONTH));
                }
                return my_date;
        }
        /***************************************************************************/
  /***************************************************************************/
        //��yyyy-mm-dd���ͻ���yyyy/mm/dd��Stringת��ΪDay
        //�����ʽ���Ϸ����򷵻�myDate(0,0,0)
        /***************************************************************************/
        public static Day StringToDay(String s)
        {
                int yy = 1900;
                int mm = 1;
                int dd = 1;
                String tmpStr;

                //����YYYY/mm/dd���͵�����
                StringTokenizer st = new StringTokenizer(s, "/", false);
                if (st.hasMoreTokens())
                {   tmpStr=st.nextToken();
                    if (!tmpStr.equals(s))
                    {
                      yy = Integer.parseInt(tmpStr);
                      if (st.hasMoreTokens())
                      {
                        mm = Integer.parseInt(st.nextToken());
                        if (st.hasMoreTokens())
                        {
                            dd = Integer.parseInt(st.nextToken());
                            Day tt = new Day(yy, mm, dd);
                            return  tt;
                        }
                      }
                    }
                 }

                 //����YYYY-mm-dd���͵�����
                 StringTokenizer st1 = new StringTokenizer(s, "-", false);
                 if (st1.hasMoreTokens())
                 {   tmpStr=st1.nextToken();
                     if (!tmpStr.equals(s))
                     {
                       yy = Integer.parseInt(tmpStr);
                       if (st1.hasMoreTokens())
                       {
                         mm = Integer.parseInt(st1.nextToken());
                         if (st1.hasMoreTokens())
                         {
                             dd = Integer.parseInt(st1.nextToken());
                             Day tt = new Day(yy, mm, dd);
                             return  tt;
                         }
                       }
                     }
                  }


                Day tt = new Day(0, 0, 0);
                return tt;
        }

/*****************************************************************************/

        /**
         * �ú���������day����ת����Date����
         */

        public Date DaytoDate()  {
//          int yyyy=year;
 //         int mm=month;
 //         int dd=day;

          Calendar cal;//�û�����
          cal = new GregorianCalendar();
          cal.set(year,month - 1,day);
          return  cal.getTime();
        }

        /**
         * �ú��������õ���ǰ����(����ʱ��)
         */

        public static String currentDateTime()  {
//          int yyyy=year;
 //         int mm=month;
 //         int dd=day;

          java.util.Date curr_cal = new java.util.Date();
          SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//����DateFormat��
          String audit_date  = formatter.format(curr_cal);
          return audit_date;
        }




   public static int SUNDAY = 1;
   public static int MONDAY = 2;
   public static int TUESDAY = 3;
   public static int WEDNESDAY = 4;
   public static int THURSDAY = 5;
   public static int FRIDAY = 6;
   public static int SATURDAY = 7;

   /** @serial */
   private int day;
   /** @serial */
   private int month;
   /** @serial */
   private int year;
}