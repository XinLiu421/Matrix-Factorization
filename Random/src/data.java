import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Random;


public class data {
	public static void main(String[] args){
		File train = new File("C:\\Users\\liuxin-pc\\Desktop\\NF_test.dat");
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new FileReader(train));
			String tempString = null;
			
			FileOutputStream out = new FileOutputStream("C:\\Users\\liuxin-pc\\Desktop\\NTNF_test.dat",true);
			OutputStreamWriter osw = new OutputStreamWriter(out, "utf-8");
			BufferedWriter bwr = new BufferedWriter(osw);
			int count =0;
			while((tempString = reader.readLine())!=null){
				count++;
				String[] content = tempString.split("::");
				int user = Integer.parseInt(content[0]);
				int item = Integer.parseInt(content[1]);
				int score = Integer.parseInt(content[2]);
				bwr.write(user + "::" +item + "::" +score + "\r\n");
				bwr.flush();
				System.out.println(count);
			}
			bwr.close();
			System.out.println("end");
		}catch(IOException e){
			e.printStackTrace();
		}
		finally{
			if(reader!=null){
				try{
					reader.close();
				}catch(IOException e){
					
				}
			}
		}
		int t = 1094785734;
	}	
}

