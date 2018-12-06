package com.part1;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
//用来给图像文件绘制字体
public class Demo04 {

	public static void main(String[] args)throws Exception {

        File dir=new File("F:\\图像");
        File[] all=dir.listFiles();
        
        for(File f:all)
        {
        	//此类可以用来访问图像缓存区的图像
        	BufferedImage img=ImageIO.read(f);
        	int width=img.getWidth();
        	int height=img.getHeight();
        	//绘图类
        	Graphics g=img.getGraphics();
        	g.setColor(Color.red);
        	g.setFont(new Font("微软雅黑", Font.BOLD, 18));
        	g.drawString("我最帅!", width-100, height-100);
        	
        	ImageIO.write(img, "jpg", new File(dir,"a"+f.getName()));
        }
        System.out.println("OK");
        
	}

}
