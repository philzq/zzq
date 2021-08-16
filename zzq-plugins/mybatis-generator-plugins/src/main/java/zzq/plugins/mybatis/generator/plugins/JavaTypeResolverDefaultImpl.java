package zzq.plugins.mybatis.generator.plugins;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import java.sql.Types;


public class JavaTypeResolverDefaultImpl extends org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl {

    public  JavaTypeResolverDefaultImpl(){
        super();
        typeMap.put(Types.TINYINT, new JdbcTypeInformation("TINYINT",
                new FullyQualifiedJavaType(Integer.class.getName())));
    }
}