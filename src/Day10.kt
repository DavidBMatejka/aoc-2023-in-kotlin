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
            "Left" to "F-J"
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
                if ((x + dx) !in map[0].indices || (y + dy) !in map.indices) continue

                val neighbour = Node(x + dx, y + dy, map[y + dy][x + dx], distance + 1)

                val neighbourContent = map[y + dy][x + dx]

                if (neighbourContent !in parts) continue

                if (allowedNeighboursOf[this.content]?.get(dir.key)?.contains(neighbourContent) == true) {
                    neighbours.add(Node(this.x + dx, this.y + dy, neighbourContent, this.distance + 1))
                }
           }
            return neighbours
        }
    }

    fun getPossibleNeighbours(node: Node, map: Array<Array<Char>>): List<Node> {
        val width = map[0].size
        val height = map.size
        val possibleNeighbours = mutableListOf<Node>()
        if (node.x > 0) {
            val left = map[node.y][node.x - 1]
            var content = ' '
            var adding = false
            when (left) {
                'L' -> { content = 'L'; adding = true }
                '-' -> { content = '-'; adding = true }
                'F' -> {content = 'F'; adding = true }
            }
            if (adding) { possibleNeighbours.add(Node(node.x-1, node.y, content, node.distance + 1)) }
        }
        if (node.x < width - 1) {
            val right = map[node.y][node.x + 1]
            var content = ' '
            var adding = false
            when (right) {
                '-' -> { content = '-'; adding = true }
                '7' -> { content = '7'; adding = true }
                'J' -> { content = 'J'; adding = true }
            }
            if (adding) { possibleNeighbours.add(Node(node.x+1, node.y, content, node.distance + 1)) }
        }
        if (node.y > 0) {
            val up = map[node.y - 1][node.x]
            var content = ' '
            var adding = false
            when (up) {
                '|' -> { content = '|'; adding = true }
                '7' -> { content = '7'; adding = true }
                'F' -> { content = 'F'; adding = true }
            }
            if (adding) { possibleNeighbours.add(Node(node.x, node.y-1, content, node.distance + 1)) }
        }
        if (node.y < height - 1) {
            val down = map[node.y + 1][node.x]
            var content = ' '
            var adding = false
            when (down) {
                '|' -> { content = '|'; adding = true }
                'L' -> { content = 'L'; adding = true }
                'J' -> { content = 'J'; adding = true }
            }
            if (adding) { possibleNeighbours.add(Node(node.x, node.y+1, content, node.distance + 1)) }
        }
        return possibleNeighbours
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

//        map.forEach { it -> it.forEach { print(it) }; println() }

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
            val neighbours:List<Node> = getPossibleNeighbours(current, map)
//            val neighbours = current.getNeighbours(map)
            q.addAll(neighbours)
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
        val x = 3
        val y = 2
        println(map[y][x])
        val start = Node(x, y, map[y][x], 0)
        println(start.getNeighbours(map))

        return -1
    }


    val input = readInput("Day10")
    part1(input).println()
//    part2(input).println()

}
