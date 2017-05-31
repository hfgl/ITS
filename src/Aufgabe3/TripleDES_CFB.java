package Aufgabe3;

import Aufgabe3.Enums.EncryptionMode;

import java.io.*;
import java.util.Arrays;


public class TripleDES_CFB
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

		if(status.equals("encrypt"))
		{
            des_cfb_algortihm(iniVector, inputFile, outputFile, key1, key2, key3, EncryptionMode.encrypt);
		}
		else if(status.equals("decrypt"))
		{
            des_cfb_algortihm(iniVector, inputFile, outputFile, key1, key2, key3, EncryptionMode.decrypt);
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

    byte[] vector;

	private byte[] des_cfb_algortihm(byte[] initialVector, String inputFile, String outputFile, byte[] key1, byte[] key2, byte[] key3, EncryptionMode cipherMode) throws IOException {
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        File file = new File(outputFile);
	    try {
            inputStream = new FileInputStream(inputFile);
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        DES des1 = new DES(key1);
        DES des2 = new DES(key2);
        DES des3 = new DES(key3);
        int len;
        byte[] input = new byte[8];
        byte[] output = new byte[8];
        vector = initialVector;

        while ((len = inputStream.read(input)) > 0) {
            byte[] xor = new byte[8];
            System.out.println("Input DES: "+ Arrays.toString(input));

            // E
            output = chiffre(vector, des1, des2, des3);

            for (int o = 0; o < len; o++) {
                xor[o] = (byte) (output[o] ^ input[o]);
            }
            System.out.println("XOR CFB: "+ Arrays.toString(xor));
            outputStream.write(xor, 0, len);

            // XOR
            if(cipherMode == EncryptionMode.encrypt) {
                System.out.println("using xor");
                vector = xor;
            } else {
                System.out.println("using input");
                vector = input.clone();
            }

        }
        inputStream.close();
        outputStream.close();
        return initialVector;
	}

    private byte[] chiffre(byte[] vector, DES des1, DES des2, DES des3) {
        byte[] tmp = new byte[8];
        byte[] tmp2 = new byte[8];
        byte[] output = new byte[8];
        des1.encrypt(vector, 0, tmp, 0);
        des2.decrypt(tmp, 0, tmp2, 0);
        des3.encrypt(tmp2, 0, output, 0);
        return output;
    }

}
