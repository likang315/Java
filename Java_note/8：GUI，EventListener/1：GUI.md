### GUI：图形化界面

- Java.awt ：抽象窗口工具包，提供创建窗口和绘制图形图像的所有类
- Javax.swing：在awt包的基础之上进行了优化，提供了可跨平台并且比较复杂的组件（常用）
- java.awt.event：事件处理，后台功能的实现

### 1：三种布局

1. 流式布局：就像在 word 中打字，组件从左向右排列，一行排满后自动换下一行。组件默认居中对齐，可以设置为左/右对齐，流布局会维持组件的原始大小。**流布局是 JPanel（面板）的默认布局，FlowLayout类提供该布局**
2. 方位布局：把整个窗口分成了5个部分，东西南北中。其中南北是整行，而中/西/东都不是整列，组件放入时需要指定放在哪个区域，默认放在中部。每个部分只能存放一个组件，如果存放多个就会覆盖前面的，**JFrame的默认布局就是边框布局，BorderLayout 提供该类的实例**
3. 网格布局：把窗口分成几行几列的表格方式，**构造时需要指定行数和列数**，组件在网状布局中**不保持原始大小**，会充满整个区域。**组件在网状布局中，一个格子只放一个组件，自动向后走，GridLayout 提供该类的实例**

```java
public static void solution( ) {
  //新建窗口
  JFrame jframe = new JFrame("JFrame demo 标题...");
  //新建窗口
  JButton jb = new JButton("中间");
  //默认添加中间
  jframe.add(jb);
  JButton jb1 = new JButton("东部");
  //把按钮添加在东部
  jframe.add(jb1, BorderLayout.EAST);
  //设置窗口大小
  jframe.setSize(600, 300);
  //设置窗口里屏幕位置
  jframe.setLocation(300,300);
  //设置关闭按钮起作用
  jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  //设置窗口可见，必须设置
  jframe.setVisible(true);
}
```



### 2：Javax.swing ：         

- JFrame ：带有标题和边框的顶层窗口     	
- JPanel ：一般轻量级容器，面板
- JButton ：按钮
- JRadioButton：单选按钮
- JCheckBox：多选按钮
- JTextField ： 文本框
- JTextArea ：  文本区域

### 3：JPanel ：面板，容纳组件的容器

- void setBounds (int x, int y, int width, int height) ：设置方位和大小
- void setBackground(Color bg)：设置背景色
- void setLayout(LayoutManager mgr) :设置布局凡方式
- Component add(Component comp)：添加组件

### 4：JButton：标签按钮，component的子类  

- Button(String label) ：构造一个带指定名称的按钮
- void addActionListener(ActionListener l)   ：添加动作侦听器，点击是触发



### 5：component：组件类，是所有 awt 组件的顶级父类，主要添加事件监听器方法         

-  void addFocusListener(FocusListener l)：添加指定的焦点侦听器，以便当此组件获得输入焦点时能够接收发自此组件的焦点事件 	
- void addKeyListener(KeyListener l)   ：添加指定的按键侦听器，以接收发自此组件的按键事件
- void addMouseListener(MouseListener l)  :添加指定的鼠标侦听器，以接收发自此组件的鼠标点击，双击事件
- void addMouseMotionListener(MouseMotionListener l) :添加指定的鼠标移动侦听器，以接收发自此组件的鼠标移动事件
- void addMouseWheelListener(MouseWheelListener l) :添加指定的鼠标滚轮侦听器，以接收发自此组件的鼠标滚轮事件

```java
//实现其接口，重写其方法
public interface FocusListener extends EventListener {
		//获得焦点
    public void focusGained(FocusEvent e);
    //失去焦点
    public void focusLost(FocusEvent e);
}
```



### 6：Java.awt   Abstract  Class  Graphics

​	提供在组件上打印文字，显示图像的方法，图形上下文就是窗口空间    	

- abstract  void drawLine(int x1, int y1, int x2, int y2)  :在此图形上下文的坐标系中，使用当前颜色在点 (x1, y1) 和 (x2, y2) 之间画一条线     	
- abstract  boolean drawImage(Image img, int x, int y, int width,int height ，null) :绘制指定图像中当前可用的图像
- abstract  void drawString(String str, int x, int y) :使用此图形上下文的当前字体和颜色绘制由指定 string 给定的文本     	
- abstract  void setColor(Color c)：将此图形上下文的当前颜色设置为指定颜色
- abstract  void setFont(Font font)：将此图形上下文的字体设置为指定字体。               

```java
//实例化此类时自动调用paint()
public class MyJpanel2 extends JPanel {
    @Override
    public void paint(Graphics g) {   
      	//保存笔的颜色
        Color c=g.getColor();
        //保存笔的颜色
        g.drawRect(100, 100, 200, 200);
        g.setColor(Color.blue);
        //保存笔的颜色
        g.fillRect(100, 100, 200, 200);
        g.setColor(c);
        
        g.drawRect(100, 100, 200, 100);
        g.setColor(Color.RED);
        g.fillRect(100, 100, 200, 100);
        g.setColor(c);   
    }  
}
```



### 7：其他工具类

1. Color：颜色类    
2. Font：字体类              	
3. Image：图形图像的超类
4. Toolkit.createImage (URL url)：从指定路径出获得图像



