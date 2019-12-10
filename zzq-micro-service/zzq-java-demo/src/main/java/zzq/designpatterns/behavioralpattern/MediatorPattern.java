package zzq.designpatterns.behavioralpattern;

import java.util.ArrayList;
import java.util.List;

/**
 * 中介者（MediatorPattern）模式的定义：定义一个中介对象来封装一系列对象之间的交互，使原有对象之间的耦合松散，
 * 且可以独立地改变它们之间的交互。中介者模式又叫调停模式，它是迪米特法则的典型应用。
 * <p>
 * 中介者模式是一种对象行为型模式，其主要优点如下。
 * 降低了对象之间的耦合性，使得对象易于独立地被复用。
 * 将对象间的一对多关联转变为一对一的关联，提高系统的灵活性，使得系统易于维护和扩展。
 * <p>
 * 其主要缺点是：当同事类太多时，中介者的职责将很大，它会变得复杂而庞大，以至于系统难以维护。
 *
 * 总结：该模式主要是用于降低对象直接的耦合，让其单一处理一件事情，复杂的事情让中介调度，类似于第三方支付
 */
public class MediatorPattern {

    //抽象中介者
    abstract class Mediator {
        public abstract void register(Colleague colleague);

        public abstract void relay(Colleague cl); //转发
    }

    //具体中介者
    class ConcreteMediator extends Mediator {
        private List<Colleague> colleagues = new ArrayList<>();

        public void register(Colleague colleague) {
            if (!colleagues.contains(colleague)) {
                colleagues.add(colleague);
                colleague.setMedium(this);
            }
        }

        public void relay(Colleague cl) {
            for (Colleague ob : colleagues) {
                if (!ob.equals(cl)) {
                    ((Colleague) ob).receive();
                }
            }
        }
    }

    //抽象同事类
    abstract class Colleague {
        protected Mediator mediator;

        public void setMedium(Mediator mediator) {
            this.mediator = mediator;
        }

        public abstract void receive();

        public abstract void send();
    }

    //具体同事类
    class ConcreteColleague1 extends Colleague {
        public void receive() {
            System.out.println("具体同事类1收到请求。");
        }

        public void send() {
            System.out.println("具体同事类1发出请求。");
            mediator.relay(this); //请中介者转发
        }
    }

    //具体同事类
    class ConcreteColleague2 extends Colleague {
        public void receive() {
            System.out.println("具体同事类2收到请求。");
        }

        public void send() {
            System.out.println("具体同事类2发出请求。");
            mediator.relay(this); //请中介者转发
        }
    }

    public static void main(String[] args) {
        Mediator md = new MediatorPattern().new ConcreteMediator();
        Colleague c1, c2;
        c1 = new MediatorPattern().new ConcreteColleague1();
        c2 = new MediatorPattern().new ConcreteColleague2();
        md.register(c1);
        md.register(c2);
        c1.send();
        System.out.println("-------------");
        c2.send();
    }


}
