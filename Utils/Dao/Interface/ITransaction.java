package demo2.Utils.Dao.Interface;

/**
 * 	事务回调函数
 * 	匿名类实现此回调函数，可以实现多条语句的事务
 * @author yiyang
 */
public interface ITransaction{
	void doSome() throws Exception;
}