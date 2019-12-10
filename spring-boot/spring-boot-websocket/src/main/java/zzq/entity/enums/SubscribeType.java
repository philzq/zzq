package zzq.entity.enums;

/**
 * 〈功能简述〉<br>
 * 〈订阅类型〉
 *
 * @create 2018/12/25
 */
public enum SubscribeType {
    HEART_BEAT("HEART_BEAT","心跳检测"),
    SUB("SUB","订阅"),
    UN_SUB("UN_SUB","取消订阅");

    //订阅类型
    private String code;
    //订阅类型描述
    private String message;

    SubscribeType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /**
     * 是否包含这个code
     * @param code
     * @return
     */
    public static boolean contains(String code){
        boolean flag = false;
        for(SubscribeType subscribeType : SubscribeType.values()){
            if(code.equals(subscribeType.code)){
                flag = true;
                break;
            }
        }
        return flag;
    }

    public static SubscribeType getInstanceByCode(String code){
        for(SubscribeType subscribeType : SubscribeType.values()){
            if(code.equals(subscribeType.code)){
                return subscribeType;
            }
        }
        return null;
    }
}
