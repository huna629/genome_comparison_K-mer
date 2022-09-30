import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.management.InstanceNotFoundException;


public class ManageAll extends SerializeAll{
	public List<Integer> index;
	public String[] fileNamesString;
	public int[] fileNames;
	public int filesize;
		
	public void getFileSize(String dir) {
		File f = new File(dir);
		int count = f.list().length;
		int index = 0;
		File[] directoryListing = f.listFiles();	//put all the files in the directory in array
		fileNamesString = new String[directoryListing.length];
		fileNames = new int[directoryListing.length];
		//List<Integer> nameList = new ArrayList<>();
		if (directoryListing != null) {
			//int index = 0;
		   for (File fi : directoryListing) {
			   String name = fi.getName();
			   
			   String s = name.substring(0, name.length()-4);
			   //System.out.println(s);
			   
			   //System.out.println(fileNames);
			   //fileNames[index] =Integer.parseInt(name.substring(0, name.length()-4));
			   fileNamesString[index] = s;
			   //System.out.println("fileNames[index]   "+fileNames[index]);
			   index++;
		   }
		}
		filesize = count;
		//System.out.println(fileNamesString);
	}
	
	public void print2dArray(int[][] matrix) {
	    int cols = matrix[0].length;
	    int[] colWidths = new int[cols];
	    for (int[] row : matrix) {
	        for (int c = 0; c < cols; c++) {
	            int width = String.valueOf(row[c]).length();
	            colWidths[c] = Math.max(colWidths[c], width);
	        }
	    }
	    for (int[] row : matrix) {
	        for (int c = 0; c < cols; c++) {
	            String fmt = String.format("%s%%%dd%s",
	                    c == 0 ? "|" : "  ",
	                    colWidths[c],
	                    c < cols - 1 ? "" : "|%n");
	            System.out.printf(fmt, row[c]);
	        }
	    } 
	}
	

	
	public PrintWriter write2dArray(double[][] result, PrintWriter p) {
	    int cols = result[0].length;
	    int[] colWidths = new int[cols];
	    for (double[] row : result) {
	        for (int c = 0; c < cols; c++) {
	            int width = String.valueOf(row[c]).length();
	            colWidths[c] = Math.max(colWidths[c], width);
	        }
	    }
	    for (double[] row : result) {
	        for (int c = 0; c < cols; c++) {
	            String fmt = String.format("%s%%%dd%s",
	                    c == 0 ? "|" : "  ",
	                    colWidths[c],
	                    c < cols - 1 ? "" : "|%n");
	            p.printf(fmt, row[c]);
	        }
	    }
	    return p;
	}
	
	public PrintWriter printMatrix(double[][] result, PrintWriter p) {
	    for (int row = 0; row < result.length; row++) {
	        for (int col = 0; col < result[row].length; col++) {
	            p.printf("%12f", (int) result[row][col]);
	        }
	        p.printf("%n");
	    }
	    return p;
	}
	
	public PrintWriter printnames(int[] names, PrintWriter p) {
		String separator = "";
	    for (int row = 0; row < names.length; row++) {
	            p.printf(separator+names[row]);
	            separator=",";
	            if(row==names.length-1)
	            	p.printf("%n");	            	
	    }
	    return p;
	}
	
