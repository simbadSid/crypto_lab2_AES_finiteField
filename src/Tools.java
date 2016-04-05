



public class Tools
{
	public static int multiplyMatrixByPoly(int[] matrix, int poly, int addPoly, int matrixXSize)
	{
		int res		= 0;
		int maskI	= 1;

		for (int i=0; i<matrix.length; i++)				// For each line of the matrix A
		{
			int xi		= (matrix[i] & poly);			//		xi = A * y  (line i)
			int bi		= (poly & maskI);				//		bi will store the bit result[i]
			int maskJ	= 1;
			for (int j=0; j<matrixXSize; j++)			//		Count the number of 1 in xi
			{
				if ((xi & maskJ) == 1)					//			If the j th bit of xi is 1 then inverse the result bit
				{
					bi = (~bi) & maskI;
				}
				maskJ = maskJ << 1;
			}
			res = res | bi;
			maskI = maskI << 1;
		}

		return res;
	}

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