fun main() {

    fun part1(input: List<String>): Int {
        var sum = 0
        for (line in input) {

            val numbersInLine = line.filter { it.isDigit() }
            sum += (numbersInLine.first() + "" + numbersInLine.last()).toInt()
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 0

        for (line in input) {
            // thanks for the tip  to change to o1e,... instead of just 1. smart idea
            // https://www.reddit.com/r/adventofcode/comments/1884fpl/2023_day_1for_those_who_stuck_on_part_2/
           val newline = line
                .replace("one", "o1e")
                .replace("two", "t2o")
                .replace("three", "t3e")
                .replace("four", "f4r")
                .replace("five", "f5e")
                .replace("six", "s6x")
                .replace("seven", "s7n")
                .replace("eight", "e8t")
                .replace("nine", "n9e")
            val numbersInLine = newline.filter { it.isDigit() }
            sum += (numbersInLine.first() + "" + numbersInLine.last()).toInt()
        }

        return sum
    }

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}