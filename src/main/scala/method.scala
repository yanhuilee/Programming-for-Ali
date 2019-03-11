
object method {

  def main(args: Array[String]): Unit = {

    // 定义方法 method
    def add(a: Int, b: Int) = a + b

    // 函数 function
    add _

    // 柯里化
    def add2(x: Int) = (y: Int) => x + y

  }

}
