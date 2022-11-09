import java.util.ArrayList;
import java.util.Stack;

public class GUIState {

    private ArrayList<Feature> leftFeats;
    private ArrayList<Feature> rightFeats;
    private Stack<Feature> leftStack;
    private Stack<Feature> rightStack;


    public GUIState() {
        leftFeats = new ArrayList<Feature>();
        rightFeats = new ArrayList<Feature>();
        leftStack = new Stack<Feature>();
        rightStack = new Stack<Feature>();
    }

    public GUIState(ArrayList<Feature> leftFeats, ArrayList<Feature> rightFeats, Stack<Feature> leftStack, Stack<Feature> rightStack) {
        this.leftFeats = leftFeats;
        this.rightFeats = rightFeats;
        this.leftStack = leftStack;
        this.rightStack = rightStack;
    }

    public ArrayList<Feature> getLeftFeats() {
        return leftFeats;
    }

    public void setLeftFeats(ArrayList<Feature> leftFeats) {
        this.leftFeats = leftFeats;
    }

    public ArrayList<Feature> getRightFeats() {
        return rightFeats;
    }

    public void setRightFeats(ArrayList<Feature> rightFeats) {
        this.rightFeats = rightFeats;
    }

    public Stack<Feature> getLeftStack() {
        return leftStack;
    }

    public void setLeftStack(Stack<Feature> leftStack) {
        this.leftStack = leftStack;
    }

    public Stack<Feature> getRightStack() {
        return rightStack;
    }

    public void setRightStack(Stack<Feature> rightStack) {
        this.rightStack = rightStack;
    }


    @Override
    public String toString() {
        return "GUIState{" +
                "leftFeats=" + leftFeats +
                ", rightFeats=" + rightFeats +
                ", leftStack=" + leftStack +
                ", rightStack=" + rightStack +
                '}';
    }
}
