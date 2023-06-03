package org.tix.cli.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.versionOption
import org.tix.cli.command.plan.PlanCommandRunner
import org.tix.cli.config.TixCLIConfig

class TixCommand : CliktCommand(invokeWithoutSubcommand = true) {
    private val path by argument().optional()
    private val dryRun by option(
        "-d", "-dryrun", "--dryrun",
        help = "prints out ticket information instead of creating tickets"
    ).flag()

    private val commandRunner = PlanCommandRunner { echo(it) }

    init {
        versionOption("") {
            """
            🚀 tix version ${TixCLIConfig.version}
            """.trimIndent()
        }
    }

    override fun run() {
        if (currentContext.invokedSubcommand == null) {
            commandRunner.runCommand(path ?: "", shouldDryRun = dryRun)
        }
    }
}