package pl.markov;

import com.sun.corba.se.impl.resolver.SplitLocalResolverImpl;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Scanner;

public class Environment {

    private Square [][] Map;
    private int n, m; // n,m - map sizes
    private double p1, p2, p3, reward, discounting; // p* - moves probabilities

    void showFile(String filename) {

        try {
            File myFile = new File(filename);
            Scanner myReader = new Scanner(myFile);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);

            }
            myReader.close();
            } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    void showEnvironment() {
        System.out.println(this.getSize()[0] + " " + this.getSize()[1]);
        System.out.println(this.getP()[0] + " " + this.getP()[1] + " " + this.getP()[2]);
        System.out.println(this.getReward());
        System.out.println(this.getDiscounting());

        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j< this.n; j++) {
                System.out.print(this.Map[i][j].getType());
                System.out.print(" ");
                if (this.Map[i][j].getType() == SquareType.Terminal) {
                    System.out.print(this.Map[i][j].getReward());
                    System.out.print(" ");
                }
            }
            System.out.println();
            }
    }


    void showUtility() {

        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j< this.n; j++) {
                System.out.print(new DecimalFormat("#0.0000").format(this.Map[i][j].getUtility()));
                System.out.print(" ");
                }
            System.out.println();
            }
    }


    void readFile(String filename) {

        try {
            String tmp = new String();
            File myFile = new File(filename);
            Scanner myReader = new Scanner(myFile);

            this.n = myReader.nextInt();
            this.m = myReader.nextInt();
            this.p1 = myReader.nextDouble();
            this.p2 = myReader.nextDouble();
            this.p3 = myReader.nextDouble();
            this.reward = myReader.nextDouble();
            this.discounting = myReader.nextDouble();

            Map = new Square[this.m][this.n];

            for (int i = 0; i < this.m; i++) {
                for (int j = 0; j< this.n; j++){
                    Map[i][j] = new Square();

                    tmp = myReader.next();
                    // System.out.println(tmp);
                    if (tmp.charAt(0) == 'S') {
                        this.Map[i][j].setType(SquareType.Start);
                        this.Map[i][j].setReward(this.reward);
                        this.Map[i][j].setUtility(this.reward);
                    }
                    else if (tmp.charAt(0) == 'N') {
                        this.Map[i][j].setType(SquareType.Normal);
                        this.Map[i][j].setReward(this.reward);
                        this.Map[i][j].setUtility(this.reward);
                    }
                    else if (tmp.charAt(0) == 'F') {
                        this.Map[i][j].setType(SquareType.Prohibited);
                        this.Map[i][j].setReward(0);
                        this.Map[i][j].setUtility(0);
                    }
                    else if (tmp.charAt(0) == 'T') {
                        this.Map[i][j].setType(SquareType.Terminal);
                        this.Map[i][j].setReward(Double.parseDouble(myReader.next()));
                        this.Map[i][j].setUtility(this.Map[i][j].getReward());
                    }
                    else if (tmp.charAt(0) == 'B') {
                        this.Map[i][j].setType(SquareType.Special);
                        this.Map[i][j].setReward(Double.parseDouble(myReader.next()));
                        this.Map[i][j].setUtility(this.Map[i][j].getReward());
                    }


                }
            }



            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public Square[][] getMap() {
        return Map;
    }

    public int[] getSize() {
        return new int[]{n, m};
    }

    public double[] getP() {
        return new double[]{p1, p2, p3};
    }

    public double getDiscounting() {
        return discounting;
    }

    public double getReward() {
        return reward;
    }

    public void calculateUtility() throws IOException {
        File fout = new File("out.txt");
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        double[] actions_utilities = new double[4];
        double err = 1;
        double e = 0;
        double U, D, L, R, Max;


        double p4 = 1 - this.p1 - this.p2 - this.p3;

        // loop works while error of previous iteration is less than 0.0001
        while (err > 0.0001)
        {
            // writing to file
            for(int i = 0; i < this.m; i++)
            {
                for(int j = 0; j < this.n; j++)
                {
                    bw.write(String.valueOf(BigDecimal.valueOf(this.Map[i][j].getUtility()).setScale(3, RoundingMode.HALF_UP).doubleValue()) + ";");
                }
            }
            bw.newLine();

            // cleaning iteration error value for new iteration
            err = 0;

            for(int i = 0; i < this.m; i++)
            {
                for(int j = 0; j < this.n; j++)
                {
                    if(Map[i][j].getType() != SquareType.Terminal && Map[i][j].getType() != SquareType.Prohibited )
                    {
                        // if up is not existing square
                        if(i == 0) U = Map[i][j].getUtility();
                        // else up is
                        else U = Map[i - 1][j].getUtility();
                        // if down is not existing square
                        if(i == this.m - 1) D = Map[i][j].getUtility();
                        // else down is
                        else D = Map[i + 1][j].getUtility();
                        // if left is not existing square
                        if(j == 0) L = Map[i][j].getUtility();
                        // else left is
                        else L = Map[i][j - 1].getUtility();
                        // if right is not existing square
                        if(j == this.n - 1) R = Map[i][j].getUtility();
                        // else right is
                        else R = Map[i][j + 1].getUtility();

                        // if up is prohibited square
                        if (i > 0 && Map[i - 1][j].getUtility() == 0) { U = Map[i][j].getUtility();  }
                        // if down is prohibited square
                        if(i < this.m-1 && Map[i + 1][j].getUtility() == 0) { D = Map[i][j].getUtility();  }
                        // if left is prohibited square
                        if(j > 0 && Map[i][j - 1].getUtility() == 0) { L = Map[i][j].getUtility();  }
                        // if right is prohibited square
                        if(j < this.n-1 && Map[i][j + 1].getUtility() == 0) { R = Map[i][j].getUtility();   }

                        //p1 < p2 ^ p3 >
                        // utilities for each action from current square
                        actions_utilities[0] = D * this.p1 + L * this.p2 + U * this.p3 + R * p4; // Left
                        actions_utilities[1] = L * this.p1 + U * this.p2 + R * this.p3 + D * p4; // Up
                        actions_utilities[2] = U * this.p1 + R * this.p2 + D * this.p3 + L * p4; // Right
                        actions_utilities[3] = R * this.p1 + D * this.p2 + L * this.p3 + U * p4; // Down

                        // choosing action with the largest utility
                        Max = max(actions_utilities);
                        // setting this action for square as an optimal action
                        Map[i][j].setOptimalAction(SquareAction.values()[index(actions_utilities)]);
                        // calculating error
                        e = Math.abs(Map[i][j].getReward() + this.discounting * Max - Map[i][j].getUtility());
                        // setting utility for current square
                        Map[i][j].setUtility(Map[i][j].getReward() + this.discounting * Max);
                        // calculating total error of iteration
                        err += e;
                    }
                    else Map[i][j].setOptimalAction(SquareAction.None);
                }
            }
        }
        bw.close();
    }

    public double max(double a[])
    {
        double max = 0;
        for (int i = 0; i < 4; i++)
        {
            if (a[i] > max) max = a[i];
        }
        return max;
    }

    public int index(double a[])
    {
        int index = 0;
        double max = 0;
        for (int i = 0; i < 4; i++)
        {
            if (a[i] > max)
            {
                max = a[i];
                index = i;
            }
        }
        return index;
    }

    public void showOptimalActions()
    {
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j< this.n; j++) {
                if (this.Map[i][j].getOptimalAction() == SquareAction.Left) System.out.print("<");
                if (this.Map[i][j].getOptimalAction() == SquareAction.Up) System.out.print("^");
                if (this.Map[i][j].getOptimalAction() == SquareAction.Right) System.out.print(">");
                if (this.Map[i][j].getOptimalAction() == SquareAction.Down) System.out.print("v");
                if (this.Map[i][j].getOptimalAction() == SquareAction.None &&
                        this.Map[i][j].getType() == SquareType.Prohibited) System.out.print("O");
                if (this.Map[i][j].getOptimalAction() == SquareAction.None &&
                        this.Map[i][j].getType() == SquareType.Terminal) System.out.print(this.Map[i][j].getReward());
                if (this.Map[i][j].getOptimalAction() == SquareAction.None &&
                        this.Map[i][j].getType() == SquareType.Special) System.out.print(this.Map[i][j].getReward());
                System.out.print(" ");
            }
            System.out.println();
        }
    }

}

