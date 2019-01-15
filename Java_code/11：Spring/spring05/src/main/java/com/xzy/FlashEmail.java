package com.xzy;

import org.springframework.stereotype.Component;

@Component
public class FlashEmail implements Email {

	@Override
	public void send() {
		// TODO Auto-generated method stub
		System.out.println("发邮件。。。。");
	}

	@Override
	public void receive() {
		// TODO Auto-generated method stub
		System.out.println("收邮件。。。。");
	}

}
