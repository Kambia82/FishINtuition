package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.NovaCastDatabase
import com.example.data.local.VoiceCommandPreferences
import com.example.data.model.*
import com.example.data.repository.NovaCastRepository
import com.example.voice.VoiceAssistantManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class NavigationTab(val label: String) {
    INTELLIGENCE("Intelligence"),
    ON_THE_BANK("On the Bank"),
    DISCOVERY("Discovery"),
    TACKLE_BOX("Tackle Box"),
    GAME_PLAN("Game Plan"),
    CATCH_LOG("Catch Log")
}

class NovaCastViewModel(application: Application) : AndroidViewModel(application) {

    private val database = NovaCastDatabase.getDatabase(application, viewModelScope)
    val repository = NovaCastRepository(database)
    val voiceAssistant = VoiceAssistantManager(application)
    private val voicePreferences = VoiceCommandPreferences(application)

    // Customizable Voice Commands
    val customVoiceTriggers: StateFlow<List<CustomVoiceTrigger>> = voicePreferences.triggersFlow

    // Active Navigation (Bible v2.0 Dashboard Architecture)
    private val _currentDestination = MutableStateFlow(NavigationDestination.HOME_DASHBOARD)
    val currentDestination: StateFlow<NavigationDestination> = _currentDestination.asStateFlow()

    fun navigateTo(destination: NavigationDestination) {
        _currentDestination.value = destination
    }

    // Repository Flows exposed
    val waterBodies = repository.waterBodies
    val selectedWaterBody = repository.selectedWaterBody
    val selectedSpecies = repository.selectedSpecies
    val conditions = repository.conditions
    val allTackle = repository.allTackle
    val allCatches = repository.allCatches
    val allGamePlans = repository.allGamePlans
    val recommendation = repository.currentRecommendation.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    // UI Dialog & Sheet States
    private val _showSpeciesPicker = MutableStateFlow(false)
    val showSpeciesPicker: StateFlow<Boolean> = _showSpeciesPicker.asStateFlow()

    private val _showConditionsEditor = MutableStateFlow(false)
    val showConditionsEditor: StateFlow<Boolean> = _showConditionsEditor.asStateFlow()

    private val _showAddTackleDialog = MutableStateFlow(false)
    val showAddTackleDialog: StateFlow<Boolean> = _showAddTackleDialog.asStateFlow()

    private val _showLogCatchDialog = MutableStateFlow(false)
    val showLogCatchDialog: StateFlow<Boolean> = _showLogCatchDialog.asStateFlow()

    private val _showCreatePlanDialog = MutableStateFlow(false)
    val showCreatePlanDialog: StateFlow<Boolean> = _showCreatePlanDialog.asStateFlow()

    private val _showAddWaterDialog = MutableStateFlow(false)
    val showAddWaterDialog: StateFlow<Boolean> = _showAddWaterDialog.asStateFlow()

    private val _showProPassDialog = MutableStateFlow(false)
    val showProPassDialog: StateFlow<Boolean> = _showProPassDialog.asStateFlow()

    private val _showVoiceSettingsDialog = MutableStateFlow(false)
    val showVoiceSettingsDialog: StateFlow<Boolean> = _showVoiceSettingsDialog.asStateFlow()

    private val _showFeedbackContactDialog = MutableStateFlow(false)
    val showFeedbackContactDialog: StateFlow<Boolean> = _showFeedbackContactDialog.asStateFlow()

    // Voice & Audio Retrieve Coach State
    val isSpeaking = voiceAssistant.isSpeaking
    val isListening = voiceAssistant.isListening
    val lastRecognizedVoiceQuery = voiceAssistant.lastRecognizedQuery
    val lastSpokenResponse = voiceAssistant.lastSpokenResponse
    val activeCadenceCoach = voiceAssistant.activeCadenceCoachText

    // Destination Navigation
    fun setDestination(dest: NavigationDestination) {
        _currentDestination.value = dest
    }

