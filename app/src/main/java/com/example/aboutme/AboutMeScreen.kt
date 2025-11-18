package com.example.aboutme

import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Api
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DesignServices
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalConfiguration
import java.util.Locale

private val CeladonBlue = Color(0xFF1783A5)
private val Onyx = Color(0xFF363843)
private val AuroMetalSaurus = Color(0xFF727584)
private val Manatee = Color(0xFF9B9CA1)
private val TextPrimary = Color(0xFFF5F5F7)
private val TextMuted = Manatee

private val ReactNeon = Color(0xFF61DAFB)   // React

private val KotlinNeon = Color(0xFF7F52FF)  // Kotlin / Compose

private val NodeNeon = Color(0xFF3ECF8E)    // Node / Supabase


enum class AppLanguage { ES, EN }

data class UiStrings(
    val profileTitle: String,
    val heroRole: String,
    val heroDescription: String,
    val heroChip1: String,
    val heroChip2: String,
    val heroChip3: String,
    val heroBtnCv: String,
    val heroBtnContact: String,
    val aboutTitle: String,
    val aboutSubtitle: String,
    val aboutBullets: List<String>,
    val skillsTitle: String,
    val skillsSubtitle: String,
    val fullStackTitle: String,
    val fullStackSubtitle: String,
    val experienceTitle: String,
    val experienceSubtitle: String,
    val projectsTitle: String,
    val projectsSubtitle: String,
    val contactTitle: String,
    val contactSubtitle: String
)

