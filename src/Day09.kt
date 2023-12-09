fun main() {

    fun extrapolate(sequence: List<Long>): Long {
        if (sequence.all { it == 0L }) {
            return 0L
        }

        var i = 0
        val new = emptyList<Long>().toMutableList()
        while (i < sequence.size - 1) {
            new.add(sequence[i + 1] - sequence[i])
            i++
        }
        return (extrapolate(new) + sequence.last())
    }

    fun part1(input: List<String>): Long {
        var sum: Long = 0
        for (line in input) {
            val next =  extrapolate(line.split(" ").map { it.toLong() })
            sum += next
        }

        return sum
    }

    fun part2(input: List<String>): Long {
        var sum: Long = 0
        for (line in input) {
            val rev = line.split(" ").reversed()
            val next =  extrapolate(rev.map { it.toLong() })
            sum += next
        }

        return sum
    }

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
