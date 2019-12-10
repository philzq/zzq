package zzq.entity.enums;

public enum Caches {

    BOOK(60, 20);

    private int maxSize;    //最大數量
    private int timeOut;        //过期时间（秒）

    Caches() {
    }

    Caches(int timeOut) {
        this.timeOut = timeOut;
    }

    Caches(int timeOut, int maxSize) {
        this.timeOut = timeOut;
        this.maxSize = maxSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int gettimeOut() {
        return timeOut;
    }
}
