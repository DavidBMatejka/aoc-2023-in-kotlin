enum class Types {
	BROADCASTER,
	FLIPFLOP,
	AND,
	OUTPUT
}

fun main() {
	data class Module(val name: String, var type: Types = Types.BROADCASTER) {
		var lastReceived = 0
		val children = mutableListOf<Module>()
		var lastPulses = mutableMapOf<String, Int>()
		var on = false

		fun receivePulse(pulse: Int, from: Module): Int {
			if (this.type == Types.AND) {
				lastPulses[from.name] = pulse
				return if (lastPulses.values.all { it == 1 }) 0
				else 1
			} else if (this.type == Types.FLIPFLOP) {
				if (pulse == 1) {
					return -10
				} else if (pulse == 0 && this.on) {
					this.on = false
					return 0
				} else if (pulse == 0 && !this.on) {
					this.on = true
					return 1
				}
			}

			return -1
		}
	}

	fun toGraph(input: List<String>): MutableMap<String, Module> {
		val graph = mutableMapOf<String, Module>()
		input.forEach { line ->
			val (source, _) = line.split("->")
			if (source.first() == '%' || source.first() == '&') {
				val type = if (source.first() == '%') {
					Types.FLIPFLOP
				} else Types.AND
				val name = source.substring(1, source.length).trim()
				graph[name] = (Module(name, type))
			} else {
				val name = source.trim()
				graph[name] = (Module(name))
			}
		}

		input.forEach { line ->
			val (source, dest) = line.split("->")
			if (source.first() == '%' || source.first() == '&') {
				val name = source.substring(1, source.length).trim()
				dest.split(",").forEach {
					val next = graph[it.trim()]
					if (next == null) {
						graph[name]?.children?.add(Module("output", Types.OUTPUT))
					}
					if (next != null) {
						if (next.type == Types.AND) {
							next.lastPulses[name] = 0
						}
						next.let { it1 -> graph[name]?.children?.add(it1) }
					}
				}
			} else {
				val name = source.trim()
				dest.split(",").forEach {
					graph[it.trim()]?.let { it1 -> graph[name]?.children?.add(it1) }
				}
			}
		}
		return graph
	}

	fun part1(input: List<String>): Long {
		val graph = toGraph(input)
		graph.forEach { (_, v) ->
			println("${v.name} -> ${v.children}")
		}
		println()

		val start = graph["broadcaster"] ?: return -10
		val q = mutableListOf<Pair<Module, Int>>()

		val states = mutableListOf<Map<String, String>>()
		var state = mutableMapOf<String, String>()
		graph.forEach { (_, v) ->
			if (v.type == Types.AND) state[v.name] = v.lastPulses.toString()
			else if (v.type == Types.FLIPFLOP) state[v.name] = v.on.toString()
		}

		var buttonPushed = 0
		var highCount = 0
		var lowCount = 0
		val cycle = mutableMapOf<Int, Pair<Int, Int>>()
        while (!states.contains(state)) {
			if (buttonPushed == 1000) break
			buttonPushed++
			lowCount++ // button pulse

			states.add(state)
//			println()
			println(buttonPushed)
			q.add(Pair(start, 0))
//			println(states)
			while (q.isNotEmpty()) {
				val (current, pulse) = q.removeFirst()
				current.children.forEach {
					val nextPulse = it.receivePulse(pulse, current)
//					println("${current.name} --$pulse-> ${it.name}")
					if (pulse == 1) highCount++
					else if (pulse == 0) lowCount++
					if (nextPulse != -10) {
						q.add(Pair(it, nextPulse))
					}
				}
			}
			state = mutableMapOf()
			graph.forEach { (_, v) ->
				if (v.type == Types.AND) state[v.name] = v.lastPulses.toString()
				else if (v.type == Types.FLIPFLOP) state[v.name] = v.on.toString()
			}
			cycle[buttonPushed] = Pair(lowCount, highCount)
		}
//		println(states)

		var sum = highCount.toLong() * lowCount.toLong()

		return sum
	}

	fun part2(input: List<String>): Int {

		return -1
	}

	val input = readInput("Day20")
	part1(input).println()
	// part2(input).println()
}