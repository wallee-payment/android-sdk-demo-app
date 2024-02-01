pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

//include(":app")
//include(":postfinance-checkout-sdk-staging")
include(":app", ":postfinance-checkout-sdk-staging")
//include ":app"
////include ':wallee-payment-sdk-staging'
//include ':postfinance-checkout-sdk-staging'

