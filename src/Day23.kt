import kotlin.collections.set
import kotlin.math.max

fun main() {

    data class Vertex(val x: Int, val y: Int) : Comparable<Vertex> {
        var distance = Int.MAX_VALUE
        override fun compareTo(other: Vertex): Int {
            return (other.distance - this.distance)
        }

    }
    data class Edge(val source: Vertex, var destination: Vertex, var distance: Int)

    data class Node(val x: Int, val y: Int) {
        var visited = mutableListOf<Node>()
        var prev: Node? = null
        var comingFrom: Vertex? = null
        var steps: Int = 0
        fun possibleNeighbours(grid: Array<Array<Char>>, slopey: Boolean): List<Node> {
            val neighbours = mutableListOf<Node>()

            if (x - 1 >= 0 && (grid[y][x - 1] == '.' || grid[y][x - 1] == '<' || (!slopey && grid[y][x - 1] == '>'))) {
                val tmp = Node(x - 1, y)
                if (tmp != this.prev) {
                    tmp.prev = this
                    neighbours.add(tmp)
                }
            }
            if (x + 1 < grid[0].size && (grid[y][x + 1] == '.' || grid[y][x + 1] == '>' || (!slopey && grid[y][x + 1] == '<'))) {
                val tmp = Node(x + 1, y)
                if (tmp != this.prev) {
                    tmp.prev = this
                    neighbours.add(tmp)
                }
            }
            if (y - 1 >= 0 && (grid[y - 1][x] == '.' || grid[y - 1][x] == '^' || (!slopey && grid[y - 1][x] == 'v'))) {
                val tmp = Node(x, y - 1)
                if (tmp != this.prev) {
                    tmp.prev = this
                    neighbours.add(tmp)
                }
            }
            if (y + 1 < grid.size && (grid[y + 1][x] == '.' || grid[y + 1][x] == 'v' || (!slopey && grid[y + 1][x] == '^'))) {
                val tmp = Node(x, y + 1)
                if (tmp != this.prev) {
                    tmp.prev = this
                    neighbours.add(tmp)
                }
            }

            return neighbours
        }
    }

    fun toGraph(input: List<String>, slopey: Boolean): Pair<MutableList<Vertex>, MutableMap<Vertex, MutableSet<Edge>>> {
        val height = input.size
        val width = input[0].length

        val grid = Array(height) {Array(width) {'.'} }
        input.forEachIndexed { i, it ->
            for (j in 0..<width) {
                grid[i][j] = it[j]
            }
        }

        val vertices = mutableListOf<Vertex>()
        val edges = mutableMapOf<Vertex,  MutableSet<Edge>>()

        val startVertex = Vertex(1, 0)
        vertices.add(startVertex)

        val startPos = Node(1, 0)
        startPos.comingFrom = startVertex

        val q = ArrayDeque<Node>()
        q.add(startPos)

        while (q.isNotEmpty()) {
            val current = q.removeFirst()
            current.visited.add(current)

            val neighbours = current.possibleNeighbours(grid, slopey)

            if (current.x == width - 2 && current.y == height - 1) {
                val newVertex = Vertex(current.x, current.y)
                if (newVertex !in vertices) vertices.add(newVertex)

                if (edges.containsKey(current.comingFrom)) {
                    edges[current.comingFrom]!!.add(Edge(current.comingFrom!!, newVertex, current.steps))
                } else {
                    edges[current.comingFrom!!] = mutableSetOf(Edge(current.comingFrom!!, newVertex, current.steps))
                }
            }

            if (neighbours.size > 1) {
                val newVertex = Vertex(current.x, current.y)
                if (!vertices.contains(newVertex)) {
                    vertices.add(newVertex)
                }

                val newEdge = Edge(current.comingFrom!!, newVertex, current.steps)
                if (edges.containsKey(current.comingFrom)) {
                    edges[current.comingFrom]!!.add(newEdge)
                } else {
                    edges[current.comingFrom!!] = mutableSetOf(newEdge)
                }

                neighbours.forEach {
                    it.comingFrom = newVertex
                    it.visited = mutableListOf(current)
                    it.steps = 1
                    q.add(it)
                }
            } else {
                neighbours.forEach {
                    if (it !in current.visited) {
                        it.comingFrom = current.comingFrom
                        it.steps = current.steps + 1
                        q.add(it)
                    }
                }
            }
        }
        return Pair(vertices, edges)
    }

    fun simplifyGraph(edges: MutableMap<Vertex, MutableSet<Edge>>): Boolean {
        val mutableIterator = edges.iterator()
        var somethingChanged = false

        while (mutableIterator.hasNext()) {
            val next = mutableIterator.next()

            for (edge in edges) {
                val iter = edge.value.iterator()
                for (entry1 in iter) {
                    for (entry2 in iter) {
                        if (entry1.destination == entry2.destination && (entry2.distance < entry1.distance || entry1.distance < entry2.distance)) {
                            entry1.distance = max(entry1.distance, entry2.distance)
                            iter.remove()
                            somethingChanged = true
                        }
                    }
                }
            }

            for (edge in next.value) {
                if ((edges[edge.destination]?.size ?: 0) == 1) {
                    edge.distance += edges[edge.destination]?.elementAt(0)!!.distance
                    edge.destination = edges[edge.destination]?.elementAt(0)!!.destination
                    somethingChanged = true
                }
            }
        }

        return somethingChanged
    }


    fun dfs(
        current: Vertex,
        edges: MutableMap<Vertex, MutableSet<Edge>>,
        width: Int,
        height: Int,
        visited: MutableSet<Vertex>
    ): Int {
        if (current.x == width - 2 && current.y == height - 1) return 0

        var maxPath = Int.MIN_VALUE

        visited.add(current)
        val neighbours = edges[current]!!.iterator()
        for (neighbour in neighbours) {
            if (neighbour.destination !in visited) {
                maxPath = max(maxPath, dfs(neighbour.destination, edges, width, height, visited) + neighbour.distance)
            }
        }
        visited.remove(current)

        return maxPath
    }


    fun part1(input: List<String>): Int {

        val startVertex = Vertex(1, 0)
        val (_, edges) = toGraph(input, true)

        while (true) {
            if (!simplifyGraph(edges)) break
        }

        val distance = edges[startVertex]?.elementAt(0)?.distance ?: -1
        return distance
    }

    fun part2(input: List<String>): Int {
        val height = input.size
        val width = input[0].length

        val grid = Array(height) {Array(width) {'.'} }
        input.forEachIndexed { i, it ->
            for (j in 0..<width) {
                grid[i][j] = it[j]
            }
        }

        val vertices = mutableListOf<Vertex>()
        val edges = mutableMapOf<Vertex,  MutableSet<Edge>>()

        val startVertex = Vertex(1, 0)

        val startPos = Node(1, 0)
        startPos.comingFrom = startVertex

        val q = ArrayDeque<Node>()
        q.add(startPos)

        while (q.isNotEmpty()) {
            val current = q.removeFirst()
            current.visited

            current.visited.add(current)

            val neighbours = current.possibleNeighbours(grid, false)

            if (current.x == width - 2 && current.y == height - 1) {
                val newVertex = Vertex(current.x, current.y)
                if (newVertex !in vertices) vertices.add(newVertex)

                if (edges.containsKey(current.comingFrom)) {
                    edges[current.comingFrom]!!.add(Edge(current.comingFrom!!, newVertex, current.steps))
                } else {
                    edges[current.comingFrom!!] = mutableSetOf(Edge(current.comingFrom!!, newVertex, current.steps))
                }
            }

            if (neighbours.size > 1) {
                val newVertex = Vertex(current.x, current.y)
                if (newVertex !in vertices) vertices.add(newVertex)

                val newEdge = Edge(current.comingFrom!!, newVertex, current.steps)
                val coming = current.comingFrom!!
                val size1 = edges[coming]?.size
                val size2 = edges[newVertex]?.size

                if (edges.containsKey(current.comingFrom)) {
                    edges[current.comingFrom]!!.add(newEdge)
                } else {
                    edges[current.comingFrom!!] = mutableSetOf(newEdge)
                }

                if (edges.containsKey(newVertex)) {
                    edges[newVertex]!!.add(Edge(newVertex, coming, current.steps))
                } else {
                    edges[newVertex] = mutableSetOf(Edge(newVertex, coming, current.steps))
                }

                if (size1 == edges[coming]!!.size && size2 == edges[newVertex]!!.size) continue

                neighbours.forEach {
                    it.comingFrom = newVertex
                    it.visited = mutableListOf(current)
                    it.steps = 1
                    q.add(it)
                }
            } else {
                neighbours.forEach {
                    if (it !in current.visited) {
                        it.comingFrom = current.comingFrom
                        it.steps = current.steps + 1
                        q.add(it)
                    }
                }
            }
        }

        val visited = mutableSetOf<Vertex>()

        val sum = dfs(startVertex, edges, width, height, visited)

        return sum
    }

    val input = readInput("Day23")
    part1(input).println()
    part2(input).println()
}