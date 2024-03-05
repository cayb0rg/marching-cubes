package com.procedural_generation;

import java.util.ArrayList;

import org.joml.Vector3f;

public class VoxelGrid {
    // pairs up an index from 0 to 7 to a point on the cube
    public static final int[][] POINTS = {
        {0, 0, 0},
        {1, 0, 0},
        {1, 1, 0},
        {0, 1, 0},
        {0, 0, 1},
        {1, 0, 1},
        {1, 1, 1},
        {0, 1, 1}
    };

    // pairs up an index from 0 to 11 to an edge on the cube
    public static final int[][] EDGES = {
        {0, 1},
        {1, 2},
        {2, 3},
        {3, 0},
        {4, 5},
        {5, 6},
        {6, 7},
        {7, 4},
        {0, 4},
        {1, 5},
        {2, 6},
        {3, 7}
    };

    public static final int[][] TRIANGULATIONS = {
        {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 0,  8,  3, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 0,  1,  9, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 1,  8,  3,  9,  8,  1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 1,  2,  10,-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 0,  8,  3,  1,  2,  10,-1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 9,  2,  10, 0,  2,  9, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 2,  8,  3,  2,  10, 8,  10, 9,  8, -1, -1, -1, -1, -1, -1 },
        { 3,  11, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 0,  11, 2,  8,  11, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 1,  9,  0,  2,  3,  11,-1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 1,  11, 2,  1,  9,  11, 9,  8,  11,-1, -1, -1, -1, -1, -1 },
        { 3,  10, 1,  11, 10, 3, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 0,  10, 1,  0,  8,  10, 8,  11, 10,-1, -1, -1, -1, -1, -1 },
        { 3,  9,  0,  3,  11, 9,  11, 10, 9, -1, -1, -1, -1, -1, -1 },
        { 9,  8,  10, 10, 8,  11,-1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 4,  7,  8, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 4,  3,  0,  7,  3,  4, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 0,  1,  9,  8,  4,  7, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 4,  1,  9,  4,  7,  1,  7,  3,  1, -1, -1, -1, -1, -1, -1 },
        { 1,  2,  10, 8,  4,  7, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 3,  4,  7,  3,  0,  4,  1,  2,  10,-1, -1, -1, -1, -1, -1 },
        { 9,  2,  10, 9,  0,  2,  8,  4,  7, -1, -1, -1, -1, -1, -1 },
        { 2,  10, 9,  2,  9,  7,  2,  7,  3,  7,  9,  4, -1, -1, -1 },
        { 8,  4,  7,  3,  11, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 11, 4,  7,  11, 2,  4,  2,  0,  4, -1, -1, -1, -1, -1, -1 },
        { 9,  0,  1,  8,  4,  7,  2,  3,  11,-1, -1, -1, -1, -1, -1 },
        { 4,  7,  11, 9,  4,  11, 9,  11, 2,  9,  2,  1, -1, -1, -1 },
        { 3,  10, 1,  3,  11, 10, 7,  8,  4, -1, -1, -1, -1, -1, -1 },
        { 1,  11, 10, 1,  4,  11, 1,  0,  4,  7,  11, 4, -1, -1, -1 },
        { 4,  7,  8,  9,  0,  11, 9,  11, 10, 11, 0,  3, -1, -1, -1 },
        { 4,  7,  11, 4,  11, 9,  9,  11, 10,-1, -1, -1, -1, -1, -1 },
        { 9,  5,  4, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 9,  5,  4,  0,  8,  3, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 0,  5,  4,  1,  5,  0, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 8,  5,  4,  8,  3,  5,  3,  1,  5, -1, -1, -1, -1, -1, -1 },
        { 1,  2,  10, 9,  5,  4, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 3,  0,  8,  1,  2,  10, 4,  9,  5, -1, -1, -1, -1, -1, -1 },
        { 5,  2,  10, 5,  4,  2,  4,  0,  2, -1, -1, -1, -1, -1, -1 },
        { 2,  10, 5,  3,  2,  5,  3,  5,  4,  3,  4,  8, -1, -1, -1 },
        { 9,  5,  4,  2,  3,  11,-1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 0,  11, 2,  0,  8,  11, 4,  9,  5, -1, -1, -1, -1, -1, -1 },
        { 0,  5,  4,  0,  1,  5,  2,  3,  11,-1, -1, -1, -1, -1, -1 },
        { 2,  1,  5,  2,  5,  8,  2,  8,  11, 4,  8,  5, -1, -1, -1 },
        { 10, 3,  11, 10, 1,  3,  9,  5,  4, -1, -1, -1, -1, -1, -1 },
        { 4,  9,  5,  0,  8,  1,  8,  10, 1,  8,  11, 10,-1, -1, -1 },
        { 5,  4,  0,  5,  0,  11, 5,  11, 10, 11, 0,  3, -1, -1, -1 },
        { 5,  4,  8,  5,  8,  10, 10, 8,  11,-1, -1, -1, -1, -1, -1 },
        { 9,  7,  8,  5,  7,  9, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 9,  3,  0,  9,  5,  3,  5,  7,  3, -1, -1, -1, -1, -1, -1 },
        { 0,  7,  8,  0,  1,  7,  1,  5,  7, -1, -1, -1, -1, -1, -1 },
        { 1,  5,  3,  3,  5,  7, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 9,  7,  8,  9,  5,  7,  10, 1,  2, -1, -1, -1, -1, -1, -1 },
        { 10, 1,  2,  9,  5,  0,  5,  3,  0,  5,  7,  3, -1, -1, -1 },
        { 8,  0,  2,  8,  2,  5,  8,  5,  7,  10, 5,  2, -1, -1, -1 },
        { 2,  10, 5,  2,  5,  3,  3,  5,  7, -1, -1, -1, -1, -1, -1 },
        { 7,  9,  5,  7,  8,  9,  3,  11, 2, -1, -1, -1, -1, -1, -1 },
        { 9,  5,  7,  9,  7,  2,  9,  2,  0,  2,  7,  11,-1, -1, -1 },
        { 2,  3,  11, 0,  1,  8,  1,  7,  8,  1,  5,  7, -1, -1, -1 },
        { 11, 2,  1,  11, 1,  7,  7,  1,  5, -1, -1, -1, -1, -1, -1 },
        { 9,  5,  8,  8,  5,  7,  10, 1,  3,  10, 3,  11,-1, -1, -1 },
        { 5,  7,  0,  5,  0,  9,  7,  11, 0,  1,  0,  10, 11, 10, 0 },
        { 11, 10, 0,  11, 0,  3,  10, 5,  0,  8,  0,  7,  5,  7,  0 },
        { 11, 10, 5,  7,  11, 5, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 10, 6,  5, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 0,  8,  3,  5,  10, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 9,  0,  1,  5,  10, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 1,  8,  3,  1,  9,  8,  5,  10, 6, -1, -1, -1, -1, -1, -1 },
        { 1,  6,  5,  2,  6,  1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 1,  6,  5,  1,  2,  6,  3,  0,  8, -1, -1, -1, -1, -1, -1 },
        { 9,  6,  5,  9,  0,  6,  0,  2,  6, -1, -1, -1, -1, -1, -1 },
        { 5,  9,  8,  5,  8,  2,  5,  2,  6,  3,  2,  8, -1, -1, -1 },
        { 2,  3,  11, 10, 6,  5, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 11, 0,  8,  11, 2,  0,  10, 6,  5, -1, -1, -1, -1, -1, -1 },
        { 0,  1,  9,  2,  3,  11, 5,  10, 6, -1, -1, -1, -1, -1, -1 },
        { 5,  10, 6,  1,  9,  2,  9,  11, 2,  9,  8,  11,-1, -1, -1 },
        { 6,  3,  11, 6,  5,  3,  5,  1,  3, -1, -1, -1, -1, -1, -1 },
        { 0,  8,  11, 0,  11, 5,  0,  5,  1,  5,  11, 6, -1, -1, -1 },
        { 3,  11, 6,  0,  3,  6,  0,  6,  5,  0,  5,  9, -1, -1, -1 },
        { 6,  5,  9,  6,  9,  11, 11, 9,  8, -1, -1, -1, -1, -1, -1 },
        { 5,  10, 6,  4,  7,  8, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 4,  3,  0,  4,  7,  3,  6,  5,  10,-1, -1, -1, -1, -1, -1 },
        { 1,  9,  0,  5,  10, 6,  8,  4,  7, -1, -1, -1, -1, -1, -1 },
        { 10, 6,  5,  1,  9,  7,  1,  7,  3,  7,  9,  4, -1, -1, -1 },
        { 6,  1,  2,  6,  5,  1,  4,  7,  8, -1, -1, -1, -1, -1, -1 },
        { 1,  2,  5,  5,  2,  6,  3,  0,  4,  3,  4,  7, -1, -1, -1 },
        { 8,  4,  7,  9,  0,  5,  0,  6,  5,  0,  2,  6, -1, -1, -1 },
        { 7,  3,  9,  7,  9,  4,  3,  2,  9,  5,  9,  6,  2,  6,  9 },
        { 3,  11, 2,  7,  8,  4,  10, 6,  5, -1, -1, -1, -1, -1, -1 },
        { 5,  10, 6,  4,  7,  2,  4,  2,  0,  2,  7,  11,-1, -1, -1 },
        { 0,  1,  9,  4,  7,  8,  2,  3,  11, 5,  10, 6, -1, -1, -1 },
        { 9,  2,  1,  9,  11, 2,  9,  4,  11, 7,  11, 4,  5,  10, 6 },
        { 8,  4,  7,  3,  11, 5,  3,  5,  1,  5,  11, 6, -1, -1, -1 },
        { 5,  1,  11, 5,  11, 6,  1,  0,  11, 7,  11, 4,  0,  4,  11},
        { 0,  5,  9,  0,  6,  5,  0,  3,  6,  11, 6,  3,  8,  4,  7 },
        { 6,  5,  9,  6,  9,  11, 4,  7,  9,  7,  11, 9, -1, -1, -1 },
        { 10, 4,  9,  6,  4,  10,-1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 4,  10, 6,  4,  9,  10, 0,  8,  3, -1, -1, -1, -1, -1, -1 },
        { 10, 0,  1,  10, 6,  0,  6,  4,  0, -1, -1, -1, -1, -1, -1 },
        { 8,  3,  1,  8,  1,  6,  8,  6,  4,  6,  1,  10,-1, -1, -1 },
        { 1,  4,  9,  1,  2,  4,  2,  6,  4, -1, -1, -1, -1, -1, -1 },
        { 3,  0,  8,  1,  2,  9,  2,  4,  9,  2,  6,  4, -1, -1, -1 },
        { 0,  2,  4,  4,  2,  6, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 8,  3,  2,  8,  2,  4,  4,  2,  6, -1, -1, -1, -1, -1, -1 },
        { 10, 4,  9,  10, 6,  4,  11, 2,  3, -1, -1, -1, -1, -1, -1 },
        { 0,  8,  2,  2,  8,  11, 4,  9,  10, 4,  10, 6, -1, -1, -1 },
        { 3,  11, 2,  0,  1,  6,  0,  6,  4,  6,  1,  10,-1, -1, -1 },
        { 6,  4,  1,  6,  1,  10, 4,  8,  1,  2,  1,  11, 8,  11, 1 },
        { 9,  6,  4,  9,  3,  6,  9,  1,  3,  11, 6,  3, -1, -1, -1 },
        { 8,  11, 1,  8,  1,  0,  11, 6,  1,  9,  1,  4,  6,  4,  1 },
        { 3,  11, 6,  3,  6,  0,  0,  6,  4, -1, -1, -1, -1, -1, -1 },
        { 6,  4,  8,  11, 6,  8, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 7,  10, 6,  7,  8,  10, 8,  9,  10,-1, -1, -1, -1, -1, -1 },
        { 0,  7,  3,  0,  10, 7,  0,  9,  10, 6,  7,  10,-1, -1, -1 },
        { 10, 6,  7,  1,  10, 7,  1,  7,  8,  1,  8,  0, -1, -1, -1 },
        { 10, 6,  7,  10, 7,  1,  1,  7,  3, -1, -1, -1, -1, -1, -1 },
        { 1,  2,  6,  1,  6,  8,  1,  8,  9,  8,  6,  7, -1, -1, -1 },
        { 2,  6,  9,  2,  9,  1,  6,  7,  9,  0,  9,  3,  7,  3,  9 },
        { 7,  8,  0,  7,  0,  6,  6,  0,  2, -1, -1, -1, -1, -1, -1 },
        { 7,  3,  2,  6,  7,  2, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 2,  3,  11, 10, 6,  8,  10, 8,  9,  8,  6,  7, -1, -1, -1 },
        { 2,  0,  7,  2,  7,  11, 0,  9,  7,  6,  7,  10, 9,  10, 7 },
        { 1,  8,  0,  1,  7,  8,  1,  10, 7,  6,  7,  10, 2,  3,  11},
        { 11, 2,  1,  11, 1,  7,  10, 6,  1,  6,  7,  1, -1, -1, -1 },
        { 8,  9,  6,  8,  6,  7,  9,  1,  6,  11, 6,  3,  1,  3,  6 },
        { 0,  9,  1,  11, 6,  7, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 7,  8,  0,  7,  0,  6,  3,  11, 0,  11, 6,  0, -1, -1, -1 },
        { 7,  11, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 7,  6,  11,-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 3,  0,  8,  11, 7,  6, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 0,  1,  9,  11, 7,  6, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 8,  1,  9,  8,  3,  1,  11, 7,  6, -1, -1, -1, -1, -1, -1 },
        { 10, 1,  2,  6,  11, 7, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 1,  2,  10, 3,  0,  8,  6,  11, 7, -1, -1, -1, -1, -1, -1 },
        { 2,  9,  0,  2,  10, 9,  6,  11, 7, -1, -1, -1, -1, -1, -1 },
        { 6,  11, 7,  2,  10, 3,  10, 8,  3,  10, 9,  8, -1, -1, -1 },
        { 7,  2,  3,  6,  2,  7, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 7,  0,  8,  7,  6,  0,  6,  2,  0, -1, -1, -1, -1, -1, -1 },
        { 2,  7,  6,  2,  3,  7,  0,  1,  9, -1, -1, -1, -1, -1, -1 },
        { 1,  6,  2,  1,  8,  6,  1,  9,  8,  8,  7,  6, -1, -1, -1 },
        { 10, 7,  6,  10, 1,  7,  1,  3,  7, -1, -1, -1, -1, -1, -1 },
        { 10, 7,  6,  1,  7,  10, 1,  8,  7,  1,  0,  8, -1, -1, -1 },
        { 0,  3,  7,  0,  7,  10, 0,  10, 9,  6,  10, 7, -1, -1, -1 },
        { 7,  6,  10, 7,  10, 8,  8,  10, 9, -1, -1, -1, -1, -1, -1 },
        { 6,  8,  4,  11, 8,  6, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 3,  6,  11, 3,  0,  6,  0,  4,  6, -1, -1, -1, -1, -1, -1 },
        { 8,  6,  11, 8,  4,  6,  9,  0,  1, -1, -1, -1, -1, -1, -1 },
        { 9,  4,  6,  9,  6,  3,  9,  3,  1,  11, 3,  6, -1, -1, -1 },
        { 6,  8,  4,  6,  11, 8,  2,  10, 1, -1, -1, -1, -1, -1, -1 },
        { 1,  2,  10, 3,  0,  11, 0,  6,  11, 0,  4,  6, -1, -1, -1 },
        { 4,  11, 8,  4,  6,  11, 0,  2,  9,  2,  10, 9, -1, -1, -1 },
        { 10, 9,  3,  10, 3,  2,  9,  4,  3,  11, 3,  6,  4,  6,  3 },
        { 8,  2,  3,  8,  4,  2,  4,  6,  2, -1, -1, -1, -1, -1, -1 },
        { 0,  4,  2,  4,  6,  2, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 1,  9,  0,  2,  3,  4,  2,  4,  6,  4,  3,  8, -1, -1, -1 },
        { 1,  9,  4,  1,  4,  2,  2,  4,  6, -1, -1, -1, -1, -1, -1 },
        { 8,  1,  3,  8,  6,  1,  8,  4,  6,  6,  10, 1, -1, -1, -1 },
        { 10, 1,  0,  10, 0,  6,  6,  0,  4, -1, -1, -1, -1, -1, -1 },
        { 4,  6,  3,  4,  3,  8,  6,  10, 3,  0,  3,  9,  10, 9,  3 },
        { 10, 9,  4,  6,  10, 4, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 4,  9,  5,  7,  6,  11,-1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 0,  8,  3,  4,  9,  5,  11, 7,  6, -1, -1, -1, -1, -1, -1 },
        { 5,  0,  1,  5,  4,  0,  7,  6,  11,-1, -1, -1, -1, -1, -1 },
        { 11, 7,  6,  8,  3,  4,  3,  5,  4,  3,  1,  5, -1, -1, -1 },
        { 9,  5,  4,  10, 1,  2,  7,  6,  11,-1, -1, -1, -1, -1, -1 },
        { 6,  11, 7,  1,  2,  10, 0,  8,  3,  4,  9,  5, -1, -1, -1 },
        { 7,  6,  11, 5,  4,  10, 4,  2,  10, 4,  0,  2, -1, -1, -1 },
        { 3,  4,  8,  3,  5,  4,  3,  2,  5,  10, 5,  2,  11, 7,  6 },
        { 7,  2,  3,  7,  6,  2,  5,  4,  9, -1, -1, -1, -1, -1, -1 },
        { 9,  5,  4,  0,  8,  6,  0,  6,  2,  6,  8,  7, -1, -1, -1 },
        { 3,  6,  2,  3,  7,  6,  1,  5,  0,  5,  4,  0, -1, -1, -1 },
        { 6,  2,  8,  6,  8,  7,  2,  1,  8,  4,  8,  5,  1,  5,  8 },
        { 9,  5,  4,  10, 1,  6,  1,  7,  6,  1,  3,  7, -1, -1, -1 },
        { 1,  6,  10, 1,  7,  6,  1,  0,  7,  8,  7,  0,  9,  5,  4 },
        { 4,  0,  10, 4,  10, 5,  0,  3,  10, 6,  10, 7,  3,  7,  10},
        { 7,  6,  10, 7,  10, 8,  5,  4,  10, 4,  8,  10,-1, -1, -1 },
        { 6,  9,  5,  6,  11, 9,  11, 8,  9, -1, -1, -1, -1, -1, -1 },
        { 3,  6,  11, 0,  6,  3,  0,  5,  6,  0,  9,  5, -1, -1, -1 },
        { 0,  11, 8,  0,  5,  11, 0,  1,  5,  5,  6,  11,-1, -1, -1 },
        { 6,  11, 3,  6,  3,  5,  5,  3,  1, -1, -1, -1, -1, -1, -1 },
        { 1,  2,  10, 9,  5,  11, 9,  11, 8,  11, 5,  6, -1, -1, -1 },
        { 0,  11, 3,  0,  6,  11, 0,  9,  6,  5,  6,  9,  1,  2,  10},
        { 11, 8,  5,  11, 5,  6,  8,  0,  5,  10, 5,  2,  0,  2,  5 },
        { 6,  11, 3,  6,  3,  5,  2,  10, 3,  10, 5,  3, -1, -1, -1 },
        { 5,  8,  9,  5,  2,  8,  5,  6,  2,  3,  8,  2, -1, -1, -1 },
        { 9,  5,  6,  9,  6,  0,  0,  6,  2, -1, -1, -1, -1, -1, -1 },
        { 1,  5,  8,  1,  8,  0,  5,  6,  8,  3,  8,  2,  6,  2,  8 },
        { 1,  5,  6,  2,  1,  6, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 1,  3,  6,  1,  6,  10, 3,  8,  6,  5,  6,  9,  8,  9,  6 },
        { 10, 1,  0,  10, 0,  6,  9,  5,  0,  5,  6,  0, -1, -1, -1 },
        { 0,  3,  8,  5,  6,  10,-1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 10, 5,  6, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 11, 5,  10, 7,  5,  11,-1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 11, 5,  10, 11, 7,  5,  8,  3,  0, -1, -1, -1, -1, -1, -1 },
        { 5,  11, 7,  5,  10, 11, 1,  9,  0, -1, -1, -1, -1, -1, -1 },
        { 10, 7,  5,  10, 11, 7,  9,  8,  1,  8,  3,  1, -1, -1, -1 },
        { 11, 1,  2,  11, 7,  1,  7,  5,  1, -1, -1, -1, -1, -1, -1 },
        { 0,  8,  3,  1,  2,  7,  1,  7,  5,  7,  2,  11,-1, -1, -1 },
        { 9,  7,  5,  9,  2,  7,  9,  0,  2,  2,  11, 7, -1, -1, -1 },
        { 7,  5,  2,  7,  2,  11, 5,  9,  2,  3,  2,  8,  9,  8,  2 },
        { 2,  5,  10, 2,  3,  5,  3,  7,  5, -1, -1, -1, -1, -1, -1 },
        { 8,  2,  0,  8,  5,  2,  8,  7,  5,  10, 2,  5, -1, -1, -1 },
        { 9,  0,  1,  5,  10, 3,  5,  3,  7,  3,  10, 2, -1, -1, -1 },
        { 9,  8,  2,  9,  2,  1,  8,  7,  2,  10, 2,  5,  7,  5,  2 },
        { 1,  3,  5,  3,  7,  5, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 0,  8,  7,  0,  7,  1,  1,  7,  5, -1, -1, -1, -1, -1, -1 },
        { 9,  0,  3,  9,  3,  5,  5,  3,  7, -1, -1, -1, -1, -1, -1 },
        { 9,  8,  7,  5,  9,  7, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 5,  8,  4,  5,  10, 8,  10, 11, 8, -1, -1, -1, -1, -1, -1 },
        { 5,  0,  4,  5,  11, 0,  5,  10, 11, 11, 3,  0, -1, -1, -1 },
        { 0,  1,  9,  8,  4,  10, 8,  10, 11, 10, 4,  5, -1, -1, -1 },
        { 10, 11, 4,  10, 4,  5,  11, 3,  4,  9,  4,  1,  3,  1,  4 },
        { 2,  5,  1,  2,  8,  5,  2,  11, 8,  4,  5,  8, -1, -1, -1 },
        { 0,  4,  11, 0,  11, 3,  4,  5,  11, 2,  11, 1,  5,  1,  11},
        { 0,  2,  5,  0,  5,  9,  2,  11, 5,  4,  5,  8,  11, 8,  5 },
        { 9,  4,  5,  2,  11, 3, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 2,  5,  10, 3,  5,  2,  3,  4,  5,  3,  8,  4, -1, -1, -1 },
        { 5,  10, 2,  5,  2,  4,  4,  2,  0, -1, -1, -1, -1, -1, -1 },
        { 3,  10, 2,  3,  5,  10, 3,  8,  5,  4,  5,  8,  0,  1,  9 },
        { 5,  10, 2,  5,  2,  4,  1,  9,  2,  9,  4,  2, -1, -1, -1 },
        { 8,  4,  5,  8,  5,  3,  3,  5,  1, -1, -1, -1, -1, -1, -1 },
        { 0,  4,  5,  1,  0,  5, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 8,  4,  5,  8,  5,  3,  9,  0,  5,  0,  3,  5, -1, -1, -1 },
        { 9,  4,  5, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 4,  11, 7,  4,  9,  11, 9,  10, 11,-1, -1, -1, -1, -1, -1 },
        { 0,  8,  3,  4,  9,  7,  9,  11, 7,  9,  10, 11,-1, -1, -1 },
        { 1,  10, 11, 1,  11, 4,  1,  4,  0,  7,  4,  11,-1, -1, -1 },
        { 3,  1,  4,  3,  4,  8,  1,  10, 4,  7,  4,  11, 10, 11, 4 },
        { 4,  11, 7,  9,  11, 4,  9,  2,  11, 9,  1,  2, -1, -1, -1 },
        { 9,  7,  4,  9,  11, 7,  9,  1,  11, 2,  11, 1,  0,  8,  3 },
        { 11, 7,  4,  11, 4,  2,  2,  4,  0, -1, -1, -1, -1, -1, -1 },
        { 11, 7,  4,  11, 4,  2,  8,  3,  4,  3,  2,  4, -1, -1, -1 },
        { 2,  9,  10, 2,  7,  9,  2,  3,  7,  7,  4,  9, -1, -1, -1 },
        { 9,  10, 7,  9,  7,  4,  10, 2,  7,  8,  7,  0,  2,  0,  7 },
        { 3,  7,  10, 3,  10, 2,  7,  4,  10, 1,  10, 0,  4,  0,  10},
        { 1,  10, 2,  8,  7,  4, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 4,  9,  1,  4,  1,  7,  7,  1,  3, -1, -1, -1, -1, -1, -1 },
        { 4,  9,  1,  4,  1,  7,  0,  8,  1,  8,  7,  1, -1, -1, -1 },
        { 4,  0,  3,  7,  4,  3, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 4,  8,  7, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 9,  10, 8,  10, 11, 8, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 3,  0,  9,  3,  9,  11, 11, 9,  10,-1, -1, -1, -1, -1, -1 },
        { 0,  1,  10, 0,  10, 8,  8,  10, 11,-1, -1, -1, -1, -1, -1 },
        { 3,  1,  10, 11, 3,  10,-1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 1,  2,  11, 1,  11, 9,  9,  11, 8, -1, -1, -1, -1, -1, -1 },
        { 3,  0,  9,  3,  9,  11, 1,  2,  9,  2,  11, 9, -1, -1, -1 },
        { 0,  2,  11, 8,  0,  11,-1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 3,  2,  11,-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 2,  3,  8,  2,  8,  10, 10, 8,  9, -1, -1, -1, -1, -1, -1 },
        { 9,  10, 2,  0,  9,  2, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 2,  3,  8,  2,  8,  10, 0,  1,  8,  1,  10, 8, -1, -1, -1 },
        { 1,  10, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 1,  3,  8,  9,  1,  8, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 0,  9,  1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        { 0,  3,  8, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
        {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 },
    };

    public ArrayList<Float> voxel_grid; // 1-dimensional representation of a 3-dimensional grid

    public int resolution;

    public VoxelGrid(int resolution) {
        voxel_grid = new ArrayList<Float>(resolution * resolution * resolution);

        this.resolution = resolution;

        int i = 0;
        for (int z = 0; z < resolution; z++) {
            for (int y = 0; y < resolution; y++) {
                for (int x = 0; x < resolution; x++) {
                    voxel_grid.set(i, scalar_field((float)x, (float)y, (float)z));
                    i++;
                }
            }
        }
    }

    public float read(int x, int y, int z) {
        return voxel_grid.get(x + y * resolution + z * resolution * resolution);
    }

    public void write(int x, int y, int z, float value) {
        voxel_grid.set(x + y * resolution + z * resolution * resolution, value);
    }

    public void push(float value) {
        voxel_grid.add(value);
    }

    public static float scalar_field(float x, float y, float z) {
        return (x * x + y * y + z * z) - 0.75f*0.75f;
    }

    public void create_grid() {
        for (int z = 0; z < this.resolution; z++) {
            for (int y = 0; y < this.resolution; y++) {
                for (int x = 0; x < this.resolution; x++) {
                    ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
                    march_cube(x, y, z,
                        this,
                        positions
                    );
                }
            }
        }
    }

    public static void march_cube(int x, int y, int z, VoxelGrid voxel_grid, ArrayList<Vector3f> positions) {
        int[] triangulation = get_triangulation(x, y, z, voxel_grid);

        for (int i = 0; i < triangulation.length; i++) {
            if (i < 0) {
                break;
            }

            int[] point_indices = EDGES[i];
            int[] point_1 = POINTS[point_indices[0]];
            int[] point_2 = POINTS[point_indices[1]];

            // positions for the two corners
            Vector3f position_1 = new Vector3f(x + point_1[0], y + point_1[1], z + point_1[2]);
            Vector3f position_2 = new Vector3f(x + point_2[0], y + point_2[1], z + point_2[2]);

            // take the average between two corners
            Vector3f position = (position_1.add(position_2)).mul(0.5f);

            // add position to the list of vertex positions to render
            positions.add(position);
        }
    }

    public static int[] get_triangulation(int x, int y, int z, VoxelGrid voxel_grid) {
        int cube_index = 0b00000000;
        // each bit represents whether that corner should be inside or outside the mesh
        cube_index |= (voxel_grid.read(x,       y,      z       ) < 0 ? 1 : 0) << 0;
        cube_index |= (voxel_grid.read(x,       y,      z + 1   ) < 0 ? 1 : 0) << 1;
        cube_index |= (voxel_grid.read(x + 1,   y,      z + 1   ) < 0 ? 1 : 0) << 2;
        cube_index |= (voxel_grid.read(x + 1,   y,      z       ) < 0 ? 1 : 0) << 3;
        cube_index |= (voxel_grid.read(x,       y + 1,  z       ) < 0 ? 1 : 0) << 4;
        cube_index |= (voxel_grid.read(x,       y + 1,  z + 1   ) < 0 ? 1 : 0) << 5;
        cube_index |= (voxel_grid.read(x + 1,   y + 1,  z + 1   ) < 0 ? 1 : 0) << 6;
        cube_index |= (voxel_grid.read(x + 1,   y + 1,  z       ) < 0 ? 1 : 0) << 7;

        return TRIANGULATIONS[cube_index];
    }
}
