package Aufgabe2;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import Aufgabe1.LCG;
import Aufgabe2.Enums.RandomMode;

public class HC1 
{
	public static void  CipherLCG(String dataPath, int key) throws IOException
	{
		Cipher(dataPath, key, RandomMode.LCG);
	}
	
	public static void  CipherSecureRandom(String dataPath, int key) throws IOException
	{
		Cipher(dataPath, key, RandomMode.SecureRandom);
	}
	
	private static void  Cipher(String dataPath, int key, RandomMode mode) throws IOException
	{
		byte[] keyBytes = intToByteArray(key);
		SecureRandom random = new SecureRandom(keyBytes);
		LCG lcg = new LCG(key);
		
		//Read the File
		FileInputStream inputstream = new FileInputStream(dataPath);
		int fileSize = (int) inputstream.getChannel().size();
		byte[] data      = new byte[fileSize];
		int    bytesRead = inputstream.read(data);
		inputstream.close();

		FileOutputStream stream = new FileOutputStream(dataPath);
		//Pick 4 bytes and xor them with an random number
		for(int i = 0; i < fileSize; i+=4)
		{
			byte[] dataBytes= new byte[4];
			for(int i2 = 0; i2 < 4; i2++)
			{
				dataBytes[i2] = data.length > i+i2 ? data[i+i2] : 0;
			}
			
			int randomNumber = mode == RandomMode.LCG ? lcg.nextInt() : random.nextInt();
			int dataNumber = ByteArrayToInt(dataBytes);
			int xor = dataNumber ^ randomNumber;
			
			stream.write(intToByteArray(xor));
		}
		stream.close();
	}
	
	public static final byte[] intToByteArray(int value) {
	    return new byte[] {
	            (byte)(value >>> 24),
	            (byte)(value >>> 16),
	            (byte)(value >>> 8),
	            (byte)value};
	}
	
	public static final int ByteArrayToInt(byte[] rno) {
	    return     (rno[0]<<24)&0xff000000|
	    	       (rno[1]<<16)&0x00ff0000|
	    	       (rno[2]<< 8)&0x0000ff00|
	    	       (rno[3]<< 0)&0x000000ff;
	}
}
