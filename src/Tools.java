



public class Tools
{
	public static int[] matrixMult(int[][] matrix, int[] vector)
	{
		if (matrix.length != vector.length) throw new RuntimeException("The matrix and the vector are not compatible for multiplication");

		int		xLength	= matrix.length;
		int		yLength	= matrix[0].length;
		int[]	res		= new int[yLength];

		for (int y=0; y<yLength; y++)
		{
			res[y] = 0;
			for (int x=0; x<xLength; x++)
			{
				res[y] += matrix[x][y] * vector[x];
			}
		}

		return res;
	}

	public static int[] vectorAdd(int[] vector1, int[] vector2)
	{
		if (vector1.length != vector2.length) throw new RuntimeException("The 2 vector have not the same dim");

		int		dim	= vector1.length;
		int[]	res	= new int[dim];

		for (int i=0; i<dim; i++)
			res[i] = vector1[i] + vector2[i];

		return null;
	}

	public static int[] vectorModulo(int[] vector, int a)
	{
		int[] res = new int[vector.length];

		for (int i=0; i<vector.length; i++)
			res[i] = vector[i] % a;

		return res;
	}

	/**========================================================</n>
	 * Return the inverse of the vector within F(rank^degree)</n>
	 ==========================================================*/
	public static int[] inverse(int[] vector, int polynomialRank, int polynomialDegree)
	{
		if (polynomialRank	<= 0)	throw new RuntimeException("Polynomial rank must be strictly positive: " + polynomialRank);
		if (polynomialDegree<= 0)	throw new RuntimeException("Polynomial degree must be strictly positive: " + polynomialDegree);
//TODO
		return null;
	}
}
