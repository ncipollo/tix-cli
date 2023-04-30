package org.tix.cli.command

import com.github.ajalt.clikt.core.CliktCommand

class PlanCommand: CliktCommand() {
    override fun run() {
        echo("plan!")
    }
}