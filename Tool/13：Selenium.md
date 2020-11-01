### Selenium

------

- 它是用于Web浏览器自动化的工具集，它使用可用的最佳技术来**远程控制浏览器实例并模拟用户与浏览器的交互**。
- Link：https://www.selenium.dev/documentation/en/grid/

##### 01：安装：

- 需要导入Jar包

  - ```xml
    <dependency>
     <groupId>org.seleniumhq.selenium</groupId>
     <artifactId>selenium-java</artifactId>
     <version>${selenium-java}</version>
    </dependency>
    ```

- 需要添加脚本到系统路径下；

  - 作用：chromedriver被实现为WebDriver远程服务器，该服务器通过公开Chrome的内部自动化代理界面来指示浏览器该怎么做；

- 启动报错，检查驱动和浏览器版本是否一致，然后在检查是否导入guaua jar包；

##### 02：示例

```java
/**
 * @author kangkang.li@qunar.com
 * @date 2020-10-28 10:41
 */
public class Main {
 
    static {
        System.setProperty("webdriver.chrome.driver",
                           "/Users/likang/Code/Java/Tool/chromedriver");
    }
 
    public static void main(String[] args) throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        driver.get("https://ebooking.ctrip.com/ebkovsassembly/Login");
        List<WebElement> elements = driver.findElements(By.className("login-input-box"));
        elements.get(0).findElement(By.tagName("input")).sendKeys("pmtest");
        elements.get(1).findElement(By.tagName("input")).sendKeys("ceshi007");
        // 查找登录按钮
        driver.findElement(By.className("remember-box"))
          .findElement(By.tagName("button")).click();
        Thread.sleep( 500);
 
        WebElement roomStatusElement = driver.findElement(By.linkText("房价房态"));
        Actions action = new Actions(driver);
        action.click(roomStatusElement).perform();
        WebElement roomPriceElement = driver.findElement(By.linkText("批量修改 - 房量"));
        action.click(roomPriceElement).perform();
        // 解析HTML页面
 
 
        Thread.sleep(500000);
        driver.close();
    }
}
```

