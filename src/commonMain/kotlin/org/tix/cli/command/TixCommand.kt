package org.tix.cli.command

import com.github.ajalt.clikt.completion.completionOption
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.versionOption
import org.tix.cli.command.plan.PlanCommandRunner
import org.tix.cli.config.TixCLIConfig
import org.tix.config.TixCoreConfig

class TixCommand : CliktCommand(invokeWithoutSubcommand = true) {
    private val path by argument().optional()
    private val dryRun by option(
        "-d", "-dryrun", "--dryrun",
        help = "prints out ticket information instead of creating tickets"
    ).flag()

    private val commandRunner = PlanCommandRunner { echo(it) }

    init {
        completionOption()
        versionOption("") {
            """
            🚀 tix cli version: ${TixCLIConfig.version}
            📚 tix core version: ${TixCoreConfig.version}
            """.trimIndent()
        }
    }

    override fun run() {
        if (currentContext.invokedSubcommand == null) {
            commandRunner.runCommand(path ?: "", shouldDryRun = dryRun)
        }
    }
}