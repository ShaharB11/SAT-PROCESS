import java.util.Vector;
public class Queue <T>  {
	private Vector<T> buffer;
	private IsOver is_over;
	public Queue(IsOver is_over)//Constructor.
	{
		this.buffer = new Vector<T>();
		this.is_over=is_over;
	}
	public synchronized void insert(T item)//Synchronized function that insert a generic type to the queue.
	{
		buffer.add(item);
		this.notifyAll();
	}
	public synchronized T extract()//Synchronized function that extract a generic type from the queue and remove it from the queue.
	{
		while(buffer.isEmpty() && !this.is_over.is_over())
		{
			try {
				this.wait(100);
			} catch (InterruptedException e) {
				continue;
			}
		}
		if(this.is_over.is_over())
		{
			return null;
		}
		return buffer.remove(0);
	}
	public int get_size() {
		return this.buffer.size();
	}
}
