import java.io.IOException;
import java.util.Arrays;

public class KmerPgrm {
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		boolean DEBUG = false;
		if(args.length<1) {
			System.out.println("KMERPGRM MANUAL----------------------------------------------------");
			System.out.println("USAGE:");
			System.out.println("KmerPgrm.jar [option] <argument1> [option] <argument2> [option3] <argument3>"+"\n");
			System.out.println("OPTIONS:");
			System.out.println("-k, 			kmer size ");
			System.out.println("-dir1, 			directory where fasta files are");
			System.out.println("-dir2, 			directory where output files will be stored");
			System.out.println("-wo,			save HashMap files without serialization");
			System.out.println("				0 for yes, 1 for no");
			System.out.println("-ser,			whether serialized files already exist");
			System.out.println("				0 for yes, 1 for no");
		}else {
			String kmer ="";
			String input = "";
			String output = "";
			String without = "";
			String ser = "";
			String os = "";
			boolean kflag = false;
			boolean iflag = false;
			boolean oflag = false;
			boolean wflag = false;
			boolean sflag = false;
			System.out.println("EXECUTE");
			
			//while()
			if(Arrays.asList(args).indexOf("-k")!=-1){
				if(DEBUG)
				System.out.println("k input");
				kmer = args[Arrays.asList(args).indexOf("-k")+1];
				if(DEBUG)
					System.out.println(kmer);
				if(Integer.parseInt(kmer)<19)
					kflag = true;
			} 
			if(Arrays.asList(args).indexOf("-dir1")!=-1) {
				if(DEBUG)
					System.out.println("dir1 input");
				input = args[Arrays.asList(args).indexOf("-dir1")+1];
				if(DEBUG)
					System.out.println(input);
				iflag = true;
			} 
			if(Arrays.asList(args).indexOf("-dir2")!=-1) {
				if(DEBUG)
					System.out.println("dir2 input");
				output = args[Arrays.asList(args).indexOf("-dir2")+1];
				if(DEBUG)
					System.out.println(output);
				oflag = true;
			}
			if(Arrays.asList(args).indexOf("-wo")!=-1) {
				if(DEBUG)
					System.out.println("wo input");
				without = args[Arrays.asList(args).indexOf("-wo")+1];
				if(DEBUG)
					System.out.println(without);
				wflag = true;
			}
			if(Arrays.asList(args).indexOf("-ser")!=-1) {
				if(DEBUG)
					System.out.println("ser input");
				ser = args[Arrays.asList(args).indexOf("-ser")+1];
				if(DEBUG)
					System.out.println(ser);
				sflag = true;
			}
			if(input.contains("\\") && output.contains("\\")) {
				os = "w";
			}
			if(input.contains("/") && output.contains("/")) {
				os = "l";
			}
			if(kflag && iflag && oflag && sflag && wflag) {
				System.out.print("RUNNING----");
				String[] arr = new String[6];
				arr[0] = kmer;
				arr[1] = input;
				arr[2] = output;
				arr[3] = without;
				arr[4] = ser;
				arr[5] = os;
				ManageAll a = new ManageAll();
				a.run(arr);
			}
			else {
				System.out.println("DIDN'T PUT TYPE ALL THE ARGS OR INCORRECT INPUTS");
			}
		}
			
	}
}
