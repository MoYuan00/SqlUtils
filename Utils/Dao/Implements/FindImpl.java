package demo2.Utils.Dao.Implements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import demo2.Utils.Dao.UtilDao;
import demo2.Utils.Dao.Interface.IFind;
import demo2.Utils.Dao.Interface.IToJson;

public final class FindImpl<T extends IToJson<T>> implements IFind<T>  {
	private UtilDao dao = null;
	private String sql = null;
	private String[] params = null;
	private T t = null;
	@Override
	public List<T> doSql() throws Exception {
		return this.find(sql, params);
	}

	@Override
	public void rollback() throws Exception {
		dao.rollback();
	}

	@Override
	public void commit() throws Exception {
		dao.commit();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public FindImpl build(UtilDao dao) {
		this.dao = dao;
		return this;
	}

	@Override
	public UtilDao getDao() {
		return dao;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public FindImpl setSql(String sql, String[] params) {
		this.sql = sql; this.params = params;
		return this;
	}
	
	@Override
	public List<T> find(String sql, String[] params) throws Exception {
		List<Map<String, String>> json = dao.getList(sql, params);
		int len = json.size();
		List<T> list = new ArrayList<T>(len);
		for(int i = 0; i < len; i++) {
			list.add((t.jsonToObj(json.get(i))));
		}
		return list;
	}

	public FindImpl<T> setT(T t) {
		this.t= t;
		return this;
	}
	
 
}
