import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import java.util.*;

public class SchedulerTest {
    private Scheduler sampleObject;
    private Scheduler.TestHook sampleTestObject;

    @Before
    public void setUp() throws Exception {
		/* Set up the object to be tested */
        sampleObject = new Scheduler();
        sampleTestObject = sampleObject.new TestHook();
    }


    /*Structured Basis: if statement is false
    Good data: minimum data */
    @Test
    public void test_initNodes_min_data(){
        int assignmentArray[][] = {{1,1}};
        sampleTestObject.initNodes(assignmentArray);
        HashMap<Integer, Assignment> assignments = sampleObject.getAssignments();
        LinkedList<Edge> startInEdges = assignments.get(1).getStartNode().getInEdges();
        LinkedList<Edge> startOutEdges = assignments.get(1).getStartNode().getOutEdges();
        LinkedList<Edge> endInEdges = assignments.get(1).getEndNode().getInEdges();
        LinkedList<Edge> endOutEdges = assignments.get(1).getEndNode().getOutEdges();
        assertTrue(startInEdges.isEmpty());
        assertTrue(endOutEdges.isEmpty());
        assertEquals(startOutEdges.getFirst().getNode(), new AssignmentNode(1,false));
        assertEquals(endInEdges.getFirst().getNode(), new AssignmentNode(1, true));

    }

    /*Bad data: less data - one */
    @Test(expected = IllegalArgumentException.class)
    public void test_initNodes_less_data(){
        int assignmentArray[][] = {{1}, {2}, {3}, {4}};
        sampleTestObject.initNodes(assignmentArray);
    }


    /*Structural basis: if statement is true
    Bad data: too much data, same assignment ID with more than one duration*/
    @Test(expected = IllegalArgumentException.class)
    public void test_initNodes_excess_data(){
        int assignmentArray[][] = {{1,1}, {1,2}, {1,3}, {1,4}};
        sampleTestObject.initNodes(assignmentArray);
    }

    /*Bad data: no data */
    @Test(expected = IllegalArgumentException.class)
    public void test_initNodes_no_data(){
        int assignmentArray[][] = {{}, {}, {}, {}};
        sampleTestObject.initNodes(assignmentArray);
    }

    /*Bad data: wrong data - duration should not be negative */
    @Test(expected = IllegalArgumentException.class)
    public void test_initNodes_wrong_data(){
        int assignmentArray[][] = {{1, -3}, {2, -4}};
        sampleTestObject.initNodes(assignmentArray);
    }

    /*Structured Basis: case 1
    Good data: nominal case, 1 relationship */
    @Test
    public void test_addRelationships_endBegin(){
        int assignmentArray[][] = {{1,3}, {2,4}};
        int relationshipArray[][] = {{1,1,2}};
        sampleTestObject.initNodes(assignmentArray);
        sampleTestObject.addRelationships(relationshipArray);
        HashMap<Integer, Assignment> assignments = sampleObject.getAssignments();
        assertTrue(assignments.get(1).getStartNode().getInEdges().contains(new Edge(new AssignmentNode(2, false), 0)));
        assertTrue(assignments.get(2).getEndNode().getOutEdges().contains(new Edge(new AssignmentNode(1, true), 0)));
    }

    /*Structured Basis: case 2*/
    @Test
    public void test_addRelationships_beginBegin(){
        int assignmentArray[][] = {{1,3}, {2,4}};
        int relationshipArray[][] = {{1,2,2}};
        sampleTestObject.initNodes(assignmentArray);
        sampleTestObject.addRelationships(relationshipArray);
        HashMap<Integer, Assignment> assignments = sampleObject.getAssignments();
        assertTrue(assignments.get(1).getStartNode().getInEdges().contains(new Edge(new AssignmentNode(2, true), 0)));
        assertTrue(assignments.get(2).getStartNode().getOutEdges().contains(new Edge(new AssignmentNode(1, true), 0)));
    }

    /*Structured Basis: case 3*/
    @Test
    public void test_addRelationships_beginEnd(){
        int assignmentArray[][] = {{1,3}, {2,4}};
        int relationshipArray[][] = {{1,3,2}};
        sampleTestObject.initNodes(assignmentArray);
        sampleTestObject.addRelationships(relationshipArray);
        HashMap<Integer, Assignment> assignments = sampleObject.getAssignments();
        assertTrue(assignments.get(1).getEndNode().getInEdges().contains(new Edge(new AssignmentNode(2, true), 0)));
        assertTrue(assignments.get(2).getStartNode().getOutEdges().contains(new Edge(new AssignmentNode(1, false), 0)));
    }

