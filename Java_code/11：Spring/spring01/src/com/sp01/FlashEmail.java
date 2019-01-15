package com.sp01;

public class FlashEmail implements Email {

	@Override
	public void send() {
		System.out.println("发邮件...");
	}

	@Override
	public void receive() {
		System.out.println("收邮件...");
	}

}
