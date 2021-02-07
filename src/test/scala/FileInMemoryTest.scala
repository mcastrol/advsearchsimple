import advsearchsimple.FileInMemory.{calcRanking, calcRankingTotal, getListOfFiles, initFileInMemory}

import java.io.File

object FileInMemoryTest {
  def TestGetListOfFiles(path: String, list_of_files: Array[String]): Unit = {
    val files_in_dir = getListOfFiles(new File(path))
    for (fd <- files_in_dir) assert(list_of_files.contains(fd.getName), "Fail in getListOfFiles")
  }

  def TestInitFileInMemorySpark(path: String, list_of_files: Map[String, Int]): Unit = {
    val files_in_dir = getListOfFiles(new File(path))
    val file_in_memory_spark = initFileInMemory(files_in_dir)
    for (fms <- file_in_memory_spark) {
      assert(list_of_files(fms.name) == fms.content.length, "Fail in initFileInMemory")
    }
  }

  def TestCalcRankingSpark(path: String, file_name: String, words_to_find: Array[String], ranking: Float): Unit = {
    val files_in_dir = getListOfFiles(new File(path))
    val file_in_memory_spark = initFileInMemory(files_in_dir)
    for (fms <- file_in_memory_spark) {
      if (fms.name == file_name) {
        assert(calcRanking(fms, words_to_find) == ranking, "Fail in ranking")
      }
    }
  }

  def TestCalcRankingTotalSpark(path: String, words_to_find: Array[String], expected_results: Map[String, Float]): Unit = {
    val map_results = calcRankingTotal(path: String, words_to_find: Array[String])
    for (x <- expected_results) {
      assert(map_results(x._1) == x._2)
    }
  }


  def main(args: Array[String]): Unit = {
    val path = "./src/test/resourses/testdata/"
    val list_of_files = Map("lorem_ipsum0.txt" -> 3248, "lorem_ipsum1.txt" -> 3049, "lorem_ipsum2.txt" -> 2204, "test.txt" -> 174)
    //check if get expected list of files
    TestGetListOfFiles(path, list_of_files.keys.toArray)
    //check initFileInMemory
    TestInitFileInMemorySpark(path, list_of_files)
    //check calcranking 100%
    TestCalcRankingSpark(path,
      "lorem_ipsum0.txt", Array("mauris", "magna", "finibus"),
      100
    )

    //check calcranking 80%
    TestCalcRankingSpark(path,
      "lorem_ipsum0.txt", Array("mauris", "magna", "finibus", "adevinta", "convallis"),
      80
    )

    //check calcranking 0%
    TestCalcRankingSpark(path,
      "lorem_ipsum0.txt", Array("mauris;", "magnax ", "finibusX", "adevintaA", "convallis0"),
      0
    )

    TestCalcRankingTotalSpark(path,
      Array("mauris", "magna", "finibus", "adevinta", "convallis"),
      Map("lorem_ipsum1.txt" -> 80f, "lorem_ipsum2.txt" -> 80f, "lorem_ipsum0.txt" -> 80f, "test.txt" -> 20f)
    )

    TestCalcRankingTotalSpark(path,
      Array("mauris", "magna", "finibus", "convallis", "himenaeos"),
      Map("lorem_ipsum1.txt" -> 100.0f, "lorem_ipsum2.txt" -> 80.0f, "lorem_ipsum0.txt" -> 100.0f, "test.txt" -> 0f)
    )

    TestCalcRankingTotalSpark(path,
      Array("mauris", "magna", "finibus", "convallis", "maximus"),
      Map("lorem_ipsum1.txt" -> 80.0f, "lorem_ipsum2.txt" -> 100.0f, "lorem_ipsum0.txt" -> 100.0f, "test.txt" -> 0f)
    )

  }

}
