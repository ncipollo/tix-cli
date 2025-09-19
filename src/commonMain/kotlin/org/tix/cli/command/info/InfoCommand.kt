package org.tix.cli.command.info

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context

class InfoCommand: CliktCommand() {
    override fun help(context: Context) = "subcommands for printing ticket info"
    
    override fun run() = Unit
}