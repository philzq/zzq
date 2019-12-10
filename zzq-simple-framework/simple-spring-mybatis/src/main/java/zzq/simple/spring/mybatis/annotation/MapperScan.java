package zzq.simple.spring.mybatis.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface MapperScan {

    /**
     * Mapper接口扫描路径
     * @return
     */
    String[] value() default {};
}
