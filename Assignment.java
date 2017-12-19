public class Assignment {

    private int ID;
    private AssignmentNode startNode;
    private AssignmentNode endNode;

    public Assignment(int ID, AssignmentNode startNode, AssignmentNode endNode) {
        this.ID = ID;
        this.startNode = startNode;
        this.endNode = endNode;
    }

    public int getID() {
        return ID;
    }

    public AssignmentNode getStartNode() {
        return startNode;
    }

    public AssignmentNode getEndNode() {
        return endNode;
    }
}
