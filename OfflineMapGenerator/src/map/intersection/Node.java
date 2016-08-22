package map.intersection;

/**
 * Created by Ethan on 8/20/2016.
 */
class Node<T> {
    private Node<T> nextNode;
    private Node<T> linkedNode;

    private final T data;

    Node(T data) {
        this.data = data;
    }

    T getData() {
        return data;
    }

    void setNextNode(Node<T> n) {
        nextNode = n;
    }

    void setLinkedNode(Node<T> n) {
        linkedNode = n;
    }

    Node<T> getNextNode() {
        return nextNode;
    }

    Node<T> getLinkedNode() {
        return linkedNode;
    }

    boolean hasNextNode() {
        return nextNode != null;
    }

    boolean hasLinkNode() {
        return linkedNode != null;
    }

    public boolean equals(Object other) {
        if (other.getClass() == Node.class) {
            return data.equals(((Node)other).data);
        }
        return false;
    }

    boolean dataEquals(T other) {
        return data.equals(other);
    }
}
