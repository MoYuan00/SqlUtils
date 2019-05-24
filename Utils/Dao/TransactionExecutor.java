package demo2.Utils.Dao;


import java.sql.SQLException;
import java.util.List;

import demo2.Utils.Logs;
import demo2.Utils.Dao.Exception.TransactionFailException;
import demo2.Utils.Dao.Interface.ISqlDo;
import demo2.Utils.Dao.Interface.ITransaction;
import demo2.Utils.Dao.Interface.ITransactionSqlDo;


/**
 * 	基于数据库访问，封装事务操作。
 * 	此类会真正调用ISqlDo接口的sqlDo方法，开始执行，并且处理事务
 * 	
 * @author yiyang
 */
public class TransactionExecutor {
	/**
	 * 	面向多条语句的事务执行
	 * 	
	 * @param transaction	执行回调函数
	 * @param transactionSqlDo	执行语句(sqlDo)列表的来源
	 * @throws Exception	如果，执行，提交发生异常那么就会抛出
	 */
	public static void execute(ITransaction transaction, ITransactionSqlDo transactionSqlDo) throws Exception {
		List<ISqlDo<?>> sqlDoList = null;
		boolean flag = false;// 是否正常执行
		try {
			try {
				// 执行回调函数
				transaction.doSome();
				flag = true;// 正常执行
			}catch(Exception e) {
				e.printStackTrace();
				Logs.logError("TransactionExecutor.execute(IT,ITDo)", 
						"事务执行失败！ 可能是语句有误！");
			}
			sqlDoList = transactionSqlDo.getSqlDos();// 不论执行是否失败都要获取sqlDo列表，此列表用来下面的提交和回滚操作
			if(sqlDoList == null) throw new NullPointerException();
			if(flag) {// 如果正常执行就提交事务
				for(int i = 0, len = sqlDoList.size(); i < len; i++) {
					sqlDoList.get(i).commit();
				}
				Logs.logMessage("TransactionExecutor.execute(IT,ITDo)", "事务提交成功");
			}else throw new SQLException();// 执行失败 直接	抛出异常, 开始回滚， 此异常抛出是为了跳转到下面的回滚
		} catch (Exception e) {
			e.printStackTrace();
			if(flag)// 如果是正常执行，但是出现异常，那么就是提交失败
				Logs.logError("TransactionExecutor.execute(IT,ITDo)", "事务提交失败");
			try {
				for(int i = 0, len = sqlDoList.size(); i < len; i++) {
					sqlDoList.get(i).rollback();// 尝试回滚
				}
				Logs.logMessage("TransactionExecutor.execute(IT,ITDo)", "事务回滚成功");
			} catch (Exception e1) {
				e1.printStackTrace();
				Logs.logError("TransactionExecutor.execute(IT,ITDo)", "事务回滚失败");
			}
			throw new TransactionFailException();// 抛出事务失败异常
		} 
	}
	/**
	 * 	面向单条语句单连接的事务处理
	 * 	使用：
	 * Integer i = TransactionExecutor.execute(new AddImpl("", new String[] {}));
	 * 	改进后 使用：
	 * Integer i = TransactionExecutor.execute(
				new AddImpl().build(new UtilDao()).setSql("", new String[] {}));
	 * @param sqlDo 传入一个ISqlDo对象，并开始执行
	 * @return
	 * @throws TransactionFailException 
	 */
	public static <T>T execute(ISqlDo<T> sqlDo) throws Exception {
		try {
			T t = sqlDo.doSql();// 执行语句
			Logs.logMessage("TransactionExecutor.execute(ISqlDo<T>)", "事务提交成功");
			return t;
		} catch (Exception e) {
			e.printStackTrace();
			Logs.logError("TransactionExecutor.execute(ISqlDo<T>)", "事务提交失败");
			try {
				sqlDo.rollback();// 尝试回滚
				Logs.logMessage("TransactionExecutor.execute(ISqlDo<T>)", "事务回滚成功");
			} catch (Exception e1) {
				e1.printStackTrace();
				Logs.logError("TransactionExecutor.execute(ISqlDo<T>)", "事务回滚失败");
			}
			throw new TransactionFailException();
		}
	}
}
