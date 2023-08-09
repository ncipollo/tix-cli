package org.tix.cli.command.plan

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import org.tix.config.domain.ConfigurationSourceOptions
import org.tix.feature.plan.domain.parse.MarkdownFileSource
import org.tix.feature.plan.presentation.PlanViewEvent

class PlanCommand: CliktCommand(help = "mass produce tickets from a markdown document") {
    private val path by argument(help = "optional workspace path to locale a tix config").optional()
    private val includeConfig by option(
        "-include", "--include", "-config", "--config",
        help = "name of configuration to include"
    )
    private val dryRun by option(
        "-d", "-dryrun", "--dryrun",
        help = "prints out ticket information instead of creating tickets"
    ).flag()

    private val commandRunner = PlanCommandRunner { echo(it) }

    override fun run() {
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