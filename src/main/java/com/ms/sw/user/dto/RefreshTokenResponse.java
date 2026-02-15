package com.ms.sw.user.dto;

import com.dev.tools.Markers.DtoMarker;

@DtoMarker
public record RefreshTokenResponse(String accessToken) {}
