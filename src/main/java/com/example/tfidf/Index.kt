package com.example.tfidf

import java.util.stream.Collectors
import com.example.tfidf.DefaultStrategy.inverseDocFrequency
import reactor.core.publisher.Mono

class Index private constructor(
        val termFreqs: List<DocTermFreq>,
        val inverseDocFreq: DefaultStrategy.InverseDocFreq,
        val strategy: TfIdfStrategy,
        val corpus: Corpus) {

    companion object IndexFactory {
        fun createIndex(strategy: TfIdfStrategy): Index {
            val corpus = Corpus()
            return Index(listOf(), corpus.inverseDocFrequency(), strategy, corpus)

        }

        private fun findTermFreqs(strategy: TfIdfStrategy, corpus: Corpus): List<DocTermFreq> {
            val termFreqs = corpus.docs.parallelStream()
                    .map(strategy::docTermFrequency)
                    .collect(Collectors.toList())
            return termFreqs
        }
    }

    operator fun plus(docs: List<Document>): Index {

        val newCorpus = this.corpus + docs

        return Index(findTermFreqs(strategy, newCorpus), inverseDocFreq + docs, strategy, newCorpus)
    }

    fun tfIdf(query: UserQuery): TfIdf {

        return strategy.tfIdf(query, termFreqs, inverseDocFreq)
    }

    override fun toString(): String {
        return "Index(termFreqs=$termFreqs,\n inverseDocFreq=$inverseDocFreq)"
    }


}