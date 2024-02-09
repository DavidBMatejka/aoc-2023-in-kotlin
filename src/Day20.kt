enum class Types {
	BROADCASTER,
	FLIPFLOP,
	AND,
	OUTPUT
}
enum class Pulses {
	HIGH,
	LOW,
	IGNORE
}

fun main() {
	data class Module(val name: String, var type: Types = Types.BROADCASTER) {
		val children = mutableListOf<Module>()
		var lastPulses = mutableMapOf<String, Pulses>()
		var on = false

		fun receivePulse(pulse: Pulses, from: Module): Pulses {
			if (this.type == Types.AND) {
				lastPulses[from.name] = pulse
				return if (lastPulses.values.all { it == Pulses.HIGH }) Pulses.LOW
				else Pulses.HIGH
			} else if (this.type == Types.FLIPFLOP) {
				if (pulse == Pulses.HIGH) {
					return Pulses.IGNORE
				} else if (pulse == Pulses.LOW && this.on) {
					this.on = false
					return Pulses.LOW
				} else if (pulse == Pulses.LOW && !this.on) {
					this.on = true
					return Pulses.HIGH
				}
			}

			return Pulses.IGNORE
		}
	}

	fun toGraph(input: List<String>): MutableMap<String, Module> {
		val graph = mutableMapOf<String, Module>()
		// adding vertices
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

		// adding edges
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
							next.lastPulses[name] = Pulses.LOW
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

		val start = graph["broadcaster"] ?: return -10
		val q = mutableListOf<Pair<Module, Pulses>>()

		var buttonPushed = 0
		var highCount = 0
		var lowCount = 0
        while (buttonPushed < 1000) {
			buttonPushed++
			lowCount++ // button pulse

			q.add(Pair(start, Pulses.LOW))
			while (q.isNotEmpty()) {
				val (current, pulse) = q.removeFirst()
				current.children.forEach {
					val nextPulse = it.receivePulse(pulse, current)
					if (current.name == "vd" && pulse == Pulses.LOW) {
						return buttonPushed.toLong()
					}
					if (pulse == Pulses.HIGH) highCount++
					else if (pulse == Pulses.LOW) lowCount++
					if (nextPulse != Pulses.IGNORE) {
						q.add(Pair(it, nextPulse))
					}
				}
			}
		}

		return highCount.toLong() * lowCount.toLong()
	}

	fun part2(input: List<String>): Long {
		val graph = toGraph(input)

		val start = graph["broadcaster"] ?: return -10
		val q = mutableListOf<Pair<Module, Pulses>>()

		var buttonPushed = 0
		var highCount = 0
		var lowCount = 0

		val vdSize = graph["vd"]?.lastPulses?.size
		val vdIns = mutableMapOf<String, Int>()

		while (true) {
			buttonPushed++
			lowCount++ // button pulse

			q.add(Pair(start, Pulses.LOW))
			while (q.isNotEmpty()) {
				val (current, pulse) = q.removeFirst()
				current.children.forEach {
					val nextPulse = it.receivePulse(pulse, current)
					if (current.name == "vd") {
						current.lastPulses.forEach { (k, vdIn) ->
							if (vdIn == Pulses.HIGH && k !in vdIns) {
								vdIns[k] = buttonPushed
							}
							if (vdSize == vdIns.size) {
								return lcm(vdIns.values.map { c -> c.toLong() }.toList())
							}
						}
					}
					if (pulse == Pulses.HIGH) highCount++
					else if (pulse == Pulses.LOW) lowCount++
					if (nextPulse != Pulses.IGNORE) {
						q.add(Pair(it, nextPulse))
					}
				}
			}
		}
	}

	val input = readInput("Day20")
	part1(input).println()
	 part2(input).println()
}