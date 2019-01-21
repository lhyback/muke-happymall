package xin.aliyang.mmall.util;

import java.math.BigDecimal;

/**
 * Created by lhy on 2019/1/17.
 */
public class BigDecimalUtil {
	private BigDecimalUtil() {}

	public static BigDecimal add(double a, double b) {
		BigDecimal A = new BigDecimal(Double.toString(a));
		BigDecimal B = new BigDecimal(Double.toString(b));
		return A.add(B);
	}

	public static BigDecimal subtract(double a, double b) {
		BigDecimal A = new BigDecimal(Double.toString(a));
		BigDecimal B = new BigDecimal(Double.toString(b));
		return A.subtract(B);
	}

	public static BigDecimal multiply(double a, double b) {
		BigDecimal A = new BigDecimal(Double.toString(a));
		BigDecimal B = new BigDecimal(Double.toString(b));
		return A.multiply(B);
	}

	/**
	 * 除法结果，四舍五入保留两位小数
	 * @param a
	 * @param b
	 * @return
	 */
	public static BigDecimal divide(double a, double b) {
		BigDecimal A = new BigDecimal(Double.toString(a));
		BigDecimal B = new BigDecimal(Double.toString(b));
		return A.divide(B, 2, BigDecimal.ROUND_HALF_UP);
	}

}
