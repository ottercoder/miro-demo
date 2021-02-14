package com.ottercoder.miro.test.controller;

import com.ottercoder.miro.test.dto.Widget;
import com.ottercoder.miro.test.service.WidgetService;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("widgets")
@RequiredArgsConstructor
public class WidgetController {

    private final WidgetService widgetService;

    @PostMapping
    public Widget createWidget(@Valid @RequestBody Widget widget) {
        return widgetService.createWidget(widget);
    }

    @PatchMapping("{id}")
    public Widget updateWidget(@PathVariable UUID id, @Valid @RequestBody Widget widget) {
        return widgetService.updateWidget(id, widget);
    }

    @DeleteMapping("{id}")
    public void deleteWidget(@PathVariable UUID id) {
        widgetService.deleteWidget(id);
    }

    @GetMapping("{id}")
    public Widget getWidget(@PathVariable UUID id) {
        return widgetService.getWidget(id);
    }

    @GetMapping()
    public List<Widget> getWidgets() {
        return widgetService.getWidgets();
    }

}
