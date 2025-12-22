package com.example.waynote

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import org.json.JSONArray
import org.json.JSONObject
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

const val DEFAULT_USERNAME = ""
const val DEFAULT_PASSWORD = ""
const val MAX_LOGIN_ATTEMPTS = 3
const val LOCKOUT_DURATION_SECONDS = 5
const val DEFAULT_TRIP_LENGTH_DAYS = 3
const val MILLIS_IN_DAY = 86_400_000L

val Context.authDataStore by preferencesDataStore(name = "auth_prefs")
val Context.tripPlannerDataStore by preferencesDataStore(name = "trip_planner_prefs")
val Context.chatDataStore by preferencesDataStore(name = "chat_prefs")
val Context.userContentDataStore by preferencesDataStore(name = "user_content_prefs")

object AuthKeys {
    val RegisteredUsername = stringPreferencesKey("registered_username")
    val RegisteredPassword = stringPreferencesKey("registered_password")
    val IsLoggedIn = booleanPreferencesKey("is_logged_in")
    val AvatarUri = stringPreferencesKey("avatar_uri")
}

object ChatKeys {
    val History = stringPreferencesKey("chat_history")
}

object TripPlannerKeys {
    val Title = stringPreferencesKey("trip_title")
    val Destination = stringPreferencesKey("trip_destination")
    val StartDate = stringPreferencesKey("trip_start_date")
    val EndDate = stringPreferencesKey("trip_end_date")
    val Notes = stringPreferencesKey("trip_notes")
    val Activities = stringPreferencesKey("trip_activities")
    val TripLengthDays = intPreferencesKey("trip_length_days")
}

object UserContentKeys {
    val FavoriteDestinations = stringPreferencesKey("favorite_destinations")
    val FavoritePosts = stringPreferencesKey("favorite_posts")
    val FlightOrders = stringPreferencesKey("flight_orders")
}

suspend fun persistAuth(
    context: Context,
    username: String,
    password: String,
    isLoggedIn: Boolean
) {
    context.authDataStore.edit { prefs ->
        prefs[AuthKeys.RegisteredUsername] = username
        prefs[AuthKeys.RegisteredPassword] = password
        prefs[AuthKeys.IsLoggedIn] = isLoggedIn
    }
}

suspend fun markLoggedOut(context: Context) {
    context.authDataStore.edit { prefs ->
        prefs[AuthKeys.IsLoggedIn] = false
    }
}

suspend fun persistAvatarUri(context: Context, uri: String) {
    context.authDataStore.edit { prefs ->
        prefs[AuthKeys.AvatarUri] = uri
    }
}

suspend fun persistChatHistory(
    context: Context,
    history: Map<String, List<ChatMessage>>
) {
    context.chatDataStore.edit { prefs ->
        prefs[ChatKeys.History] = encodeChatHistory(history)
    }
}

suspend fun persistFavorites(
    context: Context,
    destinationIds: List<Int>,
    postIds: List<Int>
) {
    context.userContentDataStore.edit { prefs ->
        prefs[UserContentKeys.FavoriteDestinations] = encodeIdList(destinationIds)
        prefs[UserContentKeys.FavoritePosts] = encodeIdList(postIds)
    }
}

suspend fun persistOrders(
    context: Context,
    orders: List<FlightOrder>
) {
    context.userContentDataStore.edit { prefs ->
        prefs[UserContentKeys.FlightOrders] = encodeOrders(orders)
    }
}

suspend fun persistTripPlan(context: Context, tripPlan: TripPlan) {
    context.tripPlannerDataStore.edit { prefs ->
        prefs[TripPlannerKeys.Title] = tripPlan.title
        prefs[TripPlannerKeys.Destination] = tripPlan.destination
        prefs[TripPlannerKeys.StartDate] = tripPlan.startDate
        prefs[TripPlannerKeys.EndDate] = tripPlan.endDate
        prefs[TripPlannerKeys.Notes] = tripPlan.notes
        prefs[TripPlannerKeys.Activities] = tripPlan.activities.joinToString(separator = "||") { it.trim() }
        prefs[TripPlannerKeys.TripLengthDays] = tripPlan.tripLengthDays
    }
}

suspend fun clearTripPlan(context: Context) {
    context.tripPlannerDataStore.edit { prefs ->
        prefs.remove(TripPlannerKeys.Title)
        prefs.remove(TripPlannerKeys.Destination)
        prefs.remove(TripPlannerKeys.StartDate)
        prefs.remove(TripPlannerKeys.EndDate)
        prefs.remove(TripPlannerKeys.Notes)
        prefs.remove(TripPlannerKeys.Activities)
        prefs.remove(TripPlannerKeys.TripLengthDays)
    }
}

fun decodeActivities(serialized: String): List<String> {
    if (serialized.isBlank()) return emptyList()
    return serialized.split("||").mapNotNull { entry ->
        val trimmed = entry.trim()
        trimmed.takeIf { it.isNotEmpty() }
    }
}

