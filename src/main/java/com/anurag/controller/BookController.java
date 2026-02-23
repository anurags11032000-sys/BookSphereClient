package com.anurag.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anurag.component.Book;
import com.anurag.component.User;
import com.anurag.service.BookService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class BookController {
	@Autowired
	BookService bookservice;
	@GetMapping("/")
	public String home() {
		return "index";
	}
	
	@PostMapping("/SearchBook")
	public String searchBook(String name , Model m) {
		List<Book> books = bookservice.searchBook(name);
		m.addAttribute("books", books);
		return "index";
	}
	
	@GetMapping("/AllBooks")
	public String allBooks(HttpSession session, Model m) {
		if(session.getAttribute("user") == null) {
			m.addAttribute("msg", "Please Login!");
			return "login-signup";
		}else {
			m.addAttribute("books", bookservice.getAllBooks());
			return "AllBooks";
		}
	}
	
	@GetMapping("/MyBooks")
	public String myBooks(HttpSession session , Model m ) {
		User u = (User)session.getAttribute("user");
		if(u == null) {
			m.addAttribute("msg", "Please Login!");
			return "login-signup";
		}else {
			m.addAttribute("books", bookservice.getAllBooksByUser(u));
			return "MyBooks";
		}
	}
	
	@PostMapping("/AddBook")
	public String addBook(Book b , @RequestPart MultipartFile ctn , @RequestPart MultipartFile cImage, HttpSession session, Model m) throws IOException {
		User u = (User) session.getAttribute("user");
		if(u == null) {
			m.addAttribute("msg", "Please Login!");
			return "login-signup";
		}else {
			byte[] c = ctn.getBytes();
			byte[] ci = cImage.getBytes();
			if(c.length == 0) {
				c=null;
			}
			if(ci.length == 0) {
				ci = null;
			}
			
			b.setCoverImage(ci);
			b.setContent(c);
			b.setUser(u);
			boolean result = bookservice.saveBook(b);
			if(result) {
				m.addAttribute("msg", "Added Successfully!");
			}else {
				m.addAttribute("msg", "Book name Already Exist!");
			}
			
			return "UserHome";
		}
	}
	
	@GetMapping("/getCoverImage")
	public void getMethodName(@RequestParam int id , HttpServletResponse response) throws IOException {
		Book book = bookservice.getBook(id);
		if(book != null) {
			byte[] image = book.getCoverImage();
			if(image == null || image.length == 0) {
				InputStream is = this.getClass().getClassLoader().getResourceAsStream("static/book.png");
				image = is.readAllBytes();
			}
			response.getOutputStream().write(image);
		}
	}
	
	@GetMapping("/viewPDF")
	public void viewPDF(@RequestParam int id, HttpServletResponse response) throws IOException {
		Book book = bookservice.getBook(id);
		if(book != null) {
			byte[] content = book.getContent();
			response.getOutputStream().write(content);
		}
	}
	
	@GetMapping("/downloadPDF")
	public void downloadPDF(@RequestParam int  id, HttpServletResponse response) throws IOException {
		Book book = bookservice.getBook(id);
		if(book != null) {
			byte[] content = book.getContent();
			response.setHeader("Content-Disposition", "attachment; filename=" + book.getName()+".pdf");
			response.getOutputStream().write(content);
		}
	}
	
	@PostMapping("/deleteBook")
	public String deleteBook(@RequestParam("id") int id , HttpSession session , Model m ) {
		boolean deleteResult = bookservice.deleteBook(id);
		m.addAttribute("deleteResult", deleteResult);
		
		User u = (User)session.getAttribute("user");
		List<Book> books = bookservice.getAllBooksByUser(u);
		m.addAttribute("books", books);
		return "MyBooks";
	}
	
	@GetMapping("/EditBook")
	public String editBook(@RequestParam int id, Model m, HttpSession session) {

	    if(session.getAttribute("user") == null) {
	        m.addAttribute("msg", "Please Login First!");
	        return "login-signup";
	    }

	    Book book = bookservice.getBook(id);
	    m.addAttribute("book", book);

	    return "EditBook";
	}
	
	@PostMapping("/updateImage")
	public String updateImage(@RequestParam int id,@RequestParam("cImage") MultipartFile file,RedirectAttributes redirectAttributes)throws IOException {

		bookservice.updateImage(id, file);
		
		redirectAttributes.addFlashAttribute("msg", "Image Updated Successfully!");
		
		return "redirect:/EditBook?id=" + id;
	}
	
	
	@PostMapping("/updatePdf")
	public String updatePdf(@RequestParam int id,@RequestParam("ctn") MultipartFile file,RedirectAttributes redirectAttributes)throws IOException {

		bookservice.updatePdf(id, file);
		
		redirectAttributes.addFlashAttribute("msg", "Pdf Updated Successfully!");
		
		return "redirect:/EditBook?id=" + id;
	}
	
	@PostMapping("/updatePrice")
	public String updatePrice(@RequestParam int id,
	                          @RequestParam int price,
	                          RedirectAttributes redirectAttributes) {

	    bookservice.updatePrice(id, price);

	    redirectAttributes.addFlashAttribute("msg",
	            "Price Updated Successfully!");

	    return "redirect:/EditBook?id=" + id;
	}
	
	@GetMapping("/liveSearch")
	@ResponseBody
	public List<Book> liveSearch(@RequestParam String keyword) {
	    return bookservice.searchBooks(keyword);
	}
	
	@GetMapping("/bookDetails")
	public String bookDetails(@RequestParam int id, Model m) {

	    Book book = bookservice.getBookById(id);

	    m.addAttribute("book", book);

	    return "BookDetails";
	}
	
	@GetMapping("/getBookDetails")
	@ResponseBody
	public Book getBookDetails(@RequestParam int id) {
	    return bookservice.getBookById(id);
	}
	
	
	
	
	
	
	
	
}
