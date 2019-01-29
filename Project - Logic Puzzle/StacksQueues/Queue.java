package StacksQueues;

public class Queue<Type> extends List
{
	public void Enqueue(Type data)
	{
		super.Last();
		super.InsertAfter(data);
	}
	
	public void Dequeue()
	{
		super.First();
		super.Remove();
	}
}