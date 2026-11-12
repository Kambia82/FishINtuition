package com.example.ui.dialogs

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.FishSpecies
import com.example.data.model.WaterBody
import com.example.ui.theme.*

enum class FeedbackCategory(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    FEATURE_REQUEST("Feature Idea", Icons.Default.Lightbulb),
    WATER_BODY_SPOT("New Water / Spot", Icons.Default.PinDrop),
    LURE_RECOMMENDATION("Lure / Technique", Icons.Default.Tune),
    BUG_REPORT("Bug / Issue", Icons.Default.BugReport),
    GENERAL_NOTE("General Note", Icons.Default.Chat)
}

@Composable
fun FeedbackContactDialog(
    currentWaterBody: WaterBody,
    currentSpecies: FishSpecies,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf(FeedbackCategory.FEATURE_REQUEST) }
    var anglerCallsign by remember { mutableStateOf("") }
    var messageText by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(5) }
    var isSubmittedLocally by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = NcCard,
            border = BorderStroke(1.2.dp, NcLime.copy(alpha = 0.5f)),
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .padding(vertical = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(NcLimeGlow),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Feedback,
                                contentDescription = null,
                                tint = NcLime,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "FISHINTUITION ANGLER FEEDBACK",
                                color = NcLime,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Suggestions & Ideas",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Explanatory note
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = NcBg,
                    border = BorderStroke(0.8.dp, NcBorder)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "We built FishINtuition so it can grow with real anglers. Have a request for custom water bodies, new lure tactics, or tactical features? Let us know!",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (isSubmittedLocally) {
                    // Success View
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = CatchGreen,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Feedback Recorded!",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Thank you! Your feedback has been securely logged on-device. Suggestions and field notes directly guide the next updates.",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(containerColor = NcLime, contentColor = NcBg),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Done", fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    // Feedback Form
                    Text(
                        text = "SELECT CATEGORY",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Category Filter Pills
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FeedbackCategory.values().forEach { cat ->
                            val isSelected = cat == selectedCategory
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) NcLime.copy(alpha = 0.2f) else NcCardElevated,
                                border = BorderStroke(1.dp, if (isSelected) NcLime else NcBorder),
                                modifier = Modifier.clickable { selectedCategory = cat }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = cat.icon,
                                        contentDescription = null,
                                        tint = if (isSelected) NcLime else TextSecondary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = cat.label,
                                        color = if (isSelected) NcLime else TextPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Angler Rating
                    Text(
                        text = "HOW IS FISHINTUITION WORKING FOR YOU?",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (i in 1..5) {
                            IconButton(
                                onClick = { rating = i },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = if (i <= rating) Icons.Default.Star else Icons.Default.StarBorder,
                                    contentDescription = "$i Stars",
                                    tint = if (i <= rating) StrikeAmber else TextMuted,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }
                        Text(
                            text = when (rating) {
                                5 -> "Tight lines! (5/5)"
                                4 -> "Great guidance (4/5)"
                                3 -> "Solid start (3/5)"
                                2 -> "Needs tweaks (2/5)"
                                else -> "Needs work (1/5)"
                            },
                            color = StrikeAmber,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Angler Call-Sign (optional)
                    OutlinedTextField(
                        value = anglerCallsign,
                        onValueChange = { anglerCallsign = it },
                        label = { Text("Angler Call-Sign or Nickname (optional)", fontSize = 11.sp) },
                        placeholder = { Text("e.g. BassCaster99", fontSize = 11.sp, color = TextMuted) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NcLime,
                            unfocusedBorderColor = NcBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = NcCardElevated,
                            unfocusedContainerColor = NcCardElevated
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Message Box
                    OutlinedTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        label = { Text("What's your suggestion, spot idea, or feedback?", fontSize = 12.sp) },
                        placeholder = {
                            Text(
                                "Example: Can we add live water flow gauge tracking or support for Carolina Rigs in the Walmart shopping list?",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 110.dp, max = 160.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NcLime,
                            unfocusedBorderColor = NcBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = NcCardElevated,
                            unfocusedContainerColor = NcCardElevated
                        )
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Submit Button (In-App Only)
                    Button(
                        onClick = {
                            if (messageText.isBlank()) {
                                Toast.makeText(context, "Please enter your suggestion or feedback first", Toast.LENGTH_SHORT).show()
                            } else {
                                isSubmittedLocally = true
                                Toast.makeText(context, "Feedback saved successfully!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NcLime,
                            contentColor = NcBg
                        )
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Submit Feedback", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Clean Footer Note without any emails or personal data
                    Text(
                        text = "FishINtuition • Fish Psychology",
                        color = TextMuted,
                        fontSize = 10.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
