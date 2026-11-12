package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.*
import com.example.ui.theme.*

@Composable
fun SpeciesPickerDialog(
    currentSpecies: FishSpecies,
    onSpeciesSelected: (FishSpecies) -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    val categories = remember {
        listOf("All") + SpeciesCatalog.allSpecies.map { it.category }.distinct()
    }

    val filteredList = remember(searchQuery, selectedCategory) {
        SpeciesCatalog.allSpecies.filter { species ->
            val matchesCategory = selectedCategory == "All" || species.category == selectedCategory
            val matchesSearch = searchQuery.isBlank() ||
                    species.commonName.contains(searchQuery, ignoreCase = true) ||
                    species.scientificName.contains(searchQuery, ignoreCase = true) ||
                    species.category.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MarineSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, HydroCyan),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "SELECT TARGET SPECIES",
                            color = HydroCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Any Freshwater or Saltwater Species",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search any species (e.g. Walleye, Snook, Trout...)", color = TextMuted, fontSize = 12.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MarineSurfaceVariant,
                        unfocusedContainerColor = MarineSurfaceVariant,
                        focusedBorderColor = HydroCyan,
                        unfocusedBorderColor = MarineOutline,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.forEach { cat ->
                        val isSelected = cat == selectedCategory
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = HydroCyan,
                                selectedLabelColor = MarineDeep,
                                containerColor = MarineSurfaceVariant,
                                labelColor = TextSecondary
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(filteredList) { species ->
                        val isCurrent = species.id == currentSpecies.id
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSpeciesSelected(species)
                                },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isCurrent) MarineSurfaceVariant else Color(0xFF091820),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isCurrent) HydroCyan else MarineOutlineSubtle
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = species.commonName,
                                            color = TextPrimary,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "${species.scientificName} • ${species.category}",
                                            color = TextSecondary,
                                            fontSize = 10.sp
                                        )
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = MarineSurfaceVariant
                                    ) {
                                        Text(
                                            text = "${species.minOptimalTempF}-${species.maxOptimalTempF}°F",
                                            color = StrikeAmber,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = species.biologicalFact,
                                    color = TextMuted,
                                    fontSize = 10.sp,
                                    maxLines = 2,
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConditionsEditorDialog(
    initialConditions: EnvironmentalConditions,
    onSave: (EnvironmentalConditions) -> Unit,
    onDismiss: () -> Unit
) {
    var waterTemp by remember { mutableStateOf(initialConditions.waterTempF.toFloat()) }
    var barometer by remember { mutableStateOf(initialConditions.barometerInHg.toFloat()) }
    var selectedTrend by remember { mutableStateOf(initialConditions.pressureTrend) }
    var selectedClarity by remember { mutableStateOf(initialConditions.waterClarity) }
    var selectedTime by remember { mutableStateOf(initialConditions.timeOfDay) }
    var selectedSeason by remember { mutableStateOf(initialConditions.season) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MarineSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, StrikeAmber),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "LIVE ENVIRONMENTAL TELEMETRY",
                            color = StrikeAmber,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Adjust Water & Weather",
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Water Temp Slider
                    item {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Water Temperature", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text("${waterTemp.toInt()}°F", color = HydroCyan, fontSize = 14.sp, fontWeight = FontWeight.Black)
                            }
                            Slider(
                                value = waterTemp,
                                onValueChange = { waterTemp = it },
                                valueRange = 36f..90f,
                                colors = SliderDefaults.colors(thumbColor = HydroCyan, activeTrackColor = HydroCyan)
                            )
                        }
                    }

                    // Barometer Slider & Trend
                    item {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Barometric Pressure", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text(String.format(java.util.Locale.US, "%.2f inHg", barometer), color = StrikeAmber, fontSize = 14.sp, fontWeight = FontWeight.Black)
                            }
                            Slider(
                                value = barometer,
                                onValueChange = { barometer = it },
                                valueRange = 29.60f..30.40f,
                                colors = SliderDefaults.colors(thumbColor = StrikeAmber, activeTrackColor = StrikeAmber)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Barometric Trend:", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                PressureTrend.values().forEach { trend ->
                                    val isSelected = trend == selectedTrend
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { selectedTrend = trend },
                                        label = { Text(trend.label, fontSize = 11.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = StrikeAmber,
                                            selectedLabelColor = MarineDeep,
                                            containerColor = MarineSurfaceVariant,
                                            labelColor = TextSecondary
                                        ),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Water Clarity Selection
                    item {
                        Column {
                            Text("Water Clarity", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                WaterClarity.values().forEach { clarity ->
                                    val isSelected = clarity == selectedClarity
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { selectedClarity = clarity },
                                        label = { Text(clarity.label, fontSize = 11.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = HydroCyan,
                                            selectedLabelColor = MarineDeep,
                                            containerColor = MarineSurfaceVariant,
                                            labelColor = TextSecondary
                                        ),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Time of Day
                    item {
                        Column {
                            Text("Time of Day", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                TimeOfDay.values().forEach { time ->
                                    val isSelected = time == selectedTime
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { selectedTime = time },
                                        label = { Text(time.label, fontSize = 11.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = StrikeAmber,
                                            selectedLabelColor = MarineDeep,
                                            containerColor = MarineSurfaceVariant,
                                            labelColor = TextSecondary
                                        ),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Season
                    item {
                        Column {
                            Text("Season Pattern", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Season.values().forEach { s ->
                                    val isSelected = s == selectedSeason
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { selectedSeason = s },
                                        label = { Text(s.label, fontSize = 11.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = HydroCyan,
                                            selectedLabelColor = MarineDeep,
                                            containerColor = MarineSurfaceVariant,
                                            labelColor = TextSecondary
                                        ),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        val roundedBarometer = Math.round(barometer * 100.0) / 100.0
                        onSave(
                            initialConditions.copy(
                                waterTempF = waterTemp.toInt(),
                                barometerInHg = roundedBarometer,
                                pressureTrend = selectedTrend,
                                waterClarity = selectedClarity,
                                timeOfDay = selectedTime,
                                season = selectedSeason
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = StrikeAmber, contentColor = MarineDeep),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Apply Live Conditions", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun AddTackleDialog(
    onAdd: (name: String, category: String, brand: String, color: String, weight: String, species: String, clarity: String, depth: String, style: String, notes: String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Soft Plastics") }
    var brand by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("3/8 oz") }
    var targetSpecies by remember { mutableStateOf("Bass, Walleye, Pike") }
    var bestClarity by remember { mutableStateOf("Clear & Stained") }
    var depthRange by remember { mutableStateOf("3 - 8 ft") }
    var style by remember { mutableStateOf("Reaction / Power Cast") }
    var notes by remember { mutableStateOf("") }

    val categories = listOf("Soft Plastics", "Crankbaits & Plugs", "Jigs & Skirts", "Topwater", "Wire & Spinners", "Spoons & Blades", "Flies & Micro", "Terminal Rigs")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MarineSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, HydroCyan),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "DIGITAL TACKLE BOX",
                            color = HydroCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Add Lure or Gear Item",
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Lure Name (e.g. KVD 1.5 Squarebill)") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = HydroCyan,
                                unfocusedBorderColor = MarineOutline,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                    }

                    item {
                        Text("Category", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            categories.forEach { cat ->
                                val isSelected = cat == category
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { category = cat },
                                    label = { Text(cat, fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = HydroCyan,
                                        selectedLabelColor = MarineDeep,
                                        containerColor = MarineSurfaceVariant,
                                        labelColor = TextSecondary
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = brand,
                                onValueChange = { brand = it },
                                label = { Text("Brand") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = HydroCyan,
                                    unfocusedBorderColor = MarineOutline,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                )
                            )
                            OutlinedTextField(
                                value = weight,
                                onValueChange = { weight = it },
                                label = { Text("Weight") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = HydroCyan,
                                    unfocusedBorderColor = MarineOutline,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                )
                            )
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = color,
                                onValueChange = { color = it },
                                label = { Text("Color / Pattern") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = HydroCyan,
                                    unfocusedBorderColor = MarineOutline,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                )
                            )
                            OutlinedTextField(
                                value = depthRange,
                                onValueChange = { depthRange = it },
                                label = { Text("Depth Range") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = HydroCyan,
                                    unfocusedBorderColor = MarineOutline,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                )
                            )
                        }
                    }

                    item {
                        OutlinedTextField(
                            value = targetSpecies,
                            onValueChange = { targetSpecies = it },
                            label = { Text("Target Species (comma separated)") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = HydroCyan,
                                unfocusedBorderColor = MarineOutline,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = notes,
                            onValueChange = { notes = it },
                            label = { Text("Tactical Notes / Mod Rigs") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = HydroCyan,
                                unfocusedBorderColor = MarineOutline,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (name.isNotBlank()) {
                            onAdd(
                                name,
                                category,
                                if (brand.isBlank()) "Generic" else brand,
                                if (color.isBlank()) "Natural" else color,
                                weight,
                                targetSpecies,
                                bestClarity,
                                depthRange,
                                style,
                                notes
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = HydroCyan, contentColor = MarineDeep),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save to My Tackle Box", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun LogCatchDialog(
    currentSpeciesName: String,
    currentWaterName: String,
    currentTemp: Int,
    currentBarometer: Double,
    onSaveCatch: (species: String, weight: Float, length: Float, lure: String, color: String, water: String, notes: String) -> Unit,
    onDismiss: () -> Unit
) {
    var species by remember { mutableStateOf(currentSpeciesName) }
    var weightText by remember { mutableStateOf("3.5") }
    var lengthText by remember { mutableStateOf("18.0") }
    var lure by remember { mutableStateOf("ChatterBait Bladed Jig") }
    var color by remember { mutableStateOf("Black / Blue") }
    var notes by remember { mutableStateOf("Caught on outside grass weedline during falling barometer.") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MarineSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, CatchGreen),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "CATCH LOGGING INTELLIGENCE",
                            color = CatchGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Record Fish Telemetry",
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MarineSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Auto-Filled: $currentTemp°F • $currentBarometer inHg",
                            color = StrikeAmber,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = currentWaterName,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        OutlinedTextField(
                            value = species,
                            onValueChange = { species = it },
                            label = { Text("Species Name") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CatchGreen,
                                unfocusedBorderColor = MarineOutline,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = weightText,
                                onValueChange = { weightText = it },
                                label = { Text("Weight (lbs)") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = CatchGreen,
                                    unfocusedBorderColor = MarineOutline,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                )
                            )
                            OutlinedTextField(
                                value = lengthText,
                                onValueChange = { lengthText = it },
                                label = { Text("Length (inches)") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = CatchGreen,
                                    unfocusedBorderColor = MarineOutline,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                )
                            )
                        }
                    }

                    item {
                        OutlinedTextField(
                            value = lure,
                            onValueChange = { lure = it },
                            label = { Text("Lure or Rig Used") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CatchGreen,
                                unfocusedBorderColor = MarineOutline,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = color,
                            onValueChange = { color = it },
                            label = { Text("Lure Color Pattern") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CatchGreen,
                                unfocusedBorderColor = MarineOutline,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = notes,
                            onValueChange = { notes = it },
                            label = { Text("Notes (Presentation, Cover, Strike)") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CatchGreen,
                                unfocusedBorderColor = MarineOutline,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val w = weightText.toFloatOrNull() ?: 1.0f
                        val l = lengthText.toFloatOrNull() ?: 12.0f
                        onSaveCatch(species, w, l, lure, color, currentWaterName, notes)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = CatchGreen, contentColor = MarineDeep),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save to Catch Log", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun CreatePlanDialog(
    currentWaterName: String,
    currentSpeciesName: String,
    onSave: (title: String, water: String, species: String, date: String, planA: String, planB: String, planC: String, rigging: String, window: String, notes: String) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("Weekend Tactical Outing") }
    var water by remember { mutableStateOf(currentWaterName) }
    var species by remember { mutableStateOf(currentSpeciesName) }
    var date by remember { mutableStateOf("Saturday Morning") }
    var planA by remember { mutableStateOf("Squarebill deflecting off 4-6ft riprap and dock pilings.") }
    var planB by remember { mutableStateOf("Wacky-rigged Senko skipped under shaded walkways if sun gets high.") }
    var planC by remember { mutableStateOf("Drop shotting 4.5\" straight tail worm on 15ft ledge transition.") }
    var rigging by remember { mutableStateOf("7'0\" MH Casting (15lb fluoro), 7'0\" Medium Spinning (8lb braid to 6lb leader)") }
    var window by remember { mutableStateOf("6:45 AM - 9:15 AM (Solunar Major Peak)") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MarineSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, StrikeAmber),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "GAME PLAN STRATEGY BUILDER",
                            color = StrikeAmber,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Create Strategic Formation",
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Game Plan Title") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = StrikeAmber,
                                unfocusedBorderColor = MarineOutline,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = planA,
                            onValueChange = { planA = it },
                            label = { Text("Plan A (Primary Reaction Strike)") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = HydroCyan,
                                unfocusedBorderColor = MarineOutline,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = planB,
                            onValueChange = { planB = it },
                            label = { Text("Plan B (Finesse Pivot)") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = StrikeAmber,
                                unfocusedBorderColor = MarineOutline,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = planC,
                            onValueChange = { planC = it },
                            label = { Text("Plan C (Emergency Lockjaw Failsafe)") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = WarningRed,
                                unfocusedBorderColor = MarineOutline,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = rigging,
                            onValueChange = { rigging = it },
                            label = { Text("Rigging & Rod Setup") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = StrikeAmber,
                                unfocusedBorderColor = MarineOutline,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = window,
                            onValueChange = { window = it },
                            label = { Text("Targeted Solunar Feeding Window") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = StrikeAmber,
                                unfocusedBorderColor = MarineOutline,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            onSave(title, water, species, date, planA, planB, planC, rigging, window, "")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = StrikeAmber, contentColor = MarineDeep),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save Game Plan", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun AddWaterDialog(
    onAddWater: (name: String, location: String, category: WaterCategory, surfaceArea: String, maxDepth: Int, tempF: Int, clarity: WaterClarity, speciesIds: List<String>, structures: List<String>, access: String, description: String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(WaterCategory.URBAN_POND) }
    var maxDepthText by remember { mutableStateOf("15") }
    var surfaceArea by remember { mutableStateOf("20 Acres") }
    var access by remember { mutableStateOf("100% Walkable Bank Access") }
    var description by remember { mutableStateOf("Local community body of water with bank access and shoreline weedlines.") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MarineSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, HydroCyan),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "WATER RECONNAISSANCE",
                            color = HydroCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Add Local Fishing Water",
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Water Name (e.g. Pinewood Pond)") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = HydroCyan,
                                unfocusedBorderColor = MarineOutline,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = location,
                            onValueChange = { location = it },
                            label = { Text("City, State / Region") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = HydroCyan,
                                unfocusedBorderColor = MarineOutline,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                    }

                    item {
                        Text("Water Type", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            WaterCategory.values().forEach { cat ->
                                val isSelected = cat == category
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { category = cat },
                                    label = { Text(cat.label, fontSize = 11.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = HydroCyan,
                                        selectedLabelColor = MarineDeep,
                                        containerColor = MarineSurfaceVariant,
                                        labelColor = TextSecondary
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = maxDepthText,
                                onValueChange = { maxDepthText = it },
                                label = { Text("Max Depth (ft)") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = HydroCyan,
                                    unfocusedBorderColor = MarineOutline,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                )
                            )
                            OutlinedTextField(
                                value = surfaceArea,
                                onValueChange = { surfaceArea = it },
                                label = { Text("Surface Area") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = HydroCyan,
                                    unfocusedBorderColor = MarineOutline,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                )
                            )
                        }
                    }

                    item {
                        OutlinedTextField(
                            value = access,
                            onValueChange = { access = it },
                            label = { Text("Shoreline / Boat Access") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = HydroCyan,
                                unfocusedBorderColor = MarineOutline,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Description & Structure") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = HydroCyan,
                                unfocusedBorderColor = MarineOutline,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (name.isNotBlank()) {
                            val depth = maxDepthText.toIntOrNull() ?: 15
                            onAddWater(
                                name,
                                location,
                                category,
                                surfaceArea,
                                depth,
                                68,
                                WaterClarity.STAINED,
                                listOf("largemouth_bass", "bluegill", "channel_catfish"),
                                listOf("Weedlines", "Fallen Trees", "Bank Drops"),
                                access,
                                description
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = HydroCyan, contentColor = MarineDeep),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save Water Body", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}
