package com.anurag.component;

import org.springframework.stereotype.Component;

import lombok.Data;


@Data
@Component
public class Book {
	private int id;
	private String name;
	private int price;
	private byte[] coverImage;
	private byte[] content;
	private User user;
}