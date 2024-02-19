import kotlin.math.pow

fun main() {

    data class Pos(val x: Int, val y: Int) {
        fun isPossible(map: Array<Array<Char>>, infiniteMap: Boolean = false): Boolean {
            var tmpX = x
            var tmpY = y

            if (infiniteMap) {
                tmpX = x % map.size
                tmpY = y % map.size
                while (tmpX < 0) tmpX += map.size
                while (tmpY < 0) tmpY += map.size
            }

            if (tmpX !in map.indices || tmpY !in map.indices) return false
            return map[tmpY][tmpX] != '#'
        }
    }

    data class Vel(val vx: Int, val vy: Int)

    val dirs = mapOf(
        "R" to Vel(1, 0),
        "L" to Vel(-1, 0),
        "U" to Vel(0, -1),
        "D" to Vel(0, 1)
    )

    fun toMap(input: List<String>): Array<Array<Char>> {
        val width = input[0].length
        val height = input.size
        val map = Array(height) { Array(width) { '.' } }

        input.forEachIndexed { i, it ->
            for (j in 0..<width) {
                map[i][j] = it[j]
            }
        }
        return map
    }

    fun bfs(map: Array<Array<Char>>, stepsToTake: Int, infiniteMap: Boolean = false): Map<Pos, Int> {
        val q = mutableListOf<Pos>()
        val seen = mutableMapOf<Pos, Int>()
        val start = Pos(map.size / 2, map.size / 2)
        q.add(start)
        seen[start] = 0

        var stepsRemaining = stepsToTake
        var steps = 1
        while (stepsRemaining > 0) {
            val current = mutableListOf<Pos>()
            q.forEach { current.add(it) }
            q.clear()

            current.forEach {
                dirs.forEach { (_, dir) ->
                    val next = Pos(it.x + dir.vx, it.y + dir.vy)
                    if (next !in seen && next.isPossible(map, infiniteMap)) {
                        seen[next] = steps
                        q.add(next)
                    }
                }
            }

            steps++
            stepsRemaining--
        }
        return seen
    }

    fun part1(input: List<String>): Int {
        val map = toMap(input)
        val seen = bfs(map, 64)

        return seen.filter {it.value % 2 == 0 }.size
    }

    // src: https://colab.research.google.com/github/derailed-dash/Advent-of-Code/blob/master/src/AoC_2023/Dazbo's_Advent_of_Code_2023.ipynb#scrollTo=JxcyYSRWDvYy
    fun part2(input: List<String>): Long {
        val map = toMap(input)

        val stepCounts = mutableListOf<Long>()
        stepCounts.add(bfs(map, 65, true).filter { it.value % 2 == 1 }.size.toLong())
        stepCounts.add(bfs(map, 196, true).filter { it.value % 2 == 0 }.size.toLong())
        stepCounts.add(bfs(map, 327, true).filter { it.value % 2 == 1 }.size.toLong())

        val a = (stepCounts[2] - (2 * stepCounts[1]) + stepCounts[0]) / 2
        val b = stepCounts[1] - stepCounts[0] - a
        val c = stepCounts[0]

        val x = (26501365 - map.size / 2) / map.size

        return (a * x.toDouble().pow(2) + b * x + c).toLong()
    }

    val input = readInput("Day21")
    part1(input).println()
    part2(input).println()
}