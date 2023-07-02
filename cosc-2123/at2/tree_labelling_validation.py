##################################################################################
# This file is a part of Assignment 2 for the course Algorithms & Analysis at RMIT
# Author: Son Hoang Dau, RMIT 2022
##################################################################################

import math
import time


def parent_of(i):
    """
    return the parent index of i
    @param i: the current index
    @return: the parent index of i
    """
    if i <= 0:
        return None
    return math.floor((i - 1) / 2)


def right_child_of(i, n):
    """
    returns the index of the right child of i
    @param i: current node (index)
    @param n: number of tree nodes
    @return: the index of the right child of i, None if i has no right child
    """
    right_child = 2 * i + 2
    if right_child < n:
        return right_child
    else:
        return None


def left_child_of(i, n):
    """
    returns the index of the left child of i
    @param i: current node (index)
    @param n: number of tree nodes
    @return: the index of the left child of i, None if i has no left child
    """
    left_child = 2 * i + 1
    if left_child < n:
        return left_child
    else:
        return None


def is_magical(n, p):
    """
    Check if a permutation p of the n-node complete binary tree is magical
    @param n: number of tree nodes
    @param p: a permutation
    @return: True if the permutation is magical
    """
    if len(p) != n:
        print("ERROR: Length of p is different from n")
        return False
    for val in p:
        if val < 0 or val > n - 1:
            print("ERROR: Entries of p are not in the range [0,n-1]")
            return False
    q = set(p)
    if len(q) < len(p):
        print("ERROR: Duplicated elements")
        return False

    last_parent_index = math.floor(n / 2) - 1
    diffs = (
        set()
    )  # set of edge labels, or differences between the parent labels and their child labels
    for i in range(last_parent_index + 1):
        parent = p[i]
        left_child_index = left_child_of(i, n)
        if left_child_index is not None:
            left_child = p[left_child_index]
            diff_left = abs(
                parent - left_child
            )  # label of the edge (parent, left_child)
            if diff_left in diffs:  # repeated edge labels: invalid permutation
                return False
            diffs.add(diff_left)  # otherwise, add the label to the set of edge labels
        right_child_index = right_child_of(i, n)
        if right_child_index is not None:
            right_child = p[right_child_index]
            diff_right = abs(parent - right_child)
            if diff_right in diffs:
                return False
            diffs.add(diff_right)

    return True


def read_and_test_permutations_from_file(file_name, n_max):
    """
    Test all the permutations from files with n ranging from 1 to n_max
    Example of such a file
    ----------
    0
    0 1
    0 2 1
    1 3 2 0
    ----------
    @param file_name: name of the input file
    @param n_max: maximum n to be tested
    @return: True if all n_max permutations are magical
    """
    n_values = set()
    input_file = open(file_name, "r")
    line = input_file.readline()
    while line != "":
        p_string = line.split()
        p = [int(pi) for pi in p_string]
        n = len(p)
        if is_magical(n, p) is False:
            input_file.close()
            print("p = ", p, " is NOT a magical permutation")
            return False
        n_values.add(n)
        line = input_file.readline()
    input_file.close()

    if set(range(1, n_max + 1)).issubset(n_values) is False:
        print("Not all n are covered")
        return False

    return True


def main():
    file_name = "permutations.txt"
    n_max = 4
    if read_and_test_permutations_from_file(file_name, n_max):
        print("PASSED. CONGRATULATIONS!!!")
    else:
        print("FAILED!")


if __name__ == "__main__":
    main()
