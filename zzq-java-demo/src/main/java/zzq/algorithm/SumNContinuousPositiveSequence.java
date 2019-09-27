package zzq.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * 和为n 连续正数序列
 * 输入一个正数n，输出所有和为n 连续正数序列
 */
public class SumNContinuousPositiveSequence {

    /**
     * 和为n 连续正数序列
     * @param n
     * @return
     */
    public static List<List<Integer>> sumNContinuousPositiveSequence(int n){
        List<List<Integer>> result = new ArrayList<>();
        int end = n%2==0?n/2:n/2+1;
        while(end>0){
            List<Integer> integers = sumN(end, n);
            if(integers!=null && !integers.isEmpty()){
                result.add(integers);
            }
            end--;
        }
        return result;
    }

    /**
     * 从end开始向前求和，得出N则返回，得不出则返回null
     * @param end
     * @param n
     * @return
     */
    private static List<Integer> sumN(int end,int n){
        int sum = 0;
        int beginEnd = end;
        while(beginEnd>0 && sum<n){
            sum += beginEnd;
            if(sum == n){
                List<Integer> resultList = new ArrayList<>();
                for(int i=beginEnd;i<=end;i++){
                    resultList.add(i);
                }
                return resultList;
            }
            beginEnd--;
        }
        return null;
    }

    public static void main(String[] args){
        long start = System.currentTimeMillis();
        System.out.println(sumNContinuousPositiveSequence(100000000).toString());
        long end = System.currentTimeMillis();
        System.out.println("耗时:"+(end-start)+"毫秒");
    }
}
