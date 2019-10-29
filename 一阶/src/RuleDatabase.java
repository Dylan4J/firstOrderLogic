import javax.print.attribute.standard.JobKOctets;
import javax.print.attribute.standard.PrinterMessageFromOperator;
import javax.swing.text.StyledEditorKit;
import java.util.ArrayList;

public class RuleDatabase {
    private ArrayList<Rule> database;//使用变长度数组来构建规则库
    private Rule rule = new Rule();//用于存放当前正在操作的rule
    private int size;

    public RuleDatabase() {
        this.database = new ArrayList<>();
    }

    public ArrayList<Rule> getDatabase() {
        return database;
    }

    /**
    通过代码直接添加规则进规则库
    */
    public void add(String conclusion,String ...primise){
        Rule rule = new Rule();
        rule.add(conclusion, primise);
        if (Size() != 0) {//如果数据库不为空，则需要进行重复性检查
            if (!isRuleRepeat(rule)) {
                database.add(rule);
                size = database.size();//更新数据库的大小
            }else {
                try {
                    throw new Exception("规则库已存在此规则");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("捕获到重复异常");

                }
            }
        }else {
            database.add(rule);
            size = database.size();//更新数据库的大小
        }
    }

    /**
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

    /**
    返回规则库规则数量
     */
    public int Size(){
        return size;
    }

    /**
    打印整个规则库
     */
    public void showAll(){
        for (Rule rule : database) {
            for (int i = 0; i < rule.size(); i++) {
                if (i == rule.size() - 2) {
                    System.out.print(rule.get(i).parameter + "("+rule.get(i).value+")" + "--->");
                } else if (i == rule.buffer.size() - 1) {
                    System.out.print(rule.get(i).parameter + "("+rule.get(i).value+")");
                } else{
                    System.out.print(rule.get(i).parameter + "("+rule.get(i).value+")" + " AND ");
                }

            }
            System.out.println();
        }
    }

    /**
    按照次序查询某条规则
     */
    public Rule get(int index){
        return database.get(index);
    }

    /**
    获取某条规则的前提和结论总数
     */
    public int getRuleSize(int index){
        return get(index).buffer.size();
    }

    /**
    获取某一条规则中的某个节点
     */
    public Node getRuleNodeAt(Rule rule,int index){
        return rule.get(index);
    }

    /**
   获取某一次序的规则的结论
     */
    public String getRuleConclusionAt(int index){
        return get(index).getConclusion();
    }

    /**
    比较输入规则是否已经在规则库中
    */
    public boolean isRuleRepeat(Rule rule){
        boolean repeat;
        for (Rule r : database) {
            if (r.equals(rule)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取某条规则的最后一个节点
     */
    public Node getConclusionNode(int index){
        return get(index).getLastNode();
    }


    /**
    内部类rule
     */
    private class Rule {
        ArrayList<Node> buffer;


        private Rule() {
            this.buffer = new ArrayList<>();
        }


        /**
         拆分前提和结论里面的true和false字眼
         */
        private String split(String string,String s){
            String str = new String();
            str = string.substring(0,string.indexOf(s));
            return str;
        }

        /**
         * 添加结论与前提
         */
        private void add(String conclusion,String ...primise){
            for (int i = 0; i < primise.length; i++) {
                //规则添加前提节点之前先检查是否有“true”/“false”字样
                if (primise[i].contains("false")) {
                    String q = split(primise[i],"f");
                    buffer.add(new Node(q,false));
                } else if (primise[i].contains("true")) {
                    String q = split(primise[i], "t");
                    buffer.add(new Node(q, true));
                } else {
                    buffer.add(new Node(primise[i]));
                }
            }

            //添加结论前检查是否有“true”/“false”字样
            if (conclusion.contains("false")) {
                String q = split(conclusion,"f");
                buffer.add(new Node(q,false));
            } else if (conclusion.contains("true")) {
                String q = split(conclusion, "t");
                buffer.add(new Node(q, true));
            } else {
                buffer.add(new Node(conclusion));
            }
        }

        private Node get(int i){
            return buffer.get(i);
        }

        private int size(){
            return buffer.size();
        }
        /*
        获取规则的结论
         */
        private String getConclusion(){
            return buffer.get(buffer.size()-1).parameter;
        }
        /*
        获取某条规则的最后一个节点
         */
        private Node getLastNode(){
            return buffer.get(buffer.size()-1);
        }

        @Override
        public boolean equals(Object rule) {//这里this指代的是数据库里面的某条规则，形参rule传入的是正在新添加的规则
            boolean same = true;
            if (this.size() != ((Rule)rule).size()) {//先判断规则长度是否相等
                same = false;
            }else {
                int t = 0;
                for (int i = 0; i < ((Rule) rule).buffer.size()-1; i++) {
                    for (int j = 0; j < this.buffer.size()-1; j++) {
                        if (((Rule) rule).buffer.get(i).equals(this.buffer.get(j))) {
                            t++;
                            break;
                        }
                    }
                }
                if (t != ((Rule) rule).size() - 1) {
                    same = false;
                } else if (!((Rule) rule).buffer.get(((Rule) rule).buffer.size() - 1).equals(this.buffer.get(this.buffer.size() - 1))){//检查正在添加的规则的结论与数据库里面的规则的结论是否一致
                    same = false;
                }
            }
            return same;
        }
    }




}
