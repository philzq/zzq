package zzq.zzq.ocr.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ImageUtil;
import cn.hutool.http.HttpRequest;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;

import java.io.File;
import java.util.UUID;

public class Tess4jUtils {

    /**
     * 从图片中提取文字,默认设置英文字库,使用classpath目录下的训练库
     * @param path
     * @return
     */
    public static String take(String path){
        // JNA Interface Mapping
        ITesseract instance = new Tesseract();
        // JNA Direct Mapping
        // ITesseract instance = new Tesseract1();
        File imageFile = new File(path);
        //In case you don't have your own tessdata, let it also be extracted for you
        //这样就能使用classpath目录下的训练库了
        File tessDataFolder = LoadLibs.extractTessResources("tessdata");
        //Set the tessdata path
        instance.setDatapath(tessDataFolder.getAbsolutePath());
        //英文库识别数字比较准确
        instance.setLanguage(Constants.ENG);
        return getOCRText(instance, imageFile);
    }

    /**
     * 从图片中提取文字,默认设置英文字库,使用classpath目录下的训练库
     * @param imageFile
     * @return
     */
    public static String take(File imageFile){
        // JNA Interface Mapping
        ITesseract instance = new Tesseract();
        //In case you don't have your own tessdata, let it also be extracted for you
        //这样就能使用classpath目录下的训练库了
        File tessDataFolder = LoadLibs.extractTessResources("tessdata");
        //Set the tessdata path
        instance.setDatapath(tessDataFolder.getAbsolutePath());
        //英文库识别数字比较准确
        instance.setLanguage(Constants.ENG);
        return getOCRText(instance, imageFile);
    }


    /**
     * 从图片中提取文字
     * @param path
     * @param dataPath
     * @param language
     * @return
     */
    public static String take(String path, String dataPath, String language){
        File imageFile = new File(path);
        ITesseract instance = new Tesseract();
        instance.setDatapath(dataPath);
        //英文库识别数字比较准确
        instance.setLanguage(language);
        return getOCRText(instance, imageFile);
    }


    /**
     * 识别图片文件中的文字
     * @param instance
     * @param imageFile
     * @return
     */
    private static String getOCRText(ITesseract instance, File imageFile){
        String result = null;
        try {
            result = instance.doOCR(imageFile);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public static void main(String[] args) throws Exception{
        System.out.println(System.getProperty("user.dir"));
        byte[] bytes = HttpRequest.get("https://passport.ganji.com/ajax.php?dir=captcha&module=checkcode&tag=phone&nocache=1577084186769")
                .execute()
                .bodyBytes();
        String flagFileName = UUID.randomUUID().toString()+".png";
        String fileName = UUID.randomUUID().toString()+".png";
        File flagImageFile = new File(Constants.IMG_ROOT+flagFileName);
        //File imageFile = new File(Constants.IMG_ROOT+fileName);
        FileUtil.writeBytes(bytes, flagImageFile);
        File imageFile = ImageUtils.cleanImage(flagImageFile, Constants.IMG_ROOT);
        String value = take(imageFile);
        //FileUtil.del(imageFile);
        System.out.println(value);
    }

}