    fun selectWaterBody(waterBody: WaterBody) {
        repository.selectWaterBody(waterBody)
    }

    fun selectSpecies(species: FishSpecies) {
        repository.selectSpecies(species)
        _showSpeciesPicker.value = false
    }

    fun selectSpeciesById(speciesId: String) {
        repository.selectSpeciesById(speciesId)
        _showSpeciesPicker.value = false
    }

    fun updateConditions(conditions: EnvironmentalConditions) {
        repository.updateConditions(conditions)
        _showConditionsEditor.value = false
    }

    fun updateWaterTemp(temp: Int) {
        repository.updateWaterTemp(temp)
    }

    fun updatePressure(barometer: Double, trend: PressureTrend) {
        repository.updatePressure(barometer, trend)
    }

    fun updateClarity(clarity: WaterClarity) {
        repository.updateClarity(clarity)
    }

    fun setShowSpeciesPicker(show: Boolean) {
        _showSpeciesPicker.value = show
    }

    fun setShowConditionsEditor(show: Boolean) {
        _showConditionsEditor.value = show
    }

    fun setShowAddTackleDialog(show: Boolean) {
        _showAddTackleDialog.value = show
    }

    fun setShowLogCatchDialog(show: Boolean) {
        _showLogCatchDialog.value = show
    }

    fun setShowCreatePlanDialog(show: Boolean) {
        _showCreatePlanDialog.value = show
    }

    fun setShowAddWaterDialog(show: Boolean) {
        _showAddWaterDialog.value = show
    }

    fun setShowProPassDialog(show: Boolean) {
        _showProPassDialog.value = show
    }

    fun setShowVoiceSettingsDialog(show: Boolean) {
        _showVoiceSettingsDialog.value = show
    }

    fun setShowFeedbackContactDialog(show: Boolean) {
        _showFeedbackContactDialog.value = show
    }

    fun addCustomVoicePhrase(intent: VoiceActionIntent, phrase: String) {
        voicePreferences.addPhrase(intent, phrase)
    }

    fun removeCustomVoicePhrase(intent: VoiceActionIntent, phrase: String) {
        voicePreferences.removePhrase(intent, phrase)
    }

    fun setPrimaryVoicePhrase(intent: VoiceActionIntent, phrase: String) {
        voicePreferences.setPrimaryPhrase(intent, phrase)
    }

    fun resetVoiceTriggersToDefaults() {
        voicePreferences.resetToDefaults()
    }

    // Voice triggers & Hands-Free Audio
    fun triggerVoiceCommand(customQuery: String) {
        val currentRec = recommendation.value
        val topMatch = currentRec?.matchedTackleFromBox?.firstOrNull()?.name
        val recommendedLure = currentRec?.primaryLureName ?: "Squarebill Crankbait"
        val altLure = currentRec?.alternateLureName
        val waterName = selectedWaterBody.value.name
        val targetSpeciesName = selectedSpecies.value.commonName
        val waterTemp = conditions.value.waterTempF
        val barometer = conditions.value.barometerInHg
        val trend = conditions.value.pressureTrend.label

        voiceAssistant.processCustomVoiceCommand(
            query = customQuery,
            targetSpeciesName = targetSpeciesName,
            waterName = waterName,
            recommendedLure = recommendedLure,
            alternateLure = altLure,
            topTackleMatched = topMatch,
            waterTemp = waterTemp,
            barometer = barometer,
            pressureTrend = trend,
            customTriggers = voicePreferences.getTriggers(),
            onLogCatchRequested = {
                _showLogCatchDialog.value = true
            },
            onSpeciesChangeRequested = { speciesId ->
                selectSpeciesById(speciesId)
                val newSpecies = SpeciesCatalog.allSpecies.find { it.id == speciesId }
                voiceAssistant.speak("Switched target species to ${newSpecies?.commonName ?: speciesId}. Generating updated lure and cadence plan.")
            }
        )
    }

