package com.ottercoder.miro.test.dao;

import com.ottercoder.miro.test.dto.Widget;
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
    public List<Widget> getWidgets() {
        return zIndexWidgetsRepository.values()
            .stream()
            .map(Widget::new)
            .collect(Collectors.toList());
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

}
