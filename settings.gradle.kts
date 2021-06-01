import java.net.URI

rootProject.name = "tix-cli"

sourceControl {
    gitRepository(URI("https://github.com/tixorg/tix-core.git")) {
        producesModule("org.tix:core")
    }
}