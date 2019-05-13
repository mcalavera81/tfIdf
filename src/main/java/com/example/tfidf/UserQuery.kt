package com.example.tfidf

class UserQuery private  constructor(val terms: List<String>, val topResults: Int) {
    companion object QueryFactory {
        fun buildQuery(input: String, topResults: Int = 10): UserQuery =
                UserQuery(input.toLowerCase().split("\\s+".toRegex()), topResults)
    }
}