fun stringsFor(lang: AppLanguage): UiStrings =
    when (lang) {
        AppLanguage.ES -> UiStrings(
            profileTitle = "Sobre KuriZd",
            heroRole = "Full-Stack / Android & Kotlin Developer",
            heroDescription = "Creo productos web y móviles modernos con React, TypeScript, Kotlin y APIs bien diseñadas.",
            heroChip1 = "React / TS",
            heroChip2 = "Kotlin / Compose",
            heroChip3 = "Node / Supabase",
            heroBtnCv = "Ver CV",
            heroBtnContact = "Contactar",
            aboutTitle = "Hello World! 👋",
            aboutSubtitle = "Un poco más sobre mí.",
            aboutBullets = listOf(
                "💫 Soy un Full-Stack Engineer enfocado en construir aplicaciones web y móviles escalables con React, Next.js, TypeScript, Kotlin y Node.",
                "⚙️ Me importa el código limpio, la mantenibilidad y la optimización de rendimiento para aportar valor real al producto.",
                "🌱 Disfruto aprender tecnologías nuevas, compartir conocimiento y colaborar con equipos multidisciplinarios.",
                "🧠 Me muevo cómodo tanto en Frontend como en Backend, y me interesa mucho la experiencia de desarrollador (DX) y la arquitectura del producto.",
                "📚 Suelo trabajar en side-projects, design systems, integraciones de APIs y pequeñas herramientas internas para automatizar flujos.",
                "📫 Puedes contactarme por LinkedIn o por correo en kurizd@protonmail.com."
            ),
            skillsTitle = "Habilidades clave",
            skillsSubtitle = "Stack con el que trabajo día a día.",
            fullStackTitle = "Full Stack Tools",
            fullStackSubtitle = "Tecnologías con las que suelo trabajar.",
            experienceTitle = "Experiencia",
            experienceSubtitle = "En qué he estado trabajando.",
            projectsTitle = "Proyectos",
            projectsSubtitle = "Algunas cosas que he construido.",
            contactTitle = "Contacto",
            contactSubtitle = "Abierto a colaborar en proyectos interesantes."
        )

        AppLanguage.EN -> UiStrings(
            profileTitle = "About KuriZd",
            heroRole = "Full-Stack / Android & Kotlin Developer",
            heroDescription = "I build modern web and mobile products with React, TypeScript, Kotlin and well-designed APIs.",
            heroChip1 = "React / TS",
            heroChip2 = "Kotlin / Compose",
            heroChip3 = "Node / Supabase",
            heroBtnCv = "View CV",
            heroBtnContact = "Contact",
            aboutTitle = "Hello World! 👋",
            aboutSubtitle = "A bit more about me.",
            aboutBullets = listOf(
                "💫 I’m a Full-Stack Engineer focused on building scalable web and mobile apps with React, Next.js, TypeScript, Kotlin and Node.",
                "⚙️ I care about clean code, maintainability and performance optimization to deliver real product value.",
                "🌱 I enjoy learning new technologies, sharing knowledge and collaborating with cross-functional teams.",
                "🧠 I feel comfortable across Frontend and Backend, and I care a lot about developer experience (DX) and product architecture.",
                "📚 I often work on side projects, design systems, API integrations and small internal tools to automate workflows.",
                "📫 You can reach me on LinkedIn or by email at kurizd@protonmail.com."
            ),
            skillsTitle = "Key Skills",
            skillsSubtitle = "Stack I use on a daily basis.",
            fullStackTitle = "Full Stack Tools",
            fullStackSubtitle = "Technologies I usually work with.",
            experienceTitle = "Experience",
            experienceSubtitle = "What I’ve been working on.",
            projectsTitle = "Projects",
            projectsSubtitle = "Some things I’ve built.",
            contactTitle = "Contact",
            contactSubtitle = "Open to collaborating on interesting projects."
        )
    }

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AboutMeScreen(modifier: Modifier = Modifier) {
    val configuration = LocalConfiguration.current

    val systemLang = remember(configuration) {
        val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            configuration.locale
        }
        val code = locale.language.lowercase()
        if (code.startsWith("es")) AppLanguage.ES else AppLanguage.EN
    }

    var appLang by remember { mutableStateOf(systemLang) }
    val t = remember(appLang) { stringsFor(appLang) }


    Box(
        modifier = modifier.background(Onyx)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedProfileTitle(
                    modifier = Modifier.weight(1f)
                )
                LanguageToggle(
                    current = appLang,
                    onChange = { appLang = it }
                )
            }


            Spacer(Modifier.height(20.dp))

            HeroCard(
                modifier = Modifier.fillMaxWidth(),
                t = t
            )

            Spacer(Modifier.height(20.dp))

            SectionCard {
                AboutSection(t)
            }

            Spacer(Modifier.height(16.dp))

            SectionCard {
                SkillsSection(t)
            }

            Spacer(Modifier.height(16.dp))

            SectionCard {
                FullStackToolsSection(t)
            }

            Spacer(Modifier.height(16.dp))

            SectionCard {
                ExperienceSection(t)
            }

            Spacer(Modifier.height(16.dp))

            SectionCard {
                PortfolioSection(t)
            }

            Spacer(Modifier.height(16.dp))

            SectionCard {
                ContactSection(t)
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
fun LanguageToggle(
    current: AppLanguage,
    onChange: (AppLanguage) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = Onyx,
        border = BorderStroke(1.dp, CeladonBlue.copy(alpha = 0.6f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LangChip("ES", current == AppLanguage.ES) { onChange(AppLanguage.ES) }
            Spacer(Modifier.width(4.dp))
            LangChip("EN", current == AppLanguage.EN) { onChange(AppLanguage.EN) }
        }
    }
}

@Composable
fun LangChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = if (selected) CeladonBlue else Color.Transparent,
        onClick = onClick
    ) {
        Text(
            text = label,
            color = if (selected) Color.White else TextPrimary,
            fontSize = 11.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun AnimatedProfileTitle(
    modifier: Modifier = Modifier
) {
    val variants = listOf(
        "KuriZd",
        "クリカベリ",
        "한국어",
        "庫里卡維里",
        "курикавери",
        "קוריקאוורי",
        "كوري"
    )

    var index by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(2000) // 2 segundos
            index = (index + 1) % variants.size
        }
    }

    Text(
        text = variants[index],
        color = TextPrimary,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        modifier = modifier
    )
}


@Composable
fun HeroCard(modifier: Modifier = Modifier, t: UiStrings) {
    Surface(
        modifier = modifier,
        color = Onyx.copy(alpha = 0.95f),
        shape = RoundedCornerShape(24.dp),
        shadowElevation = 10.dp
    ) {
        Column(
            modifier = Modifier
                .padding(18.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.my),
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .shadow(8.dp, CircleShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "KuriZd",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = t.heroRole,
                        fontSize = 13.sp,
                        color = TextMuted
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = t.heroDescription,
                        fontSize = 11.sp,
                        color = TextMuted,
                        lineHeight = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                InfoChip(t.heroChip1, ReactNeon)
                InfoChip(t.heroChip2, KotlinNeon)
                InfoChip(t.heroChip3, NodeNeon)
            }


            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilledTonalButton(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(999.dp),
                    colors = androidx.compose.material3.ButtonDefaults.filledTonalButtonColors(
                        containerColor = CeladonBlue,
                        contentColor = Color.White
                    )
                ) {
                    Text(t.heroBtnCv, fontSize = 13.sp)
                }
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(999.dp),
                    colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                        contentColor = CeladonBlue
                    )
                ) {
                    Text(t.heroBtnContact, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun SectionCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = AuroMetalSaurus.copy(alpha = 0.25f),
        shape = RoundedCornerShape(22.dp),
        tonalElevation = 2.dp,
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
            content = content
        )
    }
}

