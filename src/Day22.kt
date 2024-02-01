
import kotlin.math.abs
import kotlin.math.max

fun main() {
	data class Point3(var x: Int, var y: Int, var z: Int)
	data class Brick(val points: List<Point3>)

	class Grid3d(val dimX: Int, val dimY: Int, val dimZ: Int) {
		val grid = Array(dimX) { Array(dimY) { Array(dimZ) {0} } }

		fun addAndSettle(brick: Brick): Brick {
			val pointsToAdd = brick.points
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
			return Brick(pointsToAdd)
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

		val dx = p[0] - q[0]
		val dy = p[1] - q[1]
		val dz = p[2] - q[2]
		val scalar = max(max(abs(dx), abs(dy)), abs(dz))

		val vec = if (scalar != 0) {
			Point3(dx/scalar, dy/scalar, dz/scalar)
		} else {
			Point3(0, 0, 0)
		}
		for (i in 0..scalar) {
			points.add(Point3(
				q[0] + vec.x * i,
				q[1] + vec.y * i,
				q[2] + vec.z * i))
		}
		return points
	}

	fun part1(input: List<String>): Int {
		val maxDim = mutableMapOf(
			0 to 0,
			1 to 0,
			2 to 0
		)

		val bricks = mutableListOf<Brick>()
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
			bricks.add(Brick(endpointsToPointslist(p, q)))
		}

		val maxX = maxDim[0] ?: 0
		val maxY = maxDim[1] ?: 0
		val maxZ = maxDim[2] ?: 0
		val grid = Grid3d(maxX + 1, maxY + 1, maxZ + 1)

		val settledBricks = mutableListOf<Brick>()
		bricks.forEach {
			settledBricks.add(grid.addAndSettle(it))
		}
		println(grid)
		settledBricks.forEach { it.println() }


		return -1
	}

	fun part2(input: List<String>): Int {

		return -1
	}

	val input = readInput("Day22_test")
	part1(input).println()
//    part2(input).println()
}
