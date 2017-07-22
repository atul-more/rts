package com.att.srsbu.controller;

import static org.junit.Assert.assertEquals;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.att.srsbu.SrsBuApplication;
import com.att.srsbu.model.Center;

@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest(classes = SrsBuApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BuControllerTest {

	@LocalServerPort
	private int port;
	
	TestRestTemplate restTemplate = new TestRestTemplate();

	HttpHeaders headers = new HttpHeaders();
	
	String httpGetUrlsToTest [] = {"/admin/center/list",
			"/admin/company/list"
	};
	String expectedContentType = "application/json;charset=UTF-8"; 
	
	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
	
	private Center getTestDataCenter(String action) {
		Center center = new Center();
		
		if(action.equalsIgnoreCase("update")) {
			center.setCenterId("TSTCEN"); center.setCenterName("Update Name"); center.setCompanyId("TSUPT");
			center.setCenterDesc("Update DESC"); center.setCreatedBy("am5855"); center.setOperationHrs("Update OP_HRS");
		} else {
			center.setCenterId("TSTCEN"); center.setCenterName("TSTCEN Name"); center.setCompanyId("TSTCM");
			center.setCenterDesc("TSTCEN DESC"); center.setCreatedBy("am5854"); center.setOperationHrs("TST OP_HRS");
		}
		
		return center;
	}
	
	//Todo: Check if list can be asserted for size > 0.
	@Test
	public void test1HttpGetUrls() {
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		for (String reqUrl : httpGetUrlsToTest) {
			ResponseEntity<String> response = restTemplate.exchange(
					createURLWithPort(reqUrl),
					HttpMethod.GET, entity, String.class);
			
			assertEquals("Assert failed in class: " + this.getClass().getSimpleName() + ", method: " + Thread.currentThread().getStackTrace()[1].getMethodName(), 
					expectedContentType, response.getHeaders().getContentType().toString());
			assertEquals("Assert failed in class: " + this.getClass().getSimpleName() + ", method: " + Thread.currentThread().getStackTrace()[1].getMethodName(), 
					HttpStatus.OK, response.getStatusCode());
		}
	}
	
	@Test
	public void test2AddCenter() {
		Center center = getTestDataCenter("add");
		HttpEntity<Center> entity = new HttpEntity<Center>(center, headers);
		String [] resultsToVerify = {"0", "2"};
		
		for (String result : resultsToVerify) {
			ResponseEntity<String> response = restTemplate.exchange(
					createURLWithPort("/admin/center/add"),
					HttpMethod.POST, entity, String.class);
			
			assertEquals("Assert failed in class: " + this.getClass().getSimpleName() + ", method: " + Thread.currentThread().getStackTrace()[1].getMethodName(), 
					result, response.getBody());
			assertEquals("Assert failed in class: " + this.getClass().getSimpleName() + ", method: " + Thread.currentThread().getStackTrace()[1].getMethodName(), 
					expectedContentType, response.getHeaders().getContentType().toString());
			assertEquals("Assert failed in class: " + this.getClass().getSimpleName() + ", method: " + Thread.currentThread().getStackTrace()[1].getMethodName(), 
					HttpStatus.OK, response.getStatusCode());	
		}		
	}
	
	//Todo: Add a check whether the center exists before updating
	@Test
	public void test3UpdateCenter() {
		Center center = getTestDataCenter("update");
		HttpEntity<Center> entity = new HttpEntity<Center>(center, headers);
		
		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/admin/center/update"),
				HttpMethod.PUT, entity, String.class);
		
		assertEquals("Assert failed in class: " + this.getClass().getSimpleName() + ", method: " + Thread.currentThread().getStackTrace()[1].getMethodName(), 
				"0", response.getBody());
		assertEquals("Assert failed in class: " + this.getClass().getSimpleName() + ", method: " + Thread.currentThread().getStackTrace()[1].getMethodName(), 
				expectedContentType, response.getHeaders().getContentType().toString());
		assertEquals("Assert failed in class: " + this.getClass().getSimpleName() + ", method: " + Thread.currentThread().getStackTrace()[1].getMethodName(), 
				HttpStatus.OK, response.getStatusCode());	
	}
	
	//Todo: Add test for deleting non-existing entry	
	@Test
	public void test4DeleteCenter() {
		Center center = getTestDataCenter("delete");
		HttpEntity<Center> entity = new HttpEntity<Center>(center, headers);
		
		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/admin/center/delete/" + center.getCenterId()),
				HttpMethod.DELETE, entity, String.class);
		
		assertEquals("Assert failed in class: " + this.getClass().getSimpleName() + ", method: " + Thread.currentThread().getStackTrace()[1].getMethodName(), 
				"0", response.getBody());
		assertEquals("Assert failed in class: " + this.getClass().getSimpleName() + ", method: " + Thread.currentThread().getStackTrace()[1].getMethodName(), 
				expectedContentType, response.getHeaders().getContentType().toString());
		assertEquals("Assert failed in class: " + this.getClass().getSimpleName() + ", method: " + Thread.currentThread().getStackTrace()[1].getMethodName(), 
				HttpStatus.OK, response.getStatusCode());	
	}
}
