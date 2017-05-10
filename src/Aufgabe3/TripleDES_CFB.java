package Aufgabe3;

import Aufgabe3.Enums.EncryptionMode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
			des_cfb_algortihm(iniVector, inputFile, outputFile, key1, false);
			des_cfb_algortihm(iniVector, outputFile, inputFile, key2, false);
			des_cfb_algortihm(iniVector, inputFile, outputFile, key3, true);
		}
		else if(status.equals("decrypt"))
		{
			des_cfb_algortihm(iniVector, inputFile, outputFile, key3,false);
			des_cfb_algortihm(iniVector, outputFile, inputFile, key2,false);
			des_cfb_algortihm(iniVector, inputFile, outputFile, key1,true);
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

	private int lastByteLength = 8;
	private int whichOutput = 0;

	private void des_cfb_algortihm(byte[] initialVector, String inputFile, String outputFile, byte[] key, boolean out) throws IOException {
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
	    try {
            inputStream = new FileInputStream(inputFile);
            outputStream = new FileOutputStream(outputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        DES des = new DES(key);
        int len;
        byte[] input = new byte[8];
        byte[] output = new byte[8];

        int i = 0;
        while ((len = inputStream.read(input)) > 0) {
            if(len != 8){
                System.out.println("Padding gets applied");
                lastByteLength = len;
                whichOutput = i;
                for(int c = len; c <= 7; c++){
                    System.out.println(c);
                    input[c] = 1;
                }
            }

            byte[] xor = new byte[output.length];
            System.out.println("Input DES: "+ Arrays.toString(input));
            des.encrypt(initialVector, 0, output, 0);
            System.out.println("Output DES: "+ Arrays.toString(output));
            for (int o = 0; o < output.length; o++) {
                xor[o] = (byte) (output[o]^ input[o]);
            }
            System.out.println("XOR CFB: "+ Arrays.toString(xor));
            initialVector = xor;

            if(out && i == whichOutput && lastByteLength != 8){
                System.out.println("Padding gets deleted");
                outputStream.write(xor, 0, lastByteLength);
                lastByteLength = 8;
            } else {
                outputStream.write(xor, 0, 8);
            }
            i++;
        }
        inputStream.close();
        outputStream.close();
	}
}
