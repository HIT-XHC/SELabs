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
   * �ú����������һ���ַ�����
   * String st ΪҪ�������ַ�����
   * String St_with �������������ַ���
   * int length�������ַ����ĳ��ȣ�
   * b_or_a ��ǰ����仹�Ǻ�����䡣
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
   * �ú���������date���͵��������Ϊ�������ݿ��ʽ���ַ���yyyy-mm-dd
   */
  public String dtostring(java.util.Date indate)
  {
    Day myday = new Day();
    return myday.ConvertFromDate(indate).toString();

  }

  /**
   * �ú�������ȥ���ַ�����ǰ��ո�����ַ���Ϊ�գ��򷵻�""��
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
   * �ú�������ȥ���ַ���(���������ֵ��Ϣ����ǰ��ո�����ַ���Ϊ�գ��򷵻�0��
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
   * �ú�����������������д������Ϣ�������û����в�ѯ��
   * @param pageQuery ���ݿ��������
   * @param errText Ҫ��д�Ĵ�������
   * @param errDeal �������Ľ��鷽��
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
   * �ú�����������������д������Ϣ�������û����в�ѯ��
   * @param pageQuery ���ݿ��������
   * @param warnText Ҫ��д�ľ�������
   * @param warnDeal ������Ľ��鷽��
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
   * �ú�����������һ�����ݺ��롣
   * @param pageQuery ���ݿ��������
   * @param userId �û�Id��ǰ4λ
   * @param Field Ҫ�������ֶ�
   * @param Table Ҫ�����ı�
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

      //�����û��Ĵ��룬��֤��4λ
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
          i = Integer.parseInt(num); //�õ����
          i++;
          if (i > 99999)
          {
            i = 1;
          }
        }

        Day currentDay = new Day(); //�õ�����
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
   * �����Ķ����ֵ
   * 10-20֮��
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

      //������ڵ�����
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
   * ������Ʒ�������ĵ�λ������
   * 10-20֮��
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

      //������ڵ�����
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
   * ������Ʒ������ǰ������
   * 10-20֮��
   */

  public void generate_proleadtime(PageQuery pageQuery)
  {
    String sql = "";
    ResultSet rs = null;
    double use_num = 0, lead_time = 0, lot_qty = 0, randnum;
    String item_code = "";

    try
    {

      //������ڵ�����
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
   * �������ݲ�Ʒ�ṹ�ж��Ӽ��������������Զ�����ģ����ǰ�ڡ�
   * ��������ʱ��ʹ�á�
   * ԭ���ǣ�
   * 1�����ڵ���ǰ�ھ�����Ϊ2-1�죬����Ϊ1��
   * 2���Ӽ���������С��20�ģ�����Ϊ2-3��,��������Ϊ20��
   * 3)�Ӽ�������������20�ģ�����Ϊ3-4�죬��������Ϊ50��
   */

  public void generate_leadtime(PageQuery pageQuery)
  {
    String sql = "";
    ResultSet rs = null;
    double use_num = 0, lead_time = 0, lot_qty = 0;
    String item_code = "";

    try
    {

      //������ڵ�����
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

      //�����ӽڵ�����
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
   * �ú����������ݴ��ݵ���ݺ��·ݸ�����ǰ���µĽ������ڣ�
   * String year_code Ϊ��������ݣ�
   * String month_code Ϊ�������·ݣ�
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
   * �ú�������ά��ppc_item_master,
   * �Ѵ�����mdm_item_master�ж���������ppc_item_master�е�
   * ���Ƽ��Ĳ�Ʒ��ʶ����Ϣд�뵽ppc_item_master�С�
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
