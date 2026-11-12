package com.example.data.repository

import com.example.data.db.NovaCastDatabase
import com.example.data.engine.FishingIntelligenceEngine
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine

class NovaCastRepository(
    private val database: NovaCastDatabase
) {
    private val intelligenceEngine = FishingIntelligenceEngine()

    // All available water bodies (presets + user added)
    private val _waterBodies = MutableStateFlow(WaterCatalog.presetWaters)
    val waterBodies: StateFlow<List<WaterBody>> = _waterBodies.asStateFlow()

    // Selected water body
    private val _selectedWaterBody = MutableStateFlow(WaterCatalog.presetWaters.first())
    val selectedWaterBody: StateFlow<WaterBody> = _selectedWaterBody.asStateFlow()

    // Selected fish species (ANY species supported!)
    private val _selectedSpecies = MutableStateFlow(SpeciesCatalog.allSpecies.first())
    val selectedSpecies: StateFlow<FishSpecies> = _selectedSpecies.asStateFlow()

    // Current live environmental conditions
    private val _conditions = MutableStateFlow(
        EnvironmentalConditions(
            waterTempF = WaterCatalog.presetWaters.first().defaultTempF,
            waterClarity = WaterCatalog.presetWaters.first().defaultClarity
        )
    )
    val conditions: StateFlow<EnvironmentalConditions> = _conditions.asStateFlow()

    // Room DB Flows
    val allTackle: Flow<List<TackleItem>> = database.tackleDao().getAllTackle()
    val allCatches: Flow<List<CatchLog>> = database.catchDao().getAllCatches()
    val allGamePlans: Flow<List<GamePlan>> = database.gamePlanDao().getAllGamePlans()

    // Reactive recommendation combined with tackle box matches
    val currentRecommendation: Flow<FishingRecommendation> = combine(
        selectedSpecies,
        selectedWaterBody,
        conditions,
        allTackle
    ) { species, water, envConditions, tackleBox ->
        intelligenceEngine.generateRecommendation(
            species = species,
            waterBody = water,
            conditions = envConditions,
            userTackleBox = tackleBox
        )
    }

    fun selectWaterBody(waterBody: WaterBody) {
        _selectedWaterBody.value = waterBody
        // Update temperature and clarity defaults to match this specific water body
        _conditions.value = _conditions.value.copy(
            waterTempF = waterBody.defaultTempF,
            waterClarity = waterBody.defaultClarity
        )
        // If current species isn't in this water, suggest the primary dominant species
        val matchedSpeciesId = waterBody.primarySpecies.firstOrNull()
        if (matchedSpeciesId != null) {
            val species = SpeciesCatalog.allSpecies.find { it.id == matchedSpeciesId }
            if (species != null && !_selectedSpecies.value.id.equals(matchedSpeciesId)) {
                _selectedSpecies.value = species
            }
        }
    }

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
        val newBody = WaterBody(
            id = "custom_${System.currentTimeMillis()}",
            name = name,
            location = location,
            category = category,
            surfaceArea = surfaceArea,
            maxDepthFt = maxDepth,
            defaultTempF = tempF,
            defaultClarity = clarity,
            primarySpecies = speciesIds,
            structures = structures,
            accessType = access,
            description = description,
            lat = 35.0,
            lng = -85.0,
            dataOrigin = DataSourceOrigin.USER_CUSTOM_SPOT
        )
        _waterBodies.value = listOf(newBody) + _waterBodies.value
        selectWaterBody(newBody)
    }

    fun selectSpecies(species: FishSpecies) {
        _selectedSpecies.value = species
    }

    fun selectSpeciesById(speciesId: String) {
        val found = SpeciesCatalog.allSpecies.find { it.id == speciesId }
        if (found != null) {
            _selectedSpecies.value = found
        }
    }

    fun updateConditions(newConditions: EnvironmentalConditions) {
        _conditions.value = newConditions
    }

    fun updateWaterTemp(tempF: Int) {
        _conditions.value = _conditions.value.copy(waterTempF = tempF)
    }

    fun updatePressure(barometer: Double, trend: PressureTrend) {
        _conditions.value = _conditions.value.copy(
            barometerInHg = barometer,
            pressureTrend = trend
        )
    }

    fun updateClarity(clarity: WaterClarity) {
        _conditions.value = _conditions.value.copy(waterClarity = clarity)
    }

    fun updateTimeOfDay(time: TimeOfDay) {
        _conditions.value = _conditions.value.copy(timeOfDay = time)
    }

    fun updateSeason(season: Season) {
        _conditions.value = _conditions.value.copy(season = season)
    }

    // Tackle Box operations
    suspend fun addTackleItem(item: TackleItem): Long {
        return database.tackleDao().insertTackle(item)
    }

    suspend fun updateTackleItem(item: TackleItem) {
        database.tackleDao().updateTackle(item)
    }

    suspend fun deleteTackleItem(item: TackleItem) {
        database.tackleDao().deleteTackle(item)
    }

    // Catch log operations
    suspend fun logCatch(catchLog: CatchLog): Long {
        return database.catchDao().insertCatch(catchLog)
    }

    suspend fun deleteCatch(catchLog: CatchLog) {
        database.catchDao().deleteCatch(catchLog)
    }

    // Game plan operations
    suspend fun saveGamePlan(plan: GamePlan): Long {
        return database.gamePlanDao().insertPlan(plan)
    }

    suspend fun deleteGamePlan(plan: GamePlan) {
        database.gamePlanDao().deletePlan(plan)
    }

    fun getTacticalAdaptations(): List<TacticalAdaptation> {
        return intelligenceEngine.getTacticalAdaptations()
    }
}
