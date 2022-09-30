import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SerializeAll {
	public static List<Integer> IndexList = new ArrayList<>();
	public static List<String> NameList = new ArrayList<>();
	public static int size;
	public String directoryd;
	public String serializedpath;
	public SerializeAll() {
		
	}
	
	public int getSize() {
		return size;
	}
	
	public void serialize(String args[]) {
		
		int k = Integer.parseInt(args[0]);	//get the kmer size
		myHashMap hmap = new myHashMap(); //make a hashmap
	    String filename = "";	   
	    String directory = args[1]; //the path where the samples are stored
	    
	    String serializedDirectory = "";
	    if(args[5].equals("w")) {
	    serializedDirectory= args[2].concat("\\serializedHashMap");
	    }
	    else if(args[5].equals("l")) {
	    	serializedDirectory= args[2].concat("/serializedHashMap");
	    }
 	   	serializedpath = serializedDirectory;
	    System.out.println("DEBUG");
	    size = 0;
		//get all the files in the directory and put it into File[] array
		  File dir = new File(directory);	//the directory
		  File[] directoryListing = dir.listFiles();	//put all the files in the directory in array
		  int index = 1;
		  try { 
		    for (File f : directoryListing) {
		    	size++;
		    	filename = f.toString();
		    	IndexList.add(index++);
		    	NameList.add(f.getName());
		    	int i = 0;
		    	if(args[5].equals("w")) {
		    		i = filename.lastIndexOf("\\")+1;
		    	}
		    	else if(args[5].equals("l")) {
		    		i = filename.lastIndexOf("/")+1;
		    	}
		    	int j = filename.lastIndexOf("."); // to get rid of file extension 
		    	filename = filename.substring(i, j);
		    	System.out.println("filename: "+filename);
		    	//pass the input as a parameter to File variable

			    Scanner scA = new Scanner(f); 
			    StringBuilder strb1 = new StringBuilder();
			    //read the input file
			    while (scA.hasNextLine()) { 
			      String temp = scA.nextLine();
			      //skipping the first line where it contains the name of the file
			      if(!temp.contains(">")) {
			    	  strb1.append(temp);
			      }
			    }
			    scA.close();
			    //convert StringBuilder to String 
			    String A = strb1.toString();
			    //make the string to a single line string w/o nl or blank
			    A = A.replace("\n", "");
			    
			    //System.out.println(A);
			    hmap = hmap.kmer(A, k);
			    //System.out.println(A);
		    
			    
			    //WRITING NON SERIALIZED FILES
			    if(args[3].equals("0")) {
				//Save myHashMap in file but w/o serialization FOR WINDOWS
					if(args[5].equals("w")) {
						String nonserializedDirectory= args[2].concat("\\nonserializedHashMap");
			        	   File dir1 = new File(nonserializedDirectory);
			        	   if(! dir1.exists()) {
			        		   dir1.mkdir();
			        	   }
			        	   
					    try(FileWriter fw = new FileWriter(nonserializedDirectory+"\\"+filename+"_before"+".txt");
					    	    BufferedWriter bw = new BufferedWriter(fw);
					    	    PrintWriter out = new PrintWriter(bw))
					    	{
					    		out.println(hmap.printString());
					    		fw.close();
					    		bw.close();
					    		out.close();
					    	} catch (IOException e) {
					    	    //exception handling left as an exercise for the reader
					    	}
					}
					//SAVE FILE NONSERIALIZED FOR LINUX
					else if(args[5].equals("l")) {
						String nonserializedDirectory= args[2].concat("/nonserializedHashMap");
			        	   File dir1 = new File(nonserializedDirectory);
			        	   if(! dir1.exists()) {
			        		   dir1.mkdir();
			        	   }
			        	   
						try(FileWriter fw = new FileWriter(nonserializedDirectory+"/"+filename+"_before"+".txt");
					    	    BufferedWriter bw = new BufferedWriter(fw);
					    	    PrintWriter out = new PrintWriter(bw))
					    	{
					    		out.println(hmap.printString());
					    		fw.close();
					    		bw.close();
					    		out.close();
					    	} catch (IOException e) {
					    	    //exception handling left as an exercise for the reader
					    	}
					}
				System.out.println("File for checking created");
			    }
			if(args[5].equals("w")) {
		        try
		        {	
		        	//---------------------------------------CREATING FILE---------------------------------
		        	   //write object to file
		        	   //put the directory + file name for the new file that will be saved
		        	   String path1 = serializedDirectory+"\\"+filename+".ser";
		        	   File dir1 = new File(serializedDirectory);
		        	   if(! dir1.exists()) {
		        		   dir1.mkdir();
		        	   }
		        	   
		        	   File temp = new File(path1);
		               FileOutputStream fos =
		                  new FileOutputStream(path1);
		               ObjectOutputStream oos = new ObjectOutputStream(fos);
		              
		    	       
		               if(temp.createNewFile()) {
		                  System.out.println("The new .ser file is created");
		               } 
		               oos.writeObject(hmap);
		               //oos.flush();//not sure whether I necessarily need this
		               oos.close();
		               fos.close();
		        }catch(IOException ioe){
		               	ioe.printStackTrace();
		        }

			}
				else if(args[5].equals("l")) {
					 try
				        {	
						 	System.out.println(serializedDirectory);
				        	//---------------------------------------CREATING FILE---------------------------------
				        	   //write object to file
				        	   //put the directory + file name for the new file that will be saved
				        	   String path1 = serializedDirectory+"/"+filename+".ser";
				        	   File dir1 = new File(serializedDirectory);
				        	   if(! dir1.exists()) {
				        		   dir1.mkdir();
				        	   }
				        	   
				        	   File temp = new File(path1);
				               FileOutputStream fos =
				                  new FileOutputStream(path1);
				               ObjectOutputStream oos = new ObjectOutputStream(fos);
				              
				    	       
				               if(temp.createNewFile()) {
				                  System.out.println("The new .ser file is created");
				               } 
				               oos.writeObject(hmap);
				               //oos.flush();//not sure whether I necessarily need this
				               oos.close();
				               fos.close();
				        }catch(IOException ioe){
				               	ioe.printStackTrace();
				        }

				}
		    } //the closing bracket for for each loop that iterates File[]
		  } catch(Exception e) {
			  System.out.println("toString(): "+e.toString());
			  System.out.println("getMessage(): "+e.getMessage());
			  System.out.println("StackTrace: ");
			  e.printStackTrace();
		  }
	}
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		

		
		

	}

	
}
