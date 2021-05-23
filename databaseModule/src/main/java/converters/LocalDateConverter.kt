package converters

import androidx.room.TypeConverter
import java.time.LocalDate

class LocalDateConverter {
    @TypeConverter
    fun localDateFromEpoch(value: Long): LocalDate {
        return LocalDate.ofEpochDay(value)
    }

    @TypeConverter
    fun localDateToLong(value: LocalDate): Long {
        return value.toEpochDay()
    }
}