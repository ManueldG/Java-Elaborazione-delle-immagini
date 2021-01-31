class Util
{
	
	private static
	int partition( double[] a, int p, int r )
	{
		double	x = a[p];
		int i = p - 1;
		int j = r + 1;

		
		while(true)
		{
/*			
			do {
				--j;
			} while( a[j] > x );
			
			do { 
				++i;
			} while( a[i] < x );
*/			
			while( a[--j] < x ) 
			{
			}
				
			while( a[++i] > x ) 
			{
			}

			if ( i < j )
			{
				double t = a[i];
				a[i] = a[j];
				a[j] = t;
			}
			else
				return j;
		}
	}
	
	
	public static
	double select( double a[], int sx, int dx, int i )
	{
		if ( sx == dx )
			return a[sx];
			
		int q = partition( a, sx, dx );
		int k = q - sx + 1;
		if ( i <= k )
			return select( a, sx, q, i);
		else
			return select( a, q+1, dx, i-k);
	}


	public static
	void sort( double a[], int len ) {
		for( int i =0; i < len; i++ )
			for( int j= i+1; j < len; j++ )
				if (a[i]>a[j] )
				{
					double t = a[i];
					a[i] = a[j];
					a[j] = t;
				}
	}


	private static
	int partition( int[] a, int p, int r )
	{
		int	x = a[p];
		int i = p - 1;
		int j = r + 1;

//		int i = p;
//		int j = r;
		

//		System.out.println("* partition , p="+ p + " r=" + r + "x=" + x);
		
		while(true)
		{
			while( a[--j] < x ) {
//				j--;
			}
				
			while( a[++i] > x ) 
			{
//				i++;
			}
		

//		System.out.println("** partition, p="+ p + " r=" + r + "x=" + x);

				
			if ( i < j )
			{
				int t = a[i];
				a[i] = a[j];
				a[j] = t;
			}
			else
				return j;
		}
	}

	public static
	int	select( int a[], int sx, int dx, int i )
	{
		if ( sx == dx )
			return sx;
			
		int q = partition( a, sx, dx );
		int k = q - sx + 1;
		if ( i <= k )
			return select( a, sx, q, i);
		else
			return select( a, q+1, dx, i-k);
	}


	public static 
	void main(String args[]) {
		int	[]a = new int[100];
		
		for( int i=0; i < 100; i++ )
			a[i] = 300 - i * 3;

		int	pos = select( a, 0, 99, 90 );

//		System.out.println( "pos: " + pos  );
		System.out.println( "pos: " + pos + " value:" + a[pos] );
    }
	
}
