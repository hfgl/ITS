import java.io.IOException;

import Aufgabe1.LCG;
import Aufgabe2.HC1;
import Aufgabe3.TripleDES_CFB;

public class Main 
{
	public static void main(String[] args)
	{
		try 
		{
			//Aufgabe1
			LCG lcg = new LCG(65489432);
			for(int i = 0; i < 256; i++)
			{
				System.out.println(lcg.nextInt() & 0x000000FF);	
			}
			
			//Aufgabe2
			HC1.CipherLCG("src/testLCG.txt", 65489432);
			HC1.CipherSecureRandom("src/testRandom.txt", 65489432);



			//Aufgabe3
			TripleDES_CFB tripleDES_cfb = new TripleDES_CFB();
			tripleDES_cfb.Cipher(
					System.getProperty("user.dir") + "\\src\\Aufgabe3\\ITSAufgabe3.pdf",
					System.getProperty("user.dir") + "\\src\\Aufgabe3\\3DESTest.key",
					System.getProperty("user.dir") + "\\src\\Aufgabe3\\3DESTest.enc",
					"encrypt"
			);

			tripleDES_cfb = new TripleDES_CFB();
			tripleDES_cfb.Cipher(
				System.getProperty("user.dir") + "\\src\\Aufgabe3\\3DESTest.enc",
				System.getProperty("user.dir") + "\\src\\Aufgabe3\\3DESTest.key",
				System.getProperty("user.dir") + "\\src\\Aufgabe3\\3DESTest.pdf",
				"decrypt"
			);

		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