fun encodeIdList(ids: List<Int>): String = ids.joinToString(separator = ",")

fun decodeIdList(serialized: String): List<Int> {
    if (serialized.isBlank()) return emptyList()
    return serialized.split(",").mapNotNull { it.toIntOrNull() }
}

fun encodeOrders(orders: List<FlightOrder>): String {
    val jsonArray = JSONArray()
    orders.forEach { order ->
        val obj = JSONObject()
        obj.put("id", order.id)
        obj.put("from", order.from)
        obj.put("to", order.to)
        obj.put("date", order.date)
        obj.put("airline", order.airline)
        obj.put("flightNumber", order.flightNumber)
        obj.put("departureTime", order.departureTime)
        obj.put("arrivalTime", order.arrivalTime)
        obj.put("totalPrice", order.totalPrice)
        obj.put("seatPreference", order.seatPreference)
        obj.put("contactPhone", order.contactPhone)
        obj.put("contactEmail", order.contactEmail)
        obj.put("cabinSummary", order.cabinSummary)
        obj.put("status", order.status)
        val passengers = JSONObject()
        passengers.put("economy", order.passengers.economy)
        passengers.put("business", order.passengers.business)
        obj.put("passengers", passengers)
        jsonArray.put(obj)
    }
    return jsonArray.toString()
}

fun decodeOrders(serialized: String): List<FlightOrder> {
    if (serialized.isBlank()) return emptyList()
    return runCatching {
        val array = JSONArray(serialized)
        buildList {
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val passengersObj = obj.optJSONObject("passengers")
                val passengers = FlightPassengerSelection(
                    economy = passengersObj?.optInt("economy", 0) ?: 0,
                    business = passengersObj?.optInt("business", 0) ?: 0
                )
                add(
                    FlightOrder(
                        id = obj.optString("id"),
                        from = obj.optString("from"),
                        to = obj.optString("to"),
                        date = obj.optString("date"),
                        airline = obj.optString("airline"),
                        flightNumber = obj.optString("flightNumber"),
                        departureTime = obj.optString("departureTime"),
                        arrivalTime = obj.optString("arrivalTime"),
                        passengers = passengers,
                        totalPrice = obj.optInt("totalPrice"),
                        seatPreference = obj.optString("seatPreference"),
                        contactPhone = obj.optString("contactPhone"),
                        contactEmail = obj.optString("contactEmail"),
                        cabinSummary = obj.optString("cabinSummary"),
                        status = obj.optString("status", "Ticketed")
                    )
                )
            }
        }
    }.getOrDefault(emptyList())
}

@RequiresApi(Build.VERSION_CODES.O)
val dateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE

@RequiresApi(Build.VERSION_CODES.O)
val systemZone: ZoneId = ZoneId.systemDefault()

@RequiresApi(Build.VERSION_CODES.O)
fun currentIsoDate(): String = LocalDate.now(systemZone).format(dateFormatter)

@RequiresApi(Build.VERSION_CODES.O)
fun parseDateToMillis(dateString: String): Long? {
    if (dateString.isBlank()) return null
    return runCatching {
        LocalDate.parse(dateString, dateFormatter)
            .atStartOfDay(systemZone)
            .toInstant()
            .toEpochMilli()
    }.getOrNull()
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(millis: Long): String {
    return Instant.ofEpochMilli(millis)
        .atZone(systemZone)
        .toLocalDate()
        .format(dateFormatter)
}

fun encodeChatHistory(history: Map<String, List<ChatMessage>>): String {
    val root = JSONObject()
    history.forEach { (sender, messages) ->
        val array = JSONArray()
        messages.forEach { message ->
            val obj = JSONObject()
            obj.put("text", message.text)
            obj.put("isMe", message.isMe)
            obj.put("avatarUrl", message.avatarUrl)
            array.put(obj)
        }
        root.put(sender, array)
    }
    return root.toString()
}

fun decodeChatHistory(serialized: String): Map<String, List<ChatMessage>> {
    if (serialized.isBlank()) return emptyMap()
    return runCatching {
        val root = JSONObject(serialized)
        buildMap {
            root.keys().forEach { sender ->
                val array = root.optJSONArray(sender) ?: return@forEach
                val messages = mutableListOf<ChatMessage>()
                for (i in 0 until array.length()) {
                    val obj = array.optJSONObject(i) ?: continue
                    val text = obj.optString("text", "")
                    val isMe = obj.optBoolean("isMe", false)
                    val avatar = obj.optString("avatarUrl", "")
                    if (text.isNotBlank()) {
                        messages.add(ChatMessage(text = text, isMe = isMe, avatarUrl = avatar))
                    }
                }
                put(sender, messages)
            }
        }
    }.getOrElse { emptyMap() }
}
