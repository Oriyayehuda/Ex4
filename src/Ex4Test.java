package assignments.ex4;

import assignments.ex4.ex2_sol.Ex2Sheet;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This JUnit class represents a very partial test class for Ex1.
 * Make sure you complete all the needed JUnits
 */
public class Ex4Test {
    @Test
    public void checkIF() {
        Ex2Sheet excel = new Ex2Sheet(7,7);
        excel.set(0, 0, "=6");
        excel.set(0, 1, "=if(1<2,1,2)");
        excel.set(0,2, "=if(a1>2, big,small)");
        excel.set(0,4,"=if(3<4,3,4)");
        excel.set(0,3,"=if(4<5,4,5)");
        excel.set(1,4, "=if(a1*a4 != a3/(2-a1), =a4+2, =a1+1)");
        excel.set(1,5,"=if(a1<5,=if(5>1,4,1),5)");
        excel.set(1,6,"=if(1>2,1,=if(5<1,4,=max(a0:a1)))");
        excel.set(1,3,"=if(1<2,=if(5<1,4,=max(a0:a1))),=if(5<1,4,=if(1<2, 1, =min(a0:a1)))");
        excel.eval();
        String v = excel.value(0, 1);
        String a = excel.value(0, 2);
        String b = excel.value(1, 4);
        String c = excel.value(1, 5);
        String d = excel.value(1, 6);
        String e = excel.value(1, 6);
        Assertions.assertEquals(v, "1.0");
        Assertions.assertEquals(a, "small");
        Assertions.assertEquals(b, "5.0");
        Assertions.assertEquals(c, "4.0");
        Assertions.assertEquals(d, "6.0");
        Assertions.assertEquals(e, "6.0");
    }

    @Test
    public void checkRange() {
        Ex2Sheet excel = new Ex2Sheet(7,7);
        excel.set(0, 0, "=1");
        excel.set(0, 1, "=5");
        excel.set(1, 0, "=9");
        excel.set(1, 1, "=2");
        excel.set(2, 0, "=max(A0:B1)");
        excel.set(2, 1, "=min(A0:B1)");
        excel.set(2, 2, "=average(A0:B1)");
        excel.set(2, 3, "=sum(A0:B1)");
        excel.eval();
        String max = excel.value(2, 0);
        Assertions.assertEquals(max, "9.0");
        String min = excel.value(2, 1);
        Assertions.assertEquals(min, "1.0");
        String aver = excel.value(2, 2);
        Assertions.assertEquals(aver, "4.25");
        String sum = excel.value(2, 3);
        Assertions.assertEquals(sum, "17.0");
    }

    @Test
    public void checkRangeWithNull() {
        Ex2Sheet excel = new Ex2Sheet(7,7);
        excel.set(0, 0, "=1");
        excel.set(0, 1, "=5");
        excel.set(1, 1, "=2");
        excel.set(2, 0, "=max(A0:B1)");
        excel.set(2, 1, "=min(A0:B1)");
        excel.set(2, 2, "=average(A0:B1)");
        excel.set(2, 3, "=sum(A0:B1)");
        excel.eval();
        String max = excel.value(2, 0);
        Assertions.assertEquals(max, "FUNC_ERR!");
        String min = excel.value(2, 1);
        Assertions.assertEquals(min, "FUNC_ERR!");
        String aver = excel.value(2, 2);
        Assertions.assertEquals(aver, "FUNC_ERR!");
        String sum = excel.value(2, 3);
        Assertions.assertEquals(sum, "FUNC_ERR!");
    }
    @Test
    public void checkIFWithERR() {
        Ex2Sheet excel = new Ex2Sheet(7,7);
        excel.set(0, 1, "=1");
        excel.set(0, 2, "=5");
        excel.set(1, 1, "=2");
        excel.set(1,1,"=if(a1>a2,  =(A1, 12)");
        excel.set(1,2,"=if(a1>1, 1 )");
        excel.set(1,3,"=if(1,2,3)");
        excel.set(1,4,"=if(a5>a1,a5,a1)");
        excel.eval();
        String d = excel.value(1, 1);
        Assertions.assertEquals(d, "IF_ERR!");
        String e = excel.value(1, 2);
        Assertions.assertEquals(e, "IF_ERR!");
        String f  = excel.value(1, 3);
        Assertions.assertEquals(f, "IF_ERR!");
        String g  = excel.value(1, 4);
        Assertions.assertEquals(g, "IF_ERR!");
    }
    @Test
    public void checkIFERRCycle() {
        Ex2Sheet excel = new Ex2Sheet(7,7);
        excel.set(0,0,"=if(a0>3,2,4)");
        excel.set(0,1,"=a2");
        excel.set(0,2,"=if(a1>2,2,3)");
        excel.eval();
        String d = excel.value(0, 0);
        Assertions.assertEquals(d, "ERR_CYCLE!");
        String a = excel.value(0, 1);
        Assertions.assertEquals(a, "ERR_CYCLE!");
        String b = excel.value(0, 2);
        Assertions.assertEquals(b, "ERR_CYCLE!");
    }
}
