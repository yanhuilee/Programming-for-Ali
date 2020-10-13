package day2

object huigu {

  def main(args: Array[String]): Unit = {
//    var name: String = _
    for (i <- 1 to 10; if i % 2 == 0) {
//      println(i)
    }
  }

  def sum(nums: Int*): Int = {
    var sum = 0
    for (num <- nums) {
      sum += num
    }
    sum
  }

  // 高阶函数：参数或返回结果是函数的函数
  def apply(f: Int => String, v: Int) = f(v)
  def layout(x: Int) = "[" + x.toString() + "]"
  println(apply(layout, 10))

  var x: Array[String] = new Array[String](3)
  var y = new Array[String](3)
  var z = Array(1, 2, 3)
  z(0) = 100

}
