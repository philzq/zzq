package zzq.algorithm;

/**
 * 给定一个整数 n，计算所有小于等于 n 的非负整数中数字 1 出现的个数。
 *
 * 示例:
 *
 * 输入: 13
 * 输出: 6
 * 解释: 数字 1 出现在以下数字中: 1, 10, 11, 12, 13 。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/number-of-digit-one
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class CountOne {

    public int countDigitOne(int n) {
        if(n<=0){
            return 0;
        }
        if(n<10){
            return 1;
        }
        int length = (n + "").length();
        int[] sumNum = new int[length];

        //初始化sumNum
        getNum(length,sumNum);

        return getSum(n,sumNum);
    }

    /**
     * 递归获取结果
     * @param n
     * @param sumNum
     * @return
     */
    private int getSum(int n, int[] sumNum) {
        int length = (n + "").length();
        int count = sumNum[length-2];

        int first = pow(length-1);
        int high = div(n);
        int nextN = removeHigh(n,length);
        if(high>1){
            count += sumNum[length-2]*(high-1)+first;
        }else{
            if(high==1){
                count += nextN+1;
            }
        }
        if(nextN<10){
            if(nextN>0){
                count += 1;
            }
            return count;
        }else{
            return count + getSum(nextN,sumNum);
        }
    }

    /**
     * 记录每位数1的个数，个位数1个，十位数20个...，初始化sumNum
     * @param length
     */
    private void getNum(int length,int[] sumNum) {
        int k = 1;
        for (int i = 0; i < length; i++) {
            if(i!=0){
                k = (k * 10) + pow(i);
            }
            sumNum[i] = k;
        }
    }

    /**
     * n个10相乘
     * @param n
     * @return
     */
    public int pow(int n) {
        int count = 1;
        while (n > 0) {
            count *= 10;
            n--;
        }
        return count;
    }

    /**
     * 获取n的高位
     * @param n
     * @return
     */
    public int div(int n){
        while (n>=10){
            n /= 10;
        }
        return n;
    }

    /**
     * 去掉n的高位
     * @param n
     * @return
     */
    public int removeHigh(int n,int length){
        int pow = pow(length - 1);
        return n%pow;
    }



    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        System.out.println(new CountOne().countDigitOne(222222222));
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
