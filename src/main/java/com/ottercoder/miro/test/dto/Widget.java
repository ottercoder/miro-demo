package com.ottercoder.miro.test.dto;

import java.util.UUID;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Widget {

    private UUID id;
    @NotNull
    private final Integer x;
    @NotNull
    private final Integer y;
    private Integer z;
    @Min(value = 1, message = "Width and height are integers > 0")
    @NotNull
    private final Integer width;
    @Min(value = 1, message = "Width and height are integers > 0")
    @NotNull
    private final Integer height;

    public Widget(Widget widget) {
        this.id = UUID.fromString(widget.getId().toString());
        this.x = widget.getX();
        this.y = widget.getY();
        this.z = widget.getZ();
        this.width = widget.getWidth();
        this.height = widget.getHeight();
    }

}
