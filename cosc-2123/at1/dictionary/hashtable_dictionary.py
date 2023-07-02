from dictionary.base_dictionary import BaseDictionary
from dictionary.word_frequency import WordFrequency

# ------------------------------------------------------------------------
# This class is required TO BE IMPLEMENTED. Hash-table-based dictionary.
#
# __author__ = 'Son Hoang Dau'
# __copyright__ = 'Copyright 2022, RMIT University'
# ------------------------------------------------------------------------


class HashTableDictionary(BaseDictionary):

    def __init__(self):
        self.dictionary = {}

    def build_dictionary(self, words_frequencies: [WordFrequency]):
        """
        construct the data structure to store nodes
        @param words_frequencies: list of (word, frequency) to be stored
        """

        for wf in words_frequencies:
            self.dictionary[wf.word] = wf.frequency

    def search(self, word: str) -> int:
        """
        search for a word
        @param word: the word to be searched
        @return: frequency > 0 if found and 0 if NOT found
        """
        if not self.dictionary.get(word):
            return 0

        return self.dictionary.get(word)

    def add_word_frequency(self, word_frequency: WordFrequency) -> bool:
        """
        add a word and its frequency to the dictionary
        @param word_frequency: (word, frequency) to be added
        :return: True whether succeeded, False when word is already in the dictionary
        """
        wf = word_frequency

        if not self.dictionary.get(wf.word):
            self.dictionary[wf.word] = wf.frequency
            return True

        return False

    def delete_word(self, word: str) -> bool:
        """
        delete a word from the dictionary
        @param word: word to be deleted
        @return: whether succeeded, e.g. return False when point not found
        """
        if self.dictionary.get(word):
            self.dictionary.pop(word)
            return True

        return False

    def autocomplete(self, word: str) -> [WordFrequency]:
        """
        return a list of 3 most-frequent words in the dictionary that have 'word' as a prefix
        @param word: word to be autocompleted
        @return: a WordFrequency list (could be empty) of (at most) 3 most-frequent words with prefix 'word'
        """
        prefix_words = [
            WordFrequency(k, v)
            for (k, v) in self.dictionary.items()
            if k.startswith(word)
        ]

        return sorted(prefix_words, key=lambda wf: wf.frequency, reverse=True)[0:3]
