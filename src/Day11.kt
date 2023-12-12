import kotlin.math.abs

fun main() {

    data class Galaxy(var x: Long, var y: Long)

    fun inverse(original: List<String>): List<String> {
        val height = original.size
        val width = original[0].length

        val inversedArray = Array(width) { Array(height) { '.' } }
        for ((i, line) in original.withIndex()) {
            for ((j, s) in line.withIndex()) {
                inversedArray[j][i] = s
            }
        }

        val inversed = mutableListOf<String>()
        for (line in inversedArray) {
            var tmp = ""
            for (s in line) {
                tmp += s
            }
            inversed.add(tmp)
        }

        return inversed
    }

    fun distance(galaxy1: Galaxy, galaxy2: Galaxy): Long {
        val dx = abs(galaxy1.x - galaxy2.x)
        val dy = abs(galaxy1.y - galaxy2.y)
        return dx + dy
    }

    fun expand(
        galaxies: MutableList<Galaxy>,
        emptyRows: MutableList<Int>,
        emptyColumns: MutableList<Int>,
        expansion: Int
    ) {
        for (galaxy in galaxies) {
            var cy = 0
            var cx = 0
            for (emptyRow in emptyRows) {
                if (galaxy.y > emptyRow) cy++
            }
            for (emptyColumn in emptyColumns) {
                if (galaxy.x > emptyColumn) cx++
            }
            galaxy.x += cx * (expansion - 1)
            galaxy.y += cy * (expansion - 1)
        }
    }

    fun part1(input: List<String>, expansion: Int): Long {
        val emptyRows = emptyList<Int>().toMutableList()
        for ((i, line) in input.withIndex()) {
            if (!line.contains("#")) {
                emptyRows.add(i)
            }
        }

        val inverse = inverse(input)
        val emptyColumns = emptyList<Int>().toMutableList()
        for ((i, line) in inverse.withIndex()) {
            if (!line.contains("#")) {
                emptyColumns.add(i)
            }
        }

        val galaxies = mutableListOf<Galaxy>()
        for ((i, line) in input.withIndex()) {
            for ((j, s) in line.withIndex()) {
                if (s == '#') {
                    galaxies.add(Galaxy(j.toLong(), i.toLong()))
                }
            }
        }

        expand(galaxies, emptyRows, emptyColumns, expansion)

        var sum: Long = 0
        for (i in 0..<galaxies.size) {
            for (j in i + 1..<galaxies.size) {
                sum += distance(galaxies[i], galaxies[j])
            }
        }

        return sum
    }


    fun part2(input: List<String>, expansion: Int): Any {
        return part1(input, expansion)
    }


    val input = readInput("Day11")
    part1(input, 2).println()
    part2(input, 1000000).println()


}

