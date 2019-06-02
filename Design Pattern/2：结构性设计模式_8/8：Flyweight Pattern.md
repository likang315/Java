### 享元模式（Flyweight Pattern）：属于结构型模式

​	用于减少创建对象的数量，以减少内存占用和提高性能，重用现有的同类对象，如果未找到匹配的对象，则创建新对象

###### 实现

- 用唯一标识码判断，如果在内存中有，则返回这个唯一标识码所标识的对象
- 用 HashMap 存储这些对象

###### 示例：

- JAVA 中的 String，如果有则返回，如果没有则创建一个字符串保存在字符串缓存池里面
- 数据库连接池

```java
public class Circle {
   private String color;
   private int x;
   private int y;
 
   public Circle(String color){
      this.color = color;     
   }
 
   public void setX(int x) {
      this.x = x;
   }
 
   public void setY(int y) {
      this.y = y;
   }
  
   @Override
   public void draw() {
      System.out.println("Circle: Draw() [Color : " + color 
         +", x : " + x +", y :" + y +");
   }
}
                         
public class ShapeFactory {
   //通过颜色来作为标识码
   private static final HashMap<String, Shape> circleMap = new HashMap<>();
 
   public static Shape getCircle(String color) {
      Circle circle = (Circle)circleMap.get(color);
 
      if(circle == null) {
         circle = new Circle(color);
         circleMap.put(color, circle);
         System.out.println("Creating circle of color : " + color);
      }
      return circle;
   }
}
                    
public class FlyweightPatternDemo {
  private static final String colors[] = 
  { "Red", "Green", "Blue", "White", "Black" };

  private static String getRandomColor() {
    return colors[(int)(Math.random()*colors.length)];
  }
  private static int getRandomX() {
    return (int)(Math.random()*100 );
  }
  private static int getRandomY() {
    return (int)(Math.random()*100);
  }

  public static void main(String[] args) {
 
      for(int i=0; i < 20; ++i) {
         Circle circle = 
            (Circle)ShapeFactory.getCircle(getRandomColor());
         circle.setX(getRandomX());
         circle.setY(getRandomY());
         circle.draw();
      }
   }
}
```

