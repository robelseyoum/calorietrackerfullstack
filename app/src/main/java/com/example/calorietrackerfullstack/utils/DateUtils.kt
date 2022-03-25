package com.example.calorietrackerfullstack.utils
import java.text.SimpleDateFormat
import java.util.*

class DateUtils {

    companion object {

        // dates from server look like this: "2019-07-23T03:28:01.406944Z"
        fun convertServerStringDateToLong(sd: String): Long {
            var stringDate = sd.removeRange(sd.indexOf("T") until sd.length)
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            try {
                val time = sdf.parse(stringDate).time
                return time
            } catch (e: Exception) {
                throw Exception(e)
            }
        }

        fun convertLongToStringDate(longDate: Long): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            try {
                val date = sdf.format(Date(longDate))
                return date
            } catch (e: Exception) {
                throw Exception(e)
            }
        }

        fun currentDate(): String {
            val c = Calendar.getInstance()
            val day = c[Calendar.DAY_OF_MONTH]
            val month = c[Calendar.MONTH]
            val year = c[Calendar.YEAR]
            val date = day.toString() + "/" + (month + 1) + "/" + year

            return date.toString()
        }


        fun getCalculatedDate(days: Int): String {
            val c = Calendar.getInstance()

            c.add(Calendar.DAY_OF_YEAR, days)
            val day = c[Calendar.DAY_OF_MONTH]
            val month = c[Calendar.MONTH]
            val year = c[Calendar.YEAR]
            val date = day.toString() + "/" + (month + 1) + "/" + year

            return date.toString()
        }
    }

}