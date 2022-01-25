## Milestone 2

### steps
1. use maven to compile the whole project:
```shell
mvn compile
```
- the output files should be at the **target** folder

2. Run the test files:
- Find the JsonObject based on the input path:
  - for test extract subobject
```shell
 mvn test -D test=org.json.junit.FindXMLTest
```
   - for test replacement subobject
```shell
 mvn test -D test=org.json.junit.ReplaceXMLTest
```
#### test extract subObject
1. see the class in XML file(src/main/java/org/json/XML.java) ```JSONObject toJSONObject(Reader reader, JSONPointer path)```
    - we parse the path and xml recurively. 
    - when matching the first tag in the path, remove it and continue parsing
    - we set the ```isFirstTimeFinishParsePath``` to check if it is time to store the subobject we want

2. file location:src/test/java/org/json/junit/FindXMLTest.java 
   
3. we test six situations:

    Three are corner cases: 1. empty path 2. empty xml 3. invalid path

    Three are regular cases: 1. most inside subObject 2. General subObject 3. Unorder extraction

    Test Results：
       ![avatar](./images/img1.png)

#### test replace subObject
1. see the class in XML file(src/main/java/org/json/XML.java) ```JSONObject toJSONObject(Reader reader, JSONPointer path, JSONObject replacement)```
    - we parse the path and xml recurively.
    - when matching the first tag in the path, remove it and continue parsing
    - we set two global variables to check if it is time to replace the subobject 
    - when the last tag matches the tagName, we do the replacement 
    - at the same time we need to check if there are unrelated tags in the path. 

2. file location:src/test/java/org/json/junit/ReplaceXMLTest.java

3. we test six situations:

   Three are corner cases: 1. invalid path 2. empty xml 3. unrelated tags in JsonPointer 
   
   Three are regular cases: 1. most inside subObject 2. General subObject 3. Unorder extraction
   
   Test Results：
   ![avatar](./images/ImageTest2.png)