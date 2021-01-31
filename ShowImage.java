import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.applet.*;

public class ShowImage extends Applet {
	MemoryImageSource 	imgsrc;
	
	Image 				lennaImage;
	Image 				FFTedImage;

  	private Panel subPanel = new Panel();
	
	int[]	pixels;
	
	private final static String 	FILE_NAME = "e:\\corso\\mc\\lenna.bin";
	private final static int		IMAGE_WIDTH = 256;

	private	
	int[][] load_matrix( String fileName )
	{
		FileInputStream	fis = null;
		try {
	    		fis = new FileInputStream( fileName );
	    	}
	    	catch( FileNotFoundException e )
	    	{
	    		System.out.println( "Input file not found: " + fileName );
		}
		
		byte	row[] = new byte[ IMAGE_WIDTH ];
		int		img[][] = new int[ IMAGE_WIDTH ] [ IMAGE_WIDTH ];
		
		try {
			for( int i=0; i < 256; i++ ) {
				fis.read( row );

				// Because byte type in Java in signed by default and it's impossible
				// to change its behavious i have to add 128 in order to shift them into the range [0, 255]				
				for( int j=0; j < IMAGE_WIDTH; j++ ) {
					img[i][j] = row[j];
					if (img[i][j] < 0 ) img[i][j] += 256;
				}
			}
		}
		catch( IOException e ) 
		{
			System.out.println( "Unexpected IO error: " + e );
			
			return null;
		}
		finally
		{
			try{ fis.close(); } catch( IOException e) {};
		}
		
		return img;
	}
	
	private
	int[]	fromMatrixToArray( int[][] mat )
	{
		int	pixels[] = new int[ IMAGE_WIDTH * IMAGE_WIDTH ];
		
		for(int i=0; i < IMAGE_WIDTH; i++ )
			for(int j=0; j < IMAGE_WIDTH; j++ )
			{
//				int pixel = 256 - mat[j][i];
				int pixel = mat[j][i];
				pixels [ i * IMAGE_WIDTH + j ] = (255 << 24) | (pixel << 16) | (pixel << 8) | pixel;;
//				pixels [ i * IMAGE_WIDTH + j ] = (pixel << 24);
			}
				
		return pixels;
	}

	public 
	String getAppletInfo() {
		return "Show PlayBoy cover's girl.";
	}

	final static 
	void gbcon(GridBagLayout gridbag, GridBagConstraints c, Component cp, Panel p) {
	    gridbag.setConstraints(cp, c);    
	    p.add(cp);
	}


	public 
	double [][][] applyFFT( int[][] in )
	{
		double	out[][][] = new double[2] [ IMAGE_WIDTH ][ IMAGE_WIDTH ];		

		double	re[] = new double[ IMAGE_WIDTH ];
		double	im[] = new double[ IMAGE_WIDTH ];

		// First row by row		
		for( int i=0; i < IMAGE_WIDTH; i++ ) {
			for( int j=0; j < IMAGE_WIDTH; j++ ) {
				re[j] = in[i][j];
				im[j] = 0;
			}
				
			FT.FFT( 8, re, im );
//			FT.DFT( 1, IMAGE_WIDTH, re, im );

			for( int j=0; j < IMAGE_WIDTH; j++ ) {
				out[0][i][j] = re[j];
				out[1][i][j] = im[j];
			}
		}
		
		// and now column by columns
		for( int i=0; i < IMAGE_WIDTH; i++ ) {
			for( int j=0; j < IMAGE_WIDTH; j++ ) {
				re[j] = out[0][j][i];
				im[j] = out[1][j][i];
			}
				
			FT.FFT( 8, re, im );
//			FT.DFT( 1, IMAGE_WIDTH, re, im );

			for( int j=0; j < IMAGE_WIDTH; j++ ) {
				out[0][j][i] = re[j];
				out[1][j][i] = im[j];
			}
		}
		
		return out;
	}


