



public class Tools
{
	public static int leftCyclicShift(int a, int shift, int nbrInputBit)
	{
		if ((nbrInputBit < 0) || (nbrInputBit > Integer.SIZE))
			throw new RuntimeException("This function can only shift on integer");
		if (a > Math.pow(2, nbrInputBit)-1)
			throw new RuntimeException("The input int contains more significant bit than the expected number of bits");

		int realShift	= shift % Integer.SIZE;
		
		int nbrBitRight	= nbrInputBit - realShift;
		int maskRight	= (int) (Math.pow(2, nbrBitRight) - 1);
		int maskLeft	= (~maskRight) & ((int)Math.pow(2, nbrInputBit)-1);
		
		int resRight	= (a & maskRight)	<< realShift;
		int resLeft		= (a & maskLeft)	>> (nbrInputBit - realShift);

		return (resLeft | resRight);
	}


	public static String bitRepresentation(int a, int nbrBit)
	{
		String res = "";
		int toPrint = a;
		int limit = Math.min(nbrBit, Integer.SIZE);

		for (int i=0; i<limit; i++)
		{
			res 	= (toPrint % 2) + " " + res;
			toPrint	= toPrint >> 1;
		}

		return res;
	}
}