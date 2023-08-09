package org.tix.cli.command.plan

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import org.tix.feature.plan.presentation.PlanViewEvent

class QuickCommand: CliktCommand(help = "create a quick ticket form the provided message") {
    private val title by argument(help = "title of the ticket to create")
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
        val quickEvent = PlanViewEvent.quickTicket(title, includeConfig, dryRun)
        commandRunner.runCommand(quickEvent)
    }
}