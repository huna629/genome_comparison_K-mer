import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Node {
	NumberFormat formatter = new DecimalFormat("#0.00"); 
	int key;
	String distance; //distance to Parent node
	Node left, right;
	Boolean used;
	String position;
	public Node(int item) {
		key = item;
		distance = "0";
		left = right = null;
		used = false;
	}
	
	public Node(int item, double d, String pos) {
		key = item;
		distance = formatter.format(d);
		left = right = null;
		position = pos;
	}
	
	public Node(String item, double d) {
		key = Integer.parseInt(item);
		distance = formatter.format(d);
		left = right = null;
	}
	
	public Node(int item, double d, Node l, Node r, String pos) {
		key = item;
		distance = formatter.format(d);
		left = l;
		right = r;
		position = pos;
	}
	
	public void setDistance(double d){
		distance = formatter.format(d);
	}
	public void setUsed(Boolean b){
		used = b;
	}
	public void setPos(String b) {
		position = b;
	}
	

}
