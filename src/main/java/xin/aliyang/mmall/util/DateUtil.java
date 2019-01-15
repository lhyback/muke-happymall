package xin.aliyang.mmall.util;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * Created by lhy on 2019/1/13.
 */
public class DateUtil {
	private static final String pattern = "yyyy-MM-dd HH:mm:ss";

	public static String dateToStr(Date date) {
		if (date == null) {
			return null;
		}
		DateTime dateTime = new DateTime(date);
		return dateTime.toString(pattern);
	}

	public static Date strToDate(String dateStr) {
		if (StringUtils.isBlank(dateStr)) {
			return null;
		}
		DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
		return formatter.parseDateTime(dateStr).toDate();
	}

	public static void main(String[] args) {
		String dateStr = "1995-05-06 12:00:00";
		System.out.println(strToDate(dateStr));

		Date date = new Date();
		System.out.println(dateToStr(date));
	}
}
