package org.asr.example.filter.impl

import org.asr.example.filter.FilterInterface
import kotlin.math.absoluteValue
import kotlin.math.pow

class BloomFilter(
    /**
     * Number of bits in the filter
     */
    val m: Int = 1_000_000,
    /**
     * Actual bit_array to store the metadata of the stored value
     */
    private val bitArray: BooleanArray = BooleanArray(m) { false }
) : FilterInterface {

    /**
     * Array of functions which calculates the different Hash Codes for the string
     * Active `k` is 3, since 3 different hashing functions are active
     */
    private val hashCodeAlgorithms = arrayOf(
        { x: String -> x.hashCode().absoluteValue % m }, // Calculates Java based hash codes
//        { x: String ->
//            x.chars().sum() % m
//        }, // Calculates the sum of the code points for each character, Since, a lot of strings are of short length, this one is having a lot of collisions
        { x: String ->
            var hash = 0
            for (i in 0..x.length - 1) {
                hash += (7.0.pow(i) * x[i].code).toInt().absoluteValue % m
                hash %= m
            }
            hash
        },
        { x: String ->
            var hash = 0
            for (i in 0..x.length - 1) {
                hash += (13.0.pow(i) * x[i].code).toInt().absoluteValue % m
                hash %= m
            }
            hash
        },
    )

    /**
     * Checks if the given string exists in the collection
     *
     * @param inputString The string to check for existence
     * @return `true` if the string exists or may exist, `false` otherwise.
     */
    override fun search(inputString: String): Boolean {
        return hashCodeAlgorithms
            .map { hashCodeAlgo -> this@BloomFilter.bitArray[hashCodeAlgo(inputString)] }
            .reduce { acc: Boolean, bool: Boolean -> acc and bool }
    }

    /**
     * Adds one or more strings to the collection
     *
     * @param inputStrings The strings to be added.
     * @return `true` if the strings were successfully added, `false` if one or more strings were failed to add in
     * the filter, whereas rest of the strings might be updated in the filter.
     */
    override fun add(vararg inputStrings: String): Boolean {
        return inputStrings
            .map { s -> this.add(s) }
            .reduce { acc: Boolean, bool: Boolean -> acc and bool }
    }

    /**
     * Adds a string to the collection
     *
     * @param inputString The string to be added.
     * @return `true` if the string is successfully added
     */
    private fun add(inputString: String): Boolean {
        for (hashCodeAlgo in hashCodeAlgorithms) {
            val hashCodeValue = hashCodeAlgo(inputString)
            this@BloomFilter.bitArray[hashCodeValue] = true
        }
        return true
    }

}