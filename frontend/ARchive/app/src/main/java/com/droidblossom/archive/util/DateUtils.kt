package com.droidblossom.archive.util

import android.annotation.SuppressLint
import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale
import java.util.TimeZone


object DateUtils {

    private var timeZone: TimeZone = TimeZone.getTimeZone("GMT+09:00")

//        init {
//            try {
//                timeZone = TimeZone.getTimeZone("GMT+09:00")
//            } catch (e: Exception) {
//
//            }
//        }

    fun getSimpleDateFormat(pattern: String?): SimpleDateFormat {
        val simpleFormat = SimpleDateFormat(pattern, Locale.KOREAN)
        simpleFormat.timeZone = timeZone
        return simpleFormat
    }

    val date: Date
        get() {
            val cal = Calendar.getInstance(timeZone, Locale.KOREAN)
            return cal.time
        }

    fun getDate(offset: Long): Date {
        val cal = Calendar.getInstance(timeZone, Locale.KOREAN)
        cal.time = Date(cal.time.time + offset * 1000)
        return cal.time
    }

    fun getDate(date: Date, offset: Long): Date {
        return Date(date.time + offset * 1000)
    }

    fun getDateString(format: String?): String {
        val simpleFormat = getSimpleDateFormat(format)
        return simpleFormat.format(date)
    }

    fun getDateString(format: String?, locale: Locale?): String {
        val simpleFormat = getSimpleDateFormat(format)
        return simpleFormat.format(date)
    }

    val dateString: String
        get() = getDateString("yyyy-MM-dd HH:mm:ss")

    val dataServerString : String
        get() = getDateString("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")

        fun String.toDateLong() = getSimpleDateFormat(this).format(date).toLong()

        val dateLong: Long
            get() = "yyyyMMddHHmmss".toDateLong()
        val dateLongS: Long
            get() = "yyyyMMddHHmmssSSS".toDateLong()
    /*
        fun getDateLong(format: String?): Long {
            val simpleFormat = getSimpleDateFormat(format)
            return simpleFormat.format(date).toLong()
        }

        val dateLong: Long
            get() = getDateLong("yyyyMMddHHmmss")
        val dateLongS: Long
            get() = getDateLong("yyyyMMddHHmmssSSS")
    */
    fun getDate(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int): Date {
        val cal = GregorianCalendar(timeZone, Locale.KOREAN)
        cal[year, month - 1, day, hour, minute] = second
        return cal.time
    }

    fun dateToString(date: Date?, format: String?): String? {
        var format = format
        if (date == null) {
            return null
        }
        if (format == null || format == "") {
            format = "yyyyMMdd"
        }
        val simpleFormat = getSimpleDateFormat(format)
        val result = simpleFormat.format(date)
        return if (result.contains("요일")) {
            result.replace("요일", getDay(date) + "요일")
        } else result
    }

    fun dateToString(date: Calendar, format: String?): String {
        val simpleFormat = getSimpleDateFormat(format)
        return simpleFormat.format(date.time)
    }

    fun getDateToString(format: String?): String {
        val simpleFormat = getSimpleDateFormat(format)
        val now = Calendar.getInstance(timeZone, Locale.KOREAN)
        return simpleFormat.format(now.time)
    }

    fun stringToDate(dateString: String?, format: String?): Date {
        val simpleFormat = getSimpleDateFormat(format)
        return try {
            simpleFormat.parse(dateString)
        } catch (e: ParseException) {
            throw RuntimeException(e)
        }
    }

    fun dateToLong(date: Date?, format: String?): Long {
        val simpleFormat = getSimpleDateFormat(format)
        return simpleFormat.format(date).toLong()
    }

    @Throws(ParseException::class)
    fun longToDate(dateLong: Long, format: String?): Date {
        val simpleFormat = getSimpleDateFormat(format)
        return simpleFormat.parse(java.lang.Long.toString(dateLong))
    }

    @Throws(ParseException::class)
    fun longToString(dateLong: Long, format: String?): String? {
        return dateToString(longToDate(dateLong, "yyyyMMddHHmmss"), format)
    }

    fun getAfterDays(date1: Date, date2: Date): Int {
        return ((date1.time - date2.time) / 86400000).toInt()
    }

    fun getAfterDays(yyyyMMdd: String?, yyyyMMdd2: String?): Int {
        return if (yyyyMMdd == null) {
            0
        } else ((stringToDate(yyyyMMdd, "yyyyMMdd").time - stringToDate(
            yyyyMMdd2,
            "yyyyMMdd"
        ).time) / 86400000).toInt()
    }

