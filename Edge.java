public class Edge {

    private AssignmentNode node;
    private int weight;

    public Edge(AssignmentNode node, int weight){
        this.node = node;
        this.weight = weight;
    }

    public AssignmentNode getNode() {
        return node;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (weight != edge.weight) return false;
        return node.equals(edge.node);
    }

    @Override
    public int hashCode() {
        int result = node.hashCode();
        result = 31 * result + weight;
        return result;
    }
}
