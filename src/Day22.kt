
import kotlin.math.abs
import kotlin.math.max

fun main() {
	data class Point3(var x: Int, var y: Int, var z: Int)
	data class Box(val p: List<Int>, val q: List<Int>) {
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

//		val dx = abs(p[0] - q[0])
//		val dy = abs(p[1] - q[1])
//		val dz = abs(p[2] - q[2])
//		val scalar = max(max(dx, dy), dz)
//		val vec = if (scalar != 0) {
//			Triple(dx/scalar, dy/scalar, dz/scalar)
//		} else {
//			Triple(0, 0, 0)
//		}

		fun getAllPoints():List<Point3> {
			val allPoints = mutableListOf<Point3>()
			for (i in 0..scalar) {
				allPoints.add(Point3(
					lowerPoint[0] + vec.x * i,
					lowerPoint[1] + vec.y * i,
					lowerPoint[2] + vec.z * i))
			}
			return allPoints
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

		fun addAndSettle(box: Box) {
			val pointsToAdd = box.getAllPoints().toMutableList()
			val cz = box.lowerPoint[2]
			for (i in 0..cz) {
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

//		boxes.forEach {
//			grid.add(it)
//		}
//		println(grid)

		boxes.forEach { grid.addAndSettle(it) }
//		grid.addAndsettle(boxes[0])
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
