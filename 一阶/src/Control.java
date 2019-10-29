import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Scanner;

@RunWith(Parameterized.class)
public class Control {
    private RuleDatabase ruleDatabase;
    private Sportsman sportsman;
    ArrayList<String> conclusion = new ArrayList<>();//存放结论

    public Control(RuleDatabase ruleDatabase, Sportsman sportsman) {
        this.ruleDatabase = ruleDatabase;
        this.sportsman = sportsman;
    }
    /**
    向规则库里面添加规则
     */
    public void addRule(String conclusion,String ...primise){
        ruleDatabase.add(conclusion,primise);
    }

    /**
    删除规则库里面的某条规则
     */
    public void deleteRule(String conclusion,String ...primise){
        ruleDatabase.delete(conclusion,primise);
    }

    /**
    显示规则库所有的规则
    */
    public void showAllRules(){
        ruleDatabase.showAll();
    }

    /**
    查询规则库里面的数量
     */
    public int getRuleDatabaseSize(){
        return ruleDatabase.Size();
    }

    /**
   打印用户状态链表
    */
    public void showClientState(){
        sportsman.showState();
    }

    /**
    添加用户状态
     */
    public void addClientState(String str,String value){
        sportsman.add(str,value);
    }

    public void addClientState(String str,int value){
        sportsman.add(str,value);
    }

    public void addClientState(String str,boolean value){
        sportsman.add(str, value);
    }
    public void addClientState(Node node){
        sportsman.add(node);
    }



    /**
    查询用户状态节点数量
     */
    public int getClientStateSize(){
        return sportsman.getStateSize();
    }

    /**
    进入规则库开始对比
     */
    public int[] compareToRuleDatabase(){
        int[] result = new int[2];//第一个存放一次扫描匹配到的结论数；第二个存放一次扫描过程中咨询者的状态有没有发生改变
        int m = 0;//记录一次比较找到的结论数量
        int n = 0;//一次扫描过程中状态被修改的次数；
        for (int i = 0; i < ruleDatabase.Size(); i++) {//遍历整个规则数据库
            int t = 0;//记录一条规则匹配过程中相同的节点数
            boolean same = true;
            String currentConlusion = ruleDatabase.getRuleConclusionAt(i);//当前规则的结论
            Node currentConNode = ruleDatabase.getConclusionNode(i);
            if (ruleDatabase.getRuleSize(i) - 1 > sportsman.getStateSize() - sportsman.basicInfNum) {//状态信息长度小于规则前提则跳过此条规则的检查
                continue;
            } else {
                /*
                一个节点一个节点比较状态信息与规则库前提
                 */
                for (int j = 0; j < sportsman.getStateSize()-sportsman.basicInfNum; j++) {//遍历咨询者的状态节点
                    if (t == ruleDatabase.getRuleSize(i) - 1) {
                        break;
                    }
                    Node tempNode = sportsman.getStateNodeAt(j + sportsman.basicInfNum);
                    for (int k = 0; k < ruleDatabase.getRuleSize(i) - 1; k++) {//遍历当前对比的规则前提
                        if (ruleDatabase.getRuleNodeAt(ruleDatabase.get(i), k).equals(tempNode)) {//从基本数据类型之后开始比较
                            t++;
                            break;
                        }
                    }
                }

                if (t < ruleDatabase.getRuleSize(i) - 1) {//匹配到的节点数小于当前规则的前提数
                    same = false;
                }

                if (same) {//！！！！！如果一次比较完发现命中规则！！！！！
                    m++;//找到一条记录加1
                    boolean parameterSame = false;
                    boolean valueSame = false;
                    for (int j = 0; j < sportsman.getStateSize(); j++) {
                        if (sportsman.getStateNodeAt(j).equalsParameter(ruleDatabase.getConclusionNode(i))) {//先判断节点参数名是否一样，这里是从状态节点0开始跑得，需要修改
                            parameterSame = true;
                            if (sportsman.getStateNodeAt(j).equalsValue(ruleDatabase.getConclusionNode(i))) {//再判断节点参数值是否一样
                                valueSame = true;
                                break;
                            } else {//参数名一样，但是参数值不一样
                                //1、修改参数值
                                sportsman.getStateNodeAt(j).value = currentConNode.value;
                                //2、修改结论
                                n = 1;
                                String conAfter = new String(currentConlusion);
                                conclusion.set(conclusion.indexOf(conAfter),conAfter + "(false)");
                                break;
                            }

                        }

                    }
                    if (!(parameterSame||valueSame)) {//参数名和参数值都不一样
                        Node newNode = currentConNode;//新添加的状态节点
                        String newConclusion = currentConlusion;//新添加的结论
                        //1、这里为咨询者的状态添加一个节点
                        sportsman.add(newNode);
                        //2、如果结论里面包含“选择”字眼，则咨询一次，结论库中不添加此类结论
                        if (newConclusion.indexOf("选") == 0 && newConclusion.contains("或")) {
                            int a = newConclusion.indexOf("择") + 1;
                            int b = newConclusion.indexOf("或");
                            int c = newConclusion.length();
                            String j = newConclusion.substring(a, b);
                            String k = newConclusion.substring(b + 1, c);
                            ask("请问您是要选择" + j + "还是" + k + "呢(选择前者请输入y，选择后者请输入n)");
                        } else  {
                            //3、如果结论中不含有“选择”字眼，结论库添加此结论
                            conclusion.add(currentConlusion);
                        }
                    }

                }
            }

        }
        result[0] = m;
        result[1] = n;
        return result;

    }
    /**
     * 每次修改咨询者状态信息之后循环对比数据库
     */
    public void startCompare(){
        int temp = 0;//记录每次查询过后的匹配到的规则数
        int temp1 = 0;//每次查询过后状态是否被修改过
        int total = 0;//记录总的匹配到的规则数
        while (true) {
            total = temp;
            temp = compareToRuleDatabase()[0];
            temp1 = compareToRuleDatabase()[1];
            if (total < temp) {//客户状态添加了节点

            } else {
                if (temp1 == 1) {//客户状态节点被修改了

                } else {//没产生更新就退出循环扫描
                    System.out.println("共匹配到"+total+"条规则");
                    break;
                }
            }

        }
    }

