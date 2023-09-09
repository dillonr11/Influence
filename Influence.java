import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

/******************************************************************************
 *  Your Name: Dillon Remuck
 *
 *
 *  Compilation:  javac Influence.java
 *  Dependencies: In.java and Digraph.java, both provided in the same directory
 *  Execution:    java Influence digraph_filename [option]excluded_id1 excluded_id2
 *
 *  For example, the following command only specifies a diagraph filename and does
 *  not specify any excluded vertex. So all veritces should be included in the
 *  influence computation.
 *
 *  % java Influence digraph0.txt
 *
 *  The following command specify a diagraph filename and four excluded vertices,
 *  namely 1, 2, 3 and 8. These two vertices should not be included in the infuence
 *  computation.
 *  % java Influence digraph0.txt 1 2 3 8
 ******************************************************************************/

public class Influence {

    //digraph object to be analyzed for vertex influence
    private Digraph influenceGraph;

    /**
     * Creates an Influence object and initalizes its instance variable
     * as a deep copy of the given digraph argument
     *
     * @param G given digraph
     */
    public Influence(Digraph G) {
        influenceGraph = new Digraph(G);
    }


    /**
     * Computes and returns the influence count of the specified source vertex,
     * not including or going through any vertex in the excluded set.
     *
     * @param source   the id of the source vertex for the influence computation
     * @param excluded the HashSet of the ids of the vertices to be included,
     *                 which could be an empty or non-empty set
     * @param return   0 if source is in the excluded set;
     *                 otherwise
     *                 a positive int for the count of the vertex or vertices that
     *                 can be influenced, directly or indirectly, by the source
     *                 vertex, including itself
     */
    // Worst-case time complexity of your solution? O(E+V)
    public int getInfluenceCount(int source, HashSet<Integer> excluded) {
        if (excluded.contains(source)) {
            return 0;
        }
        int influenceCount = 1;
        boolean[] marked = new boolean[influenceGraph.V()];
        Queue<Integer> q = new LinkedList<Integer>();
        marked[source] = true;
        q.add(source);
        while (!q.isEmpty()) {
            int v = q.remove();
            for (int w : influenceGraph.adj(v)) {
                if (!marked[w] && !excluded.contains(w)) {
                    marked[w] = true;
                    q.add(w);
                    influenceCount++;
                }
            }
        }
        return influenceCount;
    }

    /**
     * Computes and returns the max influencer(s) in the influence graph
     *
     * @param excluded the HashSet of the ids of the vertices to be excluded,
     *                 which could be an empty or non-empty set
     * @param return   an Iteraable<Integer> of the id(s) of the
     *                 max influencer(s)
     */
    // Worst-case time complexity of your solution? O(E+V)
    public Iterable<Integer> getMaxInfluencers(HashSet<Integer> excluded) {
        HashSet<Integer> set = new HashSet<>();
        int maxInfluence = 0;
        for (int v = 0; v < influenceGraph.V(); v++) {
            int influence = getInfluenceCount(v, excluded);
            if (v == 0) {
                set.add(v);
                maxInfluence = influence;
            } else if (influence > maxInfluence) {
                set.clear();
                set.add(v);
                maxInfluence = influence;
            } else if (influence == maxInfluence) {
                set.add(v);
            }
        }
        return set;
    }

    /**
     * runs {@code Influence} methods and prints the results
     *
     * @param args the command-line arguments
     *             args[0]: required, digraph filename
     *             args[1] and 2: optional, int ids of the vertices to be excluded
     */
    public static void main(String[] args) {
        //parse command line arguments
        In in = new In(args[0]);
        Digraph graph = new Digraph(in);
        HashSet<Integer> excluded = new HashSet<Integer>();
        for (int i = 1; i < args.length; i++)
            excluded.add(Integer.parseInt(args[i]));

        //compute and print each node's influence count
        Influence influence = new Influence(graph);
        for (int i = 0; i < graph.V(); i++) {
            System.out.printf("vertex %d, influence count: %d\n", i,
                    influence.getInfluenceCount(i, excluded));
        }

        //compute and print the max infuencer(s)
        Iterable<Integer> maxInfluencers = influence.getMaxInfluencers(excluded);
        if (maxInfluencers != null) {
            System.out.println("Here are the max influencers:");
            for (int maxInf : maxInfluencers)
                System.out.print(maxInf + ", ");
            System.out.println();
        }
    }
}