	private
	int [][] normalizeColours( double [][][] in )
	{
		int	img[][] = new int[ IMAGE_WIDTH ][ IMAGE_WIDTH ];
		
		int	white = 0;
		int black = 0;
		int min = 1000;
		int max = -1000;

		for( int i=0; i < IMAGE_WIDTH; i++ ) {
			for( int j=0; j < IMAGE_WIDTH; j++ ) {

/*
				if (in[0][i][j] < 0 ) {
					img[i][j] = (byte)0;
					white++;
					
//					System.out.println("["+i+","+j+"] - re:"+in[0][i][j]+",i:"+in[1][i][j]);
				}
				else
				if (in[0][i][j] > 255 ) {
					img[i][j] = (byte)255;
					black++;
				}
				else
					img[i][j] = (byte)in[0][i][j];
*/					

				img[i][j] = (int)(in[0][i][j]);

//				System.out.println("["+i+","+j+"]: "+img[i][j]);

/*
				if ( in[0][i][j] < min ) min = (int)in[0][i][j];
				else
				if ( in[0][i][j] > max ) max = (int)in[0][i][j];
*/				
				if ( img[i][j] < min ) min = (int)img[i][j];
				else
				if ( img[i][j] > max ) max = (int)img[i][j];
			}
//			System.out.println();
		}

/*
			for( int j=0; j < IMAGE_WIDTH; j++ )
				System.out.println("iFFT[255,"+j+"]: "+img[255][j]);
*/


		System.out.print( "Total: [" + IMAGE_WIDTH * IMAGE_WIDTH + "] White: [" + white + "] Black: [" + black + "]" );
		System.out.println(" Min: ["+min+"] Max:["+max+"]");
		return img;
	}

	public 
	double [][] [] applyInverseFFT( double[][][] in )
	{
		double	out[][][] = new double [2] [ IMAGE_WIDTH ][ IMAGE_WIDTH ];
		double	re[] = new double[ IMAGE_WIDTH ];
		double	im[] = new double[ IMAGE_WIDTH ];

		// First row by row		
		for( int i=0; i < IMAGE_WIDTH; i++ ) {
			for( int j=0; j < IMAGE_WIDTH; j++ ) {
				re[j] = in[0][i][j];
				im[j] = in[1][i][j];
			}
				
			FT.iFFT( 8, re, im );
//			FT.DFT( -1, IMAGE_WIDTH, re, im );

			for( int j=0; j < IMAGE_WIDTH; j++ ) {
				out[0][i][j] = re[j];
				out[1][i][j] = im[j];
			}
		}
		
		// and now column by columns
		for( int i=0; i < IMAGE_WIDTH; i++ ) {
			for( int j=0; j < IMAGE_WIDTH; j++ ) {
				re[j] = out[0][j][i];
				im[j] = out[1][j][i];
			}
				
			FT.iFFT( 8, re, im );
//			FT.DFT( -1, IMAGE_WIDTH, re, im );

			for( int j=0; j < IMAGE_WIDTH; j++ ) {
				out[0][j][i] = re[j];
				out[1][j][i] = re[j];
			}
		}
		
		return out;
	}


	public void init() {
		int[][] lenna = load_matrix( FILE_NAME );
//	    pixels = fromMatrixToArray( img );
		
		// Let's print the last line of the original image		
/*		
		for( int j=0; j < IMAGE_WIDTH; j++ )
			System.out.println("lenna[255,"+j+"]: "+lenna[255][j]);
*/
		double[][][] FFTImg = applyFFT( lenna );
		double[][][] iFFTImg = applyInverseFFT( FFTImg );
	
		int[][] img2 = normalizeColours(iFFTImg);	
	    pixels = fromMatrixToArray( img2 );


	    imgsrc = new MemoryImageSource(IMAGE_WIDTH, IMAGE_WIDTH, fromMatrixToArray(lenna), 0, IMAGE_WIDTH);
	    lennaImage = createImage(imgsrc);
	    
	    imgsrc = new MemoryImageSource(IMAGE_WIDTH, IMAGE_WIDTH, pixels, 0, IMAGE_WIDTH);
	    FFTedImage = createImage(imgsrc);
 

		resize(256, 600);
	}

	public void start() {
	}

	public synchronized void stop() {
	    notify();
	}

	public void paint(Graphics g) {
	    // Draw the animated image
	    g.drawImage(lennaImage, 0, 0, this);
	    g.drawImage(FFTedImage, 0, 300, this);
	}
	
	public static 
	void main(String args[]) {
		AppletFrame.startApplet("ShowImage", "Lenna", args);
    }

}