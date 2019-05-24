package demo2.Utils.Dao.Implements;

import demo2.Utils.Dao.UtilDao;
import demo2.Utils.Dao.Interface.IDelete;

public final class DeleteImpl implements IDelete {
	private String sql = null;
	private String[] parmas = null;
	private UtilDao dao = null;
	
	@Override
	public Integer doSql()  throws Exception{
		return delete(sql, parmas);
	}

	@Override
	public int delete(String sql, String[] parmas) throws Exception {
		return dao.update(sql, parmas);
	}

	@Override
	public void rollback() throws Exception {
		dao.rollback();
	}

	@Override
	public void commit() throws Exception {
		dao.commit();
	}

	@Override
	public UtilDao getDao() {
		return dao;
	}

	@SuppressWarnings("unchecked")
	@Override
	public DeleteImpl setSql(String sql, String[] params) {
		this.sql = sql; this.parmas = params;
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public DeleteImpl build(UtilDao dao) {
		this.dao = dao;
		return this;
	}
 
}
