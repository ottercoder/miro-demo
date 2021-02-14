package com.ottercoder.miro.test.service;

import com.ottercoder.miro.test.dao.WidgetRepository;
import com.ottercoder.miro.test.dto.Coordinates;
import com.ottercoder.miro.test.dto.Widget;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class WidgetServiceImpl implements WidgetService {

    static final int Z_INCREMENT = 10000; //might be much higher, depending on expected number of widgets

    private final WidgetRepository widgetRepository;

    @Override
    public synchronized Widget createWidget(Widget widget) {
        widget.setId(UUID.randomUUID());

        if (widget.getZ() == null) {
            int highestZIndex = widgetRepository.getHighestZIndex();
            widget.setZ(highestZIndex + Z_INCREMENT);
        }

        if (widgetRepository.zIndexExists(widget.getZ())) {
            Map<Integer, Widget> zIndexMap = widgetRepository.getZIndexMap();
            Widget[] widgets = zIndexMap.entrySet().stream()
                .filter(e -> e.getKey() >= widget.getZ())
                .map(Entry::getValue)
                .map(Widget::new)
                .toArray(Widget[]::new);
            moveExistingWidgetsHigherSmart(widgets);
        }

        return widgetRepository.saveWidget(widget);
    }

    @Override
    public Widget updateWidget(UUID id, Widget widget) {
        return widgetRepository.updateWidget(id, widget);
    }

    @Override
    public void deleteWidget(UUID id) {
        widgetRepository.deleteWidget(id);
    }

    @Override
    public Widget getWidget(UUID id) {
        return widgetRepository.getWidget(id);
    }

    @Override
    public List<Widget> getWidgetsPaginated(int page, int size) {
        return widgetRepository.getWidgetsPaginated(page, size);
    }

    @Override
    public List<Widget> getWidgetsPaginatedByArea(int page, int size, Coordinates downLeft, Coordinates topRight) {
        return widgetRepository.getWidgetsPaginatedByArea(page, size, downLeft, topRight);
    }

    private Widget[] moveExistingWidgetsHigherSmart(Widget[] widgets) {
        log.debug("Moving existing widgets. WidgetsList: {}", Arrays.stream(widgets).toArray());
        if (widgets.length == 1) {
            log.debug("Widgets size just 1, so move up to increment: {}", Z_INCREMENT);
            widgets[0].setZ(widgets[0].getZ() + Z_INCREMENT);
            widgetRepository.updateWidget(widgets[0].getId(), widgets[0]);
            log.debug("New Widgets: {}", Arrays.stream(widgets).toArray());
            return widgets;
        }
        if ((widgets[1].getZ() - widgets[0].getZ()) < Z_INCREMENT / 500) {
            log.debug("Space between widget 1: {} and widget 2: {} is too little. Should be more than {}",
                widgets[0].getZ(), widgets[1].getZ(), Z_INCREMENT / 500);

            Widget[] subArray = Arrays.copyOfRange(widgets, 1, widgets.length);
            log.debug("Going to move next sub-array: {}", Arrays.stream(subArray).toArray());
            Widget[] newWidgets = moveExistingWidgetsHigherSmart(subArray);

            Widget[] newSubArray = merge(widgets[0], newWidgets);
            log.debug("New sub-array: {}", Arrays.stream(newSubArray).toArray());
            widgets = newSubArray;
        }
        int middle = widgets[0].getZ() + (widgets[1].getZ() - widgets[0].getZ()) / 2;
        log.debug("New place in between widgets is {}", middle);

        widgets[0].setZ(middle);
        widgetRepository.updateWidget(widgets[0].getId(), widgets[0]);

        return widgets;
    }

    private Widget[] merge(Widget widget, Widget[] newWidgets) {
        int length = newWidgets.length + 1;
        Widget[] mergedArray = new Widget[length];
        int position = 1;
        mergedArray[0] = widget;
        for (Widget element : newWidgets) {
            mergedArray[position] = element;
            position++;
        }

        return mergedArray;
    }
}
