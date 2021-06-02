package org.tix

import kotlinx.coroutines.runBlocking
import org.tix.builder.tixForCLI

fun main(args: Array<String>) {
    println("hi there")
    runBlocking {
        val tix = tixForCLI()
    }
}