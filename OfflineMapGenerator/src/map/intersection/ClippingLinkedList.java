package map.intersection;

import math.Vector2d;

import java.util.*;

/**
 * Created by Ethan on 8/20/2016.
 * Just a crappy linked list that keeps a clipping and subject list and iterates between them
 */
public class ClippingLinkedList<T> implements Iterable<T>{

    private class InternalLinkedList<T> {
        private Comparator<T> comparator;

        Node<T> startNode;
        Node<T> endNode;

        public void setComparator(Comparator<T> comparator) {
            this.comparator = comparator;
        }

        public void resortAll() {
            List<T> list= new LinkedList<>();

            Node<T> n = startNode;
            list.add(n.getData());

            while(n.hasNextNode()) {
                n = n.getNextNode();
                list.add(n.getData());
            }

            list.sort(comparator);

            remvoeAll();

            for (T o : list) {
                add(o);
            }

        }

        public void remvoeAll() {
            startNode = null;
            endNode = null;
        }

        public T getStart() {
            if (startNode != null) {
                return startNode.getData();
            }
            return null;
        }

        public T getEnd() {
            if (endNode != null) {
                return endNode.getData();
            }

            return null;
        }

        private int size() {
            if (startNode == null) {
                return 0;
            }
            Node n = startNode;
            int i = 0;
            while (n.hasNextNode()) {
                i++;
                n = n.getNextNode();
            }

            return i;
        }

        public void add(T object) {
            addNode(new Node<T>(object));
        }

        public void addNode(Node<T> node) {
            if (startNode == null) { //0 sized list
                startNode = node;
                endNode = startNode;
                return;
            }
            Node<T> nextNode = node;
            endNode.setNextNode(nextNode);
            endNode = nextNode;
        }

        public void addNodeSorted(Node<T> node) {
            if (comparator == null) {
                addNode(node);
                return;
            }
            if (startNode == null) {
                startNode = node;
                endNode = startNode;
                return;
            }
            if (comparator.compare(node.getData(), startNode.getData()) < 0) {
                node.setNextNode(startNode);
                startNode = node;
                return;
            }
            if (comparator.compare(node.getData(), startNode.getData()) > 0 && !startNode.hasNextNode()) {
                addNode(node);
            }

            Node<T> previous = startNode;
            Node<T> current = null;
            while(previous.hasNextNode()) {
                current = previous.getNextNode();

                int result = comparator.compare(node.getData(), current.getData());
                if (result < 0) {
                    previous.setNextNode(node);
                    node.setNextNode(previous);
                }
            }

            addNode(node);
        }

        public boolean remove(T object) {
            if (startNode == null) {
                return false;
            }

            if (startNode.dataEquals(object)) {
                startNode = startNode.getNextNode();
                return true;
            }
            Node<T> previous = startNode;
            Node<T> node = null;

            while(previous.hasNextNode()) {
                node = previous.getNextNode();
                if (node.dataEquals(object)) {
                    previous.setNextNode(node.getNextNode());
                    return true;
                }
                previous = node;
            }

            return false;
        }

        public boolean contains(T object) {
            if (startNode == null) {
                return false;
            }
            Node<T> node = startNode;
            while(node.hasNextNode()) {
                if (node.dataEquals(object)) {
                    return true;
                }
                node = node.getNextNode();
            }

            return false;
        }

    }
    public class DoubleIterator<T> implements Iterator<T> {
        private Node<T> currentNode;
        private final Node<T> startingNode;
        private int list = 0; //0 = main, 1 = secondary
        private Set<Node<T>> visitedLinks;
        private boolean switchOnNextLink;

        DoubleIterator(Node<T> startingNode) {
            this.currentNode = new Node<>(null);
            currentNode.setNextNode(startingNode);
            this.startingNode = currentNode;
            visitedLinks = new HashSet<>();
        }

        @Override
        public boolean hasNext() {

            if (currentNode.hasLinkNode() && !visitedLinks.contains(currentNode)) {
                return true;
            }
            return currentNode.hasNextNode();
        }

        public T peekNext() {
            if (currentNode.getNextNode() == null) {
                return startingNode.getNextNode().getData();
            }
            return currentNode.getNextNode().getData();
        }

        public void setSwitchOnNextLink(boolean v) {
            switchOnNextLink = v;
        }

        @Override
        public T next() {
            Node<T> nextNode = currentNode.getNextNode();

            if (switchOnNextLink && currentNode.hasLinkNode() && list == 0 && !visitedLinks.contains(currentNode)) {
                nextNode = currentNode.getLinkedNode().getNextNode();
                visitedLinks.add(currentNode);
                list = 1;
                switchOnNextLink = false;
            }
            if (currentNode.hasLinkNode() && list == 1) {
                nextNode = startingNode.getNextNode();
                list = 0;
            }
            if (visitedLinks.contains(nextNode)) {
                return nextNode.getNextNode().getData();
            }
            currentNode = nextNode;
            return currentNode.getData();
        }
    }
    private class SingleIterator<T> implements Iterator<T> {
        private Node<T> currentNode;

        SingleIterator(Node<T> startingNode) {
            this.currentNode = new Node<>(null);
            currentNode.setNextNode(startingNode);
        }
        @Override
        public boolean hasNext() {
            return currentNode.hasNextNode();
        }

        @Override
        public T next() {
            Node<T> nextNode = currentNode.getNextNode();
            currentNode = nextNode;
            return currentNode.getData();
        }
    }

    private InternalLinkedList<T> subjectList;
    private InternalLinkedList<T> clipList;

    public ClippingLinkedList() {
        subjectList = new InternalLinkedList<>();
        clipList = new InternalLinkedList<>();
    }

    @Override
    public Iterator<T> iterator() {
        return new DoubleIterator<>(subjectList.startNode);
    }

    public Iterator<T> getSubjectOnlyIterator() {
        return new SingleIterator<T>(subjectList.startNode);
    }

    public Iterator<T> getClipOnlyIterator() {
        return new SingleIterator<>(clipList.startNode);
    }

    public void addSubjectComparator(Comparator<T> c) {
        subjectList.setComparator(c);
        subjectList.resortAll();
    }

    public void addClipComparator(Comparator<T> c) {
        clipList.setComparator(c);
        clipList.resortAll();
    }

    public void addToClip(T object) {
        clipList.add(object);
    }

    public void addToSubject(T object) {
        subjectList.add(object);
    }

    public void addToBoth(T object) {
        Node<T> subjectNode = new Node<>(object);
        Node<T> clipNode = new Node<>(object);

        subjectNode.setLinkedNode(clipNode);
        clipNode.setLinkedNode(subjectNode);

        clipList.addNode(clipNode);
        subjectList.addNode(subjectNode);
    }

    public void addToBothSorted(T object) {
        Node<T> subjectNode = new Node<>(object);
        Node<T> clipNode = new Node<>(object);

        subjectNode.setLinkedNode(clipNode);
        clipNode.setLinkedNode(subjectNode);

        clipList.addNodeSorted(clipNode);
        subjectList.addNodeSorted(subjectNode);
    }

    public T getSubjectStart() {
        return subjectList.getStart();
    }

    public T getSubjectEnd() {
        return subjectList.getEnd();
    }

    public T getClipStart() {
        return clipList.getStart();
    }

    public T getClipEnd() {
        return clipList.getEnd();
    }
}

