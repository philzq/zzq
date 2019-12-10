package zzq.designpatterns.creativepattern;

/**
 * 建造者（Builder）模式的定义：指将一个复杂对象的构造与它的表示分离，使同样的构建过程可以创建不同的表示，
 * 这样的设计模式被称为建造者模式。它是将一个复杂的对象分解为多个简单的对象，然后一步一步构建而成。
 * 它将变与不变相分离，即产品的组成部分是不变的，但每一部分是可以灵活选择的。
 * <p>
 * 该模式的主要优点如下：
 * 各个具体的建造者相互独立，有利于系统的扩展。
 * 客户端不必知道产品内部组成的细节，便于控制细节风险。
 * <p>
 * 其缺点如下：
 * 产品的组成部分必须相同，这限制了其使用范围。
 * 如果产品的内部变化复杂，该模式会增加很多的建造者类。
 * <p>
 * 建造者（Builder）模式和工厂模式的关注点不同：建造者模式注重零部件的组装过程，而工厂方法模式更注重零部件的创建过程，
 * 但两者可以结合使用。
 * <p>
 * <p>
 * 总结：该模式从架构层面看可视为微服务，服务与服务直接高内聚低耦合，各干各的互不影响，多个微服务组成一个产品
 * 往代码结构层面上可以视为一个大对象内有很多小对象引用，往业务层面上看，
 * 可以理解为service单一职责原则设计，多个业务功能点组成一个大的业务体系
 */
public class Builder {

    /**
     * 产品角色：包含多个组成部件的复杂对象
     */
    class Product {
        private String partA;
        private String partB;
        private String partC;

        public void setPartA(String partA) {
            this.partA = partA;
        }

        public void setPartB(String partB) {
            this.partB = partB;
        }

        public void setPartC(String partC) {
            this.partC = partC;
        }

        public void show() {
            //显示产品的特性
        }
    }

    /**
     * 抽象建造者：包含创建产品各个子部件的抽象方法。
     */
    abstract class BuilderTest {
        //创建产品对象
        protected Product product = new Product();

        public abstract void buildPartA();

        public abstract void buildPartB();

        public abstract void buildPartC();

        //返回产品对象
        public Product getResult() {
            return product;
        }
    }

    /**
     * 具体建造者：实现了抽象建造者接口。
     */
    public class ConcreteBuilder extends BuilderTest {
        public void buildPartA() {
            product.setPartA("建造 PartA");
        }

        public void buildPartB() {
            product.setPartA("建造 PartB");
        }

        public void buildPartC() {
            product.setPartA("建造 PartC");
        }
    }

    /**
     * 指挥者：调用建造者中的方法完成复杂对象的创建。
     */
    class Director {
        private BuilderTest builder;

        public Director(BuilderTest builder) {
            this.builder = builder;
        }

        //产品构建与组装方法
        public Product construct() {
            builder.buildPartA();
            builder.buildPartB();
            builder.buildPartC();
            return builder.getResult();
        }
    }

    public static void main(String[] args) {
        BuilderTest builderTest = new Builder().new ConcreteBuilder();
        Director director = new Builder().new Director(builderTest);
        Product product = director.construct();
        product.show();
    }
}
