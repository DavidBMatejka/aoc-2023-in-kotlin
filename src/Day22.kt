import kotlin.math.abs
import kotlin.math.max

fun main() {
	data class Box(val p: List<Int>, val q: List<Int>) {
		val dx = abs(p[0] - q[0])
		val dy = abs(p[1] - q[1])
		val dz = abs(p[2] - q[2])
		val scalar = max(max(dx, dy), dz)

		val vec = if (scalar != 0) {
			Triple(dx/scalar, dy/scalar, dz/scalar)
		} else {
			Triple(0, 0, 0)
		}

		val lowerPoint = if (p[2] <= q[2]) {
			p
		} else {
			q
		}

		override fun toString(): String {
			return "$p $q $vec $scalar"
		}
	}

	class Grid3d(val dimX: Int, val dimY: Int, val dimZ: Int) {
		val grid = Array(dimX) { Array(dimY) { Array(dimZ) {0} } }

		fun add(box: Box) {
			val x = box.p[0]
			val y = box.p[1]
			val z = box.p[2]

			grid[x][y][z] = 1
			val (dx, dy, dz) = box.vec
			for (i in 0..box.scalar) {
				grid[x + dx * i][y + dy * i][z + dz * i] = 1
			}
		}

		fun settle(boxes: MutableList<Box>) {
			boxes.forEach {
				it.println()
			}

		}
		override fun toString(): String {
			var erg = ""
			for (k in dimZ - 1 downTo 0) {
				erg += "z_$k: \n"
				for (i in 0..<dimX) {
					for (j in 0..<dimY) {
						erg += grid[i][j][k]
					}
					erg += "\n"
				}
			}
			return erg
		}
	}

	fun part1(input: List<String>): Int {
		val maxDim = mutableMapOf(
			0 to 0,
			1 to 0,
			2 to 0
		)

		val boxes = mutableListOf<Box>()
		input.forEach { line ->
			val (first, second) = line.split("~")
			val p = first.split(",").map { it.toInt() }
			val q = second.split(",").map { it.toInt() }
			for (i in 0..2) {
				if (p[i] > (maxDim[i] ?: Int.MAX_VALUE)) {
					maxDim[i] = p[i]
				}
				if (q[i] > (maxDim[i] ?: Int.MAX_VALUE)) {
					maxDim[i] = q[i]
				}
			}
			boxes.add(Box(p, q))
		}
		boxes.sortBy { it.lowerPoint[2] }

		val maxX = maxDim[0] ?: 0
		val maxY = maxDim[1] ?: 0
		val maxZ = maxDim[2] ?: 0
		val grid = Grid3d(maxX + 1, maxY + 1, maxZ + 1)

		boxes.forEach {
			grid.add(it)
		}
		println(grid)
		grid.settle(boxes)
		return -1
	}

	fun part2(input: List<String>): Int {

		return -1
	}

	val input = readInput("Day22")
	part1(input).println()
//    part2(input).println()
}
