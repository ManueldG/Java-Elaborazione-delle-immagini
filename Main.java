public class Main {
	
	public static 
	void main(String args[]) {
		if ( args.length < 1 )
		{
			System.out.println("Usage: Main [file name] [xSize] [ySize]" );
			return;
		}
		new GUI( args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]) );
    }
}