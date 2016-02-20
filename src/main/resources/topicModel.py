from gensim import corpora, models, similarities
import json
from nltk.corpus import stopwords
from nltk.tokenize import RegexpTokenizer
from tokenizer import GeneratingTokenizer
import operator

filename = "/home/martin/marty_projects/git insights/github.insights/src/main/resources/comments.json"

stop = set(stopwords.words('english')).union(set(["trunk","s","t"]))

tokenizer = GeneratingTokenizer(stop)
dictionary = corpora.Dictionary(tokenizer.tokenize(json.loads(line)['fullMessage'].lower()) for line in open(filename))
stop_ids = [dictionary.token2id[stopword] for stopword in stop if stopword in dictionary.token2id]
dictionary.filter_tokens(stop_ids)
dictionary.compactify()

corpus = [dictionary.doc2bow(tokenizer.tokenize(json.loads(line)['fullMessage'].lower())) for line in open(filename)]

tfidf = models.TfidfModel(corpus)
corpus_tfidf = tfidf[corpus]
lsi = models.LsiModel(corpus_tfidf, id2word=dictionary, num_topics=200) # initialize an LSI transformation
corpus_lsi = lsi[corpus_tfidf]

t = lsi.print_topics(200)
out = {}
out['topics'] = t
out['docs'] = []
import pprint

for line in open(filename):
	document = json.loads(line)
	msg = tokenizer.tokenize(document['fullMessage'].lower())
	topTopics = sorted(lsi[tfidf[dictionary.doc2bow(msg)]], key=operator.itemgetter(1))[-3:]
	print json.dumps([lsi.print_topic(topic[0]) for topic in topTopics])


