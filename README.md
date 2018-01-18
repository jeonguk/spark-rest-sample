# spark-rest-sample
spark rest service sample

---

##
### RUN
mvn compile && mvn exec:java

##
### Visit
http://localhost:4567/posts

##
### TEST using postman
> POST
```
http://localhost:4567/posts

{
	"title": "post title",
	"content": "post content",
	"categories": ["java", "rest service"]
}
```

> GET
```
http://localhost:4567/posts
```
