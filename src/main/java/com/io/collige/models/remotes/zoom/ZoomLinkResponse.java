/**
 * @author : Oguz Kahraman
 * @since : 31 Eki 2021
 * <p>
 * Copyright - fastmeet
 **/
package com.io.collige.models.remotes.zoom;

import lombok.Data;

@Data
public class ZoomLinkResponse {

    private long id;
    private String join_url;

}
