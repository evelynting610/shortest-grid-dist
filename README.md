# shortest-grid-dist

The object of this program was to find the shortest distance between two points that were given as input.  What is interesting about this program is that it takes big-O of nlogn time per query.  Therefore, if you have n queries, the algorithm will take n squared logn time.

This incredible running time is facilitated by the use of grid squares.  To get from a point in any one grid to a point in another grid, you must pass through grid corners.
