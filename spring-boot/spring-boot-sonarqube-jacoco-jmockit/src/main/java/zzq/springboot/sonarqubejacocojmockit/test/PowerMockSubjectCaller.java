package zzq.springboot.sonarqubejacocojmockit.test;

public class PowerMockSubjectCaller {

    public String callStaticMethod(String input){
        String aa = PowerMockSubject.staticMethod(input);
        System.out.println(aa);
        return aa;
    }

}
