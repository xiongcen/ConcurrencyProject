package com.xiongcen.interview;

/**
 * 单向链表逆序
 * Created by xiongcen on 16/10/25.
 */
public class LinkList {
    public Node head;
    public void createLinkList(int []x) {   //创建一个链表
        Node pnew; //定义一个新的结点
        Node ptail = new Node();
        head = ptail;
        for(int i = 0; i < x.length; i ++) {
            pnew = new Node();
            pnew.setData(x[i]);
            ptail.setNext(pnew);
            ptail = pnew;
        }
    }
    public void displayLinkList() {  //正序输出链表的所有内容
        Node node =  head.getNext();
        while (node != null) {
            System.out.print(node.getData() + "--->");
            node = node.getNext();
        }
        System.out.println("null");
    }
    public void reverseLinkList() {  //逆序输出链表的所有内容
        if (head == null || head.getNext() == null) {  //当链表只有一个头节点或者只有一个结点，逆序还是原来的链表，所以直接返回
            return;
        } else {
            Node p = head.getNext();
            Node q = head.getNext().getNext();
            p.setNext(null);//将第一个结点的next置为空，否则会出现一个环
            Node temp = null;
            while (q != null) {
                temp = q.getNext();
                q.setNext(p);
                p = q;
                q = temp;
            }
            if (q == null) {
                head.setNext(p);
                q = null;
            }
        }
    }
    public static void main(String[] args) {
        LinkList linkList = new LinkList();
        int n = 10;
        int [] x = new int [n];
        for (int i = 0; i < n; i ++) {
            x[i] = i;
        }
        linkList.createLinkList(x);
        linkList.displayLinkList();
        linkList.reverseLinkList();
        linkList.displayLinkList();
    }

    static class Node {
        private int data;

        private Node next;

        public void setData(int data) {
            this.data = data;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public Node getNext() {
            return next;
        }

        public int getData() {
            return data;
        }
    }
}
