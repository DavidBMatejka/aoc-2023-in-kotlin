fun main() {

    data class Node (val x: Int, val y: Int, val content: Char, var distance: Int) {
        override fun equals(other: Any?): Boolean {
            return other is Node && other.x == this.x && other.y == this.y
        }

        override fun hashCode(): Int {
            var result = x
            result = 31 * result + y
            return result
        }
    }

    fun getPossibleNeighbours(node: Node, map: Array<Array<Char>>): List<Node> {
        val width = map[0].size
        val height = map.size
        val possibleNeighbours = emptyList<Node>().toMutableList()
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
            val right = map[node.y - 1][node.x]
            var content = ' '
            var adding = false
            when (right) {
                '|' -> { content = '|'; adding = true }
                '7' -> { content = '7'; adding = true }
                'F' -> { content = 'F'; adding = true }
            }
            if (adding) { possibleNeighbours.add(Node(node.x, node.y-1, content, node.distance + 1)) }
        }
        if (node.y < height - 1) {
            val right = map[node.y + 1][node.x]
            var content = ' '
            var adding = false
            when (right) {
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
            if (visited.contains(current)) continue

            if (current.distance > maxDistance) maxDistance = current.distance
            visited.add(current)
            val neighbours:List<Node> = getPossibleNeighbours(current, map)
            q.addAll(neighbours)
            //println("neighbours: $neighbours")
        }

        return maxDistance
    }

    fun part2(input: List<String>): Int {

        return -1
    }


    val input = readInput("Day10")
    part1(input).println()
    //part2(input).println()

}
