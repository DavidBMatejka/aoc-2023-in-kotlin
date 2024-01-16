fun main() {

    val dirs = mapOf(
        "Right" to Pair(1, 0),
        "Left" to Pair(-1, 0),
        "Up" to  Pair(0, -1),
        "Down" to Pair(0, 1)
    )
    val allowedDirections = mapOf(
        'S' to listOf("Up", "Down", "Left", "Right"),
        'F' to listOf("Down", "Right"),
        '-' to listOf("Right", "Left"),
        '7' to listOf("Left", "Down"),
        'L' to listOf("Up", "Right"),
        '|' to listOf("Up", "Down"),
        'J' to listOf("Up", "Left")
    )
    val allowedPartsForDirection = mapOf(
        "Up" to "F7|",
        "Down" to "L|J",
        "Right" to "-7J",
        "Left" to "F-L"
    )

    data class Node (val x: Int, val y: Int, val content: Char, var distance: Int) {
        override fun equals(other: Any?): Boolean {
            return other is Node && other.x == this.x && other.y == this.y
        }

        override fun hashCode(): Int {
            var result = x
            result = 31 * result + y
            return result
        }

        fun getNeighbours(map: Array<Array<Char>>): List<Node> {
            val neighbours = mutableListOf<Node>()

            val allowedDirs = allowedDirections[this.content] ?: return emptyList()
            for (dir in allowedDirs) {
                val (dx, dy) = dirs[dir] ?: Pair(0, 0)

                if ((x + dx) !in map[0].indices || (y + dy) !in map.indices) {
                    continue
                }

                val neighbourContent = map[y + dy][x + dx]
                if (allowedPartsForDirection[dir]?.contains(neighbourContent) == true) {
                    neighbours.add(Node(this.x + dx, this.y + dy, neighbourContent, this.distance + 1))
                }
            }
            return neighbours
        }
    }

    fun part1(input: List<String>): Int {
        val height = input.size
        val width = input[0].length
        val map = Array(height) {Array(width) {'.'} }

        val q = ArrayDeque<Node>()
        for ((i, line) in input.withIndex()) {
            for ((j, s) in line.withIndex()) {
                map[i][j] = s
                if (map[i][j] == 'S') q.add(Node(j, i, 'S', 0))
            }
        }

        var maxDistance = 0
        val visited = emptyList<Node>().toMutableList()

        while (!q.isEmpty()) {
            val current = q.removeFirst()
            if (visited.contains(current)) {
                continue
            }

            if (current.distance > maxDistance) {
                maxDistance = current.distance
            }
            visited.add(current)

            q.addAll(current.getNeighbours(map))
        }

        return maxDistance
    }

    fun part2(input: List<String>): Int {
        val height = input.size
        val width = input[0].length
        val map = Array(height) {Array(width) {'.'} }
        val mapWithoutTrash = Array(height) {Array(width) {'.'} }

        val q = ArrayDeque<Node>()
        for ((i, line) in input.withIndex()) {
            for ((j, s) in line.withIndex()) {
                map[i][j] = s
                if (map[i][j] == 'S') q.add(Node(j, i, 'S', 0))
            }
        }

        val visited = mutableListOf<Node>()
        while (q.isNotEmpty()) {
            val current = q.removeFirst()

            if (current in visited) {
                continue
            }
            mapWithoutTrash[current.y][current.x] = current.content
            visited.add(current)

            q.addAll(current.getNeighbours(map))
        }

        /* detecting inside
        *
        * seeing a wall '|' means we are inside and seeing a wall again means we're outside again
        *
        * need to consider walking on a edge:
        *                                    ............
        * walking from here on to an edge -> ..F-----7... would end up outside
        *                                    ..|.....|...
        *                                    ..|.....|...
        *                                   .............
        *
        *                                    ........|...
        *                                    ........|...
        * walking from here on to an edge -> ..F-----J... would end up inside
        *                                    ..|........
        *                                    ..|........
        *
        * same reasoning for vertical movement
        *
        * */
        var counterInside = 0
        var lastChar = ' '
        mapWithoutTrash.forEach {
            var enclosed = false
            it.forEach { s ->
                if (!enclosed && s == '|') {
                    enclosed = true
                } else if (enclosed && s == '|') {
                    enclosed = false
                }
                if (s == 'F') {
                    lastChar = 'F'
                }
                if (s == 'J' && lastChar == 'F') {
                    enclosed = !enclosed
                }
                if (s == 'L') {
                    lastChar = 'L'
                }
                if (s == '7' && lastChar == 'L') {
                    enclosed = !enclosed
                }
                if (enclosed && s == '.') {
                    // mapWithoutTrash[j][i] = 'I'
                    counterInside++
                }
            }
        }
        return counterInside
    }

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
