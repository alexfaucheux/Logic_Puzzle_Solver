import StacksQueues.*;
import java.util.ArrayList;

public class StateNode
{   
    private static int maxMis;
    private static int maxCan;
    private final int boatCap = 3;
    private int boat;
    private int startMis;
    private int startCan;
    private int targetCan;
    private int targetMis;
    private List<StateNode> Prev;
    private int level;
    public int misMove;
    public int canMove;
	
    
    public StateNode()
    {
    }
    
    //Make initial state node
    public StateNode(int startMis, int startCan, int targetMis, int targetCan, int boat, int level)
    {
        this(startMis, startCan, targetMis, targetCan, boat, null, 0, 0, level);
    }   
    
    //Make state node
    public StateNode(int startMis, int startCan, int targetMis, int targetCan, int boat, List<StateNode> prev, int misMove, int canMove, int level)
    {
        if(prev == null){maxCan = startCan; maxMis = startMis;}
        this.startMis = startMis;
        this.startCan = startCan;
        this.targetCan = targetCan;
        this.targetMis = targetMis;
        this.boat = boat;
        this.Prev = prev;
        this.level = level;
        this.misMove = misMove;
        this.canMove = canMove;
    }
    
    //Creates new state node when the program wants to move 1 or more cannibals and/or missionaries
    //If requested state is not valid, then null is returned.
    public StateNode Move(int m, int c)
    {
        int ms = this.startMis;
        int cs = this.startCan;
        int mt = this.targetMis;
        int ct = this.targetCan;
        List<StateNode> prev = new List<StateNode>();
		prev.InsertAfter(this);
        if(m + c > boatCap || m < 0 || c < 0 || m + c == 0) {return null;}
        if(boat == 0)
        {
            StateNode state = new StateNode(ms -= m, cs -= c, mt += m, ct += c, 1, prev, m, c, this.level+1);
            return state;
        }
        
        else
        {
            StateNode state = new StateNode(ms += m, cs += c, mt -= m, ct-= c, 0, prev, m, c, this.level+1);
            return state;
        }
	}
    
    //Makes list of successors of a state.
    public List<StateNode> findSuccessors()
    {
        int m = 0;
        int c = 0;
        
        List<StateNode> stateList = new List<StateNode>();
        if(this.boat == 0){m = this.startMis; c = this.startCan;}
        else{m = this.targetMis; c = this.targetCan;}
        
        for(int i=0; i<=m; i++)
        {
            for(int k=0; k<=c; k++)
                stateList.InsertAfter(this.Move(i, k));
        }

        return stateList;
    }

    //Checks to see if solution state is met.
    public boolean IsGoal()
    {
        if(this.startMis == 0
        && this.startCan == 0
        && this.targetCan == maxCan
        && this.targetMis == maxMis)
        {return true;}
        return false;
    }
    
    //Checks to see if a state list contains a certain state
    public boolean Contains(List<StateNode> list, StateNode state)
    {
        list.First();
        for(int i=0; i<list.GetSize(); i++, list.Next())
            if(list.GetValue().startMis == state.startMis
            && list.GetValue().targetMis == state.targetMis
            && list.GetValue().startCan == state.startCan
            && list.GetValue().targetCan == state.targetCan
            && list.GetValue().boat == state.boat) return true;
        return false;
    }
            
    //Compares each element in a list to each other.
    //Returns a list without dupicate elements.
    public List<StateNode> Clean(ArrayList<List<StateNode>> list)
    {
		List<StateNode> states = new List<StateNode>();
		int j=0;
		
		for(int i=0; i<list.size(); i++)
		{
			list.get(i).First();
			for(int k=0; k<list.get(i).GetSize(); list.get(i).Next(), k++)
			{
				list.get(i).SetPos(k);
				StateNode state = list.get(i).GetValue();
				if(states.IsEmpty()){states.InsertAfter(state); continue;}
				if(!Contains(states, state) || state.IsGoal()) states.InsertAfter(state);
				
				// Before deleting duplicate state, gives the link it has
				// to the state already in "states".
				else states.GetValue().addPrev(state.Prev.GetValue());
			}
		}
		return states;
	}    
            
    
    
    
    //GETTERS AND SETTERS
    public int getStartMis()
    {
        return startMis;
    }
    
    public int getStartCan()
    {
        return startCan;
    }
    
    public int getTargetMis()
    {
        return targetMis;
    }
    
    public int getTargetCan()
    {
        return targetCan;
    }
    
    public int getBoat()
    {
        return boat;
    }
    
    public List<StateNode> getPrev()
    {
        return Prev;
    }
    
	public void addPrev(StateNode state)
	{
		Prev.Last();
		Prev.InsertAfter(state);
	}
	
	public void resetPrev()
	{
		Prev = new List<StateNode>();
	}
	
    public int getLevel()
    {
        return level;
    }
}
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        