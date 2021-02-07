package advsearchsimple


import java.io.File
import java.nio.charset.CodingErrorAction
import scala.io.{Codec, Source}

//structure to keep files in memory
case class FileInMemory(name: String,content: String,ranking: Float){
}

object FileInMemory {
  private implicit val codec: Codec = Codec("UTF-8")
  codec.onMalformedInput(CodingErrorAction.REPLACE)
  codec.onUnmappableCharacter(CodingErrorAction.REPLACE)

  //function to get the list of files from a folder
  def existPath(directory_name: String): Boolean = {
    val directory=new File(directory_name)
    directory.exists()
  }

  //function to get the list of files from a folder
  def getListOfFiles(directory: File): List[File] = {
    directory.listFiles.filter(_.isFile).toList
  }

  //init List of FileInMemory with ranking=0 using the list of files in the folder
  def initFileInMemory(list_files:List[File]): List[FileInMemory] = {
    list_files map {x => FileInMemory(x.getName,Source.fromFile(x).getLines.mkString.toLowerCase,0)}
  }

  //calculate ranking of matching for an specific file in memory (file_in_memory)
  //array_findings will contain true when the word is found false if not
  def calcRanking(file_in_memory: FileInMemory, words_to_find: Array[String] ) : Float = {
    val array_findings = words_to_find map {x=>file_in_memory.content.contains(x)}
    (array_findings.count(x => x).toFloat / array_findings.length.toFloat)*100
  }

  //update ranking for an specific file in memory (fm). Returns a new copy updated
  def updateRanking(file_in_memory: FileInMemory, v:Float): FileInMemory = {
    file_in_memory.copy(ranking=v)
  }

  //wrap function to calculate ranking and update it in the element
  def changeElem(file_in_memory: FileInMemory, words_to_find: Array[String]): FileInMemory = {
    val r = calcRanking(file_in_memory,words_to_find)
    updateRanking(file_in_memory,r)
  }
  //calculate and returns a new list of files in memory with the new ranking
  def changeList(list_file_in_memory: List[FileInMemory], words_to_find: Array[String]): List[FileInMemory] = {
    list_file_in_memory map {x =>changeElem(x,words_to_find)}
  }

  def genMapToReturn(list_file_in_memory:List[FileInMemory]): Map[String,Float] = {
    val lnames = list_file_in_memory map { x => x.name}
    val lranking = list_file_in_memory map {x=> x.ranking}
    (lnames zip lranking).sortBy(_._2).reverse.toMap
  }

  //entry point: principal function to calculate the rannking of the list of words
  def calcRankingTotal(path: String, words_to_find:Array[String]): Map[String, Float] = {
    val files=getListOfFiles(new File(path))
    val list_file_in_memory_0=initFileInMemory(files)
    genMapToReturn(changeList(list_file_in_memory_0,words_to_find))
  }
}
