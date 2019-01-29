package StacksQueues;

public class Stack<Type> extends List
{
	public void Push(Type data)
	{
		super.Last();
		super.InsertAfter(data);
	}
	
	public void Pop()
	{
		super.Last();
		super.Remove();
	}
	
	public Type Peek()
	{
		super.Last();
		Type data = (Type) super.GetValue();
		return data;
	}
	
	public Type reversePeek()
	{
		super.First();
		Type data = (Type) super.GetValue();
		return data;
	}
}