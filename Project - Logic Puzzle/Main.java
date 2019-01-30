/*
Creator: Alex Faucheux
Last Modified: 1/28/2019

I took the approach to solve this problem with an AI BreadthFirst search.
The values for total missionaries and total cannibals are assigned to "maxMis" and "maxCan".
The value for max occupants for the boat is assigned to "boatCap" in stateNode class.
If you change the values for maxMis, maxCan, and/or boatCap, you can change the output!
If maxMis, maxCan, and boatCap values cause the program to reach an infinite loop, there is no answer! (the program will stop)
If there is no answer, you can increase the boatCap value in the StateNode class and/or make maxCan <= maxMis. 

NOTE:	A used class is called List<Type>, which is to not to be confused with the builtin class List<Type>.
	The class I use of that name is a custom Linked List located in StacksQueues.
*/

import StacksQueues.List;
import java.util.ArrayList;

public class Main
{
	final static int maxMis = 3;
	final static int maxCan = 3;
	final static int total = maxMis + maxCan;
	final private List<Integer> loopCheckList = new List<Integer>();
	final private ArrayList<StateNode> solStates = new ArrayList<StateNode>();
	final private ArrayList<List<StateNode>> routes = new ArrayList<List<StateNode>>();
	private List<StateNode> prevList;
	boolean branch = false;
	public int x;
	int Level;
	static boolean showLevels = false;	// Prints each level 
	static boolean showDeletedDuplicates = false; // Prints each level before duplicates are deleted (won't do anything if showLevels is false)
	int prevs;
	
	//Used to repeat characters
	public static String repeat(int count, String with) {
		return new String(new char[count]).replace("\0", with);
	}	
	
	/*
	A "state" refers to current state of the puzzle.
	ie, the root state (or initial state) is 
	[M M M C C C] v~~~ [_ _ _ _ _ _]
	*/
	
	// Used to print any state.
	// Also used to check for solution
	// Printing is triggered by assigning x to 1
	public void printState(StateNode state)
	{
		if(state != null && x == 1){
			int boat = state.getBoat();
			int ms = state.getStartMis();
			int cs = state.getStartCan();
			int mt = state.getTargetMis();
			int ct = state.getTargetCan();
			int startFill = total - (ms + cs);
			int targetFill = total - (mt + ct);
			String cMove = "";
			String mMove = "";
			String conjunction = "";
			String s = "";
			String t = "";
			String side = "";
			if(boat == 0){s = "v "; side = "left";}
			else{t = "v "; side = "right";}
			if(state.canMove != 0){cMove += state.canMove + " Cannibal" + (state.canMove > 1 ? "s " : "");}
			if(state.misMove != 0){mMove += state.misMove + (state.misMove > 1 ? " Missionaries " : " Missionary ");}
			if(state.canMove > 0 && state.misMove > 0){conjunction = " and ";}
			System.out.print("[ " + repeat(ms, "M ") + repeat(cs, "C ") + repeat(startFill, "_ ") + "] " + s + "~~~~ " + t + "[ " + repeat(targetFill, "_ ") + repeat(mt, "M ") + repeat(ct, "C ") + "]");
			if(state.canMove > 0 || state.misMove > 0)
				System.out.println((side.equals("right") ? " ------> " : " <------ ") + cMove + conjunction + mMove + " went to the " + side + " of the river.");
			else{System.out.println("		  " + maxMis + " Missionaries and " + maxCan + " Cannibals rest on the left side of the river.");}
		}
		else if(state.IsGoal()){Level = state.getLevel(); solStates.add(state);}
	}
	
