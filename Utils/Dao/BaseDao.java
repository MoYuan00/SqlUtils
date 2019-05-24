package demo2.Utils.Dao;

import java.util.ArrayList;
import java.util.List;

import demo2.Utils.Dao.Implements.AddImpl;
import demo2.Utils.Dao.Implements.DeleteImpl;
import demo2.Utils.Dao.Implements.FindImpl;
import demo2.Utils.Dao.Implements.UpdateImpl;
import demo2.Utils.Dao.Interface.ISqlDo;
import demo2.Utils.Dao.Interface.ITable;
import demo2.Utils.Dao.Interface.IToJson;
import demo2.Utils.Dao.Interface.ITransaction;
import demo2.Utils.Dao.Interface.ITransactionSqlDo;

/**
 * 	再次对数据库访问经行封装
 * 	使用TransactionExecutor来保证事务的原子性
 * 	提供commit接口实现多语句事务操作
 * @author yiyang
 * @param <T> 只要是实现了IToJson接口的Bean都可以使用此类来完成数据库操作
 */
public abstract class BaseDao<T extends IToJson<T>> 
						implements ITable, ITransactionSqlDo{
	private UtilDao dao = null;
	/**
	 * 	用来记录参与多条事务的sqlDo
	 * 	这能让TransactionExecutor中获取到sqlDo列表，经行事务提交和回滚
	 */
	private final List<ISqlDo<?>> sqlDoList = new ArrayList<>(2);
	/**
	 * 	当前执行是否为多语句事务
	 * 	这能让多条语句事务，和单语句区分开
	 */
	private boolean isComplex = false;
	public BaseDao() {
		dao = new UtilDao(this);
	}
	/**
	 * 	执行删除操作 
	 * @param sql
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public final int delete(String sql, String[] params) throws Exception {
		sql = buildSql(sql);
		DeleteImpl delete = new DeleteImpl().build(dao).setSql(sql, params);
		if(!isComplex) return TransactionExecutor.execute(delete);	// 是否是多语句事务,如果是,就不采用单语句事务
		sqlDoList.add(delete);				// 否则就添加到多语句事务中
		return delete.doSql();
	}
	/**
	 * 	执行更新操作 
	 * @param sql
	 * @param params
	 * @return 
	 * @throws Exception
	 */
	public final int update(String sql, String[] params) throws Exception {
		sql = buildSql(sql);
		UpdateImpl update = new UpdateImpl().build(dao).setSql(sql, params);
		if(!isComplex) return TransactionExecutor.execute(update);
		sqlDoList.add(update);
		return update.doSql();
	}
	/**
	 * 	执行查询操作
	 * @param t
	 * @param sql
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public final List<T> find(T t, String sql, String[] params) throws Exception {
		sql = buildSql(sql);
		FindImpl<T> find = new FindImpl<T>().build(dao).setSql(sql, params).setT(t);
		if(!isComplex) return TransactionExecutor.execute(find);
		sqlDoList.add(find);
		return find.doSql();
	}
	/**
	 * 	执行添加操作
	 * @param sql
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public final int add(String sql, String[] params) throws Exception{
		sql = buildSql(sql);
		AddImpl add = new AddImpl().build(dao).setSql(sql, params);
		if(!isComplex) return TransactionExecutor.execute(add);
		sqlDoList.add(add);
		return add.doSql();
	}
	/**
	 * 	下面提供多语句事务的能力
	 */
	/**
	 * 	提交多条语句事务
	 * 	实现回调接口，在里面调用此类其他方法，将把所有语句归到一个事务中
	 * @param transaction
	 * @throws Exception
	 */
	public final void commit(ITransaction transaction) throws Exception {
		isComplex = true;//		当调用此方法来执行语句,那么表示要开始多语句事务,将isComplex设置为true来标志它的发生
		TransactionExecutor.execute(transaction, this);// 开始执行
		isComplex = false;//	当方法结束时，还原设置
	}
	/**
	 * 	在每个语句执行开始前，将会调用此方法，方法用来处理使用自定义简写的sql语句并还原它
	 * 	构建sql语句
	 */
	@Override
	public final String buildSql(String sql) {
		// 	当sql语句中出现	{}时，我将	{} 	定义为表的名字，在此将其替换，当然要实现替换，你得实现getTableName()方法这是必须的
		return sql.replace("{}", this.getTableName());
	}
	/**
	 * 	请子类不要调用此方法
	 */
	@Override
	public List<ISqlDo<?>> getSqlDos() {
		return sqlDoList;
	}
 
}







