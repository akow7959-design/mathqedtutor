package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MintSecondary

data class LessonStage(
    val id: Int,
    val title: String,
    val durationMin: Int,
    val isDone: Boolean,
    val isActive: Boolean
)

val DEFAULT_STAGES = listOf(
    LessonStage(1, "Қайталау", 5, isDone = true, isActive = false),
    LessonStage(2, "Жаңа тақырып", 15, isDone = false, isActive = true),
    LessonStage(3, "Бірге шығару", 20, isDone = false, isActive = false),
    LessonStage(4, "Өздік жұмыс", 10, isDone = false, isActive = false),
    LessonStage(5, "Үй тапсырмасы", 5, isDone = false, isActive = false)
)

@Composable
fun LessonPlanTimeline(
    modifier: Modifier = Modifier,
    activeStageId: Int,
    onStageSelected: (Int) -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E9F7)),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "САБАҚ ЖОСПАРЫ (VIMBOX):",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6B7280),
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "45 минуттан 20 мин өтті",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = IndigoPrimary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DEFAULT_STAGES.forEach { stage ->
                    val isActive = stage.id == activeStageId
                    val isDone = stage.id < activeStageId

                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onStageSelected(stage.id) },
                        shape = RoundedCornerShape(12.dp),
                        color = when {
                            isActive -> Color(0xFFEEF0FF)
                            isDone -> Color(0xFFF0FDF4)
                            else -> Color(0xFFF9FAFD)
                        },
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            when {
                                isActive -> IndigoPrimary
                                isDone -> MintSecondary
                                else -> Color(0xFFE2E6F5)
                            }
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isActive -> IndigoPrimary
                                            isDone -> MintSecondary
                                            else -> Color(0xFFDCE2F5)
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isDone) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(12.dp)
                                    )
                                } else {
                                    Text(
                                        text = "${stage.id}",
                                        color = if (isActive) Color.White else Color(0xFF4B5563),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Column {
                                Text(
                                    text = stage.title,
                                    fontSize = 12.sp,
                                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                                    color = when {
                                        isActive -> IndigoPrimary
                                        isDone -> Color(0xFF047857)
                                        else -> Color(0xFF374151)
                                    }
                                )
                                Text(
                                    text = "${stage.durationMin} мин",
                                    fontSize = 10.sp,
                                    color = Color(0xFF9CA3AF)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