@Composable
fun InfoChip(
    text: String,
    accentColor: Color,
) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = accentColor.copy(alpha = 0.15f),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.9f))
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = accentColor.copy(alpha = 0.95f)
        )
    }
}


@Composable
fun SectionTitle(title: String, subtitle: String? = null) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )
        if (!subtitle.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = TextMuted
            )
        }
    }
}

@Composable
fun AboutSection(t: UiStrings) {
    SectionTitle(
        title = t.aboutTitle,
        subtitle = t.aboutSubtitle
    )
    Spacer(modifier = Modifier.height(10.dp))
    t.aboutBullets.forEachIndexed { index, line ->
        BulletText(line)
        if (index != t.aboutBullets.lastIndex) Spacer(Modifier.height(6.dp))
    }
}

@Composable
fun BulletText(text: String) {
    Text(
        text = text,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        color = TextPrimary.copy(alpha = 0.95f)
    )
}

data class Skill(val name: String, val level: Float)

@Composable
fun SkillsSection(t: UiStrings) {
    SectionTitle(
        title = t.skillsTitle,
        subtitle = t.skillsSubtitle
    )
    Spacer(modifier = Modifier.height(20.dp))

    val skills = listOf(
        Skill("Kotlin / Jetpack Compose", 0.9f),
        Skill("React / Next.js / TypeScript", 0.9f),
        Skill("Node.js / APIs REST", 0.85f),
        Skill("Supabase / Postgres / Prisma", 0.8f),
        Skill("Clean architecture & testing", 0.75f)
    )

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        skills.forEach { skill ->
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = skill.name,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextPrimary
                    )
                    Text(
                        text = "${(skill.level * 100).toInt()}%",
                        fontSize = 11.sp,
                        color = CeladonBlue
                    )
                }
                LinearProgressIndicator(
                    progress = { skill.level },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(999.dp)),
                    color = CeladonBlue,
                    trackColor = Onyx
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FullStackToolsSection(t: UiStrings) {
    SectionTitle(
        title = t.fullStackTitle,
        subtitle = t.fullStackSubtitle
    )
    Spacer(Modifier.height(12.dp))

    TechCategory(
        icon = Icons.Default.Code,
        title = "Frontend",
        tags = listOf(
            "HTML5", "CSS3", "JavaScript", "TypeScript",
            "React", "Next.js", "Redux", "Vue.js",
            "Svelte", "Remix", "Chakra UI", "MUI",
            "TailwindCSS", "Radix UI"
        )
    )

    Spacer(Modifier.height(12.dp))

    TechCategory(
        icon = Icons.Default.Android,
        title = "Mobile & Cross-Platform",
        tags = listOf("React Native", "Expo", "Kotlin", "Jetpack Compose")
    )

    Spacer(Modifier.height(12.dp))

    TechCategory(
        icon = Icons.Default.Api,
        title = "Backend & APIs",
        tags = listOf(
            "Node.js", "Express.js", "NestJS",
            "GraphQL", "tRPC", "Flask", "Deno", "Socket.io"
        )
    )

    Spacer(Modifier.height(12.dp))

    TechCategory(
        icon = Icons.Default.Cloud,
        title = "Database & Cloud",
        tags = listOf(
            "MySQL", "Postgres", "MongoDB",
            "Prisma", "Supabase", "Firebase",
            "AWS", "Vercel", "Netlify", "Docker"
        )
    )

    Spacer(Modifier.height(12.dp))

    TechCategory(
        icon = Icons.Default.BugReport,
        title = "Testing & QA",
        tags = listOf(
            "Jest", "Vitest", "Cypress",
            "Playwright", "Testing Library", "MSW", "SonarQube"
        )
    )

    Spacer(Modifier.height(12.dp))

    TechCategory(
        icon = Icons.Default.Extension,
        title = "Tools & Productivity",
        tags = listOf(
            "Git", "GitHub", "GitLab",
            "NPM", "Yarn", "pnpm",
            "Prettier", "ESLint", "Vite",
            "Turborepo", "Astro", "Qwik",
            "VS Code", "WebStorm", "Notion", "Obsidian"
        )
    )

    Spacer(Modifier.height(12.dp))

    TechCategory(
        icon = Icons.Default.Bolt,
        title = "DevOps & Automation",
        tags = listOf("GitHub Actions", "Docker", "Vercel", "Netlify")
    )

    Spacer(Modifier.height(12.dp))

    TechCategory(
        icon = Icons.Default.MonitorHeart,
        title = "Analytics & Monitoring",
        tags = listOf("Google Analytics", "Sentry")
    )

    Spacer(Modifier.height(12.dp))

    TechCategory(
        icon = Icons.Default.DesignServices,
        title = "Design & Prototyping",
        tags = listOf("Figma", "Dribbble", "Behance")
    )

    Spacer(Modifier.height(12.dp))

    TechCategory(
        icon = Icons.Default.Terminal,
        title = "Productivity & Editors",
        tags = listOf(
            "VS Code", "WebStorm",
            "Vim", "Neovim", "Zed",
            "Warp", "Windows Terminal"
        )
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TechCategory(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    tags: List<String>
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(CeladonBlue.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = CeladonBlue,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
        }
        Spacer(Modifier.height(8.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            tags.forEach { tag ->
                TechBadge(tag)
            }
        }
    }
}

@Composable
fun TechBadge(label: String) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = Onyx,
        border = BorderStroke(
            width = 1.dp,
            color = CeladonBlue.copy(alpha = 0.45f)
        )
    ) {
        Text(
            text = label,
            color = TextPrimary,
            fontSize = 11.sp,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

data class Experience(
    val role: String,
    val company: String,
    val period: String,
    val description: String
)

@Composable
fun ExperienceSection(t: UiStrings) {
    SectionTitle(
        title = t.experienceTitle,
        subtitle = t.experienceSubtitle
    )
    Spacer(modifier = Modifier.height(12.dp))

    val experiences = listOf(
        Experience(
            role = "Android / Full-Stack Developer",
            company = "Empresa X",
            period = "2023 - Presente",
            description = "Apps con Kotlin y Compose, integraciones con APIs REST/GraphQL y optimización de rendimiento."
        ),
        Experience(
            role = "Backend Jr.",
            company = "Startup Y",
            period = "2022 - 2023",
            description = "Servicios REST con Node.js, autenticación, despliegues en cloud y soporte a clientes web/móvil."
        )
    )

    Column {
        experiences.forEachIndexed { index, exp ->
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = exp.role,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = TextPrimary
                )
                Text(
                    text = "${exp.company} • ${exp.period}",
                    fontSize = 12.sp,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = exp.description,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    color = TextPrimary.copy(alpha = 0.9f)
                )
                if (index != experiences.lastIndex) {
                    Spacer(Modifier.height(14.dp))
                }
            }
        }
    }
}

data class Project(
    val name: String,
    val tech: String,
    val description: String
)

@Composable
fun PortfolioSection(t: UiStrings) {
    SectionTitle(
        title = t.projectsTitle,
        subtitle = t.projectsSubtitle
    )
    Spacer(modifier = Modifier.height(12.dp))

    val projects = listOf(
        Project(
            name = "RentIt App",
            tech = "React Native · Supabase · Postgres",
            description = "Plataforma para rentar y publicar artículos entre personas, con autenticación, carritos y pagos."
        ),
        Project(
            name = "Glucose Tracker",
            tech = "React · Supabase · Kotlin",
            description = "Suite para registrar glucosa, insulina, agua y citas médicas, con dashboard y exportación a PDF."
        )
    )

    Column {
        projects.forEachIndexed { index, project ->
            Column {
                Text(
                    text = project.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = TextPrimary
                )
                Text(
                    text = project.tech,
                    fontSize = 12.sp,
                    color = CeladonBlue
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = project.description,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    color = TextPrimary.copy(alpha = 0.9f)
                )
                if (index != projects.lastIndex) {
                    Spacer(Modifier.height(14.dp))
                }
            }
        }
    }
}

@Composable
fun ContactSection(t: UiStrings) {
    SectionTitle(
        title = t.contactTitle,
        subtitle = t.contactSubtitle
    )
    Spacer(modifier = Modifier.height(10.dp))

    ContactItem(title = "Email", value = "kurizd@protonmail.com")
    ContactItem(title = "GitHub", value = "github.com/KuriZd")
    ContactItem(
        title = "LinkedIn",
        value = "linkedin.com/in/oscar-kuricaveri-zamudio-damián-565bb3255"
    )
}

@Composable
fun ContactItem(title: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = title,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            color = TextMuted
        )
        Text(
            text = value,
            fontSize = 13.sp,
            color = CeladonBlue
        )
    }
}
