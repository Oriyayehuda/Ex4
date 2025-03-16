package assignments.ex4.ex2_sol;

import assignments.ex4.Range2D;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * The documentation of this class was removed as of Ex4...
 */
public class Ex2Sheet implements Sheet {
    private Cell[][] table;
    private Object[][] data;

    public Ex2Sheet(int x, int y) {
        table = new SCell[x][y];
        for (int i = 0; i < x; i = i + 1) {
            for (int j = 0; j < y; j = j + 1) {
                table[i][j] = new SCell("");
            }
        }
        eval();
    }

    public Ex2Sheet() {
        this(Ex2Utils.WIDTH, Ex2Utils.HEIGHT);
    }

    @Override
    public String value(int x, int y) {
        String ans = "";
        Cell c = get(x, y);
        ans = c.toString();
        int t = c.getType();
        if (t == Ex2Utils.ERR_CYCLE_FORM) {
            ans = Ex2Utils.ERR_CYCLE;
            c.setOrder(-1);
        } // BUG 345
        //  if(t==Ex2Utils.ERR_CYCLE_FORM) {ans = "ERR_CYCLE!";}
        if (t == Ex2Utils.NUMBER || t == Ex2Utils.FORM || t == Ex2Utils.FUNC || t == Ex2Utils.IF) {
            ans = "" + data[x][y];
        }
        if (t == Ex2Utils.ERR_FORM_FORMAT) {
            ans = Ex2Utils.ERR_FORM;
        }
        if (t == Ex2Utils.IF_ERR_FORMAT) {
            ans = Ex2Utils.ERR_IF;
        }
        if (t == Ex2Utils.FUNC_ERR_FORMAT) {
            ans = Ex2Utils.ERR_FUNC;
        }
        return ans;
    }

    @Override
    public Cell get(int x, int y) {
        return table[x][y];
    } // מחזיר את התא שנמצא במיקום

    @Override
    public Cell get(String cords) {
        Cell ans = null; //אתחול משתנה שיחזיק את התא שנמצא
        Index2D c = new CellEntry(cords); //ממיר לקורדיננטות מספריות
        int x = c.getX(), y= c.getY();  //מקבל את הערכים מהאובייקט החדש
        if(isIn(x,y)) {ans = table[x][y];} //בודק אם קורדיננטה תקינה אם כן מחזיר התא
        return ans;

    }

    @Override
    public int width() {
        return table.length;
    } //רוחב הגיליון מס עמודות
    @Override
    public int height() {
        return table[0].length;
    } //גובה הגיליון מספר שורות
    @Override
    public void set(int x, int y, String s) {
        Cell c = new SCell(s); //יוצר אובייקט חדש עם הערך שמתקבל
        table[x][y] = c; //שומר את התא
      //  eval();
    }

    ///////////////////////////////////////////////////////////

    @Override
    public void eval() {
        int[][] dd = depth(); //מחשב את סדר ההערכה של התאים
        data = new Object[width()][height()]; //מאתחל מחדש את מערך הנתונים
        for (int x = 0; x < width(); x = x + 1) {
            for (int y = 0; y < height(); y = y + 1) {
                Cell c = table[x][y];
              if (dd[x][y] != -1 && c!=null && (c.getType()!= Ex2Utils.TEXT)) {
                    String res = eval(x, y); //מחשב את הערך
                    Double d = getDouble(res); //ממיר תוצאה למס
                    if(d==null && res == null) { //'if' can return string so res can be string also
                        if (c.getType() == Ex2Utils.FUNC) { //אם הסוג הוא פונקציה אז השגיאה נעשית שם
                            c.setType(Ex2Utils.FUNC_ERR_FORMAT);
                        } else if (c.getType() == Ex2Utils.IF) { //אם הסוג הוא תנאי והשגיאה נעשית שם
                            c.setType(Ex2Utils.IF_ERR_FORMAT);
                        } else if (c.getType() != Ex2Utils.FUNC_ERR_FORMAT && c.getType() != Ex2Utils.IF_ERR_FORMAT) { //אם לא נעשה לא בפונקציה ולא בתנאי
                            c.setType(Ex2Utils.ERR_FORM_FORMAT); //להחזיר פורמט שגיאה על נוסחה כללית
                        }
                    }
                    else {
                        if (d != null) {
                            data[x][y] = d;
                        }
                        else {
                            data[x][y] = res;
                        }

                    }
                }
                if (dd[x][y] == -1 ) {
                    c.setType(Ex2Utils.ERR_CYCLE_FORM);
                }
            }
        }
    }

