package demo2.Utils.Dao.Exception;

import java.sql.SQLException;

/**
 * 	事务提交失败异常
 * @author yiyang
 *
 */
public class TransactionFailException extends SQLException {
	private static final long serialVersionUID = 1L;
	public TransactionFailException() {
		super("事务提交失败异常");
	}
	
	public TransactionFailException(String message) {
		super("事务提交失败异常：" + message);
	}
	
}
