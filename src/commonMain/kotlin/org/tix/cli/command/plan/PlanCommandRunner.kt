package org.tix.cli.command.plan

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.tix.builder.tixPlanForCLI
import org.tix.feature.plan.presentation.PlanViewEvent

class PlanCommandRunner(private val echo: (String) -> Unit) {
    fun runCommand(path: String, shouldDryRun: Boolean) {
        runBlocking {
            val viewModel = tixPlanForCLI().planViewModel(this)
            var collectJob: Job? = null
            collectJob = launch {
                viewModel.viewState.collect {
                    echo(it.message)
                    if (it.isComplete) {
                        collectJob?.cancel()
                    }
                }
            }
            launch {
                viewModel.sendViewEvent(
                    PlanViewEvent.PlanUsingMarkdown(
                        path = path,
                        shouldDryRun = shouldDryRun
                    )
                )
            }
        }
    }
}