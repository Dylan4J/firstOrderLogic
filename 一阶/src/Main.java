import jdk.swing.interop.LightweightContentWrapper;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Control control = new Control(new RuleDatabase(),new Sportsman());


        control.addRule("适合增肌","很瘦");
        control.addRule("适合增肌","很瘦","体脂率较低");
        control.addRule("适合增肌","很瘦","体脂率较低");
        control.addRule("适合增肌","很瘦","体脂率较低");
        control.addRule("适合大负载训练","很瘦","体脂率较低","适合增肌");
        control.addRule("适合大负载训练","很瘦","体脂率较低","适合增肌");
        control.addRule("适合大负载训练","很瘦","体脂率较低","适合增肌");

        control.showAllRules();
        System.out.println(control.getRuleDatabaseSize());

        System.out.println("尊敬的先生/女士，欢迎您使用keep健身教练！\n接下来我会问您几个问题，为了帮助您获取更为准确的健身计划" +
                            "请您提供真实的信息，谢谢您的配合！");
        System.out.println("请输入您的姓名、年龄、身高、体重：（格式  xxx--23--178--70）");
        collectBasicInf(control);//采集用户基本信息

        //下面的代码用于测试
        control.addClientState("很瘦",true);
        control.addClientState("体脂率较低",true);
        control.startToCompare();
        control.showAllConclusion();
        System.out.println("----------------");
        control.showClient();


    }

    /*
    采集用户基本信息
     */
    public static void collectBasicInf(Control control){
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        String[] strings = s.split("--");
        int[] temp = new int[strings.length-1];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = Integer.parseInt(strings[i+1]);
        }
        control.addClientState("姓名",strings[0]);
        control.addClientState("年龄",temp[0]);
        control.addClientState("身高",temp[1]);
        control.addClientState("体重",temp[2]);
    }


}

