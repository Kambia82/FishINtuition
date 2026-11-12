package com.example.voice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import com.example.data.model.CustomVoiceTrigger
import com.example.data.model.DefaultVoiceTriggers
import com.example.data.model.VoiceActionIntent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class VoiceAssistantManager(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isTtsReady = false

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    private val _lastRecognizedQuery = MutableStateFlow<String?>(null)
    val lastRecognizedQuery: StateFlow<String?> = _lastRecognizedQuery.asStateFlow()

    private val _lastSpokenResponse = MutableStateFlow<String?>(null)
    val lastSpokenResponse: StateFlow<String?> = _lastSpokenResponse.asStateFlow()

    private val _activeCadenceCoachText = MutableStateFlow<String?>(null)
    val activeCadenceCoachText: StateFlow<String?> = _activeCadenceCoachText.asStateFlow()

    private var speechRecognizer: SpeechRecognizer? = null

    init {
        tts = TextToSpeech(context.applicationContext, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.setLanguage(Locale.US)
            tts?.setSpeechRate(0.95f) // Natural, calm instructional tone
            tts?.setPitch(1.0f)
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _isSpeaking.value = true
                }

                override fun onDone(utteranceId: String?) {
                    _isSpeaking.value = false
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    _isSpeaking.value = false
                }
            })
            isTtsReady = true
            Log.d("VoiceAssistant", "TextToSpeech successfully initialized")
        } else {
            Log.e("VoiceAssistant", "TextToSpeech initialization failed with status $status")
        }
    }

    fun speak(text: String) {
        _lastSpokenResponse.value = text
        if (isTtsReady) {
            _isSpeaking.value = true
            val params = Bundle()
            params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "FishINtuition_${System.currentTimeMillis()}")
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, "FishINtuition_${System.currentTimeMillis()}")
        } else {
            Log.w("VoiceAssistant", "TTS not ready yet: $text")
        }
    }

    fun stopSpeaking() {
        tts?.stop()
        _isSpeaking.value = false
    }

    fun startListening(
        promptSuggestion: String = "What are they biting?",
        onCommandRecognized: (String) -> Unit
    ) {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            Log.w("VoiceAssistant", "Speech recognition not available on this device")
            return
        }

        stopSpeaking()

        try {
            speechRecognizer?.destroy()
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                setRecognitionListener(object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {
                        _isListening.value = true
                    }

                    override fun onBeginningOfSpeech() {}
                    override fun onRmsChanged(rmsdB: Float) {}
                    override fun onBufferReceived(buffer: ByteArray?) {}
                    override fun onEndOfSpeech() {
                        _isListening.value = false
                    }

                    override fun onError(error: Int) {
                        _isListening.value = false
                        Log.d("VoiceAssistant", "Speech recognizer error code: $error")
                    }

                    override fun onResults(results: Bundle?) {
                        _isListening.value = false
                        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        if (!matches.isNullOrEmpty()) {
                            val recognized = matches[0]
                            _lastRecognizedQuery.value = recognized
                            onCommandRecognized(recognized)
                        }
                    }

                    override fun onPartialResults(partialResults: Bundle?) {}
                    override fun onEvent(eventType: Int, params: Bundle?) {}
                })
            }

            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
                putExtra(RecognizerIntent.EXTRA_PROMPT, "FishINtuition listening... Ask '$promptSuggestion' or shout 'Fish on!'")
            }

            speechRecognizer?.startListening(intent)
        } catch (e: Exception) {
            Log.e("VoiceAssistant", "Error starting speech recognizer", e)
            _isListening.value = false
        }
    }

    fun stopListening() {
        try {
            speechRecognizer?.stopListening()
        } catch (e: Exception) {
            Log.e("VoiceAssistant", "Error stopping recognizer", e)
        }
        _isListening.value = false
    }

    /**
     * Matches user spoken query against the angler's configured custom voice triggers.
     */
    fun processCustomVoiceCommand(
        query: String,
        targetSpeciesName: String,
        waterName: String,
        recommendedLure: String,
        alternateLure: String? = null,
        topTackleMatched: String?,
        waterTemp: Int,
        barometer: Double,
        pressureTrend: String,
        customTriggers: List<CustomVoiceTrigger> = DefaultVoiceTriggers.createDefaults(),
        onLogCatchRequested: () -> Unit,
        onSpeciesChangeRequested: (String) -> Unit
    ) {
        _lastRecognizedQuery.value = query
        val normalized = query.lowercase(Locale.ROOT)

        fun matches(intent: VoiceActionIntent): Boolean {
            val phrases = customTriggers.find { it.intent == intent }?.triggerPhrases ?: intent.defaultPhrases
            return phrases.any { phrase ->
                val clean = phrase.lowercase(Locale.ROOT).trimEnd('?', '!', '.', ' ')
                normalized.contains(clean)
            }
        }

        when {
            // 1. What are they biting / Lure & presentation recommendation
            matches(VoiceActionIntent.LURE_RECOMMENDATION) -> {
                val boxMatchText = if (topTackleMatched != null) {
                    "From your tackle box, throw your $topTackleMatched. It matches today's clarity perfectly."
                } else {
                    "Throw a $recommendedLure."
                }
                val response = "$boxMatchText At $waterName for $targetSpeciesName, work the 4 to 8 foot drop-offs with steady pauses near shade."
                speak(response)
            }

            // 2. Tough bite / Nothing is biting
            matches(VoiceActionIntent.NOTHING_BITING) -> {
                val response = "Audio Retrieve Coach activated. Slow down your retrieve cadence by half immediately. Count four seconds between rod twitches and let the bait dead-stick on the bottom. If still quiet, downsize your lure profile by one size."
                _activeCadenceCoachText.value = "Slow crawl 2 ft... PAUSE 4 SECONDS... subtle twitch... PAUSE..."
                speak(response)
            }

            // 3. Change cadence / retrieve rhythm
            matches(VoiceActionIntent.CHANGE_CADENCE) -> {
                val response = "Switch to an erratic burn-and-kill rhythm. Turn the reel handle 3 fast rotations, then pause for a 2-count. Most reactive strikes will occur on the sudden deceleration."
                _activeCadenceCoachText.value = "Burn 3 cranks >> KILL & PAUSE 2 SEC >> Repeat"
                speak(response)
            }

            // 4. Fish on! / Log catch
            matches(VoiceActionIntent.LOG_CATCH) -> {
                speak("Fish on! Great catch! I am opening your catch logger now with current time, barometric pressure of $barometer inHg, and water temperature of $waterTemp degrees pre-filled.")
                onLogCatchRequested()
            }

            // 5. Check live conditions
            matches(VoiceActionIntent.CHECK_CONDITIONS) -> {
                val response = "Current conditions at $waterName: Water temperature is $waterTemp degrees Fahrenheit. Barometric pressure is $barometer inches with a $pressureTrend. Prime feeding window is active."
                speak(response)
            }

            // 6. Check digital tackle box
            matches(VoiceActionIntent.CHECK_TACKLE_BOX) -> {
                val response = if (topTackleMatched != null) {
                    "Your best matching gear in your tackle box right now is your $topTackleMatched, ready to cast."
                } else {
                    "Check your tackle box tab to verify you have the recommended lure class for today's clarity."
                }
                speak(response)
            }

            // 7. Backup Plan B
            matches(VoiceActionIntent.BACKUP_PLAN) -> {
                val alt = alternateLure ?: "Texas-Rigged Stick Worm"
                val response = "Backup Plan B: Switch to your $alt. If the aggressive bite has shut down, switching to a slower falling profile often triggers stubborn fish."
                speak(response)
            }

            // Species Quick Voice Switching
            normalized.contains("walleye") -> onSpeciesChangeRequested("walleye")
            normalized.contains("smallmouth") -> onSpeciesChangeRequested("smallmouth_bass")
            normalized.contains("largemouth") -> onSpeciesChangeRequested("largemouth_bass")
            normalized.contains("trout") -> onSpeciesChangeRequested("rainbow_trout")
            normalized.contains("crappie") -> onSpeciesChangeRequested("black_crappie")
            normalized.contains("redfish") -> onSpeciesChangeRequested("redfish")
            normalized.contains("snook") -> onSpeciesChangeRequested("snook")
            normalized.contains("catfish") -> onSpeciesChangeRequested("channel_catfish")
            normalized.contains("pike") -> onSpeciesChangeRequested("northern_pike")

            else -> {
                val response = "NovaCast listening. You can ask: 'What are they biting?', 'Nothing is biting', 'Change cadence', 'Check conditions', or shout 'Fish on!' to log your catch."
                speak(response)
            }
        }
    }

    fun release() {
        try {
            speechRecognizer?.destroy()
            tts?.stop()
            tts?.shutdown()
        } catch (e: Exception) {
            Log.e("VoiceAssistant", "Error shutting down VoiceAssistantManager", e)
        }
    }
}
