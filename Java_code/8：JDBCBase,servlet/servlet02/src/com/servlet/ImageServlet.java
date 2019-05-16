package com.servlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;


@WebServlet(name="aa",value="/img/welcome",initParams= {
		@WebInitParam(name="user",value="root"),
		@WebInitParam(name="aa",value="tt")
},loadOnStartup=1)
public class ImageServlet extends GenericServlet {

	@Override
	public void service(ServletRequest req, ServletResponse resp) throws ServletException, IOException {
	
		resp.setContentType("image/jpg");
		
		OutputStream os=resp.getOutputStream();

		BufferedImage img=new BufferedImage(200, 100, BufferedImage.TYPE_INT_BGR);
		
		Graphics g=img.getGraphics();
		g.setColor(new Color(220,220,220));
		g.fillRect(0, 0, 200, 100);
		
		g.setColor(Color.BLUE);
		g.drawLine(0, 0, 200, 100);
		
		g.setColor(Color.RED);
		g.setFont(new Font("宋体",Font.BOLD,20));
		g.drawString("新卓越"+this.getInitParameter("user"), 50,50);
		
	
		ImageIO.write(img, "jpg", os);
		
	}

}
