package com.part1;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;
//截图，并且保存到指定位置
public class Demo03 {

	public static void main(String[] args)throws Exception
	{
		 Robot robot=new Robot();//此类用来获取screen的截图
		 BufferedImage img=robot.createScreenCapture(
				 new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));//获取屏幕尺寸，并且截图，读入到内存中
         
		 ByteArrayOutputStream bao=new ByteArrayOutputStream();
		 ImageIO.write(img, "jpg", bao);
		 byte [] data=bao.toByteArray();
		 
		 //将字节转为图片
		 
		 ByteArrayInputStream bio=new ByteArrayInputStream(data);
		 
		 BufferedImage rimg=ImageIO.read(bio);
		 
		 
		 FileOutputStream fo=new FileOutputStream("F:\\aaa.jpg");
		 fo.write(data);
		 fo.close();
		 System.out.println("Ok");
		 
	}

}
