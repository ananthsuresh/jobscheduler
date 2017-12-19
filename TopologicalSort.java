import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;

public class TopologicalSort {

    public TopologicalSort() {
    }

    private Stack<AssignmentNode> s = new Stack<>();
    private HashMap<AssignmentNode, Integer> inCounter = new HashMap<>();

    public Stack<AssignmentNode> getS() {
        return s;
    }

    public HashMap<AssignmentNode, Integer> getInCounter() {
        return inCounter;
    }

    public LinkedList<AssignmentNode> topologicalSort(HashMap<Integer, Assignment> assignments){
        initialStep(assignments);
        LinkedList<AssignmentNode> order = new LinkedList<>();
        while(!s.empty()){
            AssignmentNode vertex = s.pop();
            order.add(vertex);
            for(Edge e : vertex.getOutEdges()){
                AssignmentNode otherVertex = e.getNode();
                int numEdges = inCounter.get(otherVertex) - 1;
                inCounter.put(otherVertex, numEdges);
                if(numEdges == 0){
                    s.push(otherVertex);
                }
            }
        }

        if(order.size() == assignments.size() * 2){
            return order;
        }
        else{
            throw new IllegalArgumentException();
        }

    }

    private void initialStep(HashMap<Integer, Assignment> assignments){
        for(Map.Entry<Integer, Assignment> assignment : assignments.entrySet()){
            AssignmentNode startNode = assignment.getValue().getStartNode();
            AssignmentNode endNode = assignment.getValue().getEndNode();
            int numInEdgesStart = startNode.getInEdges().size();
            int numInEdgesEnd = endNode.getInEdges().size();
            inCounter.put(startNode, numInEdgesStart);
            inCounter.put(endNode, numInEdgesEnd);
            if(numInEdgesStart == 0){
                s.push(startNode);
            }
//            if(numInEdgesEnd == 0){
//                s.push(endNode);
//            }
        }
    }


    public class TestHook{
        public void initialStep(HashMap<Integer, Assignment> assignments){
            TopologicalSort.this.initialStep(assignments);
        }

    }
}
