package com.anurag.service;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.anurag.component.Book;
import com.anurag.component.User;

@Service
public class UserService {
	
	private final Book book;
	private final RestTemplate restTemplate = new RestTemplate();
	private final String URL = "https://booksphereserver-production.up.railway.app/user/";
	
	UserService(Book book){
		this.book = book;
	}

	public boolean save(User user) {
		String API = "saveUser";
		return restTemplate.postForObject(URL + API, user, Boolean.class);
	}

	public User login(String email, String password) {
		String API = "checkLogin";
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("email", email);
		map.add("password", password);
		
		HttpHeaders headers = new HttpHeaders();
		
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity(map, headers);
	
		
		ResponseEntity<User> r=restTemplate.exchange(URL+API, HttpMethod.POST, requestEntity ,User.class);
		User u=r.getBody();
		System.out.println(r.getStatusCode());
		return u;

		
	}

}