    @JvmStatic
    fun main(args: Array<String>) {

    }

    @SuppressLint("SimpleDateFormat")
    fun calcLastDate(date: String): String {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val date = format.parse(date)
        val currentDate = format.parse(dataServerString)

        val difference = currentDate.time - date.time
        val daysDifference = difference / (24 * 60 * 60 * 1000)
        return if (daysDifference == 0.toLong()) "오늘" else "${daysDifference}일전"
    }

    fun getAfterSeconds(date1: Date, date2: Date): Int {
        return ((date1.time - date2.time) / 1000).toInt()
    }

    fun getAfterMilliSeconds(date1: Date, date2: Date): Int {
        return (date1.time - date2.time).toInt()
    }

    /**
     * 해당 월의 첫 번째 날짜를 구한다.
     *
     * @param year
     * @param month
     * @param format
     * @return
     */
    fun getCurMonthFirstDate(year: String, month: String, format: String?): String? {
        val cal = Calendar.getInstance(timeZone, Locale.KOREAN)
        val curYear = year.toInt()
        val curMonth = month.toInt()
        cal[curYear, curMonth - 1] = 1
        val curMinDay = cal.getActualMinimum(Calendar.DATE)
        val curDate = getDate(curYear, curMonth, curMinDay, 0, 0, 0)
        return dateToString(curDate, format)
    }

    /**
     * 해당 주의 첫 번째 날짜를 구한다.
     *
     * @param year
     * @param month
     * @param format
     * @return
     */
    fun getFirstDateOfWeek(date: Date?): Date {
        val cal = Calendar.getInstance(timeZone, Locale.KOREAN)
        cal.time = date
        while (cal[Calendar.DAY_OF_WEEK] != Calendar.SUNDAY) {
            cal.add(Calendar.DATE, -1)
        }
        return cal.time
    }

    /**
     * 이번달 마지막 날짜
     *
     * @param format
     * @return
     */
    fun getCurMonthLastDate(format: String?): String {
        val cal = Calendar.getInstance(timeZone, Locale.KOREAN)
        cal[Calendar.DATE] = cal.getActualMaximum(Calendar.DATE)
        return dateToString(cal, format)
    }

    /**
     * 지난달 첫번째 날짜
     *
     * @param format
     * @return
     */
    fun getPreMonthFirstDate(format: String?): String {
        val cal = Calendar.getInstance(timeZone, Locale.KOREAN)
        cal.add(Calendar.MONTH, -1)
        cal[Calendar.DATE] = 1
        return dateToString(cal, format)
    }

    /**
     * 지난달 마지막 날짜
     *
     * @param format
     * @return
     */
    fun getPreMonthLastDate(format: String?): String {
        val cal = Calendar.getInstance(timeZone, Locale.KOREAN)
        cal[Calendar.DATE] = 1
        cal.add(Calendar.DAY_OF_MONTH, -1)
        return dateToString(cal, format)
    }

    /**
     * 현재 요일을 구한다.
     *
     * @return
     */
    val day: String
        get() {
            val cal = Calendar.getInstance(timeZone, Locale.KOREAN)
            val curDay = cal[Calendar.DAY_OF_WEEK]
            val days = arrayOf("", "일", "월", "화", "수", "목", "금", "토")
            return days[curDay]
        }

    fun getDay(date: Date?): String {
        val cal = Calendar.getInstance(timeZone, Locale.KOREAN)
        cal.time = date
        val curDay = cal[Calendar.DAY_OF_WEEK]
        val days = arrayOf("", "일", "월", "화", "수", "목", "금", "토")
        return days[curDay]
    }

    val calendar: Calendar
        get() = Calendar.getInstance(timeZone, Locale.KOREAN)

    fun getCalendar(date: Date?): Calendar {
        val cal = Calendar.getInstance(timeZone, Locale.KOREAN)
        cal.time = date
        return cal
    }

    fun getCalendar(strDate: String?, format: String?): Calendar {
        return getCalendar(stringToDate(strDate, format))
    }

    fun getCalendar(format: String?): Calendar {
        return getCalendar(stringToDate(getDateString(format), format))
    }

    /**
     * 현재 요일을 숫자로 구한다.
     *
     * @return
     */
    val intDay: Int
        get() {
            val cal = Calendar.getInstance(timeZone, Locale.KOREAN)
            return cal[Calendar.DAY_OF_WEEK]
        }

//   Z

