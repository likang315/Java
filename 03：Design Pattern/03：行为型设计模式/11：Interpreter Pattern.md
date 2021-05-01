### 解释器模式（Interpreter Pattern）：行为型模式

------

[TOC]

##### 01：概述

- 此模式实现了一个表达式接口，该接口**解释一个特定的上下文**；

##### 02：实现：

- 定义一个解释器，这个解释器用来解释语言中的句子；


```java
// 表达式接口
public interface Expression {
   // 通过true和false 来解释语句
   public boolean interpret(Person person);
}

// 性别解释器
public class TerminalExpression implements Expression {
   private String chromosomeData;
   public TerminalExpression(String chromosomeData){
      this.chromosomeData = chromosomeData; 
   }
  
  public TerminalExpression getTerminalExpression() {
    return new TerminalExpression();
  }
 
   @Override
   public boolean interpret(Person person) {
      if (OBjects.equals(person.getChromosome(), chromosomeData)) {
         return true;
      }
      return false;
   }
}

public class InterpreterPatternDemo {
 
   public static void main(String[] args) {
      Expression isMale = getTerminalExpression();
 			System.out.println("John is male? " + isMale.interpret("John"));
   }
}
```

