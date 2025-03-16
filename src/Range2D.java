package assignments.ex4;

import assignments.ex4.ex2_sol.CellEntry;
import assignments.ex4.ex2_sol.Ex2Utils;
import assignments.ex4.ex2_sol.Index2D;

import java.util.ArrayList;

public class Range2D {
    private Index2D start;
    private Index2D end;
    private ArrayList<Double> data;

    public Range2D(CellEntry a, CellEntry b) {
        if (a == null || b == null) {
            System.out.println("ERR");
            return;
        }
        this.start = a;
        this.end = b;
    }

    public ArrayList<String> getAllCells() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = start.getX(); i <= end.getX(); i++){
            for (int j = start.getY(); j <= end.getY(); j++) {
                list.add(Ex2Utils.ABC[i]+j);
            }
        }
        return list;
    }

    @Override
    public String toString() {
        return start.toString() + ":" + end.toString();
    }

    }







