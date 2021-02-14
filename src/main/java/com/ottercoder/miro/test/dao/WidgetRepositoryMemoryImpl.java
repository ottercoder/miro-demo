package com.ottercoder.miro.test.dao;

import com.ottercoder.miro.test.dto.Coordinates;
import com.ottercoder.miro.test.dto.Widget;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class WidgetRepositoryMemoryImpl implements WidgetRepository {

    private final HashMap<UUID, Widget> widgetsRepository = new HashMap<>();
    private final TreeMap<Integer, Widget> zIndexWidgetsRepository = new TreeMap<>();

    @Override
    public synchronized Widget saveWidget(Widget widget) {
        widgetsRepository.put(widget.getId(), widget);
        zIndexWidgetsRepository.put(widget.getZ(), widget);
        return new Widget(widget);
    }

    @Override
    public synchronized Widget updateWidget(UUID id, Widget widget) {
        widget.setId(id);
        Widget existentWidget = getWidget(id);
        widgetsRepository.put(id, widget);
        zIndexWidgetsRepository.remove(existentWidget.getZ());
        zIndexWidgetsRepository.put(widget.getZ(), widget);
        return new Widget(widget);
    }

    @Override
    public synchronized void deleteWidget(UUID id) {
        Widget existentWidget = getWidget(id);
        widgetsRepository.remove(id);
        zIndexWidgetsRepository.remove(existentWidget.getZ());
    }

    @Override
    public Widget getWidget(UUID id) {
        Widget widget = widgetsRepository.get(id);
        return new Widget(widget);
    }

    @Override
    public int getHighestZIndex() {
        try {
            return zIndexWidgetsRepository.lastKey();
        } catch (NoSuchElementException e) {
            return Integer.MIN_VALUE;
        }
    }

    @Override
    public boolean zIndexExists(int z) {
        return zIndexWidgetsRepository.containsKey(z);
    }

    @Override
    public Map<Integer, Widget> getZIndexMap() {
        return zIndexWidgetsRepository;
    }

    private List<Widget> getWidgetsList() {
        return zIndexWidgetsRepository.values()
            .stream()
            .map(Widget::new)
            .collect(Collectors.toList());
    }

    @Override
    public List<Widget> getWidgetsPaginated(int page, int size) {
        List<Widget> widgets = getWidgetsList();

        return getPageOfList(page, size, widgets);
    }

    private <T> List<T> getPageOfList(int page, int size, List<T> list) {
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

    @Override
    public List<Widget> getWidgetsPaginatedByArea(int page, int size, Coordinates downLeft, Coordinates topRight) {
        List<Widget> widgetsByArea = getWidgetsList().stream()
            .filter(widget ->
                (widget.getX() - widget.getWidth()) >= downLeft.getX()
                    && (widget.getY() - widget.getHeight()) >= downLeft.getY()
                    && (widget.getX() + widget.getWidth()) <= topRight.getX()
                    && (widget.getY() + widget.getHeight()) <= topRight.getY())
            .collect(Collectors.toList());
        return getPageOfList(page, size, widgetsByArea);
    }

}
