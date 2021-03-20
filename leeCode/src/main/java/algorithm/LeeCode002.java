package algorithm;

import java.math.BigDecimal;

public class LeeCode002 {

    public static void main(String[] args) {
        ListNode node11 = new ListNode(3);
        ListNode node12 = new ListNode(4, node11);
        ListNode node13 = new ListNode(2, node12);

        ListNode node21 = new ListNode(4);
        ListNode node22 = new ListNode(6, node21);
        ListNode node23 = new ListNode(5, node22);

        addTwoNumbers(node13,node23);
    }

    /*//官方答案
     public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode head = null, tail = null;
        int carry = 0;
        while (l1 != null || l2 != null) {
            int n1 = l1 != null ? l1.val : 0;
            int n2 = l2 != null ? l2.val : 0;
            int sum = n1 + n2 + carry;
            if (head == null) {
                head = tail = new ListNode(sum % 10);
            } else {
                tail.next = new ListNode(sum % 10);
                tail = tail.next;
            }
            carry = sum / 10;
            if (l1 != null) {
                l1 = l1.next;
            }
            if (l2 != null) {
                l2 = l2.next;
            }
        }
        if (carry > 0) {
            tail.next = new ListNode(carry);
        }
        return head;
    }
     */
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        StringBuffer sb1 = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();
        while(l1 != null){
            sb1.append(l1.val);
            l1 = l1.next;
        }
        while(l2 != null){
            sb2.append(l2.val);
            l2 = l2.next;
        }
        BigDecimal bd1 = new BigDecimal(sb1.reverse().toString());
        BigDecimal bd2 = new BigDecimal(sb2.reverse().toString());
        BigDecimal bd3 = bd1.add(bd2);
        char[] value = bd3.toString().toCharArray();
        //ListNode start = new ListNode(value[0]);
        ListNode next = new ListNode(Integer.parseInt(value[0]+""));
        for (int i = 1 ; i <= value.length-1 ; i++){
            next = new ListNode(Integer.parseInt(value[i]+""),next);
        }
        return next;
    }
}


class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) {
        this.val = val;
    }
    ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}