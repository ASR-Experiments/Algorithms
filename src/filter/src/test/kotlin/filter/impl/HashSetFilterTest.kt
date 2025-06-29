package filter.impl

import org.asr.example.filter.impl.HashSetFilter
import org.asr.example.util.randomStrings
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class HashSetFilterTest {

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
        val filter = HashSetFilter()
        filter.add(*testData)
        for (data in testData) {
            assert(filter.search(data)) { "Expected $data to exist in the Bloom filter" }
        }
    }

    @Test
    fun testSearch_whenTestedWithNonExistentData_thenMayReturnTrue() {
        val filter = HashSetFilter()
        filter.add(*testData)
        for (data in nonExistentData) {
            assert(!filter.search(data)) { "Expected $data to exist in the Bloom filter" }
        }
    }

    @Test
    fun add() {
        val filter = HashSetFilter()
        val result = filter.add(*testData)
        assert(result) { "Expected all items to be added successfully to the Bloom filter" }
    }

    @RepeatedTest(10)
    fun testSearch_whenLoadedWithHugeNumberOfElements_thenExpectFewFalsePositives() {
        val filter = HashSetFilter()
        val testData = randomStrings(count = 10_000)
        filter.add(*testData)
        (1..10)
            .map { testData.random() }
            .forEach { data ->
                assert(filter.search(data)) {
                    "Expected `$data` to exist in Bloom filter"
                }
            }
        val newSample = randomStrings(count = 10_000)
        var matched = 0
        val startTime = System.nanoTime()
        for (sample in newSample) {
            if (filter.search(sample))
                matched += 1
        }
        val timeTookInNs = System.nanoTime() - startTime
        println(
            """
            Matched $matched among new samples and took total time of 
            ${timeTookInNs.toDuration(DurationUnit.NANOSECONDS)} to search all strings in Hash Set Filter
        """.trimIndent()
                .replace("\\s+".toRegex(), " ")
        )
    }
}