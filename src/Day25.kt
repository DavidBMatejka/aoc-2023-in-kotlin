fun main() {

	fun toGraph(input: List<String>): MutableMap<String, MutableList<String>> {
		val graph = mutableMapOf<String, MutableList<String>>()
		input.forEach { line ->
			val (s, dests) = line.split(":")
			dests.trim().split(" ").forEach { neighbour ->
				if (s !in graph.keys) graph[s] = mutableListOf()
				if (neighbour !in graph.keys) graph[neighbour] = mutableListOf()
				graph[s]?.add(neighbour)
				graph[neighbour]?.add(s)
			}
		}
		return graph
	}

	fun getNewNode(a: String, b: String, graph: MutableMap<String, MutableList<String>>): Pair<String, MutableList<String>> {
		val newName = "$a-$b"
		val combinedNeighbours = mutableListOf<String>()
		graph.getValue(a).filter { it != b }.forEach { combinedNeighbours.add(it) }
		graph.getValue(b).filter { it != a }.forEach { combinedNeighbours.add(it) }
		graph.getValue(a).forEach {neighbour ->
			graph[neighbour]?.replaceAll { if (it == a) newName else it }
		}
		graph.getValue(b).forEach {neighbour ->
			graph[neighbour]?.replaceAll { if (it == b) newName else it }
		}
		return Pair(newName, combinedNeighbours)
	}

	// thanks to Todd Ginsberg for the great explanation
	// source: https://todd.ginsberg.com/post/advent-of-code/2023/day25/
	fun part1(input: List<String>): Int {
		while (true) {
			val graph = toGraph(input)
			val counts = graph.keys.associateWith { 1 }.toMutableMap()

			while (graph.size > 2) {
				val a = graph.keys.random()
				val b = graph.getValue(a).random()

				val (newNode, combinedNeighbours) = getNewNode(a, b, graph)
				counts[newNode] = (counts.remove(a) ?: 0) + (counts.remove(b) ?: 0)
				graph.remove(a)
				graph.remove(b)
				graph[newNode] = combinedNeighbours
			}
			val (A, B) = graph.keys.toList()
			if (graph.getValue(A).size == 3) {
				return counts.getValue(A) * counts.getValue(B)
			}
		}
	}

//	val input = readInput("Day25_test")
	val input = readInput("Day25")
	part1(input).println()
}