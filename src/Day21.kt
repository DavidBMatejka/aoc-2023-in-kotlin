import kotlin.math.pow

fun main() {

    data class Pos (val x: Int, val y: Int, val parent: Pos?) {
        override fun equals(other: Any?): Boolean {
            return other is Pos && other.x == this.x && other.y == this.y
        }

        override fun hashCode(): Int {
            var result = x
            result = 31 * result + y
            return result
        }
    }
    data class Cell(val content: Char, var visited: Boolean, val time: Int )

    fun getPossibleMoves(current: Pos, width: Int, height: Int, map: Array<Array<Cell>>, visited: MutableList<Pos>): List<Pos> {
        val moves = mutableListOf<Pos>()
        if (current.x > 0 && map[current.y][current.x - 1].content != '#') {
            val move = Pos(current.x - 1, current.y, current)
            if (move !in visited && move !in moves) moves.add(move)
        }
        if (current.x < width - 1 && map[current.y][current.x + 1].content != '#') {
            val move = Pos(current.x + 1, current.y, current)
            if (move !in visited && move !in moves) moves.add(move)
        }
        if (current.y > 0 && map[current.y - 1][current.x].content != '#') {
            val move = Pos(current.x, current.y - 1, current)
            if (move !in visited && move !in moves) moves.add(move)
        }
        if (current.y < height - 1 && map[current.y + 1][current.x].content != '#') {
            val move = Pos(current.x, current.y + 1, current)
            if (move !in visited && move !in moves) moves.add(move)
        }

        return moves
    }

    fun countSteps(start: Pos, map: Array<Array<Cell>>, steps: Int): Int {
        val width = map[0].size
        val height = map.size
        var steps = steps
        val q = mutableListOf<Pos>()
        q.add(start)
        map[start.y][start.x] = Cell('.', false, 0)
        val visited = mutableListOf <Pos>()
        var time = 0

        while (steps > 0) {
            val current = mutableListOf<Pos>()
            q.forEach {
                val possibleMoves = getPossibleMoves(it, width, height, map, visited)
                possibleMoves.forEach { possibleMove -> if (possibleMove !in current) current.add(possibleMove) }
            }
            q.clear()

            current.forEach { if (it !in visited) visited.add(it) }
            q.addAll(current)

            time++

            for (move in current) {
                map[move.y][move.x] = Cell('O', true, time)
            }
            steps--
        }

        var sum = 0
        for ((i, line) in map.withIndex()) {
            for ((j, c) in line.withIndex()) {
                if (map[i][j].visited && (c.time % 2) == 0) {
                    sum++
                }
            }
        }
        return sum
    }

    fun part1(input: List<String>): Int {
        var start = Pos(0, 0, null)
        val width = input[0].length
        val height = input.size
        val map = Array(height) {Array(width) {Cell('.', false, 0)} }

        input.forEachIndexed { i, it ->
            if (it.contains("S")) {
                start = Pos(it.indexOf("S"), i, null)
            }
            for (j in 0..<width) {
                map[i][j] = Cell(it[j], false, 0)
            }
        }

        return countSteps(start, map, 64)
    }

    fun part2(input: List<String>): Int {
        var start = Pos(0, 0, null)
        val width = input[0].length
        val height = input.size
        val map = Array(height) {Array(width) {Cell('.', false, 0)} }

        input.forEachIndexed { i, it ->
            if (it.contains("S")) {
                start = Pos(it.indexOf("S"), i, null)
            }
            for (j in 0..<width) {
                map[i][j] = Cell(it[j], false, 0)
            }
        }
        println(start)

        val steps = 26501365L
        val gridWidth = (steps / width) - 1
        val odd = ((gridWidth / 2) * 2 + 1.0).pow(2.0).toLong()
        val even = ((gridWidth + 1) / 2 * 2.0).pow(2.0).toLong()

        println("$odd $even")
        val oddPoints = countSteps(start, map, width * 2 + 1)
        val evenPoints = countSteps(start, map, width * 2)
        println("$oddPoints $evenPoints")
        return -1
    }

    val input = readInput("Day21")
    part1(input).println()
    part2(input).println()
}