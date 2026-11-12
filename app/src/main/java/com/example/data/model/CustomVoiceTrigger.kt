package com.example.data.model

enum class VoiceActionIntent(
    val id: String,
    val title: String,
    val description: String,
    val defaultPrimaryPhrase: String,
    val defaultPhrases: List<String>
) {
    LURE_RECOMMENDATION(
        id = "lure_recommendation",
        title = "What Are They Biting? (Lure & Depth)",
        description = "Pinpoints exact lure, presentation & depth from current conditions",
        defaultPrimaryPhrase = "What are they biting?",
        defaultPhrases = listOf(
            "What are they biting?",
            "What are they hitting?",
            "What should I throw?",
            "What's working?",
            "What lure should I use?",
            "What's the hot bait?",
            "What are they eating?"
        )
    ),
    NOTHING_BITING(
        id = "nothing_biting",
        title = "Tough Bite / Fish Aren't Biting",
        description = "Activates tactical adjustments, cadence slowdown, or downsized profile",
        defaultPrimaryPhrase = "Nothing is biting",
        defaultPhrases = listOf(
            "Nothing is biting",
            "They aren't biting",
            "No bites so far",
            "It's dead out here",
            "Skunked so far",
            "Slow bite",
            "Not biting"
        )
    ),
    CHANGE_CADENCE(
        id = "change_cadence",
        title = "Switch Retrieve Cadence",
        description = "Audio retrieve metronome, burn-and-kill, or dead-stick countdown",
        defaultPrimaryPhrase = "Change cadence",
        defaultPhrases = listOf(
            "Change cadence",
            "Switch cadence",
            "Retrieve coach",
            "How fast should I reel?",
            "Slow down retrieve",
            "Speed up retrieve",
            "Switch retrieve"
        )
    ),
    LOG_CATCH(
        id = "log_catch",
        title = "Hands-Free Catch Logging",
        description = "Immediately logs catch with time, barometer & water temperature",
        defaultPrimaryPhrase = "Fish on!",
        defaultPhrases = listOf(
            "Fish on!",
            "Got one!",
            "Caught one!",
            "Landed one!",
            "Fish in the net!",
            "Got a hog!",
            "Landed a tank!"
        )
    ),
    CHECK_CONDITIONS(
        id = "check_conditions",
        title = "Check Live Conditions",
        description = "Audible report of water temp, barometer trend & prime feeding window",
        defaultPrimaryPhrase = "Check conditions",
        defaultPhrases = listOf(
            "Check conditions",
            "How's the barometer?",
            "What's the water temp?",
            "What's the weather doing?",
            "Is the feeding window open?",
            "Check the weather"
        )
    ),
    CHECK_TACKLE_BOX(
        id = "check_tackle_box",
        title = "Query Digital Tackle Box",
        description = "Checks your stored inventory for matching lures and colors",
        defaultPrimaryPhrase = "What's in my tackle?",
        defaultPhrases = listOf(
            "What's in my tackle?",
            "What's in my tackle box?",
            "Do I have the right lure?",
            "Check my gear",
            "What tackle matches?"
        )
    ),
    BACKUP_PLAN(
        id = "backup_plan",
        title = "Ask for Backup Plan B",
        description = "Alternative presentation if primary pattern stalls",
        defaultPrimaryPhrase = "What's the backup?",
        defaultPhrases = listOf(
            "What's the backup?",
            "What's the backup plan?",
            "What's plan B?",
            "If that won't work?",
            "Switch to backup"
        )
    )
}

data class CustomVoiceTrigger(
    val intent: VoiceActionIntent,
    val primaryPhrase: String,
    val triggerPhrases: List<String>
)

object DefaultVoiceTriggers {
    fun createDefaults(): List<CustomVoiceTrigger> {
        return VoiceActionIntent.values().map { intent ->
            CustomVoiceTrigger(
                intent = intent,
                primaryPhrase = intent.defaultPrimaryPhrase,
                triggerPhrases = intent.defaultPhrases
            )
        }
    }
}
