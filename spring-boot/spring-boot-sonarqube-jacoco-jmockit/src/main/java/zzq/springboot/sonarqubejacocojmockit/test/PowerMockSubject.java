package zzq.springboot.sonarqubejacocojmockit.test;

import java.io.File;

public class PowerMockSubject {

    private final String a;
    private final int b;

    public PowerMockSubject(String a, int b) {
        System.out.println("PowerMockSubject is called actually.");
        this.a = a;
        this.b = calculateB(b);
    }

    private int calculateB(int b) {
        System.out.println("calculateB is called actually.");
        if (b < 0) {
            return b * -1 + 100;
        }
        return b;
    }

    public int callPrivateMethod(int b){
        System.out.println("callPrivateMethod start");
        return calculateB(b);
    }

    public static String staticMethod(String arg1) {
        System.out.println("staticMethod is called actually.");
        return arg1 + " static";
    }

    public final String finalMethod(String arg1) {
        System.out.println("finalMethod is called actually.");
        return arg1 + " final";
    }

    public String callConstructor(String input){
        File f = new File(input);
        if(f.exists()){
            return f.getName();
        }else{
            return "NOTFOUND";
        }
    }
}
