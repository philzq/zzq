package zzq.designpatterns.structuralpattern;

import java.util.HashMap;

/**
 * 享元（Flyweight）模式的定义：运用共享技术来有効地支持大量细粒度对象的复用。它通过共享已经存在的又橡来大幅度减少需要创建的对象数量、避免大量相似类的开销，从而提高系统资源的利用率。
 * <p>
 * 享元模式的主要优点是：相同对象只要保存一份，这降低了系统中对象的数量，从而降低了系统中细粒度对象给内存带来的压力。
 * <p>
 * 其主要缺点是：
 * 为了使对象可以共享，需要将一些不能共享的状态外部化，这将增加程序的复杂性。
 * 读取享元模式的外部状态会使得运行时间稍微变长。
 * <p>
 * 总结：该模式就是对象共享使用，如果存在对象就使用已经存在的对象，没有就创建
 */
public class Flyweight {

    //非享元角色
    class UnsharedConcreteFlyweight {
        private String info;

        UnsharedConcreteFlyweight(String info) {
            this.info = info;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }

    //抽象享元角色
    interface FlyweightTest {
        void operation(UnsharedConcreteFlyweight state);
    }

    //具体享元角色
    class ConcreteFlyweight implements FlyweightTest {
        private String key;

        ConcreteFlyweight(String key) {
            this.key = key;
            System.out.println("具体享元" + key + "被创建！");
        }

        public void operation(UnsharedConcreteFlyweight outState) {
            System.out.print("具体享元" + key + "被调用，");
            System.out.println("非享元信息是:" + outState.getInfo());
        }
    }

    //享元工厂角色
    class FlyweightFactory {
        private HashMap<String, FlyweightTest> flyweights = new HashMap<String, FlyweightTest>();

        public FlyweightTest getFlyweight(String key) {
            FlyweightTest flyweight = (FlyweightTest) flyweights.get(key);
            if (flyweight != null) {
                System.out.println("具体享元" + key + "已经存在，被成功获取！");
            } else {
                flyweight = new ConcreteFlyweight(key);
                flyweights.put(key, flyweight);
            }
            return flyweight;
        }
    }

    public static void main(String[] args) {
        FlyweightFactory factory = new Flyweight().new FlyweightFactory();
        FlyweightTest f01 = factory.getFlyweight("a");
        FlyweightTest f02 = factory.getFlyweight("a");
        FlyweightTest f03 = factory.getFlyweight("a");
        FlyweightTest f11 = factory.getFlyweight("b");
        FlyweightTest f12 = factory.getFlyweight("b");
        f01.operation(new Flyweight().new UnsharedConcreteFlyweight("第1次调用a。"));
        f02.operation(new Flyweight().new UnsharedConcreteFlyweight("第2次调用a。"));
        f03.operation(new Flyweight().new UnsharedConcreteFlyweight("第3次调用a。"));
        f11.operation(new Flyweight().new UnsharedConcreteFlyweight("第1次调用b。"));
        f12.operation(new Flyweight().new UnsharedConcreteFlyweight("第2次调用b。"));
    }

}
