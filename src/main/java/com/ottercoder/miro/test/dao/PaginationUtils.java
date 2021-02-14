package com.ottercoder.miro.test.dao;

import java.util.Collections;
import java.util.List;

public class PaginationUtils {

    private PaginationUtils() {
    }

    static <T> List<T> getPageOfList(int page, int size, List<T> list) {
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        if (size <= 0 || size > list.size()) {
            size = list.size();
        }
        int numPages = (int) Math.ceil((double) list.size() / (double) size);
        if (numPages < page) {
            return Collections.emptyList();
        }
        for (int pageNum = 0; pageNum < numPages; pageNum++) {
            if (pageNum == page) {
                return list.subList(pageNum * size, Math.min((pageNum + 1) * size, list.size()));
            }
        }
        return Collections.emptyList();
    }

}
