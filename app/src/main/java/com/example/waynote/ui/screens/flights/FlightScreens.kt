package com.example.waynote


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import com.example.waynote.R
import com.example.waynote.ui.theme.WaynoteTheme
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun FlightBookingScreen(
    navController: NavHostController,
    onAddOrder: (FlightOrder) -> Unit,
    onNavigateToOrders: () -> Unit
) {
    val context = LocalContext.current
    var fromCity by rememberSaveable { mutableStateOf("Guangzhou (CAN)") }
    var toCity by rememberSaveable { mutableStateOf("Shanghai (SHA)") }
    var travelDate by rememberSaveable { mutableStateOf(currentIsoDate()) }
    var flexibleDates by rememberSaveable { mutableStateOf(true) }
    var isDatePickerOpen by rememberSaveable { mutableStateOf(false) }
    var step by rememberSaveable { mutableStateOf(FlightBookingStep.Search) }
    var selectedFlight by remember { mutableStateOf<FlightOption?>(null) }
    var expandedFlightId by rememberSaveable { mutableStateOf<Int?>(null) }
    val selectionByFlight = remember { mutableStateMapOf<Int, FlightPassengerSelection>() }
    var currentSelection by remember { mutableStateOf(FlightPassengerSelection()) }
    var sortOption by rememberSaveable { mutableStateOf(FlightSort.Recommended) }
    var contactPhone by rememberSaveable { mutableStateOf("") }
    var contactEmail by rememberSaveable { mutableStateOf("") }
    var seatPreference by rememberSaveable { mutableStateOf("Aisle") }
    var paymentMethod by rememberSaveable { mutableStateOf("WeChat Pay") }

    val travelDateValue = remember(travelDate) {
        runCatching { LocalDate.parse(travelDate, dateFormatter) }.getOrNull() ?: LocalDate.now(systemZone)
    }

    val flights = remember { guangzhouShanghaiFlights }
    val sortedFlights = remember(sortOption) {
        flights.sortedWith(
            when (sortOption) {
                FlightSort.Recommended -> compareByDescending<FlightOption> { it.tag == FlightBadge.Recommended }
                    .thenBy { it.economyFare()?.price ?: Int.MAX_VALUE }
                FlightSort.Cheapest -> compareBy { it.economyFare()?.price ?: Int.MAX_VALUE }
                FlightSort.Duration -> compareBy { it.durationMinutes }
                FlightSort.Departure -> compareBy { it.departureMinutes }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Book Flights") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    TextButton(onClick = onNavigateToOrders) {
                        Text("My Orders")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (step) {
                FlightBookingStep.Search -> {
                    FlightSearchForm(
                        fromCity = fromCity,
                        toCity = toCity,
                        date = travelDateValue,
                        flexibleDates = flexibleDates,
                        onFromChange = { fromCity = it },
                        onToChange = { toCity = it },
                        onSwap = {
                            val temp = fromCity
                            fromCity = toCity
                            toCity = temp
                        },
                        onDateClick = { isDatePickerOpen = true },
                        onDateChange = {
                            travelDate = it.format(dateFormatter)
                        },
                        onToggleFlexible = { flexibleDates = it },
                        onSearch = {
                            step = FlightBookingStep.Results
                        }
                    )
                }

                FlightBookingStep.Results -> {
                    FlightResultsSection(
                        fromCity = fromCity,
                        toCity = toCity,
                        date = travelDateValue,
                        flights = sortedFlights,
                        sort = sortOption,
                        expandedFlightId = expandedFlightId,
                        selections = selectionByFlight,
                        onSortChange = { sortOption = it },
                        onToggleExpand = { id ->
                            expandedFlightId = if (expandedFlightId == id) null else id
                        },
                        onSelectionChange = { id, selection ->
                            selectionByFlight[id] = selection
                        },
                        onCheckout = { flight ->
                            val selection = selectionByFlight[flight.id] ?: FlightPassengerSelection()
                            if (selection.totalCount() == 0) {
                                Toast.makeText(context, "Add passengers to continue.", Toast.LENGTH_SHORT).show()
                            } else {
                                selectedFlight = flight
                                currentSelection = selection
                                step = FlightBookingStep.Checkout
                            }
                        },
                        onBackToSearch = { step = FlightBookingStep.Search }
                    )
                }

                FlightBookingStep.Checkout -> {
                    val flight = selectedFlight
                    if (flight == null) {
                        step = FlightBookingStep.Search
                    } else {
                        FlightCheckoutSection(
                            flight = flight,
                            date = travelDateValue,
                            selection = currentSelection,
                            contactPhone = contactPhone,
                            contactEmail = contactEmail,
                            seatPreference = seatPreference,
                            paymentMethod = paymentMethod,
                            onContactPhoneChange = { contactPhone = it },
                            onContactEmailChange = { contactEmail = it },
                            onSeatPreferenceChange = { seatPreference = it },
                            onPaymentMethodChange = { paymentMethod = it },
                            onBack = { step = FlightBookingStep.Results },
                            onPay = {
                                if (contactPhone.isBlank() || contactEmail.isBlank()) {
                                    Toast.makeText(context, "Add phone and email to issue tickets.", Toast.LENGTH_SHORT).show()
                                    return@FlightCheckoutSection
                                }
                                val totalPrice = computeOrderPrice(flight, currentSelection)
                                val order = FlightOrder(
                                    id = UUID.randomUUID().toString(),
                                    from = fromCity,
                                    to = toCity,
                                    date = travelDateValue.format(dateFormatter),
                                    airline = flight.airline,
                                    flightNumber = flight.flightNumber,
                                    departureTime = flight.departureTime,
                                    arrivalTime = flight.arrivalTime,
                                    passengers = currentSelection,
                                    totalPrice = totalPrice,
                                    seatPreference = seatPreference,
                                    contactPhone = contactPhone,
                                    contactEmail = contactEmail,
                                    cabinSummary = currentSelection.toCabinLabel(flight)
                                )
                                onAddOrder(order)
                                step = FlightBookingStep.Success
                            }
                        )
                    }
                }

                FlightBookingStep.Success -> {
                    val flight = selectedFlight
                    if (flight == null) {
                        step = FlightBookingStep.Search
                    } else {
                        PaymentSuccessSection(
                            flight = flight,
                            selection = currentSelection,
                            onViewOrders = {
                                step = FlightBookingStep.Search
                                onNavigateToOrders()
                            },
                            onBookAgain = {
                                step = FlightBookingStep.Search
                            }
                        )
                    }
                }
            }
        }
    }

    if (isDatePickerOpen) {
        val dateState = rememberDatePickerState(
            initialSelectedDateMillis = parseDateToMillis(travelDate) ?: System.currentTimeMillis()
        )
        DatePickerDialog(
            onDismissRequest = { isDatePickerOpen = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selected = dateState.selectedDateMillis
                        if (selected != null) {
                            travelDate = formatDate(selected)
                        }
                        isDatePickerOpen = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { isDatePickerOpen = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = dateState)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun FlightSearchForm(
    fromCity: String,
    toCity: String,
    date: LocalDate,
    flexibleDates: Boolean,
    onFromChange: (String) -> Unit,
    onToChange: (String) -> Unit,
    onSwap: () -> Unit,
    onDateClick: () -> Unit,
    onDateChange: (LocalDate) -> Unit,
    onToggleFlexible: (Boolean) -> Unit,
    onSearch: () -> Unit
) {
    val quickDepartures = listOf("Guangzhou (CAN)", "Shenzhen (SZX)", "Beijing (PEK)")
    val quickArrivals = listOf("Shanghai (SHA)", "Shanghai (PVG)", "Hangzhou (HGH)")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 4.dp,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.08f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.14f))
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        modifier = Modifier.weight(1f),
                        value = fromCity,
                        onValueChange = onFromChange,
                        label = { Text("From") },
                        leadingIcon = { Icon(imageVector = Icons.Outlined.Place, contentDescription = null) },
                        singleLine = true
                    )
                    IconButton(onClick = onSwap) {
                        Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Swap", modifier = Modifier.rotate(180f))
                    }
                    OutlinedTextField(
                        modifier = Modifier.weight(1f),
                        value = toCity,
                        onValueChange = onToChange,
                        label = { Text("To") },
                        leadingIcon = { Icon(imageVector = Icons.Outlined.Search, contentDescription = null) },
                        singleLine = true
                    )
                }
                Text(text = "Popular departures", style = MaterialTheme.typography.labelLarge)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(quickDepartures) { city ->
                        QuickChip(text = city, selected = city == fromCity, onClick = { onFromChange(city) })
                    }
                }
                Text(text = "Hot arrivals", style = MaterialTheme.typography.labelLarge)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(quickArrivals) { city ->
                        QuickChip(text = city, selected = city == toCity, onClick = { onToChange(city) })
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Date", style = MaterialTheme.typography.labelLarge)
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onDateClick),
                        shape = RoundedCornerShape(14.dp),
                        tonalElevation = 1.dp,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = date.toString(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                Text(
                                    text = date.dayOfWeek.toString().lowercase().replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                            Icon(imageVector = Icons.Outlined.ExpandMore, contentDescription = "Pick date")
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { onDateChange(date.minusDays(1)) }) {
                            Icon(imageVector = Icons.Outlined.ChevronLeft, contentDescription = "Previous day")
                        }
                        Text(
                            text = date.format(dateFormatter),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        IconButton(onClick = { onDateChange(date.plusDays(1)) }) {
                            Icon(imageVector = Icons.Outlined.ChevronRight, contentDescription = "Next day")
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Flexible dates", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                        Text(
                            text = "Search ±2 days if cheaper",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                    Switch(checked = flexibleDates, onCheckedChange = onToggleFlexible)
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onSearch,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Search flights")
                }
            }
        }
    }
}

