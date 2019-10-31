import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Control control = new Control(new RuleDatabase(),new Sportsman());


        control.addRule("选择先增肌或先减脂或先玩耍","可以自由选择");
        control.addRule("适合唱歌","先玩耍");

//               //来自徐赞林的贡献
//        control.addRule("先减脂","偏胖");
//        control.addRule("先减脂","重度肥胖");
//        control.addRule("先增肌","较瘦");
//        control.addRule("选择先增肌或先减脂","可以自由选择");
        control.addRule("可以自由选择","体重合理");
        control.addRule("适合以有氧训练为主","先减脂");
//        control.addRule("适合以器械训练为主","先增肌");
//        control.addRule("有健身史","经常光顾健身房");
//        control.addRule("有健身史false","经常光顾健身房false");
//        control.addRule("有过专业器械训练经历","经常光顾健身房");
//        control.addRule("可以自由选择false","有疾病史");
//        control.addRule("有想锻炼的部位","有疾病史false");
//        control.addRule("适合做高强度器械训练","适合以器械训练为主","有健身史","有过专业器械训练经历","先增肌");
//        control.addRule("适合做一般强度器械训练","适合以器械训练为主","有健身史","有过专业器械训练经历false","先增肌");
//        control.addRule("适合做中强度器械训练","适合以器械训练为主","有健身史","有过专业器械训练经历","先减脂");
//        control.addRule("适合做一般强度器械训练","适合以器械训练为主","有健身史","有过专业器械训练经历false","先减脂");
//        control.addRule("适合做低强度器械训练","适合以器械训练为主","有健身史false");
//        control.addRule("适合做高时长有氧训练","适合以有氧训练为主","有健身史","身体素质较好","先减脂");
//        control.addRule("适合做中时长有氧训练","适合以有氧训练为主","有健身史false","身体素质较好","先减脂");
//        control.addRule("适合做一般时长有氧训练","适合以有氧训练为主","有健身史","身体素质一般","先减脂");
//        control.addRule("适合做低时长有氧训练","适合以有氧训练为主","有健身史false","身体素质一般","先减脂");
//        control.addRule("适合做一般时长有氧训练","适合以器械训练为主","有健身史false","先减脂");
//        control.addRule("适合做中时长有氧训练","适合以器械训练为主","有健身史","先减脂");
//        control.addRule("适合做一般时长有氧训练","适合以器械训练为主","有健身史","先增肌");
//
//
//        //来自方磊的贡献
//        control.addRule("适合轻量长时间无氧运动和短时间有氧运动时间比为7:3","偏胖","有健身史false","先增肌");
//        control.addRule("适合轻量长时间无氧运动和短时间有氧运动时间比为4:6","偏胖","有健身史false","先减脂");
//        control.addRule("适合极限量长时间无氧运动和短时间有氧运动时间比为7:3","偏胖","有健身史","先增肌");
//        control.addRule("适合极限量长时间无氧运动和短时间有氧运动时间比为4:6","偏胖","有健身史","先减脂");
//        control.addRule("适合中等重量长时间无氧运动和较长时间有氧运动时间比为5:5，并降低高热量食物的摄入，多摄入饱腹感强，热量低的食物","重度肥胖","有健身史false","先减脂");
//        control.addRule("适合极限量长时间无氧运动和长时间有氧运动时间比为5:5，并降低高热量食物的摄入，多摄入饱腹感强，热量低的食物","重度肥胖","有健身史","先减脂");
//        control.addRule("适合慢跑、瑜伽等轻量运动","偏胖","有健身史","先增肌","有疾病史");
//        control.addRule("适合慢跑、瑜伽等轻量运动","偏胖","有健身史false","先增肌","有疾病史");
//        control.addRule("适合慢跑、瑜伽等轻量运动，并降低高热量食物的摄入，多摄入饱腹感强，热量低的食物","偏胖","有健身史false","先减脂","有疾病史");
//        control.addRule("适合慢跑、瑜伽等轻量运动，并降低高热量食物的摄入，多摄入饱腹感强，热量低的食物","重度肥胖","有健身史false","先减脂","有疾病史");
//
//        //来自张杰的贡献
//        control.addRule("适合练习哑铃卧推","适合以器械训练为主","有健身史","先增肌","有想锻炼部位","胸部肌肉");
//        control.addRule("适合练习平地俯卧撑","适合以有氧训练为主","有健身史false","先增肌","有想锻炼部位","胸部肌肉");
//        control.addRule("适合练习腹部轮训练","适合以器械训练为主","有健身史","先增肌","有想锻炼部位","腹部肌肉");
//        control.addRule("适合练习仰卧起坐","适合以有氧训练为主","有健身史false","先增肌","有想锻炼部位","腹部肌肉");
//        control.addRule("适合练习杠铃划船","适合以器械训练为主","有健身史","先增肌","有想锻炼部位","背部肌肉");
//        control.addRule("适合练习引体向上","适合以有氧训练为主","有健身史false","先增肌","有想锻炼部位","背部肌肉");
//        control.addRule("适合练习哑铃斜托弯举","适合以器械训练为主","有健身史","先增肌","有想锻炼部位","臂部肌肉");
//        control.addRule("适合练习平地俯卧撑","适合以有氧训练为主","有健身史false","先增肌","有想锻炼部位","臂部肌肉");
//        control.addRule("适合练习杠铃深蹲","适合以器械训练为主","有健身史","先增肌","有想锻炼部位","腿部肌肉");
//        control.addRule("适合练习无负重深蹲","适合以有氧训练为主","有健身史false","先增肌","有想锻炼部位","腿部肌肉");
//        control.addRule("适合练习T型杠硬拉","适合以器械训练为主","有健身史","先增肌","有想锻炼部位","臀部肌肉");
//        control.addRule("适合练习跪蹲","适合以有氧训练为主","有健身史false","先增肌","有想锻炼部位","臀部肌肉");
//        control.addRule("适合练习哑铃前平举","适合以器械训练为主","有健身史","先增肌","有想锻炼部位","肩部肌肉");
//        control.addRule("适合练习平地俯卧撑","适合以有氧训练为主","有健身史false","先增肌","有想锻炼部位","肩部肌肉");







        control.showAllRules();
        System.out.println(control.getRuleDatabaseSize());


        System.out.println("尊敬的先生/女士，欢迎您使用keep健身教练！\n接下来我会问您几个问题，为了帮助您获取更为准确的健身计划" +
                            "请您提供真实的信息，谢谢您的配合！");
        System.out.println("请输入您的姓名、年龄、身高、体重：（格式  xxx--23--178--70）");
        collectBasicInf(control);//采集用户基本信息
        control.ask("您是否经常光顾健身房呢（请输入y/n）");
        control.ask("您是否有疾病史呢（请输入y/n）");

        control.startCompare();
        control.showAllConclusion();
        System.out.println("----------------");
        control.showClientState();


    }

    /**
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
        //计算用户的bmi指数
        double bmi =  temp[2] / Math.pow(temp[1]/100.0, 2);
        String str = "";
        if (bmi < 18.5) {
            str = "较瘦";
        } else if (bmi < 23.9) {
            str = "体重合理";
        } else if (bmi < 26.9) {
            str = "偏胖";
        } else {
            str = "重度肥胖";
        }
        control.addClientState("姓名",strings[0]);
        control.addClientState("年龄",temp[0]);
        control.addClientState("身高",temp[1]);
        control.addClientState("体重",temp[2]);
        control.addClientState(str,true);
    }




}

