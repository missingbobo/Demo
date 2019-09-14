package com.example.calbirthdaylib;

import java.util.Calendar;

public class LunarDate {

    private int year;
    /*
     * 用负数代表闰年
     */
    private int month;
    private int day;

    public LunarDate(int year, int month, int day) {
        this.setYear(year);
        this.setMonth(month);
        this.setDay(day);
    }

    public static LunarDate today() {
        return new SolarDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(
                Calendar.DAY_OF_MONTH)).toLunarDate();
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getFakeMonth() {
        return getFakeMonth(year, month);
    }

    /*
     * 修正月的下标，兼容calendardb
     */
    private static int getFakeMonth(int year, int month) {
        int lunarLeapMonth = CalendarDB.getLunarLeapMonth(year);
        int fakeMonth = month;
        if (lunarLeapMonth != 0) {
            if (month > lunarLeapMonth) {
                fakeMonth++;
            } else if (lunarLeapMonth == -1 * month) {
                fakeMonth = lunarLeapMonth + 1;
            }
        }
        return fakeMonth;
    }

    public static boolean checkValidLunarDate(int year, int month, int day) {
        if (year < CalendarDB.YEAR_BASE || year > CalendarDB.YEAR_BASE + CalendarDB.YEAR_NUM) {
            return false;
        }
        int lunarLeapMonth = CalendarDB.getLunarLeapMonth(year);
        if (month < 0) {
            if (lunarLeapMonth != month * -1) {
                return false;
            }
        } else if (month < 1 || month > 12) {
            return false;
        }

        return !(day < 1 || day > CalendarDB.getFakeLunarMonthDays(year, getFakeMonth(year, month)));
    }

    public boolean isValidDate() {
        return checkValidLunarDate(this.year, this.month, this.day);
    }

    /*
     * 计算农历年。
     */
    public int getLunarYearsBetween(SolarDate other) {
        if (other == null) {
            return 0;
        }
        return this.getLunarYearsBetween(other.toLunarDate());
    }

    /*
     * 实际上这里只是将年相减了。
     */
    public int getLunarYearsBetween(LunarDate other) {
        /*
         * if(new LunarDate(other.year, this.month, this.day).afterThan(other)){
		 * cha = 1; }
		 */
        if (other == null) {
            return 0;
        }
        return other.getYear() - this.year;
    }

    // public int getLunarYearsBetween(LunarDate other) {
    // /*
    // * if(new LunarDate(other.year, this.month, this.day).afterThan(other)){
    // * cha = 1; }
    // */
    // return this.getMonthBetween(other) / 12;
    // }

    /*
     * 只是为了方便，并不真正计算农历月。计算两个日期之间相隔多少个月,这里指的是公历月
     */
    public int getMonthBetween(SolarDate other) {
        if (other == null) {
            return 0;
        }
        return this.toSolarDate().getMonthBetween(other);
    }

    public int getMonthBetween(LunarDate other) {
        if (other == null) {
            return 0;
        }
        return this.toSolarDate().getMonthBetween(other);
    }

    /*
     * 与指定日期的相隔天数，返回小于0则代表当前日期在指定日期的未来。 大于0则表示指定日期是未来的日期。
     */
    public int getDaysBetween(LunarDate other) {
        if (other == null || toSolarDate()==null) {
            return 0;
        }
        return this.toSolarDate().getDaysBetween(other);
    }

    public int getDaysBetween(SolarDate other) {
        if (other == null) {
            return 0;
        }
        return this.toSolarDate().getDaysBetween(other);
    }

    /*
     * 返回XX年XX月XX日
     */
    public String formatString() {
        return CalendarDB.formatLunarDate(year, month, day);
    }

    public String formatStringWithOutYear() {
        return CalendarDB.formatLunarDate(month, day);
    }