    @Override
    public boolean isIn(int xx, int yy) {
        boolean ans = true; //הנחה שהקורדינטה תקינה
        if(xx<0 |yy<0 | xx>=width() | yy>=height()) {ans = false;} //בדיקה אם הערכים מחוץ לגבולות ואם כן מחזירים לא נכון
        return ans;
    }

    @Override
    public int[][] depth() { //מחשב עומק
        int[][] ans = new int[width()][height()]; //יצירת מערך דו מימדי של שורות ועמודות
        for (int x = 0; x < width(); x = x + 1) { //עוברת על כל התאים
            for (int y = 0; y < height(); y = y + 1) { //בדיקה אם הערך קטן מהגובה ואם כן מוסיף 1
                Cell c = this.get(x, y); //מחזיר את התא במיקום של הערכים
                int t = c.getType(); //מחזיר את סוג התא
                if(Ex2Utils.TEXT!=t) {
                    ans[x][y] = -1; //אם לא טקסט מחזירים -1 כי נצרך לחשב את ערכו
                }
            }
        }
        int count = 0, all = width()*height(); //סופר כמה תאים חושבו
        boolean changed = true;
        while (changed && count<all) {
            changed = false; //מציין אם היה שינוי במחזור האחרון של החישוב
            for (int x = 0; x < width(); x = x + 1) { //עוברת כל תאי הגיליון
                for (int y = 0; y < height(); y = y + 1) {
                    if(ans[x][y]==-1) { //אם לא חושב מחזיר -1
                        Cell c = this.get(x, y);
                     //   ArrayList<Coord> deps = allCells(c.toString());
                        ArrayList<Index2D> deps = allCells(c.getData());
                        int dd = canBeComputed(deps, ans);
                        if (dd!=-1) {
                            ans[x][y] = dd;
                            count++;
                            changed = true;
                        }
                    }
                }
            }
        }
        return ans;
    }

    @Override
    public void load(String fileName) throws IOException {
            Ex2Sheet sp = new Ex2Sheet();
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            String s0 = myReader.nextLine();
            if(Ex2Utils.Debug) {
                System.out.println("Loading file: "+fileName);
                System.out.println("File info (header:) "+s0);
            }
            while (myReader.hasNextLine()) {
                s0 = myReader.nextLine();
                String[] s1 = s0.split(",");
               try { //עלול לגרום לשגיאה
                   int x = Ex2Sheet.getInteger(s1[0]);
                   int y = Ex2Sheet.getInteger(s1[1]);
                   sp.set(x,y,s1[2]);
               }
               catch (Exception e) { //קוד שירוץ במקרה של השגיאה
                    e.printStackTrace();
                    System.err.println("Line: "+data+" is in the wrong format (should be x,y,cellData)");
               }
        }
            sp.eval();
            table = sp.table;
            data = sp.data;
    }

    @Override
    public void save(String fileName) throws IOException {
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write("I2CS ArielU: SpreadSheet (Ex2) assignment - this line should be ignored in the load method\n");
            for(int x = 0;x<this.width();x=x+1) {
                for(int y = 0;y<this.height();y=y+1) {
                    Cell c = get(x,y);
                    if(c!=null && !c.getData().equals("")) {
                        String s = x+","+y+","+c.getData();
                        myWriter.write(s+"\n");
                    }
                }
            }
            myWriter.close();
    }

