package us.marek.rng

import java.util.Random

object RNGBenchmark extends App {
  
  import Timing.timeIt

  val r = new Random()
  val xr = new XORShiftRandom()
  val ur = new UnsafeRandom()
  
  val max = 1000000
  val list = 1 to max

  def forCompTest = for (i <- list) r.nextInt()

  def foreachTest = list.foreach(i => r.nextInt())

  def mapTest = list.map(i => r.nextInt())

  def whileTest = {
    var sum = 0 // need for dead code avoidance
    var i = 0
    while (i < max) {
      sum |= r.nextInt()
      i += 1
    }
  }
  
  println(s"\nRunning forCompTest took ${timeIt(1)(forCompTest)} ms")
  println(s"Running forEachTest took ${timeIt(1)(foreachTest)} ms")
  println(s"Running whileTest took ${timeIt(1)(whileTest)} ms\n")
  
  val prefix = "Running 100 million iterations of"
  val numIter = 100000000
  
  println(s"$prefix java.util.Random.nextInt() took ${timeIt(numIter)(r.nextInt())} ms")
  println(s"$prefix UnsafeRandom.nextInt() took ${timeIt(numIter)(ur.nextInt())} ms")
  println(s"$prefix XORShiftRandom.nextInt() took ${timeIt(numIter)(xr.nextInt())} ms")

}


class UnsafeRandom(init: Long) extends java.util.Random(init) {
  
  def this() = this(System.nanoTime)
  
  private var seed = init
  
  override protected def next(bits: Int): Int = {
    seed = (seed * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1)
    (seed.toInt >>> (48 - bits))
  }
  
  override def setSeed(s: Long) = {
    seed = s
  }
}


class XORShiftRandom(init: Long) extends java.util.Random(init) {
  
  def this() = this(System.nanoTime)

  private var seed = init
  
  // we need to just override next - this will be called by nextInt, nextDouble,
  // nextGaussian, nextLong, etc.
  override protected def next(bits: Int): Int = {    
    var nextSeed = seed ^ (seed << 21)
    nextSeed ^= (nextSeed >>> 35)
    nextSeed ^= (nextSeed << 4)  
    seed = nextSeed
    (nextSeed & ((1L << bits) -1)).asInstanceOf[Int]
  }

  override def setSeed(s: Long) = {
    seed = s
  }
}