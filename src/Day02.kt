fun main() {

    fun isPossible(amount: Int, color: String): Boolean {
        when (color) {
            "red" -> if (amount <= 12) { return true }
            "green" -> if (amount <= 13) { return true }
            "blue" -> if (amount <= 14) { return true }
        }
        return false
    }

    // check if game is possible for: 12 red cubes, 13 green cubes, and 14 blue cubes.
    fun part1(input: List<String>): Int {
        var sum = 0
        var gameId = 0
        for (line in input) {
            val game = line.substringAfter(":").split(";")
            var possible = true
            gameId++
            for (handful in game) {
                val hand = handful.split(",")
                for (countOfColors in hand) {
                    val countOfColorsArray = countOfColors.split(" ")
                    val amount = countOfColorsArray[1].toInt()
                    val color =  countOfColorsArray[2]
                    if (!isPossible(amount, color)) { possible = false }
                }
            }
            if (possible) { sum += gameId }
        }
        return sum
    }

    fun calcPowerOfLeastAmounts(game: List<String>): Int {
        var leastRed = Int.MIN_VALUE
        var leastGreen = Int.MIN_VALUE
        var leastBlue = Int.MIN_VALUE

        for (handful in game) {
            val hand = handful.split(",")
            for (countOfColors in hand) {
                val countOfColorsArray = countOfColors.split(" ")
                val amount = countOfColorsArray[1].toInt()
                val color =  countOfColorsArray[2]
                when(color) {
                    "red" -> if (amount > leastRed) { leastRed = amount }
                    "green" -> if (amount > leastGreen) { leastGreen = amount }
                    "blue" -> if (amount > leastBlue) { leastBlue = amount }
                }
            }
        }
        return (leastRed * leastBlue * leastGreen)
    }

    fun part2(input: List<String>): Int {
        var sum = 0
        for (line in input) {
            val game = line.substringAfter(":").split(";")
            sum += calcPowerOfLeastAmounts(game)
        }
        return sum
    }

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}