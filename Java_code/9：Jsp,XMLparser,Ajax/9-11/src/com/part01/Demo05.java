package com.part01;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * 文件加密与解密
 * @author Administrator
 *
 */
public class Demo05 extends JFrame
{
	private JButton but=new JButton("请选择要加密或解密的文件");
	private JLabel  show=new JLabel("请选择要解析");
	private JButton act=new JButton("加密");
	private File target=null;
	JFileChooser chooser = new JFileChooser();
    public Demo05()
    {
    	super("文件加密与解密！");
    	this.setSize(400, 300);
    	this.setLocation(200,200);
    	this.setResizable(false);
    	this.setLayout(null);
    	show.setFont(new Font("微软雅黑",Font.BOLD,16));
    	show.setForeground(Color.RED);
    	but.setBounds(100, 50, 200, 40);
    	show.setBounds(30, 100, 370, 40);
    	act.setBounds(140, 180, 100, 30);
    	this.add(but);
    	this.add(show);
    	this.add(act);
    	
    	this.setVisible(true);
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	this.initListener();
    }
    //事件处理
    public void initListener()
    {
    	but.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
			    /*FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "JPG & GIF Images", "jpg", "gif");
			    chooser.setFileFilter(filter);*/
			    int returnVal = chooser.showOpenDialog(Demo05.this);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	target=chooser.getSelectedFile();
			    	show.setText("文件：" +
			            chooser.getSelectedFile().getName());
			      
			    }
				
			}
		});
       act.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			// 处理加密解密
			//得到文件扩展名
			if(null!=target&&null!=target.getName()&&target.getName().matches("((.*?)[.](.*?))"))
			{
				int lastdot=target.getName().lastIndexOf(".");
				//aaa_jpg.xxx
				String extname=target.getName().toLowerCase().substring(lastdot);
				String orgname=target.getName().substring(0,lastdot);
				File tar2=null;
				if(".xxx".endsWith(extname))
				{
					int lastunder=target.getName().lastIndexOf("_");
					tar2=new File(target.getParentFile(),orgname.substring(0,lastunder)+"."+orgname.substring(lastunder+1));
					
					act.setText("解密");
							
				}else
				{
					act.setText("加密");
					tar2=new File(target.getParentFile(),orgname+"_"+extname.substring(1)+".xxx");
				}
				if(encrypt(target,tar2))
				{
					JOptionPane.showMessageDialog(Demo05.this, "YES");
				}else
				{
					JOptionPane.showMessageDialog(Demo05.this, "No");
				}
				
			}
			
		}
	});
    }
    //加密业务
    public boolean  encrypt(File org,File tar)
    {
    	if(tar.exists())tar.delete();
    	boolean re=false;
    	try {
			FileInputStream fi=new FileInputStream(org);
			FileOutputStream fo=new FileOutputStream(tar);
			byte[] data=new byte[1024];
			int len=-1;
			while((len=fi.read(data))>0)
			{
				for(int i=0;i<len;i++)
				{
					data[i]=(byte)(data[i]^0xA0);
				}
				fo.write(data, 0, len);
			}
			
			fi.close();
			fo.close();
			re=true;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return re;
    	
    }
    
	public static void main(String[] args)
	{
	  new Demo05();

	}

}
