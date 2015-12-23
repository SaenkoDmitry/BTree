import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by Dmitry on 15.11.2015.
 */
public class Node {
    static int t = 2;
    int keys[];
    Field field[];
    Node nodes[];
    int n = 0;
    boolean isleaf;
    Queue < Node > treeWidth = new LinkedList<>();

    public Node()
    {
        keys = new int[2 * t];
        field = new Field[2 * t];
        nodes = new Node[2 * t + 1];
        n = 0;
        isleaf = true;
    }

    public Node(int x, Field f)
    {
        keys = new int[2 * t];
        field = new Field[2 * t];
        nodes = new Node[2 * t + 1];
        n = 1;
        isleaf = true;
        keys[0] = x;
        field[0] = f;
    }

    public Node insert(int x, Field f)
    {
        Node root = this;
        if(root.n != (2 * t - 1))
        {
            root = root.insertNonFull(x, f);
        }
        else
        {
            Node s = new Node();
            s.isleaf = false;
            s.n = 0;
            Node temp = root;
            root = s;
            root.nodes[0] = temp;
            root = root.splitNode(0);
            root = root.insertNonFull(x, f);
        }
        return root;
    }

    private Node insertNonFull(int x, Field f)
    {
        Node r = this;
        int i = r.n - 1;
        if(r.isleaf)
        {
            while((i >= 0) && (x < r.keys[i]))
            {
                r.keys[i + 1] = r.keys[i];
                r.field[i + 1] = r.field[i];
                i--;
            }
            r.keys[i + 1] = x;
            r.field[i + 1] = f;
            r.n++;
        }
        else
        {
            while((i >= 0) && (x < r.keys[i]))
            {
                i--;
            }
            i++;
            if(r.nodes[i].n == (2 * t - 1))
            {
                r = r.splitNode(i);
                if(x > r.keys[i])
                {
                    i++;
                }
            }
            r.nodes[i] = r.nodes[i].insertNonFull(x, f);
        }
        return r;
    }
    private Node splitNode(int child)
    {
        Node r = this;
        Node temp = new Node();
        Node tempChild = r.nodes[child];
        temp.isleaf = tempChild.isleaf;
        temp.n = t - 1;
        for (int i = 0; i <= (t - 1); i++) {
            temp.keys[i] = tempChild.keys[i + t];
            temp.field[i] = tempChild.field[i + t];
        }
        if(!tempChild.isleaf)
            for (int i = 0; i <= t; i++) {
                temp.nodes[i] = tempChild.nodes[i + t];
            }
        tempChild.n = t - 1;
        for(int i = r.n; i >= child + 1; i--)
        {
            r.nodes[i + 1] = r.nodes[i];//?
        }
        r.nodes[child + 1] = temp;
        for(int i = r.n - 1; i >= child; i--)
        {
            r.keys[i + 1] = r.keys[i];
            r.field[i + 1] = r.field[i];
        }
        r.keys[child] = tempChild.keys[t - 1];
        r.field[child] = tempChild.field[t - 1];
        r.n++;
        return r;
    }

    public void print_klp()
    {
        Node root = this;
        if(root != null)
        {
            int i = 0;
            String temp = "";
            while(i <= root.n - 1)
            {
                if(temp != "")
                {
                    temp = temp + "," + root.keys[i];
                }
                else
                {
                    temp = temp + root.keys[i];
                }
                i++;
            }
            temp = "\n(" + temp + ")";// + "{ " + "n = " + root.n + ", isleaf = " + root.isleaf + " }";
            System.out.print(temp);
            int j = 0;
            if(root.nodes[j] != null)
            {
                System.out.print("\n&");
                while(j <= root.n)
                {
                    root.nodes[j].print_klp();
                    j++;
                }
                System.out.print("\n%");
            }
        }
    }

    public void print_by_width()
    {
        Node root = this;
        if(root != null)
        {
            int prev = 1, cur = 0;
            treeWidth.add(root);
            while(!treeWidth.isEmpty())
            {
                int i = 0, j = 0;
                if(treeWidth.element().nodes[j] != null)
                {
                    while(j <= treeWidth.element().n)
                    {
                        root = treeWidth.element().nodes[j];
                        treeWidth.add(root);
                        j++;
                    }
                }
                String temp = "";
                while(i <= treeWidth.element().n - 1)
                {
                    if(temp != "")
                        temp = temp + "," + treeWidth.element().keys[i] + "(" + treeWidth.element().field[i].name + ")";
                    else
                        temp = temp + treeWidth.element().keys[i] + "(" + treeWidth.element().field[i].name + ")";
                    i++;
                }
                temp = "\n(" + temp + ")";// + "{ " + "n = " + treeWidth.element().n + ", isleaf = " + treeWidth.element().isleaf + " }";
                System.out.print(temp);
                prev--;
                cur += j;
                if(prev == 0)
                {
                    prev = cur;
                    cur = 0;
                    System.out.println();
                }
                treeWidth.remove();
            }
        }
    }
    public Field searchNode (Node root, int key)
    {
        int i = 0;
        if(root != null) {
            while (i <= root.n - 1 && root.keys[i] < key) {
                i = i + 1;
            }
            if (i <= root.n - 1 && root.keys[i] == key) {
                return root.field[i];
            }
            if (root.isleaf) {
                return null;
            } else {
                return searchNode(root.nodes[i], key);
            }
        }
        else
            return null;
    }

