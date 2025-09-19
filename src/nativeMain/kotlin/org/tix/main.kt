package org.tix

import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import org.tix.cli.command.TixCommand
import org.tix.cli.command.info.FieldInfoCommand
import org.tix.cli.command.info.InfoCommand
import org.tix.cli.command.plan.PlanCommand
import org.tix.cli.command.plan.QuickCommand

fun main(args: Array<String>) = TixCommand()
    .subcommands(
        PlanCommand(),
        QuickCommand(),
        InfoCommand()
            .subcommands(FieldInfoCommand())
    )
    .main(args)
