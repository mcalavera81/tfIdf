package com.example.complementarypairs

import io.kotlintest.matchers.collections.shouldContain
import io.kotlintest.matchers.collections.shouldContainAll
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.should
import io.kotlintest.specs.BehaviorSpec


class KComplementaryPairsTest: BehaviorSpec() {

    init {
        Given(""" """) {

            When("""1""") {

                Then("""""") {
                    val pairs =findKComplementaryPairs(arrayOf(2,5,7,1,4,2), 6)

                    pairs.shouldHaveSize(6)
                    pairs.shouldContainAll(listOf(0 to 4, 4 to 0, 1 to 3, 3 to 1, 4 to 5, 5 to 4))
                }
            }

            When("""2""") {

                Then("""""") {
                    val pairs =findKComplementaryPairs(arrayOf(1, 8, -3, 0, 1, 3, -2, 4, 5), 6)

                    pairs.shouldHaveSize(7)
                    pairs.shouldContainAll(listOf(0 to 8, 8 to 0, 1 to 6, 6 to 1, 4 to 8, 8 to 4, 5 to 5))
                }
            }

            When("""3""") {

                Then("""""") {
                    val pairs =findKComplementaryPairs(arrayOf(4,5,6,3,1,8,-7,-6), 1)

                    pairs.shouldHaveSize(2)
                    pairs.shouldContainAll(listOf(5 to 6, 6 to 5 ))
                }
            }
        }
    }
}

