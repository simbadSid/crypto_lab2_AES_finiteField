



public class Tools_F_2_power_3
{
	/**
	 * matrix[i] is the i line (polynomial)
	 */
	public static int multiplyMatrixByPoly(int[] matrix, int poly, int addPoly, int matrixXSize)
	{
		int res		= 0;
		int maskI	= 1;

		for (int i=0; i<matrix.length; i++)				// For each line of the matrix A
		{
			int li		= inverseNFirstBit(matrix[i], matrixXSize);
			int xi		= (li & poly);					//		xi = A * y  (line i)
			int bi		= addPoly;						//		bi will store the bit result[i]
			int maskJ	= 1;
			for (int j=0; j<matrixXSize; j++)			//		Count the number of 1 in xi
			{
				if ((xi & maskJ) != 0)					//			If the j th bit of xi is 1 then inverse the result bit
				{
					bi = (~bi);
				}
				maskJ = maskJ << 1;
			}
			res = res | (bi & maskI);
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

	/**
	 * a   = ....|an a(n-1)... a2     a1 <nl>
	 * res = ....|a1 a2    ... a(n-1) an
	 */
	public static int inverseNFirstBit(int a, int n)
	{
		int res;
		int maskI		= 1;
		int maskI_inv	= (int) Math.pow(2, (n-1));

		res = (int) (Math.pow(2, n) - 1);
		res = (~res) & a;

		for (int i=0; i<n; i++)
		{
			if ((a & maskI) != 0)
			{
				res = res | maskI_inv;
			}

			if ((a & maskI_inv) != 0)
			{
				res = res | maskI;
			}
			maskI		= maskI		<< 1;
			maskI_inv	= maskI_inv	>> 1;
		}

		return res;
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