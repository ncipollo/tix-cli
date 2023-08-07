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
import org.tix.config.domain.ConfigurationSourceOptions
import org.tix.feature.plan.domain.parse.MarkdownFileSource
import org.tix.feature.plan.presentation.PlanViewEvent

class TixCommand : CliktCommand(invokeWithoutSubcommand = true) {
    private val path by argument().optional()
    private val includeConfig by option(
        "-include", "--include", "-config", "--config",
        help = "name of configuration to include"
    )
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
            val markdownPath = path ?: ""
            commandRunner.runCommand(
                PlanViewEvent.PlanUsingMarkdown(
                    markdownSource = MarkdownFileSource(markdownPath),
                    configSourceOptions = ConfigurationSourceOptions.forMarkdownSource(markdownPath, includeConfig),
                    shouldDryRun = dryRun
                )
            )
        }
    }
}