    private int findKey(int x) {
        int index = 0;
        while(index < n && keys[index] < x)
            ++index;
        return index;
    }
    public void remove(int x) {
        Node r = this;
        int index = findKey(x);
        if(index < r.n && keys[index] == x) {
            if(r.isleaf)
                removeFromLeaf(index);
            else
                removeFromNonLeaf(index);
        } else {
            if(r.isleaf) {
                System.out.println("No such key in the tree.");
                return;
            }
            boolean flag = ((index == n)? true : false);
            if(r.nodes[index].n < t)
                fill(index);
            if(flag && index > n)
                r.nodes[index-1].remove(x);
            else
                r.nodes[index].remove(x);
        }
    }
    private void removeFromLeaf(int index) {
        for(int i = index+1;i < n;i++) {
            keys[i-1] = keys[i];
            field[i - 1] = field[i];
        }
        n--;
    }
    private void removeFromNonLeaf(int index) {
        int k = this.keys[index];
        if(nodes[index].n >= t) {
            int prev = getPrev(index);
            keys[index] = prev;
            field[index] = getPrevF(index);
            nodes[index].remove(prev);
        } else if(nodes[index+1].n >= t) {
            int succ = getSucc(index);
            keys[index] = succ;
            field[index] = getSuccF(index);
            nodes[index+1].remove(succ);
        } else {
            merge(index);
            nodes[index].remove(k);
        }
    }
    private int getPrev(int index) {
        Node current = nodes[index];
        while(!current.isleaf) {
            current = current.nodes[current.n];
        }
        return current.keys[current.n-1];
    }
    private Field getPrevF(int index) {
        Node current = nodes[index];
        while(!current.isleaf) {
            current = current.nodes[current.n];
        }
        return current.field[current.n-1];
    }
    private int getSucc(int index) {
        Node current = nodes[index+1];
        while(!current.isleaf) {
            current = current.nodes[0];
        }
        return current.keys[0];
    }
    private Field getSuccF(int index) {
        Node current = nodes[index+1];
        while(!current.isleaf) {
            current = current.nodes[0];
        }
        return current.field[0];
    }
    private void fill(int index) {
        if(index != 0 && nodes[index-1].n >= t)
            borrowPrev(index);
        else if (index != n && nodes[index+1].n >= t)
            borrowNext(index);
        else {
            if(index != n)
                merge(index);
            else
                merge(index-1);
        }
    }
    private void borrowPrev(int index) {
        Node child = nodes[index];
        Node sibling = nodes[index-1];
        for(int i = child.n-1;i >= 0;i--) {
            child.keys[i + 1] = child.keys[i];
            child.field[i+1] = child.field[i];
        }
        if(!child.isleaf) {
            for(int i = child.n;i >= 0;i--)
                child.nodes[i+1] = child.nodes[i];
        }
        child.keys[0] = keys[index-1];
        child.field[0] = field[index-1];
        if(!isleaf)
            child.nodes[0] = sibling.nodes[sibling.n];
        keys[index-1] = sibling.keys[sibling.n-1];
        field[index-1] = sibling.field[sibling.n-1];
        child.n += 1;
        sibling.n -= 1;
    }
    private void borrowNext(int index) {
        Node child = nodes[index];
        Node sibling = nodes[index+1];
        child.keys[child.n] = keys[index];
        child.field[child.n] = field[index];
        if(!child.isleaf)
            child.nodes[child.n+1] = sibling.nodes[0];
        keys[index] = sibling.keys[0];
        field[index] = sibling.field[0];
        for(int i = 1;i < sibling.n;i++) {
            sibling.keys[i - 1] = sibling.keys[i];
            sibling.field[i-1] = sibling.field[i];
        }
        if(!sibling.isleaf) {
            for(int i = 0;i <= sibling.n;i++)
                sibling.nodes[i-1] = sibling.nodes[i];
        }
        child.n += 1;
        sibling.n -= 1;
    }
    private void merge(int index) {
        Node child = nodes[index];
        Node sibling = nodes[index+1];
        child.keys[t-1] = keys[index];
        child.field[t-1] = field[index];
        System.arraycopy(sibling.keys, 0, child.keys, t, sibling.n);
        System.arraycopy(sibling.field, 0, child.field, t, sibling.n);
        if(!child.isleaf) {
            for(int i = 0;i <= sibling.n;i++)
                child.nodes[i+t] = sibling.nodes[i];
        }
        for(int i = index+1;i < n;i++) {
            keys[i - 1] = keys[i];
            field[i - 1] = field[i];
        }
        for(int i = index+2;i <= n;i++)
            nodes[i-1] = nodes[i];
        child.n += sibling.n+1;
        n--;
    }

    public Node searchNodeR (Node root, int key)
    {
        int i = 0;
        if(root != null) {
            while (i <= root.n - 1 && root.keys[i] < key) {
                i = i + 1;
            }
            if (i <= root.n - 1 && root.keys[i] == key) {
                return root;
            }
            if (root.isleaf) {
                return null;
            } else {
                return searchNodeR(root.nodes[i], key);
            }
        }
        else
            return null;
    }
}
