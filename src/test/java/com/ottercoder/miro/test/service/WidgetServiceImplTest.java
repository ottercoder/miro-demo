package com.ottercoder.miro.test.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ottercoder.miro.test.dao.WidgetRepository;
import com.ottercoder.miro.test.dto.Widget;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WidgetServiceImplTest {

    private final Widget testWidget = new Widget(UUID.randomUUID(), 0, 0, 0, 100, 100);

    private final WidgetRepository widgetRepository = Mockito.mock(WidgetRepository.class);
    private final WidgetServiceImpl widgetService = new WidgetServiceImpl(widgetRepository);

    @Test
    void createWidget() {
        when(widgetRepository.zIndexExists(anyInt())).thenReturn(Boolean.FALSE);
        when(widgetRepository.saveWidget(any())).thenReturn(testWidget);

        Widget widget = widgetService.createWidget(testWidget);
        verify(widgetRepository, times(1)).saveWidget(testWidget);
        assertThat(widget).isEqualTo(testWidget);
    }

    @Test
    void createWidget_Z_is_null() {
        final int highestZ = 0;
        when(widgetRepository.getHighestZIndex()).thenReturn(highestZ);
        when(widgetRepository.zIndexExists(anyInt())).thenReturn(false);
        when(widgetRepository.saveWidget(any())).thenReturn(testWidget);

        final Widget widgetZIsNull = new Widget(null, 0, 0, null, 100, 100);

        Widget widget = widgetService.createWidget(widgetZIsNull);
        assertThat(widget).isEqualTo(testWidget);

        verify(widgetRepository, times(1)).getHighestZIndex();
        widgetZIsNull.setZ(highestZ+WidgetServiceImpl.Z_INCREMENT);
        verify(widgetRepository, times(1)).saveWidget(widgetZIsNull);
    }

    @Test
    void updateWidget() {
        when(widgetRepository.updateWidget(any(), any())).thenReturn(testWidget);
        widgetService.updateWidget(testWidget.getId(), testWidget);
        verify(widgetRepository, times(1)).updateWidget(testWidget.getId(), testWidget);
    }

    @Test
    void deleteWidget() {
        doNothing().when(widgetRepository).deleteWidget(any());
        UUID uuid = UUID.randomUUID();
        widgetService.deleteWidget(uuid);
        verify(widgetRepository, times(1)).deleteWidget(uuid);
    }

    @Test
    void getWidget() {
        when(widgetRepository.getWidget(any())).thenReturn(testWidget);
        Widget widget = widgetService.getWidget(UUID.randomUUID());
        assertThat(widget).isEqualTo(testWidget);
    }

    @Test
    void getWidgets() {
        when(widgetRepository.getWidgets()).thenReturn(Collections.singletonList(testWidget));
        List<Widget> widgets = widgetService.getWidgets();
        assertThat(widgets.size()).isEqualTo(1);
    }
}