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
        val digits = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

        var counter = 0
        for (line in input) {
            counter++
            val indexFirstDigitString = line.indexOfAny(digits)
            val indexLastDigitString = line.lastIndexOfAny(digits)

            var newline = line
            if (indexFirstDigitString > -1) {
                // thanks for the tip: https://www.reddit.com/r/adventofcode/comments/1884fpl/2023_day_1for_those_who_stuck_on_part_2/
                when (line.substring(indexFirstDigitString, indexFirstDigitString+3)) {
                    "one" -> newline = line.replaceFirst("one", "o1e")
                    "two" -> newline = line.replaceFirst("two", "t2o")
                    "six" -> newline = line.replaceFirst("six", "s6x")

                    "fou" -> newline = line.replaceFirst("four", "f4r")
                    "fiv" -> newline = line.replaceFirst("five", "f5e")
                    "nin" -> newline = line.replaceFirst("nine", "n9e")

                    "thr" -> newline = line.replaceFirst("three", "t3e")
                    "sev" -> newline = line.replaceFirst("seven", "s7n")
                    "eig" -> newline = line.replaceFirst("eight", "e8t")
                }
            }
            if (indexLastDigitString > -1) {
                when (line.substring(indexLastDigitString, indexLastDigitString+3)) {
                    "one" -> newline = newline.replaceLast("one", "o1e")
                    "two" -> newline = newline.replaceLast("two", "t2o")
                    "six" -> newline = newline.replaceLast("six", "s6x")

                    "fou" -> newline = newline.replaceLast("four", "f4r")
                    "fiv" -> newline = newline.replaceLast("five", "f5e")
                    "nin" -> newline = newline.replaceLast("nine", "n9e")

                    "thr" -> newline = newline.replaceLast("three", "t3e")
                    "sev" -> newline = newline.replaceLast("seven", "s7n")
                    "eig" -> newline = newline.replaceLast("eight", "e8t")
                }
            }

            val numbersInLine = newline.filter { it.isDigit() }
            sum += (numbersInLine.first() + "" + numbersInLine.last()).toInt()
            //println("$counter newline: $line, numbers in line: $numbersInLine, first Digit: ${numbersInLine.first()}, last Digit: ${numbersInLine.last()} sum: $sum")
        }

        return sum
    }

    val input = readInput("Day01")
    //part1(input).println()
    part2(input).println()
}

// source: https://youtrack.jetbrains.com/issue/KT-45588
@Suppress("ACTUAL_FUNCTION_WITH_DEFAULT_ARGUMENTS")
fun String.replaceLast(oldValue: String, newValue: String, ignoreCase: Boolean = false): String {
    val index = lastIndexOf(oldValue, ignoreCase = ignoreCase)
    return if (index < 0) this else this.replaceRange(index, index + oldValue.length, newValue)
}