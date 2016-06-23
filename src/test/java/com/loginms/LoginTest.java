package com.loginms;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.cisco.blogapp.LoginMicroVerticle;
import com.hazelcast.util.Base64;
import com.jayway.restassured.filter.session.SessionFilter;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

//import java.util.Base64;
@RunWith(VertxUnitRunner.class)
public class LoginTest {
	
	String baseURL = "http://localhost:8086";
	String userName = "sand";
	String password = "pass";
	Vertx vertx;
	SessionFilter sessionFilter = new SessionFilter();
	
	@Before
    public void before(TestContext context) {
		VertxOptions options = new VertxOptions().setWorkerPoolSize(10);
		vertx = Vertx.vertx(options);
		vertx.deployVerticle(LoginMicroVerticle.class.getName(), stringAsyncResult -> {
			System.out.println(LoginMicroVerticle.class.getName() + "Deployment Completed");
		});
    }
	@After
    public void after(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

	public void validateregister() {
		String url = baseURL + "/Services/rest/user/register";
		
		given()
		.filter(sessionFilter)
			.header("Authorization","Basic"+ Base64.encode(userName.getBytes())+":"+Base64.encode(password.getBytes())+ "}\"")//"{\"Authorization: Basic"+ Base64.encode(userName.getBytes())+ "}\"")
			.body("{\"id\":\"55716669eec5ca2b6ddf5629\", \"userName\":\"sand\",\"password\":\"pass\",\"email\":\"vinay@gmail.com\",\"first\":\"Vinay\",\"last\":\"Prasad\",\"companyId\":\"55716669eec5ca2b6ddf5626\",\"siteId\":\"55716669eec5ca2b6ddf5627\",\"deptId\":\"55716669eec5ca2b6ddf5628\"}")
		.when()
			.post(url)
		.then()
			.statusCode(204);
	}
	
	
	public void validatelogin() {
		String url = baseURL +"/Services/rest/user/auth";
		System.out.println(url);
		given()
		.filter(sessionFilter)
//			.body("{\"userName\":\"sand\",\"password\":\"pass\"}")
			.header("Authorization"," Basic"+ Base64.encode(userName.getBytes())+":"+Base64.encode(password.getBytes())+ "}\"")
		.when()
			.post(url)
		.then()
			.statusCode(204);

	
	}
	
	public void validatesignin() {
		
		String url = baseURL +"/Services/rest/user/auth";
		given()
		.filter(sessionFilter)
			.body("{\"userName\":\"sand\",\"password\":\"pass\"}")
		.when()
			.post(url)
		.then()
			.statusCode(204);

		
		 url = baseURL +"/Services/rest/blogs";
		
		

	}
	@Test
	public void test001_Auth(){
		
		//validateregister();
		//validatelogin();
		//validatesignin();
		
//		String url = baseURL +"/Services/rest/user/auth";
//		System.out.println(url);
//		given()
//		.filter(sessionFilter)
////			.body("{\"userName\":\"sand\",\"password\":\"pass\"}")
//			.header("Authorization","Basic"+ Base64.encode(userName.getBytes())+":"+Base64.encode(password.getBytes())+ "}\"")
//		.when()
//			.post(url)
//		.then()
//			.statusCode(204);
		
		
	}
	
//	@Test
//	public void test002_LoginNegativeTest() {
//		String url = baseURL +"/Services/rest/user/auth";
//		given()
//			.body("{\"userName\":\"invalidUser\",\"password\":\"pass\"}")
//		.when()
//			.post(url)
//		.then()
//			.statusCode(404);
//	}
//	

	@Test
	public void test003_validatecompanies() {
		
		String url = baseURL + "/Services/rest/company";
		System.out.println(url);
		given().
		when().
			get(url).
		then().
			statusCode(200).
			body("[0].id", equalTo("55716669eec5ca2b6ddf5626")).
			body("[1].id", equalTo("559e4331c203b4638a00ba1a"));
	}
}