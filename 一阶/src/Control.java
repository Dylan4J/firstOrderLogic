import java.util.ArrayList;

public class Control {
    private RuleDatabase ruleDatabase;
    private Sportsman sportsman;
    ArrayList<String> conclusion = new ArrayList<>();//存放结论

    public Control(RuleDatabase ruleDatabase, Sportsman sportsman) {
        this.ruleDatabase = ruleDatabase;
        this.sportsman = sportsman;
    }
    /*
    向规则库里面添加规则
     */
    public void addRule(String conclusion,String ...primise){
        ruleDatabase.add(conclusion,primise);
    }

    /*
    删除规则库里面的某条规则
     */
    public void deleteRule(String conclusion,String ...primise){
        ruleDatabase.delete(conclusion,primise);
    }
    /*
    显示规则库所有的规则
    */
    public void showAllRules(){
        ruleDatabase.showAll();
    }
    /*
    查询规则库里面的数量
     */
    public int getRuleDatabaseSize(){
        return ruleDatabase.Size();
    }
    /*
   打印用户状态链表
    */
    public void showClient(){
        sportsman.showState();
    }

    /*
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

    /*
    进入规则库开始对比
     */
    public boolean startToCompare(){
        int m = 0;//记录一次比较找到的结论

        for (int i = 0; i < ruleDatabase.Size(); i++) {
            boolean same = true;
            if (ruleDatabase.getRuleSize(i) - 1 > sportsman.getStateSize() - sportsman.basicInfNum) {//状态信息长度与规则前提不一致
                continue;
            } else {
                /*
                一个节点一个节点比较状态信息与规则库前提
                 */
                for (int j = 0; j < ruleDatabase.getRuleSize(i) - 1; j++) {
                    if (ruleDatabase.getRuleNodeAt(ruleDatabase.get(i), j).equals(sportsman.getClientStateNodeAt(j + sportsman.basicInfNum))) {//从基本数据类型之后开始比较
                        continue;
                    } else {
                        same = false;
                        break;
                    }
                }

                if (same) {//！！！！！如果一次比较完发现命中规则！！！！！
                    m++;//找到一条记录加1
                    //1、这里为咨询者的状态添加一个节点
                    sportsman.add(ruleDatabase.getRuleNodeAt(ruleDatabase.get(i), ruleDatabase.getRuleSize(i) - 1));
                    //2、结论库添加一个结论
                    conclusion.add((conclusion.size() + 1) + "、" + ruleDatabase.getRuleConclusionAt(i));
                }
            }

        }
        if (m != 0) {
            System.out.println("匹配到了" + m + "条规则");
            return true;
        } else {
            System.out.println("未匹配到规则");
            return false;
        }

    }

    /*
    打印结论库
     */
    public void showAllConclusion(){
        for (String s : conclusion) {
            System.out.println(s);
        }
    }



}
