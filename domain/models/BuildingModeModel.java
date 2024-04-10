package domain.models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BuildingModeModel {


    public BuildingModeModel() {}
    public void update(long currentTime, boolean[] keys, int WIDTH, int HEIGHT) {

    }
    
    private int[][] readTxt(String fileName){
        int[][] matrix = new int[11][10];
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int row = 0;
            
            while ((line = br.readLine()) != null) {
                String[] elements = line.split(" ");
                for (int col = 0; col < elements.length; col++) {
                    matrix[row][col] = Integer.parseInt(elements[col]);
                }
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matrix;
    }

    private void writeTxt(String fileName, int[][] matrix) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    writer.write(matrix[i][j] + " ");
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
