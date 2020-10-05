package com.template.scanner;


import java.util.Arrays;
import java.util.Scanner;

public class ScannerExample {
    public static void main(String args[]) {

        // Scanner example to read an array of characters
        // can be updated to other primitive type.
        Scanner scanner = new Scanner(System.in);

        int row = scanner.nextInt();
        int col = scanner.nextInt();

        char arr[][] = new char[row][col];

        for(int i = 0; i < row; i++){
            for(int j =0; j <col; j++) {
                arr[i][j] = scanner.next().charAt(0);
            }
        }

        System.out.println(Arrays.deepToString(arr));
        scanner.close();
    }
}
