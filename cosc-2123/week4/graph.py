from sortedcontainers import SortedList


class Graph:

    def __init__(self, vert_num=None, file=None):
        self.vert_num = 0
        self.edge_num = 0
        self.adj_list = []
        if vert_num:
            if vert_num < 0:
                raise ValueError('Number of vertices must be nonnegative')
            self.vert_num = vert_num
            self.generated_adj_list(vert_num)

        if file:
            values = file.readlines()
            self.vert_num = int(values[0])
            edges = values[1:]
            self.generated_adj_list(self.vert_num)
            for edge in edges:
                v, w = list(map(int, edge.split()))
                self.add_edge(v, w)

    def generated_adj_list(self, vert_num):
        for v in range(vert_num):
            self.adj_list.append(SortedList())

    def neighbours(self, v):
        """
        Returns the vertices adjacent to vertex <tt>v</tt>.
        
        @param  v the vertex
        @return the vertices adjacent to vertex <tt>v</tt>, as an iterable
        @throws IndexOutOfBoundsException unless 0 <= v < V
        """
        self.validate_vertex(v)
        return self.adj_list[v]

    def validate_vertex(self, v):
        if v < 0 or v >= self.vert_num:
            raise IndexError(f"vertex {v} is not between 0 and {self.vert_num - 1}")

    def add_edge(self, v, w):
        """
        Adds the undirected edge v-w to this graph.
        
        @param  v one vertex in the edge
        @param  w the other vertex in the edge
        @throws IndexOutOfBoundsException unless both 0 <= v < V and 0 <= w < V   
        """
        self.validate_vertex(v)
        self.validate_vertex(w)
        self.adj_list[v].add(w)
        self.adj_list[w].add(v)
        self.edge_num += 1

    def degree(self, v):
        """
        Returns the degree of vertex <tt>v</tt>.
        
        @param  v the vertex
        @return the degree of vertex <tt>v</tt>
        @throws IndexOutOfBoundsException unless 0 <= v < V  
        """
        self.validate_vertex(v)
        return len(self.adj_list[v])

    def __str__(self):
        s = [f"{self.vert_num} vertices, {self.edge_num} edges \n"]
        for v in range(self.vert_num):
            s.append(f"{v} : ")
            for w in self.adj_list[v]:
                s.append(f"{w} ")
            s.append("\n")

        return " ".join(s)


def main():
    # main and test
    vert_num = input("Enter the number of vertex: ")
    if vert_num.isnumeric():
        g = Graph(int(vert_num))

    next_value = ""
    while True:
        next_value = input('Enter one vertex in the edge v-w (q to quit): ')
        if next_value == 'q':
            break
        v = int(next_value)
        next_value = input('Enter another vertex in the edge v-w (q to quit): ')
        if next_value == 'q':
            break
        w = int(next_value)
        g.add_edge(v, w)

    print(f"g ({id(g)}): {g}")


if __name__ == '__main__':
    main()
