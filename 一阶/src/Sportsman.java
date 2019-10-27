import java.text.NumberFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class Sportsman {
    private LinkedList<Node> client = new LinkedList();//创建单链表用于存储客户信息
    public static final int basicInfNum = 4;//状态信息里面存储的基本数据数量

    public void add(String str,String value){
        Node node = new Node(str,value);
        client.add(node);
    }

    public void add(String str,int value){
        Node node = new Node(str,value);
        client.add(node);
    }

    public void add(String str,boolean value){
        Node node = new Node(str,value);
        client.add(node);
    }

    public void add(Node node){
        client.add(node);
    }
    /*
    打印用户状态链表
     */

    public void showState(){
        Iterator<Node> iterator = client.iterator();//遍历整个用户状态链表
        while (iterator.hasNext()) {
            Node node =iterator.next();
            System.out.print(node.parameter + "   ");
            System.out.print(node.value + "   ");
            if (node.strValue != null) {
                System.out.print(node.strValue+ "   ");
            }
            if (node.intValue != 0) {
                System.out.print(node.intValue+ "   ");
            }
            System.out.println("");
        }
    }
    /*
    返回用户当前状态的大小
     */
    public int getStateSize(){
        return client.size();
    }
    /*
    获取咨询者状态处于某一次序的状态信息
     */
    public Node getClientStateNodeAt(int index){
        return client.get(index);
    }


}
