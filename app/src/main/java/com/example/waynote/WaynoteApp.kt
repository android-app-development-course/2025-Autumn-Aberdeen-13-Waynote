package com.example.waynote


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.FlightTakeoff
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.annotation.DrawableRes
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.horizontalScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.waynote.ui.theme.AquaAccent
import coil.compose.AsyncImage
import com.example.waynote.ui.theme.PowderBlue
import com.example.waynote.ui.theme.SkyBlue
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WaynoteApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var appLanguage by rememberSaveable { mutableStateOf(AppLanguage.English) }

    val authPreferencesFlow = remember {
        context.authDataStore.data.map { prefs ->
            AuthPreferences(
                registeredUsername = prefs[AuthKeys.RegisteredUsername] ?: DEFAULT_USERNAME,
                registeredPassword = prefs[AuthKeys.RegisteredPassword] ?: DEFAULT_PASSWORD,
                isLoggedIn = prefs[AuthKeys.IsLoggedIn] ?: false
            )
        }
    }
    val authPreferences by authPreferencesFlow.collectAsState(initial = AuthPreferences())

    val tripPlanFlow = remember {
        context.tripPlannerDataStore.data.map { prefs ->
            TripPlan(
                title = prefs[TripPlannerKeys.Title] ?: "",
                destination = prefs[TripPlannerKeys.Destination] ?: "",
                startDate = prefs[TripPlannerKeys.StartDate] ?: "",
                endDate = prefs[TripPlannerKeys.EndDate] ?: "",
                notes = prefs[TripPlannerKeys.Notes] ?: "",
                activities = decodeActivities(prefs[TripPlannerKeys.Activities].orEmpty()),
                tripLengthDays = prefs[TripPlannerKeys.TripLengthDays] ?: DEFAULT_TRIP_LENGTH_DAYS
            )
        }
    }
    val savedTripPlan by tripPlanFlow.collectAsState(initial = TripPlan())

    val userContentFlow = remember {
        context.userContentDataStore.data.map { prefs ->
            UserContentState(
                favoriteDestinations = decodeIdList(prefs[UserContentKeys.FavoriteDestinations].orEmpty()),
                favoritePosts = decodeIdList(prefs[UserContentKeys.FavoritePosts].orEmpty()),
                orders = decodeOrders(prefs[UserContentKeys.FlightOrders].orEmpty())
            )
        }
    }
    val userContent by userContentFlow.collectAsState(initial = UserContentState())

    val favoriteDestinationIds = remember { mutableStateListOf<Int>() }
    val favoriteCommunityPostIds = remember { mutableStateListOf<Int>() }
    val flightOrders = remember { mutableStateListOf<FlightOrder>() }

    LaunchedEffect(userContent) {
        favoriteDestinationIds.clear()
        favoriteDestinationIds.addAll(userContent.favoriteDestinations)
        favoriteCommunityPostIds.clear()
        favoriteCommunityPostIds.addAll(userContent.favoritePosts)
        flightOrders.clear()
        flightOrders.addAll(userContent.orders)
    }

    val toggleFavoriteDestination: (Int) -> Unit = { id ->
        if (favoriteDestinationIds.contains(id)) {
            favoriteDestinationIds.remove(id)
        } else {
            favoriteDestinationIds.add(id)
        }
        scope.launch { persistFavorites(context, favoriteDestinationIds, favoriteCommunityPostIds) }
    }

    val favoriteDestinations = popularDestinations.filter { favoriteDestinationIds.contains(it.id) }
    val toggleFavoritePost: (CommunityPost) -> Unit = { post ->
        if (favoriteCommunityPostIds.contains(post.id)) {
            favoriteCommunityPostIds.remove(post.id)
        } else {
            favoriteCommunityPostIds.add(post.id)
        }
        scope.launch { persistFavorites(context, favoriteDestinationIds, favoriteCommunityPostIds) }
    }
    val allCommunityPosts = remember {
        recommendedCommunityPosts + followingCommunityPosts + challengeCommunityPosts
    }
    val favoriteCommunityPosts = allCommunityPosts.filter { favoriteCommunityPostIds.contains(it.id) }
    val addFlightOrder: (FlightOrder) -> Unit = { order ->
        flightOrders.add(0, order)
        scope.launch { persistOrders(context, flightOrders) }
    }
    val avatarUriFlow = remember {
        context.authDataStore.data.map { prefs ->
            prefs[AuthKeys.AvatarUri].orEmpty()
        }
    }
    val avatarUri by avatarUriFlow.collectAsState(initial = "")

    var registeredUsername by rememberSaveable { mutableStateOf(DEFAULT_USERNAME) }
    var registeredPassword by rememberSaveable { mutableStateOf(DEFAULT_PASSWORD) }
    var loginUsername by rememberSaveable { mutableStateOf("") }
    var loginPassword by rememberSaveable { mutableStateOf("") }
    var registerUsername by rememberSaveable { mutableStateOf("") }
    var registerPassword by rememberSaveable { mutableStateOf("") }
    var loginAttempts by rememberSaveable { mutableStateOf(0) }
    var lockRemainingSeconds by rememberSaveable { mutableStateOf(0) }

    LaunchedEffect(authPreferences.registeredUsername, authPreferences.registeredPassword) {
        registeredUsername = authPreferences.registeredUsername
        registeredPassword = authPreferences.registeredPassword
        if (loginUsername.isEmpty()) {
            loginUsername = authPreferences.registeredUsername
        }
    }

    LaunchedEffect(authPreferences.isLoggedIn) {
        if (authPreferences.isLoggedIn &&
            navController.currentBackStackEntry?.destination?.route == AppDestination.Login.route
        ) {
            navController.navigate(AppDestination.Home.route) {
                popUpTo(AppDestination.Login.route) { inclusive = true }
            }
        }
    }

    val isLoginDisabled = lockRemainingSeconds > 0 || loginAttempts >= MAX_LOGIN_ATTEMPTS

    LaunchedEffect(lockRemainingSeconds) {
        if (lockRemainingSeconds > 0) {
            while (lockRemainingSeconds > 0) {
                delay(1000)
                lockRemainingSeconds--
            }
            loginAttempts = 0
        }
    }

    CompositionLocalProvider(LocalAppLanguage provides appLanguage) {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(
                navController = navController,
                startDestination = AppDestination.Login.route
            ) {
            composable(AppDestination.Login.route) {
                LoginScreen(
                    username = loginUsername,
                    password = loginPassword,
                    onUsernameChange = { loginUsername = it },
                    onPasswordChange = { loginPassword = it },
                    onLoginClick = {
                        if (loginUsername.isBlank() || loginPassword.isBlank()) {
                            Toast.makeText(
                                context,
                                "Please enter username and password.",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@LoginScreen
                        }

                        val isMatch =
                            loginUsername.equals(registeredUsername, ignoreCase = true) &&
                                loginPassword == registeredPassword
                        if (isMatch) {
                            loginAttempts = 0
                            lockRemainingSeconds = 0
                            loginUsername = ""
                            loginPassword = ""
                            scope.launch {
                                persistAuth(
                                    context = context,
                                    username = registeredUsername,
                                    password = registeredPassword,
                                    isLoggedIn = true
                                )
                            }
                            navController.navigate(AppDestination.Home.route) {
                                popUpTo(AppDestination.Login.route) { inclusive = true }
                            }
                        } else {
                            val updatedAttempts = loginAttempts + 1
                            loginAttempts = updatedAttempts
                            val reachedLimit = updatedAttempts >= MAX_LOGIN_ATTEMPTS
                            if (reachedLimit) {
                                lockRemainingSeconds = LOCKOUT_DURATION_SECONDS
                            }
                            val attemptsLeft = (MAX_LOGIN_ATTEMPTS - updatedAttempts).coerceAtLeast(0)
                            val message = if (reachedLimit) {
                                "Login disabled for $LOCKOUT_DURATION_SECONDS seconds."
                            } else {
                                "Incorrect username or password. $attemptsLeft attempt(s) remaining."
                            }
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    },
                    onRegisterClick = { navController.navigate(AppDestination.Register.route) },
                    isLoginDisabled = isLoginDisabled,
                    lockRemainingSeconds = lockRemainingSeconds,
                    onSkipLogin = {
                        scope.launch { markLoggedOut(context) }
                        navController.navigate(AppDestination.Home.route) {
                            popUpTo(AppDestination.Login.route) { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 32.dp)
                )
            }

            composable(AppDestination.Register.route) {
                RegisterScreen(
                    username = registerUsername,
                    password = registerPassword,
                    onUsernameChange = { registerUsername = it },
                    onPasswordChange = { registerPassword = it },
                    onRegisterSubmit = {
                        if (registerUsername.isBlank() || registerPassword.isBlank()) {
                            Toast.makeText(
                                context,
                                "Username and password cannot be empty.",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@RegisterScreen
                        }
                        registeredUsername = registerUsername
                        registeredPassword = registerPassword
                        registerUsername = ""
                        registerPassword = ""
                        loginAttempts = 0
                        lockRemainingSeconds = 0
                        scope.launch {
                            persistAuth(
                                context = context,
                                username = registeredUsername,
                                password = registeredPassword,
                                isLoggedIn = false
                            )
                        }
                        Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    },
                    onBackToLogin = { navController.popBackStack() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 32.dp)
                )
            }

            composable(AppDestination.Home.route) {
                MainScreen(
                    navController = navController,
                    currentTab = MainTab.Home,
                    currentUser = registeredUsername,
                    avatarUri = avatarUri,
                    onAvatarChange = { uri ->
                        scope.launch { persistAvatarUri(context, uri) }
                    },
                    favoriteDestinations = favoriteDestinations,
                    favoritePostCount = favoriteCommunityPostIds.size,
                    flightOrders = flightOrders,
                    onDestinationClick = { destination ->
                        navController.navigate("${AppDestination.DestinationDetail.route}/${destination.id}")
                    },
                    onFavoritesClick = { navController.navigate(AppDestination.Favorites.route) },
                    appLanguage = appLanguage,
                    onLanguageChange = { appLanguage = it }
                )
            }

            composable(AppDestination.Messages.route) {
                MainScreen(
                    navController = navController,
                    currentTab = MainTab.Messages,
                    currentUser = registeredUsername,
                    avatarUri = avatarUri,
                    onAvatarChange = { uri ->
                        scope.launch { persistAvatarUri(context, uri) }
                    },
                    favoriteDestinations = favoriteDestinations,
                    favoritePostCount = favoriteCommunityPostIds.size,
                    flightOrders = flightOrders,
                    onDestinationClick = { destination ->
                        navController.navigate("${AppDestination.DestinationDetail.route}/${destination.id}")
                    },
                    onFavoritesClick = { navController.navigate(AppDestination.Favorites.route) },
                    appLanguage = appLanguage,
                    onLanguageChange = { appLanguage = it }
                )
            }

            composable(AppDestination.Profile.route) {
                MainScreen(
                    navController = navController,
                    currentTab = MainTab.Profile,
                    currentUser = registeredUsername,
                    avatarUri = avatarUri,
                    onAvatarChange = { uri ->
                        scope.launch { persistAvatarUri(context, uri) }
                    },
                    favoriteDestinations = favoriteDestinations,
                    favoritePostCount = favoriteCommunityPostIds.size,
                    flightOrders = flightOrders,
                    onDestinationClick = { destination ->
                        navController.navigate("${AppDestination.DestinationDetail.route}/${destination.id}")
                    },
                    onOrdersClick = { navController.navigate(AppDestination.Orders.route) },
                    onFavoritesClick = { navController.navigate(AppDestination.Favorites.route) },
                    onLogout = {
                        loginAttempts = 0
                        lockRemainingSeconds = 0
                        loginUsername = registeredUsername
                        loginPassword = ""
                        scope.launch { markLoggedOut(context) }
                        navController.navigate(AppDestination.Login.route) {
                            popUpTo(AppDestination.Home.route) { inclusive = true }
                        }
                    },
                    appLanguage = appLanguage,
                    onLanguageChange = { appLanguage = it }
                )
            }

            composable(AppDestination.TripPlanner.route) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    TripPlannerScreen(
                        navController = navController,
                        savedTripPlan = savedTripPlan,
                        onSaveTripPlan = { plan ->
                            scope.launch { persistTripPlan(context, plan) }
                        },
                        onNavigateToSaved = { navController.navigate(AppDestination.SavedTrip.route) }
                    )
                } else {
                    UnsupportedVersionScreen(onBack = { navController.popBackStack() })
                }
            }

            composable(AppDestination.CommunityGuides.route) {
                CommunityScreen(
                    navController = navController,
                    favoritePostIds = favoriteCommunityPostIds,
                    onTogglePostFavorite = toggleFavoritePost
                )
            }

            composable(AppDestination.Favorites.route) {
                FavoritesScreen(
                    navController = navController,
                    favoriteDestinations = favoriteDestinations,
                    favoritePosts = favoriteCommunityPosts,
                    favoritePostIds = favoriteCommunityPostIds,
                    onDestinationClick = { destination ->
                        navController.navigate("${AppDestination.DestinationDetail.route}/${destination.id}")
                    },
                    onTogglePostFavorite = { post -> toggleFavoritePost(post) }
                )
            }
            composable(AppDestination.Orders.route) {
                OrdersScreen(
                    navController = navController,
                    orders = flightOrders
                )
            }

            composable(AppDestination.FlightBooking.route) {
                FlightBookingScreen(
                    navController = navController,
                    onAddOrder = { order ->
                        addFlightOrder(order)
                        Toast.makeText(context, "Flight added to My Orders", Toast.LENGTH_SHORT).show()
                    },
                    onNavigateToOrders = {
                        navController.popBackStack(AppDestination.FlightBooking.route, inclusive = true)
                        navController.navigate(AppDestination.Orders.route) {
                            popUpTo(AppDestination.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }

            composable(AppDestination.SavedTrip.route) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    SavedTripScreen(
                        navController = navController,
                        savedTripPlan = savedTripPlan,
                        onDeleteTripPlan = {
                            scope.launch { clearTripPlan(context) }
                        }
                    )
                } else {
                    UnsupportedVersionScreen(onBack = { navController.popBackStack() })
                }
            }

            composable(
                route = "${AppDestination.DestinationDetail.route}/{destinationId}",
                arguments = listOf(
                    navArgument("destinationId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val destinationId = backStackEntry.arguments?.getInt("destinationId")
                val destination = popularDestinations.firstOrNull { it.id == destinationId }
                val isFavorite = destination?.id?.let { favoriteDestinationIds.contains(it) } ?: false
                DestinationDetailScreen(
                    destination = destination,
                    isFavorite = isFavorite,
                    onToggleFavorite = { dest ->
                        toggleFavoriteDestination(dest.id)
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                route = "${AppDestination.Chat.route}/{sender}",
                arguments = listOf(
                    navArgument("sender") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val sender = backStackEntry.arguments?.getString("sender").orEmpty()
                ChatScreen(sender = sender, navController = navController)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnsupportedVersionScreen(
    title: String = localizedText("Trip Planner", "行程规划"),
    message: String = localizedText(
        "Trip Planner requires Android 8.0 or above.",
        "行程规划需要 Android 8.0 以上版本。"
    ),
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = localizedText("Back", "返回")
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun MainScreen(
    navController: NavHostController,
    currentTab: MainTab,
    currentUser: String = "",
    avatarUri: String = "",
    onAvatarChange: (String) -> Unit = {},
    favoriteDestinations: List<Destination>,
    favoritePostCount: Int,
    flightOrders: List<FlightOrder> = emptyList(),
    onDestinationClick: (Destination) -> Unit,
    onOrdersClick: () -> Unit = {},
    onFavoritesClick: () -> Unit = {},
    onLogout: () -> Unit = {},
    appLanguage: AppLanguage = AppLanguage.English,
    onLanguageChange: (AppLanguage) -> Unit = {}
) {
    Scaffold(
        bottomBar = {
            NavigationBar(tonalElevation = 6.dp) {
                MainTab.values().forEach { tab ->
                    val tabLabel = tab.localizedLabel(appLanguage)
                    NavigationBarItem(
                        selected = tab == currentTab,
                        onClick = {
                            if (tab.route != currentTab.route) {
                                navController.navigate(tab.route) {
                                    popUpTo(AppDestination.Home.route) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = { Icon(imageVector = tab.icon, contentDescription = tabLabel) },
                        label = { Text(tabLabel) }
                    )
                }
            }
        }
    ) { paddingValues ->
        when (currentTab) {
            MainTab.Home -> HomeContent(
                onNavigateToTripPlanner = { navController.navigate(AppDestination.TripPlanner.route) },
                onNavigateToGuides = { navController.navigate(AppDestination.CommunityGuides.route) },
                onNavigateToFlight = { navController.navigate(AppDestination.FlightBooking.route) },
                onDestinationClick = onDestinationClick,
                language = appLanguage,
                modifier = Modifier.padding(paddingValues)
            )
            MainTab.Messages -> MessagesScreen(
                navController = navController,
                modifier = Modifier.padding(paddingValues)
            )
            MainTab.Profile -> ProfileScreen(
                currentUser = currentUser,
                avatarUri = avatarUri,
                onAvatarChange = onAvatarChange,
                favoriteDestinationCount = favoriteDestinations.size,
                favoritePostCount = favoritePostCount,
                flightOrders = flightOrders,
                onOrdersClick = onOrdersClick,
                onFavoritesClick = onFavoritesClick,
                onLogout = onLogout,
                language = appLanguage,
                onLanguageChange = onLanguageChange,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}
