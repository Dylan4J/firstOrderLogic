import jdk.swing.interop.LightweightContentWrapper;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Sportsman mike = new Sportsman("mike");
        mike.getBmiNumber();
        mike.advice1();
        mike.getMedicalHistory();
        mike.advice2();
        mike.getSportsHistory();
        mike.getProfessionalSportsHistory();
        if (mike.isExerciseAccess()) {//是否可以正常训练
            if (mike.isSportsHistory()) {//是否有过健身经验
                mike.setIntensity(true);
                mike.advice3();
            } else {
                mike.advice4();
            }
        }
    }

}
