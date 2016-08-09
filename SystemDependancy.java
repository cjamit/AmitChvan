
    
	
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

	
public class SystemDependancy {

	static Map<String,Integer> name2id= new ConcurrentHashMap<String, Integer>() ;
	static Map<Integer, String> id2name= new ConcurrentHashMap<Integer, String>() ;
	static Map<Integer, Vector<Integer> > dependInstall;
	static Map<Integer, Vector<Integer> > invDependInstall;
	static Map<Integer, Integer> installStatus;
	static Vector<Integer> installList;
	
	
	public static int getId(String s) {
	    int ret = name2id.get(s).intValue();
	    
	    if (ret == 0) {
	        ret = (int) name2id.size();
	        id2name.put(ret, s);
	    }
	    return ret;
	}
	public static String getName(int id) {
	    return id2name.get(id);
	}
	public static int isDepended(int id) {
	    Vector<Integer> v = invDependInstall.get(id);
	    for (int i = 0; i < v.size(); i++) {
	        if (installStatus.containsValue(v.get(i)))    return 1;
	    }
	    return 0;
	}
	static void installSoft(int id, int top) {
	    if (installStatus.get(id) == 0) {
	        Vector<Integer> v = dependInstall.get(id);
	        for (int i = 0; i < v.size(); i++)
	            installSoft(v.get(i), 0);
	        System.out.println("   Installing  " + getName(id));
	        int temp =installStatus.get(id).intValue()== top ? 1 : 2;
	        installList.addElement(id);
	    }
	}
	static void removeSoft(int id, int top) {
	    if ((top == 1 || installStatus.get(id) == 2) && (isDepended(id)==0)) {
	        installStatus.put(id, 0);
	        System.out.println("   Removing " + getName(id) + "\n");
	        Vector<Integer> v = dependInstall.get(id);
	        for (int i = 0; i < v.size(); i++)
	            removeSoft(v.get(i), 0);
	        installList.removeElement(new Integer(id));
	       
	    }
	}
	
	public static void  main(String[] args) {
		
	    for(int lineNo=0;lineNo<args.length;lineNo++){
	    	
	    	String trackLine = args[lineNo];
	    	List<String> tokens = Arrays.asList(trackLine.split("\\s+"));
	        List<String> argsSplitInLine = Arrays.asList(trackLine.split("\\s+"));
	        if (argsSplitInLine.get(0) == "DEPEND") {
	            Vector<Integer> softId = new Vector<Integer>();
	            for (int i = 1; i < argsSplitInLine.size(); i++) {
	                softId.addElement(getId(argsSplitInLine.get(i)));
	            }
	            Vector<Integer> v = dependInstall.get(softId.get(0));
	            for (int i = 1; i < softId.size(); i++) {
	                v.addElement(softId.get(i));
	                invDependInstall.get(softId.get(i)).addElement(softId.get(0));
	            }
	        } else if (argsSplitInLine.get(0) == "INSTALL") {
	            if (installStatus.containsKey(getId(argsSplitInLine.get(1)))) {
	                System.out.println(argsSplitInLine.get(1).toString() + "  is already installed.\n");
	            } else {
	                installSoft(getId(argsSplitInLine.get(1)), 1);
	            }
	        } else if (argsSplitInLine.get(0).equalsIgnoreCase("REMOVE")){
	            if (installStatus.get(getId(argsSplitInLine.get(1))) == 0) {
	            	System.out.println(argsSplitInLine.get(1) + " is not installed.\n");
	            } else if (isDepended(getId(argsSplitInLine.get(1)))!=0) {
	            	System.out.println(argsSplitInLine.get(1) + " is still needed.\n");
	            } else {
	                removeSoft(getId(argsSplitInLine.get(1)), 1);
	            }
	        } else if (argsSplitInLine.get(0).equalsIgnoreCase("LIST")) {
	            for (int i = 0; i < installList.size(); i++) {
	            	System.out.println(getName(installList.get(i)));
	            }
	        } else if (argsSplitInLine.get(0).equalsIgnoreCase("END")) {
	            break;
	        }
	    
	    }
	
	}
}
