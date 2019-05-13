package tora;

import java.util.Scanner;
import java.util.Random;

//This is a direct port of a back propagating NN I made in FreeBasic back in 2005, I was 14 years old at the
//time so it's very minimal with one goal in mind, determining 12x12 digits 0-9

public class BPNN {

    public static Scanner input = new Scanner(System.in);

    public static final int IMG_SIZE = 12;
    public static final int NUM_IL = IMG_SIZE * IMG_SIZE;           //Number of Nodes in Input Layer
    public static final int NUM_HL = 20;                            //Number of Nodes in Hidden Layer
    public static final int NUM_OL = 10;                            //Number of Nodes in Output Layer
    public static final double LR = 0.1;
    public static final int GAIN = 5;

    public static Node[] hl = new Node[NUM_HL];
    public static Node[] ol = new Node[NUM_OL];

    //Digit Images
    public static int[] curImg;
    public static int[][] img = {
            //0
            {
                    0,0,0,0,0,0,0,0,0,0,0,0,
                    0,0,0,0,1,1,1,1,0,0,0,0,
                    0,0,0,1,0,0,0,0,1,0,0,0,
                    0,0,1,0,0,0,0,0,0,1,0,0,
                    0,0,1,0,0,0,0,0,0,1,0,0,
                    0,0,1,0,0,0,0,0,0,1,0,0,
                    0,0,1,0,0,0,0,0,0,1,0,0,
                    0,0,1,0,0,0,0,0,0,1,0,0,
                    0,0,1,0,0,0,0,0,0,1,0,0,
                    0,0,0,1,0,0,0,0,1,0,0,0,
                    0,0,0,0,1,1,1,1,0,0,0,0,
                    0,0,0,0,0,0,0,0,0,0,0,0
            },
            //1
            {
                    0,0,0,0,0,0,0,0,0,0,0,0,
                    0,0,0,0,0,0,1,0,0,0,0,0,
                    0,0,0,0,0,1,1,0,0,0,0,0,
                    0,0,0,0,1,0,1,0,0,0,0,0,
                    0,0,0,0,0,0,1,0,0,0,0,0,
                    0,0,0,0,0,0,1,0,0,0,0,0,
                    0,0,0,0,0,0,1,0,0,0,0,0,
                    0,0,0,0,0,0,1,0,0,0,0,0,
                    0,0,0,0,0,0,1,0,0,0,0,0,
                    0,0,0,0,0,0,1,0,0,0,0,0,
                    0,0,0,0,1,1,1,1,1,0,0,0,
                    0,0,0,0,0,0,0,0,0,0,0,0
            },
            //2
            {
                    0,0,0,0,0,0,0,0,0,0,0,0,
                    0,0,0,0,1,1,1,1,0,0,0,0,
                    0,0,0,1,0,0,0,0,1,0,0,0,
                    0,0,1,0,0,0,0,0,0,1,0,0,
                    0,0,0,0,0,0,0,0,0,1,0,0,
                    0,0,0,0,0,0,0,0,0,1,0,0,
                    0,0,0,0,0,0,0,0,1,0,0,0,
                    0,0,0,0,0,0,0,1,0,0,0,0,
                    0,0,0,0,0,0,1,0,0,0,0,0,
                    0,0,0,0,0,1,0,0,0,0,0,0,
                    0,0,0,1,1,1,1,1,1,1,0,0,
                    0,0,0,0,0,0,0,0,0,0,0,0
            },
            //3
            {
                    0,0,0,0,0,0,0,0,0,0,0,0,
                    0,0,0,0,1,1,1,1,0,0,0,0,
                    0,0,0,1,0,0,0,0,1,0,0,0,
                    0,0,0,0,0,0,0,0,1,0,0,0,
                    0,0,0,0,0,0,0,0,1,0,0,0,
                    0,0,0,0,0,1,1,1,0,0,0,0,
                    0,0,0,0,0,0,0,0,1,0,0,0,
                    0,0,0,0,0,0,0,0,1,0,0,0,
                    0,0,0,1,0,0,0,0,1,0,0,0,
                    0,0,0,0,1,1,1,1,0,0,0,0,
                    0,0,0,0,0,0,0,0,0,0,0,0,
                    0,0,0,0,0,0,0,0,0,0,0,0
            },
            //4
            {
                    0,0,0,0,0,0,0,0,0,0,0,0,
                    0,0,0,0,0,0,0,1,1,0,0,0,
                    0,0,0,0,0,0,1,0,1,0,0,0,
                    0,0,0,0,0,1,0,0,1,0,0,0,
                    0,0,0,0,1,0,0,0,1,0,0,0,
                    0,0,0,1,0,0,0,0,1,0,0,0,
                    0,0,0,1,1,1,1,1,1,1,0,0,
                    0,0,0,0,0,0,0,0,1,0,0,0,
                    0,0,0,0,0,0,0,0,1,0,0,0,
                    0,0,0,0,0,0,0,0,1,0,0,0,
                    0,0,0,0,0,0,0,1,1,1,0,0,
                    0,0,0,0,0,0,0,0,0,0,0,0
            },
            //5
            {
                    0,0,0,0,0,0,0,0,0,0,0,0,
                    0,0,0,1,1,1,1,1,1,0,0,0,
                    0,0,0,1,0,0,0,0,0,0,0,0,
                    0,0,0,1,0,0,0,0,0,0,0,0,
                    0,0,0,1,0,0,0,0,0,0,0,0,
                    0,0,0,1,1,1,1,0,0,0,0,0,
                    0,0,0,0,0,0,0,1,0,0,0,0,
                    0,0,0,0,0,0,0,0,1,0,0,0,
                    0,0,0,0,0,0,0,0,1,0,0,0,
                    0,0,0,0,0,0,0,1,0,0,0,0,
                    0,0,0,1,1,1,1,0,0,0,0,0,
                    0,0,0,0,0,0,0,0,0,0,0,0
            },
            //6
            {
                    0,0,0,0,0,0,0,0,0,0,0,0,
                    0,0,0,0,1,1,1,1,0,0,0,0,
                    0,0,0,1,0,0,0,0,0,0,0,0,
                    0,0,1,0,0,0,0,0,0,0,0,0,
                    0,0,1,0,0,0,0,0,0,0,0,0,
                    0,0,1,0,0,0,0,0,0,0,0,0,
                    0,0,1,0,1,1,1,1,0,0,0,0,
                    0,0,1,1,0,0,0,0,1,0,0,0,
                    0,0,1,0,0,0,0,0,1,0,0,0,
                    0,0,0,1,0,0,0,0,1,0,0,0,
                    0,0,0,0,1,1,1,1,0,0,0,0,
                    0,0,0,0,0,0,0,0,0,0,0,0
            },
            //7
            {
                    0,0,0,0,0,0,0,0,0,0,0,0,
                    0,0,0,1,1,1,1,1,1,1,0,0,
                    0,0,0,0,0,0,0,0,1,0,0,0,
                    0,0,0,0,0,0,0,0,1,0,0,0,
                    0,0,0,0,0,0,0,1,0,0,0,0,
                    0,0,0,0,0,0,0,1,0,0,0,0,
                    0,0,0,0,0,0,1,0,0,0,0,0,
                    0,0,0,0,0,0,1,0,0,0,0,0,
                    0,0,0,0,0,1,0,0,0,0,0,0,
                    0,0,0,0,0,1,0,0,0,0,0,0,
                    0,0,0,0,1,0,0,0,0,0,0,0,
                    0,0,0,0,0,0,0,0,0,0,0,0
            },
            //8
            {
                    0,0,0,0,0,0,0,0,0,0,0,0,
                    0,0,0,0,1,1,1,1,0,0,0,0,
                    0,0,0,1,0,0,0,0,1,0,0,0,
                    0,0,0,1,0,0,0,0,1,0,0,0,
                    0,0,0,1,0,0,0,0,1,0,0,0,
                    0,0,0,0,1,1,1,1,0,0,0,0,
                    0,0,0,1,0,0,0,0,1,0,0,0,
                    0,0,0,1,0,0,0,0,1,0,0,0,
                    0,0,0,1,0,0,0,0,1,0,0,0,
                    0,0,0,1,0,0,0,0,1,0,0,0,
                    0,0,0,0,1,1,1,1,0,0,0,0,
                    0,0,0,0,0,0,0,0,0,0,0,0
            },
            //9
            {
                    0,0,0,0,0,0,0,0,0,0,0,0,
                    0,0,0,0,1,1,1,1,0,0,0,0,
                    0,0,0,1,0,0,0,0,1,0,0,0,
                    0,0,0,1,0,0,0,0,0,1,0,0,
                    0,0,0,1,0,0,0,0,0,1,0,0,
                    0,0,0,1,0,0,0,0,1,1,0,0,
                    0,0,0,0,1,1,1,1,0,1,0,0,
                    0,0,0,0,0,0,0,0,0,1,0,0,
                    0,0,0,0,0,0,0,0,0,1,0,0,
                    0,0,0,0,0,0,0,0,1,0,0,0,
                    0,0,0,0,1,1,1,1,0,0,0,0,
                    0,0,0,0,0,0,0,0,0,0,0,0
            }

    };

