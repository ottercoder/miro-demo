package com.ottercoder.miro.test.dao;

import com.ottercoder.miro.test.dto.Widget;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface WidgetRepository {

    Widget saveWidget(Widget widget);

    Widget updateWidget(UUID id, Widget widget);

    void deleteWidget(UUID id);

    Widget getWidget(UUID id);

    List<Widget> getWidgets();

    int getHighestZIndex();

    boolean zIndexExists(int z);

    Map<Integer, Widget> getZIndexMap();
}
