fun main() {
	data class Point3(var x: Int, var y: Int, var z: Int)
	data class Brick(val voxels: List<Point3>) {
		val supportedBy = mutableSetOf<Brick>()
		val supporting = mutableSetOf<Brick>()
		var minZ = Integer.MAX_VALUE
		var dimX = 0
		var dimY = 0
		var dimZ = 0

		init {
			for (voxel in voxels) {
				if (voxel.z < minZ) minZ = voxel.z
				if (dimX < voxel.x) dimX = voxel.x
				if (dimY < voxel.y) dimY = voxel.y
				if (dimZ < voxel.z) dimZ = voxel.z
			}
		}
	}

	fun toBrick(l: String, r: String): Brick {
		val points = mutableListOf<Point3>()
		val (lx, ly, lz) = l.split(",").map { it.toInt() }
		val (rx, ry, rz) = r.split(",").map { it.toInt() }
		if (lx == rx && ly == ry && lz == rz) {
			points.add(Point3(lx, ly, lz))
			return Brick(points)
		}
		if (lx != rx) {
			var lower = lx
			var upper = rx
			if (lx > rx) {
				lower = rx; upper = lx
			}
			for (i in lower..upper) {
				points.add(Point3(i, ly, lz))
			}
		} else if (ly != ry) {
			var lower = ly
			var upper = ry
			if (ly > ry) {
				lower = ry; upper = ly
			}
			for (i in lower..upper) {
				points.add(Point3(lx, i, lz))
			}
		} else {
			var lower = lz
			var upper = rz
			if (lz > rz) {
				lower = rz; upper = lz
			}
			for (i in lower..upper) {
				points.add(Point3(lx, ly, i))
			}
		}
		return Brick(points)
	}

	fun addAndSettle(grid: Array<Array<Array<Int>>>, brick: Brick) {
		for (i in 0..grid[0][0].size) {
			var canFall = true
			brick.voxels.forEach {
				if (it.z == 1 || grid[it.x][it.y][it.z - 1] == 1) {
					canFall = false
				}
			}
			if (canFall) {
				brick.voxels.forEach { it.z -= 1 }
			} else {
				brick.voxels.forEach {
					grid[it.x][it.y][it.z] = 1
				}
			}
		}
	}

	fun createListOfSortedAndRemovableBricks(input: List<String>): Pair<List<Brick>, List<Brick>> {
		val bricks = mutableListOf<Brick>()
		input.forEach { line ->
			val (l, r) = line.split("~")
			bricks.add(toBrick(l, r))
		}

		val sortedBricks = bricks.sortedBy { it.minZ }

		var dimX = 0
		var dimY = 0
		var dimZ = 0
		sortedBricks.forEach {
			if (it.dimX > dimX) dimX = it.dimX
			if (it.dimY > dimY) dimY = it.dimY
			if (it.dimZ > dimZ) dimZ = it.dimZ
		}
		val grid = Array(dimX + 1) { Array(dimY + 1) { Array(dimZ + 1) { 0 } } }

		sortedBricks.forEach {
			addAndSettle(grid, it)
		}

		for (brick in sortedBricks) {
			for (other in sortedBricks) {
				if (brick == other) continue

				brick.voxels.forEach {
					if (Point3(it.x, it.y, it.z + 1) in other.voxels) {
						brick.supporting.add(other)
						other.supportedBy.add(brick)
					}
				}
			}
		}

		val removeableBricks = mutableListOf<Brick>()
		sortedBricks.forEach { brick ->
			var counts = true
			brick.supporting.forEach {
				if (it.supportedBy.size == 1) counts = false
			}
			if (counts) removeableBricks.add(brick)
		}

		return Pair(sortedBricks, removeableBricks)
	}

	fun part1(input: List<String>): Int {
		val (_, removeableBricks) = createListOfSortedAndRemovableBricks(input)
		return removeableBricks.size
	}

	fun part2(input: List<String>): Int {
		val (sortedBricks, removeableBricks) = createListOfSortedAndRemovableBricks(input)

		val chainreactingBricks = mutableListOf<Brick>()
		sortedBricks.forEach {brick ->
			if (brick !in removeableBricks) {
				chainreactingBricks.add(brick)
			}
		}

		var sum = 0
		chainreactingBricks.forEach { chainreactingBrick ->
			val falling = mutableSetOf(chainreactingBrick)
			val q = mutableListOf(chainreactingBrick)
			while (q.isNotEmpty()) {
				val current = q.removeFirst()
				current.supporting.forEach { supportedByCurrent ->
					if (supportedByCurrent !in falling) {
						if (supportedByCurrent.supportedBy.all { it in falling }) {
							q.add(supportedByCurrent)
							falling.add(supportedByCurrent)
						}
					}
				}
			}
			sum += falling.size - 1
		}

		println(sum)
		return -1
	}

	val input = readInput("Day22")
	part1(input).println()
    part2(input).println()
}