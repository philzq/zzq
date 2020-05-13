package zzq.mavenplugins.jmockitautoutmavenplugin;

import java.io.File;
import java.lang.reflect.Method;

public class GenerateUtil {

    /**
     * 在测试文件目录生成MockAndTest文件
     * @param rootFile
     * @param sourceDirectory
     * @param testSourceDirectory
     */
    public static void generateMockAndTestFile(File rootFile,File sourceDirectory,File testSourceDirectory){
        if(rootFile == null){
            return;
        }
        if(rootFile.isDirectory()){
            File[] listFiles = rootFile.listFiles();
            for(File file:listFiles) {
                if(file.isFile()){
                    String parent = file.getParent();
                    String testParentPath = parent.replace(sourceDirectory.getAbsolutePath(), testSourceDirectory.getAbsolutePath());
                    File testParentFile = new File(testParentPath);
                    if(!testParentFile.exists()){
                        testParentFile.mkdirs();
                    }
                    File unitTestFile = new File(testParentPath+"/"+file.getName().substring(0,file.getName().lastIndexOf("."))+"UnitTest.java");
                    File unitMockFile = new File(testParentPath+"/"+file.getName().substring(0,file.getName().lastIndexOf("."))+"UnitMock.java");
                    if(!unitTestFile.exists()){
                        try {
                            System.out.println("创建文件:"+unitTestFile.getAbsolutePath());
                            unitTestFile.createNewFile();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    if(!unitMockFile.exists()){
                        try {
                            System.out.println("创建文件:"+unitTestFile.getAbsolutePath());
                            unitMockFile.createNewFile();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    //根据源文件生成Test,Mock文件
                    try {
                        String sourcePkg = getPkg(sourceDirectory, file);
                        String mockPkg = getPkg(testSourceDirectory, unitMockFile);
                        String testPkg = getPkg(testSourceDirectory, unitTestFile);

                        Class<?> sourceClass = Class.forName(sourcePkg);
                        Method[] methods = sourceClass.getDeclaredMethods();
                        if(methods != null){
                            for(int i=0; i< methods.length;i++){
                                System.out.println(sourceClass.getName()+":"+methods[i].getName());
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    generateMockAndTestFile(file,sourceDirectory,testSourceDirectory);
                }
            }
        }else{
            generateMockAndTestFile(rootFile,sourceDirectory,testSourceDirectory);
        }
    }

    private static String getPkg(File sourceDirectory, File file) {
        String sourceAbsolutePath = file.getAbsolutePath();
        sourceAbsolutePath = sourceAbsolutePath.substring(0,sourceAbsolutePath.lastIndexOf("."));
        String sourcePkg = sourceAbsolutePath.replace(sourceDirectory.getAbsolutePath(), "")
                .replace("\\", ".")
                .substring(1);
        return sourcePkg;
    }
}
