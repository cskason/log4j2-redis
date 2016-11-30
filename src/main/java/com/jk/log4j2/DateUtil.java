package com.jk.log4j2;



import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class DateUtil {

	public static String[] allFormatStrings = new String[] {
			"yyyy-MM-dd HH:ss", "yyyyMMddHHss", "yyyy-MM-dd HHss",
			"yyyy-MM-dd", "yyyyMMddHHmm" };

	private static final Log logger = LogFactory.getLog(DateUtil.class);
	// 日期
	public static final String YEAR_TO_DAY = "yyyy-MM-dd";
	// 24小时制，精确到秒
	public static final String YEAR_TO_SEC = "yyyy-MM-dd HH:mm:ss";
	// 24小时制，精确到毫秒
	public static final String YEAR_TO_MS = "yyyy-MM-dd HH:mm:ss.SSS";
	// 24小时制，精确到分
	public static final String YEAR_TO_MINUTE = "yyyy-MM-dd HH:mm";

	public static final String MONTH_TO_MINUTE = "MM-dd HH:mm";

	public static final String HOUR_TO_SEC = "HH:mm:ss";

	public static final String YEAR_TO_MS_UN_LINE = "yyyyMMdd HHmmssSSS";

	public static final String YEAR_TO_SEC_UN_LINE = "yyyyMMdd HHmmss";

	public static final String YEAR_TO_MI_UN_LINE = "yyyyMMdd HHmm";

	public static final String YEAR_TO_DAY_UN_LINE = "yyyyMMdd";

	public static final String YEAR_TO_MS_NO_BLANK = "yyyyMMddHHmmssSSS";

	public static final String YEAR_TO_SEC_NO_BLANK = "yyyyMMddHHmmss";

	public static final String YEAR_TO_MI_NO_BLANK = "yyyyMMddHHmm";
	
	public static final String YEAR_TO_WEEK_UN_LINE = "yyyyw";

	public static final String DAY_TO_MINUTE = "dd HH:mm";

	public static final String DAY = "dd";
	
	public static final String DMY = "ddMMMyyyy";
	
	public static final String HOUR_TO_MINUTE_UN_LINE = "HHmm";
	
	public static final String HOUR_TO_MINUTE = "HH:mm";
	//把web的DateUtil工具类合并到这个类，pengws
	//添加月日格式日期  
    public static final String MONTH_TO_DAY ="MM-dd";
    //添加年月格式日期
    public static final String YEAR_TO_MONTH ="yy-MM";
    //添加年格式日期
    public static final String YEAR ="yyyy";
    //添加年月没有下划线日期格式
    public static final String YEAR_TO_MONTH_UN_LINE = "yyyyMM";
	// 系统和数据时间差
	private static long SYSTEM_DIFF_TIME = 0;
	
	// 月份英文缩写
	private static final String[] MONTH_EN = { 
		"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG",
		"SEP", "OCT", "NOV", "DEC" };

	/**
	 * 日期转换成 dd HH:mm 字符
	 * 
	 * @param date
	 * @return(设定参数)
	 * @return String(返回值说明)
	 * @author soarin 2014-7-8
	 */
	public static String getDayToMin(final Date date) {
		if (null == date) {
			return "";
		}
		return format(date, DAY_TO_MINUTE);
	}

	/**
	 * DD HH:MI
	 * 
	 * @param dateStr
	 * @return(设定参数)
	 * @return Date(返回值说明)
	 * @author soarin 2014-7-9
	 */
	public static Date parseDayToMin(final String dateStr) {
		return parseDate(dateStr, DAY_TO_MINUTE);

	}

	/**
	 * 将yyyyMMdd格式字符转换为日期
	 * 
	 * @param dateStr
	 * @return(设定参数)
	 * @return Date(返回值说明)
	 * @author soarin 2013-6-5
	 */
	public static Date parseYearToDayUnLine(String dateStr) {
		return parseDate(dateStr, YEAR_TO_DAY_UN_LINE);
	}

	/**
	 * 
	 * yyyyMMdd格式字符转换到时间
	 * 
	 * @param date
	 * @return(设定参数)
	 * @return String(返回值说明)
	 * @author soarin 2013-6-5
	 */
	public static String getYearToDayUnLine(Date date) {
		if (null == date) {
			return "";
		}
		return format(date, YEAR_TO_DAY_UN_LINE);
	}

	/**
	 * 将yyyyMMdd HHmm格式字符转换为日期
	 * 
	 * @param dateStr
	 * @return(设定参数)
	 * @return Date(返回值说明)
	 * @author soarin 2013-6-5
	 */
	public static Date parseYearToMinUnLine(String dateStr) {
		return parseDate(dateStr, YEAR_TO_MI_UN_LINE);
	}
	public static String dateToString(Date date,String patten){
		SimpleDateFormat format=new SimpleDateFormat(patten);
		return format.format(date);
	}
	/**
	 * 将字符串HHmm转换为离参照时间最近的时间 如参照时间为9月30日14点，传入1150,则返回9月30日11点50分
	 * 如参照时间为9月30日0点10分，传入2350,则返回9月29日23点50分
	 * 如参照时间为9月29日23点50分，传入0010,则返回9月30日00点10分
	 * 
	 * @param refTm
	 *            参照时间
	 * @param hourMin
	 *            'HHmm'格式
	 * @return Date(返回值说明)
	 * @author soarin 2014-3-5
	 */
	public static Date getLatestDateFromHourMinUnLine(Date refTm, String hourMin) {

		String strCurrentDt = DateUtil.getYearToDayUnLine(refTm);
		strCurrentDt += " ";
		strCurrentDt += hourMin;

		Date newTm = DateUtil.parseYearToMinUnLine(strCurrentDt);
		Date newTmPre = DateUtil.addDate(newTm, -1); // 往前一天
		Date newTmAfter = DateUtil.addDate(newTm, 1); // 往后一天

		Date finalTm = null;
		// 取和参照时间最近的时间
		if (Math.abs(DateUtil.diffMinute(refTm, newTm)) > Math.abs(DateUtil
				.diffMinute(refTm, newTmPre))) {
			finalTm = newTmPre;
		} else {
			finalTm = newTm;
		}

		if (Math.abs(DateUtil.diffMinute(refTm, finalTm)) > Math.abs(DateUtil
				.diffMinute(refTm, newTmAfter))) {
			finalTm = newTmAfter;
		}

		return finalTm;
	}

	/**
	 * 将字符串HHMM转换为离当前时刻最近的时间 如当前时间为9月30日14点，传入1150,则返回9月30日11点50分
	 * 如当前时间为9月30日0点10分，传入2350,则返回9月29日23点50分
	 * 如当前时间为9月29日23点50分，传入0010,则返回9月30日00点10分
	 * 
	 * @param hourMin
	 *            'HHMM'格式
	 * @return(设定参数)
	 * @return Date(返回值说明)
	 * @author soarin 2013-9-30
	 */
	public static Date getLatestDateFromHourMinUnLine(String hourMin) {
		Date currentTm = DateUtil.getSystemTm();

		String strCurrentDt = DateUtil.getYearToDayUnLine(currentTm);
		strCurrentDt += " ";
		strCurrentDt += hourMin;

		Date newTm = DateUtil.parseYearToMinUnLine(strCurrentDt);
		Date newTmPre = DateUtil.addDate(newTm, -1); // 往前一天
		Date newTmAfter = DateUtil.addDate(newTm, 1); // 往后一天

		Date finalTm = null;
		// 取和当前时间最近的时间
		if (Math.abs(DateUtil.diffMinute(currentTm, newTm)) > Math.abs(DateUtil
				.diffMinute(currentTm, newTmPre))) {
			finalTm = newTmPre;
		} else {
			finalTm = newTm;
		}

		if (Math.abs(DateUtil.diffMinute(currentTm, finalTm)) > Math
				.abs(DateUtil.diffMinute(currentTm, newTmAfter))) {
			finalTm = newTmAfter;
		}

		return finalTm;
	}

	/**
	 * 
	 * yyyyMMdd HHmm格式字符转换到时间
	 * 
	 * @param date
	 * @return(设定参数)
	 * @return String(返回值说明)
	 * @author soarin 2013-6-5
	 */
	public static String getYearToMinUnLine(Date date) {
		if (null == date) {
			return "";
		}
		return format(date, YEAR_TO_MI_UN_LINE);
	}

	/**
	 * 将yyyyMMdd HHmmss格式字符转换为日期
	 * 
	 * @param dateStr
	 * @return(设定参数)
	 * @return Date(返回值说明)
	 * @author soarin 2013-6-5
	 */
	public static Date parseYearToSecUnLine(String dateStr) {
		return parseDate(dateStr, YEAR_TO_SEC_UN_LINE);
	}

	/**
	 * 
	 * yyyyMMdd HHmmss格式字符转换到时间
	 * 
	 * @param date
	 * @return(设定参数)
	 * @return String(返回值说明)
	 * @author soarin 2013-6-5
	 */
	public static String getYearToSecUnLine(Date date) {
		if (null == date) {
			return "";
		}
		return format(date, YEAR_TO_SEC_UN_LINE);
	}

	/**
	 * 将yyyyMMdd HHmmssSSS格式字符转换为日期
	 * 
	 * @param dateStr
	 * @return(设定参数)
	 * @return Date(返回值说明)
	 * @author soarin 2013-6-5
	 */
	public static Date parseYearToMsUnLine(String dateStr) {
		return parseDate(dateStr, YEAR_TO_MS_UN_LINE);
	}

	/**
	 * 
	 * yyyyMMdd HHmmssSSS格式字符转换到时间
	 * 
	 * @param date
	 * @return(设定参数)
	 * @return String(返回值说明)
	 * @author soarin 2013-6-5
	 */
	public static String getYearToMsUnLine(Date date) {
		if (null == date) {
			return "";
		}
		return format(date, YEAR_TO_MS_UN_LINE);
	}

	/**
	 * 
	 * 字符串日期转换到Date类型
	 * 
	 * Examples:
	 * 
	 * @param dateStr
	 *            yyyy-MM-dd格式的字符串
	 * @return 日期类型
	 * 
	 * @return: 返回值说明
	 * @exception： 异常的说明
	 * @author soarin 2013-1-12
	 * 
	 */
	public static Date parseDate(String dateStr) {
		return parseDate(dateStr, YEAR_TO_DAY);
	}

	/**
	 * 
	 * 时间转换成yyyy-MM-dd字符 Examples:(列举一些调用的例子)
	 * 
	 * @param dt
	 * @return(设定参数)
	 * @return String(返回值说明)
	 * @author soarin 2013-6-7
	 */
	public static String getYearToDay(Date dt) {
		if (null == dt) {
			return "";
		}
		return format(dt, YEAR_TO_DAY);
	}

	/**
	 * 获取当前日期的简单形式,去掉时/分/秒(如：2012-07-04 00:00:00)
	 * 
	 * @return
	 * @author 田红兵 2012-7-5
	 */
	public static Date getSimpleDate() {
		return getSimpleDate(getSystemTm());
	}

	/**
	 * 获取给定日期的简单形式,去掉时/分/秒(如：2012-07-04 00:00:00)
	 * 
	 * @return
	 * @author 田红兵 2012-7-5
	 */
	public static Date getSimpleDate(Date d) {
		if (null == d)
			return null;
		return parseDate(getYearToDay(d));
	}

	/**
	 * 返回字符型时间
	 * 
	 * @param date
	 *            日期
	 * @return 返回字符型时间
	 */
	public static String getTime(Date date) {
		if (null == date) {
			return "";
		}
		return format(date, HOUR_TO_SEC);
	}

	/**
	 * 返回字符型日期时间
	 * 
	 * @param date
	 *            日期
	 * @return 返回字符型日期时间
	 */
	public static String getDateTime(Date date) {
		if (null == date) {
			return "";
		}
		return format(date, YEAR_TO_MINUTE);
	}

	/**
	 * yyyy-MM-dd HH:mm格式字符转换为date
	 * 
	 * @param dateStr
	 * @return(设定参数)
	 * @return Date(返回值说明)
	 * @author soarin 2014-6-11
	 */
	public static Date parseDateTime(String dateStr) {
		return DateUtil.parseDate(dateStr, YEAR_TO_MINUTE);
	}

	public static String getDateTimeSec(Date date) {
		if (null == date) {
			return "";
		}
		return format(date, YEAR_TO_SEC);
	}

	public static String getDateTimeSecTwo(Date date) {
		if (null == date) {
			return "";
		}
		return format(date, "yyyy-MM-dd HHmm");
	}

	public static String getDateTimeSec(String date) {
		if (null == date) {
			return "";
		}
		Date dateTemp = stringToUtilDate(date, YEAR_TO_SEC);
		return format(dateTemp, YEAR_TO_SEC);
	}

	/**
	 * 获取当前日期的简单形式,去掉时/分/秒(如：2012-07-04 00:00:00)
	 * 
	 * @return
	 * @author 田红兵 2012-7-5
	 */
	public static Date getSimpleDay() {
		return getSimpleDay(getSystemTm());
	}

	/**
	 * 获取给定日期的简单形式,去掉时/分/秒(如：2012-07-04 00:00:00)
	 * 
	 * @return
	 * @author 田红兵 2012-7-5
	 */
	public static Date getSimpleDay(Date d) {
		if (null == d)
			return null;
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	public static Date parseDateTimeSec(String str) throws ParseException {
		return parseDate(str, YEAR_TO_SEC);
		// java.text.DateFormat df = new
		// java.text.SimpleDateFormat(YEAR_TO_SEC);
		// return df.parse(str);
	}

	/**
	 * 
	 * 方法作用说明
	 * 
	 * Examples: 列举一些调用的例子
	 * 
	 * @param dateStr
	 * @param format
	 * @return
	 * 
	 * @return: 返回值说明
	 * @exception： 异常的说明
	 * @author soarin 2013-1-12
	 * 
	 */
	public static Date parseDate(String dateStr, String format) {
		Date date = null;
		try {
			java.text.DateFormat df = new java.text.SimpleDateFormat(format);	
			//df.setTimeZone(TimeZone.getTimeZone("GMT"));
			String dt = dateStr;
			if ((!dt.equals("")) && (dt.length() < format.length())) {
				dt += format.substring(dt.length()).replaceAll("[YyMmDdHhSs]",
						"0");
			}
			date = (Date) df.parse(dt);
		} catch (Exception e) {
			logger.error("parse Date error:" + dateStr + "|" + format,e);
		}
		return date;
	}

	/**
	 * 
	 * 方法作用说明 按format格式将时间转换成字符串 Examples: 列举一些调用的例子
	 * 
	 * @param date
	 * @param format
	 *            格式化形式
	 * @return
	 * 
	 * @return: 返回值说明
	 * @exception： 异常的说明
	 * @author soarin 2013-1-12
	 * 
	 */
	public static String format(Date date, String format) {
		String result = "";
		try {
			if (date != null) {
				java.text.DateFormat df = new java.text.SimpleDateFormat(format);
				df.setTimeZone(TimeZone.getTimeZone("GMT"));
				result = df.format(date);
			}
		} catch (Exception e) {
			logger.error(date + "|" + format , e);
		}
		return result;
	}

	/**
	 * 把包含日期值转换为字符串
	 * 
	 * @param date日期
	 *            （日期+时间）
	 * @param type
	 *            输出类型
	 * @return 字符串
	 */
	public static String dateTimeToString(Date date, String type) {
		String DateString = "";
		if (date == null) {
			DateString = "";
		} else {
			SimpleDateFormat formatDate = new SimpleDateFormat(type,
					Locale.getDefault());
			DateString = formatDate.format(date);
		}
		return DateString;
	}

	/**
	 * 把包含日期值转换为字符串
	 * 
	 * @param Object日期
	 *            （日期+时间）
	 * @param type
	 *            输出类型
	 * @return 字符串
	 */
	public static String dateTimeToString(Object dateObj, String type) {
		String DateString = "";
		if (dateObj == null) {
			DateString = "";
		} else {
			SimpleDateFormat formatDate = new SimpleDateFormat(type,
					Locale.getDefault());
			DateString = formatDate.format(dateObj);
		}
		return DateString;
	}

	/**
	 * 将指定格式的日期/时间字符串转换成Date格式
	 * 
	 * @param strDate
	 *            String类型，日期字符
	 * @param strFormat
	 *            String类型，格式
	 * @return Date类型
	 */
	public static Date stringToUtilDate(String strDate, String strFormat) {
		try {
			if (strDate == null || strDate.equals("")) {
				return null;
			} else {
				SimpleDateFormat formatdate = new SimpleDateFormat(strFormat,
						Locale.getDefault());
				Date date = new Date((formatdate.parse(strDate)).getTime());
				return date;
			}
		} catch (Exception ex) {
			logger.error(strDate + "|" + strFormat , ex);
		}
		return null;
	}

	/**
	 * 将日期转换成strFormat的转换
	 * 
	 * @param startDate
	 * @param endDate
	 * @param strFormat
	 * @return
	 */
	public static Date dateToFormat(Date startDate, String strFormat) {
		try {
			if (startDate != null && !"".equals(strFormat)) {
				String date = dateTimeToString(startDate, strFormat);
				return stringToUtilDate(date, strFormat);
			}
		} catch (Exception ex) {
			logger.error(startDate + "|" + strFormat , ex);
		}
		return startDate;
	}

	public static Date dateToFormat(Object startDate, String strFormat) {
		try {
			if (startDate != null && !"".equals(strFormat)) {
				String date = dateTimeToString(startDate, strFormat);
				return stringToUtilDate(date, strFormat);
			}
		} catch (Exception ex) {
			logger.error(startDate + "|" + strFormat , ex);
		}
		return null;
	}

	/**
	 * 根据日期Date计算星期
	 * 
	 * @param date
	 * @return 返回星期
	 */
	public static String getWeek(Date date) {
		String[] weekDaysName = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五",
				"星期六" };
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return weekDaysName[calendar.get(Calendar.DAY_OF_WEEK) - 1];
	}

	/**
	 * 得到某天星期几 1 2 3 4 5 6 7
	 * 
	 * @param date
	 * @return(设定参数)
	 * @return int(返回值说明)
	 * @author soarin 2014-3-25
	 */
	public static int getIntWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 返回年份
	 * 
	 * @param date
	 *            日期
	 * @return 返回年份
	 */
	public static int getYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.YEAR);
	}

	/**
	 * 返回月份
	 * 
	 * @param date
	 *            日期
	 * @return 返回月份
	 */
	public static int getMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MONTH) + 1;
	}

	public static String getMonth(int month) {
		String[] months = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL",
				"AUG", "SEP", "OCT", "NOV", "DEC" };
		return months[month];
	}

	public static String getMonth(String month) {
		String[] months = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL",
				"AUG", "SEP", "OCT", "NOV", "DEC" };
		for (int i = 1; i <= months.length; i++) {
			if (months[i - 1].equals(month)) {
				if (i < 10) {
					return "0" + i;
				} else {
					return "" + i;
				}
			}
		}
		return months[0];
	}

	/**
	 * 返回日份
	 * 
	 * @param date
	 *            日期
	 * @return 返回日份
	 */
	public static int getDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 返回小时
	 * 
	 * @param date
	 *            日期
	 * @return 返回小时
	 */
	public static int getHour(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 返回分钟
	 * 
	 * @param date
	 *            日期
	 * @return 返回分钟
	 */
	public static int getMinute(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MINUTE);
	}

	/**
	 * 返回秒钟
	 * 
	 * @param date
	 *            日期
	 * @return 返回秒钟
	 */
	public static int getSecond(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.SECOND);
	}

	/**
	 * 返回毫秒
	 * 
	 * @param date
	 *            日期
	 * @return 返回毫秒
	 */
	public static long getMillis(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.getTimeInMillis();
	}

	/**
	 * 日期相加
	 * 
	 * @param date
	 *            日期
	 * @param day
	 *            天数
	 * @return 返回相加后的日期
	 */
	public static Date addDate(Date date, int day) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getMillis(date) + ((long) day) * 24 * 3600 * 1000);
		return c.getTime();
	}

	/**
	 * 增加小时
	 * 
	 * @param date
	 *            原来的时间
	 * @param hour
	 *            小时
	 * @return Date 新的时间
	 * @author pengzh 2014-7-24
	 */
	public static Date addHour(Date date, double hour) {
		if (date == null) {
			return null;
		}
		return addHour(date, (int) hour);
	}

	public static Date addHour(Date date, int hour) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR_OF_DAY, hour);
		return c.getTime();
	}

	public static Date addMinute(Date date, int minute) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MINUTE, minute);
		return c.getTime();
	}

	/**
	 * 日期增加秒 方法作用说明
	 * 
	 * Examples: 列举一些调用的例子
	 * 
	 * @param date
	 *            日期
	 * @param hour
	 *            小时
	 * @return
	 * 
	 * @return: 返回值说明
	 * @exception： 异常的说明
	 * @author luxinming 2013-9-12
	 * 
	 */
	public static Date addSecond(Date date, double second) {
		if (date == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.SECOND, (int) (second));
		return c.getTime();
	}

	public static Date addSecond(Date date, int second) {
		if (date == null) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.SECOND, (second));
		return c.getTime();
	}

	/**
	 * 日期相减
	 * 
	 * @param date
	 *            日期
	 * @param day
	 *            天数
	 * @return 返回相减后的日期
	 */
	public static Date diffDate(Date date, int day) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(getMillis(date) - ((long) day) * 24 * 3600 * 1000);
		return c.getTime();
	}

	/**
	 * 日期相减
	 * 
	 * @param date
	 *            日期
	 * @param date1
	 *            日期
	 * @return 返回相减后的日期
	 */
	public static int diffDate(Date date, Date date1) {
		return (int) ((getMillis(date) - getMillis(date1)) / (24 * 3600 * 1000));
	}

	/**
	 * 日期相减,返回相减后的毫秒数 Examples:(列举一些调用的例子)
	 * 
	 * @param date
	 * @param date1
	 * @return(设定参数)
	 * @return int相减后的毫秒数
	 * @author John 2013-5-29
	 */
	public static int diffMillis(Date date, Date date1) {
		return (int) ((getMillis(date) - getMillis(date1)));
	}

	/**
	 * 取得指定月份的第一天
	 * 
	 * @param strdate
	 *            String
	 * @return String
	 */
	public static String getMonthBegin(String strdate) {
		Date date = parseDate(strdate);
		return format(date, "yyyy-MM") + "-01";
	}

	/**
	 * 取得指定月份的最后一天
	 * 
	 * @param strdate
	 *            String
	 * @return String
	 */
	public static String getMonthEnd(String strdate) {
		Date date = parseDate(getMonthBegin(strdate));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		return formatDate(calendar.getTime());
	}

	/**
	 * 常用的格式化日期
	 * 
	 * @param date
	 *            Date
	 * @return String
	 */
	public static String formatDate(Date date) {
		// 添加判断
		if (null == date) {
			return "";
		}
		return formatDateByFormat(date, "yyyy-MM-dd");
	}

	public static String formatDateSimple(Date date) {
		// 添加判断
		if (null == date) {
			return "";
		}
		return formatDateByFormat(date, YEAR_TO_DAY_UN_LINE);
	}

	public static String formatDateTimeSimple(Date date) {
		// 添加判断
		if (null == date) {
			return "";
		}
		return formatDateByFormat(date, YEAR_TO_MI_UN_LINE);
	}

	public static Date parseSimpleDate(String dateStr) {
		return parseDate(dateStr, YEAR_TO_DAY_UN_LINE);
	}

	public static Date parseSimpleDateTime(String dateStr) {
		return parseDate(dateStr, YEAR_TO_MI_UN_LINE);
	}

	/**
	 * 以指定的格式来格式化日期
	 * 
	 * @param date
	 *            Date
	 * @param format
	 *            String
	 * @return String
	 */
	public static String formatDateByFormat(Date date, String format) {
		String result = "";
		if (date != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				result = sdf.format(date);
			} catch (Exception ex) {
				logger.error(date + "|" + format , ex);
			}
		}
		return result;
	}

	/**
	 * 去掉Date里面的时分秒
	 * 
	 * @param Date
	 * @return 去掉时分秒后的Date，若参数为空则返回原日期
	 * @author 王柳新
	 */
	public static Date trimHmsForDate(Date date) {
		Date result = date;
		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.clear(Calendar.MINUTE);
			cal.clear(Calendar.SECOND);
			cal.clear(Calendar.MILLISECOND);
			result = cal.getTime();
		}
		return result;
	}

	/**
	 * 判断两个时间是否在同一天
	 * 
	 * @param d1
	 * @param d2
	 * @return(设定参数)
	 * @return boolean(返回值说明)
	 * @author soarin 2013-9-30
	 */
	public static boolean bSameDay(Date d1, Date d2) {
		if (d1 == null || d2 == null)
			return false;

		return DateUtil.formatDate(d1).equals(DateUtil.formatDate(d2));
	}

	/**
	 * 以当前日期为参照，根据偏移量，获取日期
	 * 
	 * @param dOffset
	 *            日期偏移量(以天为单位)
	 * @return
	 * @author 田红兵 2012-6-12
	 */
	public static Date getDateByOffset(int dOffset) {
		return getDateByOffset(getSystemTm(), dOffset);
	}

	/**
	 * 根据偏移量，获取日期
	 * 
	 * @param dOffset
	 *            日期偏移量(以天为单位)
	 * @return
	 * @author 田红兵 2012-6-12
	 */
	public static Date getDateByOffset(Date d, int dOffset) {
		if (null == d)
			return null;
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.DATE, dOffset);
		return c.getTime();
	}

	/**
	 * 以当前日期为参照，根据d/h/m移量，获取日期
	 * 
	 * @param dOffset
	 *            日期偏移量(以天为单位)
	 * @param hOffset
	 *            小时偏移量
	 * @param mOffset
	 *            分钟偏移量
	 * @return
	 * @author 田红兵 2012-6-12
	 */
	public static Date getDateByOffset(int dOffset, int hOffset, int mOffset) {
		return getDateByOffset(getSystemTm(), dOffset, hOffset, mOffset);
	}

	/**
	 * 根据d/h/m移量，获取日期
	 * 
	 * @param dOffset
	 *            日期偏移量(以天为单位)
	 * @param hOffset
	 *            小时偏移量
	 * @param mOffset
	 *            分钟偏移量
	 * @return
	 * @author 田红兵 2012-6-12
	 */
	public static Date getDateByOffset(Date d, int dOffset, int hOffset,
			int mOffset) {
		if (null == d)
			return null;
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.DATE, dOffset);
		c.add(Calendar.HOUR, hOffset);
		c.add(Calendar.MINUTE, mOffset);
		return c.getTime();
	}

	/**
	 * 比较两个日期(第二个日期减去第一个日期)，返回以分钟为最小单位的，最大以小时显示的字符窜(如：36H28 表示36小时28分)
	 * 
	 * @param firstDt
	 *            第一个日期
	 * @param secondDt
	 *            第二个日期
	 * @return
	 * @author 田红兵
	 */
	public static String compareDate(Date firstDt, Date secondDt) {

		if (null == firstDt || null == secondDt)
			return "";
		long diff = (secondDt.getTime() - firstDt.getTime()) / (60 * 1000);
		return formatHourAndMinByMin(diff);
	}

	/**
	 * 根据分钟数格式化成小时和分钟字符窜(如：200 -> 3H20)
	 * 
	 * @param minute
	 * @return
	 * @author 田红兵
	 */
	public static String formatHourAndMinByMin(long minute) {
		StringBuffer sb = new StringBuffer();
		if (minute < 0)
			sb.append("-");
		long m = Math.abs(minute % 60);
		long h = Math.abs(minute / 60);
		if (h > 0) {
			sb.append(h);
			sb.append("H");
		}
		if (m != 0)
			sb.append(m);
		return sb.toString();
	}

	/**
	 * 比较两个日期(第二个日期减去第一个日期)，间隔2小时以上以分钟为单位，间隔2小时以上以小时+分钟格式显示的字符窜(如：36H28
	 * 表示36小时28分，91表示1小时31分钟)
	 * 
	 * @param firstDt
	 *            第一个日期
	 * @param secondDt
	 *            第二个日期
	 * @return
	 * @author 田红兵
	 */
	public static String compareDateTransfer(Date firstDt, Date secondDt) {

		if (null == firstDt || null == secondDt)
			return "";
		long diff = (secondDt.getTime() - firstDt.getTime()) / (60 * 1000);
		return formatHourAndMinByMinTransfer(diff);
	}

	/**
	 * 2小时及以下直接显示分钟数，2小时以上根据分钟数格式化成小时和分钟字符窜(如：200 -> 3H20,91 -> 91)
	 * 
	 * @param minute
	 * @return
	 * @author 田红兵
	 */
	public static String formatHourAndMinByMinTransfer(long minute) {
		StringBuffer sb = new StringBuffer();
		if (minute < 0)
			sb.append("-");
		if (minute >= 0 && minute <= 120) {
			sb.append(minute);
		} else {
			long m = Math.abs(minute % 60);
			long h = Math.abs(minute / 60);
			if (h > 0) {
				sb.append(h);
				sb.append("H");
			}
			if (m != 0)
				sb.append(m);
		}
		return sb.toString();
	}

	/**
	 * 两个时间差距多少秒
	 * 
	 * @return
	 */
	public static long getDateMinus(Date d1, Date d2) {
		long v = d1.getTime() - d2.getTime();
		if (v < 0) {
			v = -v;
		}
		return v;
	}

	/**
	 * 时间减去多少分
	 * 
	 * @param date
	 * @param min
	 * @return
	 */
	public static Date deffDate(Date date, String min) {
		if (date == null || min == null || "".equals(min))
			return null;
		try {
			int m = Integer.parseInt(min);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.setTimeInMillis(c.getTimeInMillis() - ((long) m) * 60 * 1000);
			return c.getTime();
		} catch (Exception e) {
			logger.error(date + "|" + min , e);
		}
		return null;
	}

	/**
	 * 日期相减
	 * 
	 * @param date
	 *            日期
	 * @param date1
	 *            日期
	 * @return 返回相减后的分钟数
	 */
	public static int diffMinute(Date date, Date date1) {
		if (date == null || date1 == null)
			return 0;
		return (int) ((getMillis(date) - getMillis(date1)) / (60 * 1000));
	}

	/**
	 * 将java.util.Date 转换成 java.sql.Timestamp 用于PreparedStatement
	 * 
	 * @param date
	 * @return
	 */
	public static java.sql.Timestamp transDate(java.util.Date date) {
		if (date == null)
			return null;
		return new java.sql.Timestamp(date.getTime());
	}

	public static Integer judgeDate(Date s, Date e) {
		int mark = 0;
		Long ss = s.getTime();
		Long ee = e.getTime();
		if (ss - ee == 0) {
			mark = 0;
		} else if (ss - ee > 0) {
			mark = 1;
		} else if (ss - ee < 0) {
			mark = -1;
		}
		return mark;
	}

	/**
	 * 转换2013-01-18 00:00:0 字符日期格式为2013-01-18字符日期格式 百年之后这个函数就不对了。 by soarin
	 */
	public static String transDateStrToStr(String fltDtStr) {
		String fltDt = "";
		if (fltDtStr != null && !"".equals(fltDtStr)) {
			String fltDtArr[] = fltDtStr.split(" ");
			fltDt = fltDtArr[0];
		}
		return fltDt;
	}

	/**
	 * 两个时间是否相等
	 * 
	 * @param d1
	 * @param d2
	 * @return(设定参数)
	 * @return boolean(返回值说明)
	 * @author soarin 2013-12-6
	 */
	public static boolean bEqual(Date d1, Date d2) {
		if (null == d1) {
			return null == d2;
		} else {
			if (null == d2) {
				return false;
			}
			return d1.compareTo(d2) == 0;
		}
	}

	public static void setSystemDiffTime(long diffTime) {
		SYSTEM_DIFF_TIME = diffTime;
	}

	/**
	 * 获取系统时间
	 * 
	 * @return(设定参数)
	 * @return Date(返回值说明)
	 * @author soarin 2013-5-13
	 */
	public static Date getSystemTm() {
		return new Date(new Date().getTime() + SYSTEM_DIFF_TIME);
	}

	/**
	 * 以指定的格式把Date转换成String（Locale为English)
	 * 
	 * @param date
	 *            日期
	 * @param format
	 *            格式
	 * @return String
	 * @author 王柳新
	 * @since 2013-6-9
	 */
	public static String formatDateByFormatWithEnglishLocale(Date date,
			String format) {
		String result = "";
		if (date != null && StringUtils.isNotBlank(format)) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(format,
						Locale.ENGLISH);
				result = sdf.format(date);
			} catch (Exception e) {
				logger.error(date + "|" + format , e);
			}
		}
		return result;
	}

	/**
	 * 以指定的格式把String转换成Date（Locale为English)
	 * 
	 * @param dateStr
	 *            日期字符串
	 * @param format
	 *            格式
	 * @return Date
	 * @author 王柳新
	 * @since 2013-6-9
	 */
	public static Date parseDateByFormatWithEnglishLocale(String dateStr,
			String format) {
		Date result = null;
		if (StringUtils.isNotBlank(dateStr) && StringUtils.isNotBlank(format)) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(format,
						Locale.ENGLISH);
				result = (Date) sdf.parse(dateStr);
			} catch (Exception e) {
				logger.error(dateStr + "|" + format , e);
			}
		}
		return result;
	}

	/**
	 * 日期验证,dateStr格式需要是:yyyyMMddHHmmss或者yyyyMMdd
	 * 因为parseDate方法当dateStr不是日期格式时，也会返回一个错误的日期，不满足要求，所以新增日期验证方法
	 * 
	 * @description:
	 * @param dateStr
	 * @return 是日期，返回true
	 * @author John 2013-12-17
	 */
	public static boolean isValidDate(String dateStr) {
		// String eL =
		// "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1][0-9])|([2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
		String eL = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))((((0?[0-9])|([1][0-9])|([2][0-3]))([0-5]?[0-9])((\\s)|(([0-5]?[0-9])))))?$";
		Pattern p = Pattern.compile(eL);
		Matcher m = p.matcher(dateStr);
		return m.matches();
	}

	/**
	 * 设置日期偏移量
	 * 
	 * @description: 功能描述
	 * @param d
	 *            所要偏移的日期
	 * @param field
	 *            字段(例如:Calendar.MINUTE,Calendar.HOUR),field the calendar
	 *            field.注意：此参数必须与Calendar类定义的常量对应，否则会有错误
	 * @param amount
	 *            偏移量 the amount of date or time to be added to the field.
	 * @return Date(返回值说明)
	 * @author luxinming 2013-11-20
	 */
	public static Date setDateByOffset(Date d, int field, int amount) {
		if (null == d) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(field, amount);
		return c.getTime();
	}

	/**
	 * 
	 * 获取2个时间的差值，并格式化成xxdxxmxxs 扣除整数天后，如果时间小于等于二小时以内，则格式化成xxdxxm
	 * 
	 * @param startDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @return
	 * @author liyongsen
	 * @CreateDate 2014-07-29
	 */
	public static String getConnectTimeStr(Date startDate, Date endDate) {
		if (null == startDate || null == endDate) {
			return null;
		}
		long diffTime = diffMinute(endDate, startDate);
		String connectTimeStr = timeFormart((diffTime * 60 * 1000));
		return connectTimeStr;
	}

	/**
	 * 把时间转成字符串xxdxxmxxs 扣除整数天后，如果时间小于等于二小时以内，则格式化成xxdxxm
	 * 
	 * @param time
	 *            时间
	 * @return String 字符串形式的时间
	 * @author liyongsen
	 * @CreateDate 2014-07-29
	 */
	public static String timeFormart(long time) {
		StringBuilder buffer = new StringBuilder();
		long day = time / (24 * 60 * 60 * 1000);// 计算天数
		long hour = 0;
		long min = 0;
		if (time < 0) {
			time = time * -1;
			buffer.append("-");// 负数
		}
		if (day > 0) {
			time = time - day * (24 * 60 * 60 * 1000);// 剩下多少小时
			buffer.append(day).append("d");
		}
		hour = time / (60 * 60 * 1000);// 计算小时
		if (hour >= 0) {
			if (time <= (2 * 60 * 60 * 1000)) {// 如果时间小于等于二小时以内
				long mins = time / (60 * 1000);
				buffer.append(mins).append("m");
			} else {
				time = time - hour * 60 * 60 * 1000;// 剩下多少小时
				buffer.append(hour).append("h");
				min = time / (60 * 1000);// 剩下多少分钟
				buffer.append(min).append("m");
			}
		}
		return buffer.toString();
	}

	/**
	 * 日期格式转化，"MM-dd HHmm"--->"MM-dd HH:mm"
	 * 
	 * @param dt
	 * @param formatTypeA
	 * @param formatTypeB
	 * @return
	 */
	public static String formatDt(String dt, String formatTypeA, String formatTypeB) {
		if (dt == null || dt.equals("")) {
			return dt;
		}

		DateFormat format1 = new SimpleDateFormat(formatTypeA);
		DateFormat format2 = new SimpleDateFormat(formatTypeB);

		try {
			dt = format2.format(format1.parse(dt));
		} catch (ParseException e) {
			logger.error("formatDt eror:" + dt + "|" + formatTypeA +"|" + formatTypeB,e);
		}
		return dt;
	}

	/**
	 * <p>Title: getDateFromDMY</p>
	 * <p>Description: 把"ddMMMyy"格式的字符串转换为Date</p>
	 * @param birthday "ddMMMyy"格式的字符串
	 * @param refDate 参考日期，如航班日期
	 * @return Date
	 * @author 王柳新  2015-1-6
	 */
	public static Date getDateFromDMY(String str, Date refDate) {
		if (StringUtils.isBlank(str) || str.length()!=7 || refDate==null) {
			return null;
		}
		
		String yy = str.substring(5, 7);
		String refYear = getYearToDayUnLine(refDate).substring(0, 4);//参考年份yyyy
		String actYear;//实际年份yyyy
		
		if (yy.compareTo(refYear.substring(2, 4)) > 0) {
			actYear = String.valueOf(Integer.parseInt(refYear.substring(0, 2)) - 1) + yy;
		} else {
			actYear = refYear;
		}
		
		return parseYearToDayUnLine(actYear + getMonth(str.substring(2, 5).toUpperCase()) + str.substring(0, 2));
	}

	/**
	 * Date与long的转化
	 * @param date
	 * @return(设定参数)
	 * @return Date(返回值说明)
	 * @author yintaibing  2015年11月29日
	 */
	public static Date getDateFromLong(long date) {
		return new Date(date);
	}
	
	/**
	 * 从IBE格式的字符串（如25DEC09 1155）获取时间
	 * 日期格式有日（DD），月（MM、MMM），年（Y、YY、YYYY），时（HH），分（MI），秒（SS）
	 * @param dateStr
	 * @return(设定参数)
	 * @return Date(返回值说明)
	 * @exception/throws
	 * @author Administrator  2015年11月30日
	 */
	public static Date getDateFromIBEFormat(String dateStr, String format) throws Exception {
		if (StringUtils.isBlank(dateStr) || StringUtils.isBlank(format)
				|| dateStr.length() != format.length()) {
			throw new Exception("日期格式错误!date=" + dateStr + "|format=" + format);			
		}
	
		// 年
		int yearStrStart = -1;
		String year = null;
		if (format.contains("YYYY")) {
			yearStrStart = format.indexOf("YYYY");
			year = dateStr.substring(yearStrStart, yearStrStart + 4);
		} else if (format.contains("YY")) {
			yearStrStart = format.indexOf("YY");
			// year = "19" + 99; year = "20" + 15;
			char decade = dateStr.charAt(yearStrStart);
			String yearStr = dateStr.substring(yearStrStart, yearStrStart + 2);
			year = decade >= '7' ? ("19" + yearStr) : ("20" + yearStr);
		} else if (format.contains("Y")) {
			yearStrStart = format.indexOf("Y");
			// year = (2015 / 10) + "5";
			year = Calendar.getInstance(Locale.CHINA).get(Calendar.YEAR) / 10 + 
					dateStr.substring(yearStrStart, yearStrStart + 1);
		}

		// 月
		int monthStrStart = -1;
		String month = null;
		if (format.contains("MMM")) {
			monthStrStart = format.indexOf("MMM");
			int monthInt = getMonthByEnName(dateStr.substring(
					monthStrStart, monthStrStart + 3));
			if (monthInt > -1) {
				// month = "0" + 5;
				month = monthInt > 9 ? String.valueOf(monthInt) : ("0" + monthInt);
			}
		} else if (format.contains("MM")) {
			monthStrStart = format.indexOf("MM");
			month = dateStr.substring(monthStrStart, monthStrStart + 2);
		}
		
		// 日
		int dayStrStart = format.indexOf("DD");
		String day = dayStrStart > -1 ? dateStr.substring(dayStrStart, dayStrStart + 2) : null;
		
		// 时
		int hourStrStart = format.indexOf("HH");
		String hour = hourStrStart > -1 ? dateStr.substring(hourStrStart, hourStrStart + 2) : null;
		
		// 分
		int minStrStart = format.indexOf("MI");
		String min = minStrStart > -1 ? dateStr.substring(minStrStart, minStrStart + 2) : null;
		
		// 秒
		int secStrStart = format.indexOf("SS");
		String sec = secStrStart > -1 ? dateStr.substring(secStrStart, secStrStart + 2) : null;
		
		StringBuffer sb = new StringBuffer();
		// month和day不能为空
		if (month == null || day == null) {
			return null;
		} else {
			if (year == null) {
				// 若年为空，则默认取当年
				sb.append(Calendar.getInstance(Locale.CHINA).get(Calendar.YEAR));
			} else {
				sb.append(year);
			}
			sb.append(month);
			sb.append(day);
			sb.append(hour == null ? "00" : hour);
			sb.append(min == null ? "00" : min);
			sb.append(sec == null ? "00" : sec);
		}
		if (StringUtils.isBlank(sb.toString())) {
			throw new Exception("日期解析错误!date=" + dateStr + "|format=" + format);
		} else {
			DateFormat formatter = new SimpleDateFormat(YEAR_TO_SEC_NO_BLANK, Locale.CHINA);
			return formatter.parse(sb.toString());
		}
	}
	
	private static int getMonthByEnName(String month) {
		for (int i = 1; i <= MONTH_EN.length; i++) {
			if (month.equals(MONTH_EN[i - 1])) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * 根据日期获取周的天数
	 * @param date
	 * @return
	 */
	public static int getDayOfWeek(Date date) {
		 Calendar cal = Calendar.getInstance();  
		 cal.setTimeInMillis(date.getTime());   
		 int x = cal.get(Calendar.DAY_OF_WEEK);
		 return x;
	}
	
	
	
	

	/**
	 * 根据日期获取当前周的开始日期和结束日期
	 * @param date
	 * @return
	 */
	public static Map<String, Date> getStartDateAndEndDateByDate(String date) {
		Map<String, Date> resultMap = null;
		if (StringUtils.isNotEmpty(date)) {
			try {
				resultMap = new HashMap<String, Date>();
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				cal.setTime(df.parse(date));
				cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY); // 获取本周一的日期
				resultMap.put("startDate", cal.getTime());
				// 这种输出的是上个星期周日的日期，因为老外那边把周日当成第一天
				cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
				// 增加一个星期，才是我们中国人理解的本周日的日期
				// cal.add(Calendar.WEEK_OF_YEAR, 1);
				resultMap.put("endDate", cal.getTime());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return resultMap;
	}
	
	/**
	 *  将CXF转换后的CST格式Date,转换成正确的Date.
	 * @param date
	 * @return
	 */
	public static Date convertCstDate(Date date) {
		Date result = null;

		if (date == null) {
		   return null;	
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
		try {
			result = (Date) sdf.parse(convertCstDateStr(date.toString()));
		} catch (ParseException e) {
			logger.error("convertCstDateStr failed: ", e);
			e.printStackTrace();
		}
			 	 
	   return result;
	}	
	
	/**
	 *  将CXF转换后的CST格式Date字符串,转换成 yy-mm-dd hh:mm:ss格式字符串.
	 *  eg: Wed Jul 20 00:00:00 CST 2016 --> 2016-07-20 00:00:00
	 * @param date
	 * @return格式
	 */
	public static String convertCstDateStr(String dateStr) {
		String result = "";

		// cfx: 传递日期时，为CST格式，客户端为  CST China Standard Time UT 8:00 --》   CST Central Standard Time (USA) UT-6:00
		SimpleDateFormat  cstFormater = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+22"));
        Date date;
		try {
			 sdf.setTimeZone(TimeZone.getTimeZone("GMT+22"));
			 
			 Date utcDate = cstFormater.parse(dateStr);  
			 String fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(utcDate);  
			 date = sdf.parse(fmt);  	
			 result = date.toString();
		} catch (ParseException e) {
			logger.error("convertCstDateStr failed: ", e);
			e.printStackTrace();
		}
			 	
	   return result;
	}	
		
	
	
	
	public static void main(String[] args) throws ParseException {
		String dateStr = "Wed Jul 20 14:00:00 CST 2016"; 
		System.out.println(convertCstDateStr(dateStr));
	}
}
