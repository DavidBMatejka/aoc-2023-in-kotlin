import kotlin.math.abs

fun main() {
	val alreadyVisited = emptyMap<Int, MutableMap<Long, Long>>().toMutableMap()

	fun findLocation(seed: Long, map: List<List<String>>): Long {
		var next = seed

		for ((index, item) in map.withIndex()) {
			val list = item.map { it.split(" ") }

			if (alreadyVisited[index]?.containsKey(next) == true) {
				next = alreadyVisited[index]?.get(next)!!
			} else {
				for (line in list) {
					val seedStart = line[1].toLong()
					val destinationStart = line[0].toLong()
					val range = seedStart..<seedStart + line[2].toLong()

					if (range.contains(next)) {
						val difference = abs(next - seedStart)
						val new = destinationStart + difference
						alreadyVisited[index]?.set(next, new)
						next = new
						break
					}
				}
			}
		}

		return next
	}


	fun part1(input: List<String>): Long {
		val seeds = input[0].substringAfter(" ").split(" ").map { it.toLong() }

		val seedToSoil = input.dropWhile { it != "seed-to-soil map:" }.drop(1)
			.dropLastWhile { it != ("soil-to-fertilizer map:") }.dropLast(2)
		val soilToFertilizer = input.dropWhile { it != "soil-to-fertilizer map:" }.drop(1)
			.dropLastWhile { it != ("fertilizer-to-water map:") }.dropLast(2)
		val fertilizerToWater = input.dropWhile { it != "fertilizer-to-water map:" }.drop(1)
			.dropLastWhile { it != ("water-to-light map:") }.dropLast(2)
		val waterTolight = input.dropWhile { it != "water-to-light map:" }.drop(1)
			.dropLastWhile { it != ("light-to-temperature map:") }.dropLast(2)
		val lightToTemperature = input.dropWhile { it != "light-to-temperature map:" }.drop(1)
			.dropLastWhile { it != ("temperature-to-humidity map:") }.dropLast(2)
		val temperatureToHumidity = input.dropWhile { it != "temperature-to-humidity map:" }.drop(1)
			.dropLastWhile { it != ("humidity-to-location map:") }.dropLast(2)
		val humidityToLocation = input.dropWhile { it != "humidity-to-location map:" }.drop(1)

		val map = listOf(
			seedToSoil,
			soilToFertilizer,
			fertilizerToWater,
			waterTolight,
			lightToTemperature,
			temperatureToHumidity,
			humidityToLocation
		)

		var min = Long.MAX_VALUE
		for (seed in seeds) {
			val location = findLocation(seed, map)
			if (location < min) min = location
		}
		return min
	}

	data class SeedRange(val seedRange: LongRange)
	data class RangePair(val src: LongRange, val dst: LongRange)

	fun part2(input: List<String>): Long {
		val seeds = mutableListOf<SeedRange>()
		val tmp = input[0].substringAfter(" ").split(" ")
		for (i in tmp.indices step 2) {
			val num = tmp[i].toLong()
			val range = tmp[i + 1].toLong()
			seeds.add(SeedRange(num..<num + range))
		}

		val locToSeed = input.drop(2).fold(mutableListOf(mutableListOf<RangePair>())) { acc, string ->
			if (string.isBlank()) {
				acc.add(mutableListOf())
			} else {
				if (!string.contains(":")) {
					val (src, dst, range) = string.split(" ").map { it.toLong() }
					acc.last().add(RangePair(src..<src + range, dst..<dst + range))
				}
			}
			acc
		}
		locToSeed.reverse()

		var lowest = 0L
		while (true) {
			var next = lowest
			for (current in locToSeed) {
				for (rangePair in current) {
					if (next in rangePair.src) {
						next = next - rangePair.src.first + rangePair.dst.first
						break
					}
				}
			}

			if (seeds.any { next in it.seedRange }) {
				return lowest
			}
			lowest++
		}
	}

	val input = readInput("Day05")
    part1(input).println()
	part2(input).println()
}
