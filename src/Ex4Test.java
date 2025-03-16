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
        excel.set(0, 1, "=if(1<2,1,2)");
        excel.set(0,2, "=if(a1>2, big,small)");
        excel.set(0,4,"=if(3<4,3,4)");
        excel.set(0,3,"=if(4<5,4,5)");
        excel.set(1,4, "=if(a1*a4 != a3/(2-a1), =a4+2, =a1+1)");
        excel.set(1,5,"=if(a1<5,=if(5>1,4,1),5)");

        excel.eval();
        String v = excel.value(0, 1);
        String a = excel.value(0, 2);
        String b = excel.value(1, 4);
        String c = excel.value(1, 5);
        Assertions.assertEquals(v, "1.0");
        Assertions.assertEquals(a, "small");
        Assertions.assertEquals(b, "5.0");
        Assertions.assertEquals(c, "4.0");
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
//    @Test
//    public void isBasisNumberTest() {
//        String[] good = {"1", "1b2", "01b2", "123bA", "ABbG", "0bA"};
//        for(int i=0;i<good.length;i=i+1) {
//            boolean ok = Ex1.isValidNumber(good[i]);
//            assertTrue(ok);
//        }
//        String[] not_good = {"b2", "2b2", "1G3bG", " BbG", "0bbA", "abB", "!@b2", "A", "1bb2"};
//        for(int i=0;i<not_good.length;i=i+1) {
//            boolean not_ok = Ex1.isValidNumber(not_good[i]);
//            assertFalse(not_ok);
//        }
//    }
//    @Test
//    public void int2NumberTest() {
//        int num = 10;
//        String res = Ex1.int2Number(num, 2);
//        Assertions.assertEquals("1010b2", res);
//        res = Ex1.int2Number(num, 17);
//        Assertions.assertEquals("", res);
//        res = Ex1.int2Number(0, 2);
//        Assertions.assertEquals("0b2", res);
//        res = Ex1.int2Number(-1, 2);
//        Assertions.assertEquals("", res);
//        res = Ex1.int2Number(num, 1);
//        Assertions.assertEquals("", res);
//    }
//    @Test
//    public void maxIndexTest() {
//        String[] arr = {"2", "3", "1", "2"};
//        int res = Ex1.maxIndex(arr);
//        assertEquals(1,res);
//        String[] arr1 = {"10b2", "100b2", "1000b2"};
//        res = Ex1.maxIndex(arr1);
//        assertEquals(2,res);
//        String[] arr2 = {"1000b3", "100b2", "1000b3"};
//        res = Ex1.maxIndex(arr2);
//        assertEquals(0,res);
//        String[] arr3 = {"", "100b3", "1000b3"};
//        res = Ex1.maxIndex(arr3);
//        assertEquals(2,res);
//        String[] arr4 = {null, "1000b5", "1000b2"};
//        res = Ex1.maxIndex(arr4);
//        assertEquals(1,res);
//        String[] arr5 = {"b5", "1000", "1000b2"};
//        res = Ex1.maxIndex(arr5);
//        assertEquals(1,res);
//    }
//    @Test
//    public void number2IntTest(){
//        String num = "10";
//        int res = Ex1.number2Int(num);
//        assertEquals(10,res);
//        num = "100b2";
//        res = Ex1.number2Int(num);
//        assertEquals(4,res);
//        num = "100bA";
//        res = Ex1.number2Int(num);
//        assertEquals(100,res);
//        num = "100b";
//        res = Ex1.number2Int(num);
//        assertEquals(-1,res);
//        num = "bA";
//        res = Ex1.number2Int(num);
//        assertEquals(-1,res);
//        num = "10AbA";
//        res = Ex1.number2Int(num);
//        assertEquals(-1,res);
//        num = " 10bA";
//        res = Ex1.number2Int(num);
//        assertEquals(-1,res);
//        num = "1FbG";
//        res = Ex1.number2Int(num);
//        assertEquals(31,res);
//        num = "10AbB";
//        res = Ex1.number2Int(num);
//        assertEquals(131,res);
//        num = "100b16";
//        res = Ex1.number2Int(num);
//        assertEquals(-1,res);
//        num = "-1";
//        res = Ex1.number2Int(num);
//        assertEquals(-1,res);
//    }
//    @Test
//    public void equalsNumbersTest(){
//        String num1 = "10";
//        String num2 = "10bA";
//        assertTrue(Ex1.equals(num1,num2));
//        num2 = "1010b2";
//        assertTrue(Ex1.equals(num1,num2));
//        num2 = "-1";
//        assertFalse(Ex1.equals(num1,num2));
//        num2 = null;
//        assertFalse(Ex1.equals(num1,num2));
//        num2 = "0";
//        assertFalse(Ex1.equals(num1,num2));
//        num2 = "100b3";
//        assertFalse(Ex1.equals(num1,num2));
//        num2 = "100b17";
//        assertFalse(Ex1.equals(num1,num2));
//        num2 = "100b";
//        assertFalse(Ex1.equals(num1,num2));
//        num2 = "bA";
//        assertFalse(Ex1.equals(num1,num2));
//        num2 = "CbB";
//        assertFalse(Ex1.equals(num1,num2));
//        num1 = "31bA";
//        num2 = "1FbG";
//        assertTrue(Ex1.equals(num1,num2));
//    }
}
