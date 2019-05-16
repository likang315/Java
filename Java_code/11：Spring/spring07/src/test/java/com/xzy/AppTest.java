package com.xzy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.xzy.dao.Email;
import com.xzy.pojo.Tear;
import com.xzy.service.AdminService;
import com.xzy.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value={"/ApplicationContext.xml" })
public class AppTest  
{
	
	@Autowired
	
	UserService us;
	@Autowired
	AdminService  as;
	
	@Autowired
	Tear tt;
	@Autowired
	@Qualifier("gmail")
	Email email;
	
   @Test
    public void testApp()
    {
        us.check();
        
        as.addArticle();
        
        
       tt.display();
       
       System.out.println("----------------------");
       email.send();
    }
}
