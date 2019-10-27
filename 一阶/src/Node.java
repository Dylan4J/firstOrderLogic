public class Node {
    String parameter;//参数名
    boolean value;//参数值
    String strValue;//字符型参数
    int intValue;//整形参数


    public Node(String parameter) {
        this.parameter = parameter;
        this.value = true;
    }

    public Node(String parameter, String strValue) {
        this.parameter = parameter;
        this.strValue = strValue;
    }

    public Node(String parameter, int intValue) {
        this.parameter = parameter;
        this.intValue = intValue;
    }

    public Node(String parameter, boolean value) {
        this.parameter = parameter;
        this.value = value;
    }

    @Override
    public boolean equals(Object object){
        Node node = (Node) object;
        if (this.parameter == node.parameter && this.value == node.value) {
            return true;
        } else {
            return false;
        }
    }
}
