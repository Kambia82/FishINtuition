package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "catch_logs")
data class CatchLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val speciesName: String,
    val weightLbs: Float,
    val lengthInches: Float,
    val lureUsed: String,
    val lureColor: String,
    val waterBodyName: String,
    val waterTempF: Int,
    val barometricPressureInHg: Double,
    val waterClarity: String,
    val timeOfDay: String,
    val notes: String = "",
    val timestampMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "game_plans")
data class GamePlan(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val waterBodyName: String,
    val targetSpecies: String,
    val plannedDate: String,
    val primaryPlanA: String,
    val secondaryPlanB: String,
    val emergencyFailsafePlanC: String,
    val riggingChecklist: String,
    val feedingWindowTarget: String,
    val notes: String = "",
    val createdAtMillis: Long = System.currentTimeMillis()
)
