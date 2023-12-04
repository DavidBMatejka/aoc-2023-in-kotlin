fun main() {

    fun part1(input: List<String>): Int {
        var sum = 0
        for (line in input) {
            var counter = 0
            val winningNumbers = line
                .substringAfter(": ")
                .substringBefore(" |")
                .split(" ")
                .dropWhile { it.isBlank() }
            val numbers = line
                .substringAfter("| ")
                .split(" ")

            for (number in numbers) {
                if (number.isBlank() || !winningNumbers.contains(number)) { continue }
                else if (counter == 0) { counter++ }
                else if (counter > 0) counter *= 2
            }
            sum += counter
        }
        return sum
    }


    fun part2(input: List<String>): Int {
        var sum = 0
        val copiesOfCards = emptyMap<Int, Int>().toMutableMap()
        for (i in 1..input.size) {
            copiesOfCards[i] = 1
        }

        var gameNumber = 0
        for (line in input) {
            var counter = 0
            gameNumber++
            val winningNumbers = line
                .substringAfter(": ")
                .substringBefore(" |")
                .split(" ")
                .dropWhile { it.isBlank() }
            val numbers = line
                .substringAfter("| ")
                .split(" ")

            for (number in numbers) {
                if (number.isBlank() || !winningNumbers.contains(number)) { continue }
                else if (winningNumbers.contains(number)) { counter++ }
            }

            val amount = copiesOfCards[gameNumber]!!.toInt()
            for (i in 1 + gameNumber..counter + gameNumber) {
                if (i > input.size) break

                copiesOfCards.replace(i, copiesOfCards[i]!! + amount)
            }

        }

        for (copy in copiesOfCards) {
            sum += copy.value
        }
        return sum
    }

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}