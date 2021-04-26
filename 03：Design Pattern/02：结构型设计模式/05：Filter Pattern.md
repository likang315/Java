### 过滤器模式（FiltersPattern）：属于结构型模式

------

[TOC]

##### 01：概述

- 使用不同的过滤条件来过滤一组对象，通过逻辑运算把它们连接起来，获得最终的过滤条件，它结合多个标准来获得单一标准；

##### 01：示例

```java
// 所有过滤器的基类
public interface FilterBase {
   public List<Person> filter(List<Person> persons);
}
// 过滤出男性
public class FilterMale implements FilterBase {
   @Override
   public List<Person> filter(List<Person> persons) {
      List<Person> malePersons = new ArrayList<Person>(); 
      for (Person person : persons) {
         if(person.getGender().equals("MALE")){
            malePersons.add(person);
         }
      }
      return malePersons;
   }
}
// 过滤出所有单身的
public class FilterSingle implements FilterBase {
  
   @Override
   public List<Person> meetCriteria(List<Person> persons) {
      List<Person> singlePersons = new ArrayList<Person>(); 
      for (Person person : persons) {
         if(person.getMaritalStatus().equals("SINGLE")){
            singlePersons.add(person);
         }
      }
      return singlePersons;
   }
}
public class Client {
   public static void main(String[] args) {
      List<Person> persons = new ArrayList<Person>();
      persons.add(new Person("Robert","Male", "Single"));
      persons.add(new Person("John","Male", "Married"));
      persons.add(new Person("Laura","Female", "Married"));
      persons.add(new Person("Diana","Female", "Single"));
      persons.add(new Person("Mike","Male", "Single"));
      persons.add(new Person("Bobby","Male", "Single"));
 
      Criteria male = new FilterMale();
      Criteria single = new FilterSingle();
    	
      List<Person> male = male.filter(persons);
      printPersons(single.filter(male));
   }
 
   public static void printPersons(List<Person> persons){
      for (Person person : persons) {
         System.out.println("Person : [ Name : " + person.getName() 
            +", Gender : " + person.getGender() 
            +", Marital Status : " + person.getMaritalStatus()
            +" ]");
      }
   }      
}
```

