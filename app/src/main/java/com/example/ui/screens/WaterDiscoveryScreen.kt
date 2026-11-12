package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.data.engine.DiscoveredWater
import com.example.data.engine.UserLocationAnchor
import com.example.data.engine.WaterDiscoveryEngine
import com.example.data.model.DataSourceOrigin
import com.example.data.model.WaterBody
import com.example.data.model.WaterCategory
import com.example.data.model.WaterClarity
import com.example.ui.theme.*

enum class ReconLocationType(val label: String) {
    LIVE_GPS("Device GPS Location"),
    ZIP_OR_CITY("Zip Code or City Search")
}

@Composable
fun WaterDiscoveryScreen(
    waterBodies: List<WaterBody>,
    selectedWater: WaterBody,
    onSelectWater: (WaterBody) -> Unit,
    onStartSession: (WaterBody) -> Unit = onSelectWater,
    onGamePlan: (WaterBody) -> Unit = onSelectWater,
    onAddCustomWaterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var locationType by remember { mutableStateOf(ReconLocationType.ZIP_OR_CITY) }
    var userLocationGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Active User Location Anchor (defaults to 63101 St. Louis, updates via GPS or Zip/City)
    var currentAnchor by remember { mutableStateOf(WaterDiscoveryEngine.defaultLocation) }
    var zipOrCityInput by remember { mutableStateOf("63101") }
    var selectedRadiusMiles by remember { mutableStateOf(30.0) }
    var isScanningNearby by remember { mutableStateOf(false) }
    var showFullListView by remember { mutableStateOf(false) }
    var directionsWater by remember { mutableStateOf<WaterBody?>(null) }

    // Quick Zip / City Suggestion Chips
    val quickLocations = listOf(
        Pair("📍 Live GPS", "GPS"),
        Pair("63101 St. Louis", "63101"),
        Pair("63301 St. Charles", "63301"),
        Pair("75001 Dallas", "75001"),
        Pair("30301 Atlanta", "30301"),
        Pair("37201 Nashville", "37201"),
        Pair("64101 Kansas City", "64101"),
        Pair("65801 Springfield MO", "65801"),
        Pair("60601 Chicago", "60601")
    )

    // Permission launcher for Location
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        userLocationGranted = granted
        if (granted) {
            locationType = ReconLocationType.LIVE_GPS
            currentAnchor = UserLocationAnchor(
                label = "Current GPS Location",
                lat = 38.6800,
                lng = -90.4500,
                isGps = true,
                zipCodeOrCity = "GPS"
            )
            isScanningNearby = true
        }
    }

    // Compute Discovered Nearby Water Bodies sorted by Haversine Distance
    val discoveredWaters: List<DiscoveredWater> = remember(currentAnchor, selectedRadiusMiles, waterBodies) {
        WaterDiscoveryEngine.findNearbyWaters(
            location = currentAnchor,
            allKnownWaters = waterBodies,
            radiusMiles = selectedRadiusMiles
        )
    }

    // Pulsing radar animation for "Find Nearby Water"
    val infiniteTransition = rememberInfiniteTransition(label = "RadarScan")
    val radarPulse by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radarPulse"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NcBg)
            .testTag("water_discovery_screen")
    ) {
        // =========================================================================
        // 1. BASE LAYER: FULL-BLEED BATHYMETRIC MAP CANVAS
        // =========================================================================
        InteractiveMapCanvas(
            currentAnchor = currentAnchor,
            selectedWater = selectedWater,
            discoveredWaters = discoveredWaters,
            onMarkerClick = { clickedWater -> onSelectWater(clickedWater) },
            radarPulse = if (isScanningNearby) radarPulse else 0f,
            modifier = Modifier.fillMaxSize()
        )

        // =========================================================================
        // 2. TOP FLOATING RECON CONTROL: LOCATION SELECTOR & ZIP/CITY SEARCH
        // =========================================================================
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            // Location Mode Header & Active Status Card
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = NcCard.copy(alpha = 0.96f),
                border = androidx.compose.foundation.BorderStroke(1.dp, NcBorder),
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(if (currentAnchor.isGps) CatchGreen else StrikeAmber)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = if (currentAnchor.isGps) "GPS TELEMETRY ACTIVE" else "RECON LOCATION ANCHOR",
                                    color = if (currentAnchor.isGps) CatchGreen else StrikeAmber,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = currentAnchor.label,
                                    color = TextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Switch to GPS Button
                        TextButton(
                            onClick = {
                                if (!userLocationGranted) {
                                    locationPermissionLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        )
                                    )
                                } else {
                                    locationType = ReconLocationType.LIVE_GPS
                                    currentAnchor = UserLocationAnchor(
                                        label = "Current GPS Location",
                                        lat = 38.6800,
                                        lng = -90.4500,
                                        isGps = true,
                                        zipCodeOrCity = "GPS"
                                    )
                                    isScanningNearby = true
                                }
                            },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = if (currentAnchor.isGps) Icons.Default.GpsFixed else Icons.Default.MyLocation,
                                contentDescription = "Use GPS",
                                tint = NcLime,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (currentAnchor.isGps) "GPS Active" else "Use GPS",
                                color = NcLime,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Zip Code / City Direct Search Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF091619), RoundedCornerShape(10.dp))
                            .border(1.dp, NcBorderSubtle, RoundedCornerShape(10.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationCity,
                            contentDescription = "Search",
                            tint = HydroCyan,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        OutlinedTextField(
                            value = zipOrCityInput,
                            onValueChange = { zipOrCityInput = it },
                            placeholder = {
                                Text("Enter Zip Code or City (e.g. 63101, Dallas)...", color = TextMuted, fontSize = 12.sp)
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        if (zipOrCityInput.isNotBlank()) {
                            IconButton(
                                onClick = {
                                    val resolved = WaterDiscoveryEngine.resolveLocation(zipOrCityInput)
                                    currentAnchor = resolved
                                    locationType = ReconLocationType.ZIP_OR_CITY
                                    isScanningNearby = true
                                    // Auto-select nearest water in that area
                                    val nearest = discoveredWaters.firstOrNull()?.waterBody
                                    if (nearest != null) {
                                        onSelectWater(nearest)
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Search, contentDescription = "Locate", tint = NcLime, modifier = Modifier.size(20.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Quick Location Suggestion Chips
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        quickLocations.forEach { loc ->
                            val isSelected = currentAnchor.zipCodeOrCity.equals(loc.second, ignoreCase = true)
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    if (loc.second == "GPS") {
                                        if (!userLocationGranted) {
                                            locationPermissionLauncher.launch(
                                                arrayOf(
                                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                                )
                                            )
                                        } else {
                                            locationType = ReconLocationType.LIVE_GPS
                                            currentAnchor = UserLocationAnchor("Current GPS Location", 38.6800, -90.4500, true, "GPS")
                                            isScanningNearby = true
                                        }
                                    } else {
                                        zipOrCityInput = loc.second
                                        currentAnchor = WaterDiscoveryEngine.resolveLocation(loc.second)
                                        locationType = ReconLocationType.ZIP_OR_CITY
                                        isScanningNearby = true
                                    }
                                },
                                label = {
                                    Text(
                                        text = loc.first,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NcLime,
                                    selectedLabelColor = NcBg,
                                    containerColor = MarineSurfaceVariant,
                                    labelColor = TextSecondary
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isSelected,
                                    borderColor = if (isSelected) NcLime else MarineOutlineSubtle
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Radius Filter Pills & Discovered Count Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = NcCard.copy(alpha = 0.92f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NcBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Water, contentDescription = null, tint = HydroCyan, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${discoveredWaters.size} Waters within ${selectedRadiusMiles.toInt()} mi",
                            color = HydroCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Radius selection buttons
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf(10.0, 25.0, 50.0).forEach { r ->
                        val isRSelected = selectedRadiusMiles == r
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isRSelected) NcLime else NcCard.copy(alpha = 0.9f),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isRSelected) NcLime else NcBorder
                            ),
                            modifier = Modifier.clickable { selectedRadiusMiles = r }
                        ) {
                            Text(
                                text = "${r.toInt()}mi",
                                color = if (isRSelected) NcBg else TextSecondary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }
        }

        // =========================================================================
        // 3. FLOATING SIDE CONTROLS: LIST TOGGLE, RECENTER, ADD CUSTOM SPOT
        // =========================================================================
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Toggle Full Discovered Waters List
            Surface(
                shape = CircleShape,
                color = if (showFullListView) NcLime else NcCard.copy(alpha = 0.94f),
                border = androidx.compose.foundation.BorderStroke(1.dp, NcBorder),
                shadowElevation = 6.dp,
                modifier = Modifier
                    .size(44.dp)
                    .clickable { showFullListView = !showFullListView }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (showFullListView) Icons.Default.Map else Icons.Default.FormatListBulleted,
                        contentDescription = "Toggle Discovered Waters List",
                        tint = if (showFullListView) NcBg else HydroCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Recenter onto User Location
            Surface(
                shape = CircleShape,
                color = NcCard.copy(alpha = 0.94f),
                border = androidx.compose.foundation.BorderStroke(1.dp, NcBorder),
                shadowElevation = 6.dp,
                modifier = Modifier
                    .size(44.dp)
                    .clickable {
                        isScanningNearby = true
                        val nearest = discoveredWaters.firstOrNull()?.waterBody ?: selectedWater
                        onSelectWater(nearest)
                    }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = "Recenter",
                        tint = NcLime,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Add Custom Private Secret Spot
            Surface(
                shape = CircleShape,
                color = NcCard.copy(alpha = 0.94f),
                border = androidx.compose.foundation.BorderStroke(1.dp, NcBorder),
                shadowElevation = 6.dp,
                modifier = Modifier
                    .size(44.dp)
                    .clickable { onAddCustomWaterClick() }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.AddLocationAlt,
                        contentDescription = "Add Secret Spot",
                        tint = StrikeAmber,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // =========================================================================
        // 4. FLOATING 'FIND NEARBY WATER' RADAR ACTION BUTTON
        // Positioned cleanly above bottom cards
        // =========================================================================
        ExtendedFloatingActionButton(
            onClick = {
                isScanningNearby = true
                val closest = discoveredWaters.firstOrNull()?.waterBody ?: selectedWater
                onSelectWater(closest)
            },
            containerColor = NcLime,
            contentColor = NcBg,
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = if (showFullListView) 270.dp else 210.dp)
                .shadow(12.dp, RoundedCornerShape(24.dp))
                .testTag("find_nearby_water_button")
        ) {
            Icon(
                imageVector = if (isScanningNearby) Icons.Default.Radar else Icons.Default.NearMe,
                contentDescription = "Find Nearby Water",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isScanningNearby) "Scanning Waters..."
                else "Find Closest Water (${discoveredWaters.firstOrNull()?.distanceMiles ?: 0.0} mi)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp
            )
        }

        // =========================================================================
        // 5. HORIZONTAL CAROUSEL OF DISCOVERED WATER BODIES (Synchronized with Map)
        // =========================================================================
        if (!showFullListView) {
            LazyRow(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                contentPadding = PaddingValues(horizontal = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(discoveredWaters, key = { it.waterBody.id }) { discovered ->
                    val isSelected = discovered.waterBody.id == selectedWater.id
                    DiscoveredWaterCard(
                        discovered = discovered,
                        isSelected = isSelected,
                        onSelect = { onSelectWater(discovered.waterBody) },
                        onStartSession = { onStartSession(discovered.waterBody) },
                        onGamePlan = { onGamePlan(discovered.waterBody) },
                        onGetDirections = { directionsWater = discovered.waterBody }
                    )
                }
            }
        }

        // =========================================================================
        // 6. EXPANDABLE FULL LIST DRAWER OF ALL DISCOVERED WATER BODIES
        // =========================================================================
        AnimatedVisibility(
            visible = showFullListView,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.60f),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = NcCard,
                border = androidx.compose.foundation.BorderStroke(1.5.dp, NcLime),
                shadowElevation = 24.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "BODIES OF WATER NEAR YOUR LOCATION",
                                color = NcLime,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "${discoveredWaters.size} Spots Discovered • ${currentAnchor.label}",
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        IconButton(onClick = { showFullListView = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(discoveredWaters) { item ->
                            val isSelected = item.waterBody.id == selectedWater.id
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) NcCardElevated else Color(0xFF071820),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) NcLime else NcBorderSubtle
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onSelectWater(item.waterBody)
                                        showFullListView = false
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = NcLime.copy(alpha = 0.2f)
                                            ) {
                                                Text(
                                                    text = "📍 ${item.distanceMiles} MI",
                                                    color = NcLime,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Black,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = item.waterBody.name,
                                                color = TextPrimary,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "${item.waterBody.location} • ${item.waterBody.category.label}",
                                            color = TextSecondary,
                                            fontSize = 11.sp
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "Access: ${item.waterBody.accessType}",
                                            color = TextMuted,
                                            fontSize = 10.sp,
                                            maxLines = 1
                                        )
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = NcCardElevated
                                        ) {
                                            Text(
                                                text = "${item.waterBody.maxDepthFt} ft",
                                                color = HydroCyan,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = item.waterBody.surfaceArea,
                                            color = TextMuted,
                                            fontSize = 10.sp
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

    // Directions & Access Coordinates Dialog (§4.1)
    if (directionsWater != null) {
        val target = directionsWater!!
        AlertDialog(
            onDismissRequest = { directionsWater = null },
            icon = { Icon(Icons.Default.Directions, contentDescription = null, tint = HydroCyan) },
            title = {
                Text(
                    text = "Directions & Access",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = target.name,
                        color = NcLime,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Location: ${target.location}",
                        color = TextPrimary,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "GPS Coordinates: ${String.format(java.util.Locale.US, "%.4f, %.4f", target.lat, target.lng)}",
                        color = HydroCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(target.accessStatus.badgeColorHex).copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(target.accessStatus.badgeColorHex).copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = "Access: ${target.accessStatus.label} (${target.accessType})",
                            color = Color(target.accessStatus.badgeColorHex),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Text(
                        text = "Max Depth: ${target.maxDepthFt} ft • Surface: ${target.surfaceArea}",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val w = target
                        directionsWater = null
                        onStartSession(w)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CatchGreen, contentColor = MarineDeep)
                ) {
                    Text("Start Session Here", fontWeight = FontWeight.Black)
                }
            },
            dismissButton = {
                TextButton(onClick = { directionsWater = null }) {
                    Text("Close", color = TextSecondary)
                }
            },
            containerColor = NcCard,
            tonalElevation = 8.dp
        )
    }
}

/**
 * Compact, Glanceable Discovered Water Card for the Bottom Carousel
 */
@Composable
private fun DiscoveredWaterCard(
    discovered: DiscoveredWater,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onStartSession: () -> Unit = onSelect,
    onGamePlan: () -> Unit = onSelect,
    onGetDirections: () -> Unit = {}
) {
    val water = discovered.waterBody

    Surface(
        modifier = Modifier
            .width(310.dp)
            .clickable { onSelect() },
        shape = RoundedCornerShape(18.dp),
        color = if (isSelected) NcCard.copy(alpha = 0.98f) else Color(0xFF091B1F).copy(alpha = 0.94f),
        border = androidx.compose.foundation.BorderStroke(
            if (isSelected) 1.5.dp else 1.dp,
            if (isSelected) NcLime else NcBorderSubtle
        ),
        shadowElevation = if (isSelected) 12.dp else 4.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Distance Badge & Depth Tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = NcLime.copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NcLime.copy(alpha = 0.6f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.NearMe, contentDescription = null, tint = NcLime, modifier = Modifier.size(11.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${discovered.distanceMiles} MI AWAY",
                            color = NcLime,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MarineSurfaceVariant
                ) {
                    Text(
                        text = "${water.maxDepthFt} FT MAX",
                        color = HydroCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Water Name & Access Status Badge
            Text(
                text = water.name,
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(3.dp))

            // Access Status Badge (§4.1)
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = Color(water.accessStatus.badgeColorHex).copy(alpha = 0.15f),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(water.accessStatus.badgeColorHex).copy(alpha = 0.5f))
            ) {
                Text(
                    text = water.accessStatus.label,
                    color = Color(water.accessStatus.badgeColorHex),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${water.location} • ${water.surfaceArea}",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    modifier = Modifier.weight(1f),
                    maxLines = 1
                )
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(water.dataOrigin.badgeColorHex).copy(alpha = 0.15f)
                ) {
                    Text(
                        text = water.dataOrigin.label,
                        color = Color(water.dataOrigin.badgeColorHex),
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = NcBorderSubtle)
            Spacer(modifier = Modifier.height(8.dp))

            // Action Buttons: Start Fishing Session, Game Plan, and Directions (§4.1)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Start Fishing Session (carries water to On the Bank)
                Button(
                    onClick = onStartSession,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CatchGreen,
                        contentColor = MarineDeep
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.weight(1.3f)
                ) {
                    Icon(Icons.Default.RecordVoiceOver, contentDescription = null, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Session",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                // 2. Game Plan (carries water to Game Plan Strategy)
                OutlinedButton(
                    onClick = onGamePlan,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = HydroCyan),
                    border = androidx.compose.foundation.BorderStroke(1.dp, HydroCyan),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Plan",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // 3. Directions Action
                IconButton(
                    onClick = onGetDirections,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(NcCardElevated)
                ) {
                    Icon(
                        imageVector = Icons.Default.Directions,
                        contentDescription = "Get Directions",
                        tint = HydroCyan,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

/**
 * Interactive Vector Bathymetric Map Canvas
 * Visualizes user's location pin, distance circles, and water body markers
 */
@Composable
private fun InteractiveMapCanvas(
    currentAnchor: UserLocationAnchor,
    selectedWater: WaterBody,
    discoveredWaters: List<DiscoveredWater>,
    onMarkerClick: (WaterBody) -> Unit,
    radarPulse: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Dark Marine Bathymetric Background
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF071417), Color(0xFF0D2329), Color(0xFF061519))
            )
        )

        // Lat/Lng Coordinate Grid
        val step = 55.dp.toPx()
        var x = 0f
        while (x < w) {
            drawLine(
                color = Color(0xFF133038).copy(alpha = 0.7f),
                start = Offset(x, 0f),
                end = Offset(x, h),
                strokeWidth = 1f
            )
            x += step
        }
        var y = 0f
        while (y < h) {
            drawLine(
                color = Color(0xFF133038).copy(alpha = 0.7f),
                start = Offset(0f, y),
                end = Offset(w, y),
                strokeWidth = 1f
            )
            y += step
        }

        // Center Point: User Anchor ("You Are Here")
        val userCenter = Offset(w * 0.5f, h * 0.44f)

        // Radar Scanning Pulse
        if (radarPulse > 0f) {
            val maxPulse = w * 0.75f
            drawCircle(
                color = NcLime.copy(alpha = (1f - radarPulse) * 0.5f),
                radius = maxPulse * radarPulse,
                center = userCenter,
                style = Stroke(width = 3.dp.toPx())
            )
            drawCircle(
                color = HydroCyan.copy(alpha = (1f - radarPulse) * 0.25f),
                radius = maxPulse * radarPulse * 0.5f,
                center = userCenter,
                style = Stroke(width = 2.dp.toPx())
            )
        }

        // Distance Range Rings (5 mi, 15 mi, 30 mi)
        drawCircle(
            color = Color(0xFF1C4550).copy(alpha = 0.5f),
            radius = w * 0.22f,
            center = userCenter,
            style = Stroke(width = 1.dp.toPx())
        )
        drawCircle(
            color = Color(0xFF1C4550).copy(alpha = 0.4f),
            radius = w * 0.42f,
            center = userCenter,
            style = Stroke(width = 1.dp.toPx())
        )

        // Stylized Lake Shoreline Contour in Background
        val shoreline = Path().apply {
            moveTo(w * 0.12f, h * 0.26f)
            cubicTo(w * 0.28f, h * 0.16f, w * 0.68f, h * 0.18f, w * 0.86f, h * 0.30f)
            cubicTo(w * 0.96f, h * 0.46f, w * 0.82f, h * 0.64f, w * 0.70f, h * 0.70f)
            cubicTo(w * 0.52f, h * 0.78f, w * 0.32f, h * 0.72f, w * 0.16f, h * 0.60f)
            close()
        }
        drawPath(
            path = shoreline,
            color = Color(0xFF163E48),
            style = Stroke(width = 2.5.dp.toPx())
        )

        // Draw Discovered Water Markers around the user location
        // Relative placement mapped from real lat/long offsets
        discoveredWaters.take(8).forEachIndexed { index, discovered ->
            val water = discovered.waterBody
            val isSelected = water.id == selectedWater.id

            // Calculate relative offset from user center based on lat/lng differences
            val dLat = (water.lat - currentAnchor.lat)
            val dLng = (water.lng - currentAnchor.lng)

            // Scaled offset for screen canvas
            val scale = (w * 0.38f) / (discoveredWaters.maxOfOrNull { it.distanceMiles }?.coerceAtLeast(10.0) ?: 30.0)
            val markerOffset = Offset(
                x = (userCenter.x + (dLng * scale * 50f)).toFloat().coerceIn(40f, w - 40f),
                y = (userCenter.y - (dLat * scale * 50f)).toFloat().coerceIn(120f, h - 220f)
            )

            // Line from user to water marker
            drawLine(
                color = if (isSelected) NcLime.copy(alpha = 0.4f) else Color(0xFF1B404B).copy(alpha = 0.25f),
                start = userCenter,
                end = markerOffset,
                strokeWidth = if (isSelected) 2.dp.toPx() else 1.dp.toPx()
            )

            // Marker Glow if selected
            if (isSelected) {
                drawCircle(
                    color = NcLime.copy(alpha = 0.25f),
                    radius = 24.dp.toPx(),
                    center = markerOffset
                )
                drawCircle(
                    color = NcLime,
                    radius = 10.dp.toPx(),
                    center = markerOffset
                )
                drawCircle(
                    color = NcBg,
                    radius = 5.dp.toPx(),
                    center = markerOffset
                )
            } else {
                drawCircle(
                    color = HydroCyan.copy(alpha = 0.3f),
                    radius = 14.dp.toPx(),
                    center = markerOffset
                )
                drawCircle(
                    color = HydroCyan,
                    radius = 7.dp.toPx(),
                    center = markerOffset
                )
                drawCircle(
                    color = NcBg,
                    radius = 3.dp.toPx(),
                    center = markerOffset
                )
            }
        }

        // Draw User Location Center Pin ("You Are Here")
        drawCircle(
            color = if (currentAnchor.isGps) CatchGreen.copy(alpha = 0.3f) else StrikeAmber.copy(alpha = 0.3f),
            radius = 18.dp.toPx(),
            center = userCenter
        )
        drawCircle(
            color = if (currentAnchor.isGps) CatchGreen else StrikeAmber,
            radius = 8.dp.toPx(),
            center = userCenter
        )
        drawCircle(
            color = Color.White,
            radius = 3.dp.toPx(),
            center = userCenter
        )
    }
}
