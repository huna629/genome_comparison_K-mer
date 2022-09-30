
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.List;
import java.util.Scanner;

public class myHashMap implements Serializable {
	//some numbers I can use: 101, 233, 467, 887, 1013, 1709, 2687, 4001, 7010, 8297, 10007
	  private static final long serialVersionUID = -124L;
      private final static int TABLE_SIZE = 30000;
      int size = 0;
      int h = 0;
      int total = 0;
      List<Integer> list = new ArrayList<Integer>();
  	  public static List<Integer> IndexList = new ArrayList<>();
  	  public static List<String> NameList = new ArrayList<>();
      Sequence[] table;
      boolean DEBUG = false;
 
      public myHashMap() {
            table = new Sequence[TABLE_SIZE];
            for (int i = 0; i < TABLE_SIZE; i++)
                  table[i] = null;
      }

      public int get(long key) {
            int hash = hash(key);
            int a = -1;
                if (table[hash] == null)
                    a = -1;
            	 else {
                    Sequence entry = table[hash];
                    while (entry != null && entry.getKey() != key)
                          entry = entry.getNext();
                    if (entry == null)
                         a= -1;
                    else
                          a=entry.getValue();
              }
			return a;
      }

  	public int compare(myHashMap h1, myHashMap h2) {
		int result = 0; //A AND B , Integer variable to count blocks that both sequences have
		if(DEBUG) {
		System.out.println("h1 list:;::::: "+h1.list);
		System.out.println("h2 list:::::::: "+h2.list);
		System.out.println("IS THIS WORKING?");}
		//int index = 0;
		//Iterator<Integer> it = h1.list.iterator();
		for(Integer i : h1.list) {
			Sequence s = table[i];
			if(DEBUG)
				System.out.println("i : "+ i+" : s.key -"+s.key);
			while(s!=null) {
				long l = s.getKey();  // causing Array Out of boundce index
				int sec = h2.get(l);
				int fir = h1.get(l);
				int min = Math.min(fir, sec);
				if(DEBUG)
				  System.out.println("Math.min :::: "+min);
				if(sec!=-1 && fir!=-1) {
					if(DEBUG) {
					  System.out.println("h1.get(l)  "+fir+"   h2.get(l)  "+sec);
					  System.out.println("Math.min :::: "+min);}
					
					result += Math.min(h1.get(l), h2.get(l)); // result += min;//
					if(DEBUG)  
					  System.out.println("RESULT: "+result);
				}
				s = s.getNext();
			}
		}
		return result;
  	}
  	
      
  	 //method to get the index for the kmer
      public int hash(long key) {
    	  h = (int) (key%TABLE_SIZE);
    	  return h;
      }
      
      //putting into the hashmap
      public void put(long key, int value) {
            int hash = hash(key);
            if(DEBUG)
              System.out.println("HASH INDEX:"+hash);
            if (table[hash] == null)	//in that index it's empty
                  table[hash] = new Sequence(key, value);
            else {						//in that index there is already another value;
            	  if(DEBUG)  
                	System.out.println("table[hash]!=null");
                  Sequence entry = table[hash];
                  while (entry.getNext() != null && entry.getKey() != key) //iterate until it finds where it has the Sequence with the same key & value
                        entry = entry.getNext();
                  if (entry.getKey() == key) {
                	    if(DEBUG)
                	      System.out.println("There is same value in the map already: entry.getKey()=  "+entry.getKey()+"  key = "+key);
                        entry.setValue(value);
                  }
                  else {
                	  if(DEBUG)
                		  System.out.println("that index is not empty but I have to add a new node on that index "+hash+"  key = "+key);
                        entry.setNext(new Sequence(key, value));
                  }
            }
            
      	  if(!list.contains(hash)) {
      		  //System.out.println("Adding "+hash+" to the list");
    		  list.add(hash);
      	  }
      }

      public static void print(myHashMap a) {
    	  final StringBuilder sb = new StringBuilder();
    	  for(int i=0; i<a.table.length; i++) {
    		  sb.append(i).append(" : ");
    		  
    		  Sequence s = a.table[i];
    		  while(s!=null) {
    			  sb.append(s.getKey()).append(" -> ").append(s.getValue()).append(", ");
    			  s = s.getNext();
    		  }
    		  sb.append("\n");
    	  }
    	  //System.out.println(sb.toString());
      }
      

      public String printString() {
    	  final StringBuilder sb = new StringBuilder();
    	  for(int i=0; i<table.length; i++) {
    		  sb.append(i).append(" : ");
    		  
    		  Sequence s = table[i];
    		  while(s!=null) {
    			  sb.append(s.getKey()).append(" -> ").append(s.getValue()).append(", ");
    			  s = s.getNext();
    		  }
    		  sb.append("\n");
    	  }
    	  return sb.toString();
      }
      
      public void remove(int key) {
            int hash = (key % TABLE_SIZE);
            if (table[hash] != null) {
                  Sequence prevEntry = null;
                  Sequence entry = table[hash];
                  while (entry.getNext() != null && entry.getKey() != key) {
                        prevEntry = entry;
                        entry = entry.getNext();
                  }

                  if (entry.getKey() == key) {
                        if (prevEntry == null)
                             table[hash] = entry.getNext();
                        else
                             prevEntry.setNext(entry.getNext());
                  }
            }
      }
      

      public myHashMap kmer(String s, int k) {
    	
    	int count = 0; 
    	myHashMap h1 = new myHashMap();

    	for(int i=0; i<s.length()-k+1; i++) {
    		//get the kmer sequence
    		String temp = s.substring(i, i+k);
    		if(DEBUG)
    			System.out.println("\n"+temp+"\n");
    		if(!temp.contains("N")) {
    			//get the key by converting String into base4 system
    			//long key = convert2(temp);
    			long key = convert2(temp);
    			if(DEBUG)
    				System.out.println("CONVERT(TEMP) : "+key);
    			if(h1.get(key)==-1) {  
    				if(DEBUG)
    					System.out.println("h1.get(toDec(key))== " +h1.get(key));
    				h1.put(key, 1);
    				if(DEBUG)
    					System.out.println("toDEC(key) no inc: "+key);
    			}
    			else {
    				int v = h1.get(key)+1;	//adding 1 if there is a duplicate
    				h1.put(key, v);
    				if(DEBUG)
    					System.out.println("toDEC(key) have inc: "+key +" v is "+v);
    			}
    			count++; //increment the count variable to get the size of myhashmap
    		}
    		//System.out.println();
    	}
    	h1.size = count;
    	return h1;
      }
      
      
      
      public long convert2(String s) {
    	  //System.out.println(s);
    	  long result = 0; //if k value for kmer gets too big, it exceed
    	  				   //ITEGER MAX_VALUE
    	  StringBuilder sb = new StringBuilder();
    	  long dec = 0;
    	  int j =s.length()-1;
    	  for(int i=0; i<s.length(); i++) {
    		  if(s.charAt(i)=='A') {
    			 sb.append("1");
    		  }
    		  else if(s.charAt(i)=='C') {	//10
    			  sb.append("2");
    			  }
    		  else if(s.charAt(i)=='G') {	//01
    			  sb.append("3");
    			  }
    		  else if(s.charAt(i)=='T') {
    			  sb.append("4");
    		  }
    		  
    	  }
		  String str = sb.toString();
		  if(DEBUG)
			  System.out.println(str);
		  return Long.parseLong(str);
      }

      
      
      public static void main(String[] args) throws IOException, ClassNotFoundException {

}
}
