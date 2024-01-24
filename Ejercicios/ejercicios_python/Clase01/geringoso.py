

class GeringosoConverter:
    def __init__(self):
        self.dic = {}

    def convert_to_geringoso(self, word):
        converted_word = ""
        for char in word:
            if char in "aeiou":
                converted_word += char + "p" + char
            else:
                converted_word += char
        return converted_word.strip()

    def process_list(self, word_list):
        try:
            for gerin in word_list:
                converted_word = self.convert_to_geringoso(gerin)
                self.dic[gerin] = converted_word
            return self.dic
        except:
            print("No admite alfanumericos")

# Create an instance of the GeringosoConverter
geringoso_instance = GeringosoConverter()

# Use the instance to process a list of words
result = geringoso_instance.process_list(['pera', 'mandarina', 'naranja'])

# Display the result
print(result)
