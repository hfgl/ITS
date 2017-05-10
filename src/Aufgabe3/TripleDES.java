package Aufgabe3;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

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

		System.out.println(Arrays.toString(key1));
		System.out.println(Arrays.toString(key2));
		System.out.println(Arrays.toString(key3));
		System.out.println(Arrays.toString(iniVector));
		
		//use CFB on keys
		key1 = CFB(iniVector, key1);
		key2 = CFB(key1, key2);
		key3 = CFB(key2, key3);

		if(status.equals("encrypt"))
		{
			useDES(inputFile, outputFile, key1, EncryptionMode.encrypt, false);
			useDES(outputFile, inputFile, key1, EncryptionMode.decrypt, true);
			//useDES(inputFile, outputFile, key3, EncryptionMode.encrypt);
		}
		else if(status.equals("decrypt"))
		{
			useDES(inputFile, outputFile, key3, EncryptionMode.decrypt, false);
			useDES(outputFile, inputFile, key2, EncryptionMode.encrypt, false);
			useDES(inputFile, outputFile, key1, EncryptionMode.decrypt, true);
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
	
	private byte[] CFB(byte[] cipher, byte[] plaintext) //byte[] key
	{
	    // encryption with e()
	    // e(cipher, key)
        // decryption with d()
        // d(cipher, key)

	    byte[] xor = new byte[plaintext.length];
	    for(int i = 0; i < plaintext.length; i++){
            xor[i] = (byte) (cipher[i]^plaintext[i]);
        }
		return xor;
	}

	private int lastByteLength = 8;
	private int whichOutput = 0;

	private void useDES(String inputFile, String outputFile, byte[] key, EncryptionMode cipherMode, boolean out) throws IOException
	{
		FileInputStream inputStream = new FileInputStream(inputFile); 
		FileOutputStream outputStream = new FileOutputStream(outputFile); 
		DES des = new DES(key);
		int len; 
		byte[] input = new byte[8];
		byte[] output = new byte[8];

		int i = 0;
		while ((len = inputStream.read(input)) > 0) 
		{
            if(len != 8){
                lastByteLength = len;
                whichOutput = i;
                for(int c = len; c <= 7; c++){
                    System.out.println(c);
                    input[c] = 1;
                }
            }
			if(cipherMode == EncryptionMode.encrypt)
			{

				System.out.println("Input Encrypt: " + i + " " + Arrays.toString(input));
				des.encrypt(input, 0, output, 0);
				System.out.println("Output Encrypt: " + i + " " + Arrays.toString(output));
			}
			else
			{
                System.out.println("Input Decrypt: " + i + " " + Arrays.toString(input));
				des.decrypt(input, 0, output, 0);
                System.out.println("Output Decrypt: " + i + " " + Arrays.toString(output));
			}

			if(out && i == whichOutput && lastByteLength != 8){
                outputStream.write(output, 0, lastByteLength);
                lastByteLength = 8;
            } else {
                outputStream.write(output, 0, 8);
            }
			i++;
		}
		inputStream.close();
		outputStream.close();
	}
}
