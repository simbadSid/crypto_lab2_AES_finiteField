
public class AES
{

// ----------------------------------------
// Attributes
// ----------------------------------------
	private int		nbrRound		= 2;
	private int		maxPolyDegree	= 3;
	private int		polynomialRank	= 2;
	private int[][]	A				= {{1, 1, 1}, {0, 1, 1}, {0, 0, 1}};
	private int[]	c				= {1, 0, 1};
	private int[]	kernelPoly;

// ----------------------------------------
// Local methods
// ----------------------------------------
	private int[] subByte(int[] xVect)
	{
		int[] y;

		y = Tools.inverse(xVect, polynomialRank, maxPolyDegree);
		y = Tools.matrixMult(A, y);
		y = Tools.vectorAdd(y, c);
		y = Tools.vectorModulo(y, polynomialRank);

		return y;
	}

	private static void keySchedule()
	{
		// Custom rotation
	}

// ----------------------------------------
// Main methods
// ----------------------------------------
	public static void main(String[] args)
	{

	}

}