    public SolarDate toSolarDate() {
        if (!this.isValidDate()) {// || this.beforeThan(new SolarDateEx(1901,
            // 12, 19))
            // || this.afterThan(new SolarDateEx(2051, 2, 10))){
            return null;
        }
        int fakemonth = getFakeMonth(this.year, this.month);
        // 1月1日 到农历一月一日有多少天
        int accDays = CalendarDB.getAccDays(this.year);

        // accDays 再加上农历一月一日 到农历当天的天数
        for (int i = 1; i < fakemonth; i++) {
            accDays += CalendarDB.getFakeLunarMonthDays(this.year, i);
        }
        accDays += this.day;
        // accDays此时是1月1日到当天的天数。

        int solarYear = this.year;
        int solarMonth = 1;
        int monthDays = CalendarDB.getSolarMonthDays(solarYear, solarMonth);
        while (accDays > monthDays) {
            accDays -= monthDays;
            solarMonth++;
            if (solarMonth > 12) {
                solarMonth -= 12;
                solarYear += 1;
            }
            monthDays = CalendarDB.getSolarMonthDays(solarYear, solarMonth);
        }
        int solarDay = accDays;
        return new SolarDate(solarYear, solarMonth, solarDay);
    }

    public boolean beforeThan(LunarDate other) {
        if (this.toSolarDate() == null || other == null || other.toSolarDate() == null) {
            return false;
        }
        return this.toSolarDate().beforeThan(other.toSolarDate());
    }

    public boolean equals(LunarDate other) {
        if (other == null) {
            return false;
        }
        return this.year == other.getYear() && this.month == other.getMonth() && this.day == other.getDay();
    }

    public boolean afterThan(LunarDate other) {
        if (other == null) {
            return false;
        }
        return (!this.equals(other)) && (!this.beforeThan(other));
    }

    public boolean afterThanIgnorYear(LunarDate other) {
        if (other == null) {
            return false;
        }
        if (Math.abs(other.getMonth()) == Math.abs(this.month)) {
            return this.day > other.getDay();
        } else {
            return this.getMonth() > other.getMonth();
        }
    }

    public boolean beforeThanIgnorYear(LunarDate other) {
        if (other == null) {
            return false;
        }
        return !afterThanIgnorYear(other);
    }

    public int getAge(LunarDate other) {
        if(other == null){
            return 0;
        }
        int age = today().getYear() - other.getYear();
        if (other.afterThanIgnorYear(today())) {
            age--;
        }
        return age;
    }

    /*
     * 这个函数的意思是指，从startYear这一年开始（包括startYear),找到一个合法的month月，day日 比如说startyear
     * 2012, month 6, day 30找到下一个有六月三十的农历日期。因为农历有大小月 shiftDays 如果不存在的日期是否提前或者拖后
     * 负数代表提醒，正数代表拖后。 此处有while循环要 小心，
     */
    // 处理月日的异常
    public static LunarDate getValidLunarDateFrom(int month, int day, int startYear, int shiftDays) {
        int year = startYear;
        int absMonth = Math.abs(month);
        int shifted = 0;
        while (!LunarDate.checkValidLunarDate(year, absMonth, day) && year <= 2050) {
            if (shiftDays != 0 && LunarDate.checkValidLunarDate(year, absMonth, day + shiftDays)) {
                shifted = 1;
                break;
            }
            year++;
        }
        return new LunarDate(year, absMonth, day - shifted);
    }

    public String getFormatMonth(){
        return CalendarDB.HanziMonth[Math.abs(getMonth())-1];
    }

    public String getFormatDay(){
        return CalendarDB.LunarDayDict[getDay()-1];
    }

    public LunarDate addDays(int days) {
        if(toSolarDate() == null){
            return null;
        }
        return this.toSolarDate().addDays(days).toLunarDate();
    }

    public long getTimestamp(int hour, int min) {
        return this.toSolarDate().getTimestamp(hour, min);
    }

    public int getWeekDay() {
        return this.toSolarDate().getWeekDay();
    }

    public String getWeekDayName() {
        return this.toSolarDate().getWeekDayName();
    }

}
