package com.example.waynote

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable

data class AuthPreferences(
    val registeredUsername: String = DEFAULT_USERNAME,
    val registeredPassword: String = DEFAULT_PASSWORD,
    val isLoggedIn: Boolean = false
)

data class UserContentState(
    val favoriteDestinations: List<Int> = emptyList(),
    val favoritePosts: List<Int> = emptyList(),
    val orders: List<FlightOrder> = emptyList()
)

data class TripPlan(
    val title: String = "",
    val destination: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val notes: String = "",
    val activities: List<String> = emptyList(),
    val tripLengthDays: Int = DEFAULT_TRIP_LENGTH_DAYS
)

data class IndexedActivity(
    val index: Int,
    val label: String
)

data class DayPlanGroup(
    val key: String,
    val dayLabel: String,
    val summary: String?,
    val items: List<IndexedActivity>,
    val headerIndex: Int?
)

data class SampleDayPlan(
    val day: Int,
    val summary: String,
    val items: List<String>
)

data class SampleTripTemplate(
    val destination: String,
    val title: String,
    val suggestedStart: String,
    val notes: String,
    val dayPlans: List<SampleDayPlan>
)

data class Destination(
    val id: Int,
    val title: String,
    val description: String,
    @DrawableRes val imageRes: Int,
    val details: String,
    val developmentTime: String,
    val ticketInfo: String,
    val keywords: List<String>
)

data class DestinationTranslation(
    val titleZh: String,
    val descriptionZh: String,
    val detailsZh: String,
    val developmentTimeZh: String,
    val ticketInfoZh: String,
    val keywordsZh: List<String>
)

fun Destination.localizedTitle(language: AppLanguage): String {
    val translation = destinationTranslations[id]
    return localizedText(title, translation?.titleZh.orEmpty(), language)
}

fun Destination.localizedDescription(language: AppLanguage): String {
    val translation = destinationTranslations[id]
    return localizedText(description, translation?.descriptionZh.orEmpty(), language)
}

fun Destination.localizedDetails(language: AppLanguage): String {
    val translation = destinationTranslations[id]
    return localizedText(details, translation?.detailsZh.orEmpty(), language)
}

fun Destination.localizedDevelopmentTime(language: AppLanguage): String {
    val translation = destinationTranslations[id]
    return localizedText(developmentTime, translation?.developmentTimeZh.orEmpty(), language)
}

fun Destination.localizedTicketInfo(language: AppLanguage): String {
    val translation = destinationTranslations[id]
    return localizedText(ticketInfo, translation?.ticketInfoZh.orEmpty(), language)
}

fun Destination.localizedKeywords(language: AppLanguage): List<String> {
    val translation = destinationTranslations[id]
    return translation?.keywordsZh ?: keywords
}

enum class FlightBookingStep { Search, Results, Checkout, Success }

enum class FlightCabin { Economy, Business }

enum class FlightBadge(val label: String) {
    Recommended("Recommended"),
    Fastest("Fastest"),
    Cheapest("Cheapest")
}

enum class FlightSort(val label: String) {
    Recommended("Recommended"),
    Cheapest("Cheapest"),
    Duration("Duration"),
    Departure("Departure")
}

data class FlightOption(
    val id: Int,
    val airline: String,
    val flightNumber: String,
    val logoUrl: String,
    val from: String,
    val to: String,
    val departureTime: String,
    val arrivalTime: String,
    val durationLabel: String,
    val durationMinutes: Int,
    val departureMinutes: Int,
    val tag: FlightBadge,
    val aircraft: String,
    val fares: List<FlightFare>
)

data class FlightFare(
    val cabin: FlightCabin,
    val price: Int,
    val baggage: String,
    val carryOn: String,
    val refundRule: String,
    val changeRule: String
)

data class FlightPassengerSelection(
    val economy: Int = 1,
    val business: Int = 0
)

data class FlightOrder(
    val id: String,
    val from: String,
    val to: String,
    val date: String,
    val airline: String,
    val flightNumber: String,
    val departureTime: String,
    val arrivalTime: String,
    val passengers: FlightPassengerSelection,
    val totalPrice: Int,
    val seatPreference: String,
    val contactPhone: String,
    val contactEmail: String,
    val cabinSummary: String,
    val status: String = "Ticketed"
)

data class ChatMessage(
    val text: String,
    val isMe: Boolean,
    val avatarUrl: String
)

data class MessageThread(
    val sender: String,
    val preview: String,
    val timestamp: String,
    val unreadCount: Int,
    val tag: String,
    val avatarUrl: String
)

data class CommunityAuthor(
    val name: String,
    val avatarUrl: String
)

data class CommunityMedia(
    val type: MediaType,
    val thumbnailUrl: String,
    val description: String,
    val durationLabel: String? = null
)

enum class MediaType { Image, Video }

data class CommunityPost(
    val id: Int,
    val author: CommunityAuthor,
    val title: String,
    val summary: String,
    val media: List<CommunityMedia>,
    val tags: List<String>,
    val location: String,
    val device: String,
    val likes: Int,
    val comments: Int,
    val saves: Int,
    val highlight: String = ""
)

enum class CommunityTab(
    val labelEn: String,
    val labelZh: String,
    val subLabelEn: String,
    val subLabelZh: String
) {
    Recommended("Recommended", "推荐", "For you & editors", "为你推荐与编辑精选"),
    Following("Following", "关注", "Authors & topics", "作者与话题"),
    Challenges("Activities", "活动", "Live challenges", "实时挑战")
}

fun CommunityTab.localizedLabel(language: AppLanguage): String {
    return localizedText(labelEn, labelZh, language)
}

@Composable
fun CommunityTab.localizedLabel(): String {
    return localizedText(labelEn, labelZh)
}

fun CommunityTab.localizedSubLabel(language: AppLanguage): String {
    return localizedText(subLabelEn, subLabelZh, language)
}

@Composable
fun CommunityTab.localizedSubLabel(): String {
    return localizedText(subLabelEn, subLabelZh)
}

enum class ShareType { PhotoText, Video }

enum class FavoriteTab(val labelEn: String, val labelZh: String) {
    PopularPicks("Popular Picks", "热门精选"),
    FavoritedPosts("Favorited Posts", "收藏的帖子")
}

fun FavoriteTab.localizedLabel(language: AppLanguage): String {
    return localizedText(labelEn, labelZh, language)
}

@Composable
fun FavoriteTab.localizedLabel(): String {
    return localizedText(labelEn, labelZh)
}