    /*Structured Basis: case 4*/
    @Test
    public void test_addRelationships_endEnd(){
        int assignmentArray[][] = {{1,3}, {2,4}};
        int relationshipArray[][] = {{1,4,2}};
        sampleTestObject.initNodes(assignmentArray);
        sampleTestObject.addRelationships(relationshipArray);
        HashMap<Integer, Assignment> assignments = sampleObject.getAssignments();
        assertTrue(assignments.get(1).getEndNode().getInEdges().contains(new Edge(new AssignmentNode(2, false), 0)));
        assertTrue(assignments.get(2).getEndNode().getOutEdges().contains(new Edge(new AssignmentNode(1, false), 0)));
    }

    /* Structural basis - for loop doesn't run
    Good data: minimum data - no relationships*/
    @Test
    public void test_addRelationships_min_data(){
        int relationshipArray[][] = {};
        try {
            sampleTestObject.addRelationships(relationshipArray);
        }catch (Exception e){
            fail("Should not throw any Exception");
        }
    }

    /*Bad data: less data - 1 */
    @Test(expected = IllegalArgumentException.class)
    public void test_addRelationships_less_data_1(){
        int relationshipArray[][] = {{1}, {2}, {3}, {4}};
        sampleTestObject.addRelationships(relationshipArray);
    }

    /*Bad data: less data - 2 */
    @Test(expected = IllegalArgumentException.class)
    public void test_addRelationships_less_data_2(){
        int relationshipArray[][] = {{1,2}, {2,3}, {3,4}, {4,1}};
        sampleTestObject.addRelationships(relationshipArray);
    }

    /*Bad data: wrong data - relationship is not one of the predefined */
    @Test(expected = IllegalArgumentException.class)
    public void test_addRelationships_wrong_data(){
        int relationshipArray[][] = {{1, 8, 3}};
        sampleTestObject.addRelationships(relationshipArray);
    }

    /*Bad data: wrong data - relationship involves an assignment that doesn't exist */
    @Test(expected = IllegalArgumentException.class)
    public void test_addRelationships_nonexistent_assignment(){
        int assignmentArray[][] = {{1,5}};
        int relationshipArray[][] = {{1,1,6}};
        sampleTestObject.initNodes(assignmentArray);
        sampleTestObject.addRelationships(relationshipArray);
    }

    /*Structural basis: both if statements false
    Good data: min data - one assignment */
    @Test
    public void test_checkAssignmentArray_min_data(){
        int assignmentArray[][] = {{1,5}};
        try {
            sampleTestObject.checkAssignmentArray(assignmentArray);
        }catch (Exception e){
            fail("Should not throw any Exception");
        }
    }

    /*Structural basis: first if statement true*/
    @Test(expected = IllegalArgumentException.class)
    public void test_checkAssignmentArray_length(){
        int assignmentArray[][] = {{1,5,3}};
        sampleTestObject.checkAssignmentArray(assignmentArray);
    }

    /*Structural basis: second if statement true*/
    @Test(expected = IllegalArgumentException.class)
    public void test_checkAssignmentArray_duration(){
        int assignmentArray[][] = {{1,-1}};
        sampleTestObject.checkAssignmentArray(assignmentArray);
    }

    /*Boundary: duration = 0*/
    @Test
    public void test_checkAssignmentArray_boundary_equal(){
        int assignmentArray[][] = {{1,0}};
        try {
            sampleTestObject.checkAssignmentArray(assignmentArray);
        }catch (Exception e){
            fail("Should not throw any Exception");
        }
    }
    /*Boundary: duration = 1*/
    @Test
    public void test_checkAssignmentArray_boundary_above(){
        int assignmentArray[][] = {{1,1}};
        try {
            sampleTestObject.checkAssignmentArray(assignmentArray);
        }catch (Exception e){
            fail("Should not throw any Exception");
        }
    }

    /*Boundary: duration = -1*/
    @Test(expected = IllegalArgumentException.class)
    public void test_checkAssignmentArray_boundary_below(){
        int assignmentArray[][] = {{1,-1}};
        sampleTestObject.checkAssignmentArray(assignmentArray);
    }


    /*Structural basis: both if statements false
    Good data: min data - one relationship */
    @Test
    public void test_checkRelationshipArray_min_data(){
        int assignmentArray[][] = {{1,2},{4,5}};
        sampleTestObject.initNodes(assignmentArray);
        int relationshipArray[][] = {{1,1,4}};
        try {
            sampleTestObject.checkRelationshipArray(relationshipArray);
        }catch (Exception e){
            fail("Should not throw any Exception");
        }
    }

