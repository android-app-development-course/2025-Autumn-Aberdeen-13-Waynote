package com.example.waynote


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripPlannerScreen(
    navController: NavHostController,
    savedTripPlan: TripPlan,
    onSaveTripPlan: (TripPlan) -> Unit,
    onNavigateToSaved: () -> Unit
) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        UnsupportedVersionScreen(
            title = localizedText("Trip Planner", "行程规划"),
            message = localizedText("Trip Planner requires Android 8.0 or above.", "行程规划需要 Android 8.0 以上版本。"),
            onBack = { navController.popBackStack() }
        )
        return
    }
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val calendarPermission = Manifest.permission.READ_CALENDAR
    var hasRequestedCalendarPermission by rememberSaveable { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { }
    )
    var title by rememberSaveable { mutableStateOf("") }
    var destination by rememberSaveable { mutableStateOf("") }
    var startDate by rememberSaveable { mutableStateOf("") }
    var endDate by rememberSaveable { mutableStateOf("") }
    var notes by rememberSaveable { mutableStateOf("") }
    var newActivity by rememberSaveable { mutableStateOf("") }
    val activities = remember { mutableStateListOf<String>() }
    var tripLengthDays by rememberSaveable { mutableStateOf(DEFAULT_TRIP_LENGTH_DAYS) }
    var isDestinationMenuOpen by remember { mutableStateOf(false) }
    var isCustomDestination by rememberSaveable { mutableStateOf(false) }
    var startDateMillis by rememberSaveable { mutableStateOf(0L) }
    var endDateMillis by rememberSaveable { mutableStateOf(0L) }
    var isStartPickerOpen by remember { mutableStateOf(false) }
    var isEndPickerOpen by remember { mutableStateOf(false) }
    var hasHydrated by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!hasRequestedCalendarPermission &&
            ContextCompat.checkSelfPermission(context, calendarPermission) != PackageManager.PERMISSION_GRANTED
        ) {
            hasRequestedCalendarPermission = true
            permissionLauncher.launch(calendarPermission)
        }
    }

    LaunchedEffect(savedTripPlan) {
        val currentPlan = TripPlan(
            title = title,
            destination = destination,
            startDate = startDate,
            endDate = endDate,
            notes = notes,
            activities = activities.toList(),
            tripLengthDays = tripLengthDays
        )
        if (!hasHydrated || savedTripPlan != currentPlan) {
            title = savedTripPlan.title
            destination = savedTripPlan.destination
            isCustomDestination = destination.isNotBlank() && destination !in DESTINATION_OPTIONS
            startDate = savedTripPlan.startDate
            endDate = savedTripPlan.endDate
            notes = savedTripPlan.notes
            activities.clear()
            activities.addAll(savedTripPlan.activities)
            startDateMillis = parseDateToMillis(savedTripPlan.startDate) ?: 0L
            endDateMillis = parseDateToMillis(savedTripPlan.endDate) ?: 0L
            tripLengthDays = computeTripLengthDays(startDateMillis, endDateMillis) ?: savedTripPlan.tripLengthDays
            hasHydrated = true
        }
    }

    val savePlan: () -> Unit = {
        if (title.isBlank() || destination.isBlank()) {
            Toast.makeText(context, "Please enter a trip title and destination.", Toast.LENGTH_SHORT).show()
        } else if (startDate.isBlank() || endDate.isBlank()) {
            Toast.makeText(context, "Please select start and end dates.", Toast.LENGTH_SHORT).show()
        } else {
            val computedDays = computeTripLengthDays(startDateMillis, endDateMillis)
            if (computedDays == null) {
                Toast.makeText(context, "End date must be after start date.", Toast.LENGTH_SHORT).show()
            } else {
                tripLengthDays = computedDays
                val plan = TripPlan(
                    title = title.trim(),
                    destination = destination.trim(),
                    startDate = startDate.trim(),
                    endDate = endDate.trim(),
                    notes = notes.trim(),
                    activities = activities.map { it.trim() }.filter { it.isNotEmpty() },
                    tripLengthDays = computedDays
                )
                onSaveTripPlan(plan)
                Toast.makeText(context, "Trip saved locally.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Trip Planner") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToSaved) {
                        Icon(imageVector = Icons.Outlined.Save, contentDescription = "Saved trips")
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 4.dp
            ) {
                Button(
                    modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                shape = RoundedCornerShape(18.dp),
                onClick = savePlan
            ) {
                Text("Save trip")
            }
        }
    }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val computedDays = computeTripLengthDays(startDateMillis, endDateMillis)
            val autoPlanEnabled = computedDays != null && computedDays <= 5 && destination.isNotBlank() && !isCustomDestination
            val lengthLabel = computedDays?.toString() ?: "—"
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                tonalElevation = 3.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Basics",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Trip title") },
                        placeholder = { Text("e.g. Kyoto autumn getaway") },
                        singleLine = true
                    )
                    Box {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .then(
                                    if (isCustomDestination) {
                                        Modifier
                                    } else {
                                        Modifier.clickable { isDestinationMenuOpen = true }
                                    }
                                ),
                            value = destination,
                            onValueChange = { if (isCustomDestination) destination = it },
                            label = { Text("Destination") },
                            placeholder = { Text(if (isCustomDestination) "Type your destination" else "Select a city") },
                            singleLine = true,
                            readOnly = !isCustomDestination,
                            trailingIcon = {
                                IconButton(onClick = { isDestinationMenuOpen = true }) {
                                    Icon(
                                        imageVector = Icons.Outlined.Map,
                                        contentDescription = null
                                    )
                                }
                            }
                        )
                        DropdownMenu(
                            expanded = isDestinationMenuOpen,
                            onDismissRequest = { isDestinationMenuOpen = false }
                        ) {
                            DESTINATION_OPTIONS.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        isCustomDestination = option == "Custom"
                                        destination = if (isCustomDestination) "" else option
                                        isDestinationMenuOpen = false
                                    }
                                )
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            modifier = Modifier.weight(1f),
                            onClick = { isStartPickerOpen = true },
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(if (startDate.isBlank()) "Select start date" else "Start: $startDate")
                        }
                        OutlinedButton(
                            modifier = Modifier.weight(1f),
                            onClick = { isEndPickerOpen = true },
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(if (endDate.isBlank()) "Select end date" else "End: $endDate")
                        }
                    }
                    Text(
                        text = "Trip length: $lengthLabel day(s)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                tonalElevation = 3.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Daily plan",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                val computedDaysLocal = computeTripLengthDays(startDateMillis, endDateMillis)
                                if (isCustomDestination) {
                                    Toast.makeText(
                                        context,
                                        "Custom destination: add your stops manually.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else if (destination.isBlank() || computedDaysLocal == null) {
                                    Toast.makeText(
                                        context,
                                        "Select destination and valid dates to auto-plan.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else if (computedDaysLocal > 5) {
                                    Toast.makeText(
                                        context,
                                        "Auto-plan available for trips up to 5 days. Please trim dates or add stops manually.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    val sample = pickSamplePlan(destination, computedDaysLocal)
                                    applyTemplateToState(
                                        template = sample,
                                        activities = activities,
                                        tripLengthDays = computedDaysLocal,
                                        setTitle = { title = it },
                                        setDestination = { destination = it },
                                        setNotes = { notes = it }
                                    )
                                    tripLengthDays = computedDaysLocal
                                }
                            },
                            shape = RoundedCornerShape(16.dp),
                            enabled = autoPlanEnabled
                        ) {
                            Text("Auto-plan")
                        }
                        OutlinedButton(
                            onClick = {
                                activities.clear()
                                newActivity = ""
                            },
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text("Clear")
                        }
                    }
                    if (!autoPlanEnabled) {
                        Text(
                            text = "Auto-plan works for trips up to 5 days on preset destinations. Longer or custom trips: add stops manually.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = newActivity,
                        onValueChange = { newActivity = it },
                        label = { Text("Add stop or activity") },
                        placeholder = { Text("Morning market, museum, dinner...") },
                        singleLine = true,
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    val trimmed = newActivity.trim()
                                    if (trimmed.isNotEmpty()) {
                                        activities.add(trimmed)
                                        newActivity = ""
                                    }
                                }
                            ) {
                                Icon(imageVector = Icons.Outlined.Add, contentDescription = "Add activity")
                            }
                        }
                    )
                    if (activities.isEmpty()) {
                        Text(
                            text = "No stops yet. Add a few to sketch your day.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    } else {
                        val groupedActivities = groupActivitiesByDay(activities)
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            groupedActivities.forEach { group ->
                                DayPlanGroupCard(
                                    group = group,
                                    stateKeySuffix = "planner",
                                    onDeleteItem = { index -> activities.removeAt(index) }
                                )
                            }
                        }
                    }
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                tonalElevation = 3.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Notes",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Extra details, reminders, budgets...") },
                        maxLines = 6
                    )
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }

    if (isStartPickerOpen) {
        val startPickerState = rememberDatePickerState(
            initialSelectedDateMillis = startDateMillis.takeIf { it > 0 } ?: System.currentTimeMillis()
        )
        DatePickerDialog(
            onDismissRequest = { isStartPickerOpen = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        isStartPickerOpen = false
                        val selected = startPickerState.selectedDateMillis
                        if (selected != null) {
                            startDateMillis = selected
                            startDate = formatDate(selected)
                            val computed = computeTripLengthDays(startDateMillis, endDateMillis)
                            if (computed != null) {
                                tripLengthDays = computed
                            }
                        }
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { isStartPickerOpen = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = startPickerState)
        }
    }

    if (isEndPickerOpen) {
        val endPickerState = rememberDatePickerState(
            initialSelectedDateMillis = endDateMillis.takeIf { it > 0 } ?: System.currentTimeMillis()
        )
        DatePickerDialog(
            onDismissRequest = { isEndPickerOpen = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        isEndPickerOpen = false
                        val selected = endPickerState.selectedDateMillis
                        if (selected != null) {
                            endDateMillis = selected
                            endDate = formatDate(selected)
                            val computed = computeTripLengthDays(startDateMillis, endDateMillis)
                            if (computed != null) {
                                tripLengthDays = computed
                            }
                        }
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { isEndPickerOpen = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = endPickerState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedTripScreen(
    navController: NavHostController,
    savedTripPlan: TripPlan,
    onDeleteTripPlan: () -> Unit
) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        UnsupportedVersionScreen(
            title = "Saved Trip",
            message = "Trip Planner requires Android 8.0 or above.",
            onBack = { navController.popBackStack() }
        )
        return
    }
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Saved Trip") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (savedTripPlan.title.isBlank() && savedTripPlan.destination.isBlank()) {
                Text(
                    text = "No trip saved yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            } else {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = savedTripPlan.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Destination: ${savedTripPlan.destination}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Dates: ${savedTripPlan.startDate} - ${savedTripPlan.endDate}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        if (savedTripPlan.notes.isNotBlank()) {
                            Text(
                                text = savedTripPlan.notes,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                        }
                        val groupedActivities = groupActivitiesByDay(savedTripPlan.activities)
                        if (groupedActivities.isNotEmpty()) {
                            Text(
                                text = "Stops:",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                groupedActivities.forEach { group ->
                                    DayPlanGroupCard(
                                        group = group,
                                        stateKeySuffix = "saved",
                                        onDeleteItem = null
                                    )
                                }
                            }
                        }
                    }
                }

                OutlinedButton(
                    onClick = {
                        onDeleteTripPlan()
                        Toast.makeText(navController.context, "Saved trip cleared.", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Delete saved trip")
                }
            }
        }
    }
}

@Composable
fun DayPlanGroupCard(
    group: DayPlanGroup,
    stateKeySuffix: String,
    onDeleteItem: ((Int) -> Unit)?,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable(group.key, stateKeySuffix) { mutableStateOf(true) }
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        tonalElevation = 1.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = group.dayLabel,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    group.summary?.let { summary ->
                        Text(
                            text = summary,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                        )
                    }
                }
                if (onDeleteItem != null && group.headerIndex != null) {
                    IconButton(onClick = { onDeleteItem(group.headerIndex) }) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Remove day"
                        )
                    }
                }
                Icon(
                    imageVector = if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                    contentDescription = if (expanded) "Collapse day plan" else "Expand day plan"
                )
            }
            if (expanded) {
                if (group.items.isEmpty()) {
                    Text(
                        text = "No stops added for this day yet.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        group.items.forEach { item ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "• ${item.label}",
                                    modifier = Modifier.weight(1f),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                if (onDeleteItem != null) {
                                    IconButton(onClick = { onDeleteItem(item.index) }) {
                                        Icon(
                                            imageVector = Icons.Outlined.Delete,
                                            contentDescription = "Remove activity"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun computeTripLengthDays(startMillis: Long, endMillis: Long): Int? {
    if (startMillis <= 0 || endMillis <= 0) return null
    if (endMillis < startMillis) return null
    val days = ((endMillis - startMillis) / MILLIS_IN_DAY + 1).toInt()
    return days.coerceIn(1, 10)
}

private fun groupActivitiesByDay(activities: List<String>): List<DayPlanGroup> {
    if (activities.isEmpty()) return emptyList()
    val headerRegex = Regex("^Day\\s*(\\d+):?\\s*(.*)$", RegexOption.IGNORE_CASE)
    val groups = mutableListOf<DayPlanGroup>()
    var currentLabel: String? = null
    var currentSummary: String? = null
    var currentItems = mutableListOf<IndexedActivity>()
    var currentHeaderIndex: Int? = null

    fun appendGroup() {
        if (currentLabel == null) return
        val groupKey = "${groups.size}-${currentLabel}-${currentSummary.orEmpty()}"
        groups.add(
            DayPlanGroup(
                key = groupKey,
                dayLabel = currentLabel!!,
                summary = currentSummary,
                items = currentItems.toList(),
                headerIndex = currentHeaderIndex
            )
        )
    }

    activities.forEachIndexed { index, activity ->
        val match = headerRegex.matchEntire(activity)
        if (match != null) {
            appendGroup()
            val dayNumber = match.groupValues[1]
            currentLabel = "Day $dayNumber"
            currentSummary = match.groupValues.getOrNull(2)?.takeIf { it.isNotBlank() }
            currentItems = mutableListOf()
            currentHeaderIndex = index
        } else {
            if (currentLabel == null) {
                currentLabel = "Day 1"
            }
            currentItems.add(IndexedActivity(index = index, label = activity))
        }
    }

    appendGroup()

    return groups
}

private fun applyTemplateToState(
    template: SampleTripTemplate,
    activities: MutableList<String>,
    tripLengthDays: Int,
    setTitle: (String) -> Unit,
    setDestination: (String) -> Unit,
    setNotes: (String) -> Unit
) {
    setTitle(template.title)
    setDestination(template.destination)
    setNotes(template.notes)
    activities.clear()
    activities.addAll(template.dayPlans.take(tripLengthDays).flatMap { day ->
        listOf("Day ${day.day}: ${day.summary}") + day.items
    })
}

private val DESTINATION_OPTIONS = listOf("Kyoto", "Tokyo", "Paris", "New York", "Sydney", "Custom")

private val SAMPLE_TRIP_TEMPLATES: List<SampleTripTemplate> by lazy {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        listOf(
            SampleTripTemplate(
                destination = "Kyoto",
                title = "Kyoto Calm & Culture",
                suggestedStart = currentIsoDate(),
                notes = "Centered near Gion and Arashiyama. Add tea breaks and adjust times to your pace.",
                dayPlans = listOf(
                    SampleDayPlan(
                        day = 1,
                        summary = "Gion lanes and Kiyomizu views",
                        items = listOf(
                            "9:00 - Yasaka Shrine stroll",
                            "11:00 - Sannen-zaka + Kiyomizu-dera",
                            "13:00 - Nishiki Market lunch",
                            "15:00 - Tea break at Gion",
                            "18:00 - Pontocho dinner by the river"
                        )
                    ),
                    SampleDayPlan(
                        day = 2,
                        summary = "Arashiyama bamboo and river",
                        items = listOf(
                            "7:30 - Arashiyama Bamboo Grove",
                            "9:30 - Tenryu-ji garden",
                            "12:00 - River bento picnic",
                            "14:00 - Sagano Scenic Train",
                            "17:30 - Nishijin textile stop"
                        )
                    ),
                    SampleDayPlan(
                        day = 3,
                        summary = "Northern temples and tea",
                        items = listOf(
                            "9:00 - Kinkaku-ji (Golden Pavilion)",
                            "11:30 - Ryoan-ji rock garden",
                            "14:00 - Philosopher's Path walk",
                            "16:00 - Ginkaku-ji",
                            "18:30 - Kaiseki dinner"
                        )
                    ),
                    SampleDayPlan(
                        day = 4,
                        summary = "Nara side trip",
                        items = listOf(
                            "9:00 - Todai-ji & Nara Park",
                            "12:00 - Naramachi lunch",
                            "14:00 - Kasuga Taisha",
                            "17:30 - Return to Kyoto, stroll Kamogawa",
                            "19:30 - Izakaya near Pontocho"
                        )
                    ),
                    SampleDayPlan(
                        day = 5,
                        summary = "Tea + Fushimi",
                        items = listOf(
                            "8:00 - Fushimi Inari early climb",
                            "11:30 - Sake brewery tasting",
                            "14:00 - Uji tea ceremony",
                            "17:00 - Teramachi shopping",
                            "19:30 - Casual ramen night"
                        )
                    )
                )
            ),
            SampleTripTemplate(
                destination = "Tokyo",
                title = "Tokyo Contrasts",
                suggestedStart = currentIsoDate(),
                notes = "Use Suica/Passmo; mix classic spots with modern stops.",
                dayPlans = listOf(
                    SampleDayPlan(
                        day = 1,
                        summary = "Asakusa to Skytree",
                        items = listOf(
                            "9:00 - Senso-ji & Nakamise",
                            "12:00 - Tempura lunch",
                            "14:00 - Sumida River walk",
                            "16:00 - Tokyo Skytree view",
                            "19:00 - Solamachi dinner"
                        )
                    ),
                    SampleDayPlan(
                        day = 2,
                        summary = "Shibuya + Shinjuku night",
                        items = listOf(
                            "9:30 - Meiji Shrine",
                            "11:00 - Harajuku/Omotesando stroll",
                            "14:00 - Shibuya Sky",
                            "17:00 - Shinjuku Gyoen",
                            "20:00 - Omoide Yokocho dinner"
                        )
                    ),
                    SampleDayPlan(
                        day = 3,
                        summary = "Odaiba + teamLab",
                        items = listOf(
                            "10:00 - teamLab Planets",
                            "12:30 - Odaiba lunch",
                            "15:00 - Palette Town/Seaside Park",
                            "18:30 - Rainbow Bridge sunset",
                            "20:00 - Ginza night walk"
                        )
                    ),
                    SampleDayPlan(
                        day = 4,
                        summary = "Ueno + Yanaka",
                        items = listOf(
                            "9:30 - Ueno Park museums",
                            "12:30 - Ameya-Yokocho street bites",
                            "14:30 - Yanaka Ginza stroll",
                            "17:30 - Nezu Shrine",
                            "20:00 - Craft beer in Kanda"
                        )
                    ),
                    SampleDayPlan(
                        day = 5,
                        summary = "Kamakura or Enoshima escape",
                        items = listOf(
                            "9:00 - Train to Kamakura",
                            "10:00 - Hasedera + Great Buddha",
                            "12:30 - Komachi-dori lunch",
                            "15:00 - Enoshima sunset walk",
                            "19:30 - Return to Tokyo, late sushi"
                        )
                    )
                )
            ),
            SampleTripTemplate(
                destination = "Paris",
                title = "Paris Highlights",
                suggestedStart = currentIsoDate(),
                notes = "Stay central (1st/4th). Mix of classics and walks; swap museums if needed.",
                dayPlans = listOf(
                    SampleDayPlan(
                        day = 1,
                        summary = "Seine + Louvre",
                        items = listOf(
                            "9:00 - Louvre entry",
                            "12:30 - Tuileries picnic",
                            "14:00 - Palais Royal arcades",
                            "16:00 - Seine cruise",
                            "19:00 - Le Marais dinner"
                        )
                    ),
                    SampleDayPlan(
                        day = 2,
                        summary = "Left Bank arts",
                        items = listOf(
                            "9:30 - Musée d'Orsay",
                            "12:30 - Saint-Germain lunch",
                            "15:00 - Luxembourg Gardens",
                            "17:00 - Shakespeare & Co.",
                            "20:00 - Sunset by Notre-Dame"
                        )
                    ),
                    SampleDayPlan(
                        day = 3,
                        summary = "Montmartre + Eiffel finale",
                        items = listOf(
                            "9:00 - Sacré-Cœur + Place du Tertre",
                            "12:00 - Montmartre bistro",
                            "15:00 - Champs-Élysées stroll",
                            "17:00 - Trocadéro view",
                            "20:30 - Eiffel Tower night lights"
                        )
                    ),
                    SampleDayPlan(
                        day = 4,
                        summary = "Versailles detour",
                        items = listOf(
                            "9:30 - Train to Versailles",
                            "10:30 - Palace tour",
                            "13:00 - Gardens picnic",
                            "16:00 - Hall of Mirrors return",
                            "19:30 - Back to Paris, bistro in 7th"
                        )
                    ),
                    SampleDayPlan(
                        day = 5,
                        summary = "Canal & local life",
                        items = listOf(
                            "9:30 - Canal Saint-Martin walk",
                            "12:00 - Marché des Enfants Rouges lunch",
                            "14:30 - Picasso Museum",
                            "17:00 - Le Marais boutiques",
                            "20:00 - Oberkampf wine bar"
                        )
                    )
                )
            ),
            SampleTripTemplate(
                destination = "New York",
                title = "NYC Essentials",
                suggestedStart = currentIsoDate(),
                notes = "Subway friendly; layer up for rooftops at night.",
                dayPlans = listOf(
                    SampleDayPlan(
                        day = 1,
                        summary = "Midtown icons",
                        items = listOf(
                            "9:00 - Grand Central + Chrysler peek",
                            "10:30 - Top of the Rock view",
                            "12:30 - Bryant Park lunch",
                            "15:00 - Fifth Ave walk",
                            "19:00 - Times Square lights"
                        )
                    ),
                    SampleDayPlan(
                        day = 2,
                        summary = "Downtown + Brooklyn",
                        items = listOf(
                            "9:30 - 9/11 Memorial",
                            "11:00 - Oculus + Wall Street",
                            "13:00 - Staten Island Ferry skyline",
                            "16:00 - Walk Brooklyn Bridge",
                            "19:00 - Dumbo pizza + sunset"
                        )
                    ),
                    SampleDayPlan(
                        day = 3,
                        summary = "Central Park + museums",
                        items = listOf(
                            "9:30 - Central Park loop (Bow Bridge, Bethesda)",
                            "12:30 - Upper West Side lunch",
                            "14:00 - American Museum of Natural History",
                            "17:00 - Riverside sunset",
                            "20:00 - Hell's Kitchen dinner"
                        )
                    ),
                    SampleDayPlan(
                        day = 4,
                        summary = "Art and skyline",
                        items = listOf(
                            "10:00 - The Met",
                            "13:00 - Madison Ave coffee",
                            "15:00 - High Line walk",
                            "17:30 - Hudson Yards (Vessel view)",
                            "20:00 - Rooftop cocktail"
                        )
                    ),
                    SampleDayPlan(
                        day = 5,
                        summary = "Brooklyn day",
                        items = listOf(
                            "9:30 - Williamsburg coffee crawl",
                            "12:00 - Smorgasburg/food market",
                            "14:30 - Bushwick street art",
                            "17:30 - Domino Park sunset",
                            "20:00 - Brooklyn brewery stop"
                        )
                    )
                )
            ),
            SampleTripTemplate(
                destination = "Sydney",
                title = "Sydney Coastal Loop",
                suggestedStart = currentIsoDate(),
                notes = "Opal card ready; adjust swim spots to weather.",
                dayPlans = listOf(
                    SampleDayPlan(
                        day = 1,
                        summary = "Harbour icons",
                        items = listOf(
                            "9:00 - Opera House + Royal Botanic Garden",
                            "12:30 - Circular Quay lunch",
                            "15:00 - Ferry to Manly Beach",
                            "18:30 - Rocks dinner"
                        )
                    ),
                    SampleDayPlan(
                        day = 2,
                        summary = "Coastal walk day",
                        items = listOf(
                            "8:30 - Bondi to Coogee walk",
                            "12:30 - Coogee lunch",
                            "15:00 - Icebergs dip",
                            "19:00 - Surry Hills dinner"
                        )
                    ),
                    SampleDayPlan(
                        day = 3,
                        summary = "Culture and wildlife",
                        items = listOf(
                            "9:30 - Art Gallery of NSW",
                            "12:00 - Barangaroo lunch",
                            "14:30 - Taronga Zoo or SEA LIFE",
                            "19:00 - Darling Harbour evening"
                        )
                    ),
                    SampleDayPlan(
                        day = 4,
                        summary = "Blue Mountains day",
                        items = listOf(
                            "8:00 - Train to Katoomba",
                            "10:00 - Three Sisters lookout",
                            "12:30 - Leura lunch",
                            "14:00 - Scenic World boardwalk",
                            "18:30 - Return to Sydney"
                        )
                    ),
                    SampleDayPlan(
                        day = 5,
                        summary = "Markets + neighborhoods",
                        items = listOf(
                            "9:00 - Paddington Markets",
                            "12:00 - Brunch at Surry Hills",
                            "14:30 - Newtown vintage stroll",
                            "17:30 - Glebe foreshore walk",
                            "20:00 - Chinatown night eats"
                        )
                    )
                )
            )
        )
    } else {
        emptyList()
    }
}

private fun pickSamplePlan(destination: String, days: Int): SampleTripTemplate {
    if (SAMPLE_TRIP_TEMPLATES.isEmpty()) {
        return SampleTripTemplate(
            destination = destination.ifBlank { DESTINATION_OPTIONS.first() },
            title = "Trip Plan",
            suggestedStart = "",
            notes = "",
            dayPlans = emptyList()
        )
    }
    val normalized = destination.ifBlank { DESTINATION_OPTIONS.first() }
    val match = SAMPLE_TRIP_TEMPLATES.firstOrNull { it.destination.equals(normalized, ignoreCase = true) }
    return match ?: SAMPLE_TRIP_TEMPLATES.first()
}
