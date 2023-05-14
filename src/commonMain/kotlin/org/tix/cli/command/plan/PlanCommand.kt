package org.tix.cli.command.plan

import com.github.ajalt.clikt.core.CliktCommand

class PlanCommand: CliktCommand() {
    override fun run() {
        echo("plan!")
    }
}