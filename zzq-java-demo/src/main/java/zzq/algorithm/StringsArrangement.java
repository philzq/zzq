package zzq.algorithm;

/**
 *
 * 字符串的排列
 *
 * 题目：输入一个字符串，打印出该字符串中字符的所有排列。
 *
 * 例如输入字符串abc，则输出由字符a、b、c 所能排列出来的所有字符串abc、acb、bac、bca、cab 和cba。
 */
public class StringsArrangement {

    /**
     * 输入一个字符串，打印出该字符串中字符的所有排列
     * @param string
     * @return
     */
    public static void stringsArrangement(String string){
        getStringsArrangement(string.toCharArray(),0);
    }

    private static void getStringsArrangement(char[] str, int i){
        if (i >= str.length)
            return;
        if (i == str.length - 1) {
            System.out.println(String.valueOf(str));
        } else {
            for (int j = i; j < str.length; j++) {
                char temp = str[j];
                str[j] = str[i];
                str[i] = temp;

                getStringsArrangement(str, i + 1);

                temp = str[j];
                str[j] = str[i];
                str[i] = temp;
            }
        }

    }

    public static void main(String[] args){
        long start = System.currentTimeMillis();
        stringsArrangement("poiuytre");
        long end = System.currentTimeMillis();
        System.out.println("耗时:"+(end-start)+"毫秒");
    }
}
