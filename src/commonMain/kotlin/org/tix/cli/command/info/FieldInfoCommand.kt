package org.tix.cli.command.info

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.ProgramResult
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.option
import kotlinx.coroutines.runBlocking
import org.tix.builder.fieldInfoForCLI
import org.tix.config.domain.ConfigurationSourceOptions
import org.tix.error.TixError
import org.tix.error.toTixError
import org.tix.integrations.jira.field.Field

class FieldInfoCommand : CliktCommand(name = "fields", help = "print jira field info") {
    private val path by argument(help = "optional workspace path to locale a tix config").optional()
    private val includeConfig by option(
        "-include", "--include", "-config", "--config",
        help = "name of configuration to include"
    )

    override fun run() {
        val workspace = path ?: ""
        val configOptions = ConfigurationSourceOptions(
            workspaceDirectory = workspace,
            savedConfigName = includeConfig
        )
        val fetcher = fieldInfoForCLI()
        runBlocking {
            val fieldsResult = fetcher.fetchFields(configOptions)
            if (fieldsResult.isSuccess) {
                echoFields(fieldsResult.getOrThrow())
            } else {
                outputFailure(fieldsResult.toTixError())
            }
        }
    }

    private fun echoFields(fields: List<Field>) {
        echo("""
            Field Name : Field ID
            ================================
        """.trimIndent())
        val output = fields
            .sortedBy { it.name }
            .joinToString("\n") { "${it.name} : ${it.id}" }
        echo(output)
    }

    private fun outputFailure(tixError: TixError) {
        echo(
            message = "Failed to fetch jira fields.\nError: ${tixError.message} ${errorCauseMessage(tixError)}",
            err = true
        )
        throw ProgramResult(1)
    }

    private fun errorCauseMessage(ex: TixError) =
        ex.cause?.message?.let { "\nCause: $it" } ?: ""
}