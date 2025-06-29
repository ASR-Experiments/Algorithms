package org.asr.example.data.structure

import org.asr.example.filter.FilterInterface

/**
 * Highly dependent on memory, doesn't scale well
 */
data class Trie(
    val value: Char,
    var exists: Boolean = false,
    private val children: Array<Trie?> = arrayOfNulls(65535)
) : FilterInterface {
    fun addChild(value: Char, exists: Boolean = false) {
        val child = Trie(value = value, exists = exists)
        children[value.code] = child
    }

    fun traverse(value: String): Trie? {
        var currentNode: Trie? = this
        for (c in value.toCharArray()) {
            currentNode = currentNode?.children[c.code]
            if (currentNode == null)
                return null
        }
        return currentNode
    }

    fun addString(value: String): Trie {
        var currentNode: Trie = this
        for (c in value.toCharArray()) {
            var tempNode = currentNode.children[c.code]
            if (tempNode == null) {
                tempNode = Trie(value = c)
                currentNode.children[c.code] = tempNode
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Trie

        if (value != other.value) return false
        if (exists != other.exists) return false
        if (!children.contentEquals(other.children)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = value.hashCode()
        result = 31 * result + exists.hashCode()
        result = 31 * result + children.contentHashCode()
        return result
    }
}
