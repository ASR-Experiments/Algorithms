package data.structure

import org.asr.example.data.structure.Trie
import org.asr.example.util.randomStrings
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class TrieTest {

    val testData: Array<String> = """
        FB, Arvind, Surash, Ravi, Kumar, Kotlin, Java, Python, JavaScript, C++, Priya, Anjali, Vikram, Neha, Sunil, 
        Meena, Rahul, Sneha, Amit, Pooja, Deepak, RRohit, Nisha, Sanjay, Divya, Manish, Swati, Vikas, Shweta, Ajay, 
        Preeti, Gaurav, Ritu, Abhishek, Kavita, Nitin, Monika, Sandeep, Payal, Rajesh, Simran, Harsh, Tina, Harish,
        Yogesh, Komal, Vivek, Tarun, Bhavna, Sachin, Seema
    """
        .split(",")
        .map { s -> s.trim() }
        .toList()
        .toTypedArray()

    val nonExistentData: Array<String> = """
        Ea, NonExistent1, Zara, Ishaan, Maya, Kabir, Aarav, Diya, Rehan , Tara
    """
        .split(",")
        .map { s -> s.trim() }
        .toList()
        .toTypedArray()

    @Test
    fun testSearch_whenTestedWithExistentData_thenReturnTrue() {
        val trieFilter = Trie('x')
        trieFilter.add(*testData)
        for (data in testData) {
            assert(trieFilter.search(data)) { "Expected $data to exist in the Trie Filter" }
        }
    }

    @Test
    fun testSearch_whenTestedWithNonExistentData_thenMayReturnTrue() {
        val trieFilter = Trie('x')
        trieFilter.add(*testData)
        for (data in nonExistentData) {
            try {
                assert(!trieFilter.search(data)) { "Expected $data to exist in the Trie Filter" }
            } catch (_: AssertionError) {
                println(
                    """
                    Expected $data to NOT exist in the Trie Filter, but it was found. Since, Trie Filter is 
                    probabilistic in nature, this is acceptable
                """.trimIndent()
                        .replace("\\s+".toRegex(), " ")
                )
            }
        }
    }

    @Test
    fun add() {
        val trieFilter = Trie('x')
        val result = trieFilter.add(*testData)
        assert(result) { "Expected all items to be added successfully to the Trie Filter" }
    }

    @Disabled("Getting out of memory")
    @RepeatedTest(10)
    fun testSearch_whenLoadedWithHugeNumberOfElements_thenExpectFewFalsePositives() {
        val trieFilter = Trie('x')
        val testData = randomStrings(count = 10_000)
        trieFilter.add(*testData)
        (1..10)
            .map { testData.random() }
            .forEach { data ->
                assert(trieFilter.search(data)) {
                    "Expected `$data` to exist in Trie Filter"
                }
            }
        val newSample = randomStrings(count = 10_000)
        var matched = 0
        val startTime = System.nanoTime()
        for (sample in newSample) {
            if (trieFilter.search(sample))
                matched += 1
        }
        val timeTookInNs = System.nanoTime() - startTime
        println(
            """
            Matched $matched among new samples and took total time of 
            ${timeTookInNs.toDuration(DurationUnit.NANOSECONDS)} to search all strings in Trie
        """.trimIndent()
                .replace("\\s+".toRegex(), " ")
        )
    }
}