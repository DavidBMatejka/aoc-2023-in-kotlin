import kotlin.math.abs

fun main() {

    data class Pos(var x: Int, var y: Int) {
        var d = ""
    }

    data class LongPos(var x: Long, var y: Long)

    val dirs = mapOf(
        "R" to Pos(1, 0),
        "L" to Pos(-1, 0),
        "U" to Pos(0, -1),
        "D" to Pos(0, 1)
    )

    fun part1(input: List<String>): Int {
        val digPlan = mutableListOf<List<String>>()
        input.forEach {
            digPlan.add(it.split(" "))
        }

        val visited = mutableSetOf<Pos>()

        var current = Pos(0, 0)
        current.d = digPlan[0][0]
        visited.add(current)
        var maxX = 0
        var maxY = 0
        var minX = 0
        var minY = 0

        for (item in digPlan) {
            val (dir, steps, _) = item
            val (dx, dy) = dirs[dir]!!
            for (i in 0..<steps.toInt()) {
                current = Pos(current.x + dx, current.y + dy)
                current.d = dir
                visited.add(current)
                if (current.x < minX) {
                    minX = current.x
                }
                if (current.x > maxX) {
                    maxX = current.x
                }
                if (current.y < minY) {
                    minY = current.y
                }
                if (current.y > maxY) {
                    maxY = current.y
                }
            }
        }

        // shift all positions to positive values
        visited.forEach {
            it.x += abs(minX)
            it.y += abs(minY)
        }
        maxX += abs(minX)
        maxY += abs(minY)

        // converting the saved positions in visited to an Array grid with a modified border to mark corners with J, L, F and q
        val terrain = Array(maxY + 1){ Array(maxX + 1){"."} }
        var prev = Pos(-1, -1)
        visited.forEach {
            var s = "F"

            if (prev.d != "") {
                if (prev.d == it.d) {
                    s = if (it.d == "R" || it.d == "L") {
                        "-"
                    } else {
                        "|"
                    }
                } else {
                    if (prev.d == "R" && it.d == "U") {
                        terrain[prev.y][prev.x] = "J"
                        s = "|"
                    } else if (prev.d == "R" && it.d == "D") {
                        terrain[prev.y][prev.x] = "q"
                        s = "|"
                    } else if (prev.d == "D" && it.d == "L") {
                        terrain[prev.y][prev.x] = "J"
                        s = "-"
                    } else if (prev.d == "U" && it.d == "L") {
                        terrain[prev.y][prev.x] = "q"
                        s = "-"
                    } else if (prev.d == "L" && it.d == "D") {
                        terrain[prev.y][prev.x] = "F"
                        s = "|"
                    } else if (prev.d == "L" && it.d == "U") {
                        terrain[prev.y][prev.x] = "L"
                        s = "|"
                    } else if (prev.d == "U" && it.d == "R") {
                        terrain[prev.y][prev.x] = "F"
                        s = "-"
                    } else if (prev.d == "D" && it.d == "R") {
                        terrain[prev.y][prev.x] = "L"
                        s = "-"
                    }
                }
            }
            terrain[it.y][it.x] = s
            prev = it
        }

        // detecting inside
        var c = 0
        var lastChar = ""
        terrain.forEachIndexed { j , item ->
            var inArea = false
            item.forEachIndexed { i, s ->
                if (!inArea && s == "|") {
                    inArea = true
                } else if (inArea && s == "|") {
                    inArea = false
                }

                if (s == "F") {
                    lastChar = "F"
                }
                if (s == "J" && lastChar == "F") {
                    inArea = !inArea
                }
                if (s == "L") {
                    lastChar = "L"
                }
                if (s == "q" && lastChar == "L") {
                    inArea = !inArea
                }

                if (inArea && s == ".") {
                    terrain[j][i] = "x"
                    c++
                }
            }
        }

        return (c + visited.size)
    }

    fun part2(input: List<String>): Long {
        // shoelace formula and pick's theorem

        val splitted = mutableListOf<List<String>>()
        input.forEach {
            splitted.add(it.split(" "))
        }

        val points = mutableListOf<LongPos>()

        var current = LongPos(0, 0)

        var b = 0L
        val directions = "RDLU"
        for (item in splitted) {
            var (_, _, hex) = item
            hex = hex.drop(2).dropLast(1)

            val dir = directions[hex.last().digitToInt()].toString()
            val steps = hex.dropLast(1).toLong(16)

            b += steps

            val (dx, dy) = dirs[dir]!!

            current = LongPos(current.x + (dx * steps), current.y + (dy * steps))
            points.add(current)
        }

        var A = 0L
        for (i in 0..<points.size) {
            var j = i - 1
            var k = i + 1
            if (j == -1) j = points.size - 1
            if (k == points.size) k = 0
            A += points[i].x * (points[k].y - points[j].y)
        }
        A = abs(A/2L)
        val interior = A - b / 2L + 1L

        return (interior + b)
    }

    val input = readInput("Day18")
    part1(input).println()
    part2(input).println()
}