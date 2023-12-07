fun main() {

    fun numberOfWays(time: Long, record: Long): Long {
        var counter: Long = 0
        for (speed in 1..time) {
            val distance = (time - speed) * speed
            if (distance > record) {
                counter++
            }
        }

        return counter
    }

    fun part1(input: List<String>): Long {
        val times = input[0].split(":", " ").filter { it.isNotBlank() }.drop(1).map{ it.toLong() }
        val distances = input[1].split(":", " ").filter { it.isNotBlank() }.drop(1).map{ it.toLong() }

        var erg: Long = 1
        for (i in times.indices) {
            erg *= numberOfWays(times[i], distances[i])
        }

        return erg
    }

    fun part2(input: List<String>): Long {
        val time = input[0].split(":").drop(1)[0].filter { !it.isWhitespace() }.toLong()
        val distance = input[1].split(":").drop(1)[0].filter { !it.isWhitespace() }.toLong()

        val erg = numberOfWays(time, distance)

        return erg
    }

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
