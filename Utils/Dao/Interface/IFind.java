package demo2.Utils.Dao.Interface;

import java.util.List;

public interface IFind<T extends IToJson<T>> extends ISqlDo<List<T>> {
	List<T> find(String sql, String[] params) throws Exception;

}
