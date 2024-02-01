
import kotlin.math.abs
import kotlin.math.max

fun main() {
	data class Point3(var x: Int, var y: Int, var z: Int)
	data class Box(val points: List<Point3>)

	class Grid3d(val dimX: Int, val dimY: Int, val dimZ: Int) {
		val grid = Array(dimX) { Array(dimY) { Array(dimZ) {0} } }

		fun addAndSettle(box: Box) {
			val pointsToAdd = box.points
			for (i in 0..<dimZ) {
				var canFall = true
				pointsToAdd.forEach {
					if (it.z == 1 || grid[it.x][it.y][it.z - 1] == 1) {
						canFall = false
					}
				}
				if (canFall) {
					pointsToAdd.map { it.z -= 1 }
				} else {
					pointsToAdd.forEach {
						grid[it.x][it.y][it.z] = 1
					}
				}
			}

		}
		override fun toString(): String {
			var erg = ""
			for (k in dimZ - 1 downTo 0) {
				erg += "z_$k: \n"
				for (i in 0..<dimX) {
					for (j in 0..<dimY) {
						erg += "${grid[i][j][k]} "
					}
					erg += "\n"
				}
			}
			return erg
		}
	}

	fun endpointsToPointslist(p: List<Int>, q:List<Int>): List<Point3> {
		val points = mutableListOf<Point3>()

		var higherPoint = emptyList<Int>()
		val lowerPoint = if (p[2] <= q[2]) {
			higherPoint = q
			p
		} else {
			higherPoint = p
			q
		}

		val dx = higherPoint[0] - lowerPoint[0]
		val dy = higherPoint[1] - lowerPoint[1]
		val dz = higherPoint[2] - lowerPoint[2]
		val scalar = max(max(abs(dx), abs(dy)), abs(dz))

		val vec = if (scalar != 0) {
			Point3(dx/scalar, dy/scalar, dz/scalar)
		} else {
			Point3(0, 0, 0)
		}
		for (i in 0..scalar) {
			points.add(Point3(
				lowerPoint[0] + vec.x * i,
				lowerPoint[1] + vec.y * i,
				lowerPoint[2] + vec.z * i))
		}

		return points
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
			boxes.add(Box(endpointsToPointslist(p, q)))
		}

		val maxX = maxDim[0] ?: 0
		val maxY = maxDim[1] ?: 0
		val maxZ = maxDim[2] ?: 0
		val grid = Grid3d(maxX + 1, maxY + 1, maxZ + 1)

		boxes.forEach { grid.addAndSettle(it) }
		println(grid)
		return -1
	}

	fun part2(input: List<String>): Int {

		return -1
	}

	val input = readInput("Day22_test")
	part1(input).println()
//    part2(input).println()
}
