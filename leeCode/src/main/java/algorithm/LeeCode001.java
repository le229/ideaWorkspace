package algorithm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * LeeCode第一题，两数之和
 */
public class LeeCode001 {

    public static int[] twoSum(int[] nums, int target) {
        Map<Integer,Integer> hmap = new HashMap();
        hmap.put(nums[0],0);
        for(int i = 1; i < nums.length; i++){
            if(hmap.containsKey(target - nums[i])){
                return new int[]{i , hmap.get(target - nums[i])};
            }
            hmap.put(nums[i],i);
        }
        return nums;
    }

    public static void main(String[] args) {
        int[] a = {2,7,14,35};
        System.out.println(twoSum(a,9));
    }
}
