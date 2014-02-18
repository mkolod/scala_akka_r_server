package us.marek.rng

object Timing {
  
  def timeIt(numIters: Int)(f: => Unit): Long = {
    val start = System.currentTimeMillis
    forLoop(numIters)(f)
    val execTime = System.currentTimeMillis - start
    execTime
  }

  def forLoop(numIters: Int)(f: => Unit): Unit = {
    var i = 0
    while (i < numIters) {
      f
      i += 1
    }
  }
  
}