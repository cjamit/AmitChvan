import java.util.*;

/**
 * Class Representing the Dependency, Links between the Items
 * Retrieval of connections between Items
 * @author Amit Chavan 
 *         cj.amit@gmail.com
 *
 */
class LinkItemsConnections implements Iterable<Item> {

	Map<Item, Vector<Item>> itemMap = new HashMap<Item, Vector<Item>>();

	public boolean addItem(Item item) {
		if (itemMap.containsKey(item))
        	return false;
    	itemMap.put(item, new Vector<Item>());
    		return true;
	}

	public void addConnections(Item item1, Item item2) {
		itemMap.get(item1).add(item2);
	}

	public Vector<Item> connectionsOriginate(Item item) {
		Vector<Item> dependencies = itemMap.get(item);
		return dependencies;
	}

	public Map<Item, Vector<Item>> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<Item, Vector<Item>> itemMap) {
		this.itemMap = itemMap;
	}

	public Iterator<Item> iterator() {
    	return itemMap.keySet().iterator();
	}

}

/**
 * Main System Dependancy class accessing Item LinkItemsConnections,entities.
 * @author Amit Chavan
 *         cj.amit@gmail.com
 *
 */
class SystemDependancyConnections {
	
	static Map<String, Item> itemLinkageMap = new HashMap<String, Item>();
	static Vector<Item> seqOfItems = new Vector<Item>();
	static LinkItemsConnections linkConnect = new LinkItemsConnections();

	/**
	 * Retrieve the Items information
	 * @param name
	 * @return
	 */
	public static Item getItem(String name) {
		if (itemLinkageMap.containsKey(name)) {
			return itemLinkageMap.get(name);
		} else {
			Item item = new Item(name);
			itemLinkageMap.put(name, item);
			return item;
		}
	}	
    
	/**
	 * Installation of items- Starting with the first
	 * @param firstItem
	 */
	public static void installItems(Item firstItem) {

		linkConnect.addItem(firstItem);

		if(firstItem.linkItems >= 0) {
			System.out.println("   "+firstItem.nameOfItem+" is already installed.");
			return;
		}

		for(Item item2: linkConnect.connectionsOriginate(firstItem)) {	
			if (item2.linkItems == -1) {
				installItems(item2);
				item2.linkItems++;
			} else {
				item2.linkItems++;
			}
		}

		System.out.println("   Installing "+firstItem.nameOfItem);
		seqOfItems.add(firstItem);
		firstItem.linkItems++;
		
	}

	/**
	 * Removal of Items, stating with the least dependant
	 * @param firstItem
	 */
	public static void removeItems(Item firstItem) {
		if (firstItem.linkItems == 0) {
			System.out.println("   Removing "+firstItem.nameOfItem);
			firstItem.representLinks = false;
			seqOfItems.remove(firstItem);
			firstItem.linkItems--;
		} else if (firstItem.linkItems >= 1){
			System.out.println("   "+firstItem.nameOfItem+" is still needed.");
			return;
		} else {
			System.out.println("   "+firstItem.nameOfItem+" is not installed.");
			return;
		}
		for (Item item2: linkConnect.connectionsOriginate(firstItem)) {	
			item2.linkItems--;
		}
		for (Item item2: linkConnect.connectionsOriginate(firstItem)) {	
			if ((item2.linkItems == 0) && (item2.representLinks == false)){
				removeItems(item2);
			}
		}
	}
    
	/**
	 * Print all the items
	 */
	public static void listItems() {

		for (Item item1: seqOfItems) {
			System.out.println("   "+item1.nameOfItem);
		}
	}

    /**
     * Main method
     * @param args
     */
	public static void main(String args[]) {
 		Scanner itemActionsScanned = new Scanner(System.in);
		while(itemActionsScanned.hasNextLine()){
			String line = itemActionsScanned.nextLine(); 
			String[] tokens = line.split("\\s+");

			if (tokens[0].equals("DEPEND")) {
				//action to DEPEND command
				Item item1 = getItem(tokens[1]);
				linkConnect.addItem(item1);
				for (int  i=2; i<tokens.length; i++) {
					Item item2 = getItem(tokens[i]);
					linkConnect.addItem(item2);
					linkConnect.addConnections(item1, item2);
				}
				//response to DEPEND command
				System.out.println(line);
			} 
			else if (tokens[0].equals("INSTALL")) {
				//response to INSTALL command
				System.out.println(line);
				Item item1 = getItem(tokens[1]);
				if(item1.linkItems == -1) {
					item1.representLinks = true;
				}
				installItems(item1);
			} 
			else if (tokens[0].equals("REMOVE")) {
				//response to REMOVE command
				System.out.println(line);
				Item item1 = getItem(tokens[1]);
				removeItems(item1);
			} 
			else if (tokens[0].equals("LIST")) {
				//response to LIST command
				System.out.println(line);
				listItems();
				
			}
			else if (tokens[0].equals("END")) {
				System.out.println(line);
			}
		}
	}



}
/**
 * Item data structure 
 * @author Amit
 *
 */
class Item {
	String nameOfItem;
	int linkItems;
	boolean representLinks;
    /**
     * Constructor
     * @param nameOfItem
     */
	Item(String nameOfItem) {
		this.nameOfItem = nameOfItem;
		linkItems = -1;
		representLinks = false;
	}

	public String getNameOfItem() {
		return nameOfItem;
	}

	public void setNameOfItem(String nameOfItem) {
		this.nameOfItem = nameOfItem;
	}

	public int getLinkItems() {
		return linkItems;
	}

	public void setLinkItems(int linkItems) {
		this.linkItems = linkItems;
	}

	public boolean isRepresentLinks() {
		return representLinks;
	}

	public void setRepresentLinks(boolean representLinks) {
		this.representLinks = representLinks;
	}
}
