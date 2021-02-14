package com.ottercoder.miro.test.dao;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.ottercoder.miro.test.dto.Widget;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WidgetRepositoryMemoryImplTest {

    private final WidgetRepositoryMemoryImpl widgetRepositoryMemory = new WidgetRepositoryMemoryImpl();
    private final Widget testWidget = new Widget(UUID.randomUUID(), 0, 0, 0, 100, 100);

    @BeforeEach
    void setUp() {
        widgetRepositoryMemory.saveWidget(testWidget);
    }

    @Test
    void saveWidget() {
        List<Widget> widgets = widgetRepositoryMemory.getWidgets();

        assertThat(widgets.size()).isEqualTo(1);
        assertThat(widgets.get(0)).isEqualTo(testWidget);
    }

    @Test
    void updateWidget() {
        Widget updatedWidget = new Widget(testWidget);
        updatedWidget.setZ(100);
        widgetRepositoryMemory.updateWidget(testWidget.getId(), updatedWidget);

        Widget widget = widgetRepositoryMemory.getWidget(testWidget.getId());
        assertThat(widget.getZ()).isEqualTo(updatedWidget.getZ());
        assertThat(widget.getId()).isEqualTo(updatedWidget.getId());

    }

    @Test
    void deleteWidget() {
        List<Widget> widgets = widgetRepositoryMemory.getWidgets();
        assertThat(widgets.size()).isEqualTo(1);

        widgetRepositoryMemory.deleteWidget(testWidget.getId());
        List<Widget> newWidgetsList = widgetRepositoryMemory.getWidgets();
        assertThat(newWidgetsList.isEmpty()).isTrue();
    }

    @Test
    void getWidget() {
        Widget widget = widgetRepositoryMemory.getWidget(testWidget.getId());
        assertThat(widget).isEqualTo(testWidget);
    }

    @Test
    void getWidgets() {
        Random random = new Random();
        int min = 3;
        int max = 10;
        int numberOfWidgets = random.nextInt(max - min + 1) + min;
        for (int i = 0; i < numberOfWidgets; i++) {
            widgetRepositoryMemory.saveWidget(new Widget(UUID.randomUUID(), 0, 0, i+100, 100, 100));
        }

        List<Widget> newWidgetsList = widgetRepositoryMemory.getWidgets();
        assertThat(newWidgetsList.size()).isEqualTo(numberOfWidgets+1);
    }
}