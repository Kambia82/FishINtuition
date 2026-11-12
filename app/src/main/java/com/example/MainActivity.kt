package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.NavigationDestination
import com.example.ui.NovaCastViewModel
import com.example.ui.components.AudibleVoiceResponseBanner
import com.example.ui.components.TopTacticalBar
import com.example.ui.dialogs.*
import com.example.ui.screens.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {

    private val viewModel: NovaCastViewModel by viewModels()

    private val requestRecordAudioLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.startHandsFreeVoiceRecognition()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                NovaCastApp(
                    viewModel = viewModel,
                    onRequestRecordAudio = {
                        val hasPerm = ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.RECORD_AUDIO
                        ) == PackageManager.PERMISSION_GRANTED
                        if (hasPerm) {
                            viewModel.startHandsFreeVoiceRecognition()
                        } else {
                            requestRecordAudioLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun NovaCastApp(
    viewModel: NovaCastViewModel,
    onRequestRecordAudio: () -> Unit
) {
    val destination by viewModel.currentDestination.collectAsStateWithLifecycle()
    val waterBody by viewModel.selectedWaterBody.collectAsStateWithLifecycle()
    val waterBodies by viewModel.waterBodies.collectAsStateWithLifecycle()
    val species by viewModel.selectedSpecies.collectAsStateWithLifecycle()
    val conditions by viewModel.conditions.collectAsStateWithLifecycle()
    val recommendation by viewModel.recommendation.collectAsStateWithLifecycle()
    val tackleItems by viewModel.allTackle.collectAsStateWithLifecycle(emptyList())
    val catches by viewModel.allCatches.collectAsStateWithLifecycle(emptyList())
    val gamePlans by viewModel.allGamePlans.collectAsStateWithLifecycle(emptyList())

    val isSpeaking by viewModel.isSpeaking.collectAsStateWithLifecycle()
    val isListening by viewModel.isListening.collectAsStateWithLifecycle()
    val lastRecognizedVoiceQuery by viewModel.lastRecognizedVoiceQuery.collectAsStateWithLifecycle()
    val lastSpokenResponse by viewModel.lastSpokenResponse.collectAsStateWithLifecycle()
    val activeCadenceCoach by viewModel.activeCadenceCoach.collectAsStateWithLifecycle()

    // Dialog state
    val showSpeciesPicker by viewModel.showSpeciesPicker.collectAsStateWithLifecycle()
    val showConditionsEditor by viewModel.showConditionsEditor.collectAsStateWithLifecycle()
    val showAddTackleDialog by viewModel.showAddTackleDialog.collectAsStateWithLifecycle()
    val showLogCatchDialog by viewModel.showLogCatchDialog.collectAsStateWithLifecycle()
    val showCreatePlanDialog by viewModel.showCreatePlanDialog.collectAsStateWithLifecycle()
    val showAddWaterDialog by viewModel.showAddWaterDialog.collectAsStateWithLifecycle()
    val showVoiceSettingsDialog by viewModel.showVoiceSettingsDialog.collectAsStateWithLifecycle()
    val showFeedbackContactDialog by viewModel.showFeedbackContactDialog.collectAsStateWithLifecycle()
    val customVoiceTriggers by viewModel.customVoiceTriggers.collectAsStateWithLifecycle()

    // Handle system back button to pop sub-screens back to Home Dashboard
    if (destination != NavigationDestination.HOME_DASHBOARD) {
        BackHandler {
            viewModel.navigateTo(NavigationDestination.HOME_DASHBOARD)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = NcBg,
        topBar = {
            TopTacticalBar(
                currentDestination = destination,
                waterBody = waterBody,
                species = species,
                conditions = conditions,
                isSpeaking = isSpeaking,
                isListening = isListening,
                onHomeClick = { viewModel.navigateTo(NavigationDestination.HOME_DASHBOARD) },
                onWaterClick = { viewModel.navigateTo(NavigationDestination.RECON) },
                onSpeciesClick = { viewModel.setShowSpeciesPicker(true) },
                onConditionsClick = { viewModel.setShowConditionsEditor(true) },
                onVoiceClick = {
                    viewModel.navigateTo(NavigationDestination.HANDS_FREE_AI)
                    onRequestRecordAudio()
                },
                onStopSpeaking = { viewModel.stopSpeaking() },
                onFeedbackClick = { viewModel.setShowFeedbackContactDialog(true) }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = NcCard,
                contentColor = MaterialTheme.colorScheme.onSurface,
                tonalElevation = 8.dp,
                windowInsets = WindowInsets.navigationBars
            ) {
                // Navigation items corresponding to the core 6-tile journey
                NavigationBarItem(
                    selected = destination == NavigationDestination.HOME_DASHBOARD,
                    onClick = { viewModel.navigateTo(NavigationDestination.HOME_DASHBOARD) },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                    label = { Text("Home", fontSize = 10.sp, fontWeight = if (destination == NavigationDestination.HOME_DASHBOARD) FontWeight.Bold else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NcBg,
                        indicatorColor = NcLime,
                        selectedTextColor = NcLime,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    )
                )

                NavigationBarItem(
                    selected = destination == NavigationDestination.RECON,
                    onClick = { viewModel.navigateTo(NavigationDestination.RECON) },
                    icon = { Icon(Icons.Default.Explore, contentDescription = "Recon") },
                    label = { Text("Recon", fontSize = 10.sp, fontWeight = if (destination == NavigationDestination.RECON) FontWeight.Bold else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NcBg,
                        indicatorColor = NcLime,
                        selectedTextColor = NcLime,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    )
                )

                NavigationBarItem(
                    selected = destination == NavigationDestination.GAME_PLAN,
                    onClick = { viewModel.navigateTo(NavigationDestination.GAME_PLAN) },
                    icon = { Icon(Icons.Default.Assignment, contentDescription = "Plan") },
                    label = { Text("Plan", fontSize = 10.sp, fontWeight = if (destination == NavigationDestination.GAME_PLAN) FontWeight.Bold else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NcBg,
                        indicatorColor = NcLime,
                        selectedTextColor = NcLime,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    )
                )

                NavigationBarItem(
                    selected = destination == NavigationDestination.ON_THE_BANK,
                    onClick = { viewModel.navigateTo(NavigationDestination.ON_THE_BANK) },
                    icon = { Icon(Icons.Default.RecordVoiceOver, contentDescription = "On the Bank") },
                    label = { Text("Bank", fontSize = 10.sp, fontWeight = if (destination == NavigationDestination.ON_THE_BANK) FontWeight.Bold else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NcBg,
                        indicatorColor = NcLime,
                        selectedTextColor = NcLime,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    )
                )

                NavigationBarItem(
                    selected = destination == NavigationDestination.TACKLEBOX,
                    onClick = { viewModel.navigateTo(NavigationDestination.TACKLEBOX) },
                    icon = { Icon(Icons.Default.Inventory2, contentDescription = "Tacklebox") },
                    label = { Text("Tackle", fontSize = 10.sp, fontWeight = if (destination == NavigationDestination.TACKLEBOX) FontWeight.Bold else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NcBg,
                        indicatorColor = NcLime,
                        selectedTextColor = NcLime,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    )
                )

                NavigationBarItem(
                    selected = destination == NavigationDestination.LEARN_AND_FUN,
                    onClick = { viewModel.navigateTo(NavigationDestination.LEARN_AND_FUN) },
                    icon = { Icon(Icons.Default.School, contentDescription = "Learn") },
                    label = { Text("Learn", fontSize = 10.sp, fontWeight = if (destination == NavigationDestination.LEARN_AND_FUN) FontWeight.Bold else FontWeight.Normal) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NcBg,
                        indicatorColor = NcLime,
                        selectedTextColor = NcLime,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    )
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Audible Voice Response Display if active
            AudibleVoiceResponseBanner(
                text = lastSpokenResponse,
                isSpeaking = isSpeaking,
                onDismiss = { viewModel.stopSpeaking() }
            )

            // Dynamic view based on Bible v2.0 Architecture
            when (destination) {
                NavigationDestination.HOME_DASHBOARD -> {
                    HomeDashboardScreen(
                        onNavigate = { dest -> viewModel.navigateTo(dest) },
                        onOpenFeedback = { viewModel.setShowFeedbackContactDialog(true) }
                    )
                }

                NavigationDestination.RECON -> {
                    WaterDiscoveryScreen(
                        waterBodies = waterBodies,
                        selectedWater = waterBody,
                        onSelectWater = { water ->
                            viewModel.selectWaterBody(water)
                        },
                        onStartSession = { water ->
                            viewModel.selectWaterBody(water)
                            viewModel.navigateTo(NavigationDestination.ON_THE_BANK)
                        },
                        onGamePlan = { water ->
                            viewModel.selectWaterBody(water)
                            viewModel.navigateTo(NavigationDestination.GAME_PLAN)
                        },
                        onAddCustomWaterClick = { viewModel.setShowAddWaterDialog(true) }
                    )
                }

                NavigationDestination.GAME_PLAN -> {
                    IntelligenceScreen(
                        species = species,
                        conditions = conditions,
                        recommendation = recommendation,
                        onSpeciesClick = { viewModel.setShowSpeciesPicker(true) },
                        onConditionsClick = { viewModel.setShowConditionsEditor(true) },
                        onAskAudioCoach = { command -> viewModel.triggerVoiceCommand(command) },
                        onNavigateToTackleBox = { viewModel.navigateTo(NavigationDestination.TACKLEBOX) },
                        onSelectSpecies = { s -> viewModel.selectSpecies(s) }
                    )
                }

                NavigationDestination.ON_THE_BANK -> {
                    OnTheBankScreen(
                        waterBody = waterBody,
                        species = species,
                        conditions = conditions,
                        recommendation = recommendation,
                        tackleBoxCount = tackleItems.size,
                        isSpeaking = isSpeaking,
                        isListening = isListening,
                        activeCadenceCoach = activeCadenceCoach,
                        lastRecognizedQuery = lastRecognizedVoiceQuery,
                        lastSpokenResponse = lastSpokenResponse,
                        adaptations = viewModel.getTacticalAdaptations(),
                        onVoiceCommand = { cmd -> viewModel.triggerVoiceCommand(cmd) },
                        onStartHandsFreeListening = { onRequestRecordAudio() },
                        onStopSpeaking = { viewModel.stopSpeaking() },
                        onNavigateToGamePlan = { viewModel.navigateTo(NavigationDestination.GAME_PLAN) },
                        onLogCatchClick = { viewModel.setShowLogCatchDialog(true) },
                        onOpenHandsFreeAi = { viewModel.navigateTo(NavigationDestination.HANDS_FREE_AI) },
                        allWaterBodies = waterBodies,
                        onSelectWaterBody = { w -> viewModel.selectWaterBody(w) },
                        onUpdateClarity = { c -> viewModel.updateClarity(c) },
                        customTriggers = customVoiceTriggers,
                        onOpenVoiceSettings = { viewModel.setShowVoiceSettingsDialog(true) }
                    )
                }

                NavigationDestination.HANDS_FREE_AI -> {
                    HandsFreeAiScreen(
                        waterBody = waterBody,
                        species = species,
                        conditions = conditions,
                        recommendation = recommendation,
                        tackleBoxCount = tackleItems.size,
                        isSpeaking = isSpeaking,
                        isListening = isListening,
                        activeCadenceCoach = activeCadenceCoach,
                        lastRecognizedQuery = lastRecognizedVoiceQuery,
                        lastSpokenResponse = lastSpokenResponse,
                        onVoiceCommand = { cmd -> viewModel.triggerVoiceCommand(cmd) },
                        onStartHandsFreeListening = { onRequestRecordAudio() },
                        onStopSpeaking = { viewModel.stopSpeaking() },
                        onLogCatchClick = { viewModel.setShowLogCatchDialog(true) },
                        onNavigateToBank = { viewModel.navigateTo(NavigationDestination.ON_THE_BANK) },
                        customTriggers = customVoiceTriggers,
                        onOpenVoiceSettings = { viewModel.setShowVoiceSettingsDialog(true) }
                    )
                }

                NavigationDestination.TACKLEBOX -> {
                    TackleboxScreen(
                        tackleItems = tackleItems,
                        species = species,
                        conditions = conditions,
                        onAddTackleClick = { viewModel.setShowAddTackleDialog(true) },
                        onToggleFavorite = { item -> viewModel.toggleTackleFavorite(item) },
                        onDeleteTackle = { item -> viewModel.deleteTackleItem(item) }
                    )
                }

                NavigationDestination.LEARN_AND_FUN -> {
                    LearnAndFunScreen(
                        onBack = { viewModel.navigateTo(NavigationDestination.HOME_DASHBOARD) }
                    )
                }

                NavigationDestination.WALMART_RUN -> {
                    WalmartRunScreen(
                        onBack = { viewModel.navigateTo(NavigationDestination.HOME_DASHBOARD) },
                        onAddToTackleBox = { item ->
                            viewModel.addTackleItem(
                                name = item.name,
                                category = item.category,
                                brand = item.recommendedBrand,
                                color = "Standard / Natural",
                                weight = "Standard",
                                targetSpecies = "All Gamefish",
                                clarity = "Any",
                                depth = "All Column",
                                style = "Standard",
                                notes = "Added from Walmart Run: ${item.whyNeeded}"
                            )
                        }
                    )
                }

                NavigationDestination.CATCH_LOG -> {
                    CatchLogScreen(
                        catches = catches,
                        onLogCatchClick = { viewModel.setShowLogCatchDialog(true) },
                        onDeleteCatch = { catchLog -> viewModel.deleteCatch(catchLog) }
                    )
                }
            }
        }
    }

    // Modal Dialogs
    if (showSpeciesPicker) {
        SpeciesPickerDialog(
            currentSpecies = species,
            onSpeciesSelected = { newSpecies -> viewModel.selectSpecies(newSpecies) },
            onDismiss = { viewModel.setShowSpeciesPicker(false) }
        )
    }

    if (showConditionsEditor) {
        ConditionsEditorDialog(
            initialConditions = conditions,
            onSave = { newCond -> viewModel.updateConditions(newCond) },
            onDismiss = { viewModel.setShowConditionsEditor(false) }
        )
    }

    if (showAddTackleDialog) {
        AddTackleDialog(
            onAdd = { name, category, brand, color, weight, sp, clarity, depth, style, notes ->
                viewModel.addTackleItem(name, category, brand, color, weight, sp, clarity, depth, style, notes)
            },
            onDismiss = { viewModel.setShowAddTackleDialog(false) }
        )
    }

    if (showLogCatchDialog) {
        LogCatchDialog(
            currentSpeciesName = species.commonName,
            currentWaterName = waterBody.name,
            currentTemp = conditions.waterTempF,
            currentBarometer = conditions.barometerInHg,
            onSaveCatch = { sp, weight, len, lure, color, water, notes ->
                viewModel.logCatch(sp, weight, len, lure, color, water, notes)
            },
            onDismiss = { viewModel.setShowLogCatchDialog(false) }
        )
    }

    if (showCreatePlanDialog) {
        CreatePlanDialog(
            currentWaterName = waterBody.name,
            currentSpeciesName = species.commonName,
            onSave = { title, water, sp, date, planA, planB, planC, rigging, window, notes ->
                viewModel.saveGamePlan(title, water, sp, date, planA, planB, planC, rigging, window, notes)
            },
            onDismiss = { viewModel.setShowCreatePlanDialog(false) }
        )
    }

    if (showAddWaterDialog) {
        AddWaterDialog(
            onAddWater = { name, location, cat, area, depth, temp, clarity, spIds, struct, access, desc ->
                viewModel.addCustomWaterBody(name, location, cat, area, depth, temp, clarity, spIds, struct, access, desc)
            },
            onDismiss = { viewModel.setShowAddWaterDialog(false) }
        )
    }

    if (showVoiceSettingsDialog) {
        VoiceCommandsCustomizerDialog(
            triggers = customVoiceTriggers,
            onAddPhrase = { intent, phrase -> viewModel.addCustomVoicePhrase(intent, phrase) },
            onRemovePhrase = { intent, phrase -> viewModel.removeCustomVoicePhrase(intent, phrase) },
            onSetPrimaryPhrase = { intent, phrase -> viewModel.setPrimaryVoicePhrase(intent, phrase) },
            onResetDefaults = { viewModel.resetVoiceTriggersToDefaults() },
            onDismiss = { viewModel.setShowVoiceSettingsDialog(false) }
        )
    }

    if (showFeedbackContactDialog) {
        FeedbackContactDialog(
            currentWaterBody = waterBody,
            currentSpecies = species,
            onDismiss = { viewModel.setShowFeedbackContactDialog(false) }
        )
    }
}