	public void run(String[] args) throws ClassNotFoundException, IOException {
		boolean DEBUG = false;
		
		if(DEBUG) {
			System.out.println("Args[4]: "+args[4]);
			System.out.println("ARGS[4] LENGTH "+args[4].length());
			System.out.println("ManageAll.run(String[] args)");
		}
		
		//compare every single FASTA file in the directory
		if(args[4].equals("1")) {
			System.out.println("Creating SerializeAll obj");
			//long serializeStart = System.nanoTime();
			SerializeAll sa = new SerializeAll();
			sa.serialize(args);
			long serializeEnd = System.nanoTime();
			//long serializeTakes = serializeStart-serializeEnd;
			//System.out.println("serialization Took:"+serializeTakes/(1000000000));
			
		}
		String serializedpath ="";
		//Check whether the path exists in Windows
		if(args[5].equals("w")) {
			File f = new File(args[2]);
			if(f.exists())
				serializedpath = args[2].concat("\\serializedHashMap");
		}
		else {
			File f = new File(args[2]);
			if(f.exists())
				serializedpath = args[2].concat("/serializedHashMap");
		}
		getFileSize(serializedpath);
		
		int[] names = new int[filesize];
		double[][] result = new double[filesize][filesize];
		
		System.out.println("\n"+"size"+filesize);
		System.out.println("ManageAll is running ------------------------------------------");	//debug
		List<myHashMap> deserialized = new ArrayList<>();
		//long readingFiles = System.nanoTime();
		System.out.println("-----------------------------------------READING .SER FILE-----------------------------"); 
		for(int a = 0; a<result[0].length; a++) {
        //Read object from file
			if(a==result[0].length/2) {
				System.out.println("Finished reading half of files");
			}
			int first = Integer.parseInt(fileNamesString[a]);
			names[a] = first;
			//long readingOneFile = System.nanoTime();
		if(args[5].equals("w")){
    	try {     		         
    		FileInputStream fis = new FileInputStream(serializedpath+"\\"+first+".ser");
    		ObjectInputStream ois = new ObjectInputStream(fis);
    		boolean check=true;
    		myHashMap answer = new myHashMap();
    		while (check) {
    			try{
    				answer = (myHashMap) ois.readObject();
    			} catch(EOFException ex){
    				check=false;
    			}
    		}	
    		deserialized.add(answer);
    		fis.close();
    		ois.close();
        }catch(IOException ioe){
        		ioe.printStackTrace();}
		}
		else if(args[5].equals("l")) {
	    	try {     		         
	    		FileInputStream fis = new FileInputStream(serializedpath+"/"+first+".ser");
	    		ObjectInputStream ois = new ObjectInputStream(fis);
	    		boolean check=true;
	    		myHashMap answer = new myHashMap();
	    		while (check) {
	    			try{
	    				answer = (myHashMap) ois.readObject();
	    			} catch(EOFException ex){
	    				check=false;
	    			}
	    		}	
	    		deserialized.add(answer);
	    		fis.close();
	    		ois.close();
	        }catch(IOException ioe){
	        		ioe.printStackTrace();}
		}
		}// end of for loop

		System.out.println("Creating the distance matrix");
		for(int b=0; b<filesize; b++) {
			for(int c=0; c<filesize; c++) {
				myHashMap h1 = deserialized.get(b);
				myHashMap h2 = deserialized.get(c);
				result[b][c] = h1.size + h2.size - h1.compare(h1, h2);
			}
		}	
		
		
		System.out.println("======Created NeighborJoining object=======");
		//long snj = System.nanoTime();
		NeighborJoining n = new NeighborJoining(result, names);
		//long fnj = System.nanoTime();
		
		try {
			n.run(args);
		} catch (InstanceNotFoundException e2) {
			e2.printStackTrace();
		}
		//System.out.println("creating a 2d distance matrix took : "+(fnj-snj)/1000000000);
		//n.toFile();
		/**
		String resultdir = "";
		if(args[5].equals("w"))
			resultdir = args[2]+"\\result";
		else if(args[5].equals("l"))
			resultdir = args[2]+"/result";
		
		 File theDir = new File(resultdir);

		// if the directory does not exist, create it
		if (!theDir.exists()) {
		    System.out.println("creating directory: " + theDir.getName());
		    try{
		        theDir.mkdir();
		        System.out.println("DIR created");  
		    } 
		    catch(SecurityException se){
		        //handle it
		    }        
		}
		
		System.out.println("GOING TO STORE THE DISTANCE MATRIX INTO .TXT FILE");
		if(args[5].equals("w")) {
	   	   File before = new File(resultdir+"\\2d_all_arr.txt");
			try {
				if(before.createNewFile()) {
				    System.out.println("The new .ser file is created");
				 }
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			    try(FileWriter fw = new FileWriter(resultdir+"\\2d_all_arr.txt");
			    	    BufferedWriter bw = new BufferedWriter(fw);
			    	    PrintWriter out = new PrintWriter(bw))
			    	{
			    		printnames(names, out);
			    		out.flush();
			    		printMatrix(result, out);
			    		out.flush();
			    		fw.close();
			    		bw.close();
			    		out.close();
			    	} catch (IOException e) {
			    	    //exception handling left as an exercise for the reader
			    	}
		}
		else if(args[5].equals("l")) {
			File before = new File(resultdir+"/2d_all_arr.txt");
			try {
				if(before.createNewFile()) {
				    System.out.println("The new .ser file is created");
				 }
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			    try(FileWriter fw = new FileWriter(resultdir+"/2d_all_arr.txt");
			    	    BufferedWriter bw = new BufferedWriter(fw);
			    	    PrintWriter out = new PrintWriter(bw))
			    	{
			    		printnames(names, out);
			    		printMatrix(result, out);
			    		out.flush();
			    		fw.close();
			    		bw.close();
			    		out.close();
			    	} catch (IOException e) {
			    	    //exception handling left as an exercise for the reader
			    	}
			    	
		}
**/
		System.out.println("File for checking created");
	}
	public static void main(String[] args) throws ClassNotFoundException, IOException {
	}
}

