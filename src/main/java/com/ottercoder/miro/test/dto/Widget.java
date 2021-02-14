package com.ottercoder.miro.test.dto;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Widget {

    @Id
    private UUID id;
    @NotNull
    private Integer x;
    @NotNull
    private Integer y;
    private Integer z;
    @Min(value = 1, message = "Width and height are integers > 0")
    @NotNull
    private Integer width;
    @Min(value = 1, message = "Width and height are integers > 0")
    @NotNull
    private Integer height;

    public Widget(Widget widget) {
        this.id = UUID.fromString(widget.getId().toString());
        this.x = widget.getX();
        this.y = widget.getY();
        this.z = widget.getZ();
        this.width = widget.getWidth();
        this.height = widget.getHeight();
    }

}
