package assignments.ex4.ex2_sol;

import java.awt.*;
import java.io.IOException;

/**
 * ArielU. Intro2CS, Ex2: https://docs.google.com/document/d/1-18T-dj00apE4k1qmpXGOaqttxLn-Kwi/edit?usp=sharing&ouid=113711744349547563645&rtpof=true&sd=true
 * This is a very partial solution as of Ex4 ("הערכה חילופית"), in particular this solution
 * does NOT include a documentation and JUnit tests.
 *
 * The Code uses the STDDraw class:
 * https://introcs.cs.princeton.edu/java/stdlib/javadoc/StdDraw.html
 * Note: a few minor changes were added to STDDraw suit the logic of Ex2:
 * @author boaz.benmoshe
 *
 */
public class Ex2GUI {

	private static Sheet table; // this is the main data (an implementation of the Sheet interface).
	private static Index2D cord = null; // a table entry used by the GUI of setting up a cell value / form

	public Ex2GUI() {
		;
	}  // an empty (redundant) constructor.

	/**
	 * The main function for running Ex2
	 */
	public static void main(String[] args) {
		table = new Ex2Sheet(Ex2Utils.WIDTH, Ex2Utils.HEIGHT); //יצירת גיליון חדש
		testSimpleGUI(table);//מתחילה ממשק גרפי
		if (args.length == 0) { //בדיקה אם יש 0 ערכים
			System.out.println("Error"); //מתקבלת שגיאה
			return;
		}
		if (args.length == 1) { //אם יש ערך 1
			args = new String[]{args[0], args[0]};//מתקבל 2 עותקים שלו כדי להימנע מבעית גישה לתאים
		}
		CellEntry cell1 = new CellEntry(args[0]); //יצירת 2 אובייקטים שמתארים את התאים שקיבלנו מהמשתמש
		CellEntry cell2 = new CellEntry(args[1]);

		if (!cell1.isValid() || !cell2.isValid()) { //בדיקה אם אחד מהם לא תקף לזרוק שגיאה
			System.out.println("Error");
			return;
		}


	}
	/**
	 * This function runs the main (endlees) loop of the GUI
	 * @param table the SpreadSheet - note: this class is written as a naive implementation of "singleton" (i.e., all static).
	 */
	public static void testSimpleGUI(Sheet table) {
		// init parameters //הפעלת לולאת GUI אינסופית שמצירת את הגיליון
		StdDrawEx2.setCanvasSize(Ex2Utils.WINDOW_WIDTH, Ex2Utils.WINDOW_HEIGHT);
		StdDrawEx2.setScale(0, Ex2Utils.MAX_X); //מגדירים את גודל החלון, סקאלת הציור ועובי הקו
		StdDrawEx2.setPenRadius(Ex2Utils.PEN_RADIUS);
		StdDrawEx2.enableDoubleBuffering();
		table.eval(); //מבצע חישוב של כל התאים
		// endless loop (GUI)
		while (true) {
			StdDrawEx2.clear(); // clear the GUI (Ex2 window). //כל מחזור בלולאה מצייר מחדש את הגיליון
			drawFrame(); // draws the lines.
			drawCells(); // draws the cells
			StdDrawEx2.show(); // presents the window.
			int xx = StdDrawEx2.getXX(); // gets the x coordinate of the mouse click (-1 if none) //בודק אם המשתמש לחץ על תא אם כן פותחים חלון קלט
			int yy = StdDrawEx2.getYY(); // gets the y coordinate of the mouse click (-1 if none)
			inputCell(xx,yy); 			 // if isIn(xx,yy) an input window will be opened to allow the user to edit cell (xx,yy);
			StdDrawEx2.pause(Ex2Utils.WAIT_TIME_MS); // waits a few milliseconds - say 30 fps is sufficient.
		}
	}
	public static void save(String fileName){ //פונקצית שמירה וטעינה
		try {
			table.save(fileName);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void load(String fileName){ //טוענת גיליון מהקובץ
		try {
			table.load(fileName);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static Color getColorFromType(int t) {//קובעת צבע לפי סוג נוסחה/מס
		Color ans = Color.GRAY;
		if(t== Ex2Utils.NUMBER) {ans=Color.BLACK;}
		if(t== Ex2Utils.FORM) {ans=Color.BLUE;}
		if(t== Ex2Utils.ERR_FORM_FORMAT) {ans=Color.RED;}
		if(t== Ex2Utils.ERR_CYCLE_FORM) {ans= StdDrawEx2.BOOK_RED;}
		return ans;
	}

	/**
	 * Draws the lines of the spreadsheet.
	 */
	private static void drawFrame() { //מציירת את קווי המסגרת ומוסיפה מס שורות וכותרות עמודות
		StdDrawEx2.setPenColor(StdDrawEx2.BLACK);
		int max_y = table.height();
		double x_space = Ex2Utils.GUI_X_SPACE, x_start = Ex2Utils.GUI_X_START;
		double y_height = Ex2Utils.GUI_Y_TEXT_START;
		for (int y = 0; y < max_y; y = y + 1) {
			double xs = y * x_space;
			double xc = x_start + y * x_space;
			StdDrawEx2.line(0, y + 1, Ex2Utils.MAX_X, y + 1);
			StdDrawEx2.line(xs, 0, xs, max_y);
			int yy = max_y - (y + 1);
			StdDrawEx2.text(1, y + y_height, "" + (yy));
			StdDrawEx2.text(xc, max_y + y_height, "" + Ex2Utils.ABC[y]);
		}
	}
	/**
	 * Draws the content of each cell (none empty).
	 */
	private static void drawCells() { //מציירת ערכים של כל תא על המסך
		StdDrawEx2.setPenColor(StdDrawEx2.BLACK);
		int max_y = table.height();
		int maxx = table.width();
		double x_space = Ex2Utils.GUI_X_SPACE, x_start = Ex2Utils.GUI_X_START;
		double y_height = Ex2Utils.GUI_Y_TEXT_START;
		for (int x = 0; x < maxx; x = x + 1) {
			double xc = x_start + x * x_space;
			for (int y = 0; y < max_y; y = y + 1) {
				String w = table.value(x, y);//""+abc[x]+y;
				Cell cc = table.get(x, y);
				int t = cc.getType();
				StdDrawEx2.setPenColor(getColorFromType(t));
				int max = Math.min(Ex2Utils.MAX_CHARS, w.length());
				w = w.substring(0, max);
				double yc = max_y - (y + 1 - y_height);
				StdDrawEx2.text(xc, yc, w);
			}
		}
	}

	/** input a content into cell(xx,yy) if it is within this SpreadSheet.
	 *
	 * @param xx the x coordinate of the required cell.
	 * @param yy the y coordinate of the required cell.
	 */
	private static void inputCell(int xx,int yy) {// מאפשרת למשתמש להכניס ערך חדש לתא מסויים
		if(table.isIn(xx,yy)) { //בודק שהתא נמצא בטווח
			cord = new CellEntry(xx,yy);
			Cell cc = table.get(xx,yy);
			String ww = cord+": "+cc.toString()+" : ";
			StdDrawEx2.text(Ex2Utils.GUI_X_START, Ex2Utils.MAX_X-1, ww);
			StdDrawEx2.show();
			if(Ex2Utils.Debug) {System.out.println(ww);} //אם פועל מדפיס
			String c = StdDrawEx2.getCell(cord,cc.getData()); // bug3452 //קלט מהמשתמש
			String s1 = table.get(xx,yy).getData();
			if(c==null) {
				table.set(xx,yy,s1);//שומר את הערך הישן אם לא התקבל קלט חדש
			}
			else {
				table.set(xx, yy, c); //מעדכן את הערך החדש
				int[][] calc_d = table.depth();
				if (calc_d[xx][yy] == Ex2Utils.ERR) {
					table.set(xx, yy, c);
					table.get(xx,yy).setType(Ex2Utils.ERR_CYCLE_FORM);
				}
			}
			table.eval(); //מחשב את הערכים של הציור
			StdDrawEx2.resetXY();
		}
	}
}
