package com.sp01;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CellPhone {

  @Autowired
  Email email;
  private String name="PhoneXX";
  
  public void run()
  {
	  System.out.println("---->"+name+"<----");
	  email.send();
	  email.receive();
  }


  
  
  
}
