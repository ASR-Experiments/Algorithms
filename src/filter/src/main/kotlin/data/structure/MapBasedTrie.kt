package org.asr.example.data.structure

import org.asr.example.filter.FilterInterface

data class MapBasedTrie(
    val value: Char,
    var exists: Boolean = false,
    private val children: MutableMap<Char, MapBasedTrie?> = HashMap()
) : FilterInterface {
    fun addChild(value: Char, exists: Boolean = false) {
        val child = MapBasedTrie(value = value, exists = exists)
        children[value] = child
    }

    fun traverse(value: String): MapBasedTrie? {
        var currentNode: MapBasedTrie? = this
        for (c in value.toCharArray()) {
            currentNode = currentNode?.children[c]
            if (currentNode == null)
                return null
        }
        return currentNode
    }

    fun addString(value: String): MapBasedTrie {
        var currentNode: MapBasedTrie = this
        for (c in value.toCharArray()) {
            var tempNode = currentNode.children[c]
            if (tempNode == null) {
                tempNode = MapBasedTrie(value = c)
                currentNode.children[c] = tempNode
            }
            currentNode = tempNode
        }
        currentNode.exists = true
        return currentNode
    }

    override fun search(inputString: String): Boolean {
        val leafNode = traverse(inputString)
        return leafNode?.exists ?: false
    }

    override fun add(vararg inputStrings: String): Boolean {
        return inputStrings
            .map { x -> addString(x) }
            .map { x -> x.exists }
            .reduce { acc: Boolean, bool: Boolean -> acc and bool }
    }
}