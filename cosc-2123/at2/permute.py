from math import floor, log


def permute(n):
    tree_height = floor(log(n))

    if n == 0:
        return 0

    if n == 1:
        return 1


def tree_height(n):
    return floor(log(n))


if __name__ == "__main__":
    for i in range(1, 10):
        print(tree_height(i), i)
