import java.util.LinkedList;

public class AssignmentNode {

    private int timeToNode;
    private int ID;
    boolean isStart;
    LinkedList<Edge> inEdges = new LinkedList<>();
    LinkedList<Edge> outEdges = new LinkedList<>();

    public AssignmentNode(int ID, boolean isStart) {
        this.ID = ID;
        this.isStart = isStart;
        this.timeToNode = 0;
    }

    public int getTimeToNode() {
        return timeToNode;
    }

    public int getID() {
        return ID;
    }

    public boolean isStart() {
        return isStart;
    }

    public LinkedList<Edge> getInEdges() {
        return inEdges;
    }

    public LinkedList<Edge> getOutEdges() {
        return outEdges;
    }

    public void setTimeToNode(int timeToNode) {
        this.timeToNode = timeToNode;
    }

    public void addInEdge(Edge e){
        inEdges.add(e);
    }

    public void addOutEdge(Edge e){
        outEdges.add(e);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AssignmentNode that = (AssignmentNode) o;

        if (ID != that.ID) return false;
        return isStart == that.isStart;
    }

    @Override
    public int hashCode() {
        int result = ID;
        result = 31 * result + (isStart ? 1 : 0);
        return result;
    }
}
