package demo2.Utils.Connection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import demo2.Utils.Logs;
import demo2.Utils.Connection.SqlUtils.SqlInfo;


/**
 * 	此类用来管理数据库连接操作
 * 	使用单例模式，面向多线程是安全的
 * 	提供获取数据库连接接口，封装连接数据库细节
 * @author yiyang
 *
 */
public class ConnectionPool {
	/**
	 * 	数据库配置加载者
	 */
	private SqlUtils sqlUtils = null;
	/**
	 * 	存放数据库配置信息
	 */
	private SqlInfo sqlBean = null;
	/**
	 * 	数据库连接池，存放数据库连接，暂时只是存放，并没有其他操作
	 */
	private final Map<String, Connection> connectionPool = new ConcurrentHashMap<String, Connection>(5);
	/**
	 * 	记录驱动是否成功加载的信息，因为每次创建连接都会查看驱动是否加载。此变量来记录是否已经加载
	 */
	boolean isDriver = false;
	/**
	 * 	饿汉模式单例
	 */
	private static final ConnectionPool _connectionPool = new ConnectionPool();
	public static ConnectionPool getInstance() {
		return _connectionPool;
	}
	private ConnectionPool() {
		loadSqlConfigInfo();
	}
	/**
	 * 	加载sql配置文件
	 */
	private void loadSqlConfigInfo() {
		sqlUtils = new SqlUtils();
		sqlBean = sqlUtils.loadSqlDriverInfo();
	}
	/**
	 * 	重新加载sql配置文件
	 */
	public void reloadSqlConfigInfo() {
		loadSqlConfigInfo();
		isDriver = false;
	}
	/**
	 * 	加载驱动
	 * @return
	 */
	private synchronized boolean driver(){
		if(isDriver) return isDriver;// 如果已经加载驱动就不再加载
		try {
			Class.forName(sqlBean.driver);
			isDriver = true;
			Logs.logMessage("ConnectionPool.driver()", "build Driver successfu!");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return isDriver;
	}
	/**
	 * 	获取一个连接
	 * @return
	 */
	public Connection getConnection(){
		return getConnection(sqlBean.database);
	}
	/**
	 * 	获取一个连接
	 * @param databaseName
	 * @return
	 */
	public Connection getConnection(String databaseName){
		Connection con = connectionPool.get(databaseName);
		try {
			if(con == null || con.isClosed()){
				synchronized(connectionPool) {// 保证线程安全
					con = createConnection(databaseName);
					if(con == null) {
						return null;
					}
					connectionPool.put(databaseName, con);
				}
				Logs.logMessage("ConnectionPool.getConnection(String)", 
						"new connection into pool 数量:" + connectionPool.size());
				return con;
			}
		} catch (SQLException e) {
			Logs.logError("ConnectionPool.getConnection(String)", "建立连接失败！ ");
			e.printStackTrace();
			return con;
		}
		System.out.println("get a connection into pool! 数量:" + connectionPool.size());
		return con;
	}
	/**
	 * 	创建一个连接
	 * @param databaseName
	 * @return
	 */
	private synchronized Connection createConnection(String databaseName) {
		Connection con = null;
		if(!driver()) return con;// 如果驱动加载失败，那么就return null
		try {
			Logs.logMessage("ConnectionPool.createConnection(String)", 
					sqlBean.url + databaseName + " " + sqlBean.username + " " + sqlBean.password);
			// 建立连接
			con = DriverManager.getConnection(sqlBean.url + databaseName, sqlBean.username, sqlBean.password);
			Logs.logMessage("ConnectionPool.createConnection(String)", "create new Connection");
		} catch (SQLException e) {
			e.printStackTrace();
			Logs.logError("ConnectionPool.createConnection(String)", "数据库创建连接失败！ ");
		}
		return con;
	}
	/**
	 * 	获取配置信息
	 * @return
	 */
	public String getSqlConfig() {
		return sqlBean.toString();
	}
	public static void main(String[] args) {
		ConnectionPool.getInstance().getConnection();
		System.out.println(ConnectionPool.getInstance().getSqlConfig());
	}
}
