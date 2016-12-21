import java.util.Scanner;

public class Main {

	/**
	 * 描述: TODO 变量 @param args
	 */
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		while (s.hasNextLine()) {
			int n = Integer.parseInt(s.nextLine());
//			int num = (n * n - n) / 2 + n;
			int[][] array = new int[n][];
			int count = 1;
			for (int i = 0; i < n; i++) {
				array[i] = new int[n-i];
				for (int j = i; j >= 0; j--) {
					int k = i - j;
					array[j][k] = count;
					count++;
				}
			}
			for (int i = 0; i < array.length; i++) {
				for (int j = 0; j < array[i].length; j++) {
					System.out.print(array[i][j]);
					if (j != array[i].length-1) {
						System.out.print(" ");
					}
				}
				System.out.println();
			}
		}
	}

}
