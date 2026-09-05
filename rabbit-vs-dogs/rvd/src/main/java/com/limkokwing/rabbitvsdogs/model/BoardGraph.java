package com.limkokwing.rabbitvsdogs.model;

import java.util.*;

public final class BoardGraph
{
    public static final int NODE_COUNT = 11;

    // Adjacency: each node's neighbours
    public static final Map<Integer, List<Integer>> ADJACENCY = Map.ofEntries(
            Map.entry(0, List.of(1, 3, 4, 5, 7, 8, 9, 10)),
            Map.entry(1, List.of(0, 2, 3, 8)),
            Map.entry(2, List.of(1, 3, 9)),
            Map.entry(3, List.of(0, 1, 2, 4, 9)),
            Map.entry(4, List.of(0, 3, 5)),
            Map.entry(5, List.of(0, 4, 6, 10)),
            Map.entry(6, List.of(5, 7, 10)),
            Map.entry(7, List.of(0, 6, 8, 10)),
            Map.entry(8, List.of(0, 1, 7)),
            Map.entry(9, List.of(0, 2, 3)),
            Map.entry(10, List.of(0, 5, 6))
    );

    // will be scaled to fit the board pane
    public static final Map<Integer, double[]> POSITIONS = Map.ofEntries(
            Map.entry(2, new double[]{60, 300}),
            Map.entry(3, new double[]{180, 80}),
            Map.entry(1, new double[]{180, 520}),
            Map.entry(9, new double[]{180, 300}),
            Map.entry(0, new double[]{320, 300}),
            Map.entry(4, new double[]{320, 80}),
            Map.entry(8, new double[]{320, 520}),
            Map.entry(5, new double[]{460, 80}),
            Map.entry(6, new double[]{580, 300}),
            Map.entry(10, new double[]{460, 300}),
            Map.entry(7, new double[]{460, 520})
    );

    public static List<Integer> neighbours(int node)
    {
        List<Integer> result = ADJACENCY.get(node);
        if (result == null) throw new IllegalArgumentException("Invalid node: " + node);
        return result;
    }

    private BoardGraph() {}
}