@Composable
private fun QuickChip(text: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(50),
        color = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.14f) else MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = if (selected) 0.5f else 0.2f)),
        onClick = onClick,
        tonalElevation = if (selected) 2.dp else 0.dp
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun FlightResultsSection(
    fromCity: String,
    toCity: String,
    date: LocalDate,
    flights: List<FlightOption>,
    sort: FlightSort,
    expandedFlightId: Int?,
    selections: Map<Int, FlightPassengerSelection>,
    onSortChange: (FlightSort) -> Unit,
    onToggleExpand: (Int) -> Unit,
    onSelectionChange: (Int, FlightPassengerSelection) -> Unit,
    onCheckout: (FlightOption) -> Unit,
    onBackToSearch: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "$fromCity → $toCity",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = date.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                TextButton(onClick = onBackToSearch) { Text("Edit search") }
            }
        }
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                tonalElevation = 1.dp,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
            ) {
                LazyRow(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(FlightSort.values()) { option ->
                        QuickChip(
                            text = option.label,
                            selected = option == sort,
                            onClick = { onSortChange(option) }
                        )
                    }
                }
            }
        }
        items(flights) { flight ->
            val selection = selections[flight.id] ?: FlightPassengerSelection()
            FlightResultCard(
                flight = flight,
                selection = selection,
                expanded = expandedFlightId == flight.id,
                onToggleExpand = { onToggleExpand(flight.id) },
                onSelectionChange = { onSelectionChange(flight.id, it) },
                onCheckout = { onCheckout(flight) }
            )
        }
        if (flights.isEmpty()) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 1.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("No flights found", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text(
                            text = "Adjust dates or swap cities to refresh the list.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FlightResultCard(
    flight: FlightOption,
    selection: FlightPassengerSelection,
    expanded: Boolean,
    onToggleExpand: () -> Unit,
    onSelectionChange: (FlightPassengerSelection) -> Unit,
    onCheckout: () -> Unit
) {
    val economyFare = flight.economyFare()
    val businessFare = flight.businessFare()
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        tonalElevation = 3.dp,
        shadowElevation = 4.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.08f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Surface(shape = CircleShape, tonalElevation = 1.dp) {
                        AsyncImage(
                            model = flight.logoUrl,
                            contentDescription = flight.airline,
                            modifier = Modifier.size(44.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Column {
                        Text(flight.airline, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Text(
                            text = flight.flightNumber,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
                FlightBadgeTag(tag = flight.tag)
            }
            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.18f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(flight.departureTime, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(
                        text = flight.from,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(flight.arrivalTime, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(
                        text = flight.to,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    tonalElevation = 1.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = economyFare?.let { "¥${it.price}" } ?: "--",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Economy",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
                        )
                    }
                }
            }
            Text(
                text = flight.durationLabel,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 2.dp)
            )
            if (expanded) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    tonalElevation = 1.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.08f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Aircraft ${flight.aircraft} · ${flight.durationLabel}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        economyFare?.let {
                            FlightCabinRow(
                                title = "Economy",
                                fare = it,
                                count = selection.economy,
                                onCountChange = { count -> onSelectionChange(selection.copy(economy = count)) }
                            )
                        }
                        businessFare?.let {
                            FlightCabinRow(
                                title = "Business",
                                fare = it,
                                count = selection.business,
                                onCountChange = { count -> onSelectionChange(selection.copy(business = count)) }
                            )
                        }
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = onCheckout,
                            enabled = selection.totalCount() > 0,
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Checkout")
                        }
                        TextButton(
                            onClick = onToggleExpand,
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Hide details")
                        }
                    }
                }
            } else {
                TextButton(
                    onClick = onToggleExpand,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Show details")
                }
            }
        }
    }
}