    fun startHandsFreeVoiceRecognition() {
        val promptSuggestion = voicePreferences.getTriggers()
            .find { it.intent == VoiceActionIntent.LURE_RECOMMENDATION }?.primaryPhrase ?: "What are they biting?"
        voiceAssistant.startListening(promptSuggestion) { recognizedText ->
            triggerVoiceCommand(recognizedText)
        }
    }

    fun stopSpeaking() {
        voiceAssistant.stopSpeaking()
    }

    // Tackle Box Actions
    fun addTackleItem(
        name: String,
        category: String,
        brand: String,
        color: String,
        weight: String,
        targetSpecies: String,
        clarity: String,
        depth: String,
        style: String,
        notes: String
    ) {
        viewModelScope.launch {
            repository.addTackleItem(
                TackleItem(
                    name = name,
                    category = category,
                    brand = brand,
                    color = color,
                    weightOz = weight,
                    targetSpeciesSummary = targetSpecies,
                    bestWaterClarity = clarity,
                    optimalDepthRange = depth,
                    presentationStyle = style,
                    notes = notes,
                    isFavorite = false
                )
            )
            _showAddTackleDialog.value = false
        }
    }

    fun toggleTackleFavorite(item: TackleItem) {
        viewModelScope.launch {
            repository.updateTackleItem(item.copy(isFavorite = !item.isFavorite))
        }
    }

    fun deleteTackleItem(item: TackleItem) {
        viewModelScope.launch {
            repository.deleteTackleItem(item)
        }
    }

    // Catch Log Actions
    fun logCatch(
        species: String,
        weight: Float,
        length: Float,
        lure: String,
        color: String,
        waterBody: String,
        notes: String
    ) {
        viewModelScope.launch {
            val cond = conditions.value
            repository.logCatch(
                CatchLog(
                    speciesName = species,
                    weightLbs = weight,
                    lengthInches = length,
                    lureUsed = lure,
                    lureColor = color,
                    waterBodyName = waterBody,
                    waterTempF = cond.waterTempF,
                    barometricPressureInHg = cond.barometerInHg,
                    waterClarity = cond.waterClarity.label,
                    timeOfDay = cond.timeOfDay.label,
                    notes = notes
                )
            )
            _showLogCatchDialog.value = false
        }
    }

    fun deleteCatch(catchLog: CatchLog) {
        viewModelScope.launch {
            repository.deleteCatch(catchLog)
        }
    }

    // Game Plan Actions
    fun saveGamePlan(
        title: String,
        waterBody: String,
        species: String,
        date: String,
        planA: String,
        planB: String,
        planC: String,
        rigging: String,
        window: String,
        notes: String
    ) {
        viewModelScope.launch {
            repository.saveGamePlan(
                GamePlan(
                    title = title,
                    waterBodyName = waterBody,
                    targetSpecies = species,
                    plannedDate = date,
                    primaryPlanA = planA,
                    secondaryPlanB = planB,
                    emergencyFailsafePlanC = planC,
                    riggingChecklist = rigging,
                    feedingWindowTarget = window,
                    notes = notes
                )
            )
            _showCreatePlanDialog.value = false
        }
    }

    fun deleteGamePlan(plan: GamePlan) {
        viewModelScope.launch {
            repository.deleteGamePlan(plan)
        }
    }

    // Custom Water Body
    fun addCustomWaterBody(
        name: String,
        location: String,
        category: WaterCategory,
        surfaceArea: String,
        maxDepth: Int,
        tempF: Int,
        clarity: WaterClarity,
        speciesIds: List<String>,
        structures: List<String>,
        access: String,
        description: String
    ) {
        repository.addCustomWaterBody(
            name, location, category, surfaceArea, maxDepth, tempF, clarity,
            speciesIds, structures, access, description
        )
        _showAddWaterDialog.value = false
    }

    fun getTacticalAdaptations(): List<TacticalAdaptation> {
        return repository.getTacticalAdaptations()
    }

    override fun onCleared() {
        super.onCleared()
        voiceAssistant.release()
    }
}
