package com.example.calbirthdaylib;


import java.io.Serializable;

public class BirthData implements Comparable<BirthData>, Serializable {
    public static final int REMIND_SINGLE_CAL = 0;
    public static final int REMIND_DOUBLE_CAL = 1;
    private static final long serialVersionUID = -4143412603702614517L;
    protected int isLunar = 0;
    protected int year;
    protected int month;
    protected int day;
    protected int remindFlag;
    protected int remindSetting;
    protected int time;// 时间，用来计算出生时间
    protected int shiftDays = 0;
    private int countDays = -1;
    private boolean isSpecial = false;

    public BirthData() {
        this.year = 0;
        this.month = 0;
        this.day = 0;
        this.time = -1;
    }

    public BirthData(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        if (month < 0) {
            this.isLunar = 1;
        }
    }

    public int getShiftDays() {
        return shiftDays;
    }

    public void setShiftDays(int shiftDays) {
        this.shiftDays = shiftDays;
    }

    public boolean notSetBirthday() {
        return !isSetBirthday();
    }

    public boolean isSetBirthday() {
        return month != 0 && day != 0;
    }

    public boolean isIgnoreAge() {
        return (year == 3333 || year == 1112 || year == 0);
    }

    public boolean getIsLunar() {
        return isLunar == 1;
    }

    public void setIsLunar(int isLunar) {
        if (isLunar == 0) {
            this.isLunar = 0;
        } else {
            this.isLunar = 1;
        }
    }

