public class Node {
    String parameter;//参数名
    boolean value;//参数值

    public Node(String parameter) {
        this.parameter = parameter;
        this.value = true;
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
