package zzq.algorithm;

import java.util.Arrays;

/**
 * 调整数组顺序使奇数位于偶数前面
 *
 * 题目：输入一个整数数组，调整数组中数字的顺序，使得所有奇数位于数组的前半部分，
 * 所有偶数位于数组的后半部分。要求时间复杂度为O(n)。
 */
public class ReorderArray {

    public static void reorderArray(int[] ints){
        for(int i=0,j=ints.length-1;i<j;){
            if (ints[i]%2==0){
                if(ints[i]%2==0 && ints[j]%2==1){
                    int term = ints[i];
                    ints[i] = ints[j];
                    ints[j] = term;
                    i++;
                    j--;
                }else{
                    j--;
                }
            }else{
                i++;
            }
        }
    }
    public static void main(String[] args){
        long start = System.currentTimeMillis();
        int[] ints = {5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29};
        reorderArray(ints);
        System.out.println(Arrays.toString(ints));
        long end = System.currentTimeMillis();
        System.out.println("耗时:"+(end-start)+"毫秒");
    }
}
