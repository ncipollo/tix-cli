package org.tix

import com.github.ajalt.clikt.core.subcommands
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.tix.builder.tixPlanForCLI
import org.tix.cli.command.PlanCommand
import org.tix.cli.command.TixCommand
import org.tix.feature.plan.presentation.PlanViewEvent

fun main(args: Array<String>) = TixCommand()
    .subcommands(PlanCommand())
    .main(args)

private fun oldMain() {
    val path =  "~/Desktop/tix/story.md"
    runBlocking {
        println("in blocking")
        coroutineScope {
            val plan = tixPlanForCLI()
            val viewModel = plan.planViewModel(this)
            var job: Job? = null
            job = launch {
                viewModel.viewState.collect {
                    if (it.isComplete) {
                        job?.cancel()
                    }
                }
            }
            launch {
                viewModel.sendViewEvent(
                    PlanViewEvent.PlanUsingMarkdown(path = path, shouldDryRun = true)
                )
            }
        }
    }
}