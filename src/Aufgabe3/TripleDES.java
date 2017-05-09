package Aufgabe3;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import Aufgabe3.Enums.EncryptionMode;



public class TripleDES 
{
	public void Cipher(String inputFile, String keyFile, String outputFile, String status) throws IOException
	{
		FileInputStream in;
		
		//read keys from keyFile
		in = new FileInputStream(keyFile);
		byte[] key1 = readBlock(in);
		byte[] key2 = readBlock(in);
		byte[] key3 = readBlock(in);
		byte[] iniVector = readBlock(in);
		in.close();
		
		//use CFB on keys
		key1 = CFB(iniVector, key1);
		key2 = CFB(key1, key2);
		key3 = CFB(key2, key3);
		
		if(status.equals("encrypt"))
		{
			UseDES(inputFile, outputFile, key1, EncryptionMode.encrypt);
			UseDES(outputFile, inputFile, key2, EncryptionMode.decrypt);
			UseDES(inputFile, outputFile, key3, EncryptionMode.encrypt);
		}
		else if(status.equals("decrypt"))
		{
			UseDES(inputFile, outputFile, key3, EncryptionMode.decrypt);
			UseDES(outputFile, inputFile, key2, EncryptionMode.encrypt);
			UseDES(inputFile, outputFile, key1, EncryptionMode.decrypt);
		}
		else
		{
			throw new IllegalArgumentException("Wrong status");
		}
	}
	
	private byte[] readBlock(FileInputStream in) throws IOException{
		byte[] buffer = new byte[8];
		in.read(buffer);
		return buffer;
	}
	
	private byte[] CFB(byte[] c, byte[] k)
	{
		return k;
	}
	
	private void UseDES(String inputFile, String outputFile, byte[] key, EncryptionMode cipherMode) throws IOException
	{
		FileInputStream inputStream = new FileInputStream(inputFile); 
		FileOutputStream outputStream = new FileOutputStream(outputFile); 
		DES des = new DES(key);
		int len; 
		byte[] input = new byte[8];
		byte[] output = new byte[8];
		
		while ((len = inputStream.read(input)) > 0) 
		{	
			if(cipherMode == EncryptionMode.encrypt)
			{
				des.encrypt(input, 0, output, 0);
			}
			else
			{
				des.decrypt(input, 0, output, 0);
			}
			
			outputStream.write(output, 0, len); 
		}
		inputStream.close();
		outputStream.close();
	}
}
