package org.tix

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.tix.builder.tixForCLI
import org.tix.feature.plan.presentation.PlanViewEvent
import platform.posix.exit

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("no ticket.")
        exit(1)
    }
    val path = args.first()
    println(args.first())
    runBlocking {
        println("in blocking")
        coroutineScope {
            println("in coroutine scope")
            val tix = tixForCLI()
            val viewModel = tix.plan.planViewModel()
            launch {
                viewModel.viewState.collect { state -> println(state) }
                println("---after collect")
            }
            println("after collect launch")
            launch {
                println("sending path: ${path}")
                viewModel.sendViewEvent(PlanViewEvent.PlanUsingMarkdown(path = path))
                println("---after path send")
            }
            println("after view event sent launch")
        }
    }
}