    fun getAfterYears(year: Int): String {
        val simpleFormat = getSimpleDateFormat("yyyyMMddHHmm")
        val now = Calendar.getInstance(timeZone, Locale.KOREAN)
        now.add(Calendar.YEAR, year)
        return simpleFormat.format(now.time)
    }

    fun getAfterYears(year: Int, format: String?): String {
        val simpleFormat = getSimpleDateFormat(format)
        val now = Calendar.getInstance(timeZone, Locale.KOREAN)
        now.add(Calendar.YEAR, year)
        return simpleFormat.format(now.time)
    }

    fun getAfterMonths(month: Int): String {
        val simpleFormat = getSimpleDateFormat("yyyyMMddHHmm")
        val now = Calendar.getInstance(timeZone, Locale.KOREAN)
        now.add(Calendar.MONTH, month)
        return simpleFormat.format(now.time)
    }

    fun getAfterMonths(month: Int, format: String?): String {
        val simpleFormat = getSimpleDateFormat(format)
        val now = Calendar.getInstance(timeZone, Locale.KOREAN)
        now.add(Calendar.MONTH, month)
        return simpleFormat.format(now.time)
    }

    fun getAfterMonths(now: Date?, month: Int, format: String?): String {
        val simpleFormat = getSimpleDateFormat(format)
        val nowCal = Calendar.getInstance(timeZone, Locale.KOREAN)
        nowCal.time = now
        nowCal.add(Calendar.MONTH, month)
        return simpleFormat.format(nowCal.time)
    }

    fun getAfterMonths(now: Date?, month: Int): Date? {
        if (now == null) {
            return null
        }
        val nowCal = Calendar.getInstance(timeZone, Locale.KOREAN)
        nowCal.time = now
        nowCal.add(Calendar.MONTH, month)
        return nowCal.time
    }

    /**
     * 오늘 날짜로 부터, day가 지난 날짜를 구함
     *
     * @param day
     * @return
     */
    fun getAfterDays(day: Int): Date {
        val now = Calendar.getInstance(timeZone, Locale.KOREAN)
        now.add(Calendar.DATE, day)
        return now.time
    }

    /**
     * 특정 날짜로부터, day가 지난 날짜를 구함
     *
     * @param date
     * @param day
     * @return
     */
    fun getAfterDays(date: Date?, day: Int): Date {
        val now = Calendar.getInstance(timeZone, Locale.KOREAN)
        now.time = date
        now.add(Calendar.DATE, day)
        return now.time
    }

    /**
     * 특정 날짜로부터, 몇 주가 지난 날짜를 구함
     *
     * @param date
     * @param week 몇 주
     * @return
     */
    fun getAfterWeeks(date: Date?, week: Int): Date {
        val now = Calendar.getInstance(timeZone, Locale.KOREAN)
        now.time = date
        now.add(Calendar.WEEK_OF_MONTH, 1)
        return now.time
    }

    fun getAfterDays(day: Int, format: String?): String {
        val simpleFormat = getSimpleDateFormat(format)
        val now = Calendar.getInstance(timeZone, Locale.KOREAN)
        now.add(Calendar.DATE, day)
        return simpleFormat.format(now.time)
    }

    fun getAfterHours(hour: Int): String {
        val simpleFormat = getSimpleDateFormat("yyyyMMddHHmm")
        val now = Calendar.getInstance(timeZone, Locale.KOREAN)
        now.add(Calendar.HOUR_OF_DAY, hour)
        return simpleFormat.format(now.time)
    }

    fun getAfterMinute(minute: Int): String {
        val simpleFormat = getSimpleDateFormat("yyyyMMddHHmmss")
        val now = Calendar.getInstance(timeZone, Locale.KOREAN)
        now.add(Calendar.MINUTE, minute)
        return simpleFormat.format(now.time)
    }

    fun getAfterMinuteForDate(minute: Int): Date {
        val now = Calendar.getInstance(timeZone, Locale.KOREAN)
        now.add(Calendar.MINUTE, minute)
        return now.time
    }

    fun getyyyyMMddHHmmssSSS(): String {
        return getDateString("yyyyMMddHHmmssSSS")
    }

    // Now write logic to check the date for potential
    // matches among a list of public holidays stored
    // in an external location
    val isHoliday: Boolean
        get() {
            var isHoliday = false
            val cal = Calendar.getInstance(timeZone, Locale.KOREAN)
            if (cal[Calendar.DAY_OF_WEEK] == Calendar.SUNDAY || cal[Calendar.DAY_OF_WEEK] == Calendar.SATURDAY) {
                isHoliday = true
            }

            // Now write logic to check the date for potential
            // matches among a list of public holidays stored
            // in an external location
            return isHoliday
        }

