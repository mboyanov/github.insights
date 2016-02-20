from nltk.tokenize.api import TokenizerI
from nltk.tokenize import word_tokenize
import re
class GeneratingTokenizer(TokenizerI):



	def __init__(self, stopwords=[]):
		self.stopwords = stopwords
		self.scanner = re.Scanner([
			(ur"[0-9]+",   	lambda scanner,token:("INTEGER", token)),
			(ur"https?\:\/\/[^\s.]+\.[^\s]+", lambda scanner, token:("URL",token)),
			(ur"([^@\s]+)@([^.\s]+)\.[^\s]+", lambda scanner, token:("EMAIL",token)),
			(ur"[a-z_-]+",  	lambda scanner,token:("IDENTIFIER", token)),
			(ur"[<>=+*~#&|^@%]+",  	lambda scanner,token:("OPERATOR", token)),
			(ur"[$,.!?()/\":\;\-\'\\\]\[`{}]+",    	lambda scanner,token:("PUNCTUATION", token)),
			(ur"[\s]+", None),
			(ur"[^\s]+", lambda scanner, token:("WTF", token))
		])
		self.processers = {}
		self.processers["URL"] = URLTokenizer()
		self.processers["EMAIL"] = EmailTokenizer()
		self.processers["PUNCTUATION"] = IgnoreTokenizer()
		self.processers["OPERATOR"] = IgnoreTokenizer()
		self.processers["INTEGER"] = IgnoreTokenizer()


	def tokenize(self, input):
		input = input.lower()
		tokens = []
		results, remainder=self.scanner.scan(input)
		for r in results:
			if r[0] in self.processers:
				tokens = tokens + self.processers[r[0]].tokenize(r[1])
			else:
				tokens = tokens + word_tokenize(r[1])
		if len(remainder) > 0:
			print remainder
			print '@@@'
		return tokens

class RegexTokenizer():

	def tokenize(self, input):
		res = [input]
		res = res + list(self.regex.match(input).groups())
		return res

class EmailTokenizer(RegexTokenizer):

	def __init__(self):
		self.regex = re.compile(ur"([^@\s]+)@([^.\s]+)\.")


class URLTokenizer(RegexTokenizer):

	def __init__(self):
		self.regex = re.compile(ur"https?:\/\/([^\/]+)\.[^\s]+")

class IgnoreTokenizer(RegexTokenizer):

	def tokenize(self,input):
		return []


et = EmailTokenizer()
tokens = et.tokenize("mboyanov@gmail.com")
assert("mboyanov" in tokens)
assert("gmail" in tokens)
assert("mboyanov@gmail.com" in tokens)

ut = URLTokenizer()
tokens  = ut.tokenize("http://google.com")
assert("google" in tokens)

gt = GeneratingTokenizer()
tokens = gt.tokenize("jane@example.com knows how to access http://google.com")
assert("jane" in tokens)
assert("google" in tokens)
assert("http://google.com" in tokens)