    private int canBeComputed(ArrayList<Index2D> deps, int[][] tmpTable) {
        int ans = 0;
        for(int i=0;i<deps.size()&ans!=-1;i=i+1) {
            Index2D c = deps.get(i);
            int v = tmpTable[c.getX()][c.getY()];
            if(v==-1) {ans=-1;} // not yet computed;
            else {ans = Math.max(ans,v+1);}
        }
        return ans;
    }
    @Override
    public String eval(int x, int y) {
        Cell c = table[x][y];
        String line = c.getData();
        if(c==null || c.getType()== Ex2Utils.TEXT ) {
            data[x][y] = null;
            return line;
        }
        int type = c.getType();
        if(type== Ex2Utils.NUMBER) {
            data[x][y] = getDouble(c.toString());
            return line;
        }
        if (type == Ex2Utils.FORM | type == Ex2Utils.ERR_CYCLE_FORM || type== Ex2Utils.ERR_FORM_FORMAT) {
            line = line.substring(1); // removing the first "="
            if (isForm(line)) {
                Double dd = computeForm(x,y);
                data[x][y] = dd;
                if(dd==null) {
                    c.setType(Ex2Utils.ERR_FORM_FORMAT);
                }
                else {c.setType(Ex2Utils.FORM);}
            }
            else {data[x][y] = null;}
        }
        if (type == Ex2Utils.FUNC | type == Ex2Utils.FUNC_ERR_FORMAT) {
            line = line.substring(1); // removing the first "="
            if (isFunction(line)) {
                Double dd = computeFunction(x,y);
                data[x][y] = dd;
                if(dd==null) {
                    c.setType(Ex2Utils.FUNC_ERR_FORMAT);
                }
                else {c.setType(Ex2Utils.FUNC);}
            }
            else {data[x][y] = null;}
        }
        if (type == Ex2Utils.IF | type == Ex2Utils.IF_ERR_FORMAT) {
            line = line.substring(1); // removing the first "="
            if (isIfCondition(line)) {
                Object dd = computeIfCondition(x,y);
                if (dd instanceof Double doubleVal) {
                    data[x][y] = doubleVal;
                }
                else if (dd instanceof String strVal) {
                    data[x][y] = null;
                    return strVal;
                }
                if(dd==null) {
                    c.setType(Ex2Utils.IF_ERR_FORMAT);
                }
                else {c.setType(Ex2Utils.IF);}
            }
            else {data[x][y] = null;}
        }

        String ans = null;
        if(data[x][y]!=null) {ans = data[x][y].toString();}
        return ans;
    }
    /////////////////////////////////////////////////
    public static Integer getInteger(String line) {
        Integer ans = null;
        try {
            ans = Integer.parseInt(line);
        }
        catch (Exception e) {;}
        return ans;
    }
    public static Double getDouble(String line) {
        Double ans = null;
        try {
            ans= Double.parseDouble(line);
        }
        catch (Exception e) {;}
        return ans;
    }
    public static String removeSpaces(String s) {
        String ans = null;
        if (s!=null) {
            String[] words = s.split(" ");
            ans = new String();
            for(int i=0;i<words.length;i=i+1) {
                ans+=words[i];
            }
        }
        return ans;
    }

