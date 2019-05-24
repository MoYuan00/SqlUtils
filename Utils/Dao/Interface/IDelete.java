package demo2.Utils.Dao.Interface;

public interface IDelete extends ISqlDo<Integer> {

	int delete(String sql, String[] params) throws Exception;
	
}
