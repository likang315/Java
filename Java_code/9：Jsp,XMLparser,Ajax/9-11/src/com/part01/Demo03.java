package com.part01;

import javax.swing.JFrame;

/**
 * 文件加密与解密
 * @author Administrator
 *
 */
public class Demo03 extends JFrame
{
    public Demo03()
    {
    	super("文件加密与解密！");
    	this.setSize(400, 300);
    	this.setLocation(200,200);
    	
    	this.setVisible(true);
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
	public static void main(String[] args)
	{
	  new Demo03();

	}

}
