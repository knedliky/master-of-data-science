from graph import *
from queue import Queue

prog_name = "BFS"


def traverse(g, s):
    """
    Breadth first search traversal of input graph g from seed vertex s.
    
    @param g Input graph.
    @param s Seed vertex to start traversal from.
    
    @returns the list of vertices, stored in the order they were visited in BFS traversal.
    """
    traversal_order = []
    # IMPLEMENT ME
    return traversal_order


def usage():
    """
    Print out usage message and exits from program.
    """
    print(f"{prog_name}: <input graph file> <starting vertex>")
    exit(1)


def main():
    """
    Perform BFS traversal for input graph.
    
    """
    import sys
    args = sys.argv[1:]
    if len(args) != 2:
        usage()

    try:
        # input graph
        with open(args[0]) as f:
            g = Graph(file=f)

        # seed vertex
        s = int(args[1])

        # perform BFS
        traversal_order = traverse(g, s)

        # print out the traversal order of the BFS
        print(traversal_order)
    except FileNotFoundError as e:
        print(e)


if __name__ == '__main__':
    main()
