package com.example.palindrome

import io.kotlintest.be
import io.kotlintest.should
import io.kotlintest.specs.BehaviorSpec

class PalindromeTest: BehaviorSpec() {

    init {
        Given(""" """) {

            When("""Two letters not palindrome""") {

                Then("""is classified correctly as non palindrome""") {

                    isPalindrome("ab") should be(false)
                }
            }

            When("""Three letters not palindrome""") {

                Then("""is classified correctly as non palindrome""") {

                    isPalindrome("aab") should be(false)
                }
            }

            When("""Four letters not palindrome""") {

                Then("""is classified correctly as non palindrome""") {

                    isPalindrome("aaba") should be(false)
                }
            }


            When("""Five letters not palindrome""") {

                Then("""is classified correctly as non palindrome""") {

                    isPalindrome("abaaa") should be(false)
                }
            }


            When("""Six letters not palindrome""") {

                Then("""is classified correctly as non palindrome""") {

                    isPalindrome("aabaaa") should be(false)
                }
            }


            When("""Seven letters not palindrome""") {

                Then("""is classified correctly as non palindrome""") {

                    isPalindrome("aaabaab") should be(false)
                }
            }


            When("""Two letters palindrome""") {

                Then("""is classified correctly as palindrome""") {

                    isPalindrome("mm") should be(true)
                }
            }

            When("""Three letters palindrome""") {

                Then("""is classified correctly as palindrome""") {

                    isPalindrome("gig") should be(true)
                }
            }

            When("""Four letters palindrome""") {

                Then("""is classified correctly as palindrome""") {

                    isPalindrome("noon") should be(true)
                }
            }

            When("""Five letters palindrome""") {

                Then("""is classified correctly as palindrome""") {

                    isPalindrome("civic") should be(true)
                }
            }


            When("""Six letters palindrome""") {

                Then("""is classified correctly as palindrome""") {

                    isPalindrome("pullup") should be(true)
                }
            }


            When("""Seven letters palindrome""") {

                Then("""is classified correctly as palindrome""") {

                    isPalindrome("racecar") should be(true)
                }
            }

        }
    }
}