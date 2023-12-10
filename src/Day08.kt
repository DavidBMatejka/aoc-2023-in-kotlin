
fun main() {
    data class Node(val name: String, val left: String, val right: String)

    fun part1(input: List<String>): Int {
        val path = input[0]
        val nodes = emptyMap<String, Node>().toMutableMap()
        for (i in 2..<input.size) {
            val (node, _, left, right) = input[i].filterNot { it.isWhitespace() }.split("=", "(", ")", ",")
            nodes[node] = Node(node, left, right)
        }

        var currentPosition = nodes["AAA"]
        var counter = 0
        while (currentPosition!!.name != "ZZZ") {
            for (direction in path) {
                currentPosition = if (direction == 'R') {
                    nodes[currentPosition!!.right]
                } else nodes[currentPosition!!.left]
                counter++
            }
        }
        return counter
    }

    fun part2(input: List<String>): Long {
        val path = input[0]
        val nodes = emptyMap<String, Node>().toMutableMap()
        for (i in 2..<input.size) {
            val (node, _, left, right) = input[i].filterNot { it.isWhitespace() }.split("=", "(", ")", ",")
            nodes[node] = Node(node, left, right)
        }

        val currentPositions = emptyList<String>().toMutableList()
        for (node in nodes) {
            if (node.component1()[2] == 'A') {
                currentPositions.add(node.component1())
            }
        }

        val list = emptyList<Long>().toMutableList()
        for (startingPositions in currentPositions) {
            var counter: Long = 0
            var currentPosition: String = startingPositions
            while (currentPosition.last() != 'Z') {
                for (direction in path) {
                    currentPosition = if (direction == 'R') {
                        nodes[currentPosition]!!.right
                    } else {
                        nodes[currentPosition]!!.left
                    }
                    counter++
                    if (currentPosition.last() == 'Z') break
                }
            }
            list.add(counter)
        }

        return lcm(list.toLongArray())
    }

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}

// didn't bother implementing lcm and gcd myself... sorry
// source: https://stackoverflow.com/questions/4201860/how-to-find-gcd-lcm-on-a-set-of-numbers
private fun lcm(a: Long, b: Long): Long {
    return a * (b / gcd(a, b))
}

private fun lcm(input: LongArray): Long {
    var result = input[0]
    for (i in 1 until input.size) result = lcm(result, input[i])
    return result
}

@Suppress("NAME_SHADOWING")
private fun gcd(a: Long, b: Long): Long {
    var a = a
    var b = b
    while (b > 0) {
        val temp = b
        b = a % b
        a = temp
    }
    return a
}