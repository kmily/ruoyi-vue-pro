package cn.iocoder.yudao.module.biz.util;

import lombok.extern.slf4j.Slf4j;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 */
@Slf4j
public class DateUtil {
    private final static String[] CN_Digits =
            {"〇", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十"};
    public static final long daySpan = 24 * 60 * 60 * 1000;
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String TIME_FORMAT_NORMAL = "yyyy-MM-dd HH:mm:ss";

    public static final String MILLS_NORMAL = "HH:mm:ss";
    /**
     * yyyy-MM-dd
     */
    public static final String DATE_FORMAT_NORMAL = "yyyy-MM-dd";
    /**
     * yyyy.MM.dd
     */
    public static final String DATE_FORMAT_DOT = "yyyy.MM.dd";
    /**
     * yyyyMMdd
     */
    public static final String DATE_FORMAT_NO_MINUS = "yyyyMMdd";
    /**
     * yyMMdd
     */
    public static final String DATE_FORMAT_NO_MINUS_SHORT = "yyMMdd";
    /**
     * yyyy-MM
     */
    public static final String MONTH_FORMAT_NORMAL = "yyyy-MM";

    public static final String YEAR_FORMAT_NORMAL = "yyyy";
    /**
     * MM-dd
     */
    public static final String MONTH_DAY_FORMAT = "MM-dd";
    /**
     * yyyyMMdd
     */
    public static final String DATE_FORMAT_SHORT = "yyyyMMdd";
    /**
     * yyyyMM
     */
    public static final String MONTH_FORMAT = "yyyyMM";
    /**
     * yyyy.MM
     */
    public static final String MONTH_FORMAT_DOT = "yyyy.MM";
    /**
     * yyyyMMddHHmm
     */
    public static final String DATE_FORMAT_MINUTE = "yyyyMMddHHmm";
    /**
     * yyyyMMddHHmmss
     */
    private static final String TIME_FORMAT_SHORT = "yyyyMMddHHmmss";
    /**
     * MM/dd/yyyy HH:mm:ss
     **/
    public static final String MONTH_DAY_YEAR_HOUR_MINUTE = "MM/dd/yyyy HH:mm:ss";

    public static final String YYMMDD = "yyMMdd";

    public static final String DATE_FORMAT_NORMAL_CN = "MM月dd日";

    /**
     * 判断参数year、month、day能否组成一个合法的日期。
     *
     * @param month 月份，合法月份范围是 1-12
     * @param day   日数
     * @param year  年份，必须大于1900
     * @return boolean
     */
    public static boolean isDate(int month, int day, int year) {
        if (year < 1900) {
            return false;
        }
        if (month < 1 || month > 12) {
            return false;
        }
        if (day < 1 || day > 31) {
            return false;
        }

        // 判断年份是否为闰年
        @SuppressWarnings("unused")
        boolean leapYear = isLeapYear(year);
        // 获得该年该月的最大日期
        int maxD = getMaxDay(year, month);
        if (day > maxD) {
            return false;
        } else {
            return true;
        }
    }

    public static Date addDays(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, day);
        return cal.getTime();
    }

    /**
     * 给定一个年份和月份，可以得到该月的最大日期。 例如 2009年1月，最大日期为31。
     *
     * @param year  年份，必须大于1900
     * @param month 月份，合法月份范围是 1-12
     * @return i
     */
    public static int getMaxDay(int year, int month) {
        if ((month == 4) || (month == 6) || (month == 9) || (month == 11)) {
            return 30;
        }
        if (month == 2) {
            if (isLeapYear(year)) {
                return 29;
            } else {
                return 28;
            }
        }
        return 31;
    }

    /**
     * 判断年份是否为闰年。
     *
     * @param year 年份，必须大于1900
     * @return
     */
    public static boolean isLeapYear(int year) {
        boolean leapYear = ((year % 400) == 0);
        if (!leapYear) {
            leapYear = ((year % 4) == 0) && ((year % 100) != 0);
        }
        return leapYear;
    }

    public static String Date4FormatStr(Date date, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        String formatDate = format.format(date);
        return formatDate;
    }

