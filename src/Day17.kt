import java.util.*

fun main() {
	data class Node(
		val x: Int,
		val y: Int,
		val dx: Int,
		val dy: Int,
		var heatLoss: Int,
		val stepCounter: Int
	) {
		override fun equals(other: Any?): Boolean {
			return (other is Node && this.x == other.x && this.y == other.y && this.dx == other.dx && this.y == other.dy && this.heatLoss == other.heatLoss)
		}

		fun move(grid: MutableList<List<Int>>): List<Node> {
			val possibleNodes = mutableListOf<Node>()
			if (stepCounter < 3 && x + dx >= 0 && x + dx < grid[0].size && y + dy >= 0 && y + dy < grid.size) {
				possibleNodes.add(Node(x + dx, y + dy, dx, dy, heatLoss + grid[y + dy][x + dx], stepCounter + 1))
			}
			if (dx == 0) {
				if (x + 1 < grid[0].size) {
					possibleNodes.add(Node(x + 1, y, 1, 0, heatLoss + grid[y][x + 1], 1))
				}
				if (x - 1 >= 0) {
					possibleNodes.add(Node(x - 1, y, -1, 0, heatLoss + grid[y][x - 1], 1))
				}
			}
			if (dy == 0) {
				if (y + 1 < grid.size) {
					possibleNodes.add(Node(x, y + 1, 0, 1, heatLoss + grid[y + 1][x], 1))
				}
				if (y - 1 >= 0) {
					possibleNodes.add(Node(x, y - 1, 0, -1, heatLoss + grid[y - 1][x], 1))
				}
			}
			return possibleNodes
		}

		override fun hashCode(): Int {
			var result = x
			result = 31 * result + y
			result = 31 * result + dx
			result = 31 * result + dy
			result = 31 * result + heatLoss
			return result
		}
		/*
		*
				fun moveUltra(grid: MutableList<List<Int>>): Collection<Node> {
					val possibleNodes = mutableListOf<Node>()
					if (stepCounter < 4 && x + dx >= 0 && x + dx < grid[0].size && y + dy >= 0 && y + dy < grid.size) {
						possibleNodes.add(Node(x + dx, y + dy, dx, dy, heatLoss + grid[y + dy][x + dx], stepCounter + 1, this))
					}
					if (stepCounter >= 4) {
						if (stepCounter < 10 && x + dx >= 0 && x + dx < grid[0].size && y + dy >= 0 && y + dy < grid.size) possibleNodes.add(
							Node(x + dx, y + dy, dx, dy, heatLoss + grid[y + dy][x + dx], stepCounter + 1, this)
						)
						if (dx == 0) {
							if (x + 4 < grid[0].size) {
								possibleNodes.add(Node(x + 1, y, 1, 0, heatLoss + grid[y][x + 1], 1, this))
							}
							if (x - 4 >= 0) {
								possibleNodes.add(Node(x - 1, y, -1, 0, heatLoss + grid[y][x - 1], 1, this))
							}
						}
						if (dy == 0) {
							if (y + 4 < grid.size) {
								possibleNodes.add(Node(x, y + 1, 0, 1, heatLoss + grid[y + 1][x], 1, this))
							}
							if (y - 4 >= 0) {
								possibleNodes.add(Node(x, y - 1, 0, -1, heatLoss + grid[y - 1][x], 1, this))
							}
						}
					}
					return possibleNodes
				}
		* */
	}

	class NodeComparator : Comparator<Node> {
		override fun compare(o1: Node?, o2: Node?): Int {
			if (o1 != null && o2 != null) {
				if (o1.heatLoss > o2.heatLoss) return 1
				else if (o1.heatLoss < o2.heatLoss) return -1
			}
			return 0
		}
	}

	fun part1(input: List<String>): Int {
		val grid = mutableListOf<List<Int>>()
		input.forEach { grid.add(it.split("").drop(1).dropLast(1).map { s -> s.toInt() }) }
		val height = input.size
		val width = input[0].length

		grid.forEach {
			it.forEach { s ->
				print(s)
			}
			println()
		}

		val costSoFar = mutableMapOf<Node, Int>()

		val q = PriorityQueue(NodeComparator())
		val start = Node(0, 0, 1, 0, 0, 0)
		costSoFar[start] = 0
		q.add(start)

		var i = 0
		while (q.isNotEmpty() && i < 500) {
			i++
			val current = q.poll()

			if (current.x == width - 1 && current.y == height - 1) {
				return costSoFar[current]!!
			}

			val neighbours = current.move(grid)
			for (next in neighbours) {
				val newCost = (costSoFar[current] ?: 0) + next.heatLoss
				if (next !in costSoFar || newCost < (costSoFar[next] ?: Int.MAX_VALUE)) {
					costSoFar[next] = newCost
					next.heatLoss = newCost
					q.add(next)
				}
			}
		}


		return -1
	}


	fun part2(input: List<String>): Int {
		val grid = mutableListOf<List<Int>>()
		input.forEach { grid.add(it.split("").drop(1).dropLast(1).map { s -> s.toInt() }) }

		//grid.forEach(::println)

		val q = PriorityQueue(NodeComparator())
		q.add(Node(0, 0, 1, 0, 0, 0))

		val visited = mutableListOf<Node>()

		while (q.isNotEmpty()) {
			val current = q.poll()

			if (current.x == grid[0].size - 1 && current.y == grid.size - 1) {
				//pathToAscii(current, grid)

				return current.heatLoss
			}
			if (visited.contains(current)) {
				continue
			}

			visited.add(current)

			//val possibleMoves = current.moveUltra(grid)
			//q.addAll(possibleMoves)
		}

		return -1
	}

	val input = readInput("Day17_test")
	part1(input).println()
	//part2(input).println()
}