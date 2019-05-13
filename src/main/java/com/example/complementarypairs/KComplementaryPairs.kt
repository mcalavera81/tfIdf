package com.example.complementarypairs


// Time: O(n) Linear with the array size
fun findKComplementaryPairs(array: Array<Int>, k: Int): List<Pair<Int, Int>> {


    val valueToIndexes = array.foldIndexed(mutableMapOf()) { index: Int, acc: MutableMap<Int, List<Int>>, value: Int ->
        acc += value to acc.getOrDefault(value, mutableListOf<Int>()).plus(index)
        acc
    }

    val neededComplements: List<Int> = array.map { k -it }

    return neededComplements.foldIndexed(mutableListOf(), { index: Int, acc: List<Pair<Int, Int>>, neededComplement: Int ->
        acc + (valueToIndexes.get(neededComplement)?.map { index to it } ?:  listOf())
    })

}
