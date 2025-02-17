package assignments.ex4.ex2_sol;
/**
 * The documentation of this class was removed as of Ex4...
 */
public class CellEntry  implements Index2D {
    private String _data; // כתובת של התא
    private int x, y;
    public CellEntry(int x, int y) {
        if(x<0 | y<0 | x>= Ex2Utils.ABC.length) {_data = "ERROR!";} //אם המשתנים מחוץ לגבול התקין תשלח שגיאה
        else {_data = Ex2Utils.ABC[x]+y;} //אחרת יקבל את הכתובת של התא
        init(); // עדכון וניתוח של המשתנים
    }
    public String toString() {return _data;} //כשמדפיסים אובייקט מקבלים data
    public CellEntry(String c) { // הבנאי יוצר אובייקט הקלט הוא שם של תא בגיליון
        _data = c; //שומר את המחרוזת
        init(); // מנתח את המחרוזת באמצעות קריאה לפונק
    }
    private void init() { //מנתח את הdata וממיר ל2 אינדקסים x,y
        x = -1; y= -1; // (התא לא מוגדר עדיין) x עמודה y שורה
       if(_data!=null && _data.length()>=2) { // האורך של הדאטה צריך להיות לפחות 2 תווים
           _data = _data.toUpperCase(); // מוודא שהמשתמש הכניס אותיות גדולות ואם לא מתקן
            String s1 = _data.substring(0,1); // לוקח אות ראשונה
            String s2 = _data.substring(1); //לוקח את שאר המחרוזת
            Integer yy = Ex2Sheet.getInteger(s2); // ניסיון להמיר למס שלם
            if(yy!=null) {y=yy;} //אם מצליח מכיל רק מס אם לא יהיה null
            if(y>=0) { //בדיקה אם y חוקי אם לא אין טעם לבדוק את העמודה x
                x = s1.charAt(0) - 'A';// חישוב עמודה היא מיוצגת מA-Z שזה 0-25
                if(x<0 | x>25) {x=-1;}
          }
       }
       if(x==-1) {_data=null; y=-1;} // אם x מחוץ לטווח שווה ל-1
    }
    public String toCell() { // ממירה לשם תא
        String ans = null; // משמש להחזרת התוצאה
        if(x>=0 && y>=0) { //אם הערכים חיובים יתבצע חישוב
            ans = Ex2Utils.ABC[x]+y; //מחזירה את האות בין A-Z בהתאם לx ומוסיפה את השורה (y)
        }
        return ans; //אם תקינים מחזיר את שם התא
    }
    private void cell2coord(String cell) { //להמיר כתובת תא לx,y
        int x = -1, y=-1; //למקרה שהקלט אינו תקין
        if(cell!=null && cell.length()>=2) { //בדיקה שלא null ואורך התווים הוא לא פחות מ2
            cell = cell.toUpperCase(); //המרה לאותיות גדולות
            String s1 = cell.substring(0,1);// חילוץ האות ה1 מהעמודה
            String s2 = cell.substring(1); // חילוץ המספרים מהשורה
            y = Ex2Sheet.getInteger(s2);
            x = s1.charAt(0) - 'A';
        }
    }
    public boolean isIn(Sheet t) {
        return t!=null && t.isIn(x,y);
    }// אם t=null אז מחזירה false אם לא בודקת אם הוא בתחום
    @Override
    public boolean isValid() {
        return _data!=null;
    } //אם לא null האובייקט תקף

    @Override
    public int getX() {return x;} //cell2coordמחזירה את הערך של x שנקבע ב

    @Override
    public int getY() {return y;} // מחזירה את הערך של y
}