    public int checkType(String line) {
        line = removeSpaces(line);
        int ans = Ex2Utils.TEXT;
        Double d = getDouble(line);
        if(d != null && d>Double.MIN_VALUE) {ans= Ex2Utils.NUMBER;}
        else {
            boolean isFunc = false;
            for (String func : Ex2Utils.M_FUNC) {
                String start = "=" + func;
                if (line.startsWith(start)) {
                    isFunc = true;
                    break;
                }
            }
            if (isFunc) {
                ans = Ex2Utils.FUNC_ERR_FORMAT;
                String s = line.substring(1);
                if(isFunction(s)) {ans = Ex2Utils.FUNC;}
            }
            else if (line.startsWith("=if")) {
                ans = Ex2Utils.IF_ERR_FORMAT;
                String s = line.substring(1);
                if(isIfCondition(s)) {ans = Ex2Utils.IF;}
            }
            else if(line.charAt(0)=='=') {
                ans = Ex2Utils.ERR_FORM_FORMAT;
                String s = line.substring(1);
                if(isForm(s)) {ans = Ex2Utils.FORM;}
            }
        }
        return ans;
    }
    public boolean isForm(String form) {
        boolean ans = false;
        if(form!=null) {
            form = removeSpaces(form);
            try {
                ans = isFormP(form);
            }
            catch (Exception e) {;}
        }
        return ans;
    }
    private Double computeForm(int x, int y) {
        Double ans = null;
        String form = table[x][y].getData();
        form = form.substring(1);// remove the "="
        if(isForm(form)) {
            form = removeSpaces(form);
            ans = computeFormP(form);
        }
        return ans;
    }
    private boolean isFormP(String form) {
        boolean ans = false;
        while (canRemoveB(form)) {
            form = removeB(form);
        }
        Index2D c = new CellEntry(form);
        if (isIn(c.getX(), c.getY())) {
            ans = true;
        } else {
            if (isNumber(form)) {
                ans = true;
            } else {
                int ind = findLastOp(form);// bug
                if (ind == 0) {  // the case of -1, or -(1+1)
                    char c1 = form.charAt(0);
                    if (c1 == '-' | c1 == '+') {
                        ans = isFormP(form.substring(1));
                    } else {
                        ans = false;
                    }
                } else if (ind != -1) { // (1+(1+2)) 2/2
                    String f1 = form.substring(0, ind);
                    String f2 = form.substring(ind + 1);
                    ans = isFormP(f1) && isFormP(f2);
                }
            }
        }
        return ans;
    }

    private boolean isConditionValue(String cond) {
        cond = removeSpaces(cond);
        return isIfCondition(cond) || isFunction(cond) || isForm(cond);
    }

    private boolean isFunction(String func) {
        boolean ans = false;
        while (canRemoveB(func)) {
            func = removeB(func);
        }
        Index2D c = new CellEntry(func);
        if (isIn(c.getX(), c.getY())) {
            ans = true;
        } else {
            int ind = op(func, Ex2Utils.M_FUNC, 0);
            if (ind != -1) {
                String str = func.substring(Ex2Utils.M_FUNC[ind].length());
                ind = str.indexOf(":");
                if (str.indexOf("(") == 0 && str.indexOf(")") == str.length() - 1 &&
                        ind != -1) {
                    String f1 = str.substring(1, ind);
                    String f2 = str.substring(ind + 1, str.length() - 1);
                    ans = isFormP(f1) && isFormP(f2);
                }
            }
        }
        return ans;
    }

    private boolean isIfCondition(String ifCondition) {
        boolean ans = false;
        while (canRemoveB(ifCondition)) {
            ifCondition = removeB(ifCondition);
        }
        Index2D c = new CellEntry(ifCondition);
        if (isIn(c.getX(), c.getY())) {
            ans = true;
        } else {
            int ind = ifCondition.indexOf(Ex2Utils.IF_TEXT);
            if (ind == 0) {
                String str = ifCondition.substring(Ex2Utils.IF_TEXT.length());
                if (str.startsWith("(") && str.endsWith(")")) {
                    str = str.substring(1, str.length() -1);
                    int firstIndex = str.indexOf(",");
                    int lastIndex = str.lastIndexOf(",");
                    if (firstIndex != lastIndex && lastIndex != str.length() -1 && firstIndex != 0) {
                        String firstStr = str.substring(0, firstIndex);
                        String secondStr = str.substring(firstIndex + 1, lastIndex);
                        String thirdStr = str.substring(lastIndex + 1);
                        ind = -1;
                        for(int i=1;i<firstStr.length() -1;i++) {
                            ind = op(firstStr, Ex2Utils.M_CONDITIONS, i);
                            if(ind!=-1){
                                break;
                            }
                        }
                        if (ind != -1) {
                            String[] subCondition = firstStr.split(Ex2Utils.M_CONDITIONS[ind]);
                            boolean isCondition = false;
                            if (subCondition.length == 2) {
                                isCondition = isForm(subCondition[0]) && isForm(subCondition[1]);
                            }
                            ans = isCondition && checkType(secondStr) > 0  && checkType(thirdStr) > 0;
                        }
                    }
                }
            }
        }
        return ans;
    }

