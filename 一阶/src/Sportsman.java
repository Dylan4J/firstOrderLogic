import java.text.NumberFormat;
import java.util.Scanner;

public class Sportsman {
    private String name;
    private int height;//身高
    private int  weight;//体重
    private int age;//年龄
    private double BMI;//BMI指数
    private String bodyState;//身体情况
    private boolean muscleGain;//是否需要增肌
    private boolean fatLoss;//是否需要减脂
    private boolean bodyStandard;//身材标准
    private boolean medicalHistory;//医疗健身历史
    private boolean sportsHistory;//健身历史
    private boolean professionaSportsHistory;//健身历史
    private boolean exerciseAccess;//训练许可
    private boolean intensity;//训练强度


    private String advice01;
    private String advice02;
    private String advice03;
    private String advice04;
    private String advice05;


    public Sportsman(String name) {
        this.name = name;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setBodyState(String bodyState) {
        this.bodyState = bodyState;
    }
    public void setMuscleGain(boolean muscleGain) {
        this.muscleGain = muscleGain;
    }

    public void setFatLoss(boolean fatLoss) {
        this.fatLoss = fatLoss;
    }

    public void setBodyStandard(boolean bodyStandard) {
        this.bodyStandard = bodyStandard;
    }

    public void setSportsHistory(boolean sportsHistory) {
        this.sportsHistory = sportsHistory;
    }

    public void setMedicalHistory(boolean medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public void setExerciseAccess(boolean exerciseAccess) {
        this.exerciseAccess = exerciseAccess;
    }

    public void setProfessionaSportsHistory(boolean professionaSportsHistory) {
        this.professionaSportsHistory = professionaSportsHistory;
    }

    public void setIntensity(boolean intensity) {
        this.intensity = intensity;
    }

    public String getName() {
        return name;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public int getAge() {
        return age;
    }

    public double getBMI() {
        return BMI;
    }

    public String getBodyState() {
        return bodyState;
    }

    public boolean isMuscleGain() {
        return muscleGain;
    }

    public boolean isFatLoss() {
        return fatLoss;
    }

    public boolean isBodyStandard() {
        return bodyStandard;
    }

    public boolean isMedicalHistory() {
        return medicalHistory;
    }

    public boolean isSportsHistory() {
        return sportsHistory;
    }

    public boolean isProfessionaSportsHistory() {
        return professionaSportsHistory;
    }

    public boolean isExerciseAccess() {
        return exerciseAccess;
    }

    public boolean isIntensity() {
        return intensity;
    }

    public String getAdvice01() {
        return advice01;
    }

    public String getAdvice02() {
        return advice02;
    }

    public void getBodyData(){
        System.out.println("请告诉我你的身高体重和年龄（格式：178--49--29）：");
        Scanner s = new Scanner(System.in);
        String body = s.nextLine();
        String[] BODY = body.split("--");
        int[] iBIM = new int[3];
        for (int i = 0; i < 3; i++) {
            iBIM[i] = Integer.parseInt(BODY[i]);
        }
        setHeight(iBIM[0]);
        setWeight(iBIM[1]);
        setAge(iBIM[2]);
    }
    public double getBmiNumber(){
        getBodyData();
        BMI = weight/Math.pow(height/100.0,2);
        setBodyCondition(BMI);
        return BMI;
    }
    public void setBodyCondition(double BMI){
        if (BMI < 18.5) {
            setBodyState("偏瘦");
            setMuscleGain(true);
        } else if (BMI < 23.9) {
            setBodyState("正常");
            setBodyStandard(true);
        } else if (BMI < 26.9) {
            setBodyState("偏胖");
            setFatLoss(true);
        } else if (BMI < 29.9) {
            setBodyState("肥胖");
            setFatLoss(true);
        }else{
            setBodyState("重度肥胖");
            setFatLoss(true);
        }
    }
    public void advice1(){
        NumberFormat num = NumberFormat.getNumberInstance();//取BMI指数后四位小数
        num.setMaximumFractionDigits(4);
        if (muscleGain) {
            advice01 = "建议您采取适当训练增肌";
        }else if (fatLoss){
            advice01 = "建议您采取适当训练减脂";
        } else if (bodyStandard) {
            advice01 = "状态很好，建议您继续保持身材";
        }
        System.out.println("您好，根据您所提供的数据，您的BMI指数为" + num.format(BMI) + "，属于" + bodyState + "体形");
        System.out.println("这边给您的建议为：" + advice01);
    }

    public void getMedicalHistory(){
        System.out.println("");
        System.out.println("接下来我们将会向您获取一些信息，为了更好的为您制定健身方案，希望能您能填写更加真实的信息，感谢您的配合！");
        System.out.println("请问您过去是否有重大疾病史?(请输入 是或否)");
        Scanner s = new Scanner(System.in);
        String feedBack = s.nextLine();
        if (feedBack.equals("是")) {
            setMedicalHistory(true);
            setExerciseAccess(false);
        }else{
            setMedicalHistory(false);
            setExerciseAccess(true);
        }

    }

    public void getSportsHistory(){
        System.out.println("");
        System.out.println("请问您过去是否有过健身经历?(请输入 是或否)");
        Scanner s = new Scanner(System.in);
        String feedBack = s.nextLine();
        if (feedBack.equals("是")) {
            setSportsHistory(true);
        }else{
            setSportsHistory(false);
        }

    }

    public void getProfessionalSportsHistory(){
        System.out.println("");
        System.out.println("请问您过去是否有过专业健身器械训练经历?(请输入 是或否)");
        Scanner s = new Scanner(System.in);
        String feedBack = s.nextLine();
        if (feedBack.equals("是")) {
            setProfessionaSportsHistory(true);
        }else{
            setProfessionaSportsHistory(false);
        }

    }

    public void advice2(){
        if (!exerciseAccess) {
            advice02 = "不建议您高强度锻炼，锻炼前请仔细咨询专业医疗人员";
            System.out.println("根据您所提供的数据，这边给您的建议为：" + advice02);
        }
    }

    public void advice3(){
        if (intensity) {
            advice03 = "您基础较好，适合直接进行有一定强度的训练，建议您减少糖类摄入，多补充高蛋白食物";
            System.out.println("根据您所提供的数据，这边给您的建议为：" + advice03);
        }
    }

    public void advice4(){
        if (!intensity) {
            advice04 = "您的基础较薄弱，建议您减少糖类摄入，多补充电解质";
            System.out.println("根据您所提供的数据，这边给您的建议为：" + advice04);
        }
    }

    public void advice5(){
        if (!intensity) {
            advice05 = "建议您减少糖类摄入，多补充电解质";
            System.out.println("根据您所提供的数据，这边给您的建议为：" + advice05);
        }
    }

}
