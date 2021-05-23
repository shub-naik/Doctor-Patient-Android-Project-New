package converters

import androidx.room.TypeConverter
import java.time.LocalTime

class LocalTimeConverter {
    @TypeConverter
    fun localTimeFromStr(value: String): LocalTime = LocalTime.parse(value)

    @TypeConverter
    fun localTimeToStr(value: LocalTime): String = value.toString()
}