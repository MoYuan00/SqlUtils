package demo2.Utils.Dao.Interface;

public interface IUpdate extends ISqlDo<Integer> {
	int update(String sql, String[] params) throws Exception;
}
