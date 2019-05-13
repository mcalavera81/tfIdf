package com.example.tfidf

import com.example.tfidf.DefaultStrategy.docTermFrequency
import com.example.tfidf.DefaultStrategy.inverseDocFrequency
import com.example.tfidf.DefaultStrategy.tfIdf
import com.example.tfidf.UserQuery.QueryFactory.buildQuery
import io.kotlintest.matchers.beGreaterThan
import io.kotlintest.matchers.beGreaterThanOrEqualTo
import io.kotlintest.matchers.plusOrMinus
import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec


class TfIdfTest : BehaviorSpec() {

    private val tolerance = 0.001

    init {
        Given("""A document with terms""") {

            val (document1, document2, document3, document4) =
                    listOf(
                            Pair("file1.txt", mapOf("perro" to 1, "casa" to 2, "libro" to 3)),
                            Pair("file2.txt", mapOf("vaso" to 2, "casa" to 1)),
                            Pair("file3.txt", mapOf("libro" to 1, "casa" to 3)),
                            Pair("file4.txt", mapOf("perro" to 3, "vaso" to 1))
                    ).map(::Document)

            val corpus = Corpus(listOf(document1, document2, document3, document4))

            val totalTerms = document1.size

            When("""computing the term frequency with Default strategy""") {

                val (_, freqs) = docTermFrequency(document1)

                Then("""term frequency results in the raw count normalized by document length""") {

                    listOf(Pair("perro", 1), Pair("casa", 2), Pair("libro", 3)).forEach { (term, freq) ->
                        freqs[term] shouldBe (freq.toDouble() / totalTerms plusOrMinus tolerance)
                    }
                    freqs["NonExistent"] shouldBe (null)


                }
            }


            When("""computing the inverse document frequency with Default strategy""") {

                val inverseDocFreq = corpus.inverseDocFrequency()

                Then("""term inverse frequency results in
                    |the logarithmically scaled inverse fraction of the documents that contain the word""") {

                    val invFreqs = inverseDocFreq.freqs()
                    listOf(Pair("perro", 2), Pair("casa", 3), Pair("libro", 2), Pair("vaso", 2)).forEach { (term, numDocs) ->
                        invFreqs[term] shouldBe (Math.log(corpus.docs.size.toDouble() / numDocs) plusOrMinus tolerance)

                    }
                    invFreqs["NonExistent"] shouldBe (null)

                }
            }

            When("""computing the tf-idf with Default strategy""") {
                val inverseDocFreq = corpus.inverseDocFrequency()
                val docTermFreq = corpus.docs.map { docTermFrequency(it) }

                val tfIdf = tfIdf(buildQuery("libro"), docTermFreq, inverseDocFreq)

                Then("""tf-idf results in a multiplication""") {
                    tfIdf("file1.txt")!! should beGreaterThanOrEqualTo(tfIdf("file3.txt")!!)
                    tfIdf("file3.txt")!! should beGreaterThanOrEqualTo(tfIdf("file2.txt")!!)
                    tfIdf("file2.txt")!! should beGreaterThanOrEqualTo(tfIdf("file4.txt")!!)

                    tfIdf[0].document shouldBe ("file1.txt")
                    tfIdf[0].score should beGreaterThan(0.0)

                }
            }

            When("""computing the tf-idf for multiple terms with Default strategy""") {
                val inverseDocFreq = corpus.inverseDocFrequency()
                val docTermFreq = corpus.docs.map { docTermFrequency(it) }


                val (tfIdf, term1TfIdf, term2TfIdf) = listOf(
                        buildQuery("casa vaso"),
                        buildQuery("casa"),
                        buildQuery("vaso")).map { tfIdf(it, docTermFreq, inverseDocFreq) }

                Then("""the resulting tf-idf is the sum of the tf-idf for each term""") {
                    with("file1.txt") {
                        tfIdf[0].document shouldBe ("file2.txt")
                        tfIdf(this) shouldBe (term1TfIdf(this)!! + term2TfIdf(this)!! plusOrMinus tolerance)
                    }
                }
            }

            When("""adding a new document to the corpus changes the tf-idf scores """) {

                val (tfIdf3Docs, tfIdf4Docs) = listOf(corpus - document4, corpus).map {
                    with(it) {
                        val inverseDocFreq = inverseDocFrequency()
                        val docTermFreq = docs.map { docTermFrequency(it) }
                        tfIdf(buildQuery("perro vaso"), docTermFreq, inverseDocFreq)
                    }
                }

                tfIdf3Docs.max()!!.document shouldBe ("file2.txt")
                tfIdf4Docs.max()!!.document shouldBe ("file4.txt")

            }

        }
    }
}


