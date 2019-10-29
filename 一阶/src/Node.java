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

    /**
     * 这个函数是用来比较节点的参数名称是否一样的
     * @param object 需要进行比较的节点
     * @return
     */
    public boolean equalsParameter(Object object) {
        Node node = (Node) object;
        if (this.parameter.equals(node.parameter)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 这个函数是用来比较节点的参数值是否一样的
     * @param object 需要进行比较的节点
     * @return
     */
    public boolean equalsValue(Object object) {
        Node node = (Node) object;
        if (this.value == node.value) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public boolean equals(Object object){
        Node node = (Node) object;
        if (equalsParameter(node) && equalsValue(node)) {
            return true;
        } else {
            return false;
        }
    }
}
