package demo2.Utils.Dao.Interface;


public interface ITable {
	/**
	 * 	获取表名
	 * @return
	 */
	String getTableName();
	/**
	 * 	构建sql语句，处理
	 * 	通过提交的sql语句，处理出真正的sql语句
	 * @param sql
	 * @return
	 */
	String buildSql(String sql);
}
