rootProject.name = "tix-cli"

//includeBuild("tix-core") {
//    dependencySubstitution {
//        substitute(module("org.tix:core")).with(project(":"))
//    }
//}
include("tix-core")
include("app")