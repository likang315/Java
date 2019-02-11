/**
 * @Author: likang
 * @mail: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/2/11 20:50
 */
public class Main {
    public static void main(String[] args)
    {
        ConcreteDecorator dec = new ConcreteDecorator(new Product());
        dec.method();
    }
}
