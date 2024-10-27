package br.univesp.tcc.database

import android.util.Log
import androidx.room.TypeConverter
import br.univesp.tcc.database.model.User
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.time.LocalDate
import java.time.LocalDateTime
import com.squareup.moshi.Types
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private const val TAG = "Converters"

//class LocalDateTimeAdapter {
//    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
//
//    @FromJson
//    fun fromJson(json: String): LocalDateTime {
//        return LocalDateTime.parse(json, formatter)
//    }
//
//    @ToJson
//    fun toJson(value: LocalDateTime): String {
//        return value.format(formatter)
//    }
//}
//class MoshiConverterDateTimeAdapter {
//
//    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
//
//    @ToJson
//    fun toJson(value: LocalDateTime): String {
//        return value.atOffset(ZoneOffset.UTC).format(formatter)  // Format with UTC offset
//    }
//
//    @FromJson
//    fun fromJson(value: String): LocalDateTime {
//        return OffsetDateTime.parse(value, formatter).toLocalDateTime()  // Parse with UTC offset
//    }
//}


class RoomConverterLocalDateTime {

    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    private val formatter2: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    // Convert LocalDateTime to JSON string
    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String? {
        return value?.format(formatter2)
    }

    // Convert JSON string to LocalDateTime
    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? {
        try{
        if(value.isNullOrEmpty()) return null

        var valueReplaced = value.replace("\"", "")
        valueReplaced = valueReplaced.replace("\'", "")

        return LocalDateTime.parse(valueReplaced, formatter)
        }
        catch (e: Exception) {
            Log.i(TAG, "MoshiConverterDateTime - value: $value")
            Log.e(TAG, "RoomConverterLocalDateTime - Error: ", e)
        }
        return null
    }
}

class MoshiConverterDateTime {

    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    private val formatter2: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    // Convert LocalDateTime to JSON string
    @ToJson
    fun fromLocalDateTime(value: LocalDateTime?): String? {
        return value?.format(formatter2)
    }

    // Convert JSON string to LocalDateTime
    @FromJson
    fun toLocalDateTime(json: String?): LocalDateTime? {
        try{
        if(json.isNullOrEmpty()) return null

        var jsonReplaced = json.replace("\"", "")
        jsonReplaced = jsonReplaced.replace("\'", "")

        return LocalDateTime.parse(jsonReplaced, formatter)
        }
        catch (e: Exception) {
            Log.i(TAG, "MoshiConverterDateTime - json: $json")
            Log.e(TAG, "MoshiConverterDateTime - Error: ", e)
        }
        return null
    }
}

//class LocalDateTimeConverter {
//
//    private val moshi: Moshi = Moshi.Builder()
//        .add(KotlinJsonAdapterFactory())
//        .add(LocalDateTimeAdapter())  // Custom adapter for LocalDateTime
//        .build()
//
//    private val localDateTimeAdapter = moshi.adapter(LocalDateTime::class.java)
//
//
//    // Convert LocalDateTime to JSON string
//    @TypeConverter
//    fun fromLocalDateTime(value: LocalDateTime?): String? {
//        return value?.let { localDateTimeAdapter.toJson(it) }
//    }
//
//    // Convert JSON string to LocalDateTime
//    @TypeConverter
//    fun toLocalDateTime(json: String?): LocalDateTime? {
//        return json?.let { localDateTimeAdapter.fromJson(it) }
//    }
//}

//class Converters {
//
//
//    // Cria um adaptador para listas de User
//    private val userListAdapter = moshi.adapter<List<User>>(
//        Types.newParameterizedType(List::class.java, User::class.java)
//    )
//
//    // Converter a lista de User para JSON (String)
//    @TypeConverter
//    fun fromUserList(userList: List<User>?): String? {
//        return userList?.let { userListAdapter.toJson(it) }
//    }
//
//    // Converter JSON (String) para a lista de User
//    @TypeConverter
//    fun toUserList(json: String?): List<User>? {
//        return json?.let { userListAdapter.fromJson(it) }
//    }
//
//
//}