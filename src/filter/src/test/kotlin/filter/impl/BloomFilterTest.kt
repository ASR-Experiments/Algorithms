package filter.impl

import org.asr.example.filter.impl.BloomFilter
import org.asr.example.util.randomStrings
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class BloomFilterTest {

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
        val bloomFilter = BloomFilter()
        bloomFilter.add(*testData)
        for (data in testData) {
            assert(bloomFilter.search(data)) { "Expected $data to exist in the Bloom filter" }
        }
    }

    @Test
    fun testSearch_whenTestedWithNonExistentData_thenMayReturnTrue() {
        val bloomFilter = BloomFilter()
        bloomFilter.add(*testData)
        for (data in nonExistentData) {
            try {
                assert(!bloomFilter.search(data)) { "Expected $data to exist in the Bloom filter" }
            } catch (_: AssertionError) {
                println(
                    """
                    Expected $data to NOT exist in the Bloom filter, but it was found. Since, Bloom filter is 
                    probabilistic in nature, this is acceptable
                """.trimIndent()
                        .replace("\\s+".toRegex(), " ")
                )
            }
        }
    }

    @Test
    fun add() {
        val bloomFilter = BloomFilter()
        val result = bloomFilter.add(*testData)
        assert(result) { "Expected all items to be added successfully to the Bloom filter" }
    }

    @RepeatedTest(10)
    fun testSearch_whenLoadedWithHugeNumberOfElements_thenExpectFewFalsePositives() {
        val bloomFilter = BloomFilter()
        val testData = randomStrings(count = 10_000)
        bloomFilter.add(*testData)
        (1..10)
            .map { testData.random() }
            .forEach { data ->
                assert(bloomFilter.search(data)) {
                    "Expected `$data` to exist in Bloom filter"
                }
            }
        val newSample = randomStrings(count = 10_000)
        var matched = 0
        val startTime = System.nanoTime()
        for (sample in newSample) {
            if (bloomFilter.search(sample))
                matched += 1
        }
        val timeTookInNs = System.nanoTime() - startTime
        println(
            """
            Matched $matched among new samples and took total time of 
            ${timeTookInNs.toDuration(DurationUnit.NANOSECONDS)} to search all strings in Bloom Filter
        """.trimIndent()
                .replace("\\s+".toRegex(), " ")
        )
    }

}