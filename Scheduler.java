import java.util.HashMap;
import java.util.LinkedList;

public class Scheduler {

    private HashMap<Integer, Assignment> assignments = new HashMap<>();

    public Scheduler() {
    }

    public HashMap<Integer, Assignment> getAssignments() {
        return assignments;
    }

    private void initNodes(int[][] assignmentArray) throws IllegalArgumentException{
        checkAssignmentArray(assignmentArray);
        for(int[] assignment : assignmentArray){
            int assignmentID = assignment[0];
            if(assignments.containsKey(assignmentID)){
                throw new IllegalArgumentException();
            }
            int assignmentDuration = assignment[1];
            AssignmentNode startNode = new AssignmentNode(assignmentID, true);
            AssignmentNode endNode = new AssignmentNode(assignmentID, false);
            Edge outEdge = new Edge(endNode, assignmentDuration);
            startNode.addOutEdge(outEdge);
            Edge inEdge = new Edge(startNode, assignmentDuration);
            endNode.addInEdge(inEdge);
            Assignment a = new Assignment(assignmentID, startNode, endNode);
            assignments.put(assignmentID, a);
        }
    }

    private void addRelationships(int[][] relationships) throws IllegalArgumentException{
        checkRelationshipArray(relationships);
        for(int [] relationship : relationships){
            Edge outEdge;
            Edge inEdge;
            Assignment assignment1 = assignments.get(relationship[0]);
            Assignment assignment2 = assignments.get(relationship[2]);
            //make enum type in Edge class
            switch (relationship[1]){
                case 1:
                    outEdge = new Edge(assignment1.getStartNode(), 0);
                    assignment2.getEndNode().addOutEdge(outEdge);
                    inEdge = new Edge(assignment2.getEndNode(), 0);
                    assignment1.getStartNode().addInEdge(inEdge);
                    break;
                case 2:
                    outEdge = new Edge(assignment1.getStartNode(), 0);
                    assignment2.getStartNode().addOutEdge(outEdge);
                    inEdge = new Edge(assignment2.getStartNode(), 0);
                    assignment1.getStartNode().addInEdge(inEdge);
                    break;
                case 3:
                    outEdge = new Edge(assignment1.getEndNode(), 0);
                    assignment2.getStartNode().addOutEdge(outEdge);
                    inEdge = new Edge(assignment2.getStartNode(), 0);
                    assignment1.getEndNode().addInEdge(inEdge);
                    break;
                case 4:
                    outEdge = new Edge(assignment1.getEndNode(), 0);
                    assignment2.getEndNode().addOutEdge(outEdge);
                    inEdge = new Edge(assignment2.getEndNode(), 0);
                    assignment1.getEndNode().addInEdge(inEdge);
                    break;
                default:
                    throw new IllegalArgumentException();

            }
        }
    }


    public int calculateDuration(int[][] assignmentArray, int[][] relationships) {
        initNodes(assignmentArray);
        addRelationships(relationships);
        LinkedList<AssignmentNode> order = new TopologicalSort().topologicalSort(assignments);
        int highestTime = 0;
        for (AssignmentNode node : order) {
            int max = 0;
            for (Edge e : node.getInEdges()) {
                int edgePlusNode = e.getWeight() + e.getNode().getTimeToNode();
                if (edgePlusNode > max) {
                    max = edgePlusNode;
                }
            }
            node.setTimeToNode(max);
            if (max > highestTime) {
                highestTime = max;
            }
        }
        return highestTime;
    }

    private void checkAssignmentArray(int[][] array) throws  IllegalArgumentException{
        for(int[] innerArray : array){
            if(innerArray.length != 2){
                throw new IllegalArgumentException();
            }
            if(innerArray[1] < 0){
                throw new IllegalArgumentException();
            }
        }
    }


    private void checkRelationshipArray(int[][] array) throws  IllegalArgumentException{
        for(int[] innerArray : array){
            if(innerArray.length != 3){
                throw new IllegalArgumentException();
            }
            if(!(assignments.containsKey(innerArray[0]) && assignments.containsKey(innerArray[2]))){
                throw new IllegalArgumentException();
            }
        }
    }

    public class TestHook{
        public void initNodes(int[][] assignmentArray){
            Scheduler.this.initNodes(assignmentArray);
        }

        public void addRelationships(int[][] relationships){
            Scheduler.this.addRelationships(relationships);
        }
        public void checkAssignmentArray(int[][] array) throws  IllegalArgumentException{
            Scheduler.this.checkAssignmentArray(array);
        }
        public void checkRelationshipArray(int[][] array) throws  IllegalArgumentException{
            Scheduler.this.checkRelationshipArray(array);
        }


    }
    public static void main(String[] args){
        int assignmentArray[][]={{1,6}};
        int relationships[][]={{1,1,4}};
        Scheduler scheduler = new Scheduler();
        scheduler.calculateDuration(assignmentArray, relationships);
    }

}
