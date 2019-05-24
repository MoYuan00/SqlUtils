package demo2.Utils.Dao.Implements;

import demo2.Utils.Dao.UtilDao;
import demo2.Utils.Dao.Interface.IUpdate;

public final class UpdateImpl implements IUpdate {
	private String sql = null;
	private String[] params = null;
	private UtilDao dao = null;
	@Override
	public Integer doSql() throws Exception {
		return update(sql, params);
	}

	@Override
	public void rollback() throws Exception {
		dao.rollback();
	}

	@Override
	public void commit() throws Exception {
		dao.commit();
	}

	@SuppressWarnings("unchecked")
	@Override
	public UpdateImpl build(UtilDao dao) {
		this.dao = dao;
		return this;
	}

	@Override
	public UtilDao getDao() {
		return dao;
	}

	@SuppressWarnings("unchecked")
	@Override
	public UpdateImpl setSql(String sql, String[] params) {
		this.sql = sql; this.params = params;
		return this;
	}

	@Override
	public int update(String sql, String[] params) throws Exception {
		return dao.update(sql, params);
	}

}
