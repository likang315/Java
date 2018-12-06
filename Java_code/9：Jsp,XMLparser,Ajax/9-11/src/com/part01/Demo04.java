package com.part01;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * 文件加密与解密
 * @author Administrator
 *
 */
public class Demo04 extends JFrame
{
	private JButton but=new JButton("请选择要加密或解密的文件");
	private JLabel  show=new JLabel("请选择要解析");
	private JButton act=new JButton("加密");
    public Demo04()
    {
    	super("文件加密与解密！");
    	this.setSize(400, 300);
    	this.setLocation(200,200);
    	this.setResizable(false);
    	this.setLayout(null);
    	show.setFont(new Font("微软雅黑",Font.BOLD,16));
    	show.setForeground(Color.RED);
    	but.setBounds(100, 50, 200, 40);
    	show.setBounds(30, 100, 200, 40);
    	act.setBounds(140, 180, 100, 30);
    	this.add(but);
    	this.add(show);
    	this.add(act);
    	
    	this.setVisible(true);
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
	public static void main(String[] args)
	{
	  new Demo04();

	}

}
