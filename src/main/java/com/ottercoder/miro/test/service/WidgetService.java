package com.ottercoder.miro.test.service;

import com.ottercoder.miro.test.dto.Widget;
import java.util.List;
import java.util.UUID;

public interface WidgetService {

    Widget createWidget(Widget widget);

    Widget updateWidget(UUID id, Widget widget);

    void deleteWidget(UUID id);

    Widget getWidget(UUID id);

    List<Widget> getWidgets();

}
