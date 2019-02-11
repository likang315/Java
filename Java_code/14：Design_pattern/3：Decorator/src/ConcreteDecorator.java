/**
 * @Author: likang
 * @mail: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/2/11 20:48
 */
public class ConcreteDecorator extends Decorator {

    public ConcreteDecorator(Base t)
    {
        super(t);
    }

    @Override
    public void method()
    {
        pbase.method();
        newMethod();
    }

    public void newMethod()
    {
        System.out.println("装饰的功能...");
    }
}
