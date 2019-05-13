package com.example.tfidf

import io.kotlintest.specs.BehaviorSpec
import reactor.test.StepVerifier
import java.io.File
import java.nio.file.Files
import java.time.Duration

class DriverTest : BehaviorSpec() {

    init {

        Given("""A user query and a bunch of documents""") {

            val query = UserQuery.buildQuery("w1")
            val docs = listOf(
                    Pair("doc1.txt", mapOf("w1" to 1, "w2" to 2)),
                    Pair("doc2.txt", mapOf("w2" to 1, "w3" to 1)),
                    Pair("doc3.txt", mapOf("w3" to 2))
            ).map(::Document)



            When("""when the app is started""") {
                val stream = Driver().initTfIdf(query) { docs }

                Then("""at least one tfIdf event is generated""") {

                    val expectedTfId = (Index.createIndex(DefaultStrategy) + docs).tfIdf(query)

                    StepVerifier
                            .create(stream)
                            .expectNext(expectedTfId)
                            .expectComplete()
                            .verify()
                }
            }

            When("""when the app throws an error""") {
                val stream = Driver().initTfIdf(query) { throw RuntimeException() }

                Then("""the error is detected properly""") {

                    StepVerifier
                            .create(stream)
                            .expectError()
                            .verify()
                }
            }
        }

        Given("""A couple of documents in a folder""") {

            val query = UserQuery.buildQuery("calidad gato")


            val tmpDir = with(createTempDir("tmp")){deleteOnExit(); this}
            val inputDir = with(createTempDir("input")){deleteOnExit(); this}
            createFiles(inputDir)


            inputDir.listFiles().forEach { println(it) }
            val newFile = prepareFileToBeAppended(tmpDir)


            When("""when the app is started pointing to the folder and a document is added""") {

                val stream = Driver().tfIdfStream(inputDir.toString(), query)

                Then("2 tfIdf events are received") {


                    Thread(Runnable{
                        Thread.sleep(1000)
                        Files.move(newFile.toPath(), inputDir.toPath().resolve(newFile.name))
                    }).start()

                    StepVerifier.create(stream.take(2))
                            .expectNextMatches {it.max()!!.document.endsWith(".1")}
                            .expectNextMatches{it.max()!!.document.endsWith(".3")}
                            .expectComplete()
                            .verify(Duration.ofSeconds(5))

                }

            }


        }
    }

    private fun prepareFileToBeAppended(tmpDir: File): File {
        val newFile = createTempFile(directory = tmpDir, suffix = ".3")
        newFile.bufferedWriter().use { out ->
            out.write("calidad gato otro\n")
        }
        return newFile
    }

    private fun createFiles(inputDir: File) {
        createTempFile(directory = inputDir, suffix = ".1").bufferedWriter().use {
            it.write("gato gato perro")
        }
        createTempFile(directory = inputDir, suffix = ".2").bufferedWriter().use {
            it.write("casa jardin libro")
        }
    }
}