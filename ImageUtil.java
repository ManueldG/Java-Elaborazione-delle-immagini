import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.applet.*;

public class ImageUtil {
	private String		fileName;
	private int			imageWidth;
	private int			imageHeight;
	
	public ImageUtil( String fileName, int width, int height )
	{
		this.fileName =  fileName;
		
		imageWidth = width;
		imageHeight = height;
	}

	public	
	int[] loadImage()
	{
		FileInputStream	fis = null;
		try {
	    		fis = new FileInputStream( fileName );
	    	}
	    	catch( FileNotFoundException e )
	    	{
	    		System.out.println( "Input file not found: " + fileName );
		}
		
		byte	row[] = new byte[ imageWidth ];
		int		[]img = new int[ imageWidth * imageHeight ];
		
		try {
			for( int i=0; i < imageHeight; i++ ) {
				fis.read( row );
				// Because byte type in Java in signed by default and it's impossible
				// to change this behaviou i have to add 256 to the negative  in order to shift them into the range [0, 255]
				for( int j=0; j < imageWidth; j++ ) {
					int value = row[j];
					if ( value < 0) value += 256;

					img[j * imageWidth + i] = value;
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

	

	public
	MemoryImageSource getImageObject( int []img )
	{
		int[]	pixels = toPixels( img );
	    return new MemoryImageSource(imageWidth, imageHeight, pixels, 0, imageWidth);
	}

	private
	int[]	toPixels( int[] img )
	{
		int	length = img.length;
		int	pixels[] = new int[ length ];

		int min = +1000000;
		int max = -1000000;
		
		for(int i=0; i < img.length; i++ )
		{
			int	pixel = img[i];
			pixels[i] = (255 << 24) | (pixel << 16) | (pixel << 8) | pixel;

			if ( pixel < min ) min = pixel;
			else
				if ( pixel > max ) max = pixel;

			}

//		System.out.println( "IMAGE MIN:" + min + " MAX:" + max );
				
		return pixels;
	}
}