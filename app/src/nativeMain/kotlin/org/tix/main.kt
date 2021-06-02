package org.tix

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.tix.builder.tixForCLI

fun main(args: Array<String>) {
    println(args.first())
    runBlocking {
        tixForCLI().plan.submitter.submit(args.first()).collect { println(it.getOrNull()) }
    }
}