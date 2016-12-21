package Plus.Common;

public class InitParameter {
	/**
	  * ����: ��ʼ����������
	  * ���� @param num
	  * ���� @param factorNum
	  * ���� @return
	  */
	public double[][] init(int num,int factorNumber){
		double[][] d = new double[num][factorNumber];
		double temp = Math.sqrt(factorNumber);
		for(int i = 0; i < num; i++){
			for(int j = 0; j<factorNumber; j++){
				d[i][j] = 0.1* (float)Math.random()/temp;
			}
		}
		return d;
		
	}
	
	public int[][] initN(int userN,int itemN){
		int[][] d = new int[userN][itemN];
		for(int i=0;i<userN;i++){
			for(int j=0;j<itemN;j++){
				d[i][j] = -1;
			}
		}
		return d;
	}
}
