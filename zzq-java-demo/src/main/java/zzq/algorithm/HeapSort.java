package zzq.algorithm;

/**
 * 堆排序（Heapsort）是指利用堆这种数据结构所设计的一种排序算法。堆积是一个近似完全二叉树的结构，并同时满足堆积的性质：即子结点的键值或索引总是小于（或者大于）它的父节点。
 *
 * 堆排序的平均时间复杂度为Ο(nlogn) 。
 *
 * 算法步骤：
 *
 * 创建一个堆H[0..n-1]
 *
 * 把堆首（最大值）和堆尾互换
 *
 * 3. 把堆的尺寸缩小1，并调用shift_down(0),目的是把新的数组顶端数据调整到相应位置
 *
 * 4. 重复步骤2，直到堆的尺寸为1
 */
public class HeapSort {
}