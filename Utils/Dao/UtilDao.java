package demo2.Utils.Dao;
import java.sql.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import demo2.Utils.Logs;
import demo2.Utils.Connection.ConnectionPool;

/***
 * 	将数据库操作底层封装
 * 	提交commit()	如果不提交事务，那么操作将不会保存
 * @author yiyang
 */
public final class UtilDao {
	private PreparedStatement preS = null;
	/**
	 * 	为了防止数据库的变更而留出，暂时没有用
	 */
	public String database = null;
	/**
	 * 	构造函数
	 * @param obj	此参数是为了控制此类只能在BaseDao中初始化设置，初始化时需要传入初始化的对象this，
	 */
	public UtilDao(Object obj) {
		if(!(obj instanceof BaseDao<?>)) {
			throw new RuntimeException("UtilDao 不允许使用");
		}
	}
	/**
	 * 	保存当前的连接
	 */
	private Connection con = null;
	public static void main(String[] args) throws Exception {
//		UtilDao b = new UtilDao();
//		List<Map<String, String>> list = b.getList("select * from music", new String[]{});
//		System.out.println(list);
	}
	/**
	 * 	获取管道
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	private PreparedStatement getPreparedStatement(String sql) throws SQLException{
		this.getConnection();
		con.setAutoCommit(false);
		preS = con.prepareStatement(sql);
		return preS;
	}
	/**
	 * 	获取连接
	 */
	private void getConnection() {
		if(con == null) {
			if(database != null)
				con = ConnectionPool.getInstance().getConnection(database);
			else
				con = ConnectionPool.getInstance().getConnection();
		}
	}
	/**
	 * 	提交事务 注意 如果不调用此方法那么所做所有操作将不会保存到数据库， 也就是删除，添加，修改将失效。而对查询无影响
	 * 	如果没有异常，那么一旦提交便无法回滚， 所有应该先执行所有doSql() 然后调用提交，出错才回滚
	 */
	public void commit() throws Exception {
		if(con != null)
			con.commit();
	}
	/**
	 * 	如果提交失败就回滚事务
	 */
	public void rollback() throws Exception {
		if(con != null)
			con.rollback();
	}
	/**
	 * 	设置管道参数
	 * @param preS
	 * @param params
	 */
	private PreparedStatement setPreStatementParmas(String[] params) throws Exception{
		if(preS == null){
			Logs.logError("DaoBase.setPreStatementParmas(String[])", "preS is null");
			return null;
		}
		for(int i = 0, len = params.length; i < len; i++){
			preS.setString(i + 1, params[i]);
		}
		return preS;
	}
	/**
	 *	 执行查询类操作，返回list<map<String, String>>
	 * @param sql
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> getList(String sql, String[] params) throws Exception{
		getPreparedStatement(sql);
		setPreStatementParmas(params);
		// 存放结果
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		
		ResultSet set = preS.executeQuery();
		ResultSetMetaData metaData = set.getMetaData();
		int columnCount = metaData.getColumnCount();
		while(set.next()){
			// 存放一个元组
			Map<String, String> map = new HashMap<String, String>();
			for(int j = 0; j < columnCount; j++){
				String name = metaData.getColumnName(j + 1);
				map.put(name,  set.getString(name));// key： 属性名 value：值
			}
			list.add(map);
		}
		return list;
	}
	/**
	 * 	更新类操作
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	public int update(String sql, String[] params) throws Exception{
		getPreparedStatement(sql);
		setPreStatementParmas(params);
		return preS.executeUpdate();
	}
	
	
}
