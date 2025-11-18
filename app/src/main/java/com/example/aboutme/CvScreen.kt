package com.example.aboutme

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val CvBackground = Color(0xFF232633)
private val CvCardBackground = Color(0xFF363843)
private val CvTextPrimary = Color(0xFFF5F5F7)
private val CvTextMuted = Color(0xFF9B9CA1)

@Composable
fun CvScreen(
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current

    val appLang = remember(configuration) {
        val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            configuration.locale
        }
        val code = locale.language.lowercase()
        if (code.startsWith("es")) AppLanguage.ES else AppLanguage.EN
    }

    val t = remember(appLang) { stringsFor(appLang) }

    Box(
        modifier = modifier.background(CvBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = CvCardBackground,
                shape = RoundedCornerShape(24.dp),
                shadowElevation = 12.dp
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = if (appLang == AppLanguage.ES) "Currículum" else "Curriculum Vitae",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = CvTextPrimary
                    )
                    Text(
                        text = "Oscar \"KuriZd\" Zamudio",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CvTextPrimary
                    )
                    Text(
                        text = t.heroRole,
                        fontSize = 13.sp,
                        color = CvTextMuted
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = if (appLang == AppLanguage.ES) {
                            "Desarrollador Full-Stack y Android orientado a construir productos robustos, escalables y con foco en experiencia de usuario."
                        } else {
                            "Full-Stack and Android developer focused on building robust, scalable products with a strong user experience focus."
                        },
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = CvTextPrimary
                    )

                    Spacer(Modifier.height(12.dp))

                    SectionTitle(
                        title = if (appLang == AppLanguage.ES) "Experiencia" else "Experience"
                    )
                    Spacer(Modifier.height(8.dp))
                    ExperienceSection(t)

                    Spacer(Modifier.height(16.dp))

                    SectionTitle(
                        title = if (appLang == AppLanguage.ES) "Habilidades técnicas" else "Technical Skills"
                    )
                    Spacer(Modifier.height(8.dp))
                    SkillsSection(t)

                    Spacer(Modifier.height(16.dp))

                    SectionTitle(
                        title = if (appLang == AppLanguage.ES) "Proyectos destacados" else "Highlighted Projects"
                    )
                    Spacer(Modifier.height(8.dp))
                    PortfolioSection(t)

                    Spacer(Modifier.height(16.dp))

                    SectionTitle(
                        title = if (appLang == AppLanguage.ES) "Contacto" else "Contact"
                    )
                    Spacer(Modifier.height(8.dp))
                    ContactSection(t)
                }
            }
        }
    }
}