    public static ArrayList<Index2D> allCells(String line) {
        ArrayList<Index2D> ans = new ArrayList<Index2D>();
        int i=0;
        int len = line.length();
        while(i<len) {
            int m2 = Math.min(len, i+2);
            int m3 = Math.min(len, i+3);
            String s2 = line.substring(i,m2);
            String s3 = line.substring(i,m3);
            Index2D sc2 = new CellEntry(s2);
            Index2D sc3 = new CellEntry(s3);
            if(sc3.isValid()) {ans.add(sc3); i+=3;}
            else{
                if(sc2.isValid()) {ans.add(sc2); i+=2;}
                else {i=i+1;}
            }

        }
        return ans;
    }

    private Double computeFormP(String form) {
        Double ans = null;
        while (canRemoveB(form)) {
            form = removeB(form);
        }
        CellEntry c = new CellEntry(form);
        if (c.isValid()) {

            return getDouble(eval(c.getX(), c.getY()));
        } else {
            if (isNumber(form)) {
                ans = getDouble(form);
            } else {
                int ind = findLastOp(form);
                if (ind != -1) {
                    int opInd = opCode(form.substring(ind, ind + 1));
                    if (ind == 0) {  // the case of -1, or -(1+1)
                        double d = 1;
                        if (opInd == 1) {
                            d = -1;
                        }
                        ans = d * computeFormP(form.substring(1));
                    } else {
                        String f1 = form.substring(0, ind);
                        String f2 = form.substring(ind + 1);

                        Double a1 = computeFormP(f1);
                        Double a2 = computeFormP(f2);
                        if (a1 == null || a2 == null) {
                            ans = null;
                        } else {
                            if (opInd == 0) {
                                ans = a1 + a2;
                            }
                            if (opInd == 1) {
                                ans = a1 - a2;
                            }
                            if (opInd == 2) {
                                ans = a1 * a2;
                            }
                            if (opInd == 3) {
                                ans = a1 / a2;
                            }
                        }
                    }
                }
            }
        }
        return ans;
    }

    private Double computeFunction(int x, int y) {
        Double ans = null;
        String func = table[x][y].getData();
        func = func.substring(1);// remove the "="
        if(isFunction(func)) {
            func = removeSpaces(func);
            ans = computeFunctionP(func);
        }
        return ans;
    }

    private Object computeIfCondition(int x, int y) {
        Object ans = null;
        String func = table[x][y].getData();
        func = func.substring(1);// remove the "="
        if(isIfCondition(func)) {
            func = removeSpaces(func);
            ans = computeIfConditionP(func);
        }
        return ans;
    }

