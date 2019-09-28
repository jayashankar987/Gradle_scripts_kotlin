import com.android.build.gradle.api.ApplicationVariant

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(29)
    dataBinding {
        isEnabled = false
    }
    defaultConfig {
        applicationId = "com.jay.sample.gradlekotlin"
        minSdkVersion(23)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runnerVersion.AndroidJUnitRunner"
    }

    flavorDimensions("app")

    productFlavors {
        create("Meetup") {
            applicationId = "com.devs.meetup.demo"
            manifestPlaceholders = mapOf("appIcon" to "@mipmap/ic_launcher")
        }

        create("MyBuild") {
            applicationId = "com.jay.sample.demo"
            manifestPlaceholders = mapOf("appIcon" to "@mipmap/ic_kotlin")
        }



        create("BlrKotlin") {
            applicationId = "com.community.blr.kotlin"
            manifestPlaceholders = mapOf("appIcon" to "@mipmap/blr_kotlin")
        }

        create("minApi26") {
            minSdkVersion(26)
            versionNameSuffix = "-minApi26"
            applicationId = "com.jay.sample.gradlekotlin"
        }
    }



    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        named("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    sourceSets {
        getByName("BlrKotlin").res.srcDirs("src/BlrKotlin/res")

    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(fileTree("dir" to "libs", "include" to "*.jar"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Deps.kotlin_version}")
    implementation("androidx.appcompat:appcompat:${Deps.AndroidX.androidX}")
    implementation("androidx.core:core-ktx:${Deps.AndroidX.androidX}")
    implementation("androidx.constraintlayout:constraintlayout:${Deps.AndroidX.constraintLayout}")
    testImplementation("junit:junit:${Deps.Junit.junit}")
    androidTestImplementation("androidx.test:runner:${Deps.AndroidX.Test.runnerVersion}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${Deps.AndroidX.Test.espresso}")
}

tasks.configureEach {
    if (name == "Meetup" ||
        name ==
        "MyBuild"
    ) {
        finalizedBy("generateApk")
    }
}

tasks.register("generateApk") {
    android.applicationVariants.all(object : Action<ApplicationVariant> {
        override fun execute(variant: ApplicationVariant) {
            variant.outputs.all {
                if (variant.isSigningReady && variant.buildType.name == "release") {
                    val fileRelease = outputFile
                    val env = if (project.hasProperty("env")) project.properties["env"] as String else "debug"
                    val destDir = File(System.getenv("APK_OUTPUT_FOLDER") + env)
                    println("APK file - $fileRelease")
                    copy {
                        from(fileRelease.toString())
                        into(destDir.toString())
                    }

                }
            }
        }
    })
}


kapt {
    useBuildCache = true

}

