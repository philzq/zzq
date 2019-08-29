package zzq.designpatterns.structuralpattern;

import java.util.ArrayList;

/**
 * 组合（Composite）模式的定义：有时又叫作部分-整体模式，它是一种将对象组合成树状的层次结构的模式，用来表示“部分-整体”的关系，使用户对单个对象和组合对象具有一致的访问性。
 * <p>
 * 组合模式的主要优点有：
 * 组合模式使得客户端代码可以一致地处理单个对象和组合对象，无须关心自己处理的是单个对象，还是组合对象，这简化了客户端代码；
 * 更容易在组合体内加入新的对象，客户端不会因为加入了新的对象而更改源代码，满足“开闭原则”；
 * <p>
 * 其主要缺点是：
 * 设计较复杂，客户端需要花更多时间理清类之间的层次关系；
 * 不容易限制容器中的构件；
 * 不容易用继承的方法来增加构件的新功能；
 *
 * 总结：该模式适用于含有层级关系的结构
 */
public class Composite {

    //抽象构件
    interface Component {
        public void add(Component c);

        public void remove(Component c);

        public Component getChild(int i);

        public void operation();
    }

    //树叶构件
    class Leaf implements Component {
        private String name;

        public Leaf(String name) {
            this.name = name;
        }

        public void add(Component c) {
        }

        public void remove(Component c) {
        }

        public Component getChild(int i) {
            return null;
        }

        public void operation() {
            System.out.println("树叶" + name + "：被访问！");
        }
    }

    //树枝构件
    class CompositeTest implements Component {
        private ArrayList<Component> children = new ArrayList<Component>();

        public void add(Component c) {
            children.add(c);
        }

        public void remove(Component c) {
            children.remove(c);
        }

        public Component getChild(int i) {
            return children.get(i);
        }

        public void operation() {
            for (Object obj : children) {
                ((Component) obj).operation();
            }
        }
    }

    public static void main(String[] args) {
        Component c0 = new Composite().new CompositeTest();
        Component c1 = new Composite().new CompositeTest();
        Component leaf1 = new Composite().new Leaf("1");
        Component leaf2 = new Composite().new Leaf("2");
        Component leaf3 = new Composite().new Leaf("3");
        c0.add(leaf1);
        c0.add(c1);
        c1.add(leaf2);
        c1.add(leaf3);
        c0.operation();
    }

}
