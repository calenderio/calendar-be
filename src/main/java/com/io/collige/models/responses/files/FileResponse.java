/**
 * @author : Oguz Kahraman
 * @since : 10/8/2021
 * <p>
 * Copyright - fastmeet
 **/
package com.io.collige.models.responses.files;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FileResponse {

    @Schema(description = "Id of file", example = "1")
    private Long id;

    @Schema(description = "Name of file", example = "Example.txt")
    private String name;

    @Schema(description = "Type of file", example = "application/pdf")
    private String type;

    @Schema(description = "File link", example = "link.co/example")
    private String link;

    @Schema(description = "File size", example = "123456")
    private Long size;

}
