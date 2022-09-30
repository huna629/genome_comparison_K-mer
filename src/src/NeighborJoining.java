import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.management.InstanceNotFoundException;

public class NeighborJoining {
	List<List<Double>> original;
	List<Integer> label;
	HashMap<Integer, Node> hmap = new HashMap<Integer, Node>();
  //static int[] hashIndex;
	int[] listlist;
	int n;
	double smallest;
	int minRowIndex;
	int minColIndex;
	int smallerIndex;
	int largerIndex;
    boolean[] additivity;
	double[][] originalArray;
	HashMap<String, String> realname;
	public NeighborJoining(double[][] i, int[] name) {    
		originalArray=i;
		listlist=name;
	}
	
	public NeighborJoining(double[][] i, int[] name, HashMap<String, String> realname) {    
		originalArray=i;
		listlist=name;
		this.realname = realname;
	}
	
	public void findMin(double[][] input) {
		double smallest = Double.MAX_VALUE;
		
		for(int i=0; i<input.length; i++) {
			for(int j=0; j<input.length; j++) {
				if(input[i][j]<smallest && input[i][j]>0 && additivity[i] && additivity[j] && i!=j) {
					smallest = input[i][j];
					minRowIndex = i;
					minColIndex = j;
				}
			}
		}
		if(minRowIndex>minColIndex) {
			smallerIndex = minColIndex;
			largerIndex  = minRowIndex;
		}
		else {
			smallerIndex = minRowIndex;
			largerIndex  = minColIndex;
		}
	}
	
	public void join(double[] dist) {
		double leftdistance = dist[0];
		double rightdistance = dist[1];
		System.out.println("LEFTDISTANCE "+ leftdistance +"  RIGHTDISTANCE"+rightdistance);
		System.out.println("Trying to join "+minRowIndex+" and "+minColIndex);
		Node l = hmap.get(minRowIndex);	//cannot just do minRowIndex because in case its index's node is already combined with the other node
		l.setDistance(leftdistance);
		l.setUsed(true);
		l.setPos("left");
		//System.out.println("largerIndex"+largerIndex);
		Node r = hmap.get(minColIndex);
		r.setDistance(rightdistance);
		r.setUsed(true);
		l.setPos("right");
		//System.out.println("right Distance : "+r.distance);\
		
		//try to pass the concatanatated name for new node with the distance

		Node n = new Node(-1, 0, l, r, "middle");
		System.out.println("new node info::::::    "+"left distance = "+l.distance+" right distance " + r.distance);
		hmap.put(smallerIndex, n); //append the tree in Hashmap
		hmap.remove(largerIndex);
	}
	
	//working
	public double[] lengthEst(double[][] input, int indexA, int indexB) {
		int n = input.length;
		
		double sigA= sigma(input, 0, n, indexA);
	    double sigB= sigma(input, 0, n, indexB);
		sigB = sigA-sigB;
		int minus2 = 2*(n-2);
		double a = 0.5*input[indexA][indexB];
		//System.out.println("1/2 * d[i][j]   "+a);
		double sub = (1.0/minus2)*sigB;
		//System.out.println("subtract this from above"+sub);
		a=a-sub;
		//System.out.println("a is "+a);
		double b = input[indexA][indexB] - a;
		
		double[] ans = new double[2];
		ans[0]=a;
		ans[1]=b;
		return ans;
	}
	
	
	public double[][] setZero(double[][] input){
		for(int i=0; i<input.length; i++) {
			if(i==largerIndex)
				input[i] = new double[input.length];
			for(int j=0; j<input.length; j++) {
				if(j==largerIndex)
					input[i][j] = 0;
			}
		}
		return input;
	} 

	
	public double[][] taxa(double[][] input, int f, int g){
		double[][] ans = input;
		for(int i=0; i<input.length; i++) {
			for(int j=0; j<input.length; j++) {
				if(i==smallerIndex) {		//when I have to fill out the row when it's equal to smallerIndex, I also fill out
											//blocks vertically at the same time
						double temp = 0.5*(input[f][j]+input[g][j]-input[f][g]);
						ans[smallerIndex][j]  = temp;
						ans[j][smallerIndex] = temp;
				}
				else {
					if(j!=smallerIndex) {	//general case
						ans[i][j] = input[i][j];
					}
				}
			}
		}
		return ans;
	}
	
	//working fine
	public double sigma(double[][] input, int start, int end, int index) {
		int a = start;
		double ans = 0;
		while(a<end) {
			ans+=input[index][a];
			a++;
			//System.out.println("sigma:  when "+a+"  is "+ans);
		}
		//System.out.println();
		return ans;
	}
	
	
	public HashMap<Integer, Node> hash(int[] l){
		HashMap<Integer, Node> hmap = new HashMap<Integer, Node>();
		for(int i=0; i<l.length; i++) {
			Node n = new Node(l[i]);
			hmap.put(i, n);
		}
	    return hmap;
	}
	

    public PrintWriter toFile(HashMap<Integer, Node> h, PrintWriter p) {
	    Set<Entry<Integer, Node>> set2 = h.entrySet();
	    Iterator iterator2 = set2.iterator();
	      
	    while(iterator2.hasNext()) {
	          Map.Entry<Integer, Node> mentry2 = (Map.Entry<Integer, Node>)iterator2.next();
	          p.printf("Key is: "+mentry2.getKey() + " & Value is: "+mentry2.getValue().key +"\n");
	    }
	    return p;
    }
    
