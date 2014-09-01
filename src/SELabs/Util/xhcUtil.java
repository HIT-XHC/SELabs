package SELabs.Util;

import com.huiton.pub.dbx.*;
import com.huiton.cerp.PPC.util.Day;
import com.huiton.mainframe.util.tracer.Debug;
import java.sql.ResultSet;
import com.huiton.cerp.PPC.util.JdbOper;

public class xhcUtil
{

  public xhcUtil()
  {}

  /**
   * 该函数用来填充一个字符串：
   * String st 为要被填充的字符串；
   * String St_with 用来进行填充的字符；
   * int length填充完的字符串的长度；
   * b_or_a 在前面填充还是后面填充。
   */
  public static String fillString(String st, String St_with, int length,
                                  String b_or_a)
  {
    String st_return = st;

    int i = st_return.length();
    while (i <= length)
    {
      if (b_or_a.equals("B") || b_or_a.equals("b"))
      {
        st_return = St_with + st_return;
      }
      else
      {
        st_return = st_return + St_with;
      }

      i = i + St_with.length();
    }

    if (b_or_a.equals("B") || b_or_a.equals("b"))
    {
      st_return = st_return.substring(st_return.length() - length, st_return.length());
    }
    else
    {
      st_return = st_return.substring(0, length);
    }

    return st_return;
  }

  /**
   * 该函数用来把date类型的数据输出为符合数据库格式的字符串yyyy-mm-dd
   */
  public String dtostring(java.util.Date indate)
  {
    Day myday = new Day();
    return myday.ConvertFromDate(indate).toString();

  }

  /**
   * 该函数用来去除字符串的前后空格，如果字符串为空，则返回""。
   */

  public static String strTrim(String s)
  {
    if (s == null)
    {
      return "";
    }

    return s.trim();
  }

  /**
   * 该函数用来去除字符串(保存的是数值信息）的前后空格，如果字符串为空，则返回0。
   */
  public static String numTrim(String s)
  {
    if (s == null || s.length() < 1)
    {
      return "0";
    }

    return s.trim();
  }

  /**
   * 该函数用来向错误表中书写错误信息，便于用户进行查询。
   * @param pageQuery 数据库操作对象
   * @param errText 要书写的错误内容
   * @param errDeal 处理错误的建议方法
   *
   * */
  public static void errText(PageQuery pageQuery, String errTable, String errText,
                             String errDeal)
  {
    String sql;
    try
    {
      sql = " insert into " + errTable + " (errText,errDeal,errTime)"
            + " values ('" + errText + "','" + errDeal + "','"
            + Day.currentDateTime() + "')";
      System.out.println("sql:  " + sql );
      pageQuery.simpleUpdate(sql);
    }
    catch (Exception ex)
    {
    }

  }


  /**
   * 该函数用来向错误表中书写警告信息，便于用户进行查询。
   * @param pageQuery 数据库操作对象
   * @param warnText 要书写的警告内容
   * @param warnDeal 处理警告的建议方法
   *
   * */
  public static void warnText(PageQuery pageQuery, String warnTable, String warnText,
                             String warnDeal)
  {
    String sql;
    try
    {
      sql = " insert into " + warnTable + " (warnText,warnDeal,warnTime)"
            + " values ('" + warnText + "','" + warnDeal + "','"
            + Day.currentDateTime() + "')";
      System.out.println("sql:  " + sql );
      pageQuery.simpleUpdate(sql);
    }
    catch (Exception ex)
    {
    }

  }


