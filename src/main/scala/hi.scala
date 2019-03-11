
object hi {

  // Unit 无值
  def main(args: Array[String]): Unit = {
    var name = "hehe"
    println(f"hi: $name%s")

    println(s"hi: $name")

    val arr = Array(1, 2, 3, 4)

    // 0 to 2 / 0 until 3
    val odd = for (e <- arr if e % 2 == 0) yield e
    for (elem <- odd) {
      println(elem)
    }


  }

}
