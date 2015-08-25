public class StringNode{
	StringNode next;
	String str;
	public StringNode(){
		next = null;
		str = null;
	}
	public StringNode(StringNode next, String str){
		this.next = next;
		this.str = str;
	}
}
