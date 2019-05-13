package com.example.tfidf


interface TfIdfStrategy {
    fun docTermFrequency(document: Document): DocTermFreq
    fun Corpus.inverseDocFrequency(): DefaultStrategy.InverseDocFreq
    fun tfIdf(query: UserQuery, termFreqs: List<DocTermFreq>, inverseDocFreq: DefaultStrategy.InverseDocFreq): TfIdf
}

object DefaultStrategy : TfIdfStrategy {


    override fun docTermFrequency(document: Document): DocTermFreq {

        val totalWords = document.wordsCount.map { it.value }.sum()

        val wordFreq = document.wordsCount.mapValues { it.value.toDouble() / totalWords }

        return DocTermFreq(document.filename, wordFreq)
    }

    data class DefaultInverseDocFreq(private val corpusSize: Int = 0, private val docsWithTerm: Map<String, Int> = mapOf()) : InverseDocFreq {

        override fun freqs(): Map<String, Double> =
                docsWithTerm.mapValues { Math.log(corpusSize.toDouble() / it.value) }

        override fun plus(docs: List<Document>): InverseDocFreq {


            val updatedDocsWithTerm = docs.fold(docsWithTerm)
            { acc: Map<String, Int>, doc: Document -> updateDocFreq(doc, acc) }

            return DefaultInverseDocFreq(this.corpusSize + docs.size, updatedDocsWithTerm)
        }

        private fun updateDocFreq(doc: Document, globalAcc: Map<String, Int>): Map<String, Int> {
            return doc.wordsCount.toList().fold(globalAcc)
            { acc: Map<String, Int>, (word, _) ->
                acc + (word to acc.getOrDefault(word, 0) + 1)
            }
        }

    }

    override fun Corpus.inverseDocFrequency(): InverseDocFreq {
        return DefaultInverseDocFreq() + docs
    }

    override fun tfIdf(query: UserQuery, termFreqs: List<DocTermFreq>, inverseDocFreq: InverseDocFreq): TfIdf {



        val tfIdfList = termFreqs.map { termFreq ->
            val sumTfIfd = query.terms.map { term ->
                val idf = inverseDocFreq.freqs().getOrDefault(term, 0.0)
                termFreq.freqs.getOrDefault(term, 0.0) * idf
            }.sum()

            TfIdf.TfIdfEntry(termFreq.document,sumTfIfd)
        }


        return TfIdf(tfIdfList.sortedByDescending { it.score }.take(query.topResults))
    }

    interface InverseDocFreq{
        fun freqs(): Map<String, Double>
        operator fun plus(docs: List<Document>): InverseDocFreq

    }

}
data class DocTermFreq(val document:String, val freqs: Map<String , Double>)


data class TfIdf(val weights: List<TfIdfEntry>){

    operator fun get(index:Int)= weights[index]
    fun max(): TfIdfEntry? = weights.getOrNull(0)

    operator fun invoke(document: String):Double?{
        val filter = weights.filter { it.document == document }
        return if(filter.isNotEmpty()) filter[0].score else null
    }
    data class TfIdfEntry(val document: String, val score: Double)
}