    /**
    打印结论库
     */
    public void showAllConclusion(){
        System.out.println("结论如下：");
        int k = 0;
        for (int i = 0; i < conclusion.size(); i++) {
            String str = conclusion.get(i);
            if (str.contains("适合")) {
                k++;
                System.out.println(k + "、" + str);
            }
        }
        for (String s : conclusion) {

        }
    }

    /**
     *与咨询者交互的时候询问信息
     * @param string 系统问的话
     */
    public  void getInf(String string) {
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        int a = 0;
        int b = 0;
        int c = 0;
        /*
        系统询问的方式有两种，一种为：“您是否xxx呢”
        另外一种为：“您是要选择xxx还是xxx呢”
         */
        if (string.contains("您是否")) {
            a = string.indexOf("否") + 1;
            b = string.indexOf("呢");

            if (s.equals("y")) {
                this.addClientState(string.substring(a,b), true);
            } else if (s.equals("n")) {
                this.addClientState(string.substring(a,b), false);
            } else {
                try {
                    throw new Exception("输入有误！");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("您输入有误，请重新输入！");
                }
            }

        } else if (string.contains("您是要选择")) {
            a = string.indexOf("择") + 1;
            b = string.indexOf("呢");
            c = string.indexOf("还");

            if (s.equals("y")) {
                this.addClientState(string.substring(a, c), true);
            } else if (s.equals("n")) {
                this.addClientState(string.substring(c + 2, b), false);
            } else {
                try {
                    throw new Exception("输入有误！");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("您输入有误，请重新输入！");
                }
            }
        }


    }

    /**
     * 与咨询者交互的时候询问信息
     * @param string 系统问的话
     */
    public void ask(String string) {
        System.out.println(string);
        getInf(string);
    }



}
