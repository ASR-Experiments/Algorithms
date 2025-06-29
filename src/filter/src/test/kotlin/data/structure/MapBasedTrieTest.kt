package data.structure

import org.asr.example.data.structure.MapBasedTrie
import org.asr.example.util.randomStrings
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class MapBasedMapBasedTrieTest {

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
        val trieFilter = MapBasedTrie('x')
        trieFilter.add(*testData)
        for (data in testData) {
            assert(trieFilter.search(data)) { "Expected $data to exist in the Bloom filter" }
        }
    }

    @Test
    fun testSearch_whenTestedWithNonExistentData_thenMayReturnTrue() {
        val trieFilter = MapBasedTrie('x')
        trieFilter.add(*testData)
        for (data in nonExistentData) {
            assert(!trieFilter.search(data)) { "Expected $data to exist in the Bloom filter" }
        }
    }

    @Test
    fun add() {
        val trieFilter = MapBasedTrie('x')
        val result = trieFilter.add(*testData)
        assert(result) { "Expected all items to be added successfully to the Bloom filter" }
    }

    @RepeatedTest(10)
    fun testSearch_whenLoadedWithHugeNumberOfElements_thenExpectFewFalsePositives() {
        val trieFilter = MapBasedTrie('x')
        val testData = randomStrings(count = 10_000)
        trieFilter.add(*testData)
        (1..10)
            .map { testData.random() }
            .forEach { data ->
                assert(trieFilter.search(data)) {
                    "Expected `$data` to exist in Bloom filter"
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
            ${timeTookInNs.toDuration(DurationUnit.NANOSECONDS)} to search all strings in a Map based Trie
        """.trimIndent()
                .replace("\\s+".toRegex(), " ")
        )
    }
}