	// Runs when solution state is found.
	// Prints all found routes.
	public void printRoutes()
	{
		for(int i=0; i<routes.size(); i++)
		{
			System.out.println("\n");
			List<StateNode> list = routes.get(i);
			list.First();
			for(int k=0; k<list.GetSize(); k++, list.Next())
				printState(list.GetValue());
		}
	}
	
	
	// Traverses prior states.
	// Runs when solution state is found.
	public List<StateNode> getRoute(StateNode state)
	{	
		
		List<StateNode> stateList = new List<StateNode>();
		prevList = new List<StateNode>();
		
		// States traversed are added to list.
		// State traversal stops when root state is reached
		// or when it reaches a fork.
		// ie, the current state has multiple links.
		while(state.getLevel() != 1)
		{
			stateList.InsertBefore(state);
			if(state.getPrev().GetSize() == 1)
				state = state.getPrev().GetValue();
			
			else break;
		}
		
		// Returns list of states when root state is reached.
		if(state.getLevel() == 1)
		{
			stateList.InsertBefore(state);
			return stateList;
		}
		
		// Ran when traversal reaches a fork for the first time.
		// Routes is a list of all completed routes.
		if(!branch)
		{
			branch = true;
			state.getPrev().First();
			routes.addAll(Combine(state.getPrev(), stateList));
			return stateList;
		}
		
		// Ran when traversal reaches a fork. 
		else
		{
			prevList = state.getPrev();
			prevList.First();
			return stateList;
		}
	}
		
	/*
	For method Combine: 
	
	Used to find and return list of all routes found in tree. List used to print solutions.
	
	Takes two arguments: "leaves" and "root", both of which are linked list of states.
	"root" == list of states; "leaves" == list of leaves linked to the last state added to "root".
	This method takes the initial "root" states and "leaves" to create and return a list of all routes
	that lead to the solution.
	   
	NOTE: "root" initially starts as list of root states.
		  If Combine() repeats because a fork is reached, 
		  "branch" becomes the new "root". What happens is that
		  when root (or initial) state is reached, current branch attaches to current root, 
		  and that root will attach to its root (if it has one). This is done
		  recursively.
	*/
	
	// Runs when solution state is found.
	public ArrayList<List<StateNode>> Combine(List<StateNode> leaves, List<StateNode> root)
	{
		ArrayList<List<StateNode>> lists = new ArrayList<List<StateNode>>();
		ArrayList<List<StateNode>> subBranches = new ArrayList<List<StateNode>>();
		
		// Get route for branch
		List<StateNode> branch = getRoute(leaves.GetValue());
		
		// If branch leads to a fork
		if(prevList.GetSize() > 1)
		{
			subBranches = Combine(prevList, branch);
			for(int i=0; i<subBranches.size(); i++)
				lists.add(subBranches.get(i).Add(root));
		}
		
		else lists.add(branch.Add(root));
		
	
		// If all leaves have been traversed.
		if(root.get(0).getPrev().GetPos() == root.get(0).getPrev().GetSize() - 1)
			return lists;
		
		// Otherwise, traverse next leaf.
		else
		{
			leaves.Next();
			lists.addAll(Combine(leaves, root));
			return lists;
		}
		
	}
		
	//Finds all successors to a state and puts all legal ones in a list.
	//ie, all possible ways you can move the cannibals and missionaries legally from the current state
	public List<StateNode> Successions(StateNode state)
	{
		x = 0;
		int ms = 0;
		int cs = 0;
		int mt = 0;
		int ct = 0;
		
		//Makes list of successors for the state
		List<StateNode> stateList = state.findSuccessors();
		List<StateNode> stateList2 = new List<StateNode>();
		
		stateList.First();
		
		//Makes copy of list without nulls
		for(int i=0; i<stateList.GetSize(); i++, stateList.Next())
		{
			StateNode state2 = stateList.GetValue();
			if(state2 != null) stateList2.InsertAfter(state2);
		}
		
		//Removes all invalid states from copy
		stateList2.First();
		int num = stateList2.GetSize();
		for(int i=0; i<num; i++, stateList2.Next())
		{
			StateNode state2 = stateList2.GetValue();
			ms = state2.getStartMis();
			cs = state2.getStartCan();
			mt = state2.getTargetMis();
			ct = state2.getTargetCan();
			if((cs > ms && ms > 0) || (ct > mt && mt > 0) || (ct + mt == 0)) stateList2.Remove();
		}
		
		//In case any are missed in first run through (it was during testing, which was causing problems)
		stateList2.First();
		num = stateList2.GetSize();
		for(int i=0; i<num; i++, stateList2.Next())
		{
			StateNode state2 = stateList2.GetValue();
			ms = state2.getStartMis();
			cs = state2.getStartCan();
			mt = state2.getTargetMis();
			ct = state2.getTargetCan();
			if((cs > ms && ms > 0) || (ct > mt && mt > 0) || (ct + mt == 0)) stateList2.Remove();
		}
		
		return stateList2;
	}
	