  /*
   * 该函数用来生成一个单据号码。
   * @param pageQuery 数据库操作对象
   * @param userId 用户Id的前4位
   * @param Field 要检索的字段
   * @param Table 要检索的表
   */
  public static String UPNGenerator(PageQuery pageQuery, String userId,
                                    String Field, String Table)
  {
    String sql;
    final int MAX_LEN = 15;
    final int NUM_LEN = 5;
    int i;

    try
    {

      //处理用户的代码，保证是4位
      if (userId.length() < 4)
      {
        userId = xhcUtil.fillString(userId, "0", 4, "a");
      }
      else if (userId.length() > 4)
      {
        userId = userId.substring(0, 4);

      }

      sql = "select MAX(" + Field + ") as " + Field + " from " + Table +
            " where substring(" + Field + ",1,4)='" + userId + "'";
      Debug.println(sql);
      ResultSet rs = pageQuery.getData(sql);
      if (rs != null && rs.next())
      {
        String Bill_No = rs.getString(1);
        if (Bill_No == null || Bill_No.trim().length() < 1)
        {
          i = 1;
        }
        else
        {
          String num = Bill_No.trim().substring(10, MAX_LEN);
          i = Integer.parseInt(num); //得到序号
          i++;
          if (i > 99999)
          {
            i = 1;
          }
        }

        Day currentDay = new Day(); //得到日期
        String _ret = "";
        _ret = _ret + (currentDay.getYear() + "");
        if (currentDay.getMonth() < 10)
        {
          _ret += "0" + currentDay.getMonth();
        }
        else
        {
          _ret += currentDay.getMonth();

        }
        Bill_No = userId + _ret + fillString(i + "", "0", MAX_LEN - 10, "B");
        Debug.println(Bill_No);
        return Bill_No;

      }
      else
      {
        Day currentDay = new Day();
        String _ret = "";
        _ret = _ret + (currentDay.getYear() + "");
        if (currentDay.getMonth() < 10)
        {
          _ret += "0" + currentDay.getMonth();
        }
        else
        {
          _ret += currentDay.getMonth();

        }
        String Bill_No = userId + _ret + fillString("1", "0", MAX_LEN - 10, "B");
        Debug.println(Bill_No);
        return Bill_No;
      }

    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }

    return "";

  }

  /**
   * 产生的额定能力值
   * 10-20之间
   */

