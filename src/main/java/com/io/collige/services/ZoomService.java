/**
 * @author : Oguz Kahraman
 * @since : 31 Eki 2021
 * <p>
 * Copyright - fastmeet
 **/
package com.io.collige.services;

import com.io.collige.models.remotes.google.TokenRefreshResponse;
import com.io.collige.models.remotes.zoom.ZoomLinkCreateRequest;
import com.io.collige.models.remotes.zoom.ZoomLinkResponse;

public interface ZoomService {
    ZoomLinkResponse createZoomLink(ZoomLinkCreateRequest request, String accessToken);

    TokenRefreshResponse getNewAccessToken(String refreshToken);
}
