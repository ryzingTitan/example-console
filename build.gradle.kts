import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import java.util.*

plugins {
    id("org.springframework.boot") version "3.0.6"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.8.21"
    kotlin("plugin.spring") version "1.8.21"
    id("org.jlleitschuh.gradle.ktlint") version "11.3.2"
    id("io.gitlab.arturbosch.detekt") version "1.22.0"
    id("com.github.ben-manes.versions") version "0.46.0"
    jacoco
    distribution
}

group = "com.example"
version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.2.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.7.1")
    runtimeOnly("io.r2dbc:r2dbc-postgresql:0.8.13.RELEASE")
    runtimeOnly("io.r2dbc:r2dbc-mssql:1.0.0.RELEASE")
    runtimeOnly("io.r2dbc:r2dbc-h2:1.0.0.RELEASE")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
    testImplementation("org.junit.platform:junit-platform-suite-api:1.9.3")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
    testImplementation("io.cucumber:cucumber-java:7.12.0")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:7.12.0")
    testImplementation("io.cucumber:cucumber-spring:7.12.0")
    testImplementation("io.projectreactor:reactor-test:3.5.6")
    testImplementation("org.testcontainers:testcontainers:1.18.1")
    testImplementation("org.testcontainers:mssqlserver:1.18.1")
    testRuntimeOnly("org.postgresql:postgresql:42.6.0")
    testRuntimeOnly("com.microsoft.sqlserver:mssql-jdbc:11.2.3.jre17")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.getByName("addKtlintFormatGitPreCommitHook") {
    dependsOn("processResources")
    dependsOn("processTestResources")
    dependsOn("ktlintMainSourceSetCheck")
    dependsOn("ktlintTestSourceSetCheck")
    dependsOn("ktlintKotlinScriptCheck")
    dependsOn("detekt")
}

tasks.getByName("compileKotlin") {
    dependsOn("addKtlintFormatGitPreCommitHook")
}

ktlint {
    version.set("0.48.2")
    verbose.set(true)
    outputToConsole.set(true)
    coloredOutput.set(true)
    reporters {
        reporter(ReporterType.JSON)
    }
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = "17"

    reports {
        html.required.set(true)
        html.outputLocation.set(file("${rootProject.rootDir}/${rootProject.name}/detektHtmlReport/detekt.html"))
    }
}

jacoco {
    toolVersion = "0.8.9"
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(false)
        csv.required.set(false)
        html.outputLocation.set(file("${rootProject.rootDir}/${rootProject.name}/jacocoHtmlReport"))
    }
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.90".toBigDecimal()
            }
        }
    }
}

tasks.check {
    dependsOn("jacocoTestCoverageVerification")
}

tasks.withType<DependencyUpdatesTask> {
    checkForGradleUpdate = true
    outputFormatter = "json"
    outputDir = "build/dependencyUpdates"
    reportfileName = "report"

    resolutionStrategy {
        componentSelection {
            all {
                if (candidate.version.isNonStable() && !currentVersion.isNonStable()) {
                    reject("Release candidate")
                }
            }
        }
    }
}

fun String.isNonStable(): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { uppercase(Locale.getDefault()).contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(this)
    return isStable.not()
}

distributions {
    main {
        contents {
            into("/") {
                from("src/main/resources")
                include("application.properties")
            }
            into("/") {
                from("build/libs")
                include("${rootProject.name}-${rootProject.version}.jar")
            }
        }
    }
}

tasks.getByName("distTar") {
    enabled = false
}

tasks.getByName("distZip") {
    dependsOn("bootJar")
}
