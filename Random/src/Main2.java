import java.util.Scanner;


public class Main2 {

	/**
	 * 描述: TODO
	 * 变量 @param args
	 */
	
	public char[] encrypt(char[] encrypt){
		int length = encrypt.length;
		for(int i=0;i<length;i++){
			int asc = (int)encrypt[i];
			if(asc>=97&&asc<=122){
				if(asc!=122){
					encrypt[i] = (char)(asc+1-32);
				}else{
					encrypt[i] = (char)(97-32);
				}
			}
			if(asc>=65&&asc<=90){
				if(asc!=90){
					encrypt[i] = (char)(asc+1+32);
				}else{
					encrypt[i] = (char)(65+32);
				}
			}
			if(asc>=48&&asc<=57){
				if(asc!=57){
					encrypt[i] = (char)(asc+1);
				}else{
					encrypt[i] = (char)(48);
				}
			}
		}
		return encrypt;		
	}
	
	public char[] decrypt(char[] decrypt){
		int length = decrypt.length;
		for(int i=0;i<length;i++){
			int asc = (int)decrypt[i];
			if(asc>=97&&asc<=122){
				if(asc!=97){
					decrypt[i] = (char)(asc-1-32);
				}else{
					decrypt[i] = (char)(122-32);
				}
			}
			if(asc>=65&&asc<=90){
				if(asc!=65){
					decrypt[i] = (char)(asc-1+32);
				}else{
					decrypt[i] = (char)(90+32);
				}
			}
			if(asc>=48&&asc<=57){
				if(asc!=48){
					decrypt[i] = (char)(asc-1);
				}else{
					decrypt[i] = (char)(57);
				}
			}
		}
		return decrypt;	
	}
	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		char[] encrypt = s.nextLine().toCharArray();
		char[] decrypt = s.nextLine().toCharArray();
		Main2 b = new Main2();
		System.out.println(b.encrypt(encrypt));
		System.out.println(b.decrypt(decrypt));
	}

}
