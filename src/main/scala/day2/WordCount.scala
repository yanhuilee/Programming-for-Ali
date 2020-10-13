package day2

object WordCount {

  // 单例对象，静态属性

  def main(args: Array[String]): Unit = {
    var words: Array[String] = Array("fieo jowj jowfjw", "jowj")

    val wordSplit = words.map((x: String) => x.split(" "))
    // 将数组中的Array扁平化
    val fltWords: Array[String] = wordSplit.flatten

    val mapWords: Map[String, Array[String]] = fltWords.groupBy((wd: String) => wd)

    val wordResult: Map[String, Int] = mapWords.map(wordKV => (wordKV._1, wordKV._2.length))

    val sortResult: List[(String, Int)] = wordResult.toList.sortBy(t => t._2)
    sortResult.foreach(t => println(t))

    // TODO: 集合 set map 类

  }
}