    public String inOrderNewick(Node root) throws InstanceNotFoundException {
		String output = "";
    	if (root.left!=null || root.right!=null) {
    		output += "(";
    		output += inOrderNewick(root.left);
    		output += ",";
    		output += inOrderNewick(root.right);
    		output += "):";
    		output += root.distance;
    		return output;
    	} else {
    		if(Double.valueOf(root.distance)!=0.00) {
    			//return Integer.toString(root.key)+"--"+realname.get(Integer.toString(root.key))+":"+root.distance;
    			return Integer.toString(root.key)+":"+root.distance;
    		}else
    			return "";
    	}
    	
    }
	
    public double[][] generateQ(double[][] input) {
		double[][] q = new double[input.length][input.length];
		//variables to save the info for the smallest element in the newly generated matrix
		smallest = Integer.MAX_VALUE;
		int n = label.size();
		for(int i=0; i<q.length; i++) {
			for(int j=0; j<i; j++) {
				//if(i!=j) {
					double t = ((n-2)*(input[i][j]));
					double sigmal = sigma(input, 0, n, i)+sigma(input, 0, n, j);
					double temp = t - sigmal;
					q[i][j]=temp;
					q[j][i]=temp;
					if(temp<smallest) {
						//everytime it finds the smaller calculated value, it updates
						smallest = temp;
						minRowIndex = i;
						minColIndex = j;
						if(i>j) {
						    smallerIndex = j;
							largerIndex = i;
						}
						else {
						    smallerIndex = i; 
						    largerIndex = j; 	
						}
					}
				//}
			}

			
		}
		System.out.println("Smallest element is " + smallest);
		return q;
	}
	public void iterative(Node node) {
	    if(node==null)
	    	return;
	    iterative(node.left);
	    iterative(node.right);
	    if(node.position!="middle" && node.left!=null && node.right!=null)
	    	System.out.print(":"+node.distance);
	    else if(node.position == "left" && node.left==null && node.right==null)
	    	System.out.print("("+node.key+":"+node.distance+",");
	    else if(node.position == "middle")
	    	System.out.print(node.distance+";)");
	    else if(node.position =="right"&& node.left ==null && node.right==null)
	    	System.out.print(node.key+":"+node.distance+")");
	 }
	
	public double[][] replaceZero(double[][] arr) {
		for(int i=0; i<arr.length; i++) {
			for(int j=0; j<arr.length; j++) {
				if(arr[i][j]<0)
					arr[i][j] = 0;
			}
		}
		return arr;
	}
	
	public void run(String[] args) throws InstanceNotFoundException {
		double[][] original = originalArray;
		additivity = new boolean[listlist.length];
		Arrays.fill(additivity, Boolean.TRUE);
		HashMap<Integer, Node> firstmap = hash(listlist);
		hmap = firstmap;

	    double[][] distanceMatrix;
	    int a = original.length;
		while(a>1) {
			findMin(original);
			double[] len = lengthEst(original, minRowIndex, minColIndex);		//get the length of a and b to the parent node	
			while(len[0] <0 || len[1]<0) {
				original = replaceZero(original);
				len = lengthEst(original, minRowIndex, minColIndex);
			}
			distanceMatrix= taxa(original, minRowIndex, minColIndex);
			join(len);
			distanceMatrix = setZero(distanceMatrix); 
			original = distanceMatrix;
			a--;
		}
		if(args[5].equals("w")) {
		File before = new File(args[2]+"\\Neighbor.txt");
			try {
				if(before.createNewFile()) {
			    System.out.println("The new .ser file is created");
			 }
		    } catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		    try(FileWriter fw = new FileWriter(args[2]+"\\Neighbor.txt");
		    	    BufferedWriter bw = new BufferedWriter(fw);
		    	    PrintWriter out = new PrintWriter(bw))
		    {
			    Set<Entry<Integer, Node>> set2 = hmap.entrySet();
			    Iterator iterator2 = set2.iterator();
			      
			    while(iterator2.hasNext()) {
			          Map.Entry<Integer, Node> mentry2 = (Map.Entry<Integer, Node>)iterator2.next();
			          //print(mentry2.getValue());
			          String newick = inOrderNewick(mentry2.getValue());
			          System.out.println("NEWICK LENGTH"+newick);
			          out.printf(newick.substring(0, newick.length()-5));
			          
			    }
			    
			    out.close();
		    } catch (IOException e) {
		    	    //exception handling left as an exercise for the reader
		   	}
		}
		else if(args[5].equals("l")) {
			File before = new File(args[2]+"/Neighbor.txt");
			try {
				if(before.createNewFile()) {
			    System.out.println("The new .ser file is created");
			 }
		    } catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		    try(FileWriter fw = new FileWriter(args[2]+"/Neighbor.txt");
		    	    BufferedWriter bw = new BufferedWriter(fw);
		    	    PrintWriter out = new PrintWriter(bw))
		    {
			    Set<Entry<Integer, Node>> set2 = hmap.entrySet();
			    Iterator iterator2 = set2.iterator();
			      
			    while(iterator2.hasNext()) {
			          Map.Entry<Integer, Node> mentry2 = (Map.Entry<Integer, Node>)iterator2.next();
			          //print(mentry2.getValue());
			          String newick = inOrderNewick(mentry2.getValue());
			          System.out.println("NEWICK LENGTH"+newick);
			          out.printf(newick.substring(0, newick.length()-5));
			          
			    }
			    
			    out.close();
		    } catch (IOException e) {
		    	    //exception handling left as an exercise for the reader
		   	}
		}

		}
	
	
	public static void main(String[] args) throws IOException, InstanceNotFoundException {
	}
	
}


