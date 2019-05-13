package com.example.tfidf

import java.io.File
import java.util.stream.Collectors


data class Corpus(val docs: List<Document> = listOf()) {

    operator fun plus(newDocs: List<Document>): Corpus = Corpus(this.docs + newDocs)
    operator fun minus(doc: List<Document>): Corpus = Corpus(this.docs - doc)
    operator fun minus(doc: Document): Corpus = Corpus(this.docs - doc)
}

data class Document(val filename: String, val wordsCount: Map<String, Int>) {

    constructor(pair : Pair<String, Map<String, Int>>) : this(pair.first, pair.second)

    val size: Int
        get() = wordsCount.values.sum()

    operator fun contains(term: String): Boolean = wordsCount.containsKey(term)
}

fun buildDocuments(files: List<String>): List<Document> =
        files.parallelStream()
                .map { File(it).extractTextData() }
                .collect(Collectors.toList())
                .toList()


private fun File.extractTextData(): Document {

    val processText: (Sequence<String>) -> Map<String, Int> = {
        it.fold(mutableMapOf(), { acc: MutableMap<String, Int>, s: String ->
            val split = s.toLowerCase().split("\\s+".toRegex())
            split.forEach { term: String -> acc[term] = acc.getOrDefault(term, 0) + 1 }
            acc
        })
    }

    val wordCounts = inputStream().bufferedReader().useLines(processText)
    return Document(name, wordCounts)

}