    public static String getCurrentStringTime() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String formatDate = format.format(date);
        return formatDate;
    }

    public static String getCurrentTimeSecond() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDate = format.format(date);
        return formatDate;
    }

    /**
     * yyyy-MM-dd HH:mm:ss格式串转换为日期
     *
     * @param formatDate yyyy-MM-dd HH:mm:ss 格式日期
     * @return Date日期
     */
    public static Date paseDate(String formatDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(formatDate);
        } catch (ParseException e) {
            log.error("发生异常", e);
        }
        return date;
    }

    /**
     *
     */
    public static Date paseDate(String formatDate, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = format.parse(formatDate);
        } catch (ParseException e) {
            log.error("发生异常", e);
        }
        return date;
    }

    public static String getCurrentTimeMilliSecond() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        String formatDate = format.format(date);
        return formatDate;
    }

    public static String getCurrentMonth() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        String formatDate = format.format(date);
        return formatDate;
    }

    /**
     * 获取当前日期（格式为20110802）
     *
     * @return
     */
    public static String getCurrentDay() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String formatDate = format.format(date);
        return formatDate;
    }

    /**
     * 获取当前时间
     *
     * @param format 时间格式，例如：yyyy-MM-dd
     * @param count  增加或减少的天数
     * @return
     */
    public static String getCurrentDate(String format, Integer count) {
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, count);// 增加或减少的天数
        String currentDate = df.format(cal.getTime());
        return currentDate;
    }

    /**
     * 增加月份后的日期数
     *
     * @param dateStr 时间格式 yyyy-MM-dd
     * @param m       增加的月数
     * @return
     */
    public static String getDateAddMoney(String dateStr, int m) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = df.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, m);
            return df.format(calendar.getTime());
        } catch (ParseException e) {
            log.error("发生异常", e);
        }
        return null;
    }

    /**
     * 获取下个月的第一天
     *
     * @param format
     * @return
     */
    public static String getNextMonthFirstDay(String format) {
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);// 当前月＋1，即下个月
        cal.set(Calendar.DATE, 1);// 将下个月1号作为日期初始值
        String currentMonth = df.format(cal.getTime());
        return currentMonth;
    }

    /**
     * 获取下个月的最后一天
     *
     * @param format
     * @return
     */
    public static String getNextMonthLastDay(String format) {
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);// 将下2个月1号作为日期初始值
        cal.add(Calendar.MONTH, 2);// 当前月＋2，即下2个月
        cal.add(Calendar.DATE, -1);// 下2个月1号减去一天，即得到下1一个月最后一天
        String currentMonth = df.format(cal.getTime());
        return currentMonth;
    }

    public static Date getMonthLastDay(Date date , String format) {
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, 1);// 将下2个月1号作为日期初始值
        cal.add(Calendar.MONTH, 1);// 当前月＋2，即下2个月
        cal.add(Calendar.DATE, -1);// 下2个月1号减去一天，即得到下1一个月最后一天
        return cal.getTime();
    }

    /**
     * 获取本月第一天
     */
    public static Date getMonthFirstDay(Date date , String format) {
        Calendar cale = Calendar.getInstance();
        cale.setTime(date);
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        // 将小时至0
        cale.set(Calendar.HOUR_OF_DAY, 0);
        // 将分钟至0
        cale.set(Calendar.MINUTE, 0);
        // 将秒至0
        cale.set(Calendar.SECOND, 0);
        // 将毫秒至0
        cale.set(Calendar.MILLISECOND, 0);
        return cale.getTime();
    }

    public static void main(String [] args)
    {
        Date d = getMonthFirstDay(new Date(),DateUtil.DATE_FORMAT_NORMAL);
        System.out.println(DateUtil.format(d,DateUtil.DATE_FORMAT_NORMAL));
    }

    public static String format(Date date) {
        if (null == date) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDate = format.format(date);
        return formatDate;
    }

    public static String format(Date date, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        String formatDate = format.format(date);
        return formatDate;
    }

    public static Date getDateWithZeroHMS(Date date) {
        Date result = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String s = sdf.format(date);
        try {
            result = sdf.parse(s);
        } catch (ParseException e) {

        }
        return result;
    }


    public static String format4Null(Date date, String formatStr) {

        if (date == null) {
            return null;
        } else {

            return format(date, formatStr);
        }
    }

    /**
     * 得到2个字符串日期之间的日期差,返回结果以秒为单位
     *
     * @param beginTime
     * @param endTime
     * @return
     * @throws ParseException
     */
    public static Long getOffTime(String beginTime, String endTime) {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            java.util.Date begin = dfs.parse(beginTime);
            java.util.Date end = dfs.parse(endTime);
            long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒
            return between;
        } catch (Exception e) {
            log.error("发生异常", e);
        }
        return null;
    }

    public static String getYesterday() {
        return getCurrentDate("yyyy-MM-dd", -1) + " 00:00:00";
    }

    public static int getYear() {
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return Integer.parseInt(df.format(cal.getTime()));
    }

    /**
     * 获取指定某天的23点59分59秒
     *
     * @return
     */
    public static Date getLastTimeByFixDate(Date date) {
        Calendar cal = Calendar.getInstance();
        String dateStr = Date4FormatStr(date, DATE_FORMAT_NORMAL);
        dateStr = dateStr + "  23:59:59";
        return paseDate(dateStr);
    }


    public static Date getStartTimeByFixDate(Date date) {
        Calendar cal = Calendar.getInstance();
        String dateStr = Date4FormatStr(date, DATE_FORMAT_NORMAL);
        dateStr = dateStr + "  00:00:00";
        return paseDate(dateStr);
    }

    public static Date getDateByFixTime(Date date, String hour, String minute, String second) {
        Calendar cal = Calendar.getInstance();
        StringBuffer sb = new StringBuffer();
        sb.append(Date4FormatStr(date, DATE_FORMAT_NORMAL)).append(" ")
                .append(hour).append(":").append(minute).append(":").append(second);
        return paseDate(sb.toString());
    }

    public static String getTodayStr() {
        return getCurrentDate("yyyy-MM-dd", 0) + " 00:00:00";
    }

    public static Date getToday() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NORMAL);
        Date time = null;
        try {
            time = sdf.parse(sdf.format(new Date()));
        } catch (ParseException e) {

            log.error("发生异常", e);
        }
        return time;
    }

    public static long getTodayLong() {
        String today = getCurrentDate("yyyy-MM-dd", 0) + " 00:00:00";
        return paseDate(today).getTime();
    }


    /**
     * 计算两个日期之间相差的月数
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 两个日期之间相差的月数
     */
    public static int dateIntervalMonth(Date startDate, Date endDate) {

        int intervalMonth = 0;
        try {
            if (startDate == null || endDate == null) {
                return intervalMonth;
            }
            Calendar starCal = Calendar.getInstance();
            starCal.setTime(startDate);

            int sYear = starCal.get(Calendar.YEAR);
            int sMonth = starCal.get(Calendar.MONTH);

            Calendar endCal = Calendar.getInstance();
            endCal.setTime(endDate);
            int eYear = endCal.get(Calendar.YEAR);
            int eMonth = endCal.get(Calendar.MONTH);

            intervalMonth = ((eYear - sYear) * 12 + (eMonth - sMonth));
        } catch (Exception e) {
            intervalMonth = 0;
        }

        return intervalMonth;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int dateIntervalDay(Date startDate, Date endDate) {
        if (null == startDate || null == endDate) return 0;

        long nd = 1000 * 24 * 60 * 60;
        long diff = endDate.getTime() - startDate.getTime();

        //计算天
        long day = diff / nd;

        return Integer.parseInt(day + "");
    }

    /***
     * 根据所选格式将字符串转换为日期类型
     *
     * @param dateStr
     *            待转换的字符串
     * @param formateRule
     *            转换格式
     * @return 日期类型结果
     */
    public static Date convertAsDate(String dateStr, String formateRule) {
        DateFormat fmt = new SimpleDateFormat(formateRule);
        try {
            if (dateStr == null) {
                return null;
            }
            return fmt.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    public static SimpleDateFormat newTimeFormater() {
        return new SimpleDateFormat(TIME_FORMAT_NORMAL);
    }

    /**
     * 计算时间差，返回秒
     *
     * @param start
     * @param end
     * @return
     */
    public static long timeDifference(long start, long end) {
        return (end - start) / 1000;
    }

    public static Integer millisToTodayEnd() {
        Long start = System.currentTimeMillis();
        Long end = DateUtil.getDateByFixTime(new Date(), "23", "59", "59").getTime();
        Integer diff = Math.toIntExact(end - start);
        return diff;
    }


    /*
     * 判断输入的字符串是否是合法的生日 生日不能大于当前日期,支持 yyyy-MM-dd ,yyyyMMdd MM-dd-yyyy
     * ,yyyy年MM月dd日等
     *
     * @param birthday 一个日期格式的字符串
     *
     * @param formats 期望转换后的日期格式数组
     *
     * @return
     */
    public static boolean isRightDate(String birthday, String[] formats) {

        // 记录传入的日期字符串，转换成日期类型
        Date birth = null;

        // 判断格式是否正确，默认值为false
        boolean isRight = false;
        for (String f : formats) {
            try {
                birth = new SimpleDateFormat(f).parse(birthday);
                // 校验日期转换后是和传入的值不相同，说明日期传入有问题
                if (!new SimpleDateFormat(f).format(birth).equals(birthday)) {
                    return false;
                }
                isRight = true;
                break;
            } catch (ParseException e) {
            }
        }

        if (isRight) {
            // 获取当前日期的毫秒数
            long now = new Date().getTime();
            // 获取生日的毫秒数
            long birthTime = birth.getTime();
            // 如果当前时间小于生日，生日不合法。反之合法
            return birthTime <= now;
        } else {
            // 输入的参数类型不是日期类型，或者类型和过滤中设置的类型不匹配
            return false;
        }
    }

    /**
     * 获取本月第一天
     */
    public static Date getCurrentMonFirstDay() {
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        // 将小时至0
        cale.set(Calendar.HOUR_OF_DAY, 0);
        // 将分钟至0
        cale.set(Calendar.MINUTE, 0);
        // 将秒至0
        cale.set(Calendar.SECOND, 0);
        // 将毫秒至0
        cale.set(Calendar.MILLISECOND, 0);
        return cale.getTime();
    }


    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static Date getTomorrow() {
        Date date = new Date();//取时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);//把日期+1天
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 获取上午/下午 时分
     */
    public static String getAhh(Date dt) {
        return new SimpleDateFormat("ahh:mm").format(new Date());
    }

    //2020-01-07 星期二 下午
//    public static String getFormatDateDayAndAh(Date dt) {
//        String str = DateUtil.format(dt, DateUtil.DATE_FORMAT_NORMAL) + " " + DateUtil.getWeekOfDate(dt) + " "+ new SimpleDateFormat("a").format(dt);
//        return str;
//    }

    public static String getFormatDateDayAndAh(Date dt) {
        Calendar now = Calendar.getInstance();
        now.setTime(dt);
        int hour = now.get(Calendar.HOUR_OF_DAY);
        String str = DateUtil.format(dt, DateUtil.DATE_FORMAT_NORMAL) + " " + DateUtil.getWeekOfDate(dt) + " " + (hour == 0 ? "上午" : "下午");
        return str;
    }

    /**
     * 获得某个月最大天数
     *
     * @param monthDate yyyy-MM
     * @return 某个月最大天数
     */
    public static int getMaxDayByYearMonth(String monthDate) {
        String[] arr = monthDate.split("-");
        int year = Integer.valueOf(arr[0]);
        int month = Integer.valueOf(arr[1]) - 1;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year - 1);
        calendar.set(Calendar.MONTH, month);
        return calendar.getActualMaximum(Calendar.DATE);
    }



    /**
     * 判断指定日期是否工作日
     *
     * @param datetime
     * @return
     */
    public static boolean isWorkDay(Date datetime) {
        boolean flag = true;
        Calendar c = Calendar.getInstance();
        c.setTime(datetime);
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ||
                c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            flag = false;
        }
        return flag;
    }

    /**
     * 根据月份查询该月第一天
     */
    public static Date getFirstDayByDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        //设置为1号,当前日期既为本月第一天
        c.set(Calendar.DAY_OF_MONTH, 1);
        //将小时至0
        c.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至0
        c.set(Calendar.MINUTE, 0);
        //将秒至0
        c.set(Calendar.SECOND, 0);
        //将毫秒至0
        c.set(Calendar.MILLISECOND, 0);
        // 获取本月第一天的时间戳
        return c.getTime();
    }

    /**
     * 根据月份查询该月最后一天
     */
    public static Date getLastDayByDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        //设置为当月最后一天
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        //将小时至23
        c.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至59
        c.set(Calendar.MINUTE, 0);
        //将秒至59
        c.set(Calendar.SECOND, 0);
        //将毫秒至999
        c.set(Calendar.MILLISECOND, 0);
        // 获取本月最后一天的时间戳
        return c.getTime();
    }

    /**
     * 根据月份查询该月第一天
     */
    public static Date getFirstDayByMonth(String month) {
        Calendar c = Calendar.getInstance();
        Date date = paseDate(month, MONTH_FORMAT_NORMAL);
        c.setTime(date);
        //设置为1号,当前日期既为本月第一天
        c.set(Calendar.DAY_OF_MONTH, 1);
        //将小时至0
        c.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至0
        c.set(Calendar.MINUTE, 0);
        //将秒至0
        c.set(Calendar.SECOND, 0);
        //将毫秒至0
        c.set(Calendar.MILLISECOND, 0);
        // 获取本月第一天的时间戳
        return c.getTime();
    }

    /**
     * 根据月份查询该月最后一天
     */
    public static Date getLastDayByMonth(String month) {
        Calendar c = Calendar.getInstance();
        Date date = paseDate(month, MONTH_FORMAT_NORMAL);
        c.setTime(date);
        //设置为当月最后一天
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        //将小时至23
        c.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至59
        c.set(Calendar.MINUTE, 0);
        //将秒至59
        c.set(Calendar.SECOND, 0);
        //将毫秒至999
        c.set(Calendar.MILLISECOND, 0);
        // 获取本月最后一天的时间戳
        return c.getTime();
    }

    public static Time getCurrentMills() {

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(1970, 0, 01);
        //将毫秒至999
        c.set(Calendar.MILLISECOND, 0);
        System.out.println(c.getTime());
        return new Time(c.getTime().getTime());
    }


}
