class FFTUtil {
	
	public static
	double [][][] doFFT( int[] in, int width)
	{
		double	out[][][] = new double[2] [ width ][ width ];		

		double	re[] = new double[ width ];
		double	im[] = new double[ width ];

		// First row by row		
		for( int i=0; i < width; i++ ) {
			for( int j=0; j < width; j++ ) {
				re[j] = in[i * width + j];
				im[j] = 0;
			}
				
//			FT.FFT( 8, re, im );
			FT.DFT( 1, width, re, im );

			for( int j=0; j < width; j++ ) {
				out[0][i][j] = re[j];
				out[1][i][j] = im[j];
			}
		}
		
		// and now column by columns
		for( int i=0; i < width; i++ ) {
			for( int j=0; j < width; j++ ) {
				re[j] = out[0][j][i];
				im[j] = out[1][j][i];
			}
				
//			FT.FFT( 8, re, im );
			FT.DFT( 1, width, re, im );

			for( int j=0; j < width; j++ ) {
				out[0][j][i] = re[j];
				out[1][j][i] = im[j];
			}
		}
		
		return out;
	}
	
	
	public static
	double [][] [] doIFFT( double[][][] in, int width )
	{
		double	out[][][] = new double [2] [ width ][ width ];
		double	re[] = new double[ width ];
		double	im[] = new double[ width ];

		// First row by row		
		for( int i=0; i < width; i++ ) {
			for( int j=0; j < width; j++ ) {
				re[j] = in[0][i][j];
				im[j] = in[1][i][j];
			}
				
//			FT.iFFT( 8, re, im );
			FT.DFT( -1, width, re, im );

			for( int j=0; j < width; j++ ) {
				out[0][i][j] = re[j];
				out[1][i][j] = im[j];
			}
		}
		
		// and now column by columns
		for( int i=0; i < width; i++ ) {
			for( int j=0; j < width; j++ ) {
				re[j] = out[0][j][i];
				im[j] = out[1][j][i];
			}
				
//			FT.iFFT( 8, re, im );
			FT.DFT( -1, width, re, im );

			for( int j=0; j < width; j++ ) {
				out[0][j][i] = re[j];
				out[1][j][i] = re[j];
			}
		}
		
		return out;
	}

	public static
	double magnitude( double re, double im ) 
	{
		return Math.sqrt( re*re + im*im );
	}

	public static
	double magnitudeSquare( double re, double im ) 
	{
		return ( re*re + im*im );
	}


	public static
	int imagnitude( double re, double im ) 
	{
		return (int)Math.round(Math.sqrt( re*re + im*im ));
	}



	public static
	int [] normalizeValues( double [][][] in, int width )
	{
		int	length = width * width;
		int	img[] = new int[ length ];
		
		int	white = 0;
		int black = 0;
		int min = 1000;
		int max = -1000;

		for( int i=0; i < width; i++ ) {
			for( int j=0; j < width; j++ ) {

				int	value = (int)(in[0][i][j]);

				if ( value < min ) min = value;
				else
				if ( value > max ) max = value;
				
				img[i* width + j] = value;
			}
		}


//			for( int j=0; j < width; j++ )
//				System.out.println("iFFT[255,"+j+"]: "+img[255][j]);



//		System.out.print( "Total: [" + width * width + "] White: [" + white + "] Black: [" + black + "]" );
//		System.out.println(" Min: ["+min+"] Max:["+max+"]");
		return img;
	}

	public static
	double [][] doFFT2( int[] in, int xLength, int yLength )
	{
		return doFFT2( in, xLength, yLength, xLength, yLength );
/*		
		int	length = xLength * yLength;
		double	data[][] = new double[length][ 2 ];

		for( int i=0; i < length; i++ )
		{
			data[ i ][0] = in[i];
			data[ i ][1] = 0;
		}

		FT.FFT2( data, xLength, yLength, 0 );
	
		return data;
*/		
	}

	public static
	double [][] doFFT2( int[] in, int xLength, int yLength, int xBlock, int yBlock  )
	{
		int	length = xLength * yLength;
		double	data[][] = new double[length][ 2 ];

		for( int i=0; i < length; i++ )
		{
			data[ i ][0] = in[i];
			data[ i ][1] = 0;
		}

		FT.FFT2( data, xLength, yLength, xBlock, yBlock, 0 );
//		FT.FFT2( data, xLength, yLength, 0 );
	
		return data;
	}


	public static
	void doFFT2( double[][] data, int xLength, int yLength, int dir)
	{

		FT.FFT2( data, xLength, yLength, dir );
	}

	public static
	void doFFT2( double[][] data, int xLength, int yLength, int xBlock, int yBlock, int dir)
	{

		FT.FFT2( data, xLength, yLength, xBlock, yBlock, dir );
	}



	
	public static
	double [][] doIFFT2( double[][] in, int xLength, int yLength)
	{

		int	length = xLength * yLength;
		double	data[][] = new double[ length ][ 2 ];

		// Copy the image matrix into a flat array		
		for( int i=0; i < length; i++ ) {
			data[ i ][0] = in[i][0];
			data[ i ][1] = in[i][1];
		}

		FT.FFT2( data, xLength, yLength, 8, 8, 1 );
//		FT.FFT2( data, xLength, yLength, 1 );
	
		return data;
	}


	public static
	int [] normalizeValues( double[][] in, int xLength, int yLength )
	{
		int	length = xLength * yLength;
		int	img[] = new int[ length ];
		
		int min = 1000;
		int max = -1000;

		int cnLt = 0;
		int cnGt = 0;

		for( int i=0; i < length; i++ ) {
			int	value = (int)(in[ i ][0]);

			if ( value < min ) min = value;
			else
			if ( value > max ) max = value;

			if ( value < 0) { value = 0; cnLt++; }
			else
			if ( value > 255) { value = 250; cnGt++; }
			
			img[i] = value;
		}			

		System.out.println( "Min: " + min + " Max:" + max + " (Cn < 0 = " + cnLt + ") (Cn > 255 = "+cnGt);

		return img;
	}


}