    public static void main(String[] args) {
        InitNet();

        System.out.println("First, see how the NN performs before training");
        RunNet();
        input.nextLine();

        System.out.println("Now training network");
        TrainNet();
        input.nextLine();

        System.out.println("Now see how the NN performs after training");
        RunNet();
        input.nextLine();
    }

    public static double sig(double x) {

        return (1/ (1 + Math.exp(-GAIN * x)));

    }

    public static void DoNet() {
        double sum;

        //Run through nodes in hidden layer
        for (int i = 0; i < NUM_HL; i++) {
            sum = 0.0;
            for (int j = 0; j < NUM_IL; j++) {
                sum += hl[i].wt.get(j) * curImg[j];
            }
            hl[i].act = sig(sum - hl[i].th);
        }

        //Run through nodes in output layer
        for (int i = 0; i < NUM_OL; i++) {
            sum = 0.0;
            for (int j = 0; j < NUM_OL; j++) {
                sum += ol[i].wt.get(j) * hl[j].act;
            }
            ol[i].act = sig(sum - ol[i].th);
        }
    }

    public static void TrainNet() {
        int numCycles;
        int current;
        double sum;
        Random random = new Random();

        System.out.print("How many training cycles? ");
        numCycles = input.nextInt();

        for (int i = 0; i < numCycles; i++) {
            current = random.nextInt(10);
            curImg = img[current];

            DoNet();

            //Calculate OL Error
            for (int j = 0; j < NUM_OL; j++) {
                if (j == current) {
                    ol[j].e = (1 - ol[j].act) * GAIN * ol[j].act * (1 - ol[j].act);
                }
                else {
                    ol[j].e = (0 - ol[j].act) * GAIN * ol[j].act * (1 - ol[j].act);
                }
            }

            //Calculate HL Error
            for (int j = 0; j < NUM_HL; j++) {
                sum = 0.0;
                for (int k = 0; k < NUM_OL; k++) {
                    sum += ol[k].e * ol[k].wt.get(j);
                }
                hl[j].e = GAIN * hl[j].act * (1 - hl[j].act) * sum;
            }

            //Adjust Weights
            for (int j = 0; j < NUM_OL; j++) {
                for (int k = 0; k < NUM_HL; k++) {
                    ol[j].wt.set(k, ol[j].wt.get(k) + LR * ol[j].e * hl[k].act);
                }
                ol[j].th += LR * ol[j].e;
            }

            for (int j = 0; j < NUM_HL; j++) {
                for (int k = 0; k < NUM_IL; k++) {
                    hl[j].wt.set(k, hl[j].wt.get(k) + LR * hl[j].e * curImg[k]);
                }
                hl[j].th += LR * hl[j].e;
            }

            System.out.print(".");
        }
    }

    public static void RunNet() {
        int num;

        System.out.print("Which number do you want to test? ");
        num = input.nextInt();

        curImg = img[num];

        DoNet();

        for (int i = 0; i < NUM_OL; i++) {
            System.out.printf("Value of output node %d is %8f\n", i, ol[i].act);
        }
    }

    public static void InitNet() {
        Random random = new Random();

        //Hidden Layer
        for (int i = 0; i < NUM_HL; i++) {
            hl[i] = new Node();
            for (int j = 0; j < NUM_IL; j++) {
                hl[i].wt.add((random.nextDouble() - 0.5) * 0.1);
            }
        }

        //Output Layer
        for (int i = 0; i < NUM_OL; i++) {
            ol[i] = new Node();
            for (int j = 0; j < NUM_HL; j++) {
                ol[i].wt.add((random.nextDouble() - 0.5) * 0.1);
            }
        }
    }
}