	//Iterates through the states in the current level and creates the next level of states
	public List<StateNode> getNextLevel(List<StateNode> level)
	{
		x = 0;
		ArrayList<List<StateNode>> nextLevel = new ArrayList<List<StateNode>>();
		
		//Creates a list of successors of each state in stateList
		//Puts each list into level. 
		//Every state within these lists, and every list inside level, are on the same level
		level.First();
		for(int i=0; i<level.GetSize(); level.Next(), i++){
			List<StateNode> list = Successions(level.GetValue());
			if(!list.IsEmpty()) nextLevel.add(list);
		}
		
		//Shows each level (with duplicates)
		if(showLevels && showDeletedDuplicates)
		{
			x = 1;
			System.out.println("Level: " + (nextLevel.get(0).GetValue().getLevel()));
			for(int i=0; i<nextLevel.size(); i++){
				nextLevel.get(i).First();
				for(int k=0; k<nextLevel.get(i).GetSize(); k++, nextLevel.get(i).Next())
					printState(nextLevel.get(i).GetValue());
			}
			x = 0;
		}
		
		
		
		//Deletes all duplicate states (except for any duplicate solution states)
		//puts all remaining states into one list.
		level = new StateNode().Clean(nextLevel);
		level.First();
		
		//Shows each level (without duplicates)
		if(showLevels && !showDeletedDuplicates)
		{
			x = 1;
			System.out.println("Level: " + (level.GetValue().getLevel()));
			for(int i=0; i<level.GetSize(); level.Next(), i++)
			{
				printState(level.GetValue());
				System.out.println("\nPrev List:");
				level.GetValue().getPrev().First();
				for(int k=0; k<level.GetValue().getPrev().GetSize(); k++, level.GetValue().getPrev().Next())
				{
					printState(level.GetValue().getPrev().GetValue());
				}
				System.out.println("");
			}
			x = 0;
		}
		
		if(showLevels) System.out.println("\n\n");
		
		level.First();
		
		//Checks every state in level for the solution state.
		for(int i=0; i<level.GetSize(); i++, level.Next())
			printState(level.GetValue());
		
		
		//If solutions were found, prints all available solutions and ends program
		if(solStates.size() > 0)
		{
			x = 1;
			for(int i=0; i<solStates.size(); i++)
			{
				branch = false;
				getRoute(solStates.get(i));
			}
			
			System.out.println("Number of solutions to this problem: " + routes.size() + "\nThe " + (routes.size() > 1 ? "solutions are " : "solution is ") + "on level " + Level);
			printRoutes();
			System.exit(0);
		}
		
		return level;
	}
	
	
	//Finds Answer
	public boolean findAnswer(List<StateNode> level)
	{
		if(level.GetValue().getLevel() > 20){
			
			// Checks if the size of the list repeats. If so, kills the program.
			if(!loopCheckList.Contains(level.GetSize()))
			{
				loopCheckList.InsertAfter(level.GetSize());
				if(loopCheckList.GetSize() > 2)
				{
					loopCheckList.First();
					loopCheckList.Remove();
					loopCheckList.Last();
				}
			}
			else{System.out.println("There is no answer!"); System.exit(0);}
		}
		
		level.First();
		List<StateNode> nextLevel = getNextLevel(level);
		return findAnswer(nextLevel);
	}
		
	public static void main(String[] args)
	{
		//Initial State and initial level
		StateNode state = new StateNode(maxMis, maxCan, 0, 0, 0, 1);
		
		if(showLevels){
			Main main = new Main();
			main.x = 1;
			System.out.println("Level: " + state.getLevel());
			main.printState(state);
			System.out.println("\n\n");
			main.x = 0;
			}
		
		//Creates successors for the initial state, making level 2
		List<StateNode> stateList = new Main().Successions(state);
		
		if(showLevels){
			stateList.First();
			Main main = new Main();
			main.x = 1;
			System.out.println("Level: " + stateList.GetValue().getLevel());
			for(int i=0; i<stateList.GetSize(); i++, stateList.Next())
				main.printState(stateList.GetValue());
			System.out.println("\n\n");
			main.x = 0;
			}
			
		//Starts looking for the solution
		new Main().findAnswer(stateList);
	}
}