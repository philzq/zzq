package zzq;

public class Test {

    public static void main(String[] args){
        test("123","456");
    }

    private static void test(String... params){
        test2(params);
    }

    private static void test2(String... params){
        for(String param : params){
            System.out.println(param);
        }
    }
}
