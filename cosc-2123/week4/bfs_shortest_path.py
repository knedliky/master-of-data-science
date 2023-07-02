"""
 Compute the shortest path distance using breadth first search.
"""
prog_name = "BFSShortestPath"
disconnected_distance = -1
from queue import Queue
from graph import *


def spd(g, s, t):
    """
    Compute the shortest path distance between the source
    
    @param g Input Graph.
    @param s Source vertex.
    @param t Target vertex.
    
    @return Shortest path distance between s and t.
    If they are disconnected, then return disconnected_distance.
    """
    # IMPLEMENT ME!
    return disconnected_distance


def usage():
    """
    Print out usage message and exits from program.
    """
    print(f"{prog_name} <input graph file> <source vertex> <target vertex>")
    exit(1)


def main():
    """
    Compute shortest path distance using BFS for input graph.
    """
    import sys
    args = sys.argv[1:]
    if len(args) != 3:
        usage()

    try:
        # input graph
        with open(args[0]) as f:
            g = Graph(file=f)

        # source vertex
        source = int(args[1])

        # target vertex
        target = int(args[2])

        # compute the shortest path distance between source and target
        sp_distance = spd(g, source, target)

        # check if the two vertices are connected
        if sp_distance == disconnected_distance:
            print(f"Source {source} and Target {target} are disconnected.")
        else:
            print(f"Distance between Source {source} and Target {target} is {sp_distance}")
    except FileNotFoundError as e:
        print(e)


class Pair:
    """
    Class implement a (vertex, distance) pair.

    @author Jeffrey Chan
    """

    def __init__(self, v, dist):
        self.m_vertex = v
        self.m_distance = dist

    def get_vert(self):
        return self.m_vertex

    def get_dist(self):
        return self.m_distance


if __name__ == '__main__':
    main()
