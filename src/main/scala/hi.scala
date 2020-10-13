
object hi {

  // Unit 无值
  def main(args: Array[String]): Unit = {
    var name = "hehe"
    println(f"hi: $name%s") //${表达式}
    name = "anzi"
    println(s"hi: $name")

    val arr: Array[Int] = Array(1, 2, 3, 4)

    // 0 to 2 / 0 until 3
    val odd = for (e <- arr if e % 2 == 0) yield e
    for (elem <- odd) { // i <- odd odd(1)
      println(elem)
    }

    println("双层for；不同的两位数")
    for (i <- 1 to 3; j <- 1 to 3; if i != j) {
      print((10 * i + j) + " ")
    }

  }

}