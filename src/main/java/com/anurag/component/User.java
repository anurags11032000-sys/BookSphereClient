package com.anurag.component;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class User {
	private String email;
	private String password;
	private String name;
	private String phone;
}
