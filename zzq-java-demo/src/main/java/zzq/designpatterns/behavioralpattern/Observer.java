package zzq.designpatterns.behavioralpattern;

import java.util.ArrayList;
import java.util.List;

/**
 * 观察者（ObserverTest）模式的定义：指多个对象间存在一对多的依赖关系，当一个对象的状态发生改变时，所有依赖于它的对象都得到通知并被自动更新。这种模式有时又称作发布-订阅模式、模型-视图模式，它是对象行为型模式。
 * <p>
 * 观察者模式是一种对象行为型模式，其主要优点如下。
 * 降低了目标与观察者之间的耦合关系，两者之间是抽象耦合关系。
 * 目标与观察者之间建立了一套触发机制。
 * <p>
 * 它的主要缺点如下。
 * 目标与观察者之间的依赖关系并没有完全解除，而且有可能出现循环引用。
 * 当观察者对象很多时，通知的发布会花费很多时间，影响程序的效率。
 */
public class Observer {

    //抽象目标
    abstract class Subject {
        protected List<ObserverTest> observerTests = new ArrayList<>();

        //增加观察者方法
        public void add(ObserverTest observerTest) {
            observerTests.add(observerTest);
        }

        //删除观察者方法
        public void remove(ObserverTest observerTest) {
            observerTests.remove(observerTest);
        }

        public abstract void notifyObserver(); //通知观察者方法
    }

    //具体目标
    class ConcreteSubject extends Subject {
        public void notifyObserver() {
            System.out.println("具体目标发生改变...");
            System.out.println("--------------");

            for (Object obs : observerTests) {
                ((ObserverTest) obs).response();
            }

        }
    }

    //抽象观察者
    interface ObserverTest {
        void response(); //反应
    }

    //具体观察者1
    class ConcreteObserver1 implements ObserverTest {
        public void response() {
            System.out.println("具体观察者1作出反应！");
        }
    }

    //具体观察者1
    class ConcreteObserver2 implements ObserverTest {
        public void response() {
            System.out.println("具体观察者2作出反应！");
        }
    }

    public static void main(String[] args)
    {
        Subject subject=new Observer().new ConcreteSubject();
        ObserverTest obs1=new Observer().new ConcreteObserver1();
        ObserverTest obs2=new Observer().new ConcreteObserver2();
        subject.add(obs1);
        subject.add(obs2);
        subject.notifyObserver();
    }
}
