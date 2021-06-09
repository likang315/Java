### 责任链模式（Chain of Responsibility Pattern）：行为型模式

------

[TOC]

##### 01：概述

- 像链表一样，为请求创建了**一个接收者对象的链表**，每个接收者都包含对另一个接收者的引用，当一个接收者对象不能处理该请求，那么它会把相同的请求传给下一个接收者处理，并且沿着**链表传递请求**，直到有对象处理它为止；

###### 角色：

- 请求处理者
- 请求发送者

##### 02：示例

```java
// 所有处理结点必须要实现的接口
public interface ProcessInterence {
    String solution(String str);
}

public class ProcessFirst implements ProcessInterence {
    @Override
    public String solution(String str) {
        String result = null;
        switch (str) {
            case "1":
                result = "1";
                break;
            case "2":
                result = "2";
                break;
            default:
                System.out.println("此结点处理失败...");
        }
        return result;
    }
}

public class ProcessSecond implements ProcessInterence {
    @Override
    public String solution(String str) {
        String result = null;
        switch (str) {
            case "3":
                result = "3";
                break;
            case "4":
                result = "4";
                break;
            default:
                System.out.println("此结点处理失败...");
                break;
        }
        return result;
    }
}


public class Process {
    ArrayList<ProcessInterence>  processEventList = new ArrayList<>();

    public Process() {
        processEventList.add(new ProcessFirst());
        processEventList.add(new ProcessSecond());
    }

    public String processEvent(String str) {
        String result = null;
        for(ProcessInterence listener : processEventList) {
            result = listener.solution(str);
            if (result != null || ！result.isEmpty()) {
                break;
            }
        }
        return result;
    }
}
//请求发送者
public class Client {
    public static void main(String[] args) {
        Process pro = new Process();
        String result = pro.solution("3");
        System.out.println(result);
    }
}
```