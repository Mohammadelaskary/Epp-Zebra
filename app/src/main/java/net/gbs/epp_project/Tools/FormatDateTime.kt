package net.gbs.epp_project.Tools

import android.content.ContentValues.TAG
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class FormatDateTime(private val dateTimeString:String) {
    fun calendar(): Calendar {
        val formatDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        val date = formatDateTime.parse(dateTimeString)
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar
    }
    fun year():String = calendar().get(Calendar.YEAR).toString()
    fun month():String = if (calendar().get(Calendar.MONTH)+1<10)
        "0"+(calendar().get(Calendar.MONTH)+1).toString()
    else
        (calendar().get(Calendar.MONTH)+1).toString()
    fun day():String = if (calendar().get(Calendar.DAY_OF_MONTH)<10)
        "0"+calendar().get(Calendar.DAY_OF_MONTH).toString()
    else
        calendar().get(Calendar.DAY_OF_MONTH).toString()
    fun hours():String = if (calendar().get(Calendar.HOUR_OF_DAY)<10)
        "0"+calendar().get(Calendar.HOUR_OF_DAY).toString()
    else
        calendar().get(Calendar.HOUR_OF_DAY).toString()
    fun minutes():String = if (calendar().get(Calendar.MINUTE)<10)
        "0"+calendar().get(Calendar.MINUTE).toString()
    else
        calendar().get(Calendar.MINUTE).toString()

    fun seconds():String = if (calendar().get(Calendar.SECOND)<10)
        "0"+calendar().get(Calendar.SECOND).toString()
    else
        calendar().get(Calendar.SECOND).toString()
    fun dateBefore(minutes:Int): String {
        val formatDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val dateTime = formatDateTime.parse("${year()}-${month()}-${day()} ${hours()}:${minutes()}:${seconds()}")
        return formatDateTime.format(Date(dateTime.time - minutes * 60 * 1000))
    }
    fun dateAfter(minutes:Int): String {
        val formatDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val dateTime = formatDateTime.parse("${year()}-${month()}-${day()} ${hours()}:${minutes()}:${seconds()}")
        return formatDateTime.format(Date(dateTime.time + minutes * 60 * 1000))
    }

    companion object {


        fun compareTwoTimes(serverTime:String, deviceTime:String):Boolean {
            Log.d(TAG, "compareTwoTimes: deviceDateTime = $deviceTime")
            val t1Format = FormatDateTime(serverTime)
            val t2Format = FormatDateTime(deviceTime)
            val dateTime1 = "${t1Format.year()}-${t1Format.month()}-${t1Format.day()} ${t1Format.hours()}:${t1Format.minutes()}:00"
            val dateTime2 = "${t2Format.year()}-${t2Format.month()}-${t2Format.day()} ${t2Format.hours()}:${t2Format.minutes()}:00"
            val t2WithoutSeconds = FormatDateTime(dateTime2)
            var isCorrect = false
//            Log.d(TAG, "compareTwoTimes: dateTime1 = $dateTime1")
//            Log.d(TAG, "compareTwoTimes: dateTime2 = $dateTime2")
//            Log.d(TAG, "compareTwoTimes: dateTime2Before1 = ${t2WithoutSeconds.dateBefore(1)}")
//            Log.d(TAG, "compareTwoTimes: dateTime2Before2 = ${t2WithoutSeconds.dateBefore(2)}")
//            Log.d(TAG, "compareTwoTimes: dateTime2Before3 = ${t2WithoutSeconds.dateBefore(3)}")
//            Log.d(TAG, "compareTwoTimes: dateTime2Before4 = ${t2WithoutSeconds.dateBefore(4)}")
//            Log.d(TAG, "compareTwoTimes: dateTime2Before5 = ${t2WithoutSeconds.dateBefore(5)}")
//            Log.d(TAG, "compareTwoTimes: dateTime2After1 = ${t2WithoutSeconds.dateAfter(1)}")
//            Log.d(TAG, "compareTwoTimes: dateTime2After2 = ${t2WithoutSeconds.dateAfter(2)}")
//            Log.d(TAG, "compareTwoTimes: dateTime2After3 = ${t2WithoutSeconds.dateAfter(3)}")
//            Log.d(TAG, "compareTwoTimes: dateTime2After4 = ${t2WithoutSeconds.dateAfter(4)}")
//            Log.d(TAG, "compareTwoTimes: dateTime2After5 = ${t2WithoutSeconds.dateAfter(5)}")

            if (dateTime1 == dateTime2
                || dateTime1 == t2WithoutSeconds.dateBefore(1)
                || dateTime1 == t2WithoutSeconds.dateBefore(2)
                || dateTime1 == t2WithoutSeconds.dateBefore(3)
                || dateTime1 == t2WithoutSeconds.dateBefore(4)
                || dateTime1 == t2WithoutSeconds.dateBefore(5)
                || dateTime1 == t2WithoutSeconds.dateAfter(1)
                || dateTime1 == t2WithoutSeconds.dateAfter(2)
                || dateTime1 == t2WithoutSeconds.dateAfter(3)
                || dateTime1 == t2WithoutSeconds.dateAfter(4)
                || dateTime1 == t2WithoutSeconds.dateAfter(5)
            )
                isCorrect = true
            return isCorrect
        }
    }

}