package StacksQueues;

class Node<Type>
{
	private Type data;
	private Node<Type> link;

	// Constructor for a new, empty node.
	public Node()
	{
		this.data = null;
		this.link = null;
	}
	
	// Constructor for a new, filled node.
	public Node(Type data, Node<Type> link)
	{
		this.data = data;
		this.link = link;
	}

	// GETTERS AND SETTERS
	public Type getData()
	{
		return this.data;
	}

	public void setData(Type data)
	{
		this.data = data;
	}

	public Node<Type> getNext()
	{
		return this.link;
	}

	public void setLink(Node<Type> link)
	{
		this.link = link;
	}
}