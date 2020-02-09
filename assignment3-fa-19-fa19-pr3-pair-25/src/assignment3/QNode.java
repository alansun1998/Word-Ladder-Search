package assignment3;

// Object that has parent/child relation included. Used in BFS searching to keep track of path taken.
public class QNode {
    private String word;
    private QNode parent;

    QNode(String word, QNode parent) {
        this.word = word;
        this.parent = parent;
    }

    QNode(String word) {
        this.word = word;
        this.parent = null;
    }

    public String getWord() {
        return word;
    }

    public QNode getParent() {
        return parent;
    }
}
