package com.puresoltechnologies.parsers.parser.packrat;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is a packrat parser memo for result memoization.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class PackratMemo {

    /**
     * This is the memoization buffer to put all memoized data in. The buffer
     * stores a MemoEntry for each production on a given position.
     */
    private final Map<Integer, Map<String, MemoEntry>> memo = new HashMap<Integer, Map<String, MemoEntry>>();

    PackratMemo() {
    }

    void clear() {
	memo.clear();
    }

    /**
     * This method returns the current memoization element from the buffer. This
     * element is unprocessed. If the element is not set (null) a NONE element
     * is put into the buffer and returned.
     * 
     * @param production
     * @param position
     * @return
     */
    MemoEntry getMemo(String production, int position) {
	Map<String, MemoEntry> map = memo.get(position);
	if (map == null)
	    return null;
	MemoEntry memo = map.get(production);
	if (memo == null)
	    return null;
	return memo;
    }

    /**
     * This method puts memozation elements into the buffer. It is designed in a
     * way, that entries, once set, are not changed anymore. This is needed not
     * to break references!
     * 
     * @param production
     * @param position
     * @param stackElement
     */
    void setMemo(String production, int position, int line,
	    final MemoEntry stackElement) {
	Map<String, MemoEntry> map = memo.get(position);
	if (map == null) {
	    map = new HashMap<String, MemoEntry>();
	    memo.put(position, map);
	    map.put(production, stackElement);
	} else {
	    if (map.containsKey(production)) {
		throw new RuntimeException(
			"We should not set a memo twice. Modifying is needed afterwards.");
	    }
	    map.put(production, stackElement);
	}
    }

}
