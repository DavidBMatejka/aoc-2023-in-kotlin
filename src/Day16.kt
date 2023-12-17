fun main() {

    data class Beam(var x: Int, var y: Int, var dx: Int, var dy: Int) {
        fun move(grid: MutableList<List<String>>): List<Beam> {
            val beams = mutableListOf<Beam>()
            when {
                grid[y][x] == "." && x + dx >= 0 && x + dx < grid.size && y + dy >= 0 && y + dy < grid.size -> {
                    beams.add(Beam(x + dx, y + dy, dx, dy))
                }

                grid[y][x] == "-" && dy == 0 && x + dx >= 0 && x + dx < grid.size -> {
                    beams.add(Beam(x + dx, y, dx, dy))
                }

                grid[y][x] == "-" && dy != 0 -> {
                    if (x - 1 >= 0) beams.add(Beam(x - 1, y, -1, 0))
                    if (x + 1 < grid.size) beams.add(Beam(x + 1, y, 1, 0))
                }

                grid[y][x] == "|" && dx == 0 && y + dy >= 0 && y + dy < grid.size -> {
                    beams.add(Beam(x, y + dy, dx, dy))
                }

                grid[y][x] == "|" && dx != 0 -> {
                    if (y - 1 >= 0) beams.add(Beam(x, y - 1, 0, -1))
                    if (y + 1 < grid.size) beams.add(Beam(x, y + 1, 0, 1))
                }

                grid[y][x] == "/" && dx == 1 && y - 1 >= 0 -> {
                    beams.add(Beam(x, y - 1, 0, -1))
                }

                grid[y][x] == "/" && dx == -1 && y + 1 < grid.size -> {
                    beams.add(Beam(x, y + 1, 0, 1))
                }

                grid[y][x] == "/" && dy == 1 && x - 1 >= 0 -> {
                    beams.add(Beam(x - 1, y, -1, 0))
                }

                grid[y][x] == "/" && dy == -1 && x + 1 < grid.size -> {
                    beams.add(Beam(x + 1, y, 1, 0))
                }

                grid[y][x] == "\\" && dx == 1 && y + 1 < grid.size -> {
                    beams.add(Beam(x, y + 1, 0, 1))
                }

                grid[y][x] == "\\" && dx == -1 && y - 1 >= 0 -> {
                    beams.add(Beam(x, y - 1, 0, -1))
                }

                grid[y][x] == "\\" && dy == 1 && x + 1 < grid.size -> {
                    beams.add(Beam(x + 1, y, 1, 0))
                }

                grid[y][x] == "\\" && dy == -1 && x - 1 >= 0 -> {
                    beams.add(Beam(x - 1, y, -1, 0))
                }
            }

            return beams
        }
    }

    fun energize(beam: Beam, grid: MutableList<List<String>>): Int {
        val energized = Array(grid.size) { Array(grid.size) { false } }
        val alreadyVisited = mutableListOf<Beam>()

        val q = ArrayDeque<Beam>()
        q.add(beam)

        var sum = 0
        while (q.isNotEmpty()) {
            val current = q.removeFirst()

            if (alreadyVisited.contains(current)) {
                continue
            }
            alreadyVisited.add(current)

            if (!energized[current.y][current.x]) {
                energized[current.y][current.x] = true
                sum++
            }

            for (newBeam in current.move(grid)) q.add(newBeam)
        }

        return sum
    }

    fun part1(input: List<String>): Int {
        val grid = mutableListOf<List<String>>()
        for (line in input) {
            grid.add(line.split("").drop(1).dropLast(1))
        }

        return energize(Beam(0, 0, 1, 0), grid)
    }

    fun part2(input: List<String>): Int {
        val grid = mutableListOf<List<String>>()
        for (line in input) {
            grid.add(line.split("").drop(1).dropLast(1))
        }

        val energizeList = mutableListOf<Int>()
        for (i in 1..grid.size - 2) {
            energizeList.add(energize(Beam(i, 0, 0, 1), grid))
        }
        for (i in 1..grid.size - 2) {
            energizeList.add(energize(Beam(i, grid.size - 1, 0, -1), grid))
        }
        for (j in 1..grid.size - 2) {
            energizeList.add(energize(Beam(0, j, 1, 0), grid))
        }
        for (j in 1..grid.size - 2) {
            energizeList.add(energize(Beam(grid.size - 1, j, -1, 0), grid))
        }

        energizeList.add(energize(Beam(0, 0, 1, 0), grid))
        energizeList.add(energize(Beam(0, 0, 0, 1), grid))

        energizeList.add(energize(Beam(grid.size - 1, 0, -1, 0), grid))
        energizeList.add(energize(Beam(grid.size - 1, 0, 0, 1), grid))

        energizeList.add(energize(Beam(0, grid.size - 1, 1, 0), grid))
        energizeList.add(energize(Beam(0, grid.size - 1, 0, -1), grid))

        energizeList.add(energize(Beam(grid.size - 1, grid.size - 1, -1, 0), grid))
        energizeList.add(energize(Beam(grid.size - 1, grid.size - 1, 0, -1), grid))

        return energizeList.max()
    }

    val input = readInput("Day16")

    part1(input).println()
    part2(input).println()
}