    private Double computeFunctionP(String funcStr) {
        Double ans = null;
        while (canRemoveB(funcStr)) {
            funcStr = removeB(funcStr);
        }
        CellEntry c = new CellEntry(funcStr);
        if (c.isValid()) {
            return getDouble(eval(c.getX(), c.getY()));
        } else {
            int ind = op(funcStr, Ex2Utils.M_FUNC, 0);
            String func = Ex2Utils.M_FUNC[ind];
            if (ind != -1) {
                String str = funcStr.substring(func.length());
                int rangeIndex = str.indexOf(":");
                if (str.indexOf("(") == 0 && str.indexOf(")") == str.length() - 1 &&
                        rangeIndex != -1) {
                    CellEntry f1 = new CellEntry(str.substring(1, rangeIndex));
                    CellEntry f2 = new CellEntry(str.substring(rangeIndex + 1, str.length() - 1));
                    Range2D range = new Range2D(f1, f2);
                    ArrayList<String> allCells = range.getAllCells();
                    ArrayList<Double> results = new ArrayList<>();
                    for (String allCell : allCells) {
                        Double res = computeFormP(allCell);
                        if (res == null) {
                            return ans;
                        }
                        results.add(res);
                    }
                    if (ind == 0) {
                        ans = Collections.min(results);
                    } else if (ind == 1) {
                        ans = Collections.max(results);
                    } else if (ind == 2) {
                        for (Double res : results) {
                            if (ans == null) {
                                ans = (double) 0;
                            }
                            ans = ans + res;
                        }
                    } else if (ind == 3) {
                        double sum = 0;
                        for (Double res : results) {
                            sum = sum + res;
                        }
                        ans = sum / results.size();
                    }
                    return ans;

                }
            }
        }
        return ans;
    }

    private Object getConditionValue(String line){
        int type = checkType(line);
        if (type == Ex2Utils.TEXT) {
            return line;
        }
        if (type == Ex2Utils.NUMBER) {
            return getDouble(line);
        }
        Object ans = null;
        line = line.substring(1);
        if (type == Ex2Utils.FORM) {
            ans = computeFormP(line);
        } else if (type == Ex2Utils.FUNC) {
            ans = computeFunctionP(line);
        } else if (type == Ex2Utils.IF) {
            ans = computeIfConditionP(line);
        }
        return ans;
    }

    private Object getValue(String line) {
        Object ans = null;
        int type = checkType(line);
        if (type == Ex2Utils.NUMBER) {
            ans = getDouble(line);
        } else if (type == Ex2Utils.FORM) {
            ans = computeFormP(line);
        } else if (type == Ex2Utils.FUNC) {
            ans = computeFunctionP(line);
        } else if (type == Ex2Utils.IF) {
            ans = computeIfConditionP(line);
        }
        return ans;
    }

