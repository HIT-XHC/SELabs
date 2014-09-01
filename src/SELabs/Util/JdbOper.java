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
   * @Handler：
   * @模块：
   * @功能：连接数据库，初始化数据操作对象JdbOper
   * @作者：cameran
   * @日期：2002-5-16
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
   * @Handler：
   * @模块：
   * @功能：连接数据库，初始化数据操作对象JdbOper
   * @作者：cameran
   * @日期：2002-5-16
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
   * 生成结果集
   * Description: 如果多次调用本功能将只能保存当前结果集
   * @param mySql: 用于产生结果集的语句
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
   * 生成结果集
   * Description: 本方法使用新的会话返回结果集。
   * @param mySql: 用于产生结果集的语句
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
   * 修改一条纪录，该方法可以用来更新和删除纪录
   * @param  String sql 进行操作的sql语句
   * @return boolean 操作成功返回 TRUE，否则返回 FALSE
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
    * 插入一条记录
    * @return boolean 操作成功返回 TRUE，否则返回 FALSE
    * @param String table 表名
    * @param String[] fields 字段名称数组
    * @param String[] types 字段类型数组，对于char, varchar类型，对应值是string，其它类型可为任意值
    * @param String[] values 字段明对应的值的数组
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
   * 修改一条纪录，该方法可以用来更新和删除纪录
   * @param  String sql 进行操作的sql语句
   * @return boolean 操作成功返回 TRUE，否则返回 FALSE
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
   * 更新一条记录
   * @return boolean 操作成功返回 TRUE，否则返回 FALSE
   * @param String criteria 筛选条件
   * @param String table 表名
   * @param String[] fields 字段名称数组
   * @param String[] types 字段类型数组，对于char, varchar类型，对应值是string，其它类型可为任意值
   * @param String[] values 字段明对应的值的数组
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
         * 删除一条记录
         * @return boolean 操作成功返回 TRUE，否则返回 FALSE
         * @param String table 表名
         * @param String criteria 筛选条件
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
   * 关闭数据库操作对象
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
   * 将结果集转换为Vector向量，向量由一维字符串数组组成。第零个数组为的字段名
   * @param Rst:要转换的结果集
   * @return 成功是返回向量，否则返回null;
   */
  public Vector rst2Vector(ResultSet rst) throws Exception {
  try {
    Vector vRst = new Vector();

     //获取表头信息
     ResultSetMetaData rstm = rst.getMetaData();
//System.out.println("getmeta");
     int colcount = rstm.getColumnCount(), i;
     String colname[] = new String[colcount];
     //形成字段名数组
     for (i=1; i<=colcount; i++)
       colname[i-1] = rstm.getColumnName(i);    //字段名从1开始，数组应从0开始
     vRst.addElement(colname);    //字段名数组位于0位置
//System.out.println("cr field");

     //形成记录集,从矢量的索引1开始
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
   * 将结果集转换为Vector向量，向量由一维字符串数组组成。通过参数可以选择生成或不生成字段名头。
   * @param rst 要转换的结果集
   * @param hasTitle
   * @return 成功时返回向量，否则返回null;
   * @throws Exception 抛出所有错误
   */
  public Vector rst2Vector(ResultSet rst, boolean hasTitle) throws Exception {
    Vector vc = rst2Vector(rst);
    if (!hasTitle)
      vc.remove(0);
    return vc;
  }

  /**
   * 将结果集转换为Vector向量，向量由HashMap对象组成。每个HashMap对象由字段名作为键值，字段值字符串作为值
   * @param rst 要转换的结果集
   * @return 成功时返回向量，否则返回null;
   * @throws Exception 抛出所有错误
   */
  public Vector rst2HashVector(ResultSet rst) {
  try {
    Vector vRst = new Vector();

     //获取表头信息
     ResultSetMetaData rstm = rst.getMetaData();
//System.out.println("getmeta");
     int colcount = rstm.getColumnCount(), i;
     String colname[] = new String[colcount];
     //形成字段名数组
     for (i=1; i<=colcount; i++)
       colname[i-1] = rstm.getColumnName(i);    //字段名从1开始，数组应从0开始
//System.out.println("cr field");

     //形成记录集,从矢量的索引1开始
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
    * 添加批处理语句，本函数与getData使用相同的会话句柄
    * @param sqlStr: 要添加的语句
    * @return 成功時返回true, 否则返回false
    */
   public boolean addBatchSql(String sqlStr)  {
 try {
     if (autoCommit)  {
       //设置事务开始，在exeBatch中激发事务，并关闭自动执行
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
    * 执行批处理程序
    * @return 成功时返回每一条语句的的状态值数组，否则返回null
    */
   public int [] exeBatchSql() throws Exception{
 try{
     int[] ret;
     ret = m_statement.executeBatch();
     autoCommit = true;  //开启自动执行
     con.setAutoCommit(true);
     con.commit();
     return ret;

 }catch (Exception e)  {
     m_errMsg =this.getClass().getName() + ".exeBatchSql(): " + e;
     m_errMsg += "\n事务被回滚.";
     con.rollback();
     autoCommit = true;  //开启自动执行
     con.setAutoCommit(true);
     System.out.println(m_errMsg);
     return null;
 }
   }


}
