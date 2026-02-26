package com.anurag.service;

import org.jspecify.annotations.Nullable;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.anurag.component.Book;
import com.anurag.component.User;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class BookService {
	
	private RestTemplate restTemplate = new RestTemplate();
	private final String URL = "https://booksphereserver-production.up.railway.app/";
	
	public List<Book> searchBook(String name) {
		String API = "booksByName/" + name;
		List<Book> books = restTemplate.getForObject(URL+ API, List.class);
		return books;
	}
	
	public List<Book> getAllBooks() {
		String API = "getAllBooks";
		
	    ResponseEntity<List> r = restTemplate.exchange(URL + API, HttpMethod.GET, null, List.class);
	    List<Book> books = r.getBody();
	    
	    return books;
	}

	public List<Book> getAllBooksByUser(User u) {
		String API = "getAllBooksByUser";
		
		HttpEntity<User> requestEntity = new HttpEntity<User>(u);
		ResponseEntity<List> r = restTemplate.exchange(URL + API , HttpMethod.POST, requestEntity, List.class);
		List<Book> books = r.getBody();
		return books;
	}

	public boolean saveBook(Book book) {
		String API = "saveBook";
		
		HttpEntity<Book> requestEntity=new HttpEntity<Book>(book);
		ResponseEntity<Boolean> r=restTemplate.exchange(URL+API, HttpMethod.POST, requestEntity ,Boolean.class);
		Boolean result=r.getBody();
		System.out.println(r.getStatusCode());
		
		return result;

	}

	public Book getBook(int id) {
		String API = "getBook/"+id;
		HttpEntity requestEntity=null;
		ResponseEntity<Book> r=restTemplate.exchange(URL+API, HttpMethod.GET, requestEntity ,Book.class);
		Book result=r.getBody();
		
		return result;
	}

	public boolean deleteBook(int id) {
		String API="deleteBook/"+id;
		
		HttpEntity requestEntity=null;
		ResponseEntity<Boolean> r=restTemplate.exchange(URL+API, HttpMethod.DELETE, requestEntity ,Boolean.class);
		Boolean result=r.getBody();
		
		return result;

	}
	
	public Boolean updateImage(int id, MultipartFile file) throws IOException {

	    String API = "/updateImage/" + id;

	    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

	    body.add("cImage", new ByteArrayResource(file.getBytes()) {
	        @Override
	        public String getFilename() {
	            return file.getOriginalFilename();
	        }
	    });

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

	    HttpEntity<MultiValueMap<String, Object>> requestEntity =
	            new HttpEntity<>(body, headers);

	    ResponseEntity<Boolean> response =
	            restTemplate.exchange(
	                    URL + API,
	                    HttpMethod.POST,   // ✅ MUST BE POST
	                    requestEntity,
	                    Boolean.class
	            );

	    return response.getBody();
	}

	public Boolean updatePdf(int id, MultipartFile file) throws IOException {

	    String API = "/updatePdf/" + id;

	    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

	    body.add("ctn", new ByteArrayResource(file.getBytes()) {
	        @Override
	        public String getFilename() {
	            return file.getOriginalFilename();
	        }
	    });

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

	    HttpEntity<MultiValueMap<String, Object>> requestEntity =
	            new HttpEntity<>(body, headers);

	    ResponseEntity<Boolean> response =
	            restTemplate.exchange(
	                    URL + API,
	                    HttpMethod.POST,
	                    requestEntity,
	                    Boolean.class
	            );

	    return response.getBody();
	}

	public Boolean updatePrice(int id, int price) {

	    String API = "/updatePrice/" + id + "/" + price;

	    ResponseEntity<Boolean> response =
	            restTemplate.exchange(
	                    URL + API,
	                    HttpMethod.POST,
	                    null,
	                    Boolean.class
	            );

	    return response.getBody();
	}

	public List<Book> searchBooks(String keyword) {

	    String API = "/search/" + keyword;

	    ResponseEntity<Book[]> response =
	            restTemplate.getForEntity(
	                    URL + API,
	                    Book[].class
	            );

	    return Arrays.asList(response.getBody());
	}

	public Book getBookById(int id) {

	    String API = "/getBook2/" + id;

	    ResponseEntity<Book> response =
	            restTemplate.getForEntity(URL + API, Book.class);

	    return response.getBody();
	}
	
}
