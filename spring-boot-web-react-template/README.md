### Optional (but recommended) Development Tools

- Install the [React Developer Tools](https://chrome.google.com/webstore/detail/react-developer-tools/fmkadmapgofadopljbjfkapdkoienihi?hl=en) browser extension

### Front End Dev

Run `webpack-dev-server --content-base src/main/resources/static/ --port 8081 --inline`

The front-end will be available at http://localhost:8081 and will proxy all api requests to the backend at localhost:8080.

Javascript/CSS changes will automatically trigger a browser refresh.

## Production

- Build both the web and server components with `mvn clean verify`. This automatically runs webpack and includes the front-end resources in the fat jar.
- Run the application using `java -jar {{ jar_name }}.jar`
- For testing, use `mvn spring-boot:run`

### Useful maven dependencies

* RESTful controllers:
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-data-rest</artifactId>
	</dependency>

* Lombok:
    <dependency>
		<groupId>org.projectlombok</groupId>
		<artifactId>lombok</artifactId>
		<version>1.16.10</version>
		<scope>provided</scope>
	</dependency>
	
* MongoDB:
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-mongodb</artifactId>
    </dependency>

* Postgres:
** pom.xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <!-- Read more: http://mrbool.com/rest-server-with-spring-data-spring-boot-and-postgresql/34023 -->
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
    </dependency>

** application.properties:
    spring.jpa.database=POSTGRESQL
	spring.datasource.platform=postgres
	spring.jpa.show-sql=false
	spring.jpa.hibernate.ddl-auto=create-drop        # For testing, else use verify
	spring.database.driverClassName=org.postgresql.Driver
	spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
	spring.datasource.username=postgres
	spring.datasource.password=postgres

### Providing a RESTful interface to a database repository for objects of class X:
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface XRepository extends PagingAndSortingRepository<X, Long> {
}

### Providing additional query search methods, e.g on X.name:
import java.util.List;
import org.springframework.data.repository.query.Param;

@RepositoryRestResource
public interface XRepository extends PagingAndSortingRepository<X, Long> {
    List<X> findByName(@Param("name") String name);
}