package com.img;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 产生验证码
 * @author likang
 *
 */
@WebServlet("/abc.jpg")
public class ImageServlet extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		//返回的MIME类型为图片
		resp.setContentType("image/jpeg");
		ServletOutputStream   out=resp.getOutputStream();
		
		//缓冲区的图像
		BufferedImage img=new BufferedImage(120, 35, BufferedImage.TYPE_INT_BGR);
		Graphics g=img.getGraphics();
		g.setColor(new Color(220,220,220));
		g.fillRect(0, 0, 120, 35);//填充背景
		
		//随机画线十条线
		for(int i=0;i<10;i++)
		{
			Color color=new Color(100+rand.nextInt(50)+80,rand.nextInt(80)+100,rand.nextInt(80)+100);
			g.setColor(color);
			g.drawLine(rand.nextInt(120), rand.nextInt(35),rand.nextInt(120), rand.nextInt(35));
		}
		
		//画三个字
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<3;i++)
		{
			String str=randChinese();
			sb.append(str);
			
			Font font=new Font("雅黑",Font.BOLD,18);
			Color color=new Color(100+rand.nextInt(50),rand.nextInt(50),rand.nextInt(50));
			g.setColor(color);
			g.setFont(font);
			
			g.drawString(str, i*40+10, 20+rand.nextInt(5));
		}
		
		//把产生的汉字保存到randcode属性
		req.getSession().setAttribute("randcode", sb.toString());
		ImageIO.write(img, "png", out);
	}

	 
	static int index=0;
	static Random rand =new Random();
    public static int randInt()
    {
    	return rand.nextInt(10);
    }
    
    public static char randChar()
    {
    	char re=0;
    	//奇小写，偶大写
    	if(index++%2==0)
    		re=(char)(rand.nextInt(26)+65);
    	else
    		re=(char)(rand.nextInt(26)+97);
    	
    	return re;
    }
    
    public static String randChinese()
    {
    	String re=null;
    	byte [] tem=new byte[2];
    	tem[0]=(byte)(16+rand.nextInt(35)+0xA0);
    	tem[1]=(byte)(30+rand.nextInt(10)+0xA0);
       	try {
			re=new String(tem,"GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	return re;
    }
    
}
