package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.CustomVoiceTrigger
import com.example.data.model.DefaultVoiceTriggers
import com.example.data.model.VoiceActionIntent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class VoiceCommandPreferences(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("novacast_voice_triggers", Context.MODE_PRIVATE)

    private val _triggersFlow = MutableStateFlow<List<CustomVoiceTrigger>>(loadTriggers())
    val triggersFlow: StateFlow<List<CustomVoiceTrigger>> = _triggersFlow.asStateFlow()

    fun getTriggers(): List<CustomVoiceTrigger> {
        return _triggersFlow.value
    }

    private fun loadTriggers(): List<CustomVoiceTrigger> {
        val defaults = DefaultVoiceTriggers.createDefaults()
        return defaults.map { defaultItem ->
            val intent = defaultItem.intent
            val savedPrimary = prefs.getString("primary_${intent.id}", defaultItem.primaryPhrase) ?: defaultItem.primaryPhrase
            val savedPhrasesSet = prefs.getStringSet("phrases_${intent.id}", null)

            val phrases = if (savedPhrasesSet != null && savedPhrasesSet.isNotEmpty()) {
                savedPhrasesSet.toList()
            } else {
                defaultItem.triggerPhrases
            }

            // Ensure primary phrase is always present in phrases list
            val mergedPhrases = if (phrases.contains(savedPrimary)) phrases else listOf(savedPrimary) + phrases

            CustomVoiceTrigger(
                intent = intent,
                primaryPhrase = savedPrimary,
                triggerPhrases = mergedPhrases
            )
        }
    }

    fun addPhrase(intent: VoiceActionIntent, newPhrase: String) {
        val trimmed = newPhrase.trim()
        if (trimmed.isBlank()) return

        val currentList = _triggersFlow.value.toMutableList()
        val index = currentList.indexOfFirst { it.intent == intent }
        if (index != -1) {
            val item = currentList[index]
            if (!item.triggerPhrases.any { it.equals(trimmed, ignoreCase = true) }) {
                val updatedPhrases = item.triggerPhrases + trimmed
                val updatedItem = item.copy(triggerPhrases = updatedPhrases)
                currentList[index] = updatedItem
                saveToDisk(currentList)
                _triggersFlow.value = currentList
            }
        }
    }

    fun removePhrase(intent: VoiceActionIntent, phraseToRemove: String) {
        val currentList = _triggersFlow.value.toMutableList()
        val index = currentList.indexOfFirst { it.intent == intent }
        if (index != -1) {
            val item = currentList[index]
            // Don't allow removing if it's the only phrase left
            if (item.triggerPhrases.size <= 1) return

            val updatedPhrases = item.triggerPhrases.filterNot { it.equals(phraseToRemove, ignoreCase = true) }
            val updatedPrimary = if (item.primaryPhrase.equals(phraseToRemove, ignoreCase = true)) {
                updatedPhrases.first()
            } else {
                item.primaryPhrase
            }

            val updatedItem = item.copy(
                primaryPhrase = updatedPrimary,
                triggerPhrases = updatedPhrases
            )
            currentList[index] = updatedItem
            saveToDisk(currentList)
            _triggersFlow.value = currentList
        }
    }

    fun setPrimaryPhrase(intent: VoiceActionIntent, primaryPhrase: String) {
        val trimmed = primaryPhrase.trim()
        if (trimmed.isBlank()) return

        val currentList = _triggersFlow.value.toMutableList()
        val index = currentList.indexOfFirst { it.intent == intent }
        if (index != -1) {
            val item = currentList[index]
            val updatedPhrases = if (item.triggerPhrases.contains(trimmed)) {
                item.triggerPhrases
            } else {
                item.triggerPhrases + trimmed
            }

            val updatedItem = item.copy(
                primaryPhrase = trimmed,
                triggerPhrases = updatedPhrases
            )
            currentList[index] = updatedItem
            saveToDisk(currentList)
            _triggersFlow.value = currentList
        }
    }

    fun resetToDefaults() {
        prefs.edit().clear().apply()
        val defaults = DefaultVoiceTriggers.createDefaults()
        _triggersFlow.value = defaults
    }

    private fun saveToDisk(triggers: List<CustomVoiceTrigger>) {
        val editor = prefs.edit()
        triggers.forEach { trigger ->
            editor.putString("primary_${trigger.intent.id}", trigger.primaryPhrase)
            editor.putStringSet("phrases_${trigger.intent.id}", trigger.triggerPhrases.toSet())
        }
        editor.apply()
    }
}
