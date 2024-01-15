import java.util.*
import java.util.Collections.min

fun main() {

	data class Node(val x: Int, val y: Int, val dx: Int, val dy: Int, val stepCounter: Int) {

		fun move(grid: MutableList<List<Int>>, maxSteps: Int, minSteps: Int): List<Node> {
			val possibleNodes = mutableListOf<Node>()

			if (stepCounter < maxSteps && (x + dx) in grid[0].indices && (y + dy) in grid.indices) {
				possibleNodes.add(Node(x + dx, y + dy, dx, dy, stepCounter + 1))
			}
			if (stepCounter < minSteps) return possibleNodes

			if (dx == 0) {
				if (x + 1 < grid[0].size) {
					possibleNodes.add(Node(x + 1, y, 1, 0, 1))
				}
				if (x - 1 >= 0) {
					possibleNodes.add(Node(x - 1, y, -1, 0, 1))
				}
			}
			if (dy == 0) {
				if (y + 1 < grid.size) {
					possibleNodes.add(Node(x, y + 1, 0, 1, 1))
				}
				if (y - 1 >= 0) {
					possibleNodes.add(Node(x, y - 1, 0, -1, 1))
				}
			}
			return possibleNodes
		}
	}

	fun part1(input: List<String>, maxSteps: Int, minSteps: Int): Int {
		val height = input.size
		val width = input[0].length

		val grid = mutableListOf<List<Int>>()
		input.forEach { grid.add(it.split("").drop(1).dropLast(1).map { s -> s.toInt() }) }

		val costSoFar = mutableMapOf<Node, Int>().withDefault { Int.MAX_VALUE }
		val q = PriorityQueue<Node>(compareBy{costSoFar[it]})

		// we need to start in both directions for the heavy crucible, since it won't turn before reaching 4 steps
		val start = Node(0, 0, 1, 0, 0)
		val start2 = Node(0, 0, 0, 1, 0)
		costSoFar[start] = 0
		costSoFar[start2] = 0
		q.add(start)
		q.add(start2)

		// because of the maxMovement condition to make Dijkstra work, we need to keep minimal costSoFar for each cell
		// with the specific direction the crucible is moving. So the goal will be reached by different paths
		// , and we pick the min at the end
		val goals = mutableListOf<Int>()

		while (q.isNotEmpty()) {
			val current = q.poll()

			if (current.x == width - 1 && current.y == height - 1) {
				if (current.stepCounter >= minSteps) goals.add(costSoFar.getValue(current))
			}

			val neighbours = current.move(grid, maxSteps, minSteps)
			for (next in neighbours) {
				val newCost = costSoFar.getValue(current) + grid[next.y][next.x]
				if (newCost < costSoFar.getValue(next)) {
					costSoFar[next] = newCost
					q.add(next)
				}
			}
		}
		return min(goals)
	}


	fun part2(input: List<String>, maxSteps: Int, minSteps: Int): Int {
		return part1(input, maxSteps, minSteps)
	}

	val input = readInput("Day17")
	part1(input, 3, 0).println()
	part2(input, 10, 4).println()
}