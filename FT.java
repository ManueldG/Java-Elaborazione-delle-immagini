public final class FT {

	private static double[][][]	_uRLookup	= null;
	private static double[][][]	_uILookup	= null;
	private static int[]		_reversedBits	= null;

	private	static int			_levels = 0;
	private	static int			_bits = 0;


	public static
	void FFT(int m, double []re, double []im )
	{
		int	i,j,k,n;
		int	i1,i2,l,l1,l2;
	   
   		double c1,c2,tx,ty,t1,t2,u1,u2,z;
	
		/* Calculate the number of points */
		n = 1;
		for (i=0; i < m; i++) 
			n *= 2;
	
		/* Do the bit reversal */
		i2 = n >> 1;
		j = 0;
		for (i=0; i< n-1; i++) {
		  if (i < j) {
		     tx = re[i];
		     ty = im[i];
		     re[i] = re[j];
		     im[i] = im[j];
		     re[j] = tx;
		     im[j] = ty;
		  }
		  k = i2;
		  while (k <= j) {
		     j -= k;
		     k >>= 1;
		  }
		  j += k;
		}

	
	   /* Compute the FFT */
	   c1 = -1.0; 
	   c2 = 0.0;
	   l2 = 1;
	   for (l=0;l<m;l++) {
	      l1 = l2;
	      l2 <<= 1;
	      u1 = 1.0; 
	      u2 = 0.0;
	      for (j=0; j<l1; j++) {
	         for (i=j; i<n; i+=l2 ) {
				i1 = i + l1;
				t1 = u1 * re[i1] - u2 * im[i1];
				t2 = u1 * im[i1] + u2 * re[i1];
				re[i1] = re[i] - t1; 
				im[i1] = im[i] - t2;
				re[i] += t1;
				im[i] += t2;
	         }
	         z =  u1 * c1 - u2 * c2;
	         u2 = u1 * c2 + u2 * c1;
	         u1 = z;

//	      	System.out.println( "u1: " + u1 + " u2: " + u2 );

	      }
	      c2 = -Math.sqrt((1.0 - c1) / 2.0);
	      c1 = Math.sqrt((1.0 + c1) / 2.0);
	      
//	      System.out.println( "c1: " + c1 + " c2: " + c2 );
	   }
	
	   // Scaling for forward transform 

		for (i=0;i<n;i++) {
			re[i] /= n;
			im[i] /= n;
		}

	}

	public static
	void iFFT(long m, double []re, double []im)
	{
		int	i,j,n;
		int	i1,k,i2,l,l1,l2;

   		double c1,c2,tx,ty,t1,t2,u1,u2,z;
	
		/* Calculate the number of points */
		n = 1;
		for (i=0;i<m;i++) 
			n *= 2;
	
		/* Do the bit reversal */
		i2 = n >> 1;
		j = 0;
		for (i=0; i< n-1; i++) {
		  if (i < j) {
		     tx = re[i];
		     ty = im[i];
		     re[i] = re[j];
		     im[i] = im[j];
		     re[j] = tx;
		     im[j] = ty;
		  }
		  k = i2;
		  while (k <= j) {
		     j -= k;
		     k >>= 1;
		  }
		  j += k;
		}
	
		/* Compute the FFT */
		c1 = -1.0; 
		c2 = 0.0;
		l2 = 1;
		for(l=0; l < m; l++) {
			l1 = l2;
			l2 <<= 1;
			u1 = 1.0; 
			u2 = 0.0;
			for(j=0; j < l1; j++) {
				for (i=j; i < n; i+=l2 ) {
					i1 = i + l1;
					t1 = u1 * re[i1] - u2 * im[i1];
					t2 = u1 * im[i1] + u2 * re[i1];
					re[i1] = re[i] - t1; 
					im[i1] = im[i] - t2;
					re[i] += t1;
					im[i] += t2;
				}
				
				z =  u1 * c1 - u2 * c2;
				u2 = u1 * c2 + u2 * c1;
				u1 = z;
			}
			c2 = Math.sqrt((1.0 - c1) / 2.0);
			c1 = Math.sqrt((1.0 + c1) / 2.0);
	   }
	}
	
	
	public static
	void DFT(int dir,int n, double []x1, double []y1)
	{
	   int i,k;
	   double arg, cosarg,sinarg;
	   double []x2 = new double[ n ];
	   double []y2 = new double[ n ];
	
	
	   for (i=0; i<n; i++) {
	      x2[i] = 0;
	      y2[i] = 0;
	      arg = (-dir * 2 * Math.PI * i) / n;
	      for (k=0; k < n; k++) {
	         cosarg = Math.cos(k * arg);
	         sinarg = Math.sin(k * arg);
	         x2[i] += (x1[k] * cosarg - y1[k] * sinarg);
	         y2[i] += (x1[k] * sinarg + y1[k] * cosarg);
	      }
	   }
	
	   /* Copy the data back */
	   if (dir == 1) {
	      for (i=0;i<n;i++) {
	         x1[i] = x2[i] / n;
	         y1[i] = y2[i] / n;
	      }
	   } else {
	      for (i=0;i<n;i++) {
	         x1[i] = x2[i];
	         y1[i] = y2[i];
	      }
	   }

	}


	private static 
	void computeComplexRotations( int levels ) 
	{
		int ln = (int)FT.log2( levels );
//		int	ln = 8;

		if ( levels != _levels ) { _levels = levels; _uRLookup = null; }
		
		if ( _uRLookup != null ) return;
		
		_uRLookup = new double[ ln ][ 2 ][];
		_uILookup = new double[ ln ][ 2 ][];

		System.out.println("computeComplexRotations: BEGIN levels: " + ln );

		int cc = 0;
		
		int N = 1;
		for( int level = 0; level < ln; level ++ ) 
		{
			// int M = N << level;
			int M = N;
			N <<= 1;


			double	uR = 1;
			double	uI = 0;
			
			double	angle = 2 * Math.PI / M;
			double	wR = Math.cos( angle );
			double	wI = Math.sin( angle );

			_uRLookup[level][0] = new double[ M ];
			_uILookup[level][0] = new double[ M ];
			
			_uRLookup[level][1] = new double[ M ];
			_uILookup[level][1] = new double[ M ];

			for( int j = 0; j < M; j ++ ) 
			{
				_uRLookup[level][0][j] = uR;
				_uILookup[level][0][j] = uI;
				
				double	uwI = uR*wI + uI*wR;
				uR = uR*wR - uI*wI;
				uI = uwI;
				
				cc++;
			}

			// negative sign ( i.e. [M,1] )
			uR = 1;
			uI = 0;
			angle = (-2)* Math.PI / M;
			wR = Math.cos( angle );
			wI = Math.sin( angle );

			for( int j = 0; j < M; j ++ ) 
			{
				_uRLookup[level][1][j] = uR;
				_uILookup[level][1][j] = uI;
					
				double	uwI = uR*wI + uI*wR;
				uR = uR*wR - uI*wI;
				uI = uwI;
			}
		}
		
		System.out.println("computeComplexRotations: END cc:"  + cc);

	}

	private static 
	int	reverseBits( int bits, int n ) 
	{
		int bitsReversed = 0;
		for( int i = 0; i < n; i ++ ) 
		{
			bitsReversed = ( bitsReversed << 1 ) | ( bits & 1 );
			bits = ( bits >> 1 );
		}
		
		return bitsReversed;
	}


	static private 
	int	pow2( int exponent ) 
	{
		if( exponent >= 0 && exponent < 31 ) 
		{
			return	1 << exponent;
		}
		return	0;
	}

	static private 
	double log2( int value ) 
	{
			return	Math.log(value) / Math.log(2);
	}




	static private 
	int[] getReversedBits( int numberOfBits ) 
	{
		if ( _bits != numberOfBits ) { _bits = numberOfBits; _reversedBits = null; }
		
		if ( _reversedBits != null ) 
			return _reversedBits;

		int		maxBits = FT.pow2( numberOfBits );
		int[]	reversedBits = new int[ maxBits ];

System.out.println( " getReversedBits: Bits: " + numberOfBits  + " maxbits: "  +maxBits );
		
		for( int i = 0; i < maxBits; i ++ ) 
		{
			int bits = i;
			int bitsReversed = 0;
			for( int j = 0; j < numberOfBits; j ++ ) 
			{
				bitsReversed = ( bitsReversed << 1 ) | ( bits & 1 );
				bits = ( bits >> 1 );
			}
			reversedBits[ i ] = bitsReversed;
		}
		
		_reversedBits = reversedBits;
		return	reversedBits;
	}


	static private 
	void reorderComplexArray( double[][] data ) 
	{
		int length = data.length;
			
		int[] reversedBits = FT.getReversedBits( (int)FT.log2( length ) );

//		int[] reversedBits = FT.getReversedBits( 8 );
		for( int i = 0; i < length; i ++ ) 
		{
			int swap = reversedBits[ i ];
			if( swap > i ) 
			{
				double re = data[ i ][0];
				double im = data[ i ][1];

				data[ i ][0] = data[ swap ][0];
				data[ i ][1] = data[ swap ][1];

				data[ swap ][0] = re;
				data[ swap ][1] = im;
			}
		}
	}


	private static 
	void	LinearFFT_Quick( double[][] data, int start, int inc, int length, int direction ) 
	{
//		System.out.println("LinearFFT_Quick: length: " + length );
		// copy to buffer
		double [][] buffer = new double[ length ] [2];
		
		int j = start;
		for( int i = 0; i < length; i ++ ) 
		{
			buffer[ i ][0] = data[ j ][0];
			buffer[ i ][1] = data[ j ][1];
			j += inc;
		}

		FFT( buffer, length, direction, true );

		// copy from buffer
		j = start;
		for( int i = 0; i < length; i ++ ) 
		{
			data[ j ][0] = buffer[ i ][0];
			data[ j ][1] = buffer[ i ][1];
			j += inc;
		}
	}

	/* Perform a FFT on an array of complex value. The second index has two possible values: 
	**	0 (zero) for real part and 1 (one) for the imaginary part of the complex number
	*/
	public static 
	void FFT( double[][] data, int length, int direction, boolean quick ) 
	{
		int ln = (int)FT.log2( length );
//		int ln = 8;

		if ( quick == false )
			FT.computeComplexRotations( length );

		
		// reorder array
		FT.reorderComplexArray( data );
		
		// successive doubling
		int N = 1;
		int signIndex = direction;
		
		for( int level = 0; level < ln; level ++ ) 
		{
			int M = N;
			N <<= 1;

			double[] uRLookup = _uRLookup[ level][signIndex ];
			double[] uILookup = _uILookup[ level][signIndex ];

			for( int j = 0; j < M; j ++ ) 
			{
				double uR = uRLookup[j];
				double uI = uILookup[j];

				for( int even = j; even < length; even += N ) 
				{
					int odd	 = even + M;
					
					double	r = data[ odd ][0];
					double	i = data[ odd ][1];

					double	odduR = r * uR - i * uI;
					double	odduI = r * uI + i * uR;

					r = data[ even ][0];
					i = data[ even ][1];
					
					data[ even ][0]	= r + odduR;
					data[ even ][1]	= i + odduI;
					
					data[ odd ][0]	= r - odduR;
					data[ odd ][1]	= i - odduI;
				}
			}
		}
		
		if ( direction == 1 )
		for( int i = 0; i < length; i++ ) {
			data[i][0] /= length;
			data[i][1] /= length;
		}
		
	}


	public static 
	void	FFT2( double[][] data, int xLength, int yLength, int direction ) 
	{
		int xInc = 1;
		int yInc = xLength;

		if( xLength > 1 ) 
		{
			FT.computeComplexRotations( xLength );
			for( int y = 0; y < yLength; y ++ ) 
			{
				int xStart = y * yInc;
				FT.LinearFFT_Quick( data, xStart, xInc, xLength, direction );
			}
		}
		
		if( yLength > 1 ) 
		{
			if ( xLength != yLength )
				FT.computeComplexRotations( yLength );
				
			for( int x = 0; x < xLength; x ++ ) 
			{
				int yStart = x * xInc;
				FT.LinearFFT_Quick( data, yStart, yInc, yLength, direction );
			}
		}
	}

	public static 
	void	FFT2( double[][] data, int xLength, int yLength, int xBlock, int yBlock, int direction ) 
	{
		int	XBLOCK = xBlock;
		int	YBLOCK = yBlock;
		
		int	xBlocks = xLength / XBLOCK;
		int	yBlocks = yLength / YBLOCK;
		
		int	xSize = xLength;
		int	ySize = yLength;
		
		xLength = XBLOCK;
		yLength = YBLOCK;
		
		int xInc = 1;
//		int yInc = xLength;
		int yInc = xSize;

		System.out.println("FFT2x8: xBlocks:" + xBlocks + " yBlocks:" + yBlocks + " xLength:" + xLength + " yLength:"+ yLength);

		if( xBlocks > 0 ) 
		{
			for( int yy = 0; yy < yBlocks; yy++ )
			{
				FT.computeComplexRotations( xLength );
				for( int y = 0; y < ySize; y ++ ) 
				{
					int xStart = y * yInc + yy * YBLOCK;
					FT.LinearFFT_Quick( data, xStart, xInc, xLength, direction );
				}
			}
		}
		
		if( yBlocks > 0 ) 
		{
			
			for( int xx = 0; xx < xBlocks; xx++ )
			{
			
				if ( xLength != yLength )
					FT.computeComplexRotations( yLength );
					
				for( int x = 0; x < xSize; x ++ ) 
				{
					int yStart = x * xInc + xx * xSize * XBLOCK;
					FT.LinearFFT_Quick( data, yStart, yInc, yLength, direction );
				}
			}
		}
	}

}