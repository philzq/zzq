package zzq.simple.spring.mybatis.entity;

public class OrderLog {
    /**
     * 日志ID : 
     */
    private Long logID;

    public Long getLogID() {
        return logID;
    }

    public void setLogID(Long logID) {
        this.logID = logID;
    }

    @Override
    public String toString() {
        return "OrderLog{" +
                "logID=" + logID +
                '}';
    }
}