import jdk.swing.interop.LightweightContentWrapper;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

       RuleDatabase ruleDatabase = new RuleDatabase();
       ruleDatabase.add(new Node("鱼"),new Node("会游泳"),new Node("很好吃"));
       ruleDatabase.add(new Node("狗"),new Node("会游泳"),new Node("很好吃"));
       ruleDatabase.add(new Node("猫"),new Node("会游泳"),new Node("很好吃"));
       ruleDatabase.add(new Node("老虎"),new Node("会游泳"),new Node("很好吃"));
       ruleDatabase.delete(new Node("老虎"),new Node("会游泳"),new Node("很好吃"));

        System.out.println("");
    }
    
}