  public void generate_captime(PageQuery pageQuery)
  {
    String sql = "";
    ResultSet rs = null;
    double randnum = 0; //use_num=0,lead_time=0,lot_qty=0,randnum;
    int cap_qty = 10;
    String cap_code = "";

    try
    {

      //处理根节点数据
      sql = "select distinct cap_code,cap_qty from rccp_cap_def";
      Debug.println(sql);

      rs = pageQuery.getDataNew(sql);
      while (rs != null && rs.next())
      {
        cap_code = rs.getString("cap_code");
//          lead_time = 2;
        randnum = Math.random();
        if (randnum >= 0.2 && randnum <= 1)
        {
          cap_qty = (int) (randnum * 100);
          cap_qty = cap_qty - (cap_qty % 8);
        }
        else
        {
          cap_qty = 16;

        }
        sql = "update rccp_cap_def set cap_qty=" + cap_qty
              + " where cap_code='" + cap_code + "'";
        Debug.println(sql);
        pageQuery.simpleUpdate(sql);

      }

    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  /**
   * 产生产品对能力的单位需求量
   * 10-20之间
   */

  public void generate_procaptime(PageQuery pageQuery)
  {
    String sql = "";
    ResultSet rs = null;
    double randnum = 0; //use_num=0,lead_time=0,lot_qty=0,randnum;
    int req_qty = 100;
    String cap_code = "", item_code = "";

    try
    {

      //处理根节点数据
      sql = "select distinct item_code,cap_code,req_qty from rccp_capreq";
      Debug.println(sql);

      rs = pageQuery.getDataNew(sql);
      while (rs != null && rs.next())
      {
        item_code = rs.getString("item_code");
        cap_code = rs.getString("cap_code");

        randnum = Math.random();
        if (randnum >= 0.2 && randnum <= 1)
        {
          req_qty = (int) (randnum * 500);
          req_qty = req_qty - (req_qty%50);
        }
        else
        {
          req_qty = 150;

        }
        sql = "update rccp_capreq set req_qty=" + req_qty
              + " where cap_code='" + cap_code + "' and item_code='" + item_code + "'";
        Debug.println(sql);
        pageQuery.simpleUpdate(sql);

      }

    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  /**
   * 产生产品的总提前期数据
   * 10-20之间
   */

  public void generate_proleadtime(PageQuery pageQuery)
  {
    String sql = "";
    ResultSet rs = null;
    double use_num = 0, lead_time = 0, lot_qty = 0, randnum;
    String item_code = "";

    try
    {

      //处理根节点数据
      sql = "select distinct item_code from ppc_item_master_v where mps_flag='Y'"
            + " or item_type='FG'";

      Debug.println(sql);
      rs = pageQuery.getDataNew(sql);
      while (rs != null && rs.next())
      {
        item_code = rs.getString("item_code");
//          lead_time = 2;
        randnum = Math.random();
        if (randnum > 0.3)
        {
          lead_time = 12;
        }
        else if (randnum > 0.6)
        {
          lead_time = 15;
        }
        else
        {
          lead_time = 8;
        }
        lot_qty = 1;

        sql = "update ppc_item_master_v set cumulant_lt=" + lead_time
              + " where item_code='" + item_code + "'";
        Debug.println(sql);
        pageQuery.simpleUpdate(sql);

      }

    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  /**
   * 用来根据产品结构中对子件的需求数量，自动生成模拟提前期。
   * 编制数据时是使用。
   * 原则是：
   * 1）根节点提前期均设置为2-1天，批量为1。
   * 2）子件需求数量小于20的，设置为2-3天,批量设置为20。
   * 3)子件需求数量大于20的，设置为3-4天，批量设置为50。
   */

  public void generate_leadtime(PageQuery pageQuery)
  {
    String sql = "";
    ResultSet rs = null;
    double use_num = 0, lead_time = 0, lot_qty = 0;
    String item_code = "";

    try
    {

      //处理根节点数据
      sql = "select distinct parent_code from ppc_bom_v where parent_code"
            + " not in (select child_code from ppc_bom_v )";
      Debug.println(sql);
      rs = pageQuery.getData(sql);
      while (rs.next())
      {
        item_code = rs.getString("parent_code");
//          lead_time = 2;
        if (Math.random() > 0.5)
        {
          lead_time = 2;
        }
        else
        {
          lead_time = 1;
        }
        lot_qty = 1;

        sql = "update ppc_item_master_v set lead_time=" + lead_time
              + ",lot_qty=" + lot_qty + " where item_code='" + item_code + "'";
        Debug.println(sql);
        pageQuery.simpleUpdate(sql);

      }

      //处理子节点数据
      sql = "select child_code,max(use_num) max_usenum from ppc_bom_v "
            + " group by child_code";
      Debug.println(sql);
      rs = pageQuery.getData(sql);
      while (rs.next())
      {
        use_num = rs.getFloat("max_usenum");
        item_code = rs.getString("child_code");
        if (use_num < 20)
        {
          if (Math.random() > 0.5)
          {
            lead_time = 3;
          }
          else
          {
            lead_time = 2;

          }
          lot_qty = 20;
        }
        else if (use_num >= 20)
        {
          if (Math.random() > 0.5)
          {
            lead_time = 3;
          }
          else
          {
            lead_time = 4;

          }
          lot_qty = 50;
        }

        sql = "update ppc_item_master_v set lead_time=" + lead_time
              + ",lot_qty=" + lot_qty + " where item_code='" + item_code + "'";
        Debug.println(sql);
        pageQuery.simpleUpdate(sql);

      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  /**
   * 该函数用来根据传递的年份和月份给出当前年月的结束日期：
   * String year_code 为给定的年份；
   * String month_code 为给定的月份；
   */
  public static String endofmonth(int year,int month)
  {
    String end_date ="";
      if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 ||
          month == 10 || month == 12) {

        end_date = year + "-" + month + "-" + "31";
      }
      else if (month == 4 || month == 6 || month == 9 || month == 11) {
        end_date = year + "-" + month + "-" + "30";
      }
      else if ( (year % 4 != 0 || year % 100 == 0) && year % 400 != 0) {
        end_date = year + "-" + month + "-" + "28";
      }
      else if(month==2){
        end_date = year + "-" + month + "-" + "29";
      }
      else{
        return "";
      }

    return end_date.trim();
  }

  /**
   * 该函数用来维护ppc_item_master,
   * 把存在于mdm_item_master中而不存在于ppc_item_master中的
   * 自制件的产品标识等信息写入到ppc_item_master中。
   * */
  public static void ppc_item(PageQuery pageQuery)
  {
    String sql;
    try
    {
      sql = " insert into ppc_item_master (item_code,stra_no,item_name,item_spec,"
          + " item_model,unit,item_type,mp_flag,lot_policy,"
          + "lot_qty,batch_lt,cumulant_lt,batch_qty,batch_minlt,batch_flag)"
          + " (select item_code,stra_no,item_name,item_spec,"
          + " item_model,unit,item_type,mp_flag,'DIR','1','0','0','1','1',batch_flag "
          + " from ppc_item_master_v "
          + " where mp_flag = 'M' and item_code not in "
          + " (select item_code from ppc_item_master))";
      System.out.println("sql:  " + sql );
      pageQuery.simpleUpdate(sql);
    }
    catch (Exception ex)
    {
    }

  }



}
