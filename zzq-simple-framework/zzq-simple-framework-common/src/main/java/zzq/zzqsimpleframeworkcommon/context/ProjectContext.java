package zzq.zzqsimpleframeworkcommon.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 存储当前项目全局上下文信息
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-16 14:22
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectContext {

    /**
     * 类名
     */
    private String className;

    /**
     * 方法名
     */
    private String methodName;
}
