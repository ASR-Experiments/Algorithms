package org.asr.example

import org.asr.example.filter.FilterInterface
import org.asr.example.filter.impl.BloomFilter
import org.asr.example.util.randomStrings
import java.util.*
import kotlin.math.absoluteValue
import kotlin.time.DurationUnit
import kotlin.time.toDuration

// Uncomment the version that you would like to invoke
private val filter: FilterInterface = BloomFilter()
//private val filter: FilterInterface = Trie('x')
//private val filter: FilterInterface = MapBasedTrie('x')

private val greetings = arrayOf(
    "Cool !",
    "Amazing !",
    "Fantastic !",
    "Awesome !",
    "Great !",
    "Superb !"
)

fun addStringMenu() {
    println(
        """
        ${greetings.random()}, you would like to add a string in the filter
        Please enter the string to be added below
    """.trimIndent()
    )
    val string = readln()
    val added = filter.add(string)
    if (added) {
        println("`$string` added in the filter")
    } else {
        System.err.println("Sadly, `$string` not added in the filter, Please try again")
    }
}

fun searchStringMenu() {
    println(
        """
        ${greetings.random()}, you would like to search for a string in the filter
        Please enter the string to be searched below
    """.trimIndent()
    )
    val string = readln()
    val added = filter.search(string)
    if (added) {
        println("`$string` may be present in the filter")
    } else {
        System.err.println("`$string` not found in the filter, Please try again")
    }
}

fun autoAddStringsMenu(sc: Scanner) {
    println(
        """
        ${greetings.random()}, you would like to add few random strings within the filter,
        Please enter the number of strings that you would like to add in the filter
    """.trimIndent()
    )
    val count = sc.nextInt().absoluteValue
    val randomStrings = randomStrings(count, 6, 12)
    val startTime = System.nanoTime()
    val added = filter.add(*randomStrings)
    val timeInNs = System.nanoTime() - startTime
    if (added) {
        println(
            "${greetings.random()}, added $count strings in the Filter, in ${
                timeInNs.toDuration(DurationUnit.NANOSECONDS)
            }"
        )
    } else {
        System.err.println("Failed to add few strings among $count strings")
    }
}

fun autoSearchStringsMenu(sc: Scanner) {
    println(
        """
        ${greetings.random()}, you would like to search few random strings within the filter,
        Please enter the number of strings that you would like to search in the filter
    """.trimIndent()
    )
    val count = sc.nextInt().absoluteValue
    val randomStrings = randomStrings(count, 6, 12)
    var matched = 0
    val startTime = System.nanoTime()
    for (string in randomStrings) {
        val found = filter.search(string)
        if (found) {
            matched += 1
            println("$matched. `$string`  may be present in the filter")
        }
    }
    val timeInNs = System.nanoTime() - startTime
    println(
        "Found $matched strings among $count random strings which were searched, and took ${
            timeInNs.toDuration(DurationUnit.NANOSECONDS)
        }"
    )
}

fun invalidMenu() {
    System.err.println("Invalid input, please try again, add any number between 1 and 9")
}

fun main() {
    var exitFlag = false
    val sc = Scanner(System.`in`)

    while (!exitFlag) {
        println(
            """
            Enter one of the digits to continue with testing Filters:
            1. To Manually Add a string in the filter
            2. To Manually Search for a string in the filter
            3. To Automatically Add few random strings in the filter
            4. To Automatically Search few random strings in the filter
            5-9. To Exit the program
            Enter your input between 1-9 for the respective options:
        """.trimIndent()
        )
        val selectedMenu = sc.nextByte()
        when (selectedMenu) {
            1.toByte() -> addStringMenu()
            2.toByte() -> searchStringMenu()
            3.toByte() -> autoAddStringsMenu(sc)
            4.toByte() -> autoSearchStringsMenu(sc)
            in 5.toByte()..9.toByte() -> exitFlag = true
            else -> invalidMenu()
        }
        println()
    }
}