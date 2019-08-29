package zzq.designpatterns.behavioralpattern;

/**
 * 状态（StatePatternClient）模式的定义：对有状态的对象，把复杂的“判断逻辑”提取到不同的状态对象中，允许状态对象在其内部状态发生改变时改变其行为。
 * <p>
 * 状态模式是一种对象行为型模式，其主要优点如下。
 * 状态模式将与特定状态相关的行为局部化到一个状态中，并且将不同状态的行为分割开来，满足“单一职责原则”。
 * 减少对象间的相互依赖。将不同的状态引入独立的对象中会使得状态转换变得更加明确，且减少对象间的相互依赖。
 * 有利于程序的扩展。通过定义新的子类很容易地增加新的状态和转换。
 * <p>
 * 状态模式的主要缺点如下。
 * 状态模式的使用必然会增加系统的类与对象的个数。
 * 状态模式的结构与实现都较为复杂，如果使用不当会导致程序结构和代码的混乱。
 */
public class StatePatternClient {

    //环境类
    class Context {
        private State state;

        //定义环境类的初始状态
        public Context() {
            this.state = new ConcreteStateA();
        }

        //设置新状态
        public void setState(State state) {
            this.state = state;
        }

        //读取状态
        public State getState() {
            return (state);
        }

        //对请求做处理
        public void Handle() {
            state.Handle(this);
        }
    }

    //抽象状态类
    abstract class State {
        public abstract void Handle(Context context);
    }

    //具体状态A类
    class ConcreteStateA extends State {
        public void Handle(Context context) {
            System.out.println("当前状态是 A.");
            context.setState(new ConcreteStateB());
        }
    }

    //具体状态B类
    class ConcreteStateB extends State {
        public void Handle(Context context) {
            System.out.println("当前状态是 B.");
            context.setState(new ConcreteStateA());
        }
    }

    public static void main(String[] args) {
        Context context = new StatePatternClient().new Context();    //创建环境
        context.Handle();    //处理请求
        context.Handle();
        context.Handle();
        context.Handle();
    }
}
