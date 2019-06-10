/**
  * 方法和函数
  */
object method {

  def main(args: Array[String]): Unit = {

    // 定义方法 method
    def add(a: Int, b: Int) = a + b

    // 函数 function
    add _

    // 高阶函数
    def apply(f: Int => String, v: Int) = f(v)
    def layout(x: Int) = "[" + x.toString + "]"
    // 调用，区别？
    println(apply(layout, 10))
    println(layout(10))

    // 柯里化
    def add2(a: Int)(b: Int) = a + b
    def add3(x: Int) = (y: Int) => x + y


    def m(x: Int) = x + 3
    val f = (x: Int) => x + 3
  }

}