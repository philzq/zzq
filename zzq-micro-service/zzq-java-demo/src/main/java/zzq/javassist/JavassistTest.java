package zzq.javassist;

import com.alibaba.ttl.threadpool.agent.internal.javassist.ClassPool;
import com.alibaba.ttl.threadpool.agent.internal.javassist.CtClass;
import com.alibaba.ttl.threadpool.agent.internal.javassist.CtMethod;
import com.alibaba.ttl.threadpool.agent.internal.javassist.NotFoundException;

public class JavassistTest {

    /**
     * Description:
     *
     * @param args
     * @author Administrator  DateTime 2014-7-10 下午3:15:42
     */
///////入口启动函数
    public static void main(String[] args) throws Exception {

        //这个是得到反编译的池

        ClassPool pool = ClassPool.getDefault();

        //取得需要反编译的jar文件，设定路径

        pool.insertClassPath("D:\\Sources.git\\zzq\\spring-boot\\spring-boot-test\\target\\spring-boot-test\\spring-boot-test-0.0.1-SNAPSHOT.jar");

        //取得需要反编译修改的文件，注意是完整路径

        CtClass cc1 = pool.get("zzq.spring.boot.test.SpringBootTest");

        try {

            //取得需要修改的方法
            CtMethod method = cc1.getDeclaredMethod("main");

            //插入修改项，我们让他直接返回(注意：根据方法的具体返回值返回，因为这个方法返回值是void，所以直接return；)
            method.insertBefore("System.out.println(\"JavassistTestJavassistTestJavassistTestJavassistTestJavassistTestJavassistTestJavassistTestJavassistTestJavassistTestJavassistTestJavassistTestJavassistTestJavassistTest\");");

            //写入保存
            cc1.writeFile();

        } catch (NotFoundException e) {
            e.printStackTrace();
        }

    }
}
