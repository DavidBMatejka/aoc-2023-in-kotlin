import kotlin.math.roundToInt

fun main() {
//	data class Position(val x: Double, val y: Double, val z: Double)
	data class Position(val x: Long, val y: Long, val z: Long)
	data class Velocity(val dx: Double, val dy: Double, val dz: Double) {
		fun linearMultiple(other: Velocity) : Boolean {
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
		} else {
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
				val(pos, timeA, timeB) = intersect(vectors[i], vectors[j])
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
		while (rock.size != 1){
			val randomVectors = vectors.shuffled().take(5)
//			val randomVectors = vectors.take(5)

//			val randomVectors = mutableListOf(
//				Vector(pos=Position(x=304353660638068, y=351278759340154, z=390067282126063), vel=Velocity(dx=-82.0, dy=-112.0, dz=-217.0)),
//				Vector(pos=Position(x=283751929361304, y=359779102923536, z=253638869482629), vel=Velocity(dx=-91.0, dy=-103.0, dz=26.0)),
//				Vector(pos=Position(x=310973968748912, y=66801410788034, z=390414567275349), vel=Velocity(dx=-20.0, dy=262.0, dz=-84.0)),
//				Vector(pos=Position(x=320862011031673, y=558858538944333, z=366383159937963), vel=Velocity(dx=-229.0, dy=-678.0, dz=-330.0))
//			)

//			[Vector(pos=Position(x=240537427307140, y=322136671432640, z=249196664210477), vel=Velocity(dx=28.0, dy=-11.0, dz=44.0)), Vector(pos=Position(x=215751176267772, y=376619563956940, z=230133299986253), vel=Velocity(dx=-120.0, dy=126.0, dz=-352.0)), Vector(pos=Position(x=323115200120868, y=235587723802088, z=295954349794173), vel=Velocity(dx=22.0, dy=-59.0, dz=104.0)), Vector(pos=Position(x=238540184953467, y=346812031824029, z=230471715374112), vel=Velocity(dx=-45.0, dy=28.0, dz=9.0)), Vector(pos=Position(x=213522113613397, y=373489228592728, z=228648610847661), vel=Velocity(dx=65.0, dy=-91.0, dz=24.0))]

//			val randomVectors = mutableListOf(
//				Vector(pos=Position(x=20, y=19, z=15), vel=Velocity(dx=1.0, dy=-5.0, dz=-3.0)),
//				Vector(pos=Position(x=20, y=25, z=34), vel=Velocity(dx=-2.0, dy=-2.0, dz=-4.0)),
//				Vector(pos=Position(x=19, y=13, z=30), vel=Velocity(dx=-2.0, dy=1.0, dz=-2.0)),
//				Vector(pos=Position(x=12, y=31, z=28), vel=Velocity(dx=-1.0, dy=-2.0, dz=-1.0))
//			)
//			val copies = mutableListOf<Vector>()
//			randomVectors.forEach {
//				copies.add(it.copy(pos = it.pos, vel = it.vel.minus(-3, 1)))
//			}
//			for (v1 in copies) {
//				for (v2 in copies) {
//					if (v1 == v2) continue
//					if (v1.vel.linearMultiple(v2.vel)) continue
//					val inter = intersect(v1, v2)
//					println("$v1 and $v2")
//					println(inter)
//					println()
//				}
//			}
//			return -11

			val longRange = -500L..500L
			for (vx in longRange) {
				for (vy in longRange) {
					val copies = mutableListOf<Vector>()
					randomVectors.forEach {
						copies.add(it.copy(pos = it.pos, vel = it.vel.minus(vx, vy)))
					}
					rock = mutableSetOf()
					for (copy1 in copies) {
						for (copy2 in copies) {
							if (copy1.vel.linearMultiple(copy2.vel)) break
							if (copy1 == copy2) continue
							val (pos, _ , _) = intersect(copy1, copy2)
							rock.add(Position(pos.x, pos.y, pos.z))
						}
					}
					if (rock.size == 1) {
//						x = hailstone.pos.x + t * (hailstone.vel.dz - vx)
//						t = (x - o.x) / (o.v - vx)
//
//		I				z = hailstone1.pos.z + t * (hailstone1.vel.dz - vz)
//		II				z = hailstone2.pos.z + s * (hailstone2.vel.dz - vz)
//		I == II			hailstone2.pos.z + s * (hailstone2.vel.dz - vz) = hailstone1.pos.z + t * (hailstone1.vel.dz - vz)
//						hailstone2.pos.z + s * hailstone2.vel.dz - s * vz = hailstone1.pos.z + t * hailstone1.vel.dz - t * vz
//						hailstone2.pos.z + s * hailstone2.vel.dz - hailstone1.pos.z + t * hailstone1.vel.dz =  - t * vz + s * vz
//						hailstone2.pos.z + s * hailstone2.vel.dz - hailstone1.pos.z + t * hailstone1.vel.dz = vz * (s - t)
//						(hailstone2.pos.z + s * hailstone2.vel.dz - hailstone1.pos.z + t * hailstone1.vel.dz ) / (s - t) = vz



						println(randomVectors)
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
						val t = ((rock.elementAt(0).x - hailstone1.pos.x) / (hailstone1.vel.dx - vx)).toLong()
						val s = ((rock.elementAt(0).x - hailstone2.pos.x) / (hailstone2.vel.dx - vx)).toLong()
						println("$rock colides with $hailstone1 $vx $vy $t")
						println("$rock colides with $hailstone2 $vx $vy $s")

						val vz = (hailstone1.pos.z + t * hailstone1.vel.dz - hailstone2.pos.z - s * hailstone2.vel.dz) / (t - s)
						val z = hailstone1.pos.z + t * (hailstone1.vel.dz - vz)
						println(z.toLong())
						return (rock.elementAt(0).x.toLong() + rock.elementAt(0).y.toLong() + z.toLong())
					}
				}
			}
		}
		println(rock)
		return -1
	}

//	val input = readInput("Day24_test")
	val input = readInput("Day24")
	part1(input).println()
	part2(input).println()
}
