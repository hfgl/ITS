package Aufgabe1;

public class LCG 
{
	private int currentNumber;
	private long a = 2147001325;
	private long b = 715136305;
	private long m = (long) Math.pow(3, 32);
	
	public LCG(int seed)
	{
		currentNumber = seed;
	}
	
	public int nextInt() 
	{
		int nextNumber =(int) (((a*currentNumber)+b)%m);
		currentNumber = nextNumber;
		return currentNumber;
	}
}
