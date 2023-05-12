package org.tix.cli.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.versionOption
import org.tix.cli.config.TixCLIConfig

class TixCommand: CliktCommand(invokeWithoutSubcommand = true) {
    private val path by argument().optional()
    init {
        versionOption("") {
            """
            🚀 tix version ${TixCLIConfig.version}
            """.trimIndent()
        }
    }

    override fun run() {
        if (currentContext.invokedSubcommand == null) {
            echo("Provided path: $path")
        }
    }
}