    /**
     * Calendar에서 YYYYMMDDH24MISSMMM 추출
     *
     * @param c             Calendar
     * @param isMillisecond Millisecond 추가 여부
     * @return YYYYMMDDH24MISSMMM
     */
    fun toDTime(c: Calendar, isMillisecond: Boolean): String {
        val sb = StringBuffer(17)
        /** 년  */
        if (c[Calendar.YEAR] < 10) sb.append('0')
        sb.append(c[Calendar.YEAR])
        /** 월  */
        if (c[Calendar.MONTH] + 1 < 10) sb.append('0')
        sb.append(c[Calendar.MONTH] + 1)
        /** 일  */
        if (c[Calendar.DAY_OF_MONTH] < 10) sb.append('0')
        sb.append(c[Calendar.DAY_OF_MONTH])
        /** 시  */
        if (c[Calendar.HOUR_OF_DAY] < 10) sb.append('0')
        sb.append(c[Calendar.HOUR_OF_DAY])
        /** 분  */
        if (c[Calendar.MINUTE] < 10) sb.append('0')
        sb.append(c[Calendar.MINUTE])
        /** 초  */
        if (c[Calendar.SECOND] < 10) sb.append('0')
        sb.append(c[Calendar.SECOND])
        /** MILLISECOND  */
        if (isMillisecond) {
            val mil = c[Calendar.MILLISECOND]
            if (mil == 0) {
                sb.append("000")
            } else if (mil < 10) {
                sb.append("00")
            } else if (mil < 100) {
                sb.append("0")
            }
            sb.append(mil)
        }
        return sb.toString()
    }

    /**
     *
     *
     * Description : 남은 시간 계산
     *
     *
     *
     * @param lTime {long} lTime 미리초단위 시간
     * @return 포매팅된 문자열
     */
    fun remainingTime(lTime: Long): String {
        var lTime = lTime
        val days = lTime / 86400000 // 60*60*24*1000
        lTime = lTime % 86400000
        val hours = lTime / 3600000 // 60*60*1000
        lTime = lTime % 3600000
        val minutes = lTime / 60000 // 60*1000
        lTime = lTime % 60000
        val seconds = lTime / 1000 // 1000
        val sb = StringBuffer()
        if (days > 0) {
            sb.append(days).append("일 ")
            sb.append(hours).append("시간 ")
            sb.append(minutes).append("분 ")
        } else if (hours > 0) {
            sb.append(hours).append("시간 ")
            sb.append(minutes).append("분 ")
        } else if (minutes > 0) {
            sb.append(minutes).append("분 ")
        }
        sb.append(seconds).append("초")
        return sb.toString()
    }

    fun convertDateEngFormat(
        date: String?,
        beforFormat: String?,
        afterFormat: String?
    ): String {
        var convertDate = ""
        try {
            val time = SimpleDateFormat(beforFormat, Locale.ENGLISH).parse(date) as Date
            convertDate = SimpleDateFormat(afterFormat).format(time)
        } catch (e: ParseException) {
        }
        return convertDate
    }

    /**
     * 해당 달의 몇 주차인지 조회
     *
     * @param date
     * @return
     */
    fun getWeek(date: Date?): Int {
        val cal = Calendar.getInstance(timeZone, Locale.KOREAN)
        cal.time = date
        return cal[Calendar.DAY_OF_WEEK_IN_MONTH]
    }


    fun isHoliday(date: Date?): Boolean {
        var isHoliday = false
        val cal = Calendar.getInstance(timeZone, Locale.KOREAN)
        cal.time = date
        if (cal[Calendar.DAY_OF_WEEK] == Calendar.SUNDAY || cal[Calendar.DAY_OF_WEEK] == Calendar.SATURDAY) {
            isHoliday = true
        }

        // Now write logic to check the date for potential
        // matches among a list of public holidays stored
        // in an external location
        return isHoliday
    }

    fun getCurrentYear(): Int {
        return getDateString("yyyy").toInt()
    }
    fun getCurrentMonth(): Int {
        return getDateString("MM").toInt()
    }
    fun getCurrentDay(): Int {
        return getDateString("dd").toInt()
    }

    fun getCurrentHour(): Int {
        return getDateString("HH").toInt()
    }

    fun getCurrentMin(): Int {
        return getDateString("mm").toInt()
    }
    fun getStartOfTodayString(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
        val startOfDayCalendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return dateFormat.format(startOfDayCalendar.time)
    }

}