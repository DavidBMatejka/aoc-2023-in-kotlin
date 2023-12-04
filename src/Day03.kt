fun main() {
    data class Position(val row: Int, val column: Int, val length: Int)
    data class GearPosition(val x: Int, val y: Int)
    data class GearWithNeighbours(val gear: GearPosition?, var n1: Int, var n2: Int = 0)

    fun checkIfPartnumber(number: MutableMap.MutableEntry<Position, String>, input: List<String> ): Boolean {
        val x = number.key.column
        val y = number.key.row
        for (dy in y-1..y+1) {
            for (dx in x-1..x + number.key.length) {
                if (dx < 0 || dy < 0 || dx >= input.size || dy >= input[0].length) { continue }
                else if (!input[dy][dx].isDigit() && input[dy][dx].toString() != ".") { return true }
            }
        }
        return false
    }

    fun findAllNumbers(input: List<String>): MutableMap<Position, String> {
        val numbersAndTheirPositions = emptyMap<Position, String>().toMutableMap()
        for ((counter, line) in input.withIndex()) {
            var i = 0
            while (i < line.length - 1) {
                var number = ""
                if (line[i].isDigit()) {
                    var length = 0
                    val pos = i
                    while (i < line.length && line[i].isDigit()) {
                        number += line[i]
                        length++
                        i++
                    }
                    numbersAndTheirPositions[Position(counter, pos, length)] = number
                } else {i++}
            }
        }
        return numbersAndTheirPositions
    }

    fun part1(input: List<String>): Int {
        val numbersAndTheirPositions = findAllNumbers(input)

        var sum = 0
        for (number in numbersAndTheirPositions) {
            if(checkIfPartnumber(number, input)) {
                sum += number.value.toInt()
            }
        }
        return sum
    }


    fun findNumberWithGearPositions(number: MutableMap.MutableEntry<Position, String>, input: List<String>): GearWithNeighbours {
        val x = number.key.column
        val y = number.key.row
        for (dy in y-1..y+1) {
            for (dx in x-1..x + number.key.length) {
                if (dx < 0 || dy < 0 || dx >= input.size || dy >= input[0].length) { continue }
                else if (input[dy][dx].toString() == "*") {
                    return GearWithNeighbours(GearPosition(dx, dy), number.value.toInt())
                }
            }
        }
        return GearWithNeighbours(null, -1)
    }

    fun part2(input: List<String>): Int {
        var sum = 0
        val numbersAndTheirPositions = findAllNumbers(input)
        val gears = emptyMap<GearPosition, GearWithNeighbours>().toMutableMap()
        for(number in numbersAndTheirPositions) {
            val (gear, value) = findNumberWithGearPositions(number, input)
            if (gear == null) { continue }
            else if (!gears.containsKey(gear)) {
                gears[gear] = GearWithNeighbours(gear, value)
            }
            else if (gears.containsKey(gear)) {
                gears[gear]!!.n2 = value
            }
        }

        for (gear in gears) {
            sum += gear.value.n1 * gear.value.n2
        }


        return sum
    }

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}