package org.nlp.github.insights.db;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

public class Analyzer extends org.apache.lucene.analysis.Analyzer {


    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        Tokenizer source = new StandardTokenizer();
        TokenStream filter = new WordDelimiterFilter(source,
                    WordDelimiterFilter.SPLIT_ON_CASE_CHANGE |
                    WordDelimiterFilter.GENERATE_NUMBER_PARTS |
                    WordDelimiterFilter.GENERATE_WORD_PARTS |
                    WordDelimiterFilter.PRESERVE_ORIGINAL
                    , null);
        return new TokenStreamComponents(source, filter);
    }


}