    public int getIs_lunar() {
        return isLunar;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setNoEmptyYear(int year) {
        if(year==0){
            return;
        }
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setNoEmptyMonth(int month) {
        if(month==0){
            return;
        }
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setNoEmptyDay(int day) {
        if(day==0){
            return;
        }
        this.day = day;
    }

    public int getHour() {
        return time / 60;
    }

    public int getMinute() {
        return time % 60;
    }

    public int getTime() {
        return time;
    }


    public void setTime(int time) {
        this.time = time;
    }

    public void setTime(int hour, int minute) {
        time = hour * 60 + minute;
    }

    public int getRemindflag() {
        return remindFlag;
    }

    public void setRemindflag(int flag) {
        if (isIgnoreAge()) {
            this.remindFlag = 0;
        } else {
            this.remindFlag = flag;
        }

    }

    /*
     * 比较两个生日谁的下一个生日在前
     */
    @Override
    public int compareTo(BirthData another) {
        return this.getCountDownDays() - another.getCountDownDays();
    }

    /*
     * 生日倒数天数
     */
    public int getCountDownDays() {
        if (this.countDays == -1) {
            BooleanHolder temp = new BooleanHolder();
            countDays = getCountDownDays(null, temp);
            isSpecial = temp.getValue();
        }
        return countDays;
    }

    public int getCountDownDays(SolarDate from) {
        return getCountDownDays(from, null);
    }

    public int getCountDownDays(BooleanHolder s) {
        if (this.countDays == -1) {
            BooleanHolder temp = new BooleanHolder();
            countDays = getCountDownDays(null, temp);
            isSpecial = temp.getValue();
        }
        s.setValue(isSpecial);
        return countDays;
    }

    public int getCountDownDays(SolarDate from, BooleanHolder isSpecial) {
        if (this.remindFlag == 1) {
            // 双历提醒
            BooleanHolder s1 = new BooleanHolder();
            BooleanHolder s2 = new BooleanHolder();
            int d1 = this.getLunarCountDownDays(from, s1);
            int d2 = this.getSolarCountDownDays(from, s2);
            if (d1 < d2) {
                if (isSpecial != null) {
                    isSpecial.setValue(s2.getValue());
                }
                return d1;
            } else {
                if (isSpecial != null) {
                    isSpecial.setValue(s2.getValue());
                }
                return d2;
            }
        } else {
            // 不是双历提醒
            if (this.isLunar == 1) {
                return this.getLunarCountDownDays(from, isSpecial);
            } else {
                return this.getSolarCountDownDays(from, isSpecial);
            }
        }
    }

    /*
     * 农历生日倒数天数
     */
    public int getLunarCountDownDays() {
        return getLunarCountDownDays(null, null);
    }

    public int getLunarCountDownDays(SolarDate from) {
        return getLunarCountDownDays(from, null);
    }

    public int getLunarCountDownDays(BooleanHolder isSpecial) {
        return getLunarCountDownDays(null, isSpecial);
    }

    public int getLunarCountDownDays(SolarDate from, BooleanHolder isSpecial) {
        LunarDate next = this.getNextLunarBirthday(from, isSpecial);
        if (next != null) {
            if (from == null) {
                from = SolarDate.today();
            }
            return from.getDaysBetween(next.toSolarDate());
        }
        return 9999;
    }

    /*
     * 公历生日倒数天数
     */
    public int getSolarCountDownDays() {
        return getSolarCountDownDays(null, null);
    }

    public int getSolarCountDownDays(SolarDate from) {
        return getSolarCountDownDays(from, null);
    }

    public int getSolarCountDownDays(BooleanHolder isSpecial) {
        return getSolarCountDownDays(null, isSpecial);
    }

    public int getSolarCountDownDays(SolarDate from, BooleanHolder isSpecial) {
        SolarDate next = this.getNextSolarBirthday(isSpecial);
        if (next != null) {
            if (from == null) {
                from = SolarDate.today();
            }
            return from.getDaysBetween(next);
        }
        return 9999;
    }

    public boolean nextBirthdayIsLunar() {
        // 如果是双历提醒
        if (remindFlag == 1) {
            return getLunarCountDownDays() < getSolarCountDownDays();
        }
        return (this.isLunar == 1);
    }

    /*
     * 下个生日的日期 shiftDays:偏移计算,如果遇到特殊生日则偏移 isSpecial:返回是否是特殊生日如2月29日。
     */
    public SolarDate getNextBirthday() {
        return getNextBirthday(null);
    }

    public SolarDate getNextBirthday(SolarDate from) {
        if (!this.isSetBirthday()) {
            return null;
        }

        if (remindFlag == 1) {
            SolarDate nextSolar = this.getNextSolarBirthday(from);
            SolarDate nextLunar = this.getNextLunarBirthday(from).toSolarDate();
            return nextSolar.beforeThan(nextLunar) ? nextSolar : nextLunar;
        } else {
            if (this.getIsLunar()) {
                return this.getNextLunarBirthday(from).toSolarDate();
            } else {
                return this.getNextSolarBirthday(from);
            }
        }

    }

    /*
     * 下个公历生日的日期 shiftDays:偏移计算,如果遇到特殊生日则偏移 isSpecial:返回是否是特殊生日如2月29日。
     */
    public SolarDate getNextSolarBirthday() {
        return getNextSolarBirthday(null, null);
    }

    public SolarDate getNextSolarBirthday(SolarDate from) {
        return getNextSolarBirthday(from, null);
    }

    public SolarDate getNextSolarBirthday(BooleanHolder isSpecial) {
        return getNextSolarBirthday(null, isSpecial);
    }

    public SolarDate getNextSolarBirthday(SolarDate from, BooleanHolder isSpecial) {
        if (!this.isSetBirthday()) {
            return null;
        }
        // 如果忽略年份并且是农历那么是没法算下个公历生日的。
        if (this.isIgnoreAge() && this.isLunar == 1) {
            return null;
        }
        /*
         * if (!this.isIgnoreAge()) { thisYear = Math.max(thisYear, this.year);
		 * }
		 */

        int solarMonth = this.month;
        int solarDay = this.day;

        if (this.isLunar == 1) {
            SolarDate birth = this.getSolarBirthday();
            solarMonth = birth.getMonth();
            solarDay = birth.getDay();
        }

        SolarDate today = from == null ? SolarDate.today() : from;
        // solarMonth,solarDay是出生当天的公历月和日
        SolarDate nextbirthday = SolarDate.getValidSolarDateFrom(solarMonth, solarDay, today.getYear(), shiftDays);
        if (nextbirthday.beforeThan(today)) {
            nextbirthday = SolarDate.getValidSolarDateFrom(solarMonth, solarDay, today.getYear() + 1, shiftDays);
        }

        if (isSpecial != null) {
            if (solarDay != nextbirthday.getDay()) {
                isSpecial.setValue(true);
            } else if (this.shiftDays == 0) {
                /*
                 * 如果偏移了算出来的日子和今天不相同 那就认为这个是特殊的日子
				 */
                SolarDate shiftbirthday = SolarDate.getValidSolarDateFrom(solarMonth, solarDay, today.getYear(), -1);
                if (shiftbirthday.beforeThan(today)) {
                    shiftbirthday = SolarDate.getValidSolarDateFrom(solarMonth, solarDay, today.getYear() + 1, -1);
                }
                if (!nextbirthday.equals(shiftbirthday)) {
                    isSpecial.setValue(true);
                }
            }
        }
        return nextbirthday;
    }

	/*
     * 下个农历生日的日期 shiftDays:偏移计算,如果遇到特殊生日则偏移 isSpecial:返回是否是特殊生日如2月29日。
	 */

    public LunarDate getNextLunarBirthday() {
        return getNextLunarBirthday(null, null);
    }

    public LunarDate getNextLunarBirthday(SolarDate from) {
        return getNextLunarBirthday(from, null);
    }

    public LunarDate getNextLunarBirthday(BooleanHolder isSpecial) {
        return getNextLunarBirthday(null, isSpecial);
    }

    public LunarDate getNextLunarBirthday(SolarDate from, BooleanHolder isSpecial) {
        if (!this.isSetBirthday()) {
            return null;
        }
        // 如果忽略年份并且不是农历那么是没法算下个农历生日的。
        if (this.isIgnoreAge() && this.isLunar != 1) {
            return null;
        }

		/*
         * int thisYear = Calendar.getInstance().get(Calendar.YEAR); if
		 * (!this.isIgnoreAge()) { thisYear = Math.max(thisYear, this.year); }
		 */

        int lunarMonth = this.month;
        int lunarDay = this.day;

        if (this.isLunar != 1) {
            LunarDate birth = this.getLunarBirthday();
            lunarMonth = birth.getMonth();
            lunarDay = birth.getDay();
        }
        LunarDate today = null;
        if (from == null) {
            today = LunarDate.today();
        } else {
            today = from.toLunarDate();
        }
        // lunarMonth,lunarDay是出生当天的农历月和日
        LunarDate nextbirthday = LunarDate.getValidLunarDateFrom(lunarMonth, lunarDay, today.getYear(), shiftDays);
        if (nextbirthday.beforeThan(today)) {
            nextbirthday = LunarDate.getValidLunarDateFrom(lunarMonth, lunarDay, today.getYear() + 1, shiftDays);
        }

        if (isSpecial != null) {
            if (lunarDay != nextbirthday.getDay()) {
                isSpecial.setValue(true);
            } else if (this.shiftDays == 0) {
                /*
                 * 如果偏移了算出来的日子和今天不相同 那就认为这个是特殊的日子
				 */
                LunarDate shiftbirthday = LunarDate.getValidLunarDateFrom(lunarMonth, lunarDay, today.getYear(), -1);
                if (shiftbirthday.beforeThan(today)) {
                    shiftbirthday = LunarDate.getValidLunarDateFrom(lunarMonth, lunarDay, today.getYear() + 1, -1);
                }
                if (!nextbirthday.equals(shiftbirthday)) {
                    isSpecial.setValue(true);
                }
            }
        }
        return nextbirthday;

    }

    public int getMonthBetween(BirthData other) {
        if (other == null) {
            return 0;
        }
        int monthes = Math.abs((other.getYear() - this.year) * 12 + other.month - this.month);
        if (this.getSolarBirthday().beforeThan(other.getSolarBirthday()) && other.day > this.day) {
            monthes++;
        }else if(this.getSolarBirthday().afterThan(other.getSolarBirthday()) && this.day > other.day){
            monthes++;
        }else if(this.isLastDayOfMonth() && other.isLastDayOfMonth()){
            monthes++;
        }
        return monthes;
    }

    public boolean isLastDayOfMonth(){
        int lastDay = CalendarDB.getSolarMonthDays(this.year, this.month);
        return this.day==lastDay;
    }

    /*
     * 出生当天的公历
     */
    public SolarDate getSolarBirthday() {
        if (this.isIgnoreAge()) {
            return null;
        }
        if (this.isLunar == 1) {
            return new LunarDate(this.year, this.month, this.day).toSolarDate();
        } else {
            return new SolarDate(this.year, this.month, this.day);
        }
    }

    /*
     * 出生当天的农历
     */
    public LunarDate getLunarBirthday() {
        if (this.isIgnoreAge()) {
            return null;
        }
        if (this.isLunar == 1) {
            return new LunarDate(this.year, this.month, this.day);
        } else {
            return new SolarDate(this.year, this.month, this.day).toLunarDate();
        }
    }


    public int getShengXiaoIndex() {
        int index;
        LunarDate lunar = getLunarBirthday();
        if (lunar == null) {
            return 100;// 保证排序的时候在最后
        }
        index = (lunar.getYear() - 1900) % 12;
        return index;
    }

    public String getShengXiao() {
        int index = getShengXiaoIndex();
        if (index == 100) {
            return "未知";
        }
        return CalendarDB.getShengXiaoByIndex(index);
    }

    /*
     * 此函数得到的是当前年龄，如果计算下次生日的年龄应该用下个生日来计算
     */
    public int getSolarAge() {
        SolarDate birth = this.getSolarBirthday();
        if (birth != null) {
            // return birth.getYearsBetween(SolarDate.today());
            return birth.getAge(birth);
        }
        return -1;
    }

    /*
     * 此函数得到的是当前年龄，如果计算下次生日的年龄应该用下个生日来计算
     */
    public int getLunarAge() {
        LunarDate birth = this.getLunarBirthday();
        if (birth != null) {
            // return birth.getLunarYearsBetween(LunarDate.today());
            return birth.getAge(birth);
        }
        return -1;
    }

    public int getNextSolarAge() {
        SolarDate birth = this.getSolarBirthday();
        if (birth != null) {
            int age = birth.getYearsBetween(this.getNextSolarBirthday());
            return age < 0 ? 0 : age;
        }
        return -1;
    }

    public int getNextLunarAge() {
        LunarDate birth = this.getLunarBirthday();
        if (birth != null) {
            int age = birth.getLunarYearsBetween(this.getNextLunarBirthday());
            return age < 0 ? 0 : age;
        }
        return -1;
    }

    public String formatDateWithoutYear() {
        return formatDateWithOutYear(false);
    }

    public String formatDateWithOutYear(boolean doubleDegit) {
        if (nextBirthdayIsLunar()) {
            if (isLunar == 1) {
                return CalendarDB.formatLunarDate(month, day);
            } else {
                LunarDate lunar = this.getNextLunarBirthday();
                return CalendarDB.formatLunarDate(lunar.getMonth(), lunar.getDay());
            }
        } else {
            if (isLunar == 1) {
                SolarDate solarDate = this.getNextSolarBirthday();
                if (doubleDegit) {
                    return String.format("%02d月%02d日", solarDate.getMonth(), solarDate.getDay());
                } else {
                    return String.format("%d月%d日", solarDate.getMonth(), solarDate.getDay());
                }
            } else {
                if (doubleDegit) {
                    return String.format("%02d月%02d日", month, day);
                } else {
                    return String.format("%d月%d日", month, day);
                }
            }
        }
    }

    public String formatLunarDate() {
        if (isLunar == 1) {
            return CalendarDB.formatLunarDate(year, month, day);
        } else {
            if (!isIgnoreAge()) {
                LunarDate lunar = this.getLunarBirthday();
                if (lunar == null) {
                    return "未知";
                }
                return CalendarDB.formatLunarDate(lunar.getYear(), lunar.getMonth(), lunar.getDay());
            }
        }
        return "";
    }

    public String formatSolarDate() {
        if (!this.isSetBirthday()) {
            return "";
        }
        if (isLunar == 1) {
            if (!isIgnoreAge() && getSolarBirthday() != null) {
                return this.getSolarBirthday().formatString();
            }
        } else {
            if (isIgnoreAge()) {
                return String.format("%02d月%02d日", this.month, this.day);
            } else {
                return String.format("%04d年%02d月%02d日", this.year, this.month, this.day);
            }
        }
        return "";
    }

    public String formatSolarDateWithBar() {
        if (!this.isSetBirthday()) {
            return "";
        }
        if (isLunar == 1) {
            if (!isIgnoreAge() && getSolarBirthday() != null) {
                return this.getSolarBirthday().formatString();
            }
        } else {
            if (isIgnoreAge()) {
                return String.format("%02d月%02d日", this.month, this.day);
            } else {
                return String.format("%04d-%02d-%02d", this.year, this.month, this.day);
            }
        }
        return "";
    }

    public String formatDate() {
        if (isLunar == 1) {
            return formatLunarDate();
        } else {
            return formatSolarDate();
        }
    }

    // 星座
    public int getAstro() {
        if (this.isIgnoreAge() && isLunar == 1) {
            return -1;
        }
        if (this.isLunar == 1) {
            SolarDate birth = this.getSolarBirthday();
            return CalendarDB.getAstro(birth.getMonth(), birth.getDay());
        }
        return CalendarDB.getAstro(this.getMonth(), this.getDay());
    }


    /*
     * 对调生日，就是将公历转成农历。 农历转成公历。 如果是忽略年份的话 直接返回false
     */
    public boolean reverseBirthday() {

        if (this.isIgnoreAge()) {
            return false;
        }
        if (this.getIsLunar()) {
            convertToSolar();
        } else {
            convertToLunar();
        }
        return true;
    }

    public void convertToSolar() {
        SolarDate birth = this.getSolarBirthday();
        if (birth != null) {
            this.setYear(birth.getYear());
            this.setMonth(birth.getMonth());
            this.setDay(birth.getDay());
            this.setIsLunar(0);
        }

    }

    public void convertToLunar() {
        LunarDate birth = this.getLunarBirthday();
        if (birth != null) {
            this.setYear(birth.getYear());
            this.setMonth(birth.getMonth());
            this.setDay(birth.getDay());
            this.setIsLunar(1);
        }
    }


    public String formatTime() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getHour());
        sb.append("时");
        sb.append(this.getMinute());
        sb.append("分");
        return sb.toString();
    }

    public String formatDateAndTime(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.year);
        sb.append("-");
        sb.append(this.month);
        sb.append("-");
        sb.append(this.day);
        sb.append(" ");
        sb.append(this.getHour());
        sb.append(":");
        sb.append(this.getMinute());
        sb.append(":");
        sb.append("00");
        return sb.toString();
    }
}
