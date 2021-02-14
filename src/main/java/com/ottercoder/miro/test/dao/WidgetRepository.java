package com.ottercoder.miro.test.dao;

import com.ottercoder.miro.test.dto.Coordinates;
import com.ottercoder.miro.test.dto.Widget;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

public interface WidgetRepository {

    Widget saveWidget(Widget widget);

    Widget updateWidget(UUID id, Widget widget);

    void deleteWidget(UUID id);

    Widget getWidget(UUID id);

    int getHighestZIndex();

    boolean zIndexExists(int z);

    TreeMap<Integer, Widget> getZIndexMap();

    List<Widget> getWidgetsPaginated(int page, int size);

    List<Widget> getWidgetsPaginatedByArea(int page, int size, Coordinates downLeft, Coordinates topRight);
}
