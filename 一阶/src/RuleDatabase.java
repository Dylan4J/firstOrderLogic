import javax.print.attribute.standard.JobKOctets;
import java.util.ArrayList;

public class RuleDatabase {
    ArrayList<Rule> database;//使用变长度数组来构建规则库
    Rule rule;//用于存放当前正在操作的rule
    private int size;

    public RuleDatabase() {
        this.database = new ArrayList<>();
    }

    /*
        通过代码直接添加规则进规则库
         */
    public void add(Node conclusion,Node ...primise){
        rule = new Rule();
        rule.add(conclusion,primise);
        database.add(rule);
        size = database.size();//更新数据库的大小
    }
    /*
    通过代码直接删除规则库里面的规则
     */
    public void delete(Node conclusion,Node ...primise){
        boolean notFinded = true;
        for (Rule r : database) {//遍历整个规则库
            boolean detected = true;

            for (int i = 0; i < r.buffer.size()-1; i++) {//遍历一条规则里面的所有前提
                if (r.buffer.get(i).equals(primise[i])) {
                    continue;
                } else {
                    detected = false;
                    break;
                }
            }
            if (detected && r.buffer.get(r.buffer.size()-1).equals(conclusion)) {//追加判断结论是否一致
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
    内部类rule
     */
    private class Rule {
        ArrayList<Node> buffer;

        public Rule() {
            this.buffer = new ArrayList<>();
        }

        //添加结论与前提
        public void add(Node conclusion,Node ...primise){
            for (int i = 0; i < primise.length; i++) {
                buffer.add(primise[i]);
            }
            buffer.add(conclusion);
        }
    }


}
