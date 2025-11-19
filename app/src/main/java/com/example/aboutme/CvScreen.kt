package com.example.aboutme

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private val CvBackground = Color(0xFF232633)
private val CvCardBackground = Color(0xFFF5F5F7)
private val CvSidebarBackground = Color(0xFF1F3B63)
private val CvSidebarAccent = Color(0xFF173257)
private val CvTextPrimaryDark = Color(0xFF111827)
private val CvTextMutedDark = Color(0xFF4B5563)
private val CvTextOnSidebar = Color(0xFFF9FAFB)
private val CvDivider = Color(0xFFE5E7EB)
private val CvBullet = Color(0xFF1F3B63)

private val CvNeon = Color(0xFF22F3B7)
private val CvNeonBg = Color(0xFF072824)
private val CvDarkPanel = Color(0xFF050816)
private val CvCardBg = Color(0xFF151B28)
private val CvCardBgAlt = Color(0xFF111623)
private val CvTextBright = Color(0xFFEFF2F6)
private val CvTextDim = Color(0xFF98A2B3)
private val CvNeonGlow = Color(0x8038D4AA)

private val CvTimelineBlue = Color(0xFF2563EB)

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
    val isWide = configuration.screenWidthDp >= 700

    Box(
        modifier = modifier
            .background(CvBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 960.dp),
                color = CvCardBackground,
                shape = RoundedCornerShape(20.dp),
                shadowElevation = 10.dp
            ) {
                if (isWide) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 520.dp)
                    ) {
                        CvSidebar(appLang = appLang, modifier = Modifier.weight(0.35f))
                        CvMainContent(appLang = appLang, t = t, modifier = Modifier.weight(0.65f))
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CvSidebar(
                            appLang = appLang,
                            modifier = Modifier.fillMaxWidth()
                        )
                        CvMainContent(
                            appLang = appLang,
                            t = t,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CvSidebar(
    appLang: AppLanguage,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(CvSidebarBackground)
            .padding(vertical = 20.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.my),
            contentDescription = "Profile picture",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        )

        Spacer(Modifier.height(20.dp))

        CvSidebarSectionTitle(
            text = if (appLang == AppLanguage.ES) "Contacto" else "Contact"
        )

        Spacer(Modifier.height(8.dp))

        CvContactItem(
            icon = Icons.Default.Phone,
            label = "+52 443 369 3513"
        )
        CvContactItem(
            icon = Icons.Default.Email,
            label = "kurizd@protonmail.com"
        )
        CvContactItem(
            icon = Icons.Default.Language,
            label = "github.com/KuriZd"
        )

        Spacer(Modifier.height(20.dp))

        CvSidebarSectionTitle(
            text = if (appLang == AppLanguage.ES) "Habilidades" else "Soft Skills"
        )

        Spacer(Modifier.height(6.dp))

        val softSkillsEs = listOf(
            "Liderazgo",
            "Comunicación asertiva",
            "Gestión de activos",
            "Resolución de problemas",
            "Elaboración de reportes",
            "Trabajo en equipo"
        )

        val softSkillsEn = listOf(
            "Leadership",
            "Assertive communication",
            "Asset management",
            "Problem solving",
            "Reporting",
            "Teamwork"
        )

        val skills = if (appLang == AppLanguage.ES) softSkillsEs else softSkillsEn

        skills.forEach {
            CvSidebarBullet(text = it)
        }

        Spacer(Modifier.height(20.dp))

        CvSidebarSectionTitle(
            text = if (appLang == AppLanguage.ES) "Educación" else "Education"
        )

        Spacer(Modifier.height(8.dp))

        CvEducationBlock(appLang = appLang)
    }
}

@Composable
fun CvSidebarSectionTitle(text: String) {
    Text(
        text = text.uppercase(),
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = CvTextOnSidebar
    )
    Spacer(Modifier.height(4.dp))
    Box(
        modifier = Modifier
            .height(1.dp)
            .fillMaxWidth(0.6f)
            .background(CvSidebarAccent)
    )
}

@Composable
fun CvContactItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(CvSidebarAccent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CvTextOnSidebar,
                modifier = Modifier.size(14.dp)
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = CvTextOnSidebar
        )
    }
}

@Composable
fun CvSidebarBullet(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Text(
            text = "•",
            fontSize = 11.sp,
            color = CvTextOnSidebar
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = text,
            fontSize = 11.sp,
            color = CvTextOnSidebar,
            lineHeight = 14.sp
        )
    }
}

