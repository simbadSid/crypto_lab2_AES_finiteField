
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
	private final int	kernelPoly		= Tools_F_2_power_3.kernelPoly;	// Polynomial	X^3 + X + 1
	private final int[] mixColumnMatrix = {3, 2};						// Matrix		1 1
																		//				1 0
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

		int y	= Tools_F_2_power_3.inverse(a);
		int res	= Tools_F_2_power_3.multiplyMatrixByPoly(A, y, c, sizeBlock);
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
//TODO
if (i > 1) throw new RuntimeException("Not nbrRound > 2 is handeled yet");
			T = res[i-1][nbrBlock-1];
			T = Tools_F_2_power_3.leftCyclicShift(T, 2, sizeBlock);
			T = s_box[T];
			int xPowerI = (int) Math.pow(2, i);
			int xPowerI_modKernel = Tools_F_2_power_3.moduloPolynomial(xPowerI, kernelPoly);
			T = T ^ (xPowerI_modKernel);
//TODO
//T = T ^ 2;
			res [i][0] = res[i-1][0] ^ T;
			res [i][1] = res[i-1][1] ^ res[i][0];
			res [i][2] = res[i-1][2] ^ res[i][1];
			res [i][3] = res[i-1][3] ^ res[i][2];
		}

		return res;
	}

	/**
	 * Return the mixed msg column by column
	 * @param matrix
	 * @return
	 */
	public int[][] mixColmn(int[][] matrix)
	{
		if (matrix.length	> nbrBlockX)	throw new RuntimeException("Wrong matrix format");
		if (matrix[0].length> nbrBlockY)	throw new RuntimeException("Wrong matrix format");

		int		nbrColumn	= nbrBlock*sizeBlock;
		int[][]	res			= new int[nbrColumn];

		for (int x=0; x<nbrColumn; x++)
		{
			int msgColumn	= getColumnOfMatrix(matrix, x, sizeBlock);
			res [x]			= Tools_F_2_power_3.multiplyMatrixByPoly(mixColumnMatrix, msgColumn, 0, nbrBlockY);
		}
		return res;
	}

	public int[][] cipher (int[][] message, int[] key)
	{
		int[][]	listOfKey		= this.keySchedule(key);
		int[][]	cipheredMessage	= copyMatrix(message);

		for (int round=0; round<nbrRound; round++)
		{
			cipheredMessage	= this.shiftRows(cipheredMessage);
			cipheredMessage	= this.mixColmn(cipheredMessage);
			cipheredMessage = this.addRoundKey(cipheredMessage, listOfKey[round]);
		}
	}

	private static int getColumnOfMatrix(int[][] matrix, int column, int blockSize)
	{
		int res		= 0;
		int x		= column / matrix.length;
		int maskX	= 1 << (blockSize-1 - (column % blockSize));
		int maskY	= 1;

		for (int y=0; y<matrix[0].length; y++)
		{
			int n = matrix[x][y];
			if ((n & maskX) != 0)
			{
				res = res | maskY;
			}
			maskY = maskX << 1;
		}

		return res;
	}

	private static int[][] copyMatrix(int[][] matrix)
	{
		int nbrX = matrix.length;
		int nbrY = matrix[0].length;
		int[][] res = new int[nbrX][nbrY];

		for (int x=0; x<nbrX; x++)
		{
			for (int y=0; y<nbrY; y++)
			{
				res[x][y] = matrix[x][y];
			}
		}

		return res;
	}

// ----------------------------------------
// Main methods
// ----------------------------------------
	public static void main(String[] args)
	{
		AES coder = new AES();

		System.out.println("Question 2: full s-box on " + coder.sizeBlock + "bits");
		int[] sbox = coder.S_box();

		for (int i = 0; i<sbox.length; i++)
			System.out.println("\t\t" + i + "\t-> " + sbox[i]);

		System.out.println("\n\n-----------------------------------");
		System.out.print("Question 3: key schedule on the key");
		int[] key = {3, 4, 4, 1};
		for (int k:key)
			System.out.print("  " + Tools_F_2_power_3.bitRepresentation(k, coder.sizeBlock));
		System.out.println();
		int[][] keySchedul = coder.keySchedule(key);
		for (int i=0; i<keySchedul.length; i++)
		{
			System.out.print("\t\tRound " + i + "\t: ");
			for (int k:keySchedul[i])
			{
				String representation = Tools_F_2_power_3.bitRepresentation(k, coder.sizeBlock);
				System.out.print("   " + representation);
			}
			System.out.println();
		}

		System.out.println("\n\n-----------------------------------");
		System.out.print("Question 4: The mixed column matrix is\n");
		for (int y=0; y<coder.nbrBlockY; y++)
		{
			System.out.println("\t\t" + Tools_F_2_power_3.bitRepresentation(coder.mixColumnMatrix[y], coder.nbrBlockY));
		}
	}
}