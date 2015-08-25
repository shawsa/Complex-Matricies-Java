public class StringStack{
	StringNode head;
	public StringStack(){
		head = null;
	}
	
	public void push(String arg){
		head = new StringNode(head,arg);
	}
	
	public String peak(){
		return head.str;
	}
	
	public String pop(){
		if(head!=null){
			String ret = head.str;
			head = head.next;
			return ret;
		}
		return null;
	}
	
	public boolean isEmpty(){
		if(peak()==null)
			return true;
		return false;
	}
	
	
}
