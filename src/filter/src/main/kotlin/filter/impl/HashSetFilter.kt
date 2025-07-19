package org.asr.example.filter.impl

import org.asr.example.filter.FilterInterface

class HashSetFilter(private val presentValues: MutableSet<String> = HashSet()) : FilterInterface {
    override fun search(inputString: String): Boolean {
        return presentValues.contains(inputString)
    }

    override fun add(vararg inputStrings: String): Boolean {
        return presentValues.addAll(inputStrings)
    }
}