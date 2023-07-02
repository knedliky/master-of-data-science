from dictionary.base_dictionary import BaseDictionary
from dictionary.node import Node
from dictionary.word_frequency import WordFrequency

# ------------------------------------------------------------------------
# This class is required to be implemented. Ternary Search Tree implementation.
#
# __author__ = 'Son Hoang Dau'
# __copyright__ = 'Copyright 2022, RMIT University'
# ------------------------------------------------------------------------


class TernarySearchTreeDictionary(BaseDictionary):
    def __init__(self):
        self.root = None

    def build_dictionary(self, words_frequencies: [WordFrequency]):
        """
        construct the data structure to store nodes
        @param words_frequencies: list of (word, frequency) to be stored
        """
        for wf in words_frequencies:
            self.add_word_frequency(wf)

    def search_node(self, word: str, node) -> Node:
        if node is None:
            return

        if word[0] < node.letter:
            return self.search_node(word, node.left)

        elif word[0] > node.letter:
            return self.search_node(word, node.right)

        elif word[0] == node.letter:
            if len(word) == 1:
                return node

            return self.search_node(word[1:], node.middle)

    def search(self, word: str) -> int:
        """
        search for a word
        @param word: the word to be searched
        @return: frequency > 0 if found and 0 if NOT found
        """
        if not word:
            return 0

        node = self.search_node(word, self.root)

        if node is None:
            return 0

        if not node.end_word:
            return 0

        return node.frequency

    def insert_node(self, word, node):
        if word[0] == node.letter:
            if len(word) == 1:
                return node

            if node.middle is None:
                node.middle = Node(word[1])

            return self.insert_node(word[1:], node.middle)

        if word[0] < node.letter:
            if node.left is None:
                node.left = Node(word[0])

            return self.insert_node(word, node.left)

        elif word[0] > node.letter:
            if node.right is None:
                node.right = Node(word[0])

            return self.insert_node(word, node.right)

    def add_word_frequency(self, word_frequency: WordFrequency) -> bool:
        """
        add a word and its frequency to the dictionary
        @param word_frequency: (word, frequency) to be added
        :return: True whether succeeded, False when word is already in the dictionary
        """
        word = word_frequency.word
        frequency = word_frequency.frequency

        if not word:
            return False

        if self.root is None:
            self.root = Node(word[0])

        node = self.insert_node(word, self.root)

        if node.end_word:
            return False

        node.end_word = True
        node.frequency = frequency
        return True

    # def inorder(self, root, node):
    #     if root:
    #         self.inorder(root.right, node)
    #         self.inorder(root.left, node)
    #         if node.letter in (root.right, root.left):
    #             print(root.letter)
    #         self.inorder(root.middle, node)

    def delete_word(self, word: str) -> bool:
        if not word:
            return False

        if self.root is None:
            return False

        node = self.search_node(word, self.root)
        # node = self.inorder(self.root, node)

        if node is None or not node.end_word:
            return False

        node.end_word = False
        node.frequency = None
        return True

    def preorder(self, node, word):
        if node is None:
            return

        if node.end_word:
            yield (word + node.letter, node.frequency)

        yield from self.preorder(node.left, word)
        yield from self.preorder(node.right, word)
        yield from self.preorder(node.middle, word + node.letter)

    def autocomplete(self, prefix_word: str) -> [WordFrequency]:
        prefix_words = []

        if self.root is None:
            return prefix_words

        node = self.search_node(prefix_word, self.root)

        if node is None:
            return prefix_words

        prefix_words = [
            WordFrequency(word, freq)
            for word, freq in self.preorder(node.middle, prefix_word)
        ]

        if node.end_word:
            prefix_words.append(WordFrequency(prefix_word, node.frequency))

        return sorted(prefix_words, key=lambda item: item.frequency, reverse=True)[0:3]
