# spark-rest-sample
spark rest service sample

---

##
###
- Spark Java 2.7.1
- Jackson 2.9.3
- Java 8
- Maven 3
- Postman


##
### RUN
mvn clean install
java -jar target/spark-rest-service.jar

##
### Visit
http://localhost:8080/


##
### TEST using postman

- POST    /posts/add - POST request to add an post.
- GET     /posts/:id - GET request to get post by :id. 
- GET     /posts     - GET request to get all the posts.
- PUT     /posts/:id - UPDATE request to update an post by :id.
- DELETE  /posts/:id - DELETE request to delete an post by :id.

```
POST (application/json)
http://localhost:8080/posts
{
	"title": "post title",
	"content": "post content",
	"categories": ["java", "rest service"]
}
```

```
GET ALL POSTS
http://localhost:8080/posts
```

```
GET POST
http://localhost:8080/posts/1
```

```
PUT
http://localhost:8080/posts/1
{
  "title" : "post title edit",
  "content" : "post content edit",
  "categories" : [ "java", "rest service" ]
}
```

```
DELETE
http://localhost:8080/posts/1
```

