
public class AES
{

// ----------------------------------------
// Attributes
// ----------------------------------------
	private final int	nbrRound		= 2;
	private final int	nbrBlockX		= 2;
	private final int	nbrBlockY		= 2;
	private final int	nbrBlock		= nbrBlockX * nbrBlockY;
	private final int	sizeBlock		= 3;
	private final int[]	A				= {4, 6, 7};					// Matrix		1 0 0
																		//				1 1 0
																		//				1 1 1

	private final int	c				= 5;							// Vector		1 0 1
	private final int	kernelPoly		= 11;							// Polynomial	X^3 + X + 1
	private final int[]	inversIn2		= {0, 1, 5, 6, 7, 2, 3, 4};		// Tab[i] = Inverse of i in F(2^3)
																		// F(2^3) = (X1 X2 X3) where Xi in [0, 1]
	private final int[]	s_box;
// ----------------------------------------
// Builder
// ----------------------------------------
	public AES()
	{
		if (A.length != sizeBlock)
			throw new RuntimeException("Uncoherent data");

		this.s_box = this.S_box();
	}

// ----------------------------------------
// Local methods
// ----------------------------------------
	/**
	 * @return the vector z = A * y + c, where <nl>
	 * - y is the inverse of the input vector (vector of bits) within F(2^3) <nl>
	 * - A is 
	 */
	public int subByte(int a)
	{
		if ((a < 0) || (a >= Math.pow(2, sizeBlock)))
			throw new RuntimeException("The given block size excedes the considered block size (" + sizeBlock + ")");

		int y		= inversIn2[a];
		int res		= 0;
		int maskI	= 1;

		for (int i=0; i<A.length; i++)				// For each line of the matrix A
		{
			int xi		= (A[i] & y);				//		xi = A * y  (line i)
			int bi		= (c & maskI);				//		bi will store the bit result[i]
			int maskJ	= 1;
			for (int j=0; j<sizeBlock; j++)			//		Count the number of 1 in xi
			{
				if ((xi & maskJ) == 1)				//			If the j th bit of xi is 1 then inverse the result bit
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

	public int[] S_box()
	{
		int		nbrElem	= (int) (Math.pow(2, sizeBlock));
		int[]	res		= new int[nbrElem];

		for (int i=0; i<nbrElem; i++)
			res[i] = this.subByte(i);

		return res;
	}

	/**
	 * @return the list res[][] of keys used at each iteration.<nl>
	 * res[i][b] is the sub key (block) of the key used at the i th iteration.
	 */
	public int[][] keySchedule(int[] keyInitial)
	{
		if (keyInitial.length != nbrBlock)
			throw new RuntimeException("The input key must be composed of " + nbrBlock +" blocks");

		int[][] res = new int[nbrRound][nbrBlock];
		int T;

		for (int j=0; j<nbrBlock; j++)					// Init key
			res[0][j] = keyInitial[j];

		for (int i=1; i<nbrRound; i++)					// For each created key:
		{
			T = res[i-1][nbrBlock-1];
			T = Tools.leftCyclicShift(T, 2, sizeBlock);
			T = s_box[T];
//TODO			T = T ^ (X^i mod P8);
			res [i][0] = res[i-1][0] ^ T;
			res [i][1] = res[i-1][1] ^ res[i][0];
			res [i][2] = res[i-1][2] ^ res[i][1];
			res [i][3] = res[i-1][3] ^ res[i][2];
		}

		return res;
	}

// ----------------------------------------
// Main methods
// ----------------------------------------
	public static void main(String[] args)
	{
		AES coder = new AES();
		int[] sbox = coder.S_box();
int a = 34;
int b = 52;
System.out.println("a   = " + Tools.bitRepresentation(a, 6));
System.out.println("b   = " + Tools.bitRepresentation(b, 6));
System.out.println("a^b = " + Tools.bitRepresentation((a^b), 6));
//		for (int i = 0; i<sbox.length; i++)
//			System.out.println("\t" + i + "\t-> " + sbox[i]);

	}
}