/**
 * @Author: likang
 * @mail: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/2/11 20:42
 */

/**
 * 抽象装饰器
 */
public  abstract class Decorator implements Base {
    protected Base pbase;

    public Decorator(Base t) {
        pbase=t;
    }
}
