package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberBorder
import com.example.ui.theme.AmberContainer
import com.example.ui.theme.AmberText
import com.example.ui.theme.AmberWarning

data class CribTopic(
    val id: String,
    val name: String,
    val formulas: List<String>,
    val steps: List<String>,
    val pitfalls: List<String>,
    val checkQuestions: List<String>
)

val CRIB_TOPICS = listOf(
    CribTopic(
        id = "quad",
        name = "Квадрат теңдеу (ҰБТ)",
        formulas = listOf(
            "ax² + bx + c = 0, a ≠ 0",
            "D = b² - 4ac",
            "x₁,₂ = (-b ± √D) / 2a",
            "Виет: x₁ + x₂ = -b/a,  x₁·x₂ = c/a"
        ),
        steps = listOf(
            "1. D мәнін есептеп, түбір санын (D > 0, D = 0, D < 0) анықтау",
            "2. Егер a=1 болса, Виет теоремасымен ауызша тексерту",
            "3. Табылған түбірлерді теңдеуге қойып тексеру"
        ),
        pitfalls = listOf(
            "⚠️ D < 0 болғанда 'шешімі жоқ' деудің орнына 0 деп жазу",
            "⚠️ -b таңбасын жоғалтып алу",
            "⚠️ Виет теоремасында таңбаны шатастыру (-p және +q)"
        ),
        checkQuestions = listOf(
            "❓ 'a коэффициенті неге нөл болмауы керек?'",
            "❓ 'Егер D = 0 болса, парабола графигі Ox осіне қалай орналасады?'"
        )
    ),
    CribTopic(
        id = "deriv",
        name = "Туынды және экстремумдар (ҰБТ)",
        formulas = listOf(
            "(xⁿ)' = n · xⁿ⁻¹",
            "(u · v)' = u'v + uv'",
            "(u / v)' = (u'v - uv') / v²",
            "(c)' = 0,  (kx)' = k"
        ),
        steps = listOf(
            "1. Функцияның түрін анықтау (дәрежелік, көбейтінді, бөлшек)",
            "2. Кестелік туынды ережесін қадамдап жазғызу",
            "3. Тұрақты көбейткішті алға шығару"
        ),
        pitfalls = listOf(
            "⚠️ Күрделі функцияда ішкі туындыны көбейтуді ұмыту",
            "⚠️ Тұрақты санның туындысын 1 деп жазу"
        ),
        checkQuestions = listOf(
            "❓ 'Туындының геометриялық мағынасы қандай?'",
            "❓ 'f'(x) = 0 нүктелері графикте нені білдіреді?'"
        )
    ),
    CribTopic(
        id = "seq",
        name = "Сандық қатарлар (НИШ)",
        formulas = listOf(
            "Арифметикалық: aₙ = a₁ + (n - 1)d",
            "Геометриялық: bₙ = b₁ · qⁿ⁻¹",
            "Sₙ = (a₁ + aₙ) / 2 · n"
        ),
        steps = listOf(
            "1. Көршілес мүшелер айырымы мен қатынасын тексеру",
            "2. Заңдылықты оқушыға өз сөзімен айтқызу",
            "3. 5-ші және 10-шы мүшесін формуласыз ауызша бағалату"
        ),
        pitfalls = listOf(
            "⚠️ Айырым мен еселікті шатастыру",
            "⚠️ n мен n-1 дәрежесін ауыстырып алу"
        ),
        checkQuestions = listOf(
            "❓ 'Айырым тұрақты ма, әлде көбейтінді ме?'",
            "❓ 'Заңдылықты 10 секунд ішінде ауызша айта аласың ба?'"
        )
    ),
    CribTopic(
        id = "geom",
        name = "Пифагор және фигуралар (БИЛ)",
        formulas = listOf(
            "c² = a² + b² (Пифагор)",
            "S = ½ · a · b (тікбұрышты үшбұрыш)",
            "S = √[p(p-a)(p-b)(p-c)] (Герон)"
        ),
        steps = listOf(
            "1. Берілген шартты тақтаға сызба етіп түсіру",
            "2. Египет үшбұрышын (3, 4, 5 немесе 6, 8, 10) байқату",
            "3. Гипотенуза әрқашан ең ұзын қабырға екенін тексеру"
        ),
        pitfalls = listOf(
            "⚠️ Тік бұрышқа қарсы жатпаған қабырғаны гипотенуза деп санау",
            "⚠️ Квадрат түбірден шығару кезінде қателесу"
        ),
        checkQuestions = listOf(
            "❓ 'Егер екі катет 6 және 8 болса, гипотенуза 14-тен үлкен болуы мүмкін бе?'",
            "❓ 'Пифагор теоремасы барлық үшбұрышқа жүре ме?'"
        )
    )
)

@Composable
fun TeacherCribCard(
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(true) }
    var selectedTopicId by remember { mutableStateOf("quad") }
    val currentTopic = CRIB_TOPICS.firstOrNull { it.id == selectedTopicId } ?: CRIB_TOPICS.first()

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("teacher_crib_card"),
        shape = RoundedCornerShape(18.dp),
        color = AmberContainer,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, AmberBorder),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(AmberWarning.copy(alpha = 0.25f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = AmberText,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "🔒 Тек репетиторға",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmberText
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Әдістемелік сүйемел",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF5A3600)
                    )
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = AmberText
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    // Topic selector chips
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        CRIB_TOPICS.forEach { topic ->
                            val isSelected = topic.id == selectedTopicId
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { selectedTopicId = topic.id },
                                color = if (isSelected) AmberWarning else Color.White,
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) AmberWarning else AmberBorder
                                )
                            ) {
                                Text(
                                    text = topic.name,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else AmberText,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Formulas Grid
                    Text(
                        text = "НЕГІЗГІ ФОРМУЛАЛАР:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AmberText,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        currentTopic.formulas.forEach { f ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color.White,
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF6E4C8)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = f,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.5.sp,
                                    color = Color(0xFF1E243A)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Steps
                    Text(
                        text = "ТҮСІНДІРУ ҚАДАМДАРЫ:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AmberText,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    currentTopic.steps.forEach { step ->
                        Text(
                            text = step,
                            fontSize = 12.5.sp,
                            color = Color(0xFF374151),
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Common pitfalls
                    Text(
                        text = "ЖИІ КЕЗДЕСЕТІН ҚАТЕЛЕР:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFB91C1C),
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    currentTopic.pitfalls.forEach { p ->
                        Text(
                            text = p,
                            fontSize = 12.sp,
                            color = Color(0xFF991B1B),
                            modifier = Modifier.padding(vertical = 1.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Questions
                    Text(
                        text = "ОҚУШЫҒА СҰРАҚТАР:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1D4ED8),
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    currentTopic.checkQuestions.forEach { q ->
                        Text(
                            text = q,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF1E3A8A),
                            modifier = Modifier.padding(vertical = 1.dp)
                        )
                    }
                }
            }
        }
    }
}
