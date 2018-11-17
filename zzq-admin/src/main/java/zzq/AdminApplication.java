package zzq;

import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * 〈功能简述〉<br>
 * 〈admin启动类〉
 *
 * @author zhouzhiqiang
 * @create 2018/11/17 0017
 */
@SpringBootApplication
public class AdminApplication {

    public static void main(String[] args){
        SpringApplication.run(AdminApplication.class,args);
    }
}
