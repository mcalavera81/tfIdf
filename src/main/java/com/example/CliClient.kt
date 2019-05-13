package com.example

import com.example.tfidf.Driver
import com.example.tfidf.TfIdf
import com.example.tfidf.UserQuery.QueryFactory.buildQuery
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import mu.KotlinLogging


private val logger = KotlinLogging.logger {}

fun main(args: Array<String>) = mainBody {

    val parsedArgs = ArgParser(args).parseInto(::CliParser)
    parsedArgs.run { buildApp() }

}

private fun CliParser.buildApp() {
    val query = buildQuery(this.terms, this.topResults)

    Driver().tfIdfStream(this.directory, query)
            .subscribe(::reporter, errorHandler)
}

private fun reporter(tfIdf: TfIdf) {

    println("-------------------------")
    tfIdf.weights.forEach { println("${it.document} ${it.score}")}
    println("\n\n")
}

private val errorHandler = { t: Throwable? ->
    logger.error(t) { "An error has ocurred. Killing application ..." }
    System.exit(1)
}




private class CliParser(parser: ArgParser) {

    val directory by parser.storing(
            "-d", help = "The directory where the documents will be written")

    val topResults by parser.storing(
            "-n", help = "The count of top results to show") { toInt() }

    val terms by parser.storing(
            "-t",help = "The terms to be analyzed")
}