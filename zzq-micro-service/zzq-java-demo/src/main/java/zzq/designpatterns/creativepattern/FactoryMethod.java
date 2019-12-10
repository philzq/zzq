package zzq.designpatterns.creativepattern;

/**
 * 工厂方法（FactoryMethod）模式的定义：定义一个创建产品对象的工厂接口，将产品对象的实际创建工作推迟到具体子工厂类当中。
 * 这满足创建型模式中所要求的“创建与使用相分离”的特点。
 *
 * 我们把被创建的对象称为“产品”，把创建产品的对象称为“工厂”。如果要创建的产品不多，只要一个工厂类就可以完成，
 * 这种模式叫“简单工厂模式”，它不属于 GoF 的 23 种经典设计模式，它的缺点是增加新产品时会违背“开闭原则”。
 *
 * 本节介绍的“工厂方法模式”是对简单工厂模式的进一步抽象化，其好处是可以使系统在不修改原来代码的情况下引进新的产品，
 * 即满足开闭原则。
 *
 * 工厂方法模式的主要优点有：
 * 1.用户只需要知道具体工厂的名称就可得到所要的产品，无须知道产品的具体创建过程；
 * 2.在系统增加新的产品时只需要添加具体产品类和对应的具体工厂类，无须对原工厂进行任何修改，满足开闭原则；
 *
 * 其缺点是：每增加一个产品就要增加一个具体产品类和一个对应的具体工厂类，这增加了系统的复杂度。
 *
 * 总结：该模式可简单的理解为多态，对于一件事的多种不同实现
 */
public class FactoryMethod {

    public interface Factory{
        Product createProduct();
    }

    public class AppFactory implements Factory{
        @Override
        public Product createProduct() {
            System.out.println("生产产品App");
            return new AppProduct();
        }
    }

    public interface Product{
        void show();
    }

    public class AppProduct implements Product{
        @Override
        public void show() {
            System.out.println("展示了产品:App");
        }
    }
    
    public static void main(String[] args){
        Factory factory = new FactoryMethod().new AppFactory();
        factory.createProduct();//输出：生产产品App
        Product product = new FactoryMethod().new AppProduct();
        product.show();//输出：展示了产品:App
    }
}
