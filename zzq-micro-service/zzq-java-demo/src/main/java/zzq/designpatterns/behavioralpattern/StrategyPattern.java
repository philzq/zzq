package zzq.designpatterns.behavioralpattern;

/**
 * 策略（StrategyPattern）模式的定义：该模式定义了一系列算法，并将每个算法封装起来，使它们可以相互替换，
 * 且算法的变化不会影响使用算法的客户。策略模式属于对象行为模式，它通过对算法进行封装，
 * 把使用算法的责任和算法的实现分割开来，并委派给不同的对象对这些算法进行管理。
 * <p>
 * 策略模式的主要优点如下。
 * 多重条件语句不易维护，而使用策略模式可以避免使用多重条件语句。
 * 策略模式提供了一系列的可供重用的算法族，恰当使用继承可以把算法族的公共代码转移到父类里面，从而避免重复的代码。
 * 策略模式可以提供相同行为的不同实现，客户可以根据不同时间或空间要求选择不同的。
 * 策略模式提供了对开闭原则的完美支持，可以在不修改原代码的情况下，灵活增加新算法。
 * 策略模式把算法的使用放到环境类中，而算法的实现移到具体策略类中，实现了二者的分离。
 * <p>
 * 其主要缺点如下。
 * 客户端必须理解所有策略算法的区别，以便适时选择恰当的算法类。
 * 策略模式造成很多的策略类。
 */
public class StrategyPattern {

    //抽象策略类
    interface Strategy {
        void strategyMethod();    //策略方法
    }

    //具体策略类A
    class ConcreteStrategyA implements Strategy {
        public void strategyMethod() {
            System.out.println("具体策略A的策略方法被访问！");
        }
    }

    //具体策略类B
    class ConcreteStrategyB implements Strategy {
        public void strategyMethod() {
            System.out.println("具体策略B的策略方法被访问！");
        }
    }

    //环境类
    class Context {
        private Strategy strategy;

        public Strategy getStrategy() {
            return strategy;
        }

        public void setStrategy(Strategy strategy) {
            this.strategy = strategy;
        }

        public void strategyMethod() {
            strategy.strategyMethod();
        }
    }

    public static void main(String[] args) {
        Context c = new StrategyPattern().new Context();
        Strategy s = new StrategyPattern().new ConcreteStrategyA();
        c.setStrategy(s);
        c.strategyMethod();
        System.out.println("-----------------");
        s = new StrategyPattern().new ConcreteStrategyB();
        c.setStrategy(s);
        c.strategyMethod();
    }
}
