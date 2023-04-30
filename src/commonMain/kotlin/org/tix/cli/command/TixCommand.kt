package org.tix.cli.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.versionOption

class TixCommand: CliktCommand() {
    init {
        versionOption("🚀 0.9.7")
    }

    override fun run() {
    }
}