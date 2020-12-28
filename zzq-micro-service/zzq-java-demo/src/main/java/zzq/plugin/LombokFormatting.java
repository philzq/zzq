package zzq.plugin;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * lombok格式化处理
 *
 * @author zhiqiangzhou
 * @date 2020-12-04 17:14
 */
public class LombokFormatting {

    /**
     * 回车换行
     */
    private static final String NEW_LINE = "\r\n";

    /**
     * n个以上参数才换行，即builder()后面出现n个 .\w(即换行,嵌套函数也包含在内
     */
    private static final int NEW_LINE_OF_MORE_PARAM = 3;

    /**
     * 需要处理的文件后缀
     */
    private static final String SUFFIX = "java";

    /**
     * 排除的文件名
     */
    private static final List<String> RULE_OUT_FILE_LIST = List.of("Entity", "Enum", "DO", "Mapper", "LombokFormatting");

    /**
     * 排出的文件夹
     */
    private static final List<String> RULE_OUT_DIRECTORY_LIST = List.of("frbs-entities", "frbs-data", "offline-site", "entity", "target", "node_modules", "webjs", "idea");

    /**
     * 特殊场景不使用多行格式化，使用单行格式化
     */
    private static final List<String> RULE_OUT_MANY_LINE = List.of(";", "//", "/*");