    /*Structural basis: first if statement true*/
    @Test(expected = IllegalArgumentException.class)
    public void test_checkRelationshipArray_length(){
        int assignmentArray[][] = {{1,2}};
        sampleTestObject.initNodes(assignmentArray);
        int relationshipArray[][] = {{1,3}};
        sampleTestObject.checkRelationshipArray(relationshipArray);
    }

    /*Structural basis: second if statement true*/
    @Test(expected = IllegalArgumentException.class)
    public void test_checkRelationshipArray_notPresent(){
        int assignmentArray[][] = {{1,2}};
        sampleTestObject.initNodes(assignmentArray);
        int relationshipArray[][] = {{1,2,4}};
        sampleTestObject.checkRelationshipArray(relationshipArray);
    }

    /*Structural basis: outer for loop doesn't run
    * Good data - minimum data*/
    @Test
    public void test_calculateDuration_empty(){
        int assignmentArray[][] = {};
        int relationships[][] = {};
        assertEquals(0, sampleObject.calculateDuration(assignmentArray, relationships));
    }

    /*Structural basis: inner and outer if statements are true*/
    @Test
    public void test_calculateDuration_innerAndOuterIfTrue(){
        int assignmentArray[][] = {{1,2}};
        int relationships[][] = {};
        assertEquals(2, sampleObject.calculateDuration(assignmentArray, relationships));
    }

    /*Structural basis: inner and outer if statements are false
    * Boundary 1 equal : edgePlusNode = max
    * Boundary2 equal : max = highestTime*/
    @Test
    public void test_calculateDuration_innerAndOuterFalse(){
        int assignmentArray[][] = {{1,0}};
        int relationships[][] = {};
        assertEquals(0, sampleObject.calculateDuration(assignmentArray, relationships));
    }

    /* Boundary 1 above: edgePlusNode = max + 1
     Boundary 2 above: max = highestTime + 1*/
    @Test
    public void test_calculateDuration_boundary1and2above(){
        int assignmentArray[][] = {{1,1}};
        int relationships[][] = {};
        assertEquals(1, sampleObject.calculateDuration(assignmentArray, relationships));
    }

    /* Boundary 1 below: edgePlusNode = max - 1
    * Boundary 2 below: max = highestTime - 1*/
    @Test
    public void test_calculateDuration_boundary1and2below(){
        int assignmentArray[][] = {{1,2}, {2,1}};
        int relationships[][] = {};
        assertEquals(2, sampleObject.calculateDuration(assignmentArray, relationships));
    }

    /* Stress test */
    @Test
    public void test_calculateDuration_stressTest(){
        int assignmentArray[][] = new int[1000][1000];
        for(int i = 0; i < 1000; i++){
            int innerArray[] = {i + 1, 1};
            assignmentArray[i] = innerArray;
        }
        int relationships[][] = new int[999][999];
        for(int i = 0; i < 999; i++){
            int innerArray[] = {i + 1, 1, i + 2};
            relationships[i] = innerArray;
        }
        assertEquals(1000, sampleObject.calculateDuration(assignmentArray, relationships));
    }



    //Error Guessing tests from PA 7 below
    @Test
    public void exampleTest() {
        int assignmentArray[][] = {{1, 3}, {2, 5}, {3, 2}, {4, 4}};
        int relationships[][] = {{1, 2, 4}, {1, 1, 3}, {2, 4, 1}};
        assertEquals(5, sampleObject.calculateDuration(assignmentArray, relationships));
    }


    @Test(expected = IllegalArgumentException.class)
    public void cyclicTest() throws IllegalArgumentException{
        int assignmentArray[][] = {{1, 3}, {2, 5}, {3, 2}, {4, 4}};
        int relationships[][] = {{1, 2, 4}, {1, 1, 3}, {2, 4, 1}, {3,3,1}};
        sampleObject.calculateDuration(assignmentArray, relationships);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalRelationshipTest() throws IllegalArgumentException{
        int assignmentArray[][] = {{1, 3}, {2, 5}, {3, 2}, {4, 4}};
        int relationships[][] = {{1, 2, 4}, {1, 8, 3}, {2, 4, 1}};
        sampleObject.calculateDuration(assignmentArray, relationships);
    }

    @Test
    public void disconnectedTest() {
        int assignmentArray[][] = {{1, 3}, {2, 5}, {3, 2}, {4, 4}, {5, 3}};
        int relationships[][] = {{1, 2, 4}, {1, 1, 3}, {2, 4, 1}};
        assertEquals(5, sampleObject.calculateDuration(assignmentArray, relationships));
    }

}