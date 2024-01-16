fun main() {

    val dirs = mapOf(
        "Right" to Pair(1, 0),
        "Left" to Pair(-1, 0),
        "Up" to  Pair(0, -1),
        "Down" to Pair(0, 1)
    )
    val parts = "SF-7L|J"
    val allowedNeighboursOf = mapOf(
        'S' to mapOf(
            "Up" to "F7|",
            "Down" to "L|J",
            "Right" to "-7J",
            "Left" to "F-L"
        ),
        // S could be neighbour but since we start from S this won't occur
        'F' to mapOf(
            "Down" to "L|J",
            "Right" to "-7J",
        ),
        '-' to mapOf(
            "Right" to "-7J",
            "Left" to "F-L"
        ),
        '7' to mapOf(
            "Left" to "F-L" ,
            "Down" to "L|J"
        ),
        'L' to mapOf(
            "Up" to "F7|",
            "Right" to "-7J"
        ),
        '|' to mapOf(
            "Up" to "F7|",
            "Down" to "L|J"
        ),
        'J' to mapOf(
            "Up" to "F7|",
            "Left" to "F-L"
        )
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
            for (dir in dirs) {
                val (dx, dy) = dir.value
                if ((x + dx) !in map[0].indices || (y + dy) !in map.indices) {
                    continue
                }

                val neighbourContent = map[y + dy][x + dx]
                if (neighbourContent !in parts) {
                    continue
                }
                if (allowedNeighboursOf[this.content]?.get(dir.key)?.contains(neighbourContent) == true) {
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

        val q = ArrayDeque<Node>()
        for ((i, line) in input.withIndex()) {
            for ((j, s) in line.withIndex()) {
                map[i][j] = s
                if (map[i][j] == 'S') q.add(Node(j, i, 'S', 0))
            }
        }

        var maxValue = 0
        val visited = mutableListOf<Node>()
        while (q.isNotEmpty()) {
            val current = q.removeFirst()

            if (current in visited) {
                continue
            }
            visited.add(current)

            if (current.distance > maxValue) {
                maxValue = current.distance
            }
            q.addAll(current.getNeighbours(map))
        }
        return maxValue
    }


    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()

}
