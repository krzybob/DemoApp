import org.gradle.api.JavaVersion

object Versions {
    val java = JavaVersion.VERSION_17
    const val gradle = "8.2.2"
    const val kotlin = "1.9.22"
    const val ksp = "1.0.17"
    const val core_ktx = "1.12.0"
    const val coroutines = "1.7.3"
    const val hilt = "2.50"
    const val compose = "1.6.1"
    const val compose_compiler = "1.5.9"
    const val compose_material3 = "1.2.0"
    const val compose_activity = "1.8.2"
    const val lifecycle = "2.7.0"
    const val compose_navigation = "2.7.7"
    const val hilt_navigation = "1.1.0"
    const val paging = "3.2.1"
    const val destinations = "1.10.0"
    const val retrofit = "2.9.0"
    const val moshi = "1.15.1"
    const val okhttp = "4.12.0"
    const val coil = "2.5.0"
    const val timber = "5.0.1"
}

object Sdk {
    const val minimum = 24
    const val target = 34
    const val compile = 34
}