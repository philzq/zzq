package zzq.designpatterns.structuralpattern;

/**
 * 适配器模式（Adapter）的定义如下：将一个类的接口转换成客户希望的另外一个接口，使得原本由于接口不兼容而不能一起工作的那些类能一起工作。适配器模式分为类结构型模式和对象结构型模式两种，前者类之间的耦合度比后者高，且要求程序员了解现有组件库中的相关组件的内部结构，所以应用相对较少些。
 * <p>
 * 该模式的主要优点如下。
 * 客户端通过适配器可以透明地调用目标接口。
 * 复用了现存的类，程序员不需要修改原有代码而重用现有的适配者类。
 * 将目标类和适配者类解耦，解决了目标类和适配者类接口不一致的问题。
 * <p>
 * 其缺点是：对类适配器来说，更换适配器的实现过程比较复杂。
 * <p>
 * 总结：类适配器模式就不演示了，类似多态，可扩展性差
 * 对象适配器模式：该模式类似于Thread的实现
 * new Thread(new Runnable() {
 *
 * @Override public void run() {
 * <p>
 * }
 * }).start();
 * 根据传入的对象执行指定对象的start方法
 */
public class Adapter {

    interface Target {
        void request();
    }

    //适配者接口
    class Adaptee {
        public void specificRequest() {
            System.out.println("适配者中的业务代码被调用！");
        }
    }

    //对象适配器类
    class ObjectAdapter implements Target {
        private Adaptee adaptee;

        public ObjectAdapter(Adaptee adaptee) {
            this.adaptee = adaptee;
        }

        public void request() {
            adaptee.specificRequest();
        }
    }

    public static void main(String[] args){
        System.out.println("类适配器模式测试：");
        Target target = new Adapter().new ObjectAdapter(new Adapter().new Adaptee());
        target.request();
    }

}