    private Object computeIfConditionP(String ifCondition) {
        Object ans = null;
        while (canRemoveB(ifCondition)) {
            ifCondition = removeB(ifCondition);
        }
        CellEntry c = new CellEntry(ifCondition);
        if (c.isValid()) {
            return getDouble(eval(c.getX(), c.getY()));
        } else {
            int ind = ifCondition.indexOf(Ex2Utils.IF_TEXT);
            if (ind == 0) {
                String str = ifCondition.substring(Ex2Utils.IF_TEXT.length());
                if (str.startsWith("(") && str.endsWith(")")) {
                    str = str.substring(1, str.length() -1);
                    int firstIndex = str.indexOf(",");
                    int lastIndex = str.lastIndexOf(",");
                    if (firstIndex != lastIndex && lastIndex != str.length() -1 && firstIndex != 0) {
                        String firstStr = str.substring(0, firstIndex);
                        String secondStr = str.substring(firstIndex + 1, lastIndex);
                        String thirdStr = str.substring(lastIndex + 1);
                        ind = -1;
                        for(int i=1;i<firstStr.length() -1;i++) {
                            ind = op(firstStr, Ex2Utils.M_CONDITIONS, i);
                            if(ind!=-1){
                                break;
                            }
                        }
                        String[] subCondition = firstStr.split(Ex2Utils.M_CONDITIONS[ind]);
                        if (subCondition.length == 2) {
                            Double leftCond = computeFormP(subCondition[0]);
                            Double rightCond = computeFormP(subCondition[1]);
                            if (leftCond != null && rightCond != null) {
                                if (ind == 0) {
                                    if (leftCond < rightCond) {
                                        ans = getConditionValue(secondStr);
                                    } else {
                                        ans = getConditionValue(thirdStr);
                                    }
                                } else if (ind == 1) {
                                    if (leftCond > rightCond) {
                                        ans = getConditionValue(secondStr);
                                    } else {
                                        ans = getConditionValue(thirdStr);
                                    }
                                } else if (ind == 2) {
                                    if (leftCond.equals(rightCond)) {
                                        ans = getConditionValue(secondStr);
                                    } else {
                                        ans = getConditionValue(secondStr);
                                    }
                                } else if (ind == 3) {
                                    if (leftCond <= rightCond) {
                                        ans = getConditionValue(secondStr);
                                    } else {
                                        ans = getConditionValue(thirdStr);
                                    }
                                } else if (ind == 4) {
                                    if (leftCond >= rightCond) {
                                        ans = getConditionValue(secondStr);
                                    } else {
                                        ans = getConditionValue(thirdStr);
                                    }
                                } else if (ind == 5) {
                                    if (!leftCond.equals(rightCond)) {
                                        ans = getConditionValue(secondStr);
                                    } else {
                                        ans = getConditionValue(thirdStr);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return ans;
    }

    private static int opCode(String op){
        int ans =-1;
        for(int i = 0; i< Ex2Utils.M_OPS.length; i=i+1) {
            if(op.equals(Ex2Utils.M_OPS[i])) {ans=i;}
        }
        return ans;
    }
    private static int findFirstOp(String form) {
        int ans = -1;
        int s1=0,max=-1;
        for(int i=0;i<form.length();i++) {
            char c = form.charAt(i);
            if(c==')') {s1--;}
            if(c=='(') {s1++;}
            int op = op(form, Ex2Utils.M_OPS, i);
            if(op!=-1){
                if(s1>max) {max = s1;ans=i;}
            }
        }
        return ans;
    }
    public static int findLastOp(String form) {
        int ans = -1;
        double s1=0,min=-1;
        for(int i=0;i<form.length();i++) {
            char c = form.charAt(i);
            if(c==')') {s1--;}
            if(c=='(') {s1++;}
            int op = op(form, Ex2Utils.M_OPS, i);
            if(op!=-1){
                double d = s1;
                if(op>1) {d+=0.5;}
                if(-1==min || d<=min) {min = d;ans=i;}
            }
        }
        return ans;
    }
    private static String removeB(String s) {
        if (canRemoveB(s)) {
            s = s.substring(1, s.length() - 1);
        }
        return s;
    }
    private static boolean canRemoveB(String s) {
        boolean ans = false;
        if (s!=null && s.startsWith("(") && s.endsWith(")")) {
            ans = true;
            int s1 = 0, max = -1;
            for (int i = 0; i < s.length()-1; i++) {
                char c = s.charAt(i);
                if (c == ')') {
                    s1--;
                }
                if (c == '(') {
                    s1++;
                }
                if (s1 < 1) {
                    ans = false;
                }
            }
        }
        return ans;
    }
    private static int op(String line, String[] words, int start) {
        int ans = -1;
        line = line.substring(start);
        for(int i = 0; i<words.length&&ans==-1; i++) {
            if(line.startsWith(words[i])) {
                ans=i;
            }
        }
        return ans;
    }
    private static int func(String line, String[] words, int start) {
        int ans = -1;
        line = line.substring(start);
        for(int i = 0; i<words.length&&ans==-1; i++) {
            if(line.startsWith(words[i])) {
                ans=i;
            }
        }
        return ans;
    }

    public static boolean isNumber(String line) {
        boolean ans = false;
        try {
            double v = Double.parseDouble(line);
            ans = true;
        }
        catch (Exception e) {;}
        return ans;
    }
}
