import javax.print.attribute.standard.JobKOctets;
import javax.print.attribute.standard.PrinterMessageFromOperator;
import javax.swing.text.StyledEditorKit;
import java.util.ArrayList;

public class RuleDatabase {
    private ArrayList<Rule> database;//使用变长度数组来构建规则库
    private Rule rule;//用于存放当前正在操作的rule
    private int size;

    public RuleDatabase() {
        this.database = new ArrayList<>();
    }

    /*
    通过代码直接添加规则进规则库
    */
    public void add(String conclusion,String ...primise){
        rule = new Rule();
        rule.add(conclusion,primise);
        database.add(rule);
        size = database.size();//更新数据库的大小
    }
    /*
    通过代码直接删除规则库里面的规则
     */
    public void delete(String conclusion,String ...primise){
        Node con = new Node(conclusion);
        Node[] pri = new Node[primise.length];
        for (int i = 0; i < primise.length; i++) {
           pri[i] = new Node(primise[i]);
        }

        boolean notFinded = true;
        for (Rule r : database) {//遍历整个规则库
            boolean detected = true;

            for (int i = 0; i < r.buffer.size()-1; i++) {//遍历一条规则里面的所有前提
                if (r.buffer.get(i).equals(pri[i])) {
                    continue;
                } else {
                    detected = false;
                    break;
                }
            }
            if (detected && r.buffer.get(r.buffer.size()-1).equals(con)) {//追加判断结论是否一致
                database.remove(r);
                notFinded = false;
                break;
            }
        }
        if (notFinded) {
            System.out.println("请检查您的输入条件或结论，规则库无此规则！");
        }
        size = database.size();
    }

    /*
    返回规则库规则数量
     */
    public int Size(){
        return size;
    }

    /*
    打印整个规则库
     */
    public void showAll(){
        for (Rule rule : database) {
            for (int i = 0; i < rule.size(); i++) {
                if (i == rule.size() - 2) {
                    System.out.print(rule.get(i).parameter + "--->");
                } else if (i == rule.buffer.size() - 1) {
                    System.out.print(rule.get(i).parameter);
                } else{
                    System.out.print(rule.get(i).parameter + "and");
                }

            }
            System.out.println();
        }
    }
    /*
    按照次序查询某条规则
     */
    public Rule get(int index){
        return database.get(index);
    }

    /*
    获取某条规则的前提和结论总数
     */
    public int getRuleSize(int index){
        return get(index).buffer.size();
    }

    /*
    获取某一条规则中的某个节点
     */
    public Node getRuleNodeAt(Rule rule,int index){
        return rule.get(index);
    }

    /*
   获取某一次序的规则的结论
     */
    public String getRuleConclusionAt(int index){
        return get(index).getConclusion();
    }

    /*
    内部类rule
     */
    private class Rule {
        ArrayList<Node> buffer;


        public Rule() {
            this.buffer = new ArrayList<>();
        }

        //添加结论与前提
        public void add(String conclusion,String ...primise){
            for (int i = 0; i < primise.length; i++) {
                buffer.add(new Node(primise[i]));
            }
            buffer.add(new Node(conclusion));
        }

        public Node get(int i){
            return buffer.get(i);
        }

        public int size(){
            return buffer.size();
        }
        /*
        获取规则的结论
         */
        public String getConclusion(){
            return buffer.get(buffer.size()-1).parameter;
        }


    }


}
