package zzq.designpatterns.behavioralpattern;

/**
 * 命令（CommandPattern）模式的定义如下：将一个请求封装为一个对象，使发出请求的责任和执行请求的责任分割开。这样两者之间通过命令对象进行沟通，这样方便将命令对象进行储存、传递、调用、增加与管理。
 * <p>
 * 命令模式的主要优点如下。
 * 降低系统的耦合度。命令模式能将调用操作的对象与实现该操作的对象解耦。
 * 增加或删除命令非常方便。采用命令模式增加与删除命令不会影响其他类，它满足“开闭原则”，对扩展比较灵活。
 * 可以实现宏命令。命令模式可以与组合模式结合，将多个命令装配成一个组合命令，即宏命令。
 * 方便实现 Undo 和 Redo 操作。命令模式可以与后面介绍的备忘录模式结合，实现命令的撤销与恢复。
 * <p>
 * 其缺点是：可能产生大量具体命令类。因为计对每一个具体操作都需要设计一个具体命令类，这将增加系统的复杂性。
 * <p>
 * 总结：调用者与接收者通过命令对象间接沟通，就类似于中间件，负载均衡等
 */
public class CommandPattern {

    //调用者
    class Invoker {
        private Command command;

        public Invoker(Command command) {
            this.command = command;
        }

        public void setCommand(Command command) {
            this.command = command;
        }

        public void call() {
            System.out.println("调用者执行命令command...");
            command.execute();
        }
    }

    //抽象命令
    interface Command {
        void execute();
    }

    //具体命令
    class ConcreteCommand implements Command {
        private Receiver receiver;

        ConcreteCommand() {
            receiver = new Receiver();
        }

        public void execute() {
            receiver.action();
        }
    }

    //接收者
    class Receiver {
        public void action() {
            System.out.println("接收者的action()方法被调用...");
        }
    }

    public static void main(String[] args) {
        Command cmd = new CommandPattern().new ConcreteCommand();
        Invoker ir = new CommandPattern().new Invoker(cmd);
        System.out.println("客户访问调用者的call()方法...");
        ir.call();
    }
}
