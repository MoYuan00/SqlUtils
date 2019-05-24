package demo2.Utils.Dao.Interface;

import java.util.Map;

/**
 * 	提供将map数据转换成本对象的能力
 * @author yiyang
 *
 * @param <T>
 */
public interface IToJson<T> {
	/**
	 * 	将map集合转换成对象， 每个Bean必须实现该接口
	 * @param json
	 * @return
	 */
	T jsonToObj(Map<String, String> json);
}
