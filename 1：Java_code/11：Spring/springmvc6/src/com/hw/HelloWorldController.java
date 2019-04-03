package com.hw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * HttpSession 和HttpServletResponse
 * @author likang
 *
 */
@Controller
@RequestMapping("/admin")
public class HelloWorldController 
{

	@RequestMapping(value = "/hi")
	public String handle23(HttpSession session) 
	{
		session.setAttribute("sessionId", 1234);
		return "hello";
	}
	
	@RequestMapping("/img.htm")
	public void imgs(OutputStream os,HttpServletResponse resp)
	{
		resp.setContentType("image/jpeg");
		BufferedImage img=new BufferedImage(100, 30, BufferedImage.TYPE_INT_RGB);
		Graphics g=img.getGraphics();
		g.setColor(new Color(220,220,220));
		g.fillRect(0, 0, 100, 30);
		g.setColor(Color.RED);
		g.drawString("Hello", 40, 15);
		
		try {
			ImageIO.write(img, "jpeg", os);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

 
}