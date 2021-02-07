package advsearchsimple

import advsearchsimple.FileInMemory.{calcRankingTotal, existPath}

object AdvSearch extends App {

  def printResults(path: String, words: String, mr: Map[String, Float], topN: Int): Unit = {
    val max_name = 40
    val formatname = "%0$-40s"
    printf("Ranking of searching words: %s in folder %s\n", words, path)
    mr.take(topN).foreach {
      x => {
        val l = if (x._1.length > max_name) max_name else x._1.length
        printf("%s", String.format(formatname, x._1.substring(0, l)))
        print("\t")
        print(x._2)
        println()
      }
    }
  }

  def printWelcome(): Unit = {
    println("")
    println("Hello!!. Welcome to the search text engine for Adevinta")
    println("")
    println("I'm going to search the words you need in the files in folder: " + args(0))
    println("Please enter the list of words comma separated, for instance: ")
    println("adevinta,search,engine")
    println("If you need to use a complete word and avoid match of a larger word that contain it,")
    println("quote them and add a blank. For instance, put 'courious ' to exclude 'couriousity' in matching")
  }

  def console(path: String): Unit = {
    //set variables
    val topN = 10 //config how many files show
    val scanner = new java.util.Scanner(System.in)
    var done = false
    while (!done) {
      printf("Please, enter the words to search in %s (comma separated) - no words or quit to finish\n", path)
      print("search> ")
      val words = scanner.nextLine()
      if (words.isEmpty || words.contains("quit"))
        done = true
      else {
        val words_to_find = words.toLowerCase.split(",")
        printf("Wait... searching %d words: %s in folder %s\n", words_to_find.length, words, path)
        if (words.toLowerCase.split(",").length > 0) {
          val mr = advsearchsimple.FileInMemory.calcRankingTotal(path, words.toLowerCase.split(","))
          printResults(path, words, mr, topN)
        }
      }
    }
  }

  val path = if(args.length>0) args(0) else "./src/test/resourses/testdata/" //path to search files 1st. parameter
  if (existPath(path)) console(path)
  else println("Please, indicate a valid directory to search")
}