    /*创建可用线程数量的固定线程池*/
    private static final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());


    public static void main(String[] args) {
        //获取项目跟目录
        String projectPath = System.getProperty("user.dir");
        //遍历项目所有java文件
        File file = new File(projectPath);
        fileLombokFormattings(file);
        executorService.shutdown();
    }

    /**
     * 将该目录下的所有文件内容简体转为繁体
     *
     * @param rootFile
     */
    private static void fileLombokFormattings(File rootFile) {
        if (rootFile == null) {
            return;
        }
        if (rootFile.isDirectory()) {
            File[] listFiles = rootFile.listFiles();
            files:
            for (File file : listFiles) {
                if (file.isFile()) {
                    executorService.execute(() -> {
                        try {
                            fileLombokFormatting(file);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    });
                } else {
                    for (String s : RULE_OUT_DIRECTORY_LIST) {
                        if (file.getAbsolutePath().contains(s)) {
                            continue files;
                        }
                    }
                    fileLombokFormattings(file);
                }
            }
        } else {
            executorService.execute(() -> {
                try {
                    fileLombokFormatting(rootFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * 文件修改
     *
     * @param file 文件
     */
    private static void fileLombokFormatting(File file) {
        if (ObjectUtils.isEmpty(file)) {
            return;
        }
        String absolutePath = file.getAbsolutePath();
        String substring = absolutePath.substring(absolutePath.lastIndexOf(".") + 1);
        if (!SUFFIX.equals(substring)) {
            return;
        }

        for (String s : RULE_OUT_FILE_LIST) {
            if (absolutePath.contains(s)) {
                return;
            }
        }
        System.out.println("处理文件开始：" + file.getAbsolutePath());

        //记录文件长度
        Long fileLength = file.length();
        //记录读出来的文件的内容
        byte[] fileContext = new byte[fileLength.intValue()];
        FileInputStream in = null;
        PrintWriter out = null;
        try {
            in = new FileInputStream(file);
            //读出文件全部内容(内容和文件中的格式一致,含换行)
            in.read(fileContext);
            // 避免出现中文乱码
            String fileStr = new String(fileContext, StandardCharsets.UTF_8);
            StringBuilder stringBuilder = new StringBuilder();
            if (ObjectUtils.isNotEmpty(fileStr)) {
                fileStr = initStr(fileStr);
                if (ObjectUtils.isEmpty(fileStr)) {
                    return;
                }
                String[] lineStrs = fileStr.split(NEW_LINE,Integer.MIN_VALUE);
                for (String lineStr : lineStrs) {
                    formatLineStr(stringBuilder, lineStr);
                }
            }
            //再把新的内容写入文件
            out = new PrintWriter(file);
            out.write(stringBuilder.substring(0,stringBuilder.lastIndexOf(NEW_LINE)));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ObjectUtils.isNotEmpty(out)) {
                    out.flush();
                    out.close();
                }
                if (ObjectUtils.isNotEmpty(in)) {
                    in.close();
                }
                if (ObjectUtils.isNotEmpty(file)) {
                    System.out.println("处理文件结束：" + file.getAbsolutePath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取格式化后的字符串
     *
     * @param stringBuilder
     * @param lineStr
     */
    private static void formatLineStr(StringBuilder stringBuilder, String lineStr) {
        //修改对应字符串内容
        String matchesLineStr = matchesLineStr(lineStr);
        if (ObjectUtils.isNotEmpty(matchesLineStr)) {
            //获取格式化后的字符串
            int builderIndex = lineStr.indexOf("builder()") + 9;
            int beginIndex = lineStr.indexOf(matchesLineStr.substring(0, matchesLineStr.indexOf("builder()") + 9));
            int blankSpaceIndex = lineStr.indexOf(lineStr.trim()) + 8;
            char[] chars = matchesLineStr.toCharArray();
            StringBuilder lineStringBuilder = new StringBuilder();
            int parentheseCount = 0;
            for (int i = beginIndex; i < chars.length + beginIndex; i++) {
                char aChar = chars[i - beginIndex];
                if (aChar == '(') {
                    parentheseCount++;
                }
                if (aChar == ')') {
                    parentheseCount--;
                }
                if (aChar == '.' && i >= builderIndex && parentheseCount == 0) {
                    lineStringBuilder.append(NEW_LINE)
                            .append(StringUtils.rightPad(" ", blankSpaceIndex))
                            .append(".");
                } else {
                    lineStringBuilder.append(aChar);
                }
            }
            lineStr = lineStr.replace(matchesLineStr, lineStringBuilder.toString());
            String[] lineRecursiveStrs = lineStr.split(NEW_LINE);
            if (ObjectUtils.isNotEmpty(lineRecursiveStrs) && lineRecursiveStrs.length >= 2) {
                for (String lineRecursiveStr : lineRecursiveStrs) {
                    formatLineStr(stringBuilder, lineRecursiveStr);
                }
            } else {
                stringBuilder.append(lineStr);
                stringBuilder.append(NEW_LINE);
            }
        } else {
            stringBuilder.append(lineStr);
            stringBuilder.append(NEW_LINE);
        }
    }

    /**
     * 返回匹配的字符串
     *
     * @param lineStr
     * @return
     */
    private static String matchesLineStr(String lineStr) {
        //注释行不做处理
        if(lineStr.contains("//")){
            return null;
        }
        String matchesLineStr = null;
        String regex = "[a-zA-Z0-9]+\\s*\\.builder\\(\\)(.*?\\.\\w*\\(){" + NEW_LINE_OF_MORE_PARAM + ",}.*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(lineStr);
        while (m.find()) {
            matchesLineStr = m.group(0);
        }
        return matchesLineStr;
    }

    /**
     * 初始化需要处理的字符串
     *
     * @param str
     * @return
     */
    private static String initStr(String str) {
        if (ObjectUtils.isEmpty(str)) {
            return str;
        }
        StringBuilder resultStringBuilder = new StringBuilder();
        List<Integer> builderIndexs = new ArrayList<>();
        List<Integer> buildIndexs = new ArrayList<>();
        String builderStr = "builder()";
        String buildStr = "build()";
        getWordAllIndex(str, builderStr, builderIndexs);
        getWordAllIndex(str, buildStr, buildIndexs);
        if (ObjectUtils.isNotEmpty(builderIndexs) && ObjectUtils.isNotEmpty(buildIndexs) && builderIndexs.size() == buildIndexs.size()) {
            Map<Integer, String> buildMap = new LinkedHashMap<>();
            builderIndexs.forEach(integer -> {
                buildMap.put(integer, builderStr);
            });
            buildIndexs.forEach(integer -> {
                buildMap.put(integer, buildStr);
            });
            Map<Integer, String> operationBuildMap = new LinkedHashMap<>();
            buildMap.entrySet().stream().sorted(Map.Entry.comparingByKey())
                    .forEachOrdered(x -> operationBuildMap.put(x.getKey(), x.getValue()));

            //去掉匹配字符串中的回车
            removeMatchCarriage(str, resultStringBuilder, builderStr, buildStr, operationBuildMap);
        } else {
            //特殊场景不对str做特殊处理
            return str;
        }
        return resultStringBuilder.toString();
    }

    /**
     * 去掉匹配字符串中的回车
     *
     * @param str
     * @param resultStringBuilder
     * @param builderStr
     * @param buildStr
     * @param buildMap
     */
    private static void removeMatchCarriage(String str, StringBuilder resultStringBuilder, String builderStr, String buildStr, Map<Integer, String> buildMap) {
        int beginIndex = 0;
        int endIndex = 0;
        int parentheseCount = 0;
        int lastEndIndex = 0;
        for (Map.Entry<Integer, String> entry : buildMap.entrySet()) {
            Integer index = entry.getKey();
            String value = entry.getValue();
            if (builderStr.equals(value)) {
                parentheseCount++;
                if (beginIndex == 0 && parentheseCount == 1) {
                    beginIndex = index - lastEndIndex;
                }
            }
            if (endIndex == 0 && buildStr.equals(value)) {
                parentheseCount--;
                if (endIndex == 0 && parentheseCount == 0) {
                    endIndex = index - lastEndIndex;
                }
            }
            if (beginIndex != 0 && endIndex != 0 && parentheseCount == 0 && endIndex > beginIndex) {
                int beforeLastPointIndex = str.substring(0, beginIndex).lastIndexOf(".");
                beginIndex = beforeLastPointIndex;
                String beforeStr = str.substring(0, beginIndex);
                String betweenStr = str.substring(beginIndex, endIndex);

                //特殊场景使用单行格式化
                boolean flag = false;
                for (String s : RULE_OUT_MANY_LINE) {
                    if (betweenStr.contains(s)) {
                        flag = true;
                    }
                }

                resultStringBuilder.append(beforeStr);
                if (flag) {
                    resultStringBuilder.append(betweenStr);
                } else {
                    String replaceBetweenStr = betweenStr.replaceAll("\\s*\r\n\\s*", "");
                    resultStringBuilder.append(replaceBetweenStr);
                }
                str = str.substring(endIndex);

                lastEndIndex += endIndex;
                endIndex = 0;
                beginIndex = 0;
            }
        }
        resultStringBuilder.append(str);
    }

    /**
     * 返回word单词在str中所有下标
     *
     * @param str    字符串
     * @param word   单词
     * @param result 存储下标结果
     */
    private static void getWordAllIndex(String str, String word, List<Integer> result) {
        if (ObjectUtils.isEmpty(str) || ObjectUtils.isEmpty(word)) {
            return;
        }
        if (str.contains(word)) {
            int wordFirstIndex = str.indexOf(word);
            result.add(wordFirstIndex);
            //匹配到的前面用空格填充
            str = StringUtils.rightPad(" ", wordFirstIndex + word.length()) + str.substring(wordFirstIndex + word.length());
            getWordAllIndex(str, word, result);
        }
    }
}