@Composable
private fun FlightCabinRow(
    title: String,
    fare: FlightFare,
    count: Int,
    onCountChange: (Int) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    Text(
                        text = "Carry-on ${fare.carryOn} · Checked ${fare.baggage}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                Text(
                    text = "¥${fare.price}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "Refund: ${fare.refundRule}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
            )
            Text(
                text = "Change: ${fare.changeRule}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
            )
            PassengerStepper(count = count, onCountChange = onCountChange)
        }
    }
}

@Composable
private fun PassengerStepper(
    count: Int,
    onCountChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Passengers",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = { onCountChange((count - 1).coerceAtLeast(0)) },
                enabled = count > 0,
                shape = RoundedCornerShape(50)
            ) {
                Icon(imageVector = Icons.Outlined.Remove, contentDescription = "Minus")
            }
            Text(
                text = "$count",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            OutlinedButton(
                onClick = { onCountChange((count + 1).coerceAtMost(6)) },
                shape = RoundedCornerShape(50)
            ) {
                Icon(imageVector = Icons.Outlined.Add, contentDescription = "Add")
            }
        }
    }
}

@Composable
private fun FlightBadgeTag(tag: FlightBadge) {
    val colors = when (tag) {
        FlightBadge.Recommended -> MaterialTheme.colorScheme.primary.copy(alpha = 0.14f) to MaterialTheme.colorScheme.primary
        FlightBadge.Fastest -> Color(0xFF3DDC97).copy(alpha = 0.18f) to Color(0xFF0E9F6E)
        FlightBadge.Cheapest -> Color(0xFFFFF3CD) to Color(0xFFB77800)
    }
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = colors.first,
        border = BorderStroke(1.dp, colors.second.copy(alpha = 0.6f))
    ) {
        Text(
            text = tag.label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = colors.second
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun FlightCheckoutSection(
    flight: FlightOption,
    date: LocalDate,
    selection: FlightPassengerSelection,
    contactPhone: String,
    contactEmail: String,
    seatPreference: String,
    paymentMethod: String,
    onContactPhoneChange: (String) -> Unit,
    onContactEmailChange: (String) -> Unit,
    onSeatPreferenceChange: (String) -> Unit,
    onPaymentMethodChange: (String) -> Unit,
    onBack: () -> Unit,
    onPay: () -> Unit
) {
    val scrollState = rememberScrollState()
    val economyFare = flight.economyFare()
    val businessFare = flight.businessFare()
    val totalPrice = computeOrderPrice(flight, selection)

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .padding(bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Checkout", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                TextButton(onClick = onBack) { Text("Back to results") }
            }
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("${flight.airline} • ${flight.flightNumber}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(
                        text = "$date · ${flight.from} ${flight.departureTime} → ${flight.to} ${flight.arrivalTime}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = selection.toCabinLabel(flight),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 1.dp
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("Passenger contact", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = contactPhone,
                        onValueChange = onContactPhoneChange,
                        label = { Text("Phone") },
                        leadingIcon = { Icon(imageVector = Icons.Outlined.Phone, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next)
                    )
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = contactEmail,
                        onValueChange = onContactEmailChange,
                        label = { Text("Email") },
                        leadingIcon = { Icon(imageVector = Icons.Outlined.Inbox, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done)
                    )
                    Text("Seat preference", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Aisle", "Window", "Quiet").forEach { option ->
                            QuickChip(
                                text = option,
                                selected = seatPreference == option,
                                onClick = { onSeatPreferenceChange(option) }
                            )
                        }
                    }
                }
            }
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                tonalElevation = 1.dp
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Payment", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("WeChat Pay", "Alipay", "Credit Card").forEach { option ->
                            QuickChip(
                                text = option,
                                selected = paymentMethod == option,
                                onClick = { onPaymentMethodChange(option) }
                            )
                        }
                    }
                    Text(
                        text = "Price details",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    economyFare?.takeIf { selection.economy > 0 }?.let {
                        Text(
                            text = "Economy x${selection.economy} · ¥${it.price} each",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    businessFare?.takeIf { selection.business > 0 }?.let {
                        Text(
                            text = "Business x${selection.business} · ¥${it.price} each",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        PriceSummaryBar(
            totalPrice = totalPrice,
            passengerLabel = selection.toPassengerLabel(),
            onPay = onPay,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun PriceSummaryBar(
    totalPrice: Int,
    passengerLabel: String,
    onPay: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        shadowElevation = 6.dp,
        tonalElevation = 6.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Total", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                Text(
                    text = "¥$totalPrice",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(passengerLabel, style = MaterialTheme.typography.bodySmall)
            }
            Button(
                onClick = onPay,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.widthIn(min = 140.dp)
            ) {
                Text("Pay now")
            }
        }
    }
}

@Composable
private fun PaymentSuccessSection(
    flight: FlightOption,
    selection: FlightPassengerSelection,
    onViewOrders: () -> Unit,
    onBookAgain: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.CheckCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(72.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text("Payment successful", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(
            text = "${flight.airline} ${flight.flightNumber} • ${selection.toPassengerLabel()}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
        Spacer(modifier = Modifier.height(18.dp))
        Button(
            onClick = onViewOrders,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("View in My Orders")
        }
        OutlinedButton(
            onClick = onBookAgain,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Book another flight")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    navController: NavHostController,
    orders: List<FlightOrder>,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("My Orders") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (orders.isEmpty()) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Outlined.Inbox,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text("No flight orders yet", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(
                    text = "Book flights to see tickets and passengers here.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(orders) { order ->
                    FlightOrderRow(order)
                }
            }
        }
    }
}

@Composable
fun FlightOrdersCard(
    orders: List<FlightOrder>,
    onClick: () -> Unit,
    language: AppLanguage = LocalAppLanguage.current
) {
    val summary = when {
        orders.isEmpty() -> localizedText("No flight orders yet.", "暂无机票订单。", language)
        orders.size == 1 -> "${orders.first().from} → ${orders.first().to}"
        else -> localizedText(
            "${orders.first().from} → ${orders.first().to} · ${orders.size} orders",
            "${orders.first().from} → ${orders.first().to} · ${orders.size} 个订单",
            language
        )
    }
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = localizedText("My Orders", "我的订单", language),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = summary,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = localizedText("My Orders", "我的订单", language),
                modifier = Modifier.rotate(180f)
            )
        }
    }
}
@Composable
private fun FlightOrderRow(order: FlightOrder) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.14f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${order.from} → ${order.to}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = order.status,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "${order.date} · ${order.departureTime} - ${order.arrivalTime}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
            )
            Text(
                text = order.cabinSummary,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
            Text(
                text = "¥${order.totalPrice} • ${order.passengers.toPassengerLabel()} • ${order.airline}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

private fun FlightOption.economyFare(): FlightFare? =
    fares.firstOrNull { it.cabin == FlightCabin.Economy }

private fun FlightOption.businessFare(): FlightFare? =
    fares.firstOrNull { it.cabin == FlightCabin.Business }

private fun FlightPassengerSelection.totalCount(): Int = economy + business

private fun FlightPassengerSelection.toPassengerLabel(): String {
    val parts = mutableListOf<String>()
    if (economy > 0) parts.add("$economy economy")
    if (business > 0) parts.add("$business business")
    return if (parts.isEmpty()) "No passengers" else parts.joinToString(" · ")
}

private fun FlightPassengerSelection.toCabinLabel(flight: FlightOption): String {
    val parts = mutableListOf<String>()
    flight.economyFare()?.let { fare ->
        if (economy > 0) parts.add("Economy x$economy @ ¥${fare.price}")
    }
    flight.businessFare()?.let { fare ->
        if (business > 0) parts.add("Business x$business @ ¥${fare.price}")
    }
    return if (parts.isEmpty()) "Select passengers" else parts.joinToString(" + ")
}

private fun computeOrderPrice(flight: FlightOption, selection: FlightPassengerSelection): Int {
    val economyTotal = (flight.economyFare()?.price ?: 0) * selection.economy
    val businessTotal = (flight.businessFare()?.price ?: 0) * selection.business
    return economyTotal + businessTotal
}

