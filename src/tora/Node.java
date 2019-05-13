package tora;

import java.util.ArrayList;

public class Node {
    public ArrayList<Double> wt;    //Weights
    public double act;              //Output of this Node
    public double th;               //Threshold
    public double e;                //Error

    public Node() {
        this.wt = new ArrayList<>();
        this.act = 0.0;
        this.th = 0.0;
        this.e = 0.0;
    }
}
