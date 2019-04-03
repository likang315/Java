package com.servlet;

import com.service.UserService;
import com.service.core.ServiceFactory;

public class ServletBase 
{
   protected UserService us=(UserService)ServiceFactory.getService(UserService.class);
}
