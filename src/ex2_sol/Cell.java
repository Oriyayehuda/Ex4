package assignments.ex4.ex2_sol;

/**
 * ArielU. Intro2CS, Ex2: https://docs.google.com/document/d/1-18T-dj00apE4k1qmpXGOaqttxLn-Kwi/edit?usp=sharing&ouid=113711744349547563645&rtpof=true&sd=true
 * DO NOT CHANGE THIS INTERFACE!!
 * This interface represents a spreadsheet entry for Ex2:
 * Each spreadsheet entry (aka a Cell) which can be:
 * a number (Double), a String (Text), or a form, the data of each cell is represented as a String (e.g., "abc", "4.2", "=2+3*2", "=A1*(3-A2)".
 */
public interface Cell {
    /**
     * Return the input text (aka String) this cell was init by (without any computation).
     * @return
     */
    String getData();// מחזירה את מה שהוזן

/** Changes the underline string of this cell
 *  */
    void setData(String s); //משנה את הנתון שהוזן


    /**
     * Returns the type of this cell {TEXT,NUMBER, FORM, ERR_CYCLE_FORM, ERR_WRONG_FORM}
     * @return an int value (as defined in Ex2Utils)
     */
    public int getType(); // מחזירה מס שלם מייצג סוג תא

    /**
     * Changes the type of this Cell {TEXT,NUMBER, FORM, ERR_CYCLE_FORM, ERR_WRONG_FORM}
     * @param t an int type value as defines in Ex2Utils.
     */
    public void setType(int t); // משנה את סוג התא לערך t
    /**
     * Computes the natural order of this entry (cell) in case of a number or a String =0, else 1+ the max of all dependent cells.
     * @return an integer representing the "number of rounds" needed to compute this cell (using an iterative approach)..
     */
    public int getOrder(); // מחזירה את רמת הסדר של התא שמייצגת מס דרישות לחשב (אם בתא יש אותו סוג יהיה מסדר 1 ואם לא מסדר 0
    /**
     * Changes the order of this Cell
     * @param t
     */
    public void setOrder(int t); //משנה את ערך הסדר של התא
}
