# FileUploadServer


#Description:
Service for upload image files, with supported actions:
- upload single file
- upload multiple files
- get file by id
- delete file by id
- delete allFiles
 

**Tech:**
- Spring Boot v1.5.7 - framework
- Tomcat embedded
- MongoDB - project database
- JUnit - for testing
- Maven - for project management 

**Requirements:**
- Java 8
- Maven
- Git
- Curl (for make http request for test server, optional)

**Build and run project from command line:**

1. clone project: `$ https://github.com/kirya46/FileUploadServer.git`
2. go to project folder: `$ cd FileUploadServer`
3. build project: `$ mvn clean && mvn package`
4. run project: `$ java -jar target/FileUploadServer-1.0-SNAPSHOT.jar`

or run project from sources without building:

1. run:  `$ mvn spring-boot:run`


**Server usage:**

- upload single file: 
      
      request: curl -X POST  -F 'file=@<PATH_TO_FILE>' -i  http://localhost:8080/image-store/upload/single
      
      example: curl -X POST  -F 'file=@/Users/kirya/Desktop/Atlantis Nebula UHD.jpg' -i  http://localhost:8080/image-store/upload/single
      
      HTTP/1.1 200 
      Content-Type: application/json;charset=UTF-8
      Transfer-Encoding: chunked
      Date: Fri, 22 Sep 2017 12:45:23 GMT
      
      ["59c505e3154daa7d35ace1dd"]
      
- upload single file with existing name:

      request: curl -X POST  -F 'file=@<PATH_TO_FILE>' -i  http://localhost:8080/image-store/upload/single
      
      example: curl -X POST  -F 'file=@/Users/kirya/Desktop/Atlantis Nebula UHD.jpg' -i  http://localhost:8080/image-store/upload/single
      
      HTTP/1.1 400 
      Content-Type: application/json;charset=UTF-8
      Transfer-Encoding: chunked
      Date: Fri, 22 Sep 2017 13:21:45 GMT
      Connection: close
      
      {"cause":null,"stackTrace":[{"methodName":"...     
        
- upload single file with wrong format name:

      request: curl -X POST  -F 'file=@<FILE_PATH>' -i  http://localhost:8080/image-store/upload/single
      
      example: curl -X POST  -F 'file=@/Users/kirya/Desktop/test1.mp3' -i  http://localhost:8080/image-store/upload/single 
      
      HTTP/1.1 400 
      Content-Type: application/json;charset=UTF-8
      Transfer-Encoding: chunked
      Date: Fri, 22 Sep 2017 13:23:29 GMT
      Connection: close
      
      {"cause":null,"stackTrace":[{"methodName":"...    
      
              
- upload multiple files:

      request: curl -X POST -F 'file=@<PATH_TO_FILE>' -F 'file=@<PATH_TO_FILE>' -i http://localhost:8080/image-store/upload/multiple
      example: curl -X POST -F 'file=@/Users/kirya/Desktop/Ff6a9mL.jpg' -F 'file=@/Users/kirya/Desktop/FQVWz2.jpg' -i http://localhost:8080/image-store/upload/multiple
      
      HTTP/1.1 200 
      Content-Type: application/json;charset=UTF-8
      Transfer-Encoding: chunked
      Date: Fri, 22 Sep 2017 12:48:09 GMT
      
      ["59c50688154daa7d35ace1de","59c50689154daa7d35ace1df"]
                      
- find file by id and size SMALL: 

      request: curl -X GET -i http://localhost:8080/image-store/<FILE_ID>/small
      
      example: curl -X GET -i http://localhost:8080/image-store/59c50688154daa7d35ace1de/small
        
      HTTP/1.1 200 
      Content-Disposition: attachment; filename="Ff6a9mL.jpg"
      Content-Type: application/json
      Content-Length: 13150
      Date: Fri, 22 Sep 2017 12:51:05 GMT
      
- find file by id and size BIG: 

      request: curl -X GET -i http://localhost:8080/image-store/<FILE_ID>/big
      
      example: curl -X GET -i http://localhost:8080/image-store/59c50688154daa7d35ace1de/big
        
      HTTP/1.1 200 
      Content-Disposition: attachment; filename="Ff6a9mL.jpg"
      Content-Type: application/json
      Content-Length: 13150
      Date: Fri, 22 Sep 2017 12:51:05 GMT

- find file by id and size BIG which was deleted from file system without using server API: 
      
        request: curl -X GET -i http://localhost:8080/image-store/<FILE_ID>/big
        example: curl -X GET -i http://localhost:8080/image-store/59c50688154daa7d35ace1de/big
           
        HTTP/1.1 500 
        Content-Type: application/json;charset=UTF-8
        Transfer-Encoding: chunked
        Date: Fri, 22 Sep 2017 13:29:53 GMT
        Connection: close
        
        {"cause":null,"stackTrace":[{"methodName":"loadAsR...  
      
- delete file by id:

      request: curl -X DELETE -i http://localhost:8080/image-store/delete/<FILE_ID>
      example: curl -X DELETE -i http://localhost:8080/image-store/delete/59c50688154daa7d35ace1de
       
      HTTP/1.1 200 
      Content-Length: 0
      Date: Fri, 22 Sep 2017 12:56:58 GMT
      
- delete file by id with not existing id:

      request: curl -X DELETE -i http://localhost:8080/image-store/delete/<FILE_ID>
      example: curl -X DELETE -i http://localhost:8080/image-store/delete/59c50688154daa7d35ace1de
       
      HTTP/1.1 400 
      Content-Type: application/json;charset=UTF-8
      Transfer-Encoding: chunked
      Date: Fri, 22 Sep 2017 13:26:08 GMT
      Connection: close
      
- delete all:

      request: curl -X DELETE -i http://localhost:8080/image-store/delete/all
      
      HTTP/1.1 200 
      Content-Length: 0
      Date: Fri, 22 Sep 2017 12:57:42 GMT


                


    
    