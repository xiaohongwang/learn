package com.learn.algorithm;

import java.util.StringJoiner;

/**
 * 单向链表的头插尾插法
 *         --  每个节点包括 ：数据 、下一个节点
 *
 */

public class InsertIntoList {

    //节点数据
    static class ListNode {
        private Integer val;//数据信息
        public ListNode next;//下个节点

        public ListNode(Integer val){
            this.val = val;
        }
    }

    //链表数据
    static class SingleLinkedList{
        //头节点
        private ListNode head;

        //尾插法
        public void addTail(Integer val){
            ListNode node = new ListNode(val);
            if (head == null){
                head = node;
            }else {
                ListNode tmpHead = head;
                ListNode tmpNode = tmpHead.next;
                while (tmpNode != null){
                    tmpHead = tmpNode;
                    tmpNode = tmpHead.next;
                }
                tmpHead.next = node;
            }
        }

        //头插法
        public void addHead(Integer val){
            ListNode node = new ListNode(val);
            node.next = head;
            head = node;
        }

        //从任意位置插入
        public void addIndex(int index, Integer val){
            //从头部插入
            if (index == 0){
                addHead(val);
                return;
            }

            ListNode node = new ListNode(val);
            int pos = 0;
            ListNode current = head;
            ListNode preNode = head;

            while (pos < index && current != null){
                preNode = current;
                current = current.next;
                pos ++;
            }

            //查到匹配位置
            if (pos == index){
                preNode.next = node;
                node.next = current;
            }else {
                System.out.println("===未找到匹配位置====");
            }
        }

        @Override
        public String toString() {
            StringJoiner stringJoiner = new StringJoiner(",","[","]");
            ListNode node = head;
            while (node != null){
                stringJoiner.add(String.valueOf(node.val));
                node = node.next;
            }
            return stringJoiner.toString();
        }
    }


    public static void main(String[] args) {

        SingleLinkedList singleLinkedList = new SingleLinkedList();


//        for (int i  = 0; i < 5; i++){
//           singleLinkedList.addTail(i);
//        }
//
//
//       ListNode c = singleLinkedList.head;
//        for (int i = 0 ;i < 5 ; i++){
//            System.out.println(c.val);
//            c = c.next;
//        }


//        for (int i  = 0; i < 5; i++){
//            singleLinkedList.addHead(i);
//        }
//
//
//        ListNode c = singleLinkedList.head;
//        for (int i = 0 ;i < 5 ; i++){
//            System.out.println(c.val);
//            c = c.next;
//        }

        singleLinkedList.addIndex(1, 5);

        System.out.println("first" + singleLinkedList);

        //头部
        singleLinkedList.addIndex(0,1);
        System.out.println(singleLinkedList);
        //头部
        singleLinkedList.addIndex(0,2);
        System.out.println(singleLinkedList);

        //尾部
        singleLinkedList.addIndex(2,5);
        System.out.println(singleLinkedList);
        //中间位置
        singleLinkedList.addIndex(1,6);
        System.out.println(singleLinkedList);


    }
}
