package org.tix.cli.command.plan

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option

class PlanCommand: CliktCommand() {
    private val path by argument().optional()
    private val dryRun by option(
        "-d", "-dryrun", "--dryrun",
        help = "prints out ticket information instead of creating tickets"
    ).flag()

    private val commandRunner = PlanCommandRunner { echo(it) }

    override fun run() {
        commandRunner.runCommand(path ?: "", shouldDryRun = dryRun)
    }
}