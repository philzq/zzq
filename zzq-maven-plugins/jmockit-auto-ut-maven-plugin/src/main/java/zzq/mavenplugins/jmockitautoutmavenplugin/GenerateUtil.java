package zzq.mavenplugins.jmockitautoutmavenplugin;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import zzq.mavenplugins.jmockitautoutmavenplugin.config.GenerateConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
                        String sourceClassPath = getClassPath(sourceDirectory, file);
                        GenerateConfiguration configuration = GenerateConfiguration.getInstance();
                        ClassPool pool = ClassPool.getDefault();
                        CtClass ctClass = pool.getCtClass(sourceClassPath);
                        CtMethod[] methods = ctClass.getDeclaredMethods();
                        if(methods != null){
                            for(int i=0;i<methods.length;i++){
                                CtMethod ctMethod = methods[i];
                                System.out.println("******"+ctMethod.getName()+"******");
                                Map<CtMethod,Set<CtMethod>> methodSetMap = new HashMap<>();
                                final Set<CtMethod> ctMethods = new HashSet<>();
                                ctMethod.instrument(new ExprEditor(){
                                    @Override
                                    public void edit(MethodCall m) throws CannotCompileException {
                                        System.out.println("className:"+m.getClassName()+",methodName:"+m.getMethodName());
                                        try {
                                            CtMethod method = m.getMethod();
                                            ctMethods.add(method);
                                        }catch (Exception e){
                                            System.out.println(e.getStackTrace());
                                        }
                                    }
                                });
                                methodSetMap.put(ctMethod,ctMethods);
                                configuration.addClassMethodDependency(ctClass,methodSetMap);
                            }
                        }
                        for (CtClass ctClass1 : configuration.getMethodDependency().keySet()) {
                            System.out.println("name" + ctClass1.getName());
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

    /**
     * 获取类路径
     * @param sourceDirectory
     * @param file
     * @return
     */
    public static String getClassPath(File sourceDirectory, File file){
        String sourceAbsolutePath = file.getAbsolutePath();
        sourceAbsolutePath = sourceAbsolutePath.substring(0,sourceAbsolutePath.lastIndexOf("."));
        String sourcePkg = sourceAbsolutePath.replace(sourceDirectory.getAbsolutePath(), "")
                .replace("\\", ".").substring(1);
        return sourcePkg;
    }
}
