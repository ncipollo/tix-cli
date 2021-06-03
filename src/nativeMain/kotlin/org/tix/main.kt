package org.tix

import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.tix.builder.tixForCLI
import org.tix.feature.plan.presentation.PlanViewEvent
import platform.posix.exit

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("no ticket.")
        exit(1)
    }
    val path = args.first()
    runBlocking {
        println("in blocking")
        coroutineScope {
            val tix = tixForCLI()
            val viewModel = tix.plan.planViewModel()
            var job: Job? = null
            job = launch {
                viewModel.viewState.collect {
                    if (it.complete) {
                        job?.cancel()
                    }
                }
            }
            launch {
                viewModel.sendViewEvent(PlanViewEvent.PlanUsingMarkdown(path = path))
            }
        }
    }
}