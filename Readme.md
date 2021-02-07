### **Data Engineer - Coding exercise**
**Simple version**
This application searches a list of words in a directory
and returns up to 10 files in the directory, ordered by ranking 
in descending order. The ranking indicates 
which percentatge of the list of words were found in the file.
The search is not cap sensivity, then it will find the words even they 
are in caps or lower case or mix.
In the version, the word is considerd a sequence of characters.
Then, if you search "manage" and the file contents "management", the 
search considered as found. However, you still can search for 'manage ', adding space to the end, then,
management will not be included.


**Repository contents**
* Package advsearcksimple
    * **AdvSearch:**  Main app to run. Requires the directory as a parameter.      
      if the directory is not indicated, testdata folder is assumed
      if the directory is invalid, an error is showed
    * **FileInMemory:** Object class with functions to manage the FileInMemorySpark structure
* Test
    * **Resources/testdata:** sample files text to test the app.
    * **FileInMemorySpark**: Object class with function to test the FileInMemorySpark class
      using the sample files.

**Run the application.**
- Clone the repository in IntelliJ
- Open the Terminal
- In the folder of the repository exec:
    * sbt clean
    * sbt compile
    * sbt "run [directory_to_search]"

You can run "sbt run", then the directory is setted to the sample files: ./src/test/resources/testdata



  
