package com.example.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.ScreenShare
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MintSecondary

@Composable
fun VideoCallBar(
    modifier: Modifier = Modifier,
    isTeacherRole: Boolean,
    onToggleRole: () -> Unit
) {
    var isMicOn by remember { mutableStateOf(true) }
    var isCamOn by remember { mutableStateOf(true) }
    var isScreenSharing by remember { mutableStateOf(false) }

    // Sound wave pulse animation
    val infiniteTransition = rememberInfiniteTransition(label = "audio_pulse")
    val waveHeight1 by infiniteTransition.animateFloat(
        initialValue = 4f,
        targetValue = 18f,
        animationSpec = infiniteRepeatable(tween(400), RepeatMode.Reverse),
        label = "w1"
    )
    val waveHeight2 by infiniteTransition.animateFloat(
        initialValue = 14f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(tween(550), RepeatMode.Reverse),
        label = "w2"
    )
    val waveHeight3 by infiniteTransition.animateFloat(
        initialValue = 8f,
        targetValue = 22f,
        animationSpec = infiniteRepeatable(tween(350), RepeatMode.Reverse),
        label = "w3"
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFF161A2E),
        shadowElevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Tutor Tile
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(listOf(IndigoPrimary, Color(0xFF8B7BFF)))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCamOn) {
                        Text(
                            text = "Е",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.VideocamOff,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Green live dot
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(2.dp)
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(MintSecondary)
                            .border(1.dp, Color(0xFF161A2E), CircleShape)
                    )
                }

                Column {
                    Text(
                        text = if (isTeacherRole) "Сіз (Репетитор)" else "Еркем (Мұғалім)",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.5.sp
                    )
                    Text(
                        text = if (isMicOn) "Сөйлеп тұр" else "Микрофон өшірулі",
                        color = if (isMicOn) Color(0xFF7DF0CE) else Color(0xFF9EA6C6),
                        fontSize = 11.sp
                    )
                }
            }

            // Student Tile
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(listOf(MintSecondary, Color(0xFF0E9E7B)))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "А",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    // Live audio wave indicator
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 3.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Box(modifier = Modifier.width(3.dp).height(waveHeight1.dp).background(Color.White, RoundedCornerShape(2.dp)))
                        Box(modifier = Modifier.width(3.dp).height(waveHeight2.dp).background(Color.White, RoundedCornerShape(2.dp)))
                        Box(modifier = Modifier.width(3.dp).height(waveHeight3.dp).background(Color.White, RoundedCornerShape(2.dp)))
                    }
                }

                Column {
                    Text(
                        text = if (!isTeacherRole) "Сіз (Оқушы)" else "Айсұлу (11-сынып)",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.5.sp
                    )
                    Text(
                        text = "Онлайн ҰБТ",
                        color = Color(0xFFA5B4FC),
                        fontSize = 11.sp
                    )
                }
            }

            // Controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                IconButton(
                    onClick = { isMicOn = !isMicOn },
                    modifier = Modifier.size(34.dp).testTag("call_mic_btn")
                ) {
                    Icon(
                        imageVector = if (isMicOn) Icons.Default.Mic else Icons.Default.MicOff,
                        contentDescription = "Mic",
                        tint = if (isMicOn) Color.White else Color(0xFFFF5C72),
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = { isCamOn = !isCamOn },
                    modifier = Modifier.size(34.dp).testTag("call_cam_btn")
                ) {
                    Icon(
                        imageVector = if (isCamOn) Icons.Default.Videocam else Icons.Default.VideocamOff,
                        contentDescription = "Cam",
                        tint = if (isCamOn) Color.White else Color(0xFFFF5C72),
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = { isScreenSharing = !isScreenSharing },
                    modifier = Modifier.size(34.dp).testTag("call_screenshare_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.ScreenShare,
                        contentDescription = "Screen",
                        tint = if (isScreenSharing) MintSecondary else Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Role switcher pill
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(onClick = onToggleRole)
                        .testTag("toggle_role_btn"),
                    color = if (isTeacherRole) Color(0xFF283156) else Color(0xFF0F4D3C),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (isTeacherRole) "👩‍🏫 Мұғалім" else "🎓 Оқушы",
                        color = if (isTeacherRole) Color(0xFFC7D2FE) else Color(0xFFA7F3D0),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}
