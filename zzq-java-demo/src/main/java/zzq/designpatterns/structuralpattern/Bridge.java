package zzq.designpatterns.structuralpattern;

/**
 * 桥接（Bridge）模式的定义如下：将抽象与实现分离，使它们可以独立变化。它是用组合关系代替继承关系来实现，从而降低了抽象和实现这两个可变维度的耦合度。
 * <p>
 * 桥接（Bridge）模式的优点是：
 * 由于抽象与实现分离，所以扩展能力强；
 * 其实现细节对客户透明。
 * <p>
 * 缺点是：由于聚合关系建立在抽象层，要求开发者针对抽象化进行设计与编程，这增加了系统的理解与设计难度。
 * <p>
 * 总结:高内聚低耦合设计很重要
 */
public class Bridge {

    //实现化角色
    interface Implementor {
        public void OperationImpl();
    }

    //具体实现化角色
    class ConcreteImplementorA implements Implementor {
        public void OperationImpl() {
            System.out.println("具体实现化(Concrete Implementor)角色被访问");
        }
    }

    //抽象化角色
    abstract class Abstraction {
        protected Implementor imple;

        protected Abstraction(Implementor imple) {
            this.imple = imple;
        }

        public abstract void Operation();
    }

    //扩展抽象化角色
    class RefinedAbstraction extends Abstraction {
        protected RefinedAbstraction(Implementor imple) {
            super(imple);
        }

        public void Operation() {
            System.out.println("扩展抽象化(Refined Abstraction)角色被访问");
            imple.OperationImpl();
        }
    }

    public static void main(String[] args) {
        Implementor imple = new Bridge().new ConcreteImplementorA();
        Abstraction abs = new Bridge().new RefinedAbstraction(imple);
        abs.Operation();
    }
}
