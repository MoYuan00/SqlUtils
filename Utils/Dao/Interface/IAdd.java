package demo2.Utils.Dao.Interface;

public interface IAdd extends ISqlDo<Integer>{
	int add(String sql, String[] params) throws Exception;
}
