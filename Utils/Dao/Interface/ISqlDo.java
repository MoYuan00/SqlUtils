package demo2.Utils.Dao.Interface;

import demo2.Utils.Dao.UtilDao;

/**
 * 	所有类型数据库语句执行的接口
 * 	如果要经行数据库操作请实现次接口
 * @author yiyang
 *	
 * @param <T>
 */
public interface ISqlDo<T> {
	/**
	 * 	调用此接口方法将执行数据库语句
	 * @return
	 * @throws Exception
	 */
	T doSql() throws Exception;
	/**
	 * 	提供事务提交的能力
	 * @throws Exception
	 */
	void rollback() throws Exception;
	/**
	 * 	提供事务回滚的能力
	 * @throws Exception
	 */
	void commit() throws Exception;
	
	/**
	 * 	为了解决 add，del，update，find，每次使用都用新建一个UtilDao的问题而定义的接口
	 * 	设置语句使用的UtilDao
	 * 	TODO 
	 * 	这里实在设计的不太好，对UtilDao的引用太多，可以在对Utildao封装一层然后减少耦合度
	 * @param sqlDo
	 * @return
	 */
	<T1 extends ISqlDo<T>> T1 build(UtilDao dao);
	UtilDao getDao();
	/**
	 * 	设置sql语句，和参数
	 * 	实际上这个接口并不是必须的，但是为了解决UtilDao的统一问题，于是定义了此接口
	 * @param sql
	 * @param params
	 * @return
	 */
	<T1 extends ISqlDo<T>> T1 setSql(String sql, String[] params);
}
