def selection_sort(array):
    array = list(array)

    for i in range(len(array)):
        smallest_index = i

        for j in range(i + 1, len(array)):

            if array[j] < array[smallest_index]:
                smallest_index = j

        array[i], array[smallest_index] = array[smallest_index], array[i]

    return array


def bubble_sort(array):
    array = list(array)

    for i in range(len(array)):
        sorted = True

        for j in range(len(array) - i - 1):
            if array[j] > array[j + 1]:
                array[j + 1], array[j] = array[j], array[j + 1]
                sorted = False

        if sorted:
            break

    return array


word = "supercalifragilisticexpealidocious"
print("selection sort: ", selection_sort(word))
print("bubble sort: ", bubble_sort(word))
