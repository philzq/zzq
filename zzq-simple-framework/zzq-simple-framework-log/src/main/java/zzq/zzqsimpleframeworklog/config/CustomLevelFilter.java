package zzq.zzqsimpleframeworklog.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * 自定义filter，控制warn及以下级别的输出到一个文件
 *
 * @author zhouzhiqiang
 * @version 1.0
 * @date 2023-02-15 16:01
 */
public class CustomLevelFilter extends AbstractMatcherFilter<ILoggingEvent> {

    Level level;

    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (!isStarted()) {
            return FilterReply.NEUTRAL;
        }

        if (level.isGreaterOrEqual(event.getLevel())) {
            return onMatch;
        } else {
            return onMismatch;
        }
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void start() {
        if (this.level != null) {
            super.start();
        }
    }
}
