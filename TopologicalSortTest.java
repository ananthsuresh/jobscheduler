import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import java.util.*;

public class TopologicalSortTest {

    private TopologicalSort sampleObject;
    private TopologicalSort.TestHook sampleTestObject;
    private Scheduler sampleScheduler;
    private Scheduler.TestHook sampleTestScheduler;

    @Before
    public void setUp() throws Exception {
		/* Set up the object to be tested */
        sampleObject = new TopologicalSort();
        sampleTestObject = sampleObject.new TestHook();
        sampleScheduler = new Scheduler();
        sampleTestScheduler = sampleScheduler.new TestHook();
    }

    /*Structured Basis: if statement true
    Good data: min data - one assignment, no relations*/
    @Test
    public void test_initialStep_true(){
        int assignmentArray[][] = {{1,2}};
        int relationshipArray[][] = {};
        sampleTestScheduler.initNodes(assignmentArray);
        sampleTestScheduler.addRelationships(relationshipArray);
        HashMap<Integer, Assignment> assignmentHashMap = sampleScheduler.getAssignments();
        AssignmentNode startNode = assignmentHashMap.get(1).getStartNode();
        AssignmentNode endNode = assignmentHashMap.get(1).getEndNode();
        sampleTestObject.initialStep(assignmentHashMap);
        assertEquals(new Integer(0), sampleObject.getInCounter().get(startNode));
        assertEquals(new Integer(1), sampleObject.getInCounter().get(endNode));
        assertEquals(startNode, sampleObject.getS().peek());
    }

    /*Structured Basis: if statement false*/
    @Test
    public void test_initialStep_false(){
        int assignmentArray[][] = {{1,2}, {2,3}};
        int relationshipArray[][] = {{1,1,2}};
        sampleTestScheduler.initNodes(assignmentArray);
        sampleTestScheduler.addRelationships(relationshipArray);
        HashMap<Integer, Assignment> assignmentHashMap = sampleScheduler.getAssignments();
        AssignmentNode startNode = assignmentHashMap.get(1).getStartNode();
        AssignmentNode endNode = assignmentHashMap.get(1).getEndNode();
        sampleTestObject.initialStep(assignmentHashMap);
        assertEquals(new Integer(1), sampleObject.getInCounter().get(startNode));
        assertEquals(new Integer(1), sampleObject.getInCounter().get(endNode));
        assertFalse(sampleObject.getS().contains(startNode));
    }

    /*Structured Basis: for loop not run
    Good data: minimum data, empty Hashmap*/
    @Test
    public void test_initialStep_emptyMap(){
        HashMap<Integer, Assignment> assignmentHashMap = sampleScheduler.getAssignments();
        sampleTestObject.initialStep(assignmentHashMap);
        assertTrue(sampleObject.getS().isEmpty());

    }

    /*Structural Basis: while loop not run and last if statement true
     Good data :minimum data*/
    @Test
    public void test_topologicalSort_emptyMap(){
        HashMap<Integer, Assignment> assignmentHashMap = sampleScheduler.getAssignments();
        LinkedList<AssignmentNode> order = sampleObject.topologicalSort(assignmentHashMap);
        assertTrue(order.isEmpty());

    }

    /*Structural Basis: inner if statement true */
    @Test
    public void test_topologicalSort_innerIfTrue(){
        int assignmentArray[][] = {{1,2},{2,3}};
        int relationshipArray[][] = {{1,1,2}};
        sampleTestScheduler.initNodes(assignmentArray);
        sampleTestScheduler.addRelationships(relationshipArray);
        HashMap<Integer, Assignment> assignmentHashMap = sampleScheduler.getAssignments();
        LinkedList<AssignmentNode> order = sampleObject.topologicalSort(assignmentHashMap);
        assertEquals(assignmentHashMap.get(2).getStartNode(),order.get(0));
        assertEquals(assignmentHashMap.get(2).getEndNode(),order.get(1));
        assertEquals(assignmentHashMap.get(1).getStartNode(),order.get(2));
        assertEquals(assignmentHashMap.get(1).getEndNode(),order.get(3));

    }

    /*Structural Basis: inner if statement false */
    @Test
    public void test_topologicalSort_innerIfFalse(){
        int assignmentArray[][] = {{1,2}};
        int relationshipArray[][] = {};
        sampleTestScheduler.initNodes(assignmentArray);
        sampleTestScheduler.addRelationships(relationshipArray);
        HashMap<Integer, Assignment> assignmentHashMap = sampleScheduler.getAssignments();
        LinkedList<AssignmentNode> order = sampleObject.topologicalSort(assignmentHashMap);
        assertEquals(assignmentHashMap.get(1).getStartNode(),order.get(0));
        assertEquals(assignmentHashMap.get(1).getEndNode(),order.get(1));
    }

    /*Structural Basis: last if statement false */
    @Test(expected = IllegalArgumentException.class)
    public void test_topologicalSort_cyclic(){
        int assignmentArray[][] = {{1, 3}, {2, 5}, {3, 2}, {4, 4}};
        int relationshipArray[][] = {{1, 2, 4}, {1, 1, 3}, {2, 4, 1}, {3,3,1}};
        sampleTestScheduler.initNodes(assignmentArray);
        sampleTestScheduler.addRelationships(relationshipArray);
        HashMap<Integer, Assignment> assignmentHashMap = sampleScheduler.getAssignments();
        sampleObject.topologicalSort(assignmentHashMap);
    }



}