package com.example.calbirthdaylib;

import java.io.Serializable;
import java.util.Calendar;

public class SolarDate implements Serializable {

    private int year;
    private int month;
    private int day;

    public SolarDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public static SolarDate today() {
        return new SolarDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(
                Calendar.DAY_OF_MONTH));
    }

    public static boolean checkValidSolarDate(int year, int month, int day) {
        return (!(year < 1 || month < 1 || month > 12 || day < 1 || day > CalendarDB.getSolarMonthDays(year, month)));
    }

    public boolean isValidDate() {
        return checkValidSolarDate(this.year, this.month, this.day);

    }

    public boolean isLeapYear() {
        return CalendarDB.isLeapYear(this.year);
    }

    public boolean beforeThan(SolarDate other) {
        if (other == null) {
            return false;
        }
        return this.year < other.getYear()
                || ((this.year == other.getYear() && (this.month < other.getMonth() || (this.year == other.getYear() && this.month == other.getMonth() && this.day < other.getDay()))));
    }

    // 只考虑当年的月日
    public boolean beforeThanIgnorYear(SolarDate other) {
        if (other == null) {
            return false;
        }
        if (this.month == other.month) {
            return this.day < other.day;
        } else {
            return this.month < other.month;
        }
    }

    public boolean equals(SolarDate other) {
        if (other == null) {
            return false;
        }
        return this.year == other.getYear() && this.month == other.getMonth() && this.day == other.getDay();
    }

    public boolean afterThan(SolarDate other) {
        if (other == null) {
            return false;
        }
        return (!this.equals(other)) && (!this.beforeThan(other));
    }

    public int getDaysThisYear() {
        return this.isLeapYear() ? 366 : 365;
    }

    private static int getLeapYears(int year) {
        if (year == 0) {
            return 0;
        }
        int y = year - 1;
        return (y / 4 - y / 100 + y / 400);
    }

    /*
     * 计算两个日期之间相隔多少年， 不足12个月的话返回0
     */
    public int getYearsBetween(SolarDate other) {
        if (other == null) {
            return 0;
        }
        return this.getMonthBetween(other) / 12;
    }

    public int getAge(SolarDate other) {
        if (other == null) {
            return 0;
        }
        int age = today().getYear() - other.getYear();
        if (today().beforeThanIgnorYear(other)) {
            age--;
        }
        return age;
    }

    public int getAge(LunarDate other) {
        if (other == null) {
            return 0;
        }
        return this.getAge(other.toSolarDate());
    }

    public int getYearsBetween(LunarDate other) {
        if (other == null) {
            return 0;
        }
        return this.getYearsBetween(other.toSolarDate());
    }

    /*
     * 计算两个日期之间相隔多少个月
     */
    public int getMonthBetween(SolarDate other) {
        if (other == null) {
            return 0;
        }
        int monthes = (other.getYear() - this.year) * 12 + other.month - this.month;
        if (other.month == this.month) {
            if (other.day > this.day) {
                monthes--;
            }
        }
        return monthes;
    }

    public int getMonthBetween(LunarDate other) {
        if (other == null) {
            return 0;
        }
        return this.getMonthBetween(other.toSolarDate());
    }

    /*
     * 与指定日期的相隔天数，返回小于0则代表当前日期在指定日期的未来。 大于0则表示指定日期是未来的日期。
     */
    public int getDaysBetween(SolarDate other) {
        if (this.equals(other) || other == null) {
            return 0;
        }
        int days = (other.getYear() - this.getYear()) * 365;
        days += getLeapYears(other.getYear());
        days -= getLeapYears(this.getYear());
        days += CalendarDB.getSolarYearDays(other.getYear(), other.getMonth(), other.getDay());
        days -= CalendarDB.getSolarYearDays(this.getYear(), this.getMonth(), this.getDay());
        return days;
    }

    public int getDaysBetween(LunarDate other) {
        if (other == null || other.toSolarDate() == null) {
            return 0;
        }
        return this.getDaysBetween(other.toSolarDate());
    }

    /*
     * 返回XX年XX月XX日
     */
    public String formatString() {
        return String.format("%04d年%02d月%02d日", this.year, this.month, this.day);
    }

    /*
     * 返回XX.XX.XX
     */
    public String formatSimpleString(){
        return this.year + "." + this.month + "." + this.day;
    }

    /*
     * 2012-3-28
	 */
    public String formatDateString() {
        return String.format("%04d-%02d-%02d", this.year, this.month, this.day);
    }
    public String formatDateString2(){
        return String.format("%04d%02d%02d", this.year, this.month, this.day);
    }
    public String formatStringWithoutYear() {
        return String.format("%02d月%02d日", this.month, this.day);
    }

    public LunarDate toLunarDate() {
        if (!this.isValidDate() || this.beforeThan(new SolarDate(1901, 12, 19)) || this.afterThan(new SolarDate(2051, 2, 10))) {
            return null;
        }

        // 从当年1月1日到当天的天数，此处如果是1月29日，此处算出来是29天
        int accDays = CalendarDB.getSolarYearDays(this.year, this.month, this.day);

        int lunarYear = this.year;
        // 如果从当年1月1日到当天天数小于等于当年1月1日到当年一月一日的数,（如果一月一日是1月29日那getAccDays算出来是28天),说明还没有到农历新年呢
        if (accDays <= CalendarDB.getAccDays(this.year)) {
            lunarYear--;
            // 下面计算从上一年1月1日到当天的天数
            accDays += 365;
            // 如果上一年是闰年那么多一天
            if (CalendarDB.isLeapYear(lunarYear)) {
                accDays++;
            }
        }

        int lastAccDays = CalendarDB.getAccDays(lunarYear);
        // accDays 是lunarYear年 1月1日到 当天的天数
        // lastAccDays lunarYear年 1月1日到 lunar_y年一月一日的天数
        int lunarMonth = 0;
        int nextAccDays = 0;
        for (lunarMonth = 1; lunarMonth <= 13; lunarMonth++) {
            nextAccDays = lastAccDays + CalendarDB.getFakeLunarMonthDays(lunarYear, lunarMonth);
            if (accDays <= nextAccDays) {
                break;
            }
            lastAccDays = nextAccDays;
        }

        int lunarDay = accDays - lastAccDays;

        int lunarLeapMonth = CalendarDB.getLunarLeapMonth(lunarYear);
        if (lunarLeapMonth != 0) {
            if (lunarMonth > lunarLeapMonth) {
                lunarMonth--;
                // 闰月用负数表示
                if (lunarMonth == lunarLeapMonth) {
                    lunarMonth *= -1;
                }
            }
        }
        return new LunarDate(lunarYear, lunarMonth, lunarDay);

    }

    /*
     * 这个函数的意思是指，从startYear这一年开始（包括startYear),找到一个合法的month月，day日 比如说startyear
     * 2012, month 2, day 29找到下一个有2-29的农历日期。 负数代表提醒，正数代表拖后。 此处有while循环要 小心，
     */
    // 处理月日的异常
    public static SolarDate getValidSolarDateFrom(int month, int day, int startYear, int shiftDays) {
        int year = startYear;
        int shifted = 0;
        while (!SolarDate.checkValidSolarDate(year, month, day) && year <= 2045) {
            if (shiftDays != 0 && SolarDate.checkValidSolarDate(year, month, day + shiftDays)) {
                shifted = 1;
                break;
            }
            year++;
        }
        return new SolarDate(year, month, day - shifted);
    }

    public long getTimestamp(int hour, int min) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day, hour, min, 0);
        return cal.getTime().getTime();
    }

    /*
     * 公历添加n个月
     */
    public SolarDate addMonth(int n) {
        year += n / 12;
        month += n % 12;

        if (month > 12) {
            month -= 12;
            year++;
        } else if (month < 1) {
            month += 12;
            year--;
        }
        return this;
    }

    public SolarDate clone() {
        return new SolarDate(year, month, day);
    }

    // SolarDate next = today + 1;
    // 不能太多哦， 要注意
    //会改变当前的数据
    public SolarDate addDays(int days) {
        // days 从year年1月1日 多少天
        days += CalendarDB.getSolarYearDays(year, month, day);
        if (days == 0) {
            year--;
            month = 12;
            day = 31;
            return this;
        }
        if (days > 0) {
            while (days > CalendarDB.getSolarYearDays(year)) {
                days -= CalendarDB.getSolarYearDays(year);
                year++;
            }

        } else {
            while (days < 0) {
                days += CalendarDB.getSolarYearDays(year - 1);
                year--;
            }
        }
        for (month = 1; month <= 12; month++) {
            int monthDays = CalendarDB.getSolarMonthDays(year, month);
            if (days <= monthDays) {
                break;
            } else {
                days -= monthDays;
            }
        }
        this.day = days;
        return this;
    }

    /*
     * 返回日期为星期几，0为星期天，1为星期一以此类推
     */
    public int getWeekDay() {
        return (year - 1 + getLeapYears(year) + CalendarDB.getSolarYearDays(year, month, day)) % 7;
    }

    public String getFormatWeekDay() {
        String[] arrays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        int index = (year - 1 + getLeapYears(year) + CalendarDB.getSolarYearDays(year, month, day)) % 7;
        return arrays[index];
    }

    public String getFormatMonth(){
        return CalendarDB.HanziMonth[getMonth()-1];
    }

    public String getFormatENMonth(){
        return CalendarDB.EnglishMonth[getMonth()-1];
    }

    public String getWeekDayName() {
        return CalendarDB.WeekDayName[getWeekDay()];
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getYear() {
        return this.year;
    }

    public int getMonth() {
        return this.month;
    }

    public int getDay() {
        return this.day;
    }

    public int getLastDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, this.year);
        calendar.set(Calendar.MONTH, this.month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
}
