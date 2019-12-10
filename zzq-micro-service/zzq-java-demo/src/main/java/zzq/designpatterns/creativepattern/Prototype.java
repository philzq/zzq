package zzq.designpatterns.creativepattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import zzq.utils.CloneUtils;

import java.io.Serializable;

/**
 * 原型（Prototype）模式的定义如下：用一个已经创建的实例作为原型，通过复制该原型对象来创建一个和原型相同或相似的新对象。
 * 在这里，原型实例指定了要创建的对象的种类。用这种方式创建对象非常高效，
 * 根本无须知道对象创建的细节。例如，Windows 操作系统的安装通常较耗时，如果复制就快了很多。
 *
 * 衍生概念--深克隆与浅克隆
 * 浅复制(浅克隆)：被复制对象的所有变量都含有与原来的对象相同的值，而所有的对其他对象的引用仍然指向原来的对象。
 * 换言之，浅复制仅仅复制所拷贝的对象，而不复制它所引用的对象。
 *
 * 深复制(深克隆)：被复制对象的所有变量都含有与原来的对象相同的值，除去那些引用其他对象的变量。那些引用其他对象的变量将指向被复制过的新对象，而不再是原有的那些被引用的对象。
 * 换言之，深复制把要复制的对象所引用的对象都复制了一遍。
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Prototype implements Cloneable, Serializable {

    /**
     * 用于测试克隆是不是同一对象
     */
    private String name;

    /**
     * 用于测试深克隆与浅克隆
     */
    private DepthCloneTest depthCloneTest;

    @Data
    public class DepthCloneTest implements Serializable{
        private String name;
        private String age;
    }

    public static void main(String[] args) throws CloneNotSupportedException{
        Prototype prototype = Prototype.builder().depthCloneTest(new Prototype().new DepthCloneTest()).build();
        Prototype prototypeShallowClone = (Prototype)prototype.clone();
        /************浅克隆测试************/
        //false 说明prototype与prototypeClone不是同一个对象
        System.out.println(prototype == prototypeShallowClone);
        //true,说明prototype.getDepthCloneTest()与prototypeClone.getDepthCloneTest()是同一个对象
        System.out.println(prototype.getDepthCloneTest() == prototypeShallowClone.getDepthCloneTest());
        /***************深克隆测试***************/
        Prototype prototypeDeepClone = CloneUtils.cloneObject(prototype);
        //false 说明prototype与prototypeDeepClone不是同一个对象
        System.out.println(prototype == prototypeDeepClone);
        //false,说明prototype.getDepthCloneTest()与prototypeDeepClone.getDepthCloneTest()不是同一个对象
        System.out.println(prototype.getDepthCloneTest() == prototypeDeepClone.getDepthCloneTest());
    }
}
