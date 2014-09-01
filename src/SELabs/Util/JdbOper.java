package SELabs.Util;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */
import java.sql.*;
import java.util.HashMap;
import java.util.Vector;

public class JdbOper {

  private Connection con;
  //private Statement statement;
  private String m_errMsg;
  public Statement m_statement;
  boolean autoCommit = true;

  /**
   * @Handler��
   * @ģ�飺
   * @���ܣ��������ݿ⣬��ʼ�����ݲ�������JdbOper
   * @���ߣ�cameran
   * @���ڣ�2002-5-16
  */
  public JdbOper()
  {
  try {
    Class.forName("com.mysql.jdbc.Driver");
    con = DriverManager.getConnection("jdbc:mysql://localhost/ad?useUnicode=true&characterEncoding=GBK&jdbcCompliantTruncation=false",  
            "root", "a12345");
    Statement m_statement=con.createStatement() ;
    }
    catch (Exception e) {
      e.printStackTrace();
    }

  }

    /**
   * @Handler��
   * @ģ�飺
   * @���ܣ��������ݿ⣬��ʼ�����ݲ�������JdbOper
   * @���ߣ�cameran
   * @���ڣ�2002-5-16
  */
  public JdbOper(String ipaddress,String dbname,String user,String password)
  {
  try {
    Class.forName("com.inet.tds.TdsDriver");
    con = DriverManager.getConnection("jdbc:inetdae:" + ipaddress + "?database="
                                      + dbname + "&charset=GBK",user,password);
    Statement m_statement=con.createStatement() ;
    }
    catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * ���ɽ����
   * Description: �����ε��ñ����ܽ�ֻ�ܱ��浱ǰ�����
   * @param mySql: ���ڲ�������������
   */
   public ResultSet getData(String mySql) throws Exception {
        m_errMsg = "";
    try {
       // System.out.println("Native Form: " + m_conn.nativeSQL(mySql));

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(mySql);

        return rs;
    }
    catch (Exception e) {
      m_errMsg =this.getClass().getName() + ".getData(): " + e;
  //    return null;
      System.out.println(m_errMsg+":\n" + mySql);
      throw new Exception(m_errMsg);
   }
  }

  /**
   * ���ɽ����
   * Description: ������ʹ���µĻỰ���ؽ������
   * @param mySql: ���ڲ�������������
   */
   public ResultSet getDataNew(String mySql) {
        m_errMsg = "";
    try {
       // System.out.println("Native Form: " + m_conn.nativeSQL(mySql));
      Statement st =con.createStatement();
      ResultSet rs = st.executeQuery(mySql);

      return rs;
    }
    catch (Exception e) {
      m_errMsg =this.getClass().getName() + ".getDataNew(): " + e;
      System.out.println(m_errMsg);
      return null;
   }
  }
   /**
   * �޸�һ����¼���÷��������������º�ɾ����¼
   * @param  String sql ���в�����sql���
   * @return boolean �����ɹ����� TRUE�����򷵻� FALSE
   */
    public boolean simpleUpdate (String sql){

    m_errMsg = "";
    try  {
            Statement statement = con.createStatement() ;
            int i=statement.executeUpdate(sql);
            System.out.println(String.valueOf(i));
           //statement.close();
            return true;
        }
      catch(Exception e)
        {
          e.printStackTrace();
          m_errMsg =this.getClass().getName() + ".exeSql(): " + e;
          System.out.println(m_errMsg);
         return false;
        }
    }
    /**
    * ����һ����¼
    * @return boolean �����ɹ����� TRUE�����򷵻� FALSE
    * @param String table ����
    * @param String[] fields �ֶ���������
    * @param String[] types �ֶ��������飬����char, varchar���ͣ���Ӧֵ��string���������Ϳ�Ϊ����ֵ
    * @param String[] values �ֶ�����Ӧ��ֵ������
    */
    public boolean insert(String table,
                          String[] fields,
                          String[] values) throws SQLException, Exception
    {
         m_errMsg = "";
         if(fields.length!=values.length)
           throw new Exception("The numerber of fields and values must be identical.");

         Statement statement = con.createStatement();

         String strFields="", strValues="";

         for(int i=0;i<fields.length;i++) {
             strFields += fields[i] + ", ";
             strValues += "'" + values[i] + "', ";
         }

         strFields = strFields.substring(0, strFields.length() - 2);
         strValues = strValues.substring(0, strValues.length() - 2);
         String mySql = "INSERT INTO " + table + "("
                      + strFields + ") VALUES(" + strValues + ")";
         // System.out.println(mySql);
         statement.execute(mySql);

         return true;
   }

   /**
   * �޸�һ����¼���÷��������������º�ɾ����¼
   * @param  String sql ���в�����sql���
   * @return boolean �����ɹ����� TRUE�����򷵻� FALSE
   */
    public boolean commit()
    {
     try  {
            con.commit();
            return true;
        }
      catch(Exception e)
        {
          e.printStackTrace();
         return false;
        }
    }
   /**
   * ����һ����¼
   * @return boolean �����ɹ����� TRUE�����򷵻� FALSE
   * @param String criteria ɸѡ����
   * @param String table ����
   * @param String[] fields �ֶ���������
   * @param String[] types �ֶ��������飬����char, varchar���ͣ���Ӧֵ��string���������Ϳ�Ϊ����ֵ
   * @param String[] values �ֶ�����Ӧ��ֵ������
   */
   public boolean update(String table,
                              String criteria,
                              String[] fields,
                              String[] values){

        m_errMsg = "";
        try {
             if(fields.length!=values.length)
                throw new Exception("The numerber of fields and values must be identical.");

             Statement statement = con.createStatement();

             String strFields="";
             for(int i=0;i<fields.length;i++) {
                 strFields += fields[i] + " = '" + values[i] + "', ";
             }
             strFields = strFields.substring(0, strFields.length() - 2);

             if(criteria.equals("")) criteria = " 1=1 ";
             System.out.println("UPDATE " + table + " SET " + strFields + " WHERE " + criteria);
             statement.execute("UPDATE " + table + " SET " + strFields + " WHERE " + criteria);

             return true;
         }
         catch(Exception e) {
             m_errMsg =this.getClass().getName() + ".update(): " + e;
             System.out.println(e.toString());
             return false;
        }
   }


        /**
         * ɾ��һ����¼
         * @return boolean �����ɹ����� TRUE�����򷵻� FALSE
         * @param String table ����
         * @param String criteria ɸѡ����
         */
        public boolean delete(String table, String criteria)
        {
        m_errMsg = "";
        try {

             Statement statement = con.createStatement();
             statement.execute("DELETE FROM " + table + " WHERE " + criteria);

             return true;
        }
        catch(Exception e) {
             System.out.println(e.toString());
             return false;
        }
   }




   /**
   * �ر����ݿ��������
   * @param
   * @return
   */
    public boolean close()
    {
     try  {
            con.close();
            return true;
        }
      catch(Exception e)
        {
          e.printStackTrace();
         return false;
        }
    }

 /**
   * �������ת��ΪVector������������һά�ַ���������ɡ����������Ϊ���ֶ���
   * @param Rst:Ҫת���Ľ����
   * @return �ɹ��Ƿ������������򷵻�null;
   */
  public Vector rst2Vector(ResultSet rst) throws Exception {
  try {
    Vector vRst = new Vector();

     //��ȡ��ͷ��Ϣ
     ResultSetMetaData rstm = rst.getMetaData();
//System.out.println("getmeta");
     int colcount = rstm.getColumnCount(), i;
     String colname[] = new String[colcount];
     //�γ��ֶ�������
     for (i=1; i<=colcount; i++)
       colname[i-1] = rstm.getColumnName(i);    //�ֶ�����1��ʼ������Ӧ��0��ʼ
     vRst.addElement(colname);    //�ֶ�������λ��0λ��
//System.out.println("cr field");

     //�γɼ�¼��,��ʸ��������1��ʼ
     rst.beforeFirst();
     while (rst.next())
       {String row[] = new String[colcount];
        for (i=0; i<colcount; i++)
          {row[i] = rst.getString(i+1);
           if (row[i]==null)
             row[i] = "";
          }
//System.out.println(row[0]);
        vRst.addElement(row);
       }

    return vRst;
  }catch (Exception e)  {
    return null;
  }

  }


 /**
   * �������ת��ΪVector������������һά�ַ���������ɡ�ͨ����������ѡ�����ɻ������ֶ���ͷ��
   * @param rst Ҫת���Ľ����
   * @param hasTitle
   * @return �ɹ�ʱ�������������򷵻�null;
   * @throws Exception �׳����д���
   */
  public Vector rst2Vector(ResultSet rst, boolean hasTitle) throws Exception {
    Vector vc = rst2Vector(rst);
    if (!hasTitle)
      vc.remove(0);
    return vc;
  }

  /**
   * �������ת��ΪVector������������HashMap������ɡ�ÿ��HashMap�������ֶ�����Ϊ��ֵ���ֶ�ֵ�ַ�����Ϊֵ
   * @param rst Ҫת���Ľ����
   * @return �ɹ�ʱ�������������򷵻�null;
   * @throws Exception �׳����д���
   */
  public Vector rst2HashVector(ResultSet rst) {
  try {
    Vector vRst = new Vector();

     //��ȡ��ͷ��Ϣ
     ResultSetMetaData rstm = rst.getMetaData();
//System.out.println("getmeta");
     int colcount = rstm.getColumnCount(), i;
     String colname[] = new String[colcount];
     //�γ��ֶ�������
     for (i=1; i<=colcount; i++)
       colname[i-1] = rstm.getColumnName(i);    //�ֶ�����1��ʼ������Ӧ��0��ʼ
//System.out.println("cr field");

     //�γɼ�¼��,��ʸ��������1��ʼ
     rst.beforeFirst();
     while (rst.next())
       {HashMap hm = new HashMap();
        for (i=0; i<colcount; i++)
          hm.put(colname[i],rst.getString(i+1));
        vRst.addElement(hm);
       }
    rst.beforeFirst();
    return vRst;
  }catch (Exception e)  {
    return null;
  }
  }


  /**
    * �����������䣬��������getDataʹ����ͬ�ĻỰ���
    * @param sqlStr: Ҫ��ӵ����
    * @return �ɹ��r����true, ���򷵻�false
    */
   public boolean addBatchSql(String sqlStr)  {
 try {
     if (autoCommit)  {
       //��������ʼ����exeBatch�м������񣬲��ر��Զ�ִ��
       autoCommit = false;
       con.setAutoCommit(false);
     }
      m_statement.addBatch(sqlStr);
      return true;
 }catch (Exception e)  {
     m_errMsg =this.getClass().getName() + ".addBatchSql(): " + e;
     System.out.println(m_errMsg);
     return false;
 }
   }


   /**
    * ִ�����������
    * @return �ɹ�ʱ����ÿһ�����ĵ�״ֵ̬���飬���򷵻�null
    */
   public int [] exeBatchSql() throws Exception{
 try{
     int[] ret;
     ret = m_statement.executeBatch();
     autoCommit = true;  //�����Զ�ִ��
     con.setAutoCommit(true);
     con.commit();
     return ret;

 }catch (Exception e)  {
     m_errMsg =this.getClass().getName() + ".exeBatchSql(): " + e;
     m_errMsg += "\n���񱻻ع�.";
     con.rollback();
     autoCommit = true;  //�����Զ�ִ��
     con.setAutoCommit(true);
     System.out.println(m_errMsg);
     return null;
 }
   }


}
