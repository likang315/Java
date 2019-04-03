package com.xzy.dao.imp;

import org.springframework.stereotype.Component;

import com.xzy.dao.Email;
@Component("flash")
public class FlashEmail implements Email {

	public void send() {
		// TODO Auto-generated method stub
		System.out.println("FlashEmail..........");
	}

}
