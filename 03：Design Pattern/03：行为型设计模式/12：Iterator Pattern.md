### 迭代器模式（Iterator Pattern）：行为型模式

------

[TOC]

##### 01：概述

-  这种模式用于**顺序访问集合对象的元**素，不需要知道集合对象的底层表示；

###### 意图：

​	提供一种方法顺序访问一个聚合对象中各个元素, 而又无须暴露该对象的内部表示

###### 缺点：

​	由于迭代器模式**将存储数据和遍历数据的职责分离**，增加新的目标类需要对应增加新的迭代器类，类的个数成对增加，这在一定程度上**增加了系统的复杂性**

#####  02：示例

​	JAVA 中的 iterator

```java
public interface Iterator {
    boolean hasNext();
    Object next();
}
// 迭代器类
public class NameRepository {
    public String names[] = {"Robert" , "John" ,"Julie" , "Lora"};
    @Override
    public Iterator getIterator() {
        return new NameIterator();
    }
    // 迭代器内部类
    private class NameIterator implements Iterator {
        int index;
        @Override
        public boolean hasNext() {
            if(index < names.length){
                return true;
            }
            return false;
        }

        @Override
        public Object next() {
            if(this.hasNext()){
                return names[index++];
            }
            return null;
        }
    }
}

public class IteratorPatternDemo {
    public static void main(String[] args) {
        NameRepository namesRepository = new NameRepository();
        for(Iterator iter = namesRepository.getIterator(); iter.hasNext();){
            String name = (String)iter.next();
            System.out.println("Name : " + name);
        }  
    }
} 
```

