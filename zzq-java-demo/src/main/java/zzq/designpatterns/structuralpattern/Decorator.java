package zzq.designpatterns.structuralpattern;

/**
 * 装饰（Decorator）模式的定义：指在不改变现有对象结构的情况下，动态地给该对象增加一些职责（即增加其额外功能）的模式，它属于对象结构型模式。
 * <p>
 * 装饰（Decorator）模式的主要优点有：
 * 采用装饰模式扩展对象的功能比采用继承方式更加灵活。
 * 可以设计出多个不同的具体装饰类，创造出多个不同行为的组合。
 * <p>
 * 其主要缺点是：装饰模式增加了许多子类，如果过度使用会使程序变得很复杂。
 * <p>
 * 总结：不改变原有功能的重写
 */
public class Decorator {

    //抽象构件角色
    interface Component {
        void operation();
    }

    //具体构件角色
    class ConcreteComponent implements Component {
        public ConcreteComponent() {
            System.out.println("创建具体构件角色");
        }

        public void operation() {
            System.out.println("调用具体构件角色的方法operation()");
        }
    }

    //抽象装饰角色
    class DecoratorComponent implements Component {
        private Component component;

        public DecoratorComponent(Component component) {
            this.component = component;
        }

        public void operation() {
            component.operation();
        }
    }

    //具体装饰角色
    class ConcreteDecorator extends DecoratorComponent {
        public ConcreteDecorator(Component component) {
            super(component);
        }

        public void operation() {
            super.operation();
            addedFunction();
        }

        public void addedFunction() {
            System.out.println("为具体构件角色增加额外的功能addedFunction()");
        }
    }

    public static void main(String[] args) {
        Component p = new Decorator().new ConcreteComponent();
        p.operation();
        System.out.println("---------------------------------");
        Component d = new Decorator().new ConcreteDecorator(p);
        d.operation();
    }
}
