package com.example.palindrome


// Time: O(n) Linear with the array size
fun isPalindrome(s: String): Boolean {
    if (s.isBlank()) return false

    var isPalindrome = true

    for ((index, _) in s.withIndex()) {
        if(index >= s.length/2) break
        if(s[index] != s[s.length-1-index]){
            isPalindrome = false
        }
    }
    return isPalindrome
}
