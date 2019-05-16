package com.filter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class Myresponse extends HttpServletResponseWrapper {

	public Myresponse(HttpServletResponse response) {
		super(response);
	}
	

}
