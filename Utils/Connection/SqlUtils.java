package demo2.Utils.Connection;



import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.google.gson.Gson;


/**
 * 	此类用来加载数据库配置信息等
 * 	获取sql路径, 连接数据库名，数据库密码等
 * @author Administrator
 *
 */
public class SqlUtils {
	/**
	 * 	此类定义了创建一个数据库连接所需要的必要信息
	 * @author yiyang
	 */
	public static class SqlInfo{
		public String driver;
		public String url;
		public String username;
		public String password;
		public String database;
		public SqlInfo(String driver, String url, String username, String password, String database){
			this.driver = driver; this.url = url; this.username = username; 
			this.password = password; this.database = database;
		}
		@Override
		public String toString() {
			return new Gson().toJson(this);
		}
	}
	/**
	 * 	sql配置文件加载路径
	 */
	String sqlPropertiesPath = ConnectionPool.class.getClassLoader().getResource("").getPath() + "sqlConfig.properties";
	/**
	 * 	加载文件中的sql连接配置信息
	 * @return
	 */
	public SqlInfo loadSqlDriverInfo(){
		Properties pro = new Properties();
		FileInputStream in = null;
		SqlInfo sqlBean = null;
		try {
			in = new FileInputStream(sqlPropertiesPath);
			pro.load(in);
			String use = pro.getProperty("use");
			String driver = pro.getProperty(use + "Driver");
			String url = pro.getProperty(use + "Url");
			String database = pro.getProperty(use + "Database");
			String username = pro.getProperty(use + "UserName");
			String password = pro.getProperty(use + "UserPassword");
			sqlBean = new SqlInfo(driver, url, username, password, database);
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			pro.clear();
			if(in != null){// 关流
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sqlBean;
	}
	
}
