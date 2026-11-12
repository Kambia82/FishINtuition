package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.CustomVoiceTrigger
import com.example.data.model.VoiceActionIntent
import com.example.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VoiceCommandsCustomizerDialog(
    triggers: List<CustomVoiceTrigger>,
    onAddPhrase: (VoiceActionIntent, String) -> Unit,
    onRemovePhrase: (VoiceActionIntent, String) -> Unit,
    onSetPrimaryPhrase: (VoiceActionIntent, String) -> Unit,
    onResetDefaults: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MarineSurface,
            border = androidx.compose.foundation.BorderStroke(1.5.dp, HydroCyan),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .testTag("voice_commands_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(HydroCyan.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.GraphicEq,
                                contentDescription = null,
                                tint = HydroCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "CUSTOMIZE VOICE TRIGGERS",
                                color = HydroCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Your Natural Phrasing",
                                color = TextPrimary,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "No angler speaks like a robot. Add your own phrases, slang, or questions. Tap any phrase to make it your primary button label on the water.",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = MarineOutlineSubtle)
                Spacer(modifier = Modifier.height(10.dp))

                // Triggers List
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(triggers, key = { it.intent.id }) { trigger ->
                        VoiceIntentCard(
                            trigger = trigger,
                            onAddPhrase = { phrase -> onAddPhrase(trigger.intent, phrase) },
                            onRemovePhrase = { phrase -> onRemovePhrase(trigger.intent, phrase) },
                            onSetPrimary = { phrase -> onSetPrimaryPhrase(trigger.intent, phrase) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = MarineOutlineSubtle)
                Spacer(modifier = Modifier.height(12.dp))

                // Bottom Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onResetDefaults,
                        colors = ButtonDefaults.textButtonColors(contentColor = TextMuted)
                    ) {
                        Icon(Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Reset to Defaults", fontSize = 12.sp)
                    }

                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HydroCyan,
                            contentColor = MarineDeep
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        Text("Done", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun VoiceIntentCard(
    trigger: CustomVoiceTrigger,
    onAddPhrase: (String) -> Unit,
    onRemovePhrase: (String) -> Unit,
    onSetPrimary: (String) -> Unit
) {
    var newPhraseText by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MarineSurfaceVariant),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MarineOutline)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Title & Description
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = trigger.intent.title,
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = HydroCyan.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "${trigger.triggerPhrases.size} Recognized",
                        color = HydroCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = trigger.intent.description,
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Primary display chip indicator
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Primary Button Label: ",
                    color = TextMuted,
                    fontSize = 11.sp
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = CatchGreen.copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CatchGreen)
                ) {
                    Text(
                        text = "\"${trigger.primaryPhrase}\"",
                        color = CatchGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // FlowRow of Trigger Phrases
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                trigger.triggerPhrases.forEach { phrase ->
                    val isPrimary = phrase.equals(trigger.primaryPhrase, ignoreCase = true)
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isPrimary) HydroCyan.copy(alpha = 0.25f) else MarineSurface,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isPrimary) HydroCyan else MarineOutlineSubtle
                        ),
                        modifier = Modifier.clickable {
                            onSetPrimary(phrase)
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isPrimary) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Primary",
                                    tint = HydroCyan,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            Text(
                                text = phrase,
                                color = if (isPrimary) HydroCyanLight else TextPrimary,
                                fontSize = 11.sp,
                                fontWeight = if (isPrimary) FontWeight.Bold else FontWeight.Normal
                            )
                            if (trigger.triggerPhrases.size > 1) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove phrase",
                                    tint = TextMuted,
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clickable { onRemovePhrase(phrase) }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Add phrase inline field
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newPhraseText,
                    onValueChange = { newPhraseText = it },
                    placeholder = {
                        Text(
                            text = "Add phrase (e.g. \"${trigger.intent.defaultPhrases.firstOrNull() ?: "What's hitting?"}\")",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = HydroCyan,
                        unfocusedBorderColor = MarineOutlineSubtle,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = MarineSurface,
                        unfocusedContainerColor = MarineSurface
                    ),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                FilledTonalButton(
                    onClick = {
                        if (newPhraseText.isNotBlank()) {
                            onAddPhrase(newPhraseText.trim())
                            newPhraseText = ""
                        }
                    },
                    enabled = newPhraseText.isNotBlank(),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = HydroCyan,
                        contentColor = MarineDeep,
                        disabledContainerColor = MarineSurface,
                        disabledContentColor = TextMuted
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
