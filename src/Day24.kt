import kotlin.math.roundToInt

fun main() {
	//	data class Position(val x: Double, val y: Double, val z: Double)
	data class Position(val x: Long, val y: Long, val z: Long)
	data class Velocity(val dx: Double, val dy: Double, val dz: Double) {
		fun linearMultiple(other: Velocity): Boolean {
//			 x * t = other.x
//		<=>  t = other.x / x
//	same for t = other.y / y

			return (other.dx / dx == other.dy / dy)
		}

		fun minus(vx: Long, vy: Long): Velocity {
			return Velocity(dx - vx, dy - vy, dz)
		}
	}

	data class Vector(val pos: Position, val vel: Velocity)

	data class Intersection(val pos: Position, val timeA: Double, val timeB: Double)

	fun intersect(v1: Vector, v2: Vector): Intersection {
		// p + s * p.v = q + t * q.v
		val (p, dirP) = v1
		val (q, dirQ) = v2

//  I		    p.x + s * dirP.dx = q.x + t * dirQ.dx
//  II          p.y + s * dirP.dy = q.y + t * dirQ.dy
//
//	II          s = (q.y + t * dirQ.dy - p.y) / dirP.dy
//	II in I     p.x + ((q.y + t * dirQ.dy - p.y) / dirP.dy) * dirP.dx = q.x + t * dirQ.dx
//		<=>     p.x + q.y * (dirP.dx / dirP.dy) + t * dirQ.dy * (dirP.dx / dirP.dy) - p.y * (dirP.dx / dirP.dy)   = q.x + t * dirQ.dx
//		<=>     p.x - q.x + q.y * (dirP.dx / dirP.dy) - p.y * (dirP.dx / dirP.dy)   = t * dirQ.dx - t * dirQ.dy * (dirP.dx / dirP.dy)
//		<=>     p.x - q.x + q.y * (dirP.dx / dirP.dy) - p.y * (dirP.dx / dirP.dy)   = t * (dirQ.dx - dirQ.dy * (dirP.dx / dirP.dy))
//		<=>     t = (p.x - q.x + q.y * (dirP.dx / dirP.dy) - p.y * (dirP.dx / dirP.dy)) / (dirQ.dx - dirQ.dy * (dirP.dx / dirP.dy))


//  II			       	p.y + s * dirP.dy = q.y + t * dirQ.dy
//	if dirP.y == 0 =>  	p.y = q.y + t * dirQ.dy
//						t = (p.y - q.y) / dirQ.dy

//  I			    p.x + s * dirP.dx = q.x + t * dirQ.dx
//  II in I		    p.x + s * dirP.dx = q.x + ((p.y - q.y) / dirQ.dy) * dirQ.dx
// 			<=> 	s  = (q.x - p.x + ((p.y - q.y) / dirQ.dy) * dirQ.dx) / (dirP.dx)

		val t: Double
		val s: Double

		 if (dirP.dy == 0.0) {
			t = (p.y - q.y) / dirQ.dy
			s = (q.x - p.x + t * dirQ.dx) / dirP.dx
		} else if (dirQ.dy == 0.0){
			s = (q.y - p.y) / dirP.dy
			t = (p.x + s * dirP.dx - q.x) / dirQ.dx
		}  else {
			t =
				(p.x - q.x + q.y * (dirP.dx / dirP.dy) - p.y * (dirP.dx / dirP.dy)) / (dirQ.dx - dirQ.dy * (dirP.dx / dirP.dy))
			s = (q.y + t * dirQ.dy - p.y) / dirP.dy
		}
		val x = q.x + t * dirQ.dx
		val y = q.y + t * dirQ.dy
		if (s.isNaN() || t.isNaN()) {
			println("$v1 $v2")
			println("$s $t")
		}
		return Intersection(Position(x.toLong(), y.toLong(), 0), s.roundToInt().toDouble(), t.roundToInt().toDouble())
	}

	fun part1(input: List<String>): Int {
		val vectors = mutableListOf<Vector>()
		for (line in input) {
			val (pos, vel) = line.split("@")
			val (x, y, z) = pos.split(",").map { it.trim() }.map { it.toLong() }
			val (dx, dy, dz) = vel.split(",").map { it.trim() }.map { it.toDouble() }
			vectors.add(Vector(Position(x, y, z), Velocity(dx, dy, dz)))
		}

		val boundary = 200000000000000..400000000000000
		var sum = 0
		for (i in vectors.indices) {
			for (j in i..<vectors.size) {
				if (vectors[i].vel.linearMultiple(vectors[j].vel)) continue
				val (pos, timeA, timeB) = intersect(vectors[i], vectors[j])
				if ((timeA >= 0) && (timeB >= 0) && (pos.x in boundary) && (pos.y in boundary)) {
					sum++
				}
			}
		}

		return sum
	}

	fun part2(input: List<String>): Long {
		val vectors = mutableListOf<Vector>()
		for (line in input) {
			val (pos, vel) = line.split("@")
			val (x, y, z) = pos.split(",").map { it.trim() }.map { it.toLong() }
			val (dx, dy, dz) = vel.split(",").map { it.trim() }.map { it.toDouble() }
			vectors.add(Vector(Position(x, y, z), Velocity(dx, dy, dz)))
		}

		var rock = mutableSetOf<Position>()
		while (rock.size != 1) {
			val randomVectors = vectors.shuffled().take(4)

			val longRange = -300L..300L
			for (vx in longRange) {
				for (vy in longRange) {
					val copies = mutableListOf<Vector>()
					randomVectors.forEach {
						copies.add(it.copy(pos = it.pos, vel = it.vel.minus(vx, vy)))
					}
					rock = mutableSetOf()
					for (copy1 in copies) {
						for (copy2 in copies) {
							if (copy1 == copy2) break
							if (copy1.vel.linearMultiple(copy2.vel)) break
							if (copy1.vel.dy == 0.0 && copy2.vel.dy == 0.0) break
							val (pos, _, _) = intersect(copy1, copy2)
							rock.add(Position(pos.x, pos.y, pos.z))
						}
					}
					if (rock.size == 1) {
						var hailstone1 = randomVectors[0]
						var hailstone2 = randomVectors[1]
						var i = 0
						while ((hailstone1.vel.dx - vx) == 0.0) {
							hailstone1 = randomVectors[i]
							i++
						}
						i = 0
						while (hailstone1 != hailstone2 && (hailstone2.vel.dx - vx) == 0.0) {
							hailstone2 = randomVectors[i]
							i++
						}
//						x = hailstone.pos.x + t * (hailstone.vel.dz - vx)
//						t = (x - o.x) / (o.v - vx)
						val t = ((rock.elementAt(0).x - hailstone1.pos.x) / (hailstone1.vel.dx - vx)).toLong()
						val s = ((rock.elementAt(0).x - hailstone2.pos.x) / (hailstone2.vel.dx - vx)).toLong()

//		I				z = hailstone1.pos.z + t * (hailstone1.vel.dz - vz)
//		II				z = hailstone2.pos.z + s * (hailstone2.vel.dz - vz)
//		I == II			hailstone2.pos.z + s * (hailstone2.vel.dz - vz) = hailstone1.pos.z + t * (hailstone1.vel.dz - vz)
						val vz =
							(hailstone1.pos.z + t * hailstone1.vel.dz - hailstone2.pos.z - s * hailstone2.vel.dz) / (t - s)
						val z = hailstone1.pos.z + t * (hailstone1.vel.dz - vz)
						return (rock.elementAt(0).x + rock.elementAt(0).y + z.toLong())
					}
				}
			}
		}
		return -1
	}

	val input = readInput("Day24")
	part1(input).println()
	part2(input).println()
}
