package org.asr.example.filter

/**
 * An interface that defines methods for checking existence of strings within collection of existing strings.
 */
interface FilterInterface {

    /**
     * Checks if the given string exists in the collection
     *
     * @param inputString The string to check for existence
     * @return `true` if the string exists, `false` otherwise.
     */
    fun search(inputString: String): Boolean

    /**
     * Adds one or more strings to the collection
     *
     * @param inputStrings The strings to be added.
     * @return `true` if the strings were successfully added, `false` if one or more strings were failed to add in
     * the filter, whereas rest of the strings might be updated in the filter.
     */
    fun add(vararg inputStrings: String): Boolean

}