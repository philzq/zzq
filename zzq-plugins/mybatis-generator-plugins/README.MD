# 官方信息
|id|Url
|---|---    
|1| [MyBatis Generator官方地址](http://www.mybatis.org/generator/)
|2| [MyBatis Generator Plugin Lifecycle](http://www.mybatis.org/generator/reference/pluggingIn.html)
|3| [官方plugin例子](https://github.com/mybatis/generator/tree/master/core/mybatis-generator-core/src/main/java/org/mybatis/generator/plugins)
写在前面的plugin先执行。

整个执行过程都是在处理一些内存对象，最终所有plugin执行完才写入。

# 版本记录
- **1.1.5** SelectSelectiveElementGenerator 插件生成的 selectSelective 加上提示抑制 <!--suppress MybatisMapperXmlInspection -->

# 使用方法

### 配置pom.xml 
```
    <build>
        <plugins>
            <!--=====================================================================-->
            <!--====================== mybatis.generator 代码生成工具 =========================-->
            <!--=====================================================================-->
            <plugin>
                <groupId>org.mybatis.generator</groupId>
                <artifactId>mybatis-generator-maven-plugin</artifactId>
                <version>1.4.0</version>
                <executions>
                    <execution>
                        <id>Generate MyBatis Artifacts</id>
                        <!--修改自动生成代码的 时机-->
                        <phase>deploy</phase>
                        <goals>
                            <goal>generate</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <!-- generator 工具配置文件的位置 -->
                    <configurationFile>src/main/resources/mybatis/mybatis-generator.xml</configurationFile>
                    <verbose>true</verbose>
                    <overwrite>true</overwrite>
                </configuration>

                <dependencies>
                    <dependency>
                        <groupId>org.mybatis.generator</groupId>
                        <artifactId>mybatis-generator-core</artifactId>
                        <version>1.4.0</version>
                    </dependency>

                    <!-- https://mvnrepository.com/artifact/com.microsoft.sqlserver/sqljdbc4 -->
                    <dependency>
                        <groupId>com.microsoft.sqlserver</groupId>
                        <artifactId>sqljdbc4</artifactId>
                        <version>4.0</version>
                    </dependency>
                    
                    <!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
                    <dependency>
                        <groupId>mysql</groupId>
                        <artifactId>mysql-connector-java</artifactId>
                        <version>8.0.17</version>
                    </dependency>


                    <dependency>
                        <groupId>zzq.framework</groupId>
                        <artifactId>mybatis-generator-plugins</artifactId>
                        <version>1.0.0</version>
                    </dependency>
                </dependencies>
            </plugin>
        </plugins>
    </build>
```

### 配置src\main\resources\mybatis\mybatis-generator.xml
```
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE generatorConfiguration PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd" >
<!--
官方：
http://www.mybatis.org/generator/reference/pluggingIn.html
https://github.com/mybatis/generator/tree/master/core/mybatis-generator-core/src/main/java/org/mybatis/generator/plugins
-->

<generatorConfiguration>
    <!--  defaultModelType="flat" 设置这个 联合主键生成到一个类里面 -->
    <!--  可以配置多个context节点来支持多数据库，id设置不同便可 -->
    <context id="4SqlServer1" targetRuntime="MyBatis3Simple" defaultModelType="flat">
        <property name="javaFileEncoding" value="UTF-8"/>
        <property name="suppressTypeWarnings" value="true"/>

        <!--jpa需要-->
        <plugin type="org.mybatis.generator.plugins.SerializablePlugin"/>

        <!--视图没有主键，自己在table标签里创建个虚拟的-->
        <!--https://github.com/mybatis/generator/blob/master/core/mybatis-generator-core/src/main/java/org/mybatis/generator/plugins/VirtualPrimaryKeyPlugin.java -->
        <plugin type="org.mybatis.generator.plugins.VirtualPrimaryKeyPlugin"/>

        <!--删除所有自动生成的目标目录里的文件，默认是全删除，一般要放在最前面，如果使用融合方案在应注释掉这个-->
        <!--        <plugin type="DeleteGeneratedFilesPlugin"/>-->

        <!--MyBatis Generator 1.3.7还没有实现生成MSSQL  SqlServer备注，用插件填充上，之后再使用就有了，应该放的尽可能靠前-->
        <!-- mysql不需要此配置 -->
        <plugin type="zzq.plugins.mybatis.generator.plugins.FixSqlServerCommentPlugin"/>

        <!--为Model加上Swagger的注解-->
        <plugin type="zzq.plugins.mybatis.generator.plugins.SwaggerAnnotationPlugin"/>

        <!--为Model加上lombok的import及自己想在类上加的注解，都是以|分割-->
        <plugin type="zzq.plugins.mybatis.generator.plugins.LombokAnnotationPlugin">
            <property name="imports" value="lombok.*|lombok.experimental.*"/>
            <property name="annotations" value="@Data|@Builder|@AllArgsConstructor|@NoArgsConstructor"/>
        </plugin>

        <!--为Model加上Jpa注解@Entity,@Id,@Table,@Column等-->
        <plugin type="zzq.plugins.mybatis.generator.plugins.JpaAnnotationPlugin"/>

        <!--集成tk 通用mapper，Model，SqlMap，DAO都是融合的：同一文件上面自动，下面手动-->
        <!-- TkMapperPlugin不能与MybatisPlusPlugin同时使用，请搜索下，注释掉其中一个 -->
        <plugin type="zzq.plugins.mybatis.generator.plugins.TkMapperPlugin">
            <!--所有生成的Mapper都继承自这个设定，多个可以“,”隔开-->
            <property name="mappers" value="hk.com.wot.dao.MyMapper"/>
            <!-- Field加类似@ColumnType(jdbcType = JdbcType.NVARCHAR)，最终运行时效果：#{resourceID,jdbcType=BIGINT}-->
            <property name="isGenColumnTypeWithJdbcType" value="true"/>
            <!--是否融合Model新旧imports-->
            <property name="isMixModelImports" value="true"/>
        </plugin>
        
        <!--集成Mybatisplus 通用mapper，Model，SqlMap，DAO都是融合的：同一文件上面自动，下面手动-->
        <!-- TkMapperPlugin不能与MybatisPlusPlugin同时使用，请搜索下，注释掉其中一个 -->
        <plugin type="zzq.plugins.mybatis.generator.plugins.MybatisPlusPlugin">
            <!--所有生成的Mapper都继承自这个设定，多个可以“,”隔开-->
            <property name="mappers" value="hk.com.wot.dao.MyMapper"/>
            <!-- Field加类似@ColumnType(jdbcType = JdbcType.NVARCHAR)，最终运行时效果：#{resourceID,jdbcType=BIGINT}-->
            <property name="isGenColumnTypeWithJdbcType" value="true"/>
            <!--是否融合Model新旧imports-->
            <property name="isMixModelImports" value="true"/>
        </plugin>

        <!--Model与XML增量更新插件-->
        <!--放在TkMapperPlugin与MybatisPlusPlugin插件后-->
        <plugin type="zzq.plugins.mybatis.generator.plugins.IncrementalUpdatingPlugin"/>

        <!--生成QueryDsl的专用实体-->
        <plugin type="zzq.plugins.mybatis.generator.plugins.QueryDslPlugin">
            <property name="targetPackage" value="hk.com.wot.model.domain"/>
            <property name="targetProject" value="src/main/java"/>
        </plugin>

        <!-- 生成标示是否Db加密字段的注释插件 -->
        <plugin type="zzq.plugins.mybatis.generator.plugins.EncrypedColumnAnnotationPlugin">
            <property name="annotaionClass" value="zzq.frbs.entities.common.EncryptedColumn"/>
            <property name="annotaionClassValue" value="★★★很重要：此注解不能去掉。此字段是加密的，要有此注解查询和更新时 mybatis 才会自动处理★★★"/>
        </plugin>


        <!--为Model加上javadoc，内容是数据库备注-->
        <commentGenerator type="zzq.plugins.mybatis.generator.plugins.DatabaseCommentGenerator">
            <property name="suppressDate" value="true"/>
            <!--zzq生成实体备注的插件不关注此设置-->
            <property name="suppressAllComments" value="true"/>
        </commentGenerator>


        <jdbcConnection driverClass="com.microsoft.sqlserver.jdbc.SQLServerDriver"
                <!-- connectionURL="jdbc:mysql://127.0.0.1:3306/wtsdb?useUnicode=true&amp;characterEncoding=UTF-8"-->
                        connectionURL="jdbc:sqlserver://127.0.0.1;DatabaseName=PackageDB"
                        userId="sa"
                        password="********">
        </jdbcConnection>

        <javaTypeResolver type="zzq.plugins.mybatis.generator.plugins.JavaTypeResolverDefaultImpl">
            <property name="forceBigDecimals" value="false"/>
            <!-- 简单说就是生成 LocalDateTime而不是Date -->
            <property name="useJSR310Types" value="true"/>
        </javaTypeResolver>

        <!--    Model    -->
        <javaModelGenerator targetPackage="hk.com.wot.model.domain" targetProject="src/main/java">
        </javaModelGenerator>

        <!--    Xml  SqlMap    -->
        <sqlMapGenerator targetPackage="mybatis/sqlmap" targetProject="src/main/resources">
        </sqlMapGenerator>

        <!--   DAO Mapper    -->
        <javaClientGenerator type="XMLMAPPER" targetPackage="hk.com.wot.dao.mybatis" targetProject="src/main/java">
        </javaClientGenerator>


        <!-- tableName:用于自动生成代码的数据库表；domainObjectName:对应于数据库表的javaBean类名-->
        <!-- mysql虽然在jdbcConnection节点中的connectionURL指定了数据库，但实际会把所有数据库都扫描了，所以出现相同名称的表时会报错，需要通过catalog属性指定数据库-->
        <table tableName="Pkg_BaseResourceLimitation" domainObjectName="BaseResourceLimitation" catalog="xxxdb">
            <!-- 与数据库表字段命名有关, 设为true会直接使用原命名;设为false时,若名称中带有下划线会生成camel格式,否则为全小写 -->
            <property name="useActualColumnNames" value="true"/>
            <!-- mysql加了catalog配置后，在sqlmap里的sql里会多出from xxxdb..TableName，此配置可消除 -->
            <property name="ignoreQualifiersAtRuntime" value="true"/>
        </table>

        <!--    表放上面，视图放下面    -->
        <table tableName="V_Types" domainObjectName="VTypes">
              <!--    视图没有主键，生成的实体会导致Jpa报错，给他造个    -->
            <property name="virtualKeyColumns" value="Category,TypeName,TypeValue"/>
            <property name="useActualColumnNames" value="true"/>
        </table>

    </context>
</generatorConfiguration>

```
