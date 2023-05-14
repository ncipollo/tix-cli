package org.tix

import com.github.ajalt.clikt.core.subcommands
import org.tix.cli.command.TixCommand
import org.tix.cli.command.plan.PlanCommand

fun main(args: Array<String>) = TixCommand()
    .subcommands(PlanCommand())
    .main(args)
