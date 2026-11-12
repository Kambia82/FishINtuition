package com.example.data.model

enum class PressureTrend(val label: String, val shortDesc: String) {
    FALLING_PREFRONTAL("Falling (Pre-Frontal)", "Aggressive feeding window — fish actively chasing forage before storm/front."),
    RISING_POSTFRONTAL("Rising (Post-Frontal)", "Lockjaw / bluebird skies — fish push deep into thickest shade and cover. Finesse required."),
    STEADY_HIGH("Steady High (30.15+ inHg)", "Stable but cautious bite. Target secondary structure and edge shadows."),
    STEADY_LOW("Steady Low (<29.85 inHg)", "Extended overcast or drizzle. Fish roam freely outside heavy cover.")
}

enum class WaterClarity(val label: String, val visibility: String) {
    CRYSTAL_CLEAR("Crystal Clear", "Visibility 6+ ft — requires natural translucent patterns and light line."),
    STAINED("Stained / Green Tint", "Visibility 2 to 4 ft — optimal compromise; high-action lures and pearlescent finishes."),
    MURKY("Murky / Off-Color", "Visibility 1 to 2 ft — needs contrast (dark silhouette or chartreuse) and vibration."),
    MUDDY("Muddy / Blown Out", "Visibility < 1 ft — relies strictly on sound, heavy thump, scent, and pitch-black profile.")
}

enum class TimeOfDay(val label: String) {
    DAWN("Dawn / First Light"),
    MORNING("Morning (7 AM - 11 AM)"),
    MIDDAY("Midday (11 AM - 3 PM)"),
    AFTERNOON("Afternoon (3 PM - 6 PM)"),
    DUSK("Dusk / Golden Hour"),
    NIGHT("Night / Twilight")
}

enum class Season(val label: String) {
    EARLY_SPRING("Early Spring (Transitions)"),
    PRE_SPAWN("Pre-Spawn (Aggressive Staging)"),
    SPAWN("Spawn (Shallow Beds / Nesting)"),
    POST_SPAWN("Post-Spawn (Recovery & Feed)"),
    SUMMER("Summer (Thermoclines & Offshore)"),
    EARLY_FALL("Early Fall (Following Bait to Creeks)"),
    LATE_FALL("Late Fall (Wintering Holes)"),
    WINTER("Winter (Metabolic Slowdown / Deep)")
}

data class SolunarPeriod(
    val name: String,             // e.g. "Major Window 1 (Overhead Transit)"
    val timeRange: String,        // e.g. "7:15 AM - 9:15 AM"
    val durationHours: Double,    // 2.0 or 1.0
    val intensity: String,        // "Peak Feeding Window" or "Minor Transition Feeding"
    val astronomicalBasis: String // "Moon Transit Overhead"
)

enum class MoonPhase(val label: String, val iconSymbol: String, val illuminationPercent: Int) {
    NEW_MOON("New Moon", "🌑", 0),
    WAXING_CRESCENT("Waxing Crescent", "🌒", 25),
    FIRST_QUARTER("First Quarter", "🌓", 50),
    WAXING_GIBBOUS("Waxing Gibbous", "🌔", 75),
    FULL_MOON("Full Moon", "🌕", 100),
    WANING_GIBBOUS("Waning Gibbous", "🌖", 75),
    LAST_QUARTER("Last Quarter", "🌗", 50),
    WANING_CRESCENT("Waning Crescent", "🌘", 25)
}

data class EnvironmentalConditions(
    val waterTempF: Int = 68,
    val airTempF: Int = 72,
    val barometerInHg: Double = 29.92,
    val pressureTrend: PressureTrend = PressureTrend.FALLING_PREFRONTAL,
    val waterClarity: WaterClarity = WaterClarity.STAINED,
    val timeOfDay: TimeOfDay = TimeOfDay.MORNING,
    val season: Season = Season.PRE_SPAWN,
    val windSpeedMph: Int = 8,
    val skyCover: String = "Partly Cloudy",
    val solunarRating: Float = 8.6f,
    val solunarWindow: String = "Major Feeding Window: 7:15 AM - 9:30 AM (Peak Solunar & Falling Barometer)",
    val moonPhase: MoonPhase = MoonPhase.FULL_MOON,
    val majorWindows: List<SolunarPeriod> = listOf(
        SolunarPeriod("Major Window 1", "7:15 AM - 9:30 AM", 2.25, "Peak Feeding Window", "Moon Transit Overhead"),
        SolunarPeriod("Major Window 2", "7:45 PM - 9:45 PM", 2.0, "Substantial Activity", "Moon Underfoot Transit")
    ),
    val minorWindows: List<SolunarPeriod> = listOf(
        SolunarPeriod("Minor Window 1", "1:20 AM - 2:20 AM", 1.0, "Moderate Feeding", "Moonrise Period"),
        SolunarPeriod("Minor Window 2", "1:50 PM - 2:50 PM", 1.0, "Moderate Feeding", "Moonset Period")
    ),
    val dataProvenance: String = "Calculated Astronomical & Meteorological Model"
)
