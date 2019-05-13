package com.example.tfidf

import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.core.publisher.Mono
import java.nio.file.Path

class Driver(var index: Index = Index.createIndex(DefaultStrategy)){

    fun tfIdfStream(directory: String, query: UserQuery): Flux<TfIdf> {

        val initTfIdf = initTfIdf(query) { buildDocuments(getFiles(directory)) }
        return initTfIdf.concatWith(Flux.create { subscriber: FluxSink<TfIdf> ->
            Thread(Runnable {
                pollDirectory(directory, { path -> subscriber.next(refreshTfIdf(query,path)) }, { subscriber.error(it) })
            }).start()
        })

    }


    fun initTfIdf(query: UserQuery, documents: () -> List<Document>): Mono<TfIdf> =
            Mono.create<TfIdf> { t ->
                try {
                    index = Index.createIndex(DefaultStrategy)
                    index += documents()
                    t.success(index.tfIdf(query))
                } catch (e: Throwable) {
                    t.error(e)
                }
            }

    private fun refreshTfIdf(query: UserQuery, file: Path): TfIdf {
        index += buildDocuments(listOf(file.toString()))
        return index.tfIdf(query)
    }


}