@Composable
fun CvEducationBlock(appLang: AppLanguage) {
    val title = if (appLang == AppLanguage.ES) {
        "Ingeniería en Sistemas Computacionales"
    } else {
        "B.Sc. in Computer Systems Engineering"
    }
    val school = "Instituto Tecnológico de Morelia"
    val years = "2019 – 2024"

    Text(
        text = title,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        color = CvTextOnSidebar
    )
    Spacer(Modifier.height(2.dp))
    Text(
        text = school,
        fontSize = 10.sp,
        color = CvTextOnSidebar.copy(alpha = 0.9f)
    )
    Spacer(Modifier.height(2.dp))
    Text(
        text = years,
        fontSize = 10.sp,
        color = CvTextOnSidebar.copy(alpha = 0.8f)
    )
}

@Composable
fun CvMainContent(
    appLang: AppLanguage,
    t: UiStrings,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Text(
            text = "Oscar Kuricaveri",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = CvTextPrimaryDark
        )
        Text(
            text = if (appLang == AppLanguage.ES) {
                "Ing. Sistemas Computacionales"
            } else {
                "Computer Systems Engineer"
            },
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = CvTextMutedDark
        )

        Spacer(Modifier.height(16.dp))

        CvMainSectionTitle(
            text = if (appLang == AppLanguage.ES) "Acerca de mí" else "About me"
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = if (appLang == AppLanguage.ES) {
                "Profesional en desarrollo web y móvil con experiencia en diseño e implementación de aplicaciones tanto en el frontend como en el backend. Apasionado por las nuevas tecnologías, el código limpio y la entrega de soluciones confiables en entornos desafiantes."
            } else {
                "Web and mobile developer with experience designing and implementing frontend and backend applications. Passionate about new technologies, clean code and delivering reliable solutions in challenging environments."
            },
            fontSize = 12.sp,
            lineHeight = 18.sp,
            color = CvTextMutedDark
        )

        Spacer(Modifier.height(18.dp))

        CvMainSectionTitle(
            text = if (appLang == AppLanguage.ES) "Principios de trabajo" else "Principles"
        )

        Spacer(Modifier.height(8.dp))

        CvPrinciplesSection(appLang = appLang)

        Spacer(Modifier.height(18.dp))

        CvMainSectionTitle(
            text = if (appLang == AppLanguage.ES) "Experiencia laboral" else "Work experience"
        )

        Spacer(Modifier.height(10.dp))

        CvExperienceTimeline(appLang = appLang)

        Spacer(Modifier.height(18.dp))

        CvMainSectionTitle(
            text = if (appLang == AppLanguage.ES) "Proyectos seleccionados" else "Selected projects"
        )

        Spacer(Modifier.height(8.dp))

        CvProjectsCompact(appLang = appLang, t = t)
    }
}

@Composable
fun CvMainSectionTitle(text: String) {
    Text(
        text = text.uppercase(),
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = CvTextPrimaryDark
    )
    Spacer(Modifier.height(4.dp))
    Box(
        modifier = Modifier
            .height(1.dp)
            .fillMaxWidth(0.8f)
            .background(CvDivider)
    )
}

data class CvJob(
    val periodEs: String,
    val periodEn: String,
    val roleEs: String,
    val roleEn: String,
    val company: String,
    val locationEs: String,
    val locationEn: String,
    val bulletsEs: List<String>,
    val bulletsEn: List<String>
)

@Composable
fun CvExperienceTimeline(appLang: AppLanguage) {
    val jobs = listOf(
        CvJob(
            periodEs = "Mar 2019 – Jul 2020",
            periodEn = "Mar 2019 – Jul 2020",
            roleEs = "Administrador/a de Sistemas",
            roleEn = "Systems Administrator",
            company = "Dolce Porto",
            locationEs = "Presencial · Morelia, México",
            locationEn = "On-site · Morelia, Mexico",
            bulletsEs = listOf(
                "Automatización de tareas y procesos mediante scripts para optimizar el rendimiento del sistema.",
                "Administración y mantenimiento de infraestructura para garantizar la disponibilidad del sistema."
            ),
            bulletsEn = listOf(
                "Automated tasks and processes through scripts to improve overall system performance.",
                "Managed and maintained infrastructure to ensure system uptime and reliability."
            )
        )
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFF9FAFF),
        shape = RoundedCornerShape(18.dp),
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            jobs.forEachIndexed { index, job ->
                CvExperienceTimelineItem(
                    job = job,
                    appLang = appLang,
                    isFirst = index == 0,
                    isLast = index == jobs.lastIndex
                )
            }
        }
    }
}

@Composable
fun CvExperienceTimelineItem(
    job: CvJob,
    appLang: AppLanguage,
    isFirst: Boolean,
    isLast: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.width(92.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (appLang == AppLanguage.ES) job.periodEs else job.periodEn,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = CvTextMutedDark
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = if (appLang == AppLanguage.ES) job.locationEs else job.locationEn,
                fontSize = 10.sp,
                color = CvTextMutedDark.copy(alpha = 0.85f)
            )
        }

        Spacer(Modifier.width(10.dp))

        Column(
            modifier = Modifier.width(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!isFirst) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(14.dp)
                        .background(CvTimelineBlue.copy(alpha = 0.5f))
                )
            }
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .border(
                        width = 2.dp,
                        color = CvTimelineBlue,
                        shape = CircleShape
                    )
                    .background(Color.White)
            )
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(30.dp)
                        .background(CvTimelineBlue.copy(alpha = 0.5f))
                )
            }
        }

        Spacer(Modifier.width(10.dp))

        val title = if (appLang == AppLanguage.ES) job.roleEs else job.roleEn
        val location = if (appLang == AppLanguage.ES) job.locationEs else job.locationEn
        val bullets = if (appLang == AppLanguage.ES) job.bulletsEs else job.bulletsEn

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "$title — ${job.company}",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = CvTextPrimaryDark
            )
            Text(
                text = location,
                fontSize = 11.sp,
                color = CvTextMutedDark
            )
            Spacer(Modifier.height(4.dp))
            bullets.forEach { bullet ->
                CvExperienceBullet(text = bullet)
            }
        }
    }
}

@Composable
fun CvExperienceBullet(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Text(
            text = "•",
            fontSize = 12.sp,
            color = CvTextPrimaryDark
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            lineHeight = 18.sp,
            color = CvTextMutedDark
        )
    }
}

@Composable
fun CvProjectsCompact(
    appLang: AppLanguage,
    t: UiStrings
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "RentIt App",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = CvTextPrimaryDark
        )
        Text(
            text = if (appLang == AppLanguage.ES) {
                "Aplicación móvil para renta de artículos entre personas, con autenticación, carritos y manejo de inventario usando React Native y Supabase."
            } else {
                "Mobile app for peer-to-peer item rentals, with authentication, carts and inventory using React Native and Supabase."
            },
            fontSize = 12.sp,
            lineHeight = 18.sp,
            color = CvTextMutedDark
        )

        Text(
            text = "Glucose Tracker",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = CvTextPrimaryDark
        )
        Text(
            text = if (appLang == AppLanguage.ES) {
                "Suite para registrar glucosa, insulina, agua y citas médicas, con dashboard web y exportación a PDF."
            } else {
                "Suite to track glucose, insulin, water intake and medical appointments, with web dashboard and PDF exports."
            },
            fontSize = 12.sp,
            lineHeight = 18.sp,
            color = CvTextMutedDark
        )
    }
}

data class Principle(
    val title: String,
    val description: String
)

@Composable
fun CvPrinciplesSection(appLang: AppLanguage) {
    val principles = if (appLang == AppLanguage.ES) {
        listOf(
            Principle("Diseño responsivo", "Experiencias fluidas en móvil, tablet y escritorio."),
            Principle("Stack moderno", "Tecnologías actuales para desarrollo web y móvil."),
            Principle("Rendimiento", "Interfaces rápidas y optimizadas."),
            Principle("Accesibilidad", "Productos inclusivos para todos."),
            Principle("Diseño visual", "Experiencias atractivas que comunican."),
            Principle("Experiencia Dev", "Código mantenible y testeable.")
        )
    } else {
        listOf(
            Principle("Responsive Design", "Seamless experiences across devices."),
            Principle("Modern Stack", "Up-to-date web and mobile tools."),
            Principle("Performance", "Fast and optimized UX."),
            Principle("Accessibility", "Inclusive and universal."),
            Principle("Visual Design", "Clean and engaging interfaces."),
            Principle("Developer Experience", "Maintainable and testable code.")
        )
    }

    var highlighted by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            highlighted = (highlighted + 1) % principles.size
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CvDarkPanel, RoundedCornerShape(18.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        principles.forEachIndexed { index, item ->
            PrincipleCard(
                principle = item,
                highlight = (index == highlighted)
            )
        }
    }
}

@Composable
fun PrincipleCard(
    principle: Principle,
    highlight: Boolean
) {
    val bg = if (highlight) CvCardBgAlt else CvCardBg
    val borderColor = if (highlight) CvNeon else Color(0xFF1E2533)
    val glow = if (highlight) CvNeonGlow else Color.Transparent

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(bg, RoundedCornerShape(16.dp))
            .border(
                width = if (highlight) 1.8.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .shadow(
                elevation = if (highlight) 25.dp else 0.dp,
                ambientColor = glow,
                spotColor = glow,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = principle.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (highlight) CvNeon else CvTextBright
            )
            Text(
                text = principle.description,
                fontSize = 11.sp,
                lineHeight = 15.sp,
                color = CvTextDim
            )
        }
    }
}
