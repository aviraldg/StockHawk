package com.sam_chordas.android.stockhawk.data;

import java.util.List;

/**
 * Created by aviraldg on 26/6/16.
 */

public class YQLResult {
    public static class YQLQuote {
        public List<Quote> quote;
    }
    public YQLQuote results;
}
