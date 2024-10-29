package net.gbs.epp_project.Tools

import android.content.ContentValues.TAG
import android.util.Log

class FormatDateTime(private val dateTime:String) {
    fun year():String = dateTime.substring(0,4)
    fun month():String = dateTime.substring(5,7)
    fun day():String = dateTime.substring(8,10)
    fun hours():String {
        var hoursInt = 0
        hoursInt = if (dateTime.substring(11,13).toInt()>12){
            dateTime.substring(11,13).toInt()-12
        } else {
            dateTime.substring(11,13).toInt()
        }
        return if (hoursInt<10)
            "0${hoursInt}"
        else
            hoursInt.toString()
    }
    fun minutes():String = dateTime.substring(14,16)
    companion object {
        fun compareTwoTimes(serverTime:String, deviceTime:String):Boolean {
            val t1Format = FormatDateTime(serverTime)
            val t2Format = FormatDateTime(deviceTime)
            Log.d(TAG, "compareTwoTimes: year= ${t1Format.year()} month= ${t1Format.month()} day= ${t1Format.day()} hour= ${t1Format.hours()} minutes= ${t1Format.minutes()}")
            Log.d(TAG, "compareTwoTimes: year= ${t2Format.year()} month= ${t2Format.month()} day= ${t2Format.day()} hour= ${t2Format.hours()} minutes= ${t2Format.minutes()}")
            var isCorrect = false
            if (t1Format.year()==t2Format.year()
                && t1Format.month()==t2Format.month()
                && t1Format.day()==t2Format.day()
                && t1Format.hours()==t2Format.hours()
                && (t1Format.minutes()==t2Format.minutes()
                        ||t1Format.minutes().toInt()+1==t2Format.minutes().toInt()
                        ||t1Format.minutes().toInt()-1==t2Format.minutes().toInt()
                        ||t1Format.minutes().toInt()+2==t2Format.minutes().toInt()
                        ||t1Format.minutes().toInt()-2==t2Format.minutes().toInt()
                        ||t1Format.minutes().toInt()+3==t2Format.minutes().toInt()
                        ||t1Format.minutes().toInt()-3==t2Format.minutes().toInt()
                        ||t1Format.minutes().toInt()+4==t2Format.minutes().toInt()
                        ||t1Format.minutes().toInt()-4==t2Format.minutes().toInt()
                        ||t1Format.minutes().toInt()+5==t2Format.minutes().toInt()
                        ||t1Format.minutes().toInt()-5==t2Format.minutes().toInt())
            )
                isCorrect = true
            return isCorrect
